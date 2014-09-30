package com.ewebstore.dbutil;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.concurrent.ArrayBlockingQueue;

public class DBConnection {

	private static final int MAXIMUMCONNECTION = 10;
	private static Object synchronizer = null;
	private static Connection sharedConnection = null;
	private static ArrayBlockingQueue<Connection> availableConnections = new ArrayBlockingQueue<Connection>(
			MAXIMUMCONNECTION);

	private static String dbURL = "jdbc:mysql://localhost:3306/ewebstore";
	private static String username = "crysoberil";
	private static String password = "clrscr";

	static {
		synchronizer = new Object();

		synchronized (synchronizer) {
			try {
				Class.forName("com.mysql.jdbc.Driver");

				sharedConnection = DriverManager.getConnection(dbURL, username,
						password);

				for (int i = 0; i < MAXIMUMCONNECTION; i++)
					availableConnections.add(DriverManager.getConnection(dbURL,
							username, password));

			} catch (SQLException | ClassNotFoundException ex) {
				System.out.println("Failed to create a connection");
				ex.printStackTrace();
			}
		}
	}

	public static Connection getConnection() {
		while (sharedConnection == null)
			;
		return sharedConnection;
	}

	static Connection getNewConnection() {
		synchronized (synchronizer) {
			try {
				return availableConnections.take();
			} catch (InterruptedException ex) {
				ex.printStackTrace();
				return null;
			}
		}
	}

	static void submitConnection(Connection connection) {
		synchronized (synchronizer) {
			if (connection == sharedConnection)
				return;

			try {
				connection.setAutoCommit(true);
				availableConnections.put(connection);
			} catch (InterruptedException | SQLException ex) {
				ex.printStackTrace();
			}
		}
	}
}