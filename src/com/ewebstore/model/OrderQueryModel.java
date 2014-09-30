package com.ewebstore.model;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.ewebstore.dbutil.DBConnection;
import com.ewebstore.dbutil.DBUtil;
import com.ewebstore.entity.Order;
import com.ewebstore.entity.OrderProduct;

public class OrderQueryModel {

	public static ArrayList<Order> getOrdersToDispatch(String branchID) {

		ArrayList<String> collectiveOrderIDs = getToDispatchOrderIDs(branchID);
		ArrayList<Order> collectiveOrders = new ArrayList<Order>();

		for (String collectiveOrderID : collectiveOrderIDs) {
			Order newOrder = getOrderByID(collectiveOrderID);

			if (newOrder != null)
				collectiveOrders.add(newOrder);
		}

		return collectiveOrders;
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
							"SELECT customerID, orderDate, detailedDeliveryLocation, orderStausID, branchID, associatedEmployee, totalOrderingCost FROM Order WHERE orderID = ?");

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
				double totalOrderingCost = resultSet.getDouble(7);

				String customerName = CustomerQueryModel
						.getCustomerName(customerID);

				String orderBranchName = BranchQueryModel
						.getBranchNameByID(orderBranchID);

				String orderStatus = getOrderStatusByID(orderStatusID);

				ArrayList<OrderProduct> orderProducts = getOrderProducts(orderID);

				order = new Order(orderID, customerID, customerName, orderDate,
						deliveryLocation, orderStatusID, orderStatus,
						orderBranchID, orderBranchName, associatedEmployeeID,
						totalOrderingCost, orderProducts);
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
		} finally {
			DBUtil.dispose(preparedStatement);
			DBUtil.dispose(resultSet);
		}

		return order;
	}

	public static ArrayList<OrderProduct> getOrderProducts(String orderID) {
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
							"SELECT orderID FROM Order WHERE orderStatusID = ? AND NOT EXISTS (SELECT orderID FROM OrderProducts WHERE OrderProducts.orderID = Order.orderID AND OrderProducts.quantity > (SELECT availableQuantity FROM BranchInventory WHERE BranchInventory.branchID = ? AND BranchInventory.productID = OrderProducts.productID))");

			preparedStatement.setLong(1,
					Long.valueOf(getOrderStatusIDByStatus("unhandled")));
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

	public static void updateOrderStatus(String orderID, String orderStatus)
			throws SQLException {
		PreparedStatement preparedStatement = null;

		try {
			preparedStatement = DBConnection
					.getConnection()
					.prepareStatement(
							"UPDATE Order SET orderStatusID = (SELECT orderID FROM OrderStatus WHERE status = ?) WHERE orderID = ?");

			preparedStatement.setString(1, orderStatus);
			preparedStatement.setLong(2, Long.valueOf(orderID));

			preparedStatement.executeUpdate();
		} catch (SQLException ex) {
			throw ex;
		} finally {
			DBUtil.dispose(preparedStatement);
		}
	}

	public static ArrayList<Order> getOnDeliveryOrders(String branchID) {
		return getOrdersWithStatus(branchID, "ondelivery");
	}

	public static ArrayList<Order> getDeliveredOrders(String branchID) {
		return getOrdersWithStatus(branchID, "delivered");
	}

	public static ArrayList<Order> getFailedDeliveryOrders(String branchID) {
		return getOrdersWithStatus(branchID, "faileddelivery");
	}

	private static ArrayList<Order> getOrdersWithStatus(String branchID,
			String deliveryStatus) {

		ArrayList<String> collectiveOrderIDs = getOrdersIDsOfStatus(branchID,
				deliveryStatus);
		ArrayList<Order> collectiveOrders = new ArrayList<Order>();

		for (String collectiveOrderId : collectiveOrderIDs) {
			Order newOrder = getOrderByID(collectiveOrderId);

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
							"SELECT orderID FROM Order WHERE orderStatusID = ? AND branchID = ?");

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

	public static boolean assignEmployeeForDelivery(String orderID,
			String employeeID) {
		// this method returns true if target employee can be assigned to an
		// order

		PreparedStatement preparedStatement = null;

		try {
			String assignedEmployeeID = getAssociatedEmployeeID(orderID);

			if (assignedEmployeeID != null)
				return false;

			preparedStatement = DBConnection
					.getConnection()
					.prepareStatement(
							"UPDATE Order SET associatedEmployee = ? WHERE orderID = ?");

			preparedStatement.setLong(1, Long.valueOf(employeeID));
			preparedStatement.setLong(2, Long.valueOf(orderID));

			return preparedStatement.executeUpdate() == 1;
		} catch (SQLException ex) {
			return false;
		} finally {
			DBUtil.dispose(preparedStatement);
		}
	}

	public static String getAssociatedEmployeeID(String orderID) {
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;

		try {
			preparedStatement = DBConnection
					.getConnection()
					.prepareStatement(
							"SELECT associatedEmployee FROM Order WHERE orderID = ?");

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
			preparedStatement = DBConnection
					.getConnection()
					.prepareStatement(
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
			preparedStatement = DBConnection
					.getConnection()
					.prepareStatement(
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

}
