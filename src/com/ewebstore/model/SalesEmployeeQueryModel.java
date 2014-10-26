package com.ewebstore.model;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.ewebstore.dbutil.DBConnection;
import com.ewebstore.dbutil.DBUtil;
import com.ewebstore.entity.SalesEmployee;

/**
 * The SalesEmployeeQueryModel class handles the database operations related to
 * the sales employees.
 * 
 * @author ewebstore.org
 *
 */
public class SalesEmployeeQueryModel {

	/**
	 * Updates database by inserting a new sales employee
	 * 
	 * @param name
	 *            Name of the employee
	 * @param gender
	 *            Gender of the employee (true if male; false if female)
	 * @param email
	 *            Email address of the employee
	 * @param contactNumber
	 *            Contact no. of the employee
	 * @param dob
	 *            Date of birth of the employee
	 * @param address
	 *            Address of the employee
	 * @param branchID
	 *            Branch ID of the employee
	 * @throws SQLException
	 *             if a database access error occurs
	 */
	public static void addSalesEmployee(String name, boolean gender,
			String email, String contactNumber, Date dob, String address,
			String branchID) throws SQLException {

		PreparedStatement preparedStatement = null;

		try {
			String sqlString = "INSERT INTO SalesEmployee VALUES(NULL, ?, ?, ?, ?, ?, CURDATE(), ?, ?, 1)";

			preparedStatement = DBConnection.getConnection().prepareStatement(
					sqlString);

			preparedStatement.setString(1, name);
			preparedStatement.setInt(2, gender ? 1 : 0);
			preparedStatement.setString(3, email);
			preparedStatement.setString(4, contactNumber);
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

	/**
	 * Returns information on the sales employee corresponding to the provided
	 * employee ID
	 * 
	 * @param employeeID
	 *            ID of the sales employee
	 * @return Information on the sales employee
	 * @throws SQLException
	 *             if a database access error occurs
	 */
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
		Boolean currentlyEmployed = (resultSet.getInt(10) == 1 ? true : false);

		return new SalesEmployee(employeeID, name, gender, email,
				contactNumber, dob, joinDate, address, branchID, branchName,
				currentlyEmployed);
	}

	/**
	 * Updates database by modifying the record of the sales employee
	 * corresponding to the provided sales employee
	 * 
	 * @param employeeID
	 *            ID of the employee
	 * @param name
	 *            Name of the employee
	 * @param gender
	 *            Gender of the employee (true if male; false if female)
	 * @param email
	 *            Email address of the employee
	 * @param contactNumber
	 *            Contact no. of the employee
	 * @param dob
	 *            Date of birth of the employee
	 * @param address
	 *            Address of the employee
	 * @throws SQLException
	 *             if a database access error occurs
	 */
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

	/**
	 * Updates database by deleting the record of the sales employee
	 * corresponding to the provided employee ID
	 * 
	 * @param employeeID
	 *            ID of the sales employee
	 * @throws SQLException
	 *             if a database access error occurs
	 */
	public static void removeSalesEmployee(String employeeID)
			throws SQLException {
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;

		try {
			preparedStatement = DBConnection
					.getConnection()
					.prepareStatement(
							"UPDATE SalesEmployee SET currentlyEmployed = 0 WHERE employeeID = ?");

			preparedStatement.setLong(1, Long.parseLong(employeeID));

			preparedStatement.executeUpdate();

		} catch (SQLException ex) {
			throw ex;
		} finally {
			DBUtil.dispose(resultSet);
			DBUtil.dispose(preparedStatement);
		}
	}

	/**
	 * Checks if the sales employee corresponding to the provided employee ID is
	 * currently employed or not
	 * 
	 * @param employeeID
	 *            ID of the sales employee
	 * @return true if the sales employee is currently employed; false otherwise
	 * @throws SQLException
	 *             if a database access error occurs
	 */
	public static boolean isCurrentlyEmployed(String employeeID)
			throws SQLException {
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;

		try {
			preparedStatement = DBConnection
					.getConnection()
					.prepareStatement(
							"SELECT currentlyEmployed FROM SalesEmployee WHERE employeeID = ? ");

			preparedStatement.setLong(1, Long.parseLong(employeeID));

			resultSet = preparedStatement.executeQuery();

			if (!resultSet.next())
				throw new IllegalArgumentException("No such sales employee");

			return (resultSet.getInt(1) == 1);
		} catch (SQLException ex) {
			throw ex;
		} finally {
			DBUtil.dispose(preparedStatement);
			DBUtil.dispose(resultSet);
		}
	}

	/**
	 * Returns the name of the sales employee corresponding to the provided
	 * employee ID
	 * 
	 * @param employeeID
	 *            ID of the sales employee
	 * @return Name of sales employee
	 * @throws SQLException
	 *             if a database access error occurs
	 */
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

	/**
	 * Returns the contact number of the sales employee corresponding to the
	 * provided employee ID
	 * 
	 * @param employeeID
	 *            ID of the sales employee
	 * @return Contact no. of the sales employee
	 * @throws SQLException
	 *             if a database access error occurs
	 */
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

	/**
	 * Returns a list of sales employee IDs of employees who are currently
	 * available for delivery and are employed to the branch corresponding to
	 * the provided branch ID
	 * 
	 * @param branchID
	 *            ID of the branch
	 * @return List of sales employee IDs
	 * @throws SQLException
	 *             if a database access error occurs
	 */
	public static ArrayList<String> getAvailableSalesEmployeeIDs(String branchID)
			throws SQLException {
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;

		try {
			preparedStatement = DBConnection
					.getConnection()
					.prepareStatement(
							"SELECT employeeID FROM SalesEmployee WHERE branchID = ? AND currentlyEmployed = 1 AND employeeID NOT IN (SELECT associatedEmployee FROM `Order` WHERE orderStatusID = (SELECT orderStatusID FROM OrderStatus WHERE status = \'Being Delivered\') AND branchID = SalesEmployee.branchID)");

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

	/**
	 * Returns a list of sales employee IDs of employees who are employed to the
	 * branch corresponding to the provided branch ID
	 * 
	 * @param branchID
	 *            ID of the branch
	 * @return List of sales employee IDs
	 * @throws SQLException
	 *             if a database access error occurs
	 */
	public static ArrayList<String> getAllSalesEmployeeIDs(String branchID)
			throws SQLException {
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;

		try {
			preparedStatement = DBConnection
					.getConnection()
					.prepareStatement(
							"SELECT employeeID FROM SalesEmployee WHERE branchID = ? AND currentlyEmployed = 1");

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

	/**
	 * Checks if the sales employee corresponding to the provided employee ID is
	 * currently available for delivery
	 * 
	 * @param employeeID
	 *            ID of the sales employee
	 * @return true if the sales employee is available; false otherwise
	 * @throws SQLException
	 *             if a database access error occurs
	 */
	public static boolean isSalesEmployeeAvailable(String employeeID)
			throws SQLException {
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;

		try {
			preparedStatement = DBConnection
					.getConnection()
					.prepareStatement(
							"SELECT employeeID FROM SalesEmployee SE1 WHERE employeeID = ? AND employeeID NOT IN (SELECT associatedEmployee FROM `Order` AS ORD1 WHERE SE1.employeeID = ORD1.associatedEmployee AND orderStatusID = (SELECT orderStatusID FROM OrderStatus WHERE status = \'Being Delivered\')) AND currentlyEmployed = 1");
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
