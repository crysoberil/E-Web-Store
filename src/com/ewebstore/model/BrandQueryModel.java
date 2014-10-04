package com.ewebstore.model;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.ewebstore.dbutil.DBConnection;
import com.ewebstore.dbutil.DBUtil;
import com.ewebstore.entity.Brand;

public class BrandQueryModel {
	public static String getBrandName(String brandID) throws SQLException {
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;

		try {
			preparedStatement = DBConnection.getConnection().prepareStatement(
					"SELECT brandName FROM Brand WHERE brandID = ?");

			preparedStatement.setLong(1, Long.parseLong(brandID));

			resultSet = preparedStatement.executeQuery();

			if (!resultSet.next())
				throw new IllegalArgumentException("No such brand");

		} catch (SQLException e) {
			throw e;
		}
		return brandID;
	}

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

	public static ArrayList<Brand> getPopularBrands(int brandCount)
			throws SQLException {
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;

		try {
			preparedStatement = DBConnection
					.getConnection()
					.prepareStatement(
							"SELECT brandID, brandName FROM Brand ORDER BY (SELECT COUNT(*) FROM OrderProducts, Product WHERE OrderProducts.productID = Product.productID AND Product.brandID = Brand.brandID) DESC  LIMIT ?");
			preparedStatement.setInt(1, brandCount);

			resultSet = preparedStatement.executeQuery();

			ArrayList<Brand> popularBrands = new ArrayList<Brand>();
			while (resultSet.next()) {
				String brandID = Long.toString(resultSet.getLong(1));
				String brandName = resultSet.getString(2);

				popularBrands.add(new Brand(brandID, brandName));
			}

			return popularBrands;
		} catch (SQLException e) {
			throw e;
		} finally {
			DBUtil.dispose(preparedStatement);
			DBUtil.dispose(resultSet);
		}
	}
}
