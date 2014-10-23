package com.ewebstore.model;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.ewebstore.dbutil.DBConnection;
import com.ewebstore.dbutil.DBUtil;

public class BranchInventoryQueryModel {
	public static void updateBranchInventoryAfterTransfer(
			String inventoryTransferID) throws SQLException {
		if (BranchInventoryQueryModel
				.branchInventoryHasTransferProduct(inventoryTransferID)) {
			PreparedStatement preparedStatement = null;
			try {
				preparedStatement = DBConnection
						.getConnection()
						.prepareStatement(
								"UPDATE BranchInventory SET inStockQuantity = inStockQuantity + (SELECT quantity FROM BranchInventoryTransfer WHERE inventoryTransferID = ?) WHERE BranchInventory.branchID = (SELECT toBranchID FROM BranchInventoryTransfer WHERE inventoryTransferID = ?) AND BranchInventory.productID = (SELECT productID FROM BranchInventoryTransfer WHERE inventoryTransferID = ?)");

				preparedStatement.setLong(1, Long.valueOf(inventoryTransferID));
				preparedStatement.setLong(2, Long.valueOf(inventoryTransferID));
				preparedStatement.setLong(3, Long.valueOf(inventoryTransferID));

				preparedStatement.executeUpdate();
			} catch (SQLException ex) {
				throw ex;
			} finally {
				DBUtil.dispose(preparedStatement);
			}
		} else {
			PreparedStatement preparedStatement = null;
			try {
				preparedStatement = DBConnection
						.getConnection()
						.prepareStatement(
								"INSERT INTO BranchInventory VALUES((SELECT toBranchID FROM BranchInventoryTransfer WHERE inventoryTransferID = ?), (SELECT productID FROM BranchInventoryTransfer WHERE inventoryTransferID = ?), (SELECT quantity FROM BranchInventoryTransfer WHERE inventoryTransferID = ?), 0, 0, 0)");

				preparedStatement.setLong(1, Long.valueOf(inventoryTransferID));
				preparedStatement.setLong(2, Long.valueOf(inventoryTransferID));
				preparedStatement.setLong(3, Long.valueOf(inventoryTransferID));

				preparedStatement.executeUpdate();
			} catch (SQLException ex) {
				throw ex;
			} finally {
				DBUtil.dispose(preparedStatement);
			}
		}
	}

	private static boolean branchInventoryHasTransferProduct(
			String inventoryTransferID) throws SQLException {
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;

		try {
			preparedStatement = DBConnection
					.getConnection()
					.prepareStatement(
							"SELECT COUNT(*) FROM BranchInventory WHERE branchID = (SELECT toBranchID FROM BranchInventoryTransfer WHERE toBranchID = ?)");

			preparedStatement.setLong(1, Long.valueOf(inventoryTransferID));

			resultSet = preparedStatement.executeQuery();

			if (!resultSet.next())
				throw new SQLException();

			int count = resultSet.getInt(1);

			return count == 1;

		} catch (SQLException ex) {
			throw ex;
		} finally {
			DBUtil.dispose(preparedStatement);
			DBUtil.dispose(resultSet);
		}
	}

	public static void addProductToInventory(String branchManagerID,
			String productID, int quantity) throws SQLException {
		PreparedStatement preparedStatement = null;

		try {
			preparedStatement = DBConnection
					.getConnection()
					.prepareStatement(
							"INSERT INTO BranchInventory VALUES ((SELECT branchID FROM BranchManager WHERE managerID = ?), ?, ?, 0, 0, ?) ON DUPLICATE KEY UPDATE inStockQuantity = inStockQuantity + ?, availableQuantity = availableQuantity + ?");

			preparedStatement.setLong(1, Long.parseLong(branchManagerID));
			preparedStatement.setLong(2, Long.parseLong(productID));
			preparedStatement.setInt(3, quantity);
			preparedStatement.setInt(4, quantity);
			preparedStatement.setInt(5, quantity);
			preparedStatement.setInt(6, quantity);

			preparedStatement.executeUpdate();
		} catch (SQLException ex) {
			throw ex;
		} finally {
			DBUtil.dispose(preparedStatement);
		}
	}

	public static int getAvailableProductQuantity(String branchID,
			String productID) throws SQLException {
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;

		try {
			preparedStatement = DBConnection
					.getConnection()
					.prepareStatement(
							"SELECT availableQuantity FROM BranchInventory WHERE branchID = ? AND productID = ?");

			preparedStatement.setLong(1, Long.valueOf(branchID));
			preparedStatement.setLong(2, Long.valueOf(productID));

			resultSet = preparedStatement.executeQuery();

			if (!resultSet.next())
				throw new SQLException();

			return resultSet.getInt(1);

		} catch (SQLException ex) {
			throw ex;
		} finally {
			DBUtil.dispose(preparedStatement);
			DBUtil.dispose(resultSet);
		}
	}

