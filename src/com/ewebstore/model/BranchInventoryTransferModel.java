package com.ewebstore.model;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

import com.ewebstore.dbutil.DBConnection;
import com.ewebstore.dbutil.DBUtil;
import com.ewebstore.entity.ShoppingCart;

// TODO repair class
public class BranchInventoryTransferModel {

	private static final int TOBETRANSFERREDSTATUS = 1;
	private static final int BEINGTRANSFERREDSTATUS = 2;

	// TODO update inventorytransfer, Branchinventory
	// avalilability
	public static void distributeOrderBetweenBranches(ShoppingCart cart,
			String targetBranchID) throws SQLException {
		ArrayList<String> newInventoryTransferIDs = new ArrayList<String>();

		// TODO for cartitems, if necessary create branchtransferreqm update
		// inventory
	}

	public static void confirmInventoryTransfer(String inventoryTransferID)
			throws SQLException {
		try {
			updateBranchInventoryAfterTransfer(inventoryTransferID);
			deleteCompletedInventoryTransfer(inventoryTransferID);
		} catch (SQLException ex) {
			ex.printStackTrace();
			throw ex;
		}
	}

	private static void updateBranchInventoryAfterTransfer(
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

	private static void deleteCompletedInventoryTransfer(
			String inventoryTransferID) throws SQLException {
		PreparedStatement preparedStatement = null;
		try {
			preparedStatement = DBConnection
					.getConnection()
					.prepareStatement(
							"DELETE FROM BranchInventoryTransfer inventoryTransferID = ?");

			preparedStatement.setLong(1, Long.valueOf(inventoryTransferID));
			preparedStatement.executeUpdate();
		} catch (SQLException ex) {
			throw ex;
		} finally {
			DBUtil.dispose(preparedStatement);
		}
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
					.getConnection()
					.prepareStatement(
							"UPDATE BranchInventoryTransfer SET transferStatus = ? WHERE inventoryTransferID = ?");

			preparedStatement.setInt(1, targetStatus);
			preparedStatement.setLong(2, Long.valueOf(inventoryTransferID));

			preparedStatement.executeUpdate();
		} catch (SQLException ex) {
			ex.printStackTrace();
			throw ex;
		} finally {
			DBUtil.dispose(preparedStatement);
		}
	}
}
