package com.ewebstore.model;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.ewebstore.dbutil.DBConnection;
import com.ewebstore.dbutil.DBUtil;
import com.ewebstore.entity.CartItem;
import com.ewebstore.entity.ProductTransferEntity;
import com.ewebstore.entity.ShoppingCart;

public class BranchInventoryTransferModel {

	private static final int TOBETRANSFERREDSTATUS = 1;
	private static final int BEINGTRANSFERREDSTATUS = 2;

	public static void distributeOrderBetweenBranches(ShoppingCart cart,
			String targetBranchID) throws SQLException {
		for (CartItem cartItem : cart.getCartItems())
			distributeCartItemBetweenBranches(cartItem, targetBranchID);
	}

	private static void distributeCartItemBetweenBranches(CartItem cartItem,
			String targetBranchID) throws SQLException {
		int quantity = cartItem.getQuantity();

		while (quantity != 0) {
			int assignedQuantity = assignSomeBranchForItemSupply(
					cartItem.getProductID(), quantity, targetBranchID);
			quantity -= assignedQuantity;
		}
	}

	private static int assignSomeBranchForItemSupply(String productID,
			int maxQuantity, String targetBranchID) throws SQLException {

		String supplierBranchID = BranchQueryModel
				.getNearestBranchIDWithProductAvailble(targetBranchID,
						productID);

		int availableAmount = BranchInventoryQueryModel
				.getAvailableProductQuantity(supplierBranchID, productID);

		int toSupplyQuantity = Math.min(availableAmount, maxQuantity);

		BranchQueryModel.removeAvailableProduct(supplierBranchID, productID,
				toSupplyQuantity);

		if (!supplierBranchID.equals(targetBranchID))
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
								"UPDATE BranchInventoryTransfer SET quantity = quantity + ? WHERE fromBranchID = ? AND toBranchID = ?");
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

				if (preparedStatement.executeUpdate() != 1)
					throw new SQLException();
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
							"SELECT COUNT(*) FROM BranchInventoryTransfer WHERE fromBranchID = ? AND toBranchID = ? AND productID = ? AND transferStatus = 1");

			preparedStatement.setLong(1, Long.valueOf(supplierBranchID));
			preparedStatement.setLong(2, Long.valueOf(targetBranchID));
			preparedStatement.setLong(3, Long.valueOf(productID));

			resultSet = preparedStatement.executeQuery();

			if (!resultSet.next()) // must happen
				return false;

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
			BranchInventoryQueryModel
					.updateBranchInventoryAfterTransfer(inventoryTransferID);
			deleteCompletedInventoryTransfer(inventoryTransferID);
		} catch (SQLException ex) {
			ex.printStackTrace();
			throw ex;
		}
	}

	private static void deleteCompletedInventoryTransfer(
			String inventoryTransferID) throws SQLException {
		PreparedStatement preparedStatement = null;
		try {
			preparedStatement = DBConnection
					.getConnection()
					.prepareStatement(
							"DELETE FROM BranchInventoryTransfer WHERE inventoryTransferID = ?");

			preparedStatement.setLong(1, Long.valueOf(inventoryTransferID));
			preparedStatement.executeUpdate();
		} catch (SQLException ex) {
			throw ex;
		} finally {
			DBUtil.dispose(preparedStatement);
		}
	}

	private static void markAsOngoingInventoryTransfer(
			String inventoryTransferID) throws SQLException {
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

			if (preparedStatement.executeUpdate() != 1)
				throw new SQLException();
		} catch (SQLException ex) {
			ex.printStackTrace();
			throw ex;
		} finally {
			DBUtil.dispose(preparedStatement);
		}
	}

	public static ArrayList<ProductTransferEntity> getToSendTransferRequests(
			String branchID) {
		ArrayList<ProductTransferEntity> transferEntities = new ArrayList<ProductTransferEntity>();

		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;

		try {
			String branchName = BranchQueryModel.getBranchNameByID(branchID);

			preparedStatement = DBConnection
					.getConnection()
					.prepareStatement(
							"SELECT inventoryTransferID, toBranchID, productID, quantity FROM BranchInventoryTransfer WHERE fromBranchID = ? AND transferStatus = 1");
			preparedStatement.setLong(1, Long.valueOf(branchID));

			resultSet = preparedStatement.executeQuery();

			while (resultSet.next()) {
				String inventoryTransferID = Long
						.toString(resultSet.getLong(1));
				String toBranchID = Long.toString(resultSet.getLong(2));
				String productID = Long.toString(resultSet.getLong(3));
				int quantity = resultSet.getInt(4);

				String toBranchName = BranchQueryModel
						.getBranchNameByID(toBranchID);
				String productName = ProductQueryModel
						.getProductName(productID);

				ProductTransferEntity transferEntity = new ProductTransferEntity(
						inventoryTransferID, productID, productName, branchID,
						branchName, toBranchID, toBranchName, quantity);

				transferEntities.add(transferEntity);
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
		} finally {
			DBUtil.dispose(resultSet);
			DBUtil.dispose(preparedStatement);
		}

		return transferEntities;
	}

	public static void dispatchInventoryTransfer(String inventoryTransferID)
			throws SQLException {
		// get product out of stock
		markAsOngoingInventoryTransfer(inventoryTransferID);
		BranchInventoryQueryModel
				.withdrawProductsForTransfer(inventoryTransferID);
	}

	public static ArrayList<ProductTransferEntity> getToReceiveTransferRequests(
			String branchID) {
		ArrayList<ProductTransferEntity> transferEntities = new ArrayList<ProductTransferEntity>();

		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;

		try {
			String branchName = BranchQueryModel.getBranchNameByID(branchID);

			preparedStatement = DBConnection
					.getConnection()
					.prepareStatement(
							"SELECT inventoryTransferID, fromBranchID, productID, quantity FROM BranchInventoryTransfer WHERE toBranchID = ? AND transferStatus = 2");
			preparedStatement.setLong(1, Long.valueOf(branchID));

			resultSet = preparedStatement.executeQuery();

			while (resultSet.next()) {
				String inventoryTransferID = Long
						.toString(resultSet.getLong(1));
				String fromBranchID = Long.toString(resultSet.getLong(2));
				String productID = Long.toString(resultSet.getLong(3));
				int quantity = resultSet.getInt(4);

				String fromBranchName = BranchQueryModel
						.getBranchNameByID(fromBranchID);
				String productName = ProductQueryModel
						.getProductName(productID);

				ProductTransferEntity transferEntity = new ProductTransferEntity(
						inventoryTransferID, productID, productName,
						fromBranchID, fromBranchName, branchID, branchName,
						quantity);

				transferEntities.add(transferEntity);
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
		} finally {
			DBUtil.dispose(resultSet);
			DBUtil.dispose(preparedStatement);
		}

		return transferEntities;
	}

	public static void receiveInventoryTransfer(String inventoryTransferID)
			throws SQLException {
		// add to stock and NOT availability, then remove entry
		BranchInventoryQueryModel
				.updateBranchInventoryAfterTransfer(inventoryTransferID);
		deleteCompletedInventoryTransfer(inventoryTransferID);
	}
}
