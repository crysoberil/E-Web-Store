package com.ewebstore.model;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.ewebstore.dbutil.DBConnection;
import com.ewebstore.dbutil.DBUtil;

public class ProductQueryModel {

	public static String getProductName(String productID) throws SQLException {
		PreparedStatement statement = null;
		ResultSet resultSet = null;

		try {
			statement = DBConnection
					.getSharedConnection()
					.prepareStatement(
							"SELECT productName FROM Product WHERE productID = ?");

			statement.setLong(1, Long.valueOf(productID));

			resultSet = statement.executeQuery();

			if (!resultSet.next())
				throw new SQLException("invalid productID");

			return resultSet.getString(1);

		} catch (SQLException ex) {
			throw ex;
		} finally {
			DBUtil.dispose(resultSet);
			DBUtil.dispose(statement);
		}
	}
}
