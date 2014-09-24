package com.ewebstore.model;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.ewebstore.dbutil.DBConnection;
import com.ewebstore.dbutil.DBUtil;
import com.ewebstore.dbutil.DatabaseTransaction;
import com.ewebstore.entity.CartItem;
import com.ewebstore.entity.ShoppingCart;

public class ShoppingCartQueryModel {
	private static final double SHIPPINGCOST = 30.0; // in BDT

	private synchronized static boolean isOrderDeliverable(ShoppingCart cart) {
		ArrayList<CartItem> cartItems = cart.getCartItems();

		for (CartItem cartItem : cartItems)
			if (!isProductDeliverable(cartItem))
				return false;

		return true;
	}

	private synchronized static boolean isProductDeliverable(CartItem cartItem) {
		return getAvailableProductQuantity(cartItem.getProductID()) >= cartItem
				.getQuantity();
	}

	private synchronized static int getAvailableProductQuantity(String productID) {
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;

		try {
			preparedStatement = DBConnection
					.getSharedConnection()
					.prepareStatement(
							"SELECT SUM(availableQuantity) FROM BranchInventory WHERE productID = ?");

			preparedStatement.setLong(1, Long.valueOf(productID));

			resultSet = preparedStatement.executeQuery();

			return resultSet.getInt(1);
		} catch (SQLException ex) {
			return 0;
		} finally {
			DBUtil.dispose(resultSet);
			DBUtil.dispose(preparedStatement);
		}
	}

	public synchronized static void placeOrder(ShoppingCart cart,
			String deliveryLocation, String nearestDistrictID)
			throws SQLException {
		synchronized (cart) {
			if (!isOrderDeliverable(cart))
				throw new SQLException("Order can not be delivered");

			try {
				String branchID = getClosestBranchID(nearestDistrictID);
				DatabaseTransaction transaction = new DatabaseTransaction();
				addOrderToOrderTable(transaction, cart, deliveryLocation,
						branchID);
				BranchInventoryTransferModel.distributeOrderBetweenBranches(
						transaction, cart, branchID);
				transaction.commit();
			} catch (SQLException ex) {
				ex.printStackTrace();
				throw ex;
			}
		}
	}

	private static void addOrderToOrderTable(DatabaseTransaction transaction,
			ShoppingCart cart, String deliveryLocation, String branchID)
			throws SQLException {
		double totalCost = getTotalOrderingCost(cart);

		PreparedStatement preparedStatement = transaction
				.newPreparedStatement("INSERT INTO Order VALUES(NULL, ?, CURDATE(), ?, ?, ?, NULL, ?)");

		preparedStatement.setLong(1, Long.valueOf(cart.getCustomerID()));
		preparedStatement.setString(2, deliveryLocation);
		preparedStatement.setLong(3, Long.valueOf(OrderQueryModel
				.getOrderStatusIDByStatus("unhandled")));
		preparedStatement.setLong(4, Long.valueOf(branchID));
		preparedStatement.setDouble(5, totalCost);
	}

	private static double getTotalOrderingCost(ShoppingCart cart)
			throws SQLException {
		double cost = SHIPPINGCOST;

		for (CartItem cartItem : cart.getCartItems())
			cost += getCartItemCost(cartItem);

		if (CustomerQueryModel.isPremiumUser(cart.getCustomerID()))
			cost = cost * (1 - CustomerQueryModel.DISCOUNTPERCENTAGE / 100);

		return cost;
	}

	private static double getCartItemCost(CartItem cartItem)
			throws SQLException {
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;

		try {
			preparedStatement = DBConnection.getSharedConnection()
					.prepareStatement(
							"SELECT price FROM Product WHERE productID = ?");

			preparedStatement.setLong(1, Long.valueOf(cartItem.getProductID()));

			resultSet = preparedStatement.executeQuery();

			if (!resultSet.next())
				throw new SQLException();

			return resultSet.getDouble(1);
		} catch (SQLException ex) {
			throw ex;
		} finally {
			DBUtil.dispose(resultSet);
			DBUtil.dispose(preparedStatement);
		}
	}

	private static String getClosestBranchID(String nearestDistrictID)
			throws SQLException {
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;

		try {
			preparedStatement = DBConnection
					.getSharedConnection()
					.prepareStatement(
							"SELECT branchID FROM Branch ORDER BY (SELECT distance FROM DistrictDistance WHERE DistrictDistance.district1ID = ? AND DistrictDistance.district2ID = Branch.branchDistrict) DESC LIMIT 1");

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
