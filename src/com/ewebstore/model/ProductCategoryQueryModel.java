package com.ewebstore.model;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.ewebstore.dbutil.DBConnection;
import com.ewebstore.dbutil.DBUtil;
import com.ewebstore.entity.ProductCategory;

public class ProductCategoryQueryModel {
	public static ArrayList<ProductCategory> getAllProductCategories() {
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;

		ArrayList<ProductCategory> categories = new ArrayList<ProductCategory>();

		try {
			preparedStatement = DBConnection.getConnection().prepareStatement(
					"SELECT * FROM Category ORDER BY categoryName");

			resultSet = preparedStatement.executeQuery();

			while (resultSet.next()) {
				String categoryID = Long.toString(resultSet.getLong(1));
				String categoryName = resultSet.getString(2);
				categories.add(new ProductCategory(categoryID, categoryName));
			}

		} catch (SQLException ex) {
			ex.printStackTrace();
		} finally {
			DBUtil.dispose(preparedStatement);
			DBUtil.dispose(resultSet);
		}

		return categories;
	}

	public static ArrayList<String> getProductCategoryNames()
			throws SQLException {
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;

		ArrayList<String> productCategoryNames = new ArrayList<String>();

		try {
			preparedStatement = DBConnection.getConnection().prepareStatement(
					"SELECT categoryName FROM Category ORDER BY categoryName");

			resultSet = preparedStatement.executeQuery();

			while (resultSet.next())
				productCategoryNames.add(resultSet.getString(1));

			return productCategoryNames;

		} catch (SQLException ex) {
			throw ex;
		} finally {
			DBUtil.dispose(preparedStatement);
			DBUtil.dispose(resultSet);
		}
	}

	public static void addProductCategories(String productName,
			ArrayList<String> categorieIDs) throws SQLException {

		PreparedStatement preparedStatement = null;

		try {
			preparedStatement = DBConnection
					.getConnection()
					.prepareStatement(
							"INSERT INTO ProductCategory VALUES ((SELECT productID FROM Product WHERE productName = ?), ?)");

			for (String categoryID : categorieIDs) {
				preparedStatement.setString(1, productName);
				preparedStatement.setLong(2, Long.valueOf(categoryID));
				preparedStatement.addBatch();
			}

			preparedStatement.executeBatch();

		} catch (SQLException ex) {
			throw ex;
		} finally {
			DBUtil.dispose(preparedStatement);
		}
	}
}
