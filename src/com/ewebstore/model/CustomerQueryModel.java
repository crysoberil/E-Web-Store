package com.ewebstore.model;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.ewebstore.dbutil.DBConnection;
import com.ewebstore.dbutil.DBUtil;
import com.ewebstore.entity.Customer;

/**
 * The CustomerQueryModel class handles the database operations related to the
 * customers.
 * 
 * @author ewebstore.org
 *
 */
public class CustomerQueryModel {

	public static final double DISCOUNTPERCENTAGE = 5;

	/**
	 * Returns the ID of the customer corresponding to the provided email and
	 * password
	 * 
	 * @param email
	 *            Email address of the customer
	 * @param password
	 *            Password of the customer
	 * @return ID of the customer
	 * @throws SQLException
	 *             if a database access error occurs
	 */
	public static String getCustomerID(String email, String password)
			throws SQLException {

		PreparedStatement statement = null;
		ResultSet resultSet = null;

		try {
			statement = DBConnection
					.getConnection()
					.prepareStatement(
							"SELECT customerID FROM Customer WHERE LOWER(email) = ? AND password = ?");

			statement.setString(1, email);
			statement.setString(2, password);

			resultSet = statement.executeQuery();

			if (!resultSet.next())
				throw new SQLException("no such customer");

			return Long.toString(resultSet.getLong(1));

		} catch (SQLException ex) {
			throw ex;
		} finally {
			DBUtil.dispose(resultSet);
			DBUtil.dispose(statement);
		}
	}

	/**
	 * Returns the name of the customer corresponding to the provided customer
	 * ID
	 * 
	 * @param customerID
	 *            ID of the customer
	 * @return Name of the customer
	 * @throws SQLException
	 *             if a database access error occurs
	 */
	public static String getCustomerName(String customerID) throws SQLException {
		PreparedStatement statement = null;
		ResultSet resultSet = null;

		try {

			statement = DBConnection.getConnection().prepareStatement(
					"SELECT name FROM Customer WHERE customerID = ?");

			statement.setLong(1, Long.valueOf(customerID));

			resultSet = statement.executeQuery();

			if (!resultSet.next())
				throw new SQLException("no such customerID");

			return resultSet.getString(1);

		} catch (SQLException ex) {
			throw ex;
		} finally {
			DBUtil.dispose(resultSet);
			DBUtil.dispose(statement);
		}
	}

	/**
	 * Checks if the customer corresponding to the provided customer ID is a
	 * premium customer
	 * 
	 * @param customerID
	 *            ID of the customer
	 * @return true if the customer is a premium customer; false otherwise.
	 */
	public static boolean isPremiumUser(String customerID) {
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;

		try {
			preparedStatement = DBConnection
					.getConnection()
					.prepareStatement(
							"SELECT premiumCustomer FROM Customer WHERE customerID = ?");

			preparedStatement.setLong(1, Long.valueOf(customerID));

			resultSet = preparedStatement.executeQuery();

			if (!resultSet.next())
				return false;

			return resultSet.getInt(1) == 1;
		} catch (SQLException ex) {
			return false;
		} finally {
			DBUtil.dispose(resultSet);
			DBUtil.dispose(preparedStatement);
		}
	}

	/**
	 * Updates database by inserting a new customer record
	 * 
	 * @param customerName
	 *            Name of the customer
	 * @param email
	 *            Email of the customer
	 * @param password
	 *            Password of the customer
	 * @param isMale
	 *            Gender of the customer (true if male, false if female)
	 * @param dob
	 *            Date of birth of the customer
	 * @param address
	 *            Address of the customer
	 * @param contactNumber
	 *            Contact number of the customer
	 * @throws SQLException
	 *             if a database access error occurs
	 */
	public static void addCustomer(String customerName, String email,
			String password, boolean isMale, Date dob, String address,
			String contactNumber) throws SQLException {
		PreparedStatement statement = null;

		try {
			statement = DBConnection
					.getConnection()
					.prepareStatement(
							"INSERT INTO Customer VALUES(NULL, ?, ?, ?, ?, ?, ?, CURDATE(), 0, 0, ?)");

			statement.setString(1, customerName);
			statement.setDate(2, dob);
			statement.setBoolean(3, isMale);
			statement.setString(4, email);
			statement.setString(5, address);
			statement.setString(6, contactNumber);
			statement.setString(7, password);

			if (statement.executeUpdate() == 0)
				throw new SQLException();
		} catch (SQLException ex) {
			throw ex;
		} finally {
			DBUtil.dispose(statement);
		}
	}

