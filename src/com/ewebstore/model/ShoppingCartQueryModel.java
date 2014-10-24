package com.ewebstore.model;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.ewebstore.dbutil.DBConnection;
import com.ewebstore.dbutil.DBUtil;
import com.ewebstore.entity.CartItem;
import com.ewebstore.entity.ShoppingCart;

/**
 * The ShoppingCartQueryModel class handles the database operations related to
 * shopping carts and checkout.
 * 
 * @author ewebstore.org
 *
 */
public class ShoppingCartQueryModel {

	/**
	 * Checks if an order corresponding to the provided shopping cart is
	 * deliverable or not
	 * 
	 * @param cart
	 *            Shopping cart information
	 * @return true if the order is deliverable; false otherwise
	 */
	private static boolean isOrderDeliverable(ShoppingCart cart) {
		ArrayList<CartItem> cartItems = cart.getCartItems();

		for (CartItem cartItem : cartItems)
			if (!isItemDeliverable(cartItem))
				return false;

		return true;
	}

	/**
	 * Checks if an order item corresponding to the provided cart item is
	 * deliverable or not
	 * 
	 * @param cartItem
	 *            Shopping cart item information
	 * @return true if the order item is deliverable; false otherwise
	 */
	private static boolean isItemDeliverable(CartItem cartItem) {
		int availiableQuantity = getTotalAvailableProductQuantity(cartItem
				.getProductID());
		return availiableQuantity >= cartItem.getQuantity();
	}

	/**
	 * Returns the available quantity of the product corresponding to the
	 * provided product ID
	 * 
	 * @param productID
	 *            ID of the product
	 * @return Available quantity of the product
	 */
	private static int getTotalAvailableProductQuantity(String productID) {
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;

		try {
			preparedStatement = DBConnection
					.getConnection()
					.prepareStatement(
							"SELECT COALESCE(SUM(availableQuantity), 0) FROM BranchInventory WHERE productID = ?");

			preparedStatement.setLong(1, Long.valueOf(productID));

			resultSet = preparedStatement.executeQuery();

			if (!resultSet.next())
				return 0;

			return resultSet.getInt(1);
		} catch (SQLException ex) {
			return 0;
		} finally {
			DBUtil.dispose(resultSet);
			DBUtil.dispose(preparedStatement);
		}
	}

	/**
	 * Updates database by placing an order
	 * 
	 * @param cart
	 *            Shopping cart information
	 * @param deliveryLocation
	 *            Delivery location
	 * @param nearestDistrictID
	 *            ID of the district nearest to the delivery location
	 * @return true if the order could be placed; false otherwise
	 * @throws SQLException
	 *             if a database access error occurs
	 */
	public static boolean placeOrder(ShoppingCart cart,
			String deliveryLocation, String nearestDistrictID)
			throws SQLException {
		synchronized (cart) {
			synchronized (DBConnection.getConnection()) {
				if (!isOrderDeliverable(cart))
					return false;

				try {
					String branchID = getClosestBranchID(nearestDistrictID);

					String orderID = addOrderToOrderTable(cart,
							deliveryLocation, branchID);

					for (CartItem cartItem : cart.getCartItems())
						addOrderProduct(orderID, cartItem);

					BranchInventoryTransferModel
							.distributeOrderBetweenBranches(cart, branchID);
				} catch (SQLException ex) {
					ex.printStackTrace();
					throw ex;
				}

				return true;
			}
		}
	}

	/**
	 * Updates database by adding a cart item to the order corresponding to the
	 * provided order ID
	 * 
	 * @param orderID
	 *            ID of the order
	 * @param cartItem
	 *            Cart item information
	 * @throws SQLException
	 *             if a database access error occurs
	 */
	private static void addOrderProduct(String orderID, CartItem cartItem)
			throws SQLException {
		PreparedStatement preparedStatement = null;

		try {
			preparedStatement = DBConnection.getConnection().prepareStatement(
					"INSERT INTO OrderProducts VALUES(NULL, ?, ?, ?)");

			preparedStatement.setLong(1, Long.valueOf(orderID));
			preparedStatement.setLong(2, Long.valueOf(cartItem.getProductID()));
			preparedStatement.setLong(3,
					Integer.valueOf(cartItem.getQuantity()));

			if (preparedStatement.executeUpdate() != 1)
				throw new SQLException();
		} catch (SQLException ex) {
			throw ex;
		} finally {
			DBUtil.dispose(preparedStatement);
		}
	}

