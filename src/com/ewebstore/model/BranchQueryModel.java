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
			preparedStatement = DBConnection.getSharedConnection()
					.prepareStatement(
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
}
