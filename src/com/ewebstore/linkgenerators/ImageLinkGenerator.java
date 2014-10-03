package com.ewebstore.linkgenerators;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Scanner;

import com.ewebstore.dbutil.DBConnection;

public class ImageLinkGenerator {
	private static String base = "/home/crysoberil/images/";

	public synchronized static String getNewImagePath() {
		while (true) {
			try {
				Scanner scanner = new Scanner(new File(base + "tracker"));
				long nextNum = scanner.nextLong();
				scanner.close();
				writeValueToTracker(nextNum + 1);
				return base + nextNum + ".jpg";
			} catch (FileNotFoundException ex) {
				writeValueToTracker(1);
			}
		}
	}

	private static void writeValueToTracker(long num) {
		while (true) {
			try {
				PrintWriter writer = new PrintWriter(new File(base + "tracker"));
				writer.println(num);
				writer.close();
				return;
			} catch (IOException ex) {

			}
		}
	}
}
