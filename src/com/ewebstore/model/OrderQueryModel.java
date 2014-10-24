package com.ewebstore.model;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import com.ewebstore.dbutil.DBConnection;
import com.ewebstore.dbutil.DBUtil;
import com.ewebstore.entity.BriefOrder;
import com.ewebstore.entity.Order;
import com.ewebstore.entity.OrderDisplayInformation;
import com.ewebstore.entity.OrderProduct;

/**
 * The OrderQueryModel class handles the database operations related to the
 * orders.
 * 
 * @author ewebstore.org
 *
 */
public class OrderQueryModel {

	// Note-
	// alloted for delivery from this branch := instock - available -
	// sum(quantity)_for_this_branch_in_BranchInventoryTransfer_table

	/**
	 * Returns a list of breif descriptions on the orders corresponding to the
	 * provided branch ID
	 * 
	 * @param branchID
	 *            ID of the branch
	 * @return
	 */
	public static ArrayList<BriefOrder> getOrdersToDispatch(String branchID) {

		ArrayList<String> collectiveOrderIDs = getToDispatchOrderIDs(branchID);
		ArrayList<BriefOrder> collectiveOrders = new ArrayList<BriefOrder>();

		for (String collectiveOrderID : collectiveOrderIDs) {
			BriefOrder newOrder = getBriefOrderByID(collectiveOrderID);

			if (newOrder != null)
				collectiveOrders.add(newOrder);
		}

		return collectiveOrders;
	}

