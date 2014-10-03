package com.ewebstore.model;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.ewebstore.dbutil.DBConnection;
import com.ewebstore.dbutil.DBUtil;
import com.ewebstore.entity.BriefOrder;
import com.ewebstore.entity.Order;
import com.ewebstore.entity.OrderProduct;

public class OrderQueryModel {

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

	private static ArrayList<String> getToDispatchOrderIDs(String branchID) {
		ArrayList<String> orderIDs = new ArrayList<String>();

		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;

		try {
			preparedStatement = DBConnection
					.getConnection()
					.prepareStatement(
							"SELECT orderID FROM `Order` AS ORD1 WHERE orderStatusID = ? AND branchID = ? AND NOT EXISTS(SELECT orderProductsID FROM OrderProducts AS ORDP1 WHERE ORDP1.orderID = ORD1.orderID AND ORDP1.quantity > (SELECT inStockQuantity - availableQuantity - (SELECT COALESCE(SUM(quantity), 0) FROM BranchInventoryTransfer AS BRIT1 WHERE fromBranchID = BRINV1.branchID AND BRIT1.productID = BRINV1.productID) FROM BranchInventory AS BRINV1 WHERE BRINV1.branchID = ORD1.branchID AND BRINV1.productID = ORDP1.productID)) ORDER BY orderDate DESC");

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

	private static void updateOrderStatus(String orderID, String orderStatus)
			throws SQLException {
		PreparedStatement preparedStatement = null;

		try {
			preparedStatement = DBConnection
					.getConnection()
					.prepareStatement(
							"UPDATE `Order` SET orderStatusID = (SELECT orderID FROM OrderStatus WHERE status = ?) WHERE orderID = ?");

			preparedStatement.setString(1, orderStatus);
			preparedStatement.setLong(2, Long.valueOf(orderID));

			preparedStatement.executeUpdate();
		} catch (SQLException ex) {
			throw ex;
		} finally {
			DBUtil.dispose(preparedStatement);
		}
	}

	public static ArrayList<BriefOrder> getOnDeliveryOrders(String branchID) {
		return getBriefOrdersWithStatus(branchID, "Being Delivered");
	}

	public static ArrayList<BriefOrder> getDeliveredOrders(String branchID) {
		return getBriefOrdersWithStatus(branchID, "Delivered");
	}

	public static ArrayList<BriefOrder> getFailedDeliveryOrders(String branchID) {
		return getBriefOrdersWithStatus(branchID, "Failed Delivery");
	}

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

	// returns null on failure
	private static String getAssociatedEmployeeID(String orderID) {
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;

		try {
			preparedStatement = DBConnection.getConnection().prepareStatement(
					"SELECT associatedEmployee FROM `Order` WHERE orderID = ?");

			preparedStatement.setLong(1, Long.valueOf(orderID));

			resultSet = preparedStatement.executeQuery();

			resultSet.next();

			return Long.toString(resultSet.getLong(1));
		} catch (SQLException ex) {
			return null;
		} finally {
			DBUtil.dispose(preparedStatement);
			DBUtil.dispose(resultSet);
		}
	}

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

	public static void dispatchOrder(String orderID, String employeeID)
			throws SQLException {
		if (!assignEmployeeForDelivery(orderID, employeeID))
			throw new SQLException();
		else
			updateOrderStatus(orderID, "Being Delivered");
	}
}