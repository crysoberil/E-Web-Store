package com.ewebstore.test;

import java.io.File;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;

import com.ewebstore.dbutil.DBConnection;
import com.ewebstore.dbutil.DBUtil;

public class Main {
	public static void main(String[] args) throws Exception {
//		 pushDistrictNamesToDataBase();
		updateDistrictDistances();
	}

	private static void updateDistrictDistances() throws Exception {
		Scanner scanner = new Scanner(new File(
				"/home/crysoberil/districtcoordinates"));

		ArrayList<Long> districtIDs = new ArrayList<Long>();
		ArrayList<Double> latitudes = new ArrayList<Double>();
		ArrayList<Double> longitudes = new ArrayList<Double>();

		while (scanner.hasNext()) {
			districtIDs.add(Long.parseLong(scanner.next()));
			latitudes.add(Double.parseDouble(scanner.next()));
			longitudes.add(Double.parseDouble(scanner.next()));
		}

		scanner.close();

		PreparedStatement preparedStatement = null;

		try {
			preparedStatement = DBConnection
					.getConnection()
					.prepareStatement(
							"INSERT INTO DistrictDistance VALUES(?, ?, ?)");

			for (int i = 0; i < districtIDs.size(); i++)
				for (int j = 0; j < districtIDs.size(); j++) {
					{
						try {
							preparedStatement.setLong(1, districtIDs.get(i));
							preparedStatement.setLong(2, districtIDs.get(j));

							double distance = getDistance(latitudes.get(i),
									latitudes.get(j), longitudes.get(i),
									longitudes.get(j));

							preparedStatement.setDouble(3, distance);

							preparedStatement.addBatch();
						} catch (SQLException ex) {
							ex.printStackTrace();
						}
					}
				}
			preparedStatement.executeBatch();
		} catch (SQLException ex) {
			ex.printStackTrace();
		} finally {
			DBUtil.dispose(preparedStatement);
		}
	}

	private static double getDistance(double x1, double x2, double y1, double y2) {
		double dLon = y1 - y2;
		double dLat = x1 - x2;
		double a = (Math.sin(dLat / 2)) * (Math.sin(dLat / 2)) + Math.cos(x1)
				* Math.cos(x2) * (Math.sin(dLon / 2)) * (Math.sin(dLon / 2));
		double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
		double d = 6378.1 * c;

		return d;
	}

	private static void pushDistrictNamesToDataBase() throws Exception {
		Scanner scanner = new Scanner(
				new File("/home/crysoberil/districtnames"));

		// ArrayList<String> list = new ArrayList<String>();

		while (scanner.hasNext()) {
			PreparedStatement preparedStatement = null;

			try {
				preparedStatement = DBConnection.getConnection()
						.prepareStatement(
								"INSERT INTO District VALUES(NULL, ?)");
				preparedStatement.setString(1, scanner.nextLine());

				preparedStatement.executeUpdate();
			} catch (SQLException ex) {
				ex.printStackTrace();
			} finally {
				DBUtil.dispose(preparedStatement);
			}
		}

		scanner.close();
	}
}