	/**
	 * Updates database by inserting an order
	 * 
	 * @param cart
	 *            Shopping cart information
	 * @param deliveryLocation
	 *            Delivery location
	 * @param branchID
	 *            ID of the branch
	 * @return ID of the new order
	 * @throws SQLException
	 *             if a database access error occurs
	 */
	private static String addOrderToOrderTable(ShoppingCart cart,
			String deliveryLocation, String branchID) throws SQLException {
		double totalCost = getTotalOrderingCost(cart);

		PreparedStatement preparedStatement = null;

		try {
			preparedStatement = DBConnection
					.getConnection()
					.prepareStatement(
							"INSERT INTO `Order` VALUES(NULL, ?, CURDATE(), ?, ?, ?, NULL, ?)");

			preparedStatement.setLong(1, Long.valueOf(cart.getCustomerID()));
			preparedStatement.setString(2, deliveryLocation);
			preparedStatement.setLong(3, Long.valueOf(OrderQueryModel
					.getOrderStatusIDByStatus("unhandled")));
			preparedStatement.setLong(4, Long.valueOf(branchID));
			preparedStatement.setDouble(5, totalCost);

			if (preparedStatement.executeUpdate() != 1)
				throw new SQLException();

			return OrderQueryModel.getLatestOrderID();
		} catch (SQLException ex) {
			throw ex;
		} finally {
			DBUtil.dispose(preparedStatement);
		}
	}

	/**
	 * Returns the cost of a cart item
	 * 
	 * @param cartItem
	 *            Cart item information
	 * @return Cost of the cart item
	 * @throws SQLException
	 *             if a database access error occurs
	 */
	public static double getCartItemCost(CartItem cartItem) throws SQLException {
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;

		try {
			preparedStatement = DBConnection.getConnection().prepareStatement(
					"SELECT price FROM Product WHERE productID = ?");

			preparedStatement.setLong(1, Long.valueOf(cartItem.getProductID()));

			resultSet = preparedStatement.executeQuery();

			if (!resultSet.next())
				throw new SQLException();

			return resultSet.getDouble(1) * cartItem.getQuantity();
		} catch (SQLException ex) {
			throw ex;
		} finally {
			DBUtil.dispose(resultSet);
			DBUtil.dispose(preparedStatement);
		}
	}

	/**
	 * Returns the cost of the total cart
	 * 
	 * @param cart
	 *            Shopping cart information
	 * @return Total cost of the cart
	 * @throws SQLException
	 *             if a database access error occurs
	 */
	public static double getCartSubTotalCost(ShoppingCart cart)
			throws SQLException {
		double cost = 0;

		for (CartItem cartItem : cart.getCartItems())
			cost += getCartItemCost(cartItem);

		return cost;
	}

	/**
	 * Returns the total cost of the order related to the shopping cart
	 * 
	 * @param cart
	 *            Shopping cart information
	 * @return Cost of the order
	 * @throws SQLException
	 *             if a database access error occurs
	 */
	public static double getTotalOrderingCost(ShoppingCart cart)
			throws SQLException {
		double cost = getCartSubTotalCost(cart);

		if (cost > 0) {
			cost += SharedData.getShippingCost();

			if (cart.getCustomerID() != null
					&& CustomerQueryModel.isPremiumUser(cart.getCustomerID()))
				cost = cost * (1 - CustomerQueryModel.DISCOUNTPERCENTAGE / 100);
		}

		return cost;
	}

	/**
	 * Returns the ID of the branch that is the closest to the district
	 * corresponding to the provided district ID
	 * 
	 * @param nearestDistrictID
	 *            ID of the district
	 * @return ID of the closest branch
	 * @throws SQLException
	 *             if a database access error occurs
	 */
	private static String getClosestBranchID(String nearestDistrictID)
			throws SQLException {
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;

		try {
			preparedStatement = DBConnection
					.getConnection()
					.prepareStatement(
							"SELECT branchID FROM Branch ORDER BY (SELECT distance FROM DistrictDistance WHERE DistrictDistance.district1ID = ? AND DistrictDistance.district2ID = Branch.branchDistrict) LIMIT 1");

			preparedStatement.setLong(1, Long.valueOf(nearestDistrictID));

			resultSet = preparedStatement.executeQuery();

			if (!resultSet.next())
				throw new SQLException();

			return Long.toString(resultSet.getLong(1));

		} catch (SQLException ex) {
			throw ex;
		} finally {
			DBUtil.dispose(resultSet);
			DBUtil.dispose(preparedStatement);
		}
	}
}
