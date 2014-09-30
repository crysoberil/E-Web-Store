package com.ewebstore.model;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.ewebstore.dbutil.DBConnection;
import com.ewebstore.dbutil.DBUtil;
import com.ewebstore.entity.CartItem;
import com.ewebstore.entity.ShoppingCart;

// TODO repair class
public class BranchInventoryTransferModel {

	private static final int TOBETRANSFERREDSTATUS = 1;
	private static final int BEINGTRANSFERREDSTATUS = 2;

	public static void distributeOrderBetweenBranches(ShoppingCart cart,
			String targetBranchID) throws SQLException {
		// TODO for cartitems, if necessary create branchtransferreq; update
		// inventory

		for (CartItem cartItem : cart.getCartItems())
			distributeCartItemBetweenBranches(cartItem, targetBranchID);
	}

	private static void distributeCartItemBetweenBranches(CartItem cartItem,
			String targetBranchID) throws SQLException {
		// if necessary create branchtransferreq; update inventory

		int quantity = cartItem.getQuantity();

		while (quantity != 0) {
			int assignedQuantity = assignBranchForItemSupply(
					cartItem.getProductID(), quantity, targetBranchID);
			quantity -= assignedQuantity;
		}
	}

	private static int assignBranchForItemSupply(String productID,
			int maxQuantity, String targetBranchID) throws SQLException {

		String supplierBranchID = BranchQueryModel
				.getNearestBranchIDWithProductAvailble(targetBranchID,
						productID);

		int availableAmount = BranchQueryModel.getAvailableProductQuantity(
				supplierBranchID, productID);

		int toSupplyQuantity = Math.min(availableAmount, maxQuantity);

		BranchQueryModel.removeAvailableProduct(supplierBranchID, productID,
				toSupplyQuantity);

		if (supplierBranchID != targetBranchID)
			addBranchTransferRequest(supplierBranchID, targetBranchID,
					productID, toSupplyQuantity);

		return toSupplyQuantity;
	}

	private static void addBranchTransferRequest(String supplierBranchID,
			String targetBranchID, String productID, int toSupplyQuantity)
			throws SQLException {
		if (branchTransferRecordExists(supplierBranchID, targetBranchID,
				productID)) {
			// then update

			PreparedStatement preparedStatement = null;

			try {
				preparedStatement = DBConnection
						.getConnection()
						.prepareStatement(
								"UPDATE BranchInventoryTransfer SET quntity = quntity + ? WHERE fromBranchID = ? AND toBranchID = ?");
				preparedStatement.setInt(1, toSupplyQuantity);
				preparedStatement.setLong(2, Long.valueOf(supplierBranchID));
				preparedStatement.setLong(2, Long.valueOf(targetBranchID));
				preparedStatement.executeUpdate();
			} catch (SQLException ex) {
				throw ex;
			} finally {
				DBUtil.dispose(preparedStatement);
			}

		} else {
			// insert

			PreparedStatement preparedStatement = null;

			try {
				preparedStatement = DBConnection
						.getConnection()
						.prepareStatement(
								"INSERT INTO BranchInventoryTransfer VALUES(?, ?, ?, ?, NULL, ?)");
				preparedStatement.setLong(1, Long.valueOf(supplierBranchID));
				preparedStatement.setLong(2, Long.valueOf(targetBranchID));
				preparedStatement.setLong(3, Long.valueOf(productID));
				preparedStatement.setInt(4, toSupplyQuantity);
				preparedStatement.setInt(5, TOBETRANSFERREDSTATUS);

				preparedStatement.executeUpdate();
			} catch (SQLException ex) {
				throw ex;
			} finally {
				DBUtil.dispose(preparedStatement);
			}
		}
	}

	private static boolean branchTransferRecordExists(String supplierBranchID,
			String targetBranchID, String productID) throws SQLException {
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;

		try {
			preparedStatement = DBConnection
					.getConnection()
					.prepareStatement(
							"SELECT COUNT(*) FROM BranchInventoryTransfer WHERE fromBranchID = ? AND toBranchID = ? AND productID = ?");

			preparedStatement.setLong(1, Long.valueOf(supplierBranchID));
			preparedStatement.setLong(2, Long.valueOf(targetBranchID));
			preparedStatement.setLong(3, Long.valueOf(productID));

			resultSet = preparedStatement.executeQuery();

			if (!resultSet.next()) // must happen
				throw new SQLException();

			return resultSet.getInt(1) != 0;
		} catch (SQLException ex) {
			throw ex;
		} finally {
			DBUtil.dispose(preparedStatement);
			DBUtil.dispose(resultSet);
		}
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
