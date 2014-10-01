package com.ewebstore.model;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.ewebstore.dbutil.DBConnection;
import com.ewebstore.dbutil.DBUtil;

public class BranchInventoryQueryModel {
	public static void updateBranchInventoryAfterTransfer(
			String inventoryTransferID) throws SQLException {
		PreparedStatement preparedStatement = null;
		try {
			preparedStatement = DBConnection
					.getConnection()
					.prepareStatement(
							"UPDATE BranchInventory SET availableQuantity = availableQuantity + (SELECT quantity FROM BranchInventoryTransfer WHERE BranchInventoryTransfer.productID = BranchInventory.productID AND inventoryTransferID = ?), inStockQuantity = inStockQuantity + (SELECT quantity FROM BranchInventoryTransfer WHERE BranchInventoryTransfer.productID = BranchInventory.productID AND inventoryTransferID = ?) WHERE BranchInventory.branchID = (SELECT toBranchID FROM BranchInventoryTransfer WHERE inventoryTransferID = ?)");

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
}
