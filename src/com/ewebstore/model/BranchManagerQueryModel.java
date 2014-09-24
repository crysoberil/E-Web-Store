package com.ewebstore.model;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.ewebstore.dbutil.DBConnection;
import com.ewebstore.dbutil.DBUtil;

public class BranchManagerQueryModel {
	public static String getManagerId(String email, String password)
			throws SQLException {

		PreparedStatement statement = null;
		ResultSet resultSet = null;

		try {

			statement = DBConnection
					.getSharedConnection()
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

			statement = DBConnection.getSharedConnection().prepareStatement(
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
}
