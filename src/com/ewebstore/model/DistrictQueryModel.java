package com.ewebstore.model;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.ewebstore.dbutil.DBConnection;
import com.ewebstore.dbutil.DBUtil;
import com.ewebstore.entity.District;

/**
 * The DistrictQueryModel class handles the database operations related to the
 * districts.
 * 
 * @author ewebstore.org
 *
 */
public class DistrictQueryModel {

	/**
	 * Returns information on all the districts from database.
	 * 
	 * @return List of district information.
	 */
	public static ArrayList<District> getAllDistricts() {
		ArrayList<District> districts = new ArrayList<>();

		PreparedStatement statement = null;
		ResultSet resultSet = null;

		try {
			statement = DBConnection
					.getConnection()
					.prepareStatement(
							"SELECT districtID, districtName FROM District ORDER BY districtName");

			resultSet = statement.executeQuery();

			while (resultSet.next()) {
				String districtID = resultSet.getString(1);
				String districtName = capitalizedDistrictName(resultSet
						.getString(2));

				districts.add(new District(districtID, districtName));
			}
		} catch (SQLException ex) {

		} finally {
			DBUtil.dispose(resultSet);
			DBUtil.dispose(statement);
		}

		return districts;
	}

	/**
	 * Returns capitalized district name corresponding to the provided district
	 * ID
	 * 
	 * @param distName
	 *            Name of the district
	 * @return
	 */
	public static String capitalizedDistrictName(String distName) {
		String[] toks = distName.split(" +");
		StringBuilder builder = new StringBuilder();

		for (String tok : toks)
			builder.append(tok.substring(0, 1).toUpperCase())
					.append(tok.substring(1).toLowerCase()).append(" ");

		return builder.substring(0, builder.length() - 1);
	}
}
