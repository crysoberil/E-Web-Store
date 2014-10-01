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
		// TODO Auto-generated method stub
		
	}
}
