package com.ewebstore.model;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.ewebstore.dbutil.DBConnection;
import com.ewebstore.dbutil.DBUtil;

public class BrandQueryModel {
	public static String pushAndGetBrandIDByName(String brandName)
			throws SQLException {
		try {
			return getBrandIDByName(brandName);
		} catch (SQLException ex) {
			tryPushBrand(brandName);
		}

		return getBrandIDByName(brandName);
	}

	private static void tryPushBrand(String brandName) {
		PreparedStatement preparedStatement = null;

		try {
			preparedStatement = DBConnection.getConnection().prepareStatement(
					"INSERT INTO Brand VALUES (NULL, ?, ?)");

			preparedStatement.setString(1, brandName);
			preparedStatement.setString(2, "");

			preparedStatement.executeUpdate();

		} catch (SQLException ex) {
			ex.printStackTrace();
		} finally {
			DBUtil.dispose(preparedStatement);
		}
	}

	private static String getBrandIDByName(String brandName)
			throws SQLException {
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;

		try {
			preparedStatement = DBConnection
					.getConnection()
					.prepareStatement(
							"SELECT brandID FROM Brand WHERE LOWER(brandName) = LOWER(?)");

			preparedStatement.setString(1, brandName);
			resultSet = preparedStatement.executeQuery();

			if (!resultSet.next())
				throw new SQLException();

			return Long.toString(resultSet.getLong(1));
		} catch (SQLException ex) {
			throw ex;
		} finally {
			DBUtil.dispose(preparedStatement);
			DBUtil.dispose(resultSet);
		}
	}
}
