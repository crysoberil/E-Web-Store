package com.ewebstore.dbutil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DBUtil {
	public static void dispose(ResultSet resultSet) {
		if (resultSet != null) {
			try {
				resultSet.close();
			} catch (SQLException ex) {
				ex.printStackTrace();
			}
		}
	}
	
	public static void dispose(PreparedStatement preparedStatement) {
		if (preparedStatement != null) {
			try {
				preparedStatement.close();
			} catch (SQLException ex) {
				ex.printStackTrace();
			}
		}
	}
	
	public static void dispose(Statement statement) {
		if (statement != null) {
			try {
				statement.close();
			} catch (SQLException ex) {
				ex.printStackTrace();
			}
		}
	}
	
	public static void dispose(Connection connection) {
		if (connection != null) {
			try {
				if (connection == DBConnection.getSharedConnection())
					throw new SQLException("Trying to close the shared connection");
				connection.close();
			} catch (SQLException ex) {
				ex.printStackTrace();
			}
		}
	}
}
