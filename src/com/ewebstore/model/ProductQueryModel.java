package com.ewebstore.model;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.ewebstore.dbutil.DBConnection;
import com.ewebstore.dbutil.DBUtil;
import com.ewebstore.entity.Product;

/**
 * The ProductQueryModel class handles the database operations related to the
 * products.
 * 
 * @author ewebstore.org
 *
 */
public class ProductQueryModel {

	/**
	 * Returns the information of the product corresponding to the provided
	 * product ID
	 * 
	 * @param productID
	 *            ID of the product
	 * @return Information on the product
	 * @throws SQLException
	 *             if a database access error occurs
	 */
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

	/**
	 * Returns the name of the product corresponding to the provided product ID
	 * 
	 * @param productID
	 *            ID of the product
	 * @return Name of the product
	 * @throws SQLException
	 *             if a database access error occurs
	 */
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

	/**
	 * Returns the image source link of the product corresponding to the
	 * provided product ID
	 * 
	 * @param productID
	 *            ID of the product
	 * @return Image source link of the product
	 * @throws SQLException
	 *             if a database access error occurs
	 */
	public static String getProductImageLink(String productID)
			throws SQLException {
		PreparedStatement statement = null;
		ResultSet resultSet = null;

		try {
			statement = DBConnection.getConnection().prepareStatement(
					"SELECT productImageLink FROM Product WHERE productID = ?");

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

	/**
	 * Returns the price of the product corresponding to the provided product ID
	 * 
	 * @param productID
	 *            ID of the product
	 * @return price of the product
	 * @throws SQLException
	 *             if a database access error occurs
	 */
	public static double getProductPrice(String productID) throws SQLException {
		PreparedStatement statement = null;
		ResultSet resultSet = null;

		try {
			statement = DBConnection.getConnection().prepareStatement(
					"SELECT price FROM Product WHERE productID = ?");

			statement.setLong(1, Long.valueOf(productID));

			resultSet = statement.executeQuery();

			if (!resultSet.next())
				throw new SQLException("invalid productID");

			return resultSet.getDouble(1);

		} catch (SQLException ex) {
			throw ex;
		} finally {
			DBUtil.dispose(resultSet);
			DBUtil.dispose(statement);
		}
	}

	/**
	 * Updates database by inserting a new generic product
	 * 
	 * @param productName
	 *            Name of the product
	 * @param brandName
	 *            Name of the brand of the product
	 * @param description
	 *            Description of the product
	 * @param price
	 *            Price of the product
	 * @param selectedCategoryIDs
	 *            IDs of the categories of the product
	 * @param imageLink
	 *            Image source link of the product
	 * @throws SQLException
	 *             if a database access error occurs
	 */
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

	/**
	 * Returns a list of product IDs with their names containing the provided
	 * prefix
	 * 
	 * @param productNamePrefix
	 *            Prefix string
	 * @return List of product IDs
	 * @throws SQLException
	 *             if a database access error occurs
	 */
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

	/**
	 * Returns the name of the brand of the product corresponding to the
	 * provided product ID
	 * 
	 * @param productID
	 *            ID of the product
	 * @return Name of the brand of the product
	 * @throws SQLException
	 *             if a database access error occurs
	 */
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

	/**
	 * Returns a list of information on products having the category
	 * corresponding to the provided category ID as a category
	 * 
	 * @param categoryID
	 *            ID of the category
	 * @return List of product information
	 * @throws SQLException
	 *             if a database access error occurs
	 */
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

	/**
	 * Returns a list of information on most popular products
	 * 
	 * @param productCount
	 *            Number of products on which information is to be returned
	 * @return List of product information
	 * @throws SQLException
	 *             if a database access error occurs
	 */
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

	/**
	 * Returns a list of information on recommended products for the customer
	 * corresponding to the provided customer ID
	 * 
	 * @param customerID
	 *            ID of the customer
	 * @param productCount
	 *            Number of products on which information is to be returned
	 * @return List of product information
	 * @throws SQLException
	 *             if a database access error occurs
	 */
	public static ArrayList<Product> getRecommendedProducts(String customerID,
			int productCount) {
		ArrayList<Product> products = new ArrayList<>();

		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;

		try {
			String sqlString = "SELECT productID, productName, brandID, productImageLink, price FROM Product AS P1 WHERE productID NOT IN (SELECT productID FROM OrderProducts WHERE orderID IN (SELECT orderID FROM `Order` WHERE customerID = ?)) ORDER BY (SELECT COUNT(*) FROM OrderProducts AS OP1 WHERE P1.productID = OP1.productID AND orderID IN (SELECT orderID FROM `Order` WHERE customerID IN (SELECT customerID FROM `Order` WHERE orderID IN (SELECT orderID FROM OrderProducts WHERE productID IN (SELECT productID FROM OrderProducts WHERE orderID IN (SELECT orderID FROM `Order` WHERE customerID = ?)))))) DESC LIMIT ?";

			preparedStatement = DBConnection.getConnection().prepareStatement(
					sqlString);

			preparedStatement.setLong(1, Long.valueOf(customerID));
			preparedStatement.setLong(2, Long.valueOf(customerID));
			preparedStatement.setInt(3, productCount);

			resultSet = preparedStatement.executeQuery();

			while (resultSet.next()) {
				String productID = Long.toString(resultSet.getLong(1));
				String productName = resultSet.getString(2);
				String brandID = Long.toString(resultSet.getLong(3));
				String brandName = BrandQueryModel.getBrandName(brandID);
				String productImageLink = resultSet.getString(4);
				Double price = resultSet.getDouble(5);

				products.add(new Product(productID, productName, brandID,
						brandName, null, productImageLink, price, null));
			}

		} catch (SQLException ex) {
			ex.printStackTrace();
		} finally {
			DBUtil.dispose(resultSet);
			DBUtil.dispose(preparedStatement);
		}

		return products;
	}

	/**
	 * Returns information on products relating to the provided search key
	 * 
	 * @param searchKey
	 *            Search key
	 * @return List of product information
	 */
	public static ArrayList<Product> searchProducts(String searchKey) {
		ArrayList<Product> products = new ArrayList<>();

		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;

		try {
			String sqlString = "SELECT productID, productName, brandID, productImageLink, price FROM Product WHERE LOWER(productName) LIKE \'%"
					+ searchKey + "%\'";

			preparedStatement = DBConnection.getConnection().prepareStatement(
					sqlString);

			resultSet = preparedStatement.executeQuery();

			while (resultSet.next()) {
				String productID = Long.toString(resultSet.getLong(1));
				String productName = resultSet.getString(2);
				String brandID = Long.toString(resultSet.getLong(3));
				String brandName = BrandQueryModel.getBrandName(brandID);
				String productImageLink = resultSet.getString(4);
				Double price = resultSet.getDouble(5);

				products.add(new Product(productID, productName, brandID,
						brandName, null, productImageLink, price, null));
			}

		} catch (SQLException ex) {
			ex.printStackTrace();
		} finally {
			DBUtil.dispose(resultSet);
			DBUtil.dispose(preparedStatement);
		}

		return products;
	}
}