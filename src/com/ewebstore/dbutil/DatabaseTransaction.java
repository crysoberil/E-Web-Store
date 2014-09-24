package com.ewebstore.dbutil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.LinkedList;

public class DatabaseTransaction {
	private Connection connection;
	LinkedList<PreparedStatement> preparedStatements;

	public DatabaseTransaction() throws SQLException {
		connection = DBConnection.getNewConnection();

		if (connection == null)
			throw new SQLException("Could not obtain connection");

		connection.setAutoCommit(false);
		preparedStatements = new LinkedList<PreparedStatement>();
	}

	public PreparedStatement newPreparedStatement(String sqlStatement)
			throws SQLException {
		PreparedStatement statement = connection.prepareStatement(sqlStatement);
		preparedStatements.addFirst(statement);
		return statement;
	}

	public void commit() throws SQLException {
		try {
			for (PreparedStatement preparedStatement : preparedStatements)
				preparedStatement.executeUpdate();
			connection.commit();
		} catch (SQLException ex) {
			ex.printStackTrace();
			connection.rollback();
			throw ex;
		} finally {
			for (PreparedStatement preparedStatement : preparedStatements)
				DBUtil.dispose(preparedStatement);

			connection.setAutoCommit(true);
			DBConnection.submitConnection(connection);

			connection = null;
			preparedStatements = null;
		}
	}
}
