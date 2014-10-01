package com.ewebstore.model;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.ewebstore.dbutil.DBConnection;
import com.ewebstore.dbutil.DBUtil;
import com.ewebstore.entity.BranchManager;

public class BranchManagerQueryModel {
	public static String getManagerId(String email, String password)
			throws SQLException {

		PreparedStatement statement = null;
		ResultSet resultSet = null;

		try {

			statement = DBConnection
					.getConnection()
					.prepareStatement(
							"SELECT managerID FROM BranchManager WHERE lower(email) = ? AND password = ?");

			statement.setString(1, email);
			statement.setString(2, password);

			resultSet = statement.executeQuery();

			if (!resultSet.next())
				throw new SQLException("Invalid branch manager");

			return Long.toString(resultSet.getLong(1));

		} catch (SQLException ex) {
			throw ex;
		} finally {
			DBUtil.dispose(resultSet);
			DBUtil.dispose(statement);
		}
	}

	public static String getManagerName(String managerID) {
		PreparedStatement statement = null;
		ResultSet resultSet = null;

		try {

			statement = DBConnection.getConnection().prepareStatement(
					"SELECT name FROM BranchManager WHERE managerID = ?");

			statement.setLong(1, Long.valueOf(managerID));

			resultSet = statement.executeQuery();

			if (!resultSet.next())
				throw new SQLException("No such branch manager");

			return resultSet.getString(1);

		} catch (SQLException ex) {
			return "invalid manager";
		} finally {
			DBUtil.dispose(resultSet);
			DBUtil.dispose(statement);

		}
	}

	public static String getBranchID(String managerID) {
		PreparedStatement statement = null;
		ResultSet resultSet = null;

		try {
			statement = DBConnection.getConnection().prepareStatement(
					"SELECT branchID FROM BranchManager WHERE managerID = ?");

			statement.setLong(1, Long.valueOf(managerID));

			resultSet = statement.executeQuery();

			if (!resultSet.next())
				throw new SQLException("No such branch manager");

			String branchID = Long.toString(resultSet.getLong(1));

			return branchID;
		} catch (SQLException ex) {
			ex.printStackTrace();
			return null;
		} finally {
			DBUtil.dispose(resultSet);
			DBUtil.dispose(statement);
		}
	}

	public static BranchManager getBranchManager(String managerID) {
		PreparedStatement statement = null;
		ResultSet resultSet = null;

		try {
			statement = DBConnection.getConnection().prepareStatement(
					"SELECT * FROM BranchManager WHERE managerID = ?");

			statement.setLong(1, Long.valueOf(managerID));

			resultSet = statement.executeQuery();

			if (!resultSet.next())
				throw new SQLException("No such branch manager");

			String branchID = Long.toString(resultSet.getLong(1));
			String managerName = resultSet.getString(2);
			boolean gender = resultSet.getInt(3) == 1;
			String email = resultSet.getString(4);
			String address = resultSet.getString(5);
			Date dob = resultSet.getDate(6);
			String contactNumber = resultSet.getString(7);
			String branchName = BranchQueryModel.getBranchNameByID(branchID);

			return new BranchManager(managerID, branchID, branchName,
					managerName, gender, email, address, dob, contactNumber);

		} catch (SQLException ex) {
			ex.printStackTrace();
			return null;
		} finally {
			DBUtil.dispose(resultSet);
			DBUtil.dispose(statement);
		}
	}
}
