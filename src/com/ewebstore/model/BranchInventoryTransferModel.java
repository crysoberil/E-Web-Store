package com.ewebstore.model;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

import com.ewebstore.dbutil.DBConnection;
import com.ewebstore.dbutil.DBUtil;
import com.ewebstore.dbutil.DatabaseTransaction;
import com.ewebstore.entity.ShoppingCart;

// TODO repair class
public class BranchInventoryTransferModel {

	private static final int TOBETRANSFERREDSTATUS = 1;
	private static final int BEINGTRANSFERREDSTATUS = 2;
	private static final int TRANSFERCOMPLETEDSTATUS = 3;

	// TODO update inventorytransfer, Branchinventory
	// avalilability
	public static void distributeOrderBetweenBranches(
			DatabaseTransaction transaction, ShoppingCart cart,
			String targetBranchID) throws SQLException {
		ArrayList<String> newInventoryTransferIDs = new ArrayList<String>();

		// TODO for cartitems, if necessary create branchtransferreqm update
		// inventory
	}

	public static void confirmInventoryTransfer(String inventoryTransferID)
			throws SQLException {
		try {
			DatabaseTransaction transaction = new DatabaseTransaction();
			updateBranchInventoryAfterTransfer(transaction, inventoryTransferID);
			markCompletedInventoryTransfer(transaction, inventoryTransferID);
			transaction.commit();
		} catch (SQLException ex) {
			ex.printStackTrace();
			throw ex;
		}
	}

	private static void updateBranchInventoryAfterTransfer(
			DatabaseTransaction transaction, String inventoryTransferID)
			throws SQLException {
		PreparedStatement preparedStatement = transaction
				.newPreparedStatement("UPDATE BranchInventory SET availableQuantity = availableQuantity + (SELECT quantity FROM BranchInventoryTransfer WHERE BranchInventoryTransfer.productID = BranchInventory.productID AND inventoryTransferID = ?), inStockQuantity = inStockQuantity + (SELECT quantity FROM BranchInventoryTransfer WHERE BranchInventoryTransfer.productID = BranchInventory.productID AND inventoryTransferID = ?) WHERE BranchInventory.branchID = (SELECT toBranchID FROM BranchInventoryTransfer WHERE inventoryTransferID = ?)");

		preparedStatement.setString(1, inventoryTransferID);
		preparedStatement.setString(2, inventoryTransferID);
		preparedStatement.setString(3, inventoryTransferID);
	}

	private static void markCompletedInventoryTransfer(
			DatabaseTransaction transaction, String inventoryTransferID)
			throws SQLException {
		PreparedStatement preparedStatement = transaction
				.newPreparedStatement("UPDATE BranchInventoryTransfer SET transferStatus = ? WHERE inventoryTransferID = ?");
		preparedStatement.setInt(1, TRANSFERCOMPLETEDSTATUS);
		preparedStatement.setString(2, inventoryTransferID);
	}

	public static void markAsOngoingInventoryTransfer(String inventoryTransferID)
			throws SQLException {
		updateInventoryTransferStatus(inventoryTransferID,
				BEINGTRANSFERREDSTATUS);
	}

	private static void updateInventoryTransferStatus(
			String inventoryTransferID, int targetStatus) throws SQLException {
		PreparedStatement preparedStatement = null;

		try {
			preparedStatement = DBConnection
					.getSharedConnection()
					.prepareStatement(
							"UPDATE BranchInventoryTransfer SET transferStatus = ? WHERE inventoryTransferID = ?");

			preparedStatement.setInt(1, targetStatus);
			preparedStatement.setString(2, inventoryTransferID);

			preparedStatement.executeUpdate();
		} catch (SQLException ex) {
			ex.printStackTrace();
			throw ex;
		} finally {
			DBUtil.dispose(preparedStatement);
		}
	}
}
