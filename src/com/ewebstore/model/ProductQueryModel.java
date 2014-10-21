package com.ewebstore.model;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.ewebstore.dbutil.DBConnection;
import com.ewebstore.dbutil.DBUtil;
import com.ewebstore.entity.Product;

public class ProductQueryModel {

	public static Product getProduct(String productID) throws SQLException {
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;

		try {
			preparedStatement = DBConnection.getConnection().prepareStatement(
					"SELECT * FROM Product WHERE productID = ?");

			preparedStatement.setLong(1, Long.parseLong(productID));

			resultSet = preparedStatement.executeQuery();

			if (!resultSet.next())
				throw new IllegalArgumentException("No such product");

			String productName = resultSet.getString(2);
			String brandID = Long.toString(resultSet.getLong(3));
			String brandName = BrandQueryModel.getBrandName(brandID);
			String productDetail = resultSet.getString(4);
			String productImageLink = resultSet.getString(5);
			Double price = resultSet.getDouble(6);
			ArrayList<String> categories = new ArrayList<String>();

			PreparedStatement preparedStatement2 = null;
			ResultSet resultSet2 = null;

			try {
				preparedStatement2 = DBConnection
						.getConnection()
						.prepareStatement(
								"SELECT categoryID FROM ProductCategory WHERE productID = ?");

				preparedStatement2.setLong(1, Long.parseLong(productID));

				resultSet2 = preparedStatement2.executeQuery();

				while (resultSet2.next()) {
					String categoryID = resultSet2.getString(1);
					String categoryName = ProductCategoryQueryModel
							.getCategoryName(categoryID);
					categories.add(categoryName);
				}
			} catch (SQLException e) {
				throw e;
			} finally {
				DBUtil.dispose(resultSet2);
				DBUtil.dispose(preparedStatement2);
			}

			return new Product(productID, productName, brandID, brandName,
					productDetail, productImageLink, price, categories);

		} catch (SQLException e) {
			throw e;
		} finally {
			DBUtil.dispose(resultSet);
			DBUtil.dispose(preparedStatement);
		}
	}

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

			ProductCategoryQueryModel.addProductCategories(productName,
					selectedCategoryIDs);

		} catch (SQLException ex) {
			ex.printStackTrace();
			throw ex;
		} finally {
			DBUtil.dispose(preparedStatement);
		}
	}

	public static ArrayList<String> getAllProductIDsWithNameSubstring(
			String productNamePrefix) throws SQLException {
		ArrayList<String> productIDs = new ArrayList<String>();

		PreparedStatement statement = null;
		ResultSet resultSet = null;

		try {
			statement = DBConnection.getConnection().prepareStatement(
					"SELECT productID FROM Product WHERE productName LIKE ?");

			statement.setString(1, "%" + productNamePrefix + "%");

			resultSet = statement.executeQuery();

			while (resultSet.next())
				productIDs.add(Long.toString(resultSet.getLong(1)));

			return productIDs;
		} catch (SQLException ex) {
			throw ex;
		} finally {
			DBUtil.dispose(resultSet);
			DBUtil.dispose(statement);
		}
	}

	public static String getProductBrandName(String productID)
			throws SQLException {
		PreparedStatement statement = null;
		ResultSet resultSet = null;

		try {
			statement = DBConnection
					.getConnection()
					.prepareStatement(
							"SELECT brandName FROM Brand WHERE brandID = (SELECT brandID FROM Product WHERE productID = ?)");

			statement.setLong(1, Long.valueOf(productID));

			resultSet = statement.executeQuery();

			if (!resultSet.next())
				throw new SQLException();

			return resultSet.getString(1);
		} catch (SQLException ex) {
			throw ex;
		} finally {
			DBUtil.dispose(resultSet);
			DBUtil.dispose(statement);
		}
	}

	public static ArrayList<Product> getProductsByCategory(String categoryID)
			throws SQLException {
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;

		try {
			preparedStatement = DBConnection
					.getConnection()
					.prepareStatement(
							"SELECT productID from ProductCategory WHERE categoryID = ?");

			preparedStatement.setLong(1, Long.parseLong(categoryID));
			resultSet = preparedStatement.executeQuery();

			ArrayList<Product> products = new ArrayList<Product>();
			PreparedStatement preparedStatement2 = null;
			ResultSet resultSet2 = null;

			while (resultSet.next()) {

				String productID = Long.toString(resultSet.getLong(1));

				try {
					preparedStatement2 = DBConnection
							.getConnection()
							.prepareStatement(
									"SELECT * from Product WHERE productID = ?");

					preparedStatement2.setLong(1, Long.parseLong(productID));

					resultSet2 = preparedStatement2.executeQuery();
					if (!resultSet2.next())
						throw new IllegalArgumentException("Invalid ProductID");

					String productName = resultSet2.getString(2);
					String brandID = Long.toString(resultSet2.getLong(3));
					String brandName = BrandQueryModel.getBrandName(brandID);
					String productDetail = resultSet2.getString(4);
					String productImageLink = resultSet2.getString(5);
					Double price = resultSet2.getDouble(6);
					ArrayList<String> categories = null;

					products.add(new Product(productID, productName, brandID,
							brandName, productDetail, productImageLink, price,
							categories));
				} catch (SQLException e) {
					throw e;
				} finally {
					DBUtil.dispose(resultSet2);
					DBUtil.dispose(preparedStatement2);
				}
			}

			return products;
		} catch (SQLException e) {
			throw e;
		} finally {
			DBUtil.dispose(resultSet);
			DBUtil.dispose(preparedStatement);
		}
	}

	public static ArrayList<Product> getPopularProducts(int productCount)
			throws SQLException {
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;

		try {
			preparedStatement = DBConnection
					.getConnection()
					.prepareStatement(
							"SELECT * from Product ORDER BY (SELECT COUNT(*) FROM OrderProducts WHERE OrderProducts.productID = Product.productID) DESC LIMIT ?");

			preparedStatement.setInt(1, productCount);

			resultSet = preparedStatement.executeQuery();

			ArrayList<Product> popularProducts = new ArrayList<Product>();

			while (resultSet.next()) {
				String productID = Long.toString(resultSet.getLong(1));
				String productName = resultSet.getString(2);
				String brandID = Long.toString(resultSet.getLong(3));
				String brandName = BrandQueryModel.getBrandName(brandID);
				String productDetail = resultSet.getString(4);
				String productImageLink = resultSet.getString(5);
				Double price = resultSet.getDouble(6);
				ArrayList<String> categories = null;

				popularProducts.add(new Product(productID, productName,
						brandID, brandName, productDetail, productImageLink,
						price, categories));
			}

			return popularProducts;

		} catch (SQLException e) {
			throw e;
		} finally {
			DBUtil.dispose(resultSet);
			DBUtil.dispose(preparedStatement);
		}
	}

	public static ArrayList<Product> getRecommendedProducts(String customerID,
			int productCount) {
		return null;
	}
}