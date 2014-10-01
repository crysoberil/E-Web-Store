package com.ewebstore.model;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.ewebstore.dbutil.DBConnection;
import com.ewebstore.dbutil.DBUtil;

public class ProductQueryModel {

	public static String getProductName(String productID) throws SQLException {
		PreparedStatement statement = null;
		ResultSet resultSet = null;

		try {
			statement = DBConnection.getConnection().prepareStatement(
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

	public static void addGenericProduct(String productName, String brandName,
			String description, double price,
			ArrayList<String> selectedCategoryIDs, String imageLink)
			throws SQLException {

		PreparedStatement preparedStatement = null;

		try {
			preparedStatement = DBConnection.getConnection().prepareStatement(
					"INSERT INTO Product VALUES(NULL, ?, ?, ?, ?, ?)");
			
			String brandID = BrandQueryModel.pushAndGetBrandIDByName(brandName);
			
			preparedStatement.setString(1, productName);
			preparedStatement.setLong(2, Long.valueOf(brandID));
			preparedStatement.setString(3, description);
			preparedStatement.setString(4, imageLink);
			preparedStatement.setDouble(5, price);
			
			preparedStatement.executeUpdate();
			
			ProductCategoryQueryModel.addProductCategories(productName, selectedCategoryIDs);
			
		} catch (SQLException ex) {
			ex.printStackTrace();
			throw ex;
		} finally {
			DBUtil.dispose(preparedStatement);
		}
	}
}