	/**
	 * Returns information on a customer corresponding to the provided customer
	 * ID
	 * 
	 * @param customerID
	 *            ID of the customer
	 * @return Information on the customer
	 * @throws SQLException
	 *             if a database access error occurs
	 */
	public static Customer getCustomer(String customerID) throws SQLException {
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;

		try {
			preparedStatement = DBConnection
					.getConnection()
					.prepareStatement(
							"SELECT name, dob, gender, email, address, contactNumber, registrationDate, premiumCustomer FROM Customer WHERE customerID = ?");
			preparedStatement.setLong(1, Long.parseLong(customerID));

			resultSet = preparedStatement.executeQuery();

			if (!resultSet.next())
				throw new IllegalArgumentException("No such customer");

			String name = resultSet.getString(1);
			Date dob = resultSet.getDate(2);
			boolean isMale = (resultSet.getInt(3) == 1);
			String email = resultSet.getString(4);
			String address = resultSet.getString(5);
			String contactNumber = resultSet.getString(6);
			Date registrationDate = resultSet.getDate(7);
			boolean isPremiumCustomer = (resultSet.getInt(8) == 1);

			return new Customer(customerID, name, dob, isMale, email, address,
					contactNumber, registrationDate, isPremiumCustomer);

		} catch (SQLException ex) {
			throw ex;
		} finally {
			DBUtil.dispose(resultSet);
			DBUtil.dispose(preparedStatement);
		}
	}

	// public static boolean createNewUser(String userName, String name,
	// String password, Date dob, String sex, String email)
	// throws SQLException {
	//
	// CallableStatement statement = null;
	//
	// try {
	//
	// long userID = RandomIDGenerator.getRandomAvailableID("USRINFO",
	// "usrid");
	//
	// statement = DBConnectionManager.getConnection().prepareCall(
	// "{ CALL INSERTUSERINFO(?, ?, 0, ?, ?, ?, ?, ?) }");
	//
	// statement.setLong(1, userID);
	// statement.setString(2, userName);
	// statement.setString(3, name);
	// statement.setString(4, email);
	// statement.setString(5, password);
	// statement.setDate(6, new java.sql.Date(dob.getTime()));
	// statement.setString(7, sex);
	//
	// return statement.executeUpdate() == 1;
	//
	// } catch (SQLException ex) {
	// throw ex;
	// } finally {
	// if (statement != null)
	// statement.close();
	// }
	//
	// }
	//
	// public static boolean emailAvailableForNewUser(String email) {
	//
	// if (email == null || !validEmailAddress(email))
	// return false;
	//
	// try {
	//
	// PreparedStatement statement = null;
	// ResultSet resultSet = null;
	//
	// try {
	//
	// statement = DBConnectionManager.getConnection()
	// .prepareStatement(
	// "SELECT usrid FROM USRINFO WHERE emailid = ?");
	//
	// statement.setString(1, email);
	//
	// resultSet = statement.executeQuery();
	//
	// if (resultSet.next())
	// return false;
	//
	// return true;
	//
	// } catch (SQLException ex) {
	// throw ex;
	// } finally {
	// if (resultSet != null)
	// resultSet.close();
	// if (statement != null)
	// statement.close();
	// }
	//
	// } catch (SQLException ex) {
	// ex.printStackTrace();
	// return false;
	// }
	// }
	//
	// public static boolean validEmailAddress(String email) {
	// return email.toLowerCase().matches("[a-z0-9_.]+@[a-z0-9_]+.[a-z0-9_]+");
	// }
	//
	// public static boolean userNameAvailable(String userName) {
	//
	// if (userName == null || userName.length() < 5)
	// return false;
	//
	// try {
	//
	// PreparedStatement statement = null;
	// ResultSet resultSet = null;
	//
	// try {
	//
	// statement = DBConnectionManager.getConnection()
	// .prepareStatement(
	// "SELECT usrid FROM USRINFO WHERE username = ?");
	// statement.setString(1, userName);
	// resultSet = statement.executeQuery();
	//
	// return !resultSet.next();
	//
	// } catch (SQLException ex) {
	// throw ex;
	// } finally {
	// if (statement != null)
	// statement.close();
	// }
	//
	// } catch (SQLException ex) {
	// ex.printStackTrace();
	// return false;
	// }
	// }
	//
	// public static boolean getAdminStatus(long userID) throws SQLException {
	//
	// PreparedStatement statement = null;
	// ResultSet resultSet = null;
	//
	// try {
	//
	// statement = DBConnectionManager.getConnection().prepareStatement(
	// "SELECT isadmin FROM USRINFO WHERE usrid = " + userID);
	//
	// resultSet = statement.executeQuery();
	//
	// resultSet.next();
	//
	// return resultSet.getInt(1) == 1;
	//
	// } catch (SQLException ex) {
	// throw ex;
	// } finally {
	// if (statement != null)
	// statement.close();
	// if (resultSet != null)
	// resultSet.close();
	// }
	// }
}
