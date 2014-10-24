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

/**
 * This class manages database query model for inter-branch inventory transfer
 * 
 * @author ewebstore.com
 *
 */
public class BranchInventoryTransferModel {
	/**
	 * An integer representing yet to be transferred inventory transfer status
	 */
	private static final int TOBETRANSFERREDSTATUS = 1;

	/**
	 * An integer representing currently being transferred inventory transfer
	 * status
	 */
	private static final int BEINGTRANSFERREDSTATUS = 2;

	/**
	 * Generates inventory transfer requests and updates branch inventory
	 * information according to optimal distribution of order products between
	 * branches
	 * 
	 * @param cart
	 *            Shopping cart
	 * @param targetBranchID
	 *            ID of branch associated with delivery for this order
	 * @throws SQLException
	 *             When {@code targetBranchID} is invalid
	 */
	public static void distributeOrderBetweenBranches(ShoppingCart cart,
			String targetBranchID) throws SQLException {
		for (CartItem cartItem : cart.getCartItems())
			distributeCartItemBetweenBranches(cartItem, targetBranchID);
	}

	/**
	 * Optimally distributes cart items between branches
	 * 
	 * @param cartItem
	 *            The shopping cart item
	 * @param targetBranchID
	 *            ID of branch associated with delivery for this order
	 * @throws SQLException
	 *             When {@code targetBranchID} is invalid
	 */
	private static void distributeCartItemBetweenBranches(CartItem cartItem,
			String targetBranchID) throws SQLException {
		int quantity = cartItem.getQuantity();

		while (quantity != 0) {
			int assignedQuantity = assignSomeBranchForItemSupply(
					cartItem.getProductID(), quantity, targetBranchID);
			quantity -= assignedQuantity;
		}
	}

	/**
	 * Optimally assigns a branch for supplying a product
	 * 
	 * @param productID
	 *            Traget product
	 * @param maxQuantity
	 *            Maximum amount of the product that this branch needs to supply
	 * @param targetBranchID
	 *            ID of branch associated with delivery for this order
	 * @return Assigned quantity of the product
	 * @throws SQLException
	 *             When {@code targetBranchID} or {@code productID} is invalid
	 */
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

	/**
	 * Adds or updates(in case record already exists) a branch transfer request
	 * 
	 * @param supplierBranchID
	 *            ID of supplier branch
	 * @param targetBranchID
	 *            ID of receiver branch
	 * @param productID
	 *            Product ID
	 * @param toSupplyQuantity
	 *            Quantity of the product to be transferred
	 * @throws SQLException
	 *             When any parameter is inexistent in the database
	 */
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

	/**
	 * Checks if such branch transfer record already exists
	 * 
	 * @param supplierBranchID
	 *            ID of supplier branch
	 * @param targetBranchID
	 *            ID of receiver branch
	 * @param productID
	 *            Product ID
	 * @return {@code true} if such record exists
	 * @throws SQLException
	 *             When any parameter is inexistent in the database
	 */
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

	/**
	 * This method updates database accordingly to reflect a inventory transfer
	 * completion
	 * 
	 * @param inventoryTransferID
	 *            Inventory transfer ID
	 * @throws SQLException
	 *             When no such inventory transfer record exists
	 */
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

	/**
	 * Deletes record for completed inventory transfer
	 * 
	 * @param inventoryTransferID
	 *            Inventory transfer ID
	 * @throws SQLException
	 *             When no such inventory transfer record exists
	 */
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

	/**
	 * Marks an inventory transfer as ongoing
	 * 
	 * @param inventoryTransferID
	 *            Inventory transfer ID
	 * @throws SQLException
	 *             For invalid inventory transfer ID
	 */
	private static void markAsOngoingInventoryTransfer(
			String inventoryTransferID) throws SQLException {
		updateInventoryTransferStatus(inventoryTransferID,
				BEINGTRANSFERREDSTATUS);
	}

	/**
	 * Updates inventory transfer status according to {@code targetStatus}
	 * 
	 * @param inventoryTransferID
	 *            Inventory transfer ID
	 * @param targetStatus
	 *            Target status
	 * @throws SQLException
	 *             For invalid inventory transfer ID
	 */
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

	/**
	 * Returns a list of to transfer to other branches inventory transfers
	 * 
	 * @param branchID
	 *            Source branch ID
	 * @return List of {@code ProductTransferEntity} entities
	 */
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

	/**
	 * Updates database when an inventory transfer is dispatched
	 * 
	 * @param inventoryTransferID
	 *            Inventory transfer ID
	 * @throws SQLException
	 *             For invalid inventory transfer ID
	 */
	public static void dispatchInventoryTransfer(String inventoryTransferID)
			throws SQLException {
		// get product out of stock
		markAsOngoingInventoryTransfer(inventoryTransferID);
		BranchInventoryQueryModel
				.withdrawProductsForTransfer(inventoryTransferID);
	}

	/**
	 * Returns a list of to receive transfers from other branches
	 * 
	 * @param branchID
	 *            Inventory transfer destination branch ID
	 * @return List of {@code ProductTransferEntity} entities
	 */
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

	/**
	 * Updates database according when received an inventory transfer
	 * 
	 * @param inventoryTransferID
	 *            Inventory transfer ID
	 * @throws SQLException
	 *             For invalid inventory transfer ID
	 */
	public static void receiveInventoryTransfer(String inventoryTransferID)
			throws SQLException {
		// add to stock and NOT availability, then remove entry
		BranchInventoryQueryModel
				.updateBranchInventoryAfterTransfer(inventoryTransferID);
		deleteCompletedInventoryTransfer(inventoryTransferID);
	}
}
