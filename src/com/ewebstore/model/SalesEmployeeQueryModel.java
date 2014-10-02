package com.ewebstore.model;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.ewebstore.dbutil.DBConnection;
import com.ewebstore.dbutil.DBUtil;
import com.ewebstore.entity.SalesEmployee;

public class SalesEmployeeQueryModel {
	public static void addSalesEmployee(String name, boolean gender,
			String email, String contactNumber, Date dob, String address,
			String branchID) throws SQLException {

		PreparedStatement preparedStatement = null;

		try {
			String sqlString = "INSERT INTO SalesEmployee VALUES(NULL, ?, ?, ?, ?, CURDATE(), CURDATE(), ?, ?)";

			preparedStatement = DBConnection.getConnection().prepareStatement(
					sqlString);

			preparedStatement.setString(1, name);
			preparedStatement.setInt(2, gender ? 1 : 0);
			preparedStatement.setString(3, email);
			preparedStatement.setString(4, contactNumber);
			// preparedStatement.setDate(5, dob);
			preparedStatement.setString(5, address);
			preparedStatement.setLong(6, Long.valueOf(branchID));

			preparedStatement.executeUpdate();
		} catch (SQLException ex) {
			throw ex;
		} finally {
			DBUtil.dispose(preparedStatement);
		}
	}

	public static SalesEmployee getSalesEmployee(String employeeID)
			throws SQLException {
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;

		preparedStatement = DBConnection.getConnection().prepareStatement(
				"SELECT * FROM SalesEmployee WHERE employeeID = ?");

		preparedStatement.setLong(1, Long.parseLong(employeeID));

		resultSet = preparedStatement.executeQuery();

		if (!resultSet.next())
			throw new IllegalArgumentException("No such sales employee");

		String name = resultSet.getString(2);
		boolean gender = (resultSet.getInt(3) == 1);
		String email = resultSet.getString(4);
		String contactNumber = resultSet.getString(5);
		Date dob = resultSet.getDate(6);
		Date joinDate = resultSet.getDate(7);
		String address = resultSet.getString(8);
		String branchID = Long.toString(resultSet.getLong(9));
		String branchName = BranchQueryModel.getBranchNameByID(branchID);

		return new SalesEmployee(employeeID, name, gender, email,
				contactNumber, dob, joinDate, address, branchID, branchName);
	}

	public static void updateSalesEmployee(String employeeID, String name,
			boolean gender, String email, String contactNumber, Date dob,
			String address) throws SQLException {

		PreparedStatement preparedStatement = null;

		try {
			preparedStatement = DBConnection
					.getConnection()
					.prepareStatement(
							"UPDATE SalesEmployee SET name = ?, gender = ?, email = ?, contactNumber = ?, dob = CURDATE(), address = ? WHERE employeeID = ?");

			preparedStatement.setString(1, name);
			preparedStatement.setInt(2, gender ? 1 : 0);
			preparedStatement.setString(3, email);
			preparedStatement.setString(4, contactNumber);
			preparedStatement.setString(5, address);
			preparedStatement.setLong(6, Long.parseLong(employeeID));

			preparedStatement.executeUpdate();
		} catch (SQLException ex) {
			throw ex;
		} finally {
			DBUtil.dispose(preparedStatement);
		}
	}

	public static void removeSalesEmployee(String employeeID)
			throws SQLException {
		PreparedStatement statement = null;
		ResultSet resultSet = null;

		try {
			statement = DBConnection
					.getConnection()
					.prepareStatement(
							"SELECT employeeID FROM SalesEmployee WHERE employeeID = ?");

			statement.setLong(1, Long.parseLong(employeeID));

			resultSet = statement.executeQuery();

			if (!resultSet.next())
				throw new IllegalArgumentException("No such sales employee");

			statement = DBConnection.getConnection().prepareStatement(
					"DELETE FROM SalesEmployee WHERE employeeID = ?");

			statement.setLong(1, Long.parseLong(employeeID));

			statement.executeUpdate();

		} catch (SQLException ex) {
			throw ex;
		} finally {
			DBUtil.dispose(resultSet);
			DBUtil.dispose(statement);
		}
	}
	
	public static String getSalesEmployeeName(String employeeID)
			throws SQLException {
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;

		try {
			preparedStatement = DBConnection.getConnection().prepareStatement(
					"SELECT name FROM SalesEmployee WHERE employeeID = ?");

			preparedStatement.setLong(1, Long.valueOf(employeeID));

			resultSet = preparedStatement.executeQuery();

			if (!resultSet.next())
				throw new SQLException();

			return resultSet.getString(1);

		} catch (SQLException ex) {
			throw ex;
		} finally {
			DBUtil.dispose(preparedStatement);
			DBUtil.dispose(resultSet);
		}
	}
}
