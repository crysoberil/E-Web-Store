package com.ewebstore.model;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.ewebstore.dbutil.DBConnection;
import com.ewebstore.dbutil.DBUtil;

public class CustomerQueryModel {

	public static final double DISCOUNTPERCENTAGE = 5;

	public static String getCustomerID(String email, String password)
			throws SQLException {

		PreparedStatement statement = null;
		ResultSet resultSet = null;

		try {
			statement = DBConnection
					.getSharedConnection()
					.prepareStatement(
							"SELECT customerID FROM Customer WHERE LOWER(email) = ? AND password = ?");

			statement.setString(1, email);
			statement.setString(2, password);

			resultSet = statement.executeQuery();

			if (!resultSet.next())
				throw new SQLException("no such branch manager");

			return Long.toString(resultSet.getLong(1));

		} catch (SQLException ex) {
			throw ex;
		} finally {
			DBUtil.dispose(resultSet);
			DBUtil.dispose(statement);
		}
	}

	public static String getCustomerName(String customerID) throws SQLException {
		PreparedStatement statement = null;
		ResultSet resultSet = null;

		try {

			statement = DBConnection.getSharedConnection().prepareStatement(
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

	public static boolean isPremiumUser(String customerID) {
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;

		try {
			preparedStatement = DBConnection
					.getSharedConnection()
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
