package com.ewebstore.model;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.ewebstore.dbutil.DBConnection;
import com.ewebstore.dbutil.DBUtil;
import com.ewebstore.entity.Branch;

/**
 * The BranchQueryModel class handles the database operations related to the
 * branches.
 * 
 * @author ewebstore.org
 *
 */
public class BranchQueryModel {

	/**
	 * Returns the branch name corresponding to the provided branch ID
	 * 
	 * @param branchID
	 *            ID of the branch
	 * @return Corresponding branch name
	 * @throws SQLException
	 *             if a database access error occurs
	 */
	public static String getBranchNameByID(String branchID) throws SQLException {
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;

		try {
			preparedStatement = DBConnection.getConnection().prepareStatement(
					"SELECT branchName FROM Branch WHERE branchID = ?");

			preparedStatement.setLong(1, Long.valueOf(branchID));

			resultSet = preparedStatement.executeQuery();

			if (!resultSet.next())
				throw new SQLException("No such branchid");

			return resultSet.getString(1);
		} catch (SQLException ex) {
			throw ex;
		} finally {
			DBUtil.dispose(resultSet);
			DBUtil.dispose(preparedStatement);
		}
	}

	/**
	 * Returns the ID of the branch among the branches that has the product
	 * available corresponding to the provided product ID and is the nearest to
	 * the branch corresponding to the provided branch ID
	 * 
	 * @param branchID
	 *            ID of the branch that requires the product
	 * @param productID
	 *            ID of the required product
	 * @return ID of the branch that is the nearest among the branches with the
	 *         product available
	 * @throws SQLException
	 *             if a database access error occurs
	 */
	public static String getNearestBranchIDWithProductAvailble(String branchID,
			String productID) throws SQLException {
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;

		try {
			preparedStatement = DBConnection
					.getConnection()
					.prepareStatement(
							"SELECT branchID FROM BranchInventory as BI1 WHERE productID = ? AND availableQuantity <> 0 ORDER BY (SELECT distance FROM DistrictDistance WHERE district1ID = (SELECT branchDistrict FROM Branch WHERE branchID = BI1.branchID) AND district2ID = (SELECT branchDistrict FROM Branch WHERE branchID = ?)) LIMIT 1");
			preparedStatement.setLong(1, Long.valueOf(productID));
			preparedStatement.setLong(2, Long.valueOf(branchID));

			resultSet = preparedStatement.executeQuery();

			if (!resultSet.next())
				throw new SQLException();

			return Long.toString(resultSet.getLong(1));

		} catch (SQLException ex) {
			throw ex;
		} finally {
			DBUtil.dispose(preparedStatement);
			DBUtil.dispose(resultSet);
		}
	}

	/**
	 * Updates the database by removing the product corresponding to the
	 * provided product ID to the provided quantity
	 * 
	 * @param supplierBranchID
	 *            ID of the branch supplying the product
	 * @param productID
	 *            ID of the product to be removed
	 * @param toSupplyQuantity
	 *            Quantity of the product to be removed
	 * @throws SQLException
	 *             if a database access error occurs
	 */
	public static void removeAvailableProduct(String supplierBranchID,
			String productID, int toSupplyQuantity) throws SQLException {
		PreparedStatement preparedStatement = null;

		try {
			preparedStatement = DBConnection
					.getConnection()
					.prepareStatement(
							"UPDATE BranchInventory SET availableQuantity = availableQuantity - ? WHERE branchID = ? AND productID = ?");

			preparedStatement.setLong(1, toSupplyQuantity);
			preparedStatement.setLong(2, Long.valueOf(supplierBranchID));
			preparedStatement.setLong(3, Long.valueOf(productID));

			preparedStatement.executeUpdate();

		} catch (SQLException ex) {
			throw ex;
		} finally {
			DBUtil.dispose(preparedStatement);
		}
	}

	/**
	 * Returns the ID of the branch corresponding to the provided branch manager
	 * ID
	 * 
	 * @param managerID
	 *            ID of the branch manager
	 * @return Corresponding branch ID
	 * @throws SQLException
	 *             if a database access error occurs
	 */
	public static String getBranchID(String managerID) throws SQLException {
		PreparedStatement statement = null;
		ResultSet resultSet = null;

		try {
			statement = DBConnection.getConnection().prepareStatement(
					"SELECT branchID FROM BranchManager WHERE managerID = ?");

			statement.setLong(1, Long.valueOf(managerID));

			resultSet = statement.executeQuery();

			if (!resultSet.next())
				throw new SQLException("No such branch manager");

			return Long.toString(resultSet.getLong(1));

		} catch (SQLException ex) {
			throw ex;
		} finally {
			DBUtil.dispose(resultSet);
			DBUtil.dispose(statement);
		}
	}