	/**
	 * Returns a brief description on the order corresponding to the provided
	 * order ID
	 * 
	 * @param collectiveOrderID
	 *            ID of the order
	 * @return Brief description on the order
	 */
	private static BriefOrder getBriefOrderByID(String collectiveOrderID) {
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		BriefOrder order = null;

		try {
			preparedStatement = DBConnection
					.getConnection()
					.prepareStatement(
							"SELECT customerID, orderDate, detailedDeliveryLocation, associatedEmployee FROM `Order` WHERE orderID = ?");

			preparedStatement.setLong(1, Long.valueOf(collectiveOrderID));

			resultSet = preparedStatement.executeQuery();

			if (resultSet.next()) {
				String customerID = Long.toString(resultSet.getLong(1));
				Date orderDate = resultSet.getDate(2);
				String deliveryLocation = resultSet.getString(3);
				String associatedEmployeeID = Long.toString(resultSet
						.getLong(4));

				order = new BriefOrder(collectiveOrderID, customerID,
						orderDate, deliveryLocation, associatedEmployeeID);
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
		} finally {
			DBUtil.dispose(preparedStatement);
			DBUtil.dispose(resultSet);
		}

		return order;
	}

	/**
	 * Returns information on a order corresponding to the provided order ID
	 * 
	 * @param orderID
	 *            ID of the order
	 * @return Information on the order
	 */
	// returns null on invalid orderID
	public static Order getOrderByID(String orderID) {
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		Order order = null;

		try {
			preparedStatement = DBConnection
					.getConnection()
					.prepareStatement(
							"SELECT customerID, orderDate, detailedDeliveryLocation, orderStatusID, branchID, associatedEmployee, totalOrderingCost FROM `Order` WHERE orderID = ?");

			preparedStatement.setLong(1, Long.valueOf(orderID));

			resultSet = preparedStatement.executeQuery();

			if (resultSet.next()) {
				String customerID = Long.toString(resultSet.getLong(1));
				Date orderDate = resultSet.getDate(2);
				String deliveryLocation = resultSet.getString(3);
				String orderStatusID = Long.toString(resultSet.getLong(4));
				String orderBranchID = Long.toString(resultSet.getLong(5));
				String associatedEmployeeID = Long.toString(resultSet
						.getLong(6));

				if (associatedEmployeeID.equals("0"))
					associatedEmployeeID = null;

				double totalOrderingCost = resultSet.getDouble(7);

				String customerName = CustomerQueryModel
						.getCustomerName(customerID);

				String orderBranchName = BranchQueryModel
						.getBranchNameByID(orderBranchID);

				String associatedEmployeeName = null;

				if (associatedEmployeeID != null)
					associatedEmployeeName = SalesEmployeeQueryModel
							.getSalesEmployeeName(associatedEmployeeID);

				String orderStatus = getOrderStatusByID(orderStatusID);

				ArrayList<OrderProduct> orderProducts = getOrderProducts(orderID);

				order = new Order(orderID, customerID, customerName, orderDate,
						deliveryLocation, orderStatusID, orderStatus,
						orderBranchID, orderBranchName, associatedEmployeeID,
						associatedEmployeeName, totalOrderingCost,
						orderProducts);
			}
		} catch (SQLException | NumberFormatException | NullPointerException ex) {
			ex.printStackTrace();
		} finally {
			DBUtil.dispose(preparedStatement);
			DBUtil.dispose(resultSet);
		}

		return order;
	}

	/**
	 * Returns a list of products order information of a order corresponding to
	 * the provided order ID
	 * 
	 * @param orderID
	 *            ID of the order
	 * @return List of products order information
	 */
	private static ArrayList<OrderProduct> getOrderProducts(String orderID) {
		ArrayList<OrderProduct> orderProducts = new ArrayList<OrderProduct>();

		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;

		try {
			preparedStatement = DBConnection
					.getConnection()
					.prepareStatement(
							"SELECT productID, quantity FROM OrderProducts WHERE orderID = ?");

			preparedStatement.setLong(1, Long.valueOf(orderID));

			resultSet = preparedStatement.executeQuery();

			while (resultSet.next()) {
				String productID = Long.toString(resultSet.getLong(1));
				int orderQuantity = resultSet.getInt(2);
				String productName = ProductQueryModel
						.getProductName(productID);

				orderProducts.add(new OrderProduct(productID, productName,
						orderQuantity));
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
		} finally {
			DBUtil.dispose(preparedStatement);
			DBUtil.dispose(resultSet);
		}

		return orderProducts;
	}

	/**
	 * Returns a list of the order IDs of the orders that are to be dispatched
	 * from the branch corresponding to the provided branch ID
	 * 
	 * @param branchID
	 *            ID of the branch
	 * @return List of order IDs
	 */
	private static ArrayList<String> getToDispatchOrderIDs(String branchID) {
		ArrayList<String> orderIDs = new ArrayList<String>();

		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;

		try {
			preparedStatement = DBConnection
					.getConnection()
					.prepareStatement(
							"SELECT orderID FROM `Order` AS ORD1 WHERE orderStatusID = ? AND branchID = ? AND NOT EXISTS(SELECT orderProductsID FROM OrderProducts AS ORDP1 WHERE ORDP1.orderID = ORD1.orderID AND ORDP1.quantity > COALESCE((SELECT inStockQuantity - availableQuantity - (SELECT COALESCE(SUM(quantity), 0) FROM BranchInventoryTransfer AS BRIT1 WHERE fromBranchID = BRINV1.branchID AND BRIT1.productID = BRINV1.productID) FROM BranchInventory AS BRINV1 WHERE BRINV1.branchID = ORD1.branchID AND BRINV1.productID = ORDP1.productID), 0)) ORDER BY orderDate ASC");

			preparedStatement.setLong(1,
					Long.valueOf(getOrderStatusIDByStatus("Unhandled")));
			preparedStatement.setLong(2, Long.valueOf(branchID));

			resultSet = preparedStatement.executeQuery();

			while (resultSet.next())
				orderIDs.add(Long.toString(resultSet.getLong(1)));
		} catch (SQLException ex) {
			ex.printStackTrace();
		} finally {
			DBUtil.dispose(preparedStatement);
			DBUtil.dispose(resultSet);
		}

		return orderIDs;
	}

	/**
	 * Updates database by setting the status of a order corresponding to the
	 * provided order ID to the 'orderStatus'
	 * 
	 * @param orderID
	 *            ID of the order
	 * @param orderStatus
	 *            Status to be set
	 * @throws SQLException
	 *             if a database access error occurs
	 */
	private static void updateOrderStatus(String orderID, String orderStatus)
			throws SQLException {
		PreparedStatement preparedStatement = null;

		try {
			preparedStatement = DBConnection
					.getConnection()
					.prepareStatement(
							"UPDATE `Order` SET orderStatusID = (SELECT orderStatusID FROM OrderStatus WHERE status = ?) WHERE orderID = ?");

			preparedStatement.setString(1, orderStatus);
			preparedStatement.setLong(2, Long.valueOf(orderID));

			if (preparedStatement.executeUpdate() != 1)
				throw new SQLException();
		} catch (SQLException ex) {
			throw ex;
		} finally {
			DBUtil.dispose(preparedStatement);
		}
	}

	/**
	 * Returns a list of of the brief descriptions on the orders that are being
	 * delivered at the moment from the branch corresponding to the provided
	 * branch ID
	 * 
	 * @param branchID
	 *            ID of the branch
	 * @return List of brief descriptions on orders
	 */
	public static ArrayList<BriefOrder> getOnDeliveryOrders(String branchID) {
		return getBriefOrdersWithStatus(branchID, "Being Delivered");
	}

	/**
	 * Returns a list of of the brief descriptions on the orders that are
	 * delivered from the branch corresponding to the provided branch ID
	 * 
	 * @param branchID
	 *            ID of the branch
	 * @return List of brief descriptions on orders
	 */
	public static ArrayList<BriefOrder> getDeliveredOrders(String branchID) {
		return getBriefOrdersWithStatus(branchID, "Delivered");
	}

	/**
	 * Returns a list of of the brief descriptions on the orders that have
	 * failed from being delivered from the branch corresponding to the provided
	 * branch ID
	 * 
	 * @param branchID
	 *            ID of the branch
	 * @return List of brief descriptions on orders
	 */
	public static ArrayList<BriefOrder> getFailedDeliveryOrders(String branchID) {
		return getBriefOrdersWithStatus(branchID, "Failed Delivery");
	}

	/**
	 * Returns a list of of the brief descriptions on the orders that have the
	 * status 'deliveryStatus' from the branch corresponding to the provided
	 * branch ID
	 * 
	 * @param branchID
	 *            ID of the branch
	 * @param deliveryStatus
	 *            Status of the delivery
	 * @return List of brief descriptions on orders
	 */
	private static ArrayList<BriefOrder> getBriefOrdersWithStatus(
			String branchID, String deliveryStatus) {

		ArrayList<String> collectiveOrderIDs = getOrdersIDsOfStatus(branchID,
				deliveryStatus);
		ArrayList<BriefOrder> collectiveOrders = new ArrayList<BriefOrder>();

		for (String collectiveOrderId : collectiveOrderIDs) {
			BriefOrder newOrder = getBriefOrderByID(collectiveOrderId);

			if (newOrder != null)
				collectiveOrders.add(newOrder);
		}

		return collectiveOrders;
	}

	/**
	 * Returns a list of of the order IDs that have the status 'deliveryStatus'
	 * from the branch corresponding to the provided branch ID
	 * 
	 * @param branchID
	 *            ID of the branch
	 * @param deliveryStatus
	 *            Status of the delivery
	 * @return List of brief descriptions on orders
	 */
	private static ArrayList<String> getOrdersIDsOfStatus(String branchID,
			String deliveryStatus) {
		ArrayList<String> orderIDs = new ArrayList<String>();

		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;

		try {
			preparedStatement = DBConnection
					.getConnection()
					.prepareStatement(
							"SELECT orderID FROM `Order` WHERE orderStatusID = ? AND branchID = ?");

			preparedStatement.setLong(1,
					Long.valueOf(getOrderStatusIDByStatus(deliveryStatus)));
			preparedStatement.setLong(2, Long.valueOf(branchID));

			resultSet = preparedStatement.executeQuery();

			while (resultSet.next())
				orderIDs.add(Long.toString(resultSet.getLong(1)));
		} catch (SQLException ex) {
			ex.printStackTrace();
		} finally {
			DBUtil.dispose(preparedStatement);
			DBUtil.dispose(resultSet);
		}

		return orderIDs;
	}

	/**
	 * Updates database by assigning the employee to the order corresponding
	 * respectively to the provided order ID and employee ID and returns a
	 * boolean value denoting if the employee was assigned to the order
	 * 
	 * @param orderID
	 *            ID of the order
	 * @param employeeID
	 *            ID of the employee
	 * @return true if the customer was assigned to the order; false otherwise
	 */
	private static boolean assignEmployeeForDelivery(String orderID,
			String employeeID) {
		// this method returns true if target employee can be assigned to an
		// order

		PreparedStatement preparedStatement = null;

		try {
			String assignedEmployeeID = getAssociatedEmployeeID(orderID);

			if (assignedEmployeeID != null)
				return false;

			if (!SalesEmployeeQueryModel.isSalesEmployeeAvailable(employeeID))
				return false;

			preparedStatement = DBConnection
					.getConnection()
					.prepareStatement(
							"UPDATE `Order` AS ORD1 SET ORD1.associatedEmployee = ? WHERE ORD1.orderID = ? AND ORD1.branchID = (SELECT branchID FROM SalesEmployee WHERE SalesEmployee.employeeID = ?)");

			preparedStatement.setLong(1, Long.valueOf(employeeID));
			preparedStatement.setLong(2, Long.valueOf(orderID));
			preparedStatement.setLong(3, Long.valueOf(employeeID));

			return preparedStatement.executeUpdate() == 1;
		} catch (SQLException ex) {
			return false;
		} finally {
			DBUtil.dispose(preparedStatement);
		}
	}

	/**
	 * Returns the ID of the employee associated to the order corresponding to
	 * the provided order ID
	 * 
	 * @param orderID
	 *            ID of the order
	 * @return ID of the associated employee
	 */
	// returns null on failure
	private static String getAssociatedEmployeeID(String orderID) {
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;

		try {
			preparedStatement = DBConnection.getConnection().prepareStatement(
					"SELECT associatedEmployee FROM `Order` WHERE orderID = ?");

			preparedStatement.setLong(1, Long.valueOf(orderID));

			resultSet = preparedStatement.executeQuery();

			if (!resultSet.next())
				throw new SQLException();

			long associatedEmployeeID = resultSet.getLong(1);

			if (associatedEmployeeID == 0)
				throw new SQLException();

			return Long.toString(associatedEmployeeID);
		} catch (SQLException ex) {
			return null;
		} finally {
			DBUtil.dispose(preparedStatement);
			DBUtil.dispose(resultSet);
		}
	}

	/**
	 * Returns the status ID of the provided status
	 * 
	 * @param status
	 *            A status of orders
	 * @return ID of the status
	 * @throws SQLException
	 *             if a database access error occurs
	 */
	public static String getOrderStatusIDByStatus(String status)
			throws SQLException {
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;

		try {
			preparedStatement = DBConnection.getConnection().prepareStatement(
					"SELECT orderStatusID FROM OrderStatus WHERE status = ?");

			preparedStatement.setString(1, status);

			resultSet = preparedStatement.executeQuery();

			if (!resultSet.next())
				throw new SQLException("invalid order status");

			return Long.toString(resultSet.getLong(1));
		} catch (SQLException ex) {
			throw ex;
		} finally {
			DBUtil.dispose(resultSet);
			DBUtil.dispose(preparedStatement);
		}
	}

	/**
	 * Returns the status corresponding to the provided status ID
	 * 
	 * @param statusID
	 *            ID of a status
	 * @return Status
	 * @throws SQLException
	 *             if a database access error occurs
	 */
	public static String getOrderStatusByID(String statusID)
			throws SQLException {
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;

		try {
			preparedStatement = DBConnection.getConnection().prepareStatement(
					"SELECT status FROM OrderStatus WHERE orderStatusID = ?");

			preparedStatement.setLong(1, Long.valueOf(statusID));

			resultSet = preparedStatement.executeQuery();

			if (!resultSet.next())
				throw new SQLException("invalid order statusID");

			return resultSet.getString(1);
		} catch (SQLException ex) {
			throw ex;
		} finally {
			DBUtil.dispose(resultSet);
			DBUtil.dispose(preparedStatement);
		}
	}

	/**
	 * Updates database by dispatching the order corresponding to the provided
	 * orderID and associating the employee corresponding to the provided
	 * employee ID to the order
	 * 
	 * @param orderID
	 *            ID of the order
	 * @param employeeID
	 *            ID of the employee
	 * @throws SQLException
	 *             if a database access error occurs
	 */
	public static void dispatchOrder(String orderID, String employeeID)
			throws SQLException {
		if (!assignEmployeeForDelivery(orderID, employeeID))
			throw new SQLException();
		else {
			updateOrderStatus(orderID, "Being Delivered");
			BranchInventoryQueryModel.withdrawProductsFromStock(orderID);
		}
	}

	/**
	 * Updates database by confirming the order corresponding to the provided
	 * order ID as either delivered or failed, denoted by the provided boolean
	 * status
	 * 
	 * @param orderID
	 *            ID of the order
	 * @param successfulDelivery
	 *            Boolean flag denoting the order as delivered or failed
	 * @throws SQLException
	 *             if a database access error occurs
	 */
	public static void confirmOrderDelivery(String orderID,
			boolean successfulDelivery) throws SQLException {
		if (successfulDelivery) {
			updateOrderStatus(orderID, "Delivered"); // checks order existence
			BranchInventoryQueryModel.markProductsAsSold(orderID);
		} else {
			updateOrderStatus(orderID, "Failed Delivery");
			BranchInventoryQueryModel
					.addProductsBackToStockAndMakeAvailable(orderID);
		}
	}

	/**
	 * Returns the ID of the latest order
	 * 
	 * @return ID of the latest order
	 */
	public static String getLatestOrderID() {
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;

		try {
			preparedStatement = DBConnection
					.getConnection()
					.prepareStatement(
							"SELECT orderID FROM `Order` ORDER BY orderID DESC LIMIT 1");

			resultSet = preparedStatement.executeQuery();

			if (!resultSet.next())
				throw new SQLException("invalid order statusID");

			return resultSet.getString(1);
		} catch (SQLException ex) {
			ex.printStackTrace();
			return null;
		} finally {
			DBUtil.dispose(resultSet);
			DBUtil.dispose(preparedStatement);
		}
	}

	/**
	 * Returns a list of order IDs related to the customer corresponding to the
	 * provided customer ID
	 * 
	 * @param customerID
	 *            ID of the customer
	 * @return List of order IDs
	 */
	public static ArrayList<String> getOrderIDsByCustomerID(String customerID) {
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		ArrayList<String> orderIDs = new ArrayList<String>();

		try {
			preparedStatement = DBConnection.getConnection().prepareStatement(
					"SELECT orderID FROM `Order` WHERE customerID = ?");
			preparedStatement.setLong(1, Long.parseLong(customerID));

			resultSet = preparedStatement.executeQuery();

			while (resultSet.next())
				orderIDs.add(resultSet.getString(1));

			return orderIDs;
		} catch (SQLException ex) {
			ex.printStackTrace();
			return null;
		} finally {
			DBUtil.dispose(resultSet);
			DBUtil.dispose(preparedStatement);
		}
	}

	/**
	 * Returns a brief description to be displayed of the order corresponding to
	 * provided order ID
	 * 
	 * @param orderID
	 *            ID of the order
	 * @return Display information of the order
	 */
	public static OrderDisplayInformation getOrderDisplayInformation(
			String orderID) {
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;

		try {
			preparedStatement = DBConnection
					.getConnection()
					.prepareStatement(
							"SELECT customerID, orderDate, detailedDeliveryLocation, orderStatusID, totalOrderingCost FROM `Order` WHERE orderID = ?");
			preparedStatement.setLong(1, Long.parseLong(orderID));

			resultSet = preparedStatement.executeQuery();

			if (!resultSet.next())
				throw new IllegalArgumentException("No such order");

			String customerID = resultSet.getString(1);
			HashMap<String, Integer> orderProducts = new HashMap<String, Integer>();

			PreparedStatement preparedStatement2 = DBConnection
					.getConnection()
					.prepareStatement(
							"SELECT productID, quantity FROM OrderProducts WHERE orderID = ?");
			preparedStatement2.setLong(1, Long.parseLong(orderID));

			ResultSet resultSet2 = preparedStatement2.executeQuery();

			try {
				while (resultSet2.next()) {
					String productID = String.valueOf(resultSet2.getLong(1));
					String productName = ProductQueryModel
							.getProductName(productID);
					int quantity = resultSet2.getInt(2);

					orderProducts.put(productName, quantity);
				}
			} catch (SQLException ex) {
				ex.printStackTrace();
				return null;
			} finally {
				DBUtil.dispose(resultSet2);
				DBUtil.dispose(preparedStatement2);
			}

			Date orderDate = resultSet.getDate(2);
			String detailedDeliveryLocation = resultSet.getString(3);
			String orderStatusID = String.valueOf(resultSet.getLong(4));
			String orderStatus = getDisplayOrderStatus(getOrderStatusByID(orderStatusID));
			double totalOrderingCost = resultSet.getDouble(5);

			return new OrderDisplayInformation(orderID, customerID,
					orderProducts, orderDate, detailedDeliveryLocation,
					orderStatus, totalOrderingCost);
		} catch (SQLException ex) {
			ex.printStackTrace();
			return null;
		} finally {
			DBUtil.dispose(resultSet);
			DBUtil.dispose(preparedStatement);
		}
	}

	/**
	 * Returns the status to be displayed corresponding to the status ID
	 * 
	 * @param orderStatus
	 *            ID of the status
	 * @return Status to be displayed
	 */
	private static String getDisplayOrderStatus(String orderStatus) {
		if (orderStatus.equals("Unhandled"))
			return "Order Placed";

		return orderStatus;
	}

	/**
	 * Returns a list of brief descriptions on orders to displayed related to
	 * the customer corresponding to the provided customer ID
	 * 
	 * @param customerID
	 *            ID of the customer
	 * @return List of order display information
	 */
	public static ArrayList<OrderDisplayInformation> getOrderDisplayInformationByCustomerID(
			String customerID) {
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		ArrayList<OrderDisplayInformation> displayInformation = new ArrayList<OrderDisplayInformation>();

		try {
			preparedStatement = DBConnection
					.getConnection()
					.prepareStatement(
							"SELECT orderID FROM `Order` WHERE customerID = ? ORDER BY orderDate DESC");
			preparedStatement.setLong(1, Long.parseLong(customerID));

			resultSet = preparedStatement.executeQuery();

			while (resultSet.next()) {
				String orderID = resultSet.getString(1);
				displayInformation.add(getOrderDisplayInformation(orderID));
			}

			return displayInformation;

		} catch (SQLException ex) {
			ex.printStackTrace();
			return null;
		} finally {
			DBUtil.dispose(resultSet);
			DBUtil.dispose(preparedStatement);
		}
	}
}
