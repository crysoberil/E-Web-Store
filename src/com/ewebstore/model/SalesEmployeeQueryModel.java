package com.ewebstore.model;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.ewebstore.dbutil.DBConnection;
import com.ewebstore.dbutil.DBUtil;

public class SalesEmployeeQueryModel {
	public static void addSalesEmployee(String name, boolean gender,
			String email, String contactnumber, Date dob, String address,
			String branchID, String branchName) throws SQLException {

		PreparedStatement preparedStatement = null;

		try {
			String sqlString = "INSERT INTO SalesEmployee VALUES(NULL, ?, ?, ?, ?, ?, CURDATE(), ?, ?)";

			preparedStatement = DBConnection.getConnection().prepareStatement(
					sqlString);

			preparedStatement.setString(1, name);
			preparedStatement.setBoolean(2, gender);
			preparedStatement.setString(3, email);
			preparedStatement.setString(4, contactnumber);
			preparedStatement.setDate(5, dob);
			preparedStatement.setString(6, address);
			preparedStatement.setLong(7, Long.valueOf(branchID));

			preparedStatement.executeUpdate();
		} catch (SQLException ex) {
			throw ex;
		} finally {
			DBUtil.dispose(preparedStatement);
		}
	}
}