	public static void withdrawProductsFromStock(String orderID)
			throws SQLException {
		// availability of products already reduced during shopping cart
		// checkout
		// so just withdraw from stock

		PreparedStatement preparedStatement = null;

		try {
			preparedStatement = DBConnection
					.getConnection()
					.prepareStatement(
							"UPDATE BranchInventory AS BRI1 SET inStockQuantity = inStockQuantity - (SELECT quantity FROM OrderProducts AS ORDP1 WHERE ORDP1.orderID = ? AND ORDP1.productID = BRI1.productID) WHERE branchID = (SELECT branchID FROM `Order` WHERE orderID = ?) AND productID IN (SELECT productID FROM OrderProducts AS ORDP2 WHERE ORDP2.orderID = ?)");

			preparedStatement.setLong(1, Long.parseLong(orderID));
			preparedStatement.setLong(2, Long.parseLong(orderID));
			preparedStatement.setLong(3, Long.parseLong(orderID));

			preparedStatement.executeUpdate();
		} catch (SQLException ex) {
			throw ex;
		} finally {
			DBUtil.dispose(preparedStatement);
		}
	}

	public static void addProductsBackToStockAndMakeAvailable(String orderID)
			throws SQLException {
		PreparedStatement preparedStatement = null;

		try {
			preparedStatement = DBConnection
					.getConnection()
					.prepareStatement(
							"UPDATE BranchInventory AS BRI1 SET inStockQuantity = inStockQuantity + (SELECT quantity FROM OrderProducts AS ORDP1 WHERE ORDP1.orderID = ? AND ORDP1.productID = BRI1.productID), availableQuantity = availableQuantity + (SELECT quantity FROM OrderProducts AS ORDP1 WHERE ORDP1.orderID = ? AND ORDP1.productID = BRI1.productID) WHERE branchID = (SELECT branchID FROM `Order` WHERE orderID = ?) AND productID IN (SELECT productID FROM OrderProducts AS ORDP2 WHERE ORDP2.orderID = ?)");

			preparedStatement.setLong(1, Long.parseLong(orderID));
			preparedStatement.setLong(2, Long.parseLong(orderID));
			preparedStatement.setLong(3, Long.parseLong(orderID));
			preparedStatement.setLong(4, Long.parseLong(orderID));

			preparedStatement.executeUpdate();
		} catch (SQLException ex) {
			throw ex;
		} finally {
			DBUtil.dispose(preparedStatement);
		}
	}

	public static void markProductsAsSold(String orderID) throws SQLException {
		PreparedStatement preparedStatement = null;

		try {
			preparedStatement = DBConnection
					.getConnection()
					.prepareStatement(
							"UPDATE BranchInventory AS BRI1 SET soldQuantity = soldQuantity + (SELECT quantity FROM OrderProducts AS ORDP1 WHERE ORDP1.orderID = ? AND ORDP1.productID = BRI1.productID) WHERE branchID = (SELECT branchID FROM `Order` WHERE orderID = ?) AND productID IN (SELECT productID FROM OrderProducts AS ORDP2 WHERE ORDP2.orderID = ?)");

			preparedStatement.setLong(1, Long.parseLong(orderID));
			preparedStatement.setLong(2, Long.parseLong(orderID));
			preparedStatement.setLong(3, Long.parseLong(orderID));

			preparedStatement.executeUpdate();
		} catch (SQLException ex) {
			throw ex;
		} finally {
			DBUtil.dispose(preparedStatement);
		}
	}

	public static void withdrawProductsForTransfer(String inventoryTransferID)
			throws SQLException {
		PreparedStatement preparedStatement = null;

		try {
			preparedStatement = DBConnection
					.getConnection()
					.prepareStatement(
							"UPDATE BranchInventory SET inStockQuantity = inStockQuantity - (SELECT quantity FROM BranchInventoryTransfer WHERE inventoryTransferID = ?) WHERE branchID = (SELECT fromBranchID FROM BranchInventoryTransfer WHERE inventoryTransferID = ?) AND productID = (SELECT productID FROM BranchInventoryTransfer WHERE inventoryTransferID = ?)");

			preparedStatement.setLong(1, Long.parseLong(inventoryTransferID));
			preparedStatement.setLong(2, Long.parseLong(inventoryTransferID));
			preparedStatement.setLong(3, Long.parseLong(inventoryTransferID));

			preparedStatement.executeUpdate();
		} catch (SQLException ex) {
			throw ex;
		} finally {
			DBUtil.dispose(preparedStatement);
		}
	}
}
