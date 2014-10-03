package com.ewebstore.model;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

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
		PreparedStatement preparedStatement1 = null;
		PreparedStatement preparedStatement2 = null;
		ResultSet resultSet = null;

		try {
			preparedStatement1 = DBConnection
					.getConnection()
					.prepareStatement(
							"SELECT employeeID FROM SalesEmployee WHERE employeeID = ?");

			preparedStatement1.setLong(1, Long.parseLong(employeeID));

			resultSet = preparedStatement1.executeQuery();

			if (!resultSet.next())
				throw new IllegalArgumentException("No such sales employee");

			preparedStatement2 = DBConnection.getConnection().prepareStatement(
					"DELETE FROM SalesEmployee WHERE employeeID = ?");

			preparedStatement2.setLong(1, Long.parseLong(employeeID));

			preparedStatement2.executeUpdate();

		} catch (SQLException ex) {
			throw ex;
		} finally {
			DBUtil.dispose(resultSet);
			DBUtil.dispose(preparedStatement1);
			DBUtil.dispose(preparedStatement2);
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

	public static String getSalesEmployeeContactNumber(String employeeID)
			throws SQLException {
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;

		try {
			preparedStatement = DBConnection
					.getConnection()
					.prepareStatement(
							"SELECT contactNumber FROM SalesEmployee WHERE employeeID = ?");

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

	public static ArrayList<String> getAvailableSalesEmployeeIDs(String branchID)
			throws SQLException {
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;

		try {
			preparedStatement = DBConnection
					.getConnection()
					.prepareStatement(
							"SELECT employeeID FROM SalesEmployee WHERE branchID = ? AND employeeID NOT IN (SELECT associatedEmployee FROM `Order` WHERE orderStatusID = (SELECT orderStatusID FROM OrderStatus WHERE status = \'Being Delivered\') AND branchID = SalesEmployee.branchID)");

			preparedStatement.setLong(1, Long.parseLong(branchID));

			resultSet = preparedStatement.executeQuery();

			ArrayList<String> availableSalesEmployeeIDs = new ArrayList<String>();

			while (resultSet.next())
				availableSalesEmployeeIDs.add(resultSet.getString(1));

			return availableSalesEmployeeIDs;

		} catch (SQLException ex) {
			System.out.println(ex.getMessage());
			throw ex;
		} finally {
			DBUtil.dispose(preparedStatement);
			DBUtil.dispose(resultSet);
		}
	}

	public static ArrayList<String> getAllSalesEmployeeIDs(String branchID)
			throws SQLException {
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;

		try {
			preparedStatement = DBConnection.getConnection().prepareStatement(
					"SELECT employeeID FROM SalesEmployee WHERE branchID = ?");

			preparedStatement.setLong(1, Long.parseLong(branchID));

			resultSet = preparedStatement.executeQuery();

			ArrayList<String> allSalesEmployeeIDs = new ArrayList<String>();

			while (resultSet.next())
				allSalesEmployeeIDs.add(resultSet.getString(1));

			return allSalesEmployeeIDs;

		} catch (SQLException ex) {
			System.out.println(ex.getMessage());
			throw ex;
		} finally {
			DBUtil.dispose(preparedStatement);
			DBUtil.dispose(resultSet);
		}
	}

	public static boolean isSalesEmployeeAvailable(String employeeID)
			throws SQLException {
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;

		try {
			preparedStatement = DBConnection
					.getConnection()
					.prepareStatement(
							"SELECT employeeID FROM SalesEmployee SE1 WHERE employeeID = ? AND employeeID NOT IN (SELECT associatedEmployee FROM `Order` AS ORD1 WHERE SE1.employeeID = ORD1.associatedEmployee AND orderStatusID = (SELECT orderStatusID FROM OrderStatus WHERE status = \'Being Delivered\'))");
			preparedStatement.setLong(1, Long.valueOf(employeeID));

			resultSet = preparedStatement.executeQuery();

			return resultSet.next();
		} catch (SQLException ex) {
			throw ex;
		} finally {
			DBUtil.dispose(preparedStatement);
			DBUtil.dispose(resultSet);
		}
	}
}
