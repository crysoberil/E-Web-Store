package com.ewebstore.model;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.ewebstore.dbutil.DBConnection;
import com.ewebstore.dbutil.DBUtil;

public class BranchQueryModel {

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
}