package com.ewebstore.model;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.ewebstore.dbutil.DBConnection;
import com.ewebstore.dbutil.DBUtil;
import com.ewebstore.entity.ProductCategory;

/**
 * The ProductCategoryQueryModel class handles the database operations related
 * to the product categories.
 * 
 * @author ewebstore.org
 *
 */
public class ProductCategoryQueryModel {

	/**
	 * Returns the name of the category corresponding to the provided category
	 * ID
	 * 
	 * @param categoryID
	 *            ID of the category
	 * @return Name of the category
	 * @throws SQLException
	 *             if a database access error occurs
	 */
	public static String getCategoryName(String categoryID) throws SQLException {
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;

		try {
			preparedStatement = DBConnection.getConnection().prepareStatement(
					"SELECT categoryName FROM Category WHERE categoryID = ?");

			preparedStatement.setLong(1, Long.parseLong(categoryID));

			resultSet = preparedStatement.executeQuery();

			if (!resultSet.next())
				throw new IllegalArgumentException("No such category");

			String categoryName = resultSet.getString(1);
			return categoryName;

		} catch (SQLException e) {
			throw e;
		}
	}

	/**
	 * Returns a list of information on all the categories from database
	 * 
	 * @return List of category information
	 */
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

	/**
	 * Updates database by inserting product-category pairs
	 * 
	 * @param productName
	 *            Name of the products
	 * @param categorieIDs
	 *            List of the categories corresponding to the product
	 * @throws SQLException
	 *             if a database access error occurs
	 */
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
