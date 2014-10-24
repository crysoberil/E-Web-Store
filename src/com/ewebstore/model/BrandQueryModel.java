package com.ewebstore.model;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.ewebstore.dbutil.DBConnection;
import com.ewebstore.dbutil.DBUtil;
import com.ewebstore.entity.Brand;
import com.ewebstore.entity.Product;

/**
 * The BrandQueryModel class handles database operations related to the brands.
 * 
 * @author ewebstore.org
 *
 */
public class BrandQueryModel {

	/**
	 * Returns the brand name corresponding to the provided brand ID
	 * 
	 * @param brandID
	 *            ID of the brand
	 * @return Corresponding brand name
	 * @throws SQLException
	 *             if a database access error occurs
	 */
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

			String brandName = resultSet.getString(1);
			return brandName;

		} catch (SQLException e) {
			throw e;
		} finally {
			DBUtil.dispose(resultSet);
			DBUtil.dispose(preparedStatement);
		}
	}

	/**
	 * 
	 * @param brandName
	 * @return
	 * @throws SQLException
	 */
	public static String pushAndGetBrandIDByName(String brandName)
			throws SQLException {
		try {
			return getBrandIDByName(brandName);
		} catch (SQLException ex) {
			tryPushBrand(brandName);
		}

		return getBrandIDByName(brandName);
	}

	/**
	 * Updates database by inserting a brand
	 * 
	 * @param brandName
	 *            Name of the brand
	 */
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

	/**
	 * Returns the ID of the brand corresponding to the provided brand name
	 * 
	 * @param brandName
	 *            Name of the brand
	 * @return ID of the brand
	 * @throws SQLException
	 *             if a database access error occurs
	 */
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

	/**
	 * Returns information on the popular brands
	 * 
	 * @param brandCount
	 *            Number of the brands on which information is to be returned
	 * @return List of brand information
	 * @throws SQLException
	 *             if a database access error occurs
	 */
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

	/**
	 * Returns information on the popular products of the provided brand
	 * 
	 * @param brandID
	 *            ID of the brand
	 * @return List of product information
	 */
	public static ArrayList<Product> getPopularBrandProducts(String brandID) {
		ArrayList<Product> brandProducts = new ArrayList<>();

		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;

		try {
			String brandName = getBrandName(brandID);

			preparedStatement = DBConnection
					.getConnection()
					.prepareStatement(
							"SELECT productID, productName, productImageLink, price FROM Product WHERE brandID = ? ORDER BY (SELECT COALESCE(SUM(quantity), 0) FROM OrderProducts WHERE OrderProducts.productID = Product.productID) DESC LIMIT 10");
			preparedStatement.setLong(1, Long.valueOf(brandID));

			resultSet = preparedStatement.executeQuery();

			while (resultSet.next()) {
				String productID = resultSet.getString(1);
				String productName = resultSet.getString(2);
				String productImageLink = resultSet.getString(3);
				double price = resultSet.getDouble(4);

				Product product = new Product(productID, productName, brandID,
						brandName, null, productImageLink, price, null);

				brandProducts.add(product);
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
		} finally {
			DBUtil.dispose(preparedStatement);
			DBUtil.dispose(resultSet);
		}

		return brandProducts;
	}

	/**
	 * Returns information on brands relating to the provided search key
	 * 
	 * @param searchKey
	 *            Search key
	 * @return List of brand information
	 */
	public static ArrayList<Brand> searchBrands(String searchKey) {
		ArrayList<Brand> brands = new ArrayList<>();

		PreparedStatement statement = null;
		ResultSet resultSet = null;

		try {
			String sqlString = "SELECT brandID, brandName FROM Brand WHERE brandName LIKE \'%"
					+ searchKey + "%\'";

			statement = DBConnection.getConnection()
					.prepareStatement(sqlString);

			resultSet = statement.executeQuery();

			while (resultSet.next()) {
				String brandID = resultSet.getString(1);
				String brandName = resultSet.getString(2);

				brands.add(new Brand(brandID, brandName));
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
		} finally {
			DBUtil.dispose(resultSet);
			DBUtil.dispose(statement);
		}

		return brands;
	}
}