	/**
	 * Returns the number of the completed deliveries between the last
	 * 'numberOfDays' from the branch corresponding to the provided branch ID
	 * 
	 * @param branchID
	 *            ID of the branch
	 * @param numberOfDays
	 *            Interval of days
	 * @return Number of successful deliveries between the interval
	 * @throws SQLException
	 *             if a database access error occurs
	 */
	public static int numberOfCompletedDeliveries(String branchID,
			int numberOfDays) throws SQLException {
		PreparedStatement statement = null;
		ResultSet resultSet = null;

		try {
			statement = DBConnection
					.getConnection()
					.prepareStatement(
							"SELECT COUNT(*) FROM `Order` as ORD1 WHERE branchID = ? AND orderStatusID = ? AND orderDate BETWEEN DATE_SUB(NOW(), INTERVAL ? DAY) AND NOW()");

			statement.setLong(1, Long.valueOf(branchID));
			statement.setInt(2, Integer.valueOf(OrderQueryModel
					.getOrderStatusIDByStatus("Delivered")));
			statement.setInt(3, numberOfDays);

			resultSet = statement.executeQuery();

			if (!resultSet.next())
				throw new SQLException();

			int count = resultSet.getInt(1);

			return count;

		} catch (SQLException ex) {
			throw ex;
		} finally {
			DBUtil.dispose(resultSet);
			DBUtil.dispose(statement);
		}
	}

	/**
	 * Returns the total transaction done over the last 'numberOfDays' in the
	 * branch corresponding to the provided branch ID
	 * 
	 * @param branchID
	 *            ID of the branch
	 * @param numberOfDays
	 *            Interval of days
	 * @return Total transaction done between the interval
	 * @throws SQLException
	 *             if a database access error occurs
	 */
	public static double totalTransaction(String branchID, int numberOfDays)
			throws SQLException {
		PreparedStatement statement = null;
		ResultSet resultSet = null;

		try {
			statement = DBConnection
					.getConnection()
					.prepareStatement(
							"SELECT COALESCE(SUM(totalOrderingCost), 0) FROM `Order` as ORD1 WHERE branchID = ? AND orderStatusID = ? AND orderDate BETWEEN DATE_SUB(NOW(), INTERVAL ? DAY) AND NOW()");

			statement.setLong(1, Long.valueOf(branchID));
			statement.setInt(2, Integer.valueOf(OrderQueryModel
					.getOrderStatusIDByStatus("Delivered")));
			statement.setInt(3, numberOfDays);

			resultSet = statement.executeQuery();

			if (!resultSet.next())
				throw new SQLException();

			double totalTransaction = resultSet.getInt(1);

			return totalTransaction;

		} catch (SQLException ex) {
			throw ex;
		} finally {
			DBUtil.dispose(resultSet);
			DBUtil.dispose(statement);
		}
	}

	/**
	 * Returns the branch information corresponding to the provided branch ID
	 * 
	 * @param branchID
	 *            ID of the branch
	 * @return Corresponding branch information
	 * @throws SQLException
	 *             if a database access error occurs
	 */
	public static Branch getBranch(String branchID) throws SQLException {
		PreparedStatement statement = null;
		ResultSet resultSet = null;

		try {
			statement = DBConnection
					.getConnection()
					.prepareStatement(
							"SELECT branchName, branchLocation, branchDistrict, (SELECT districtName FROM District WHERE District.districtID = Branch.branchDistrict) FROM Branch WHERE branchID = ?");

			statement.setLong(1, Long.valueOf(branchID));

			resultSet = statement.executeQuery();

			if (!resultSet.next())
				throw new SQLException();

			String branchName = resultSet.getString(1);
			String branchLocation = resultSet.getString(2);
			String branchDistrictID = Long.toString(resultSet.getLong(3));
			String branchDistrictName = DistrictQueryModel
					.capitalizedDistrictName(resultSet.getString(4));

			return new Branch(branchID, branchName, branchLocation,
					branchDistrictID, branchDistrictName);

		} catch (SQLException ex) {
			throw ex;
		} finally {
			DBUtil.dispose(resultSet);
			DBUtil.dispose(statement);
		}
	}
}