package com.ewebstore.linkgenerators;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

public class ImageLinkGenerator {
	private static String imagesFolderAbsolutePathExtension = File.separator
			+ "images" + File.separator + "productimages" + File.separator;
	private static String imagesContextRelativeExtension = "images/productimages/";

	// [0]-> absolute address; [1]-> context relative address
	public synchronized static String[] getNewImagePath(String base) {
		while (true) {
			try {
				Scanner scanner = new Scanner(new File(base
						+ imagesFolderAbsolutePathExtension + "tracker.config"));
				long nextNum = scanner.nextLong();
				scanner.close();
				writeValueToTracker(base, nextNum + 1);

				String[] ret = new String[2];

				ret[0] = base + imagesFolderAbsolutePathExtension + nextNum
						+ ".jpg";
				ret[1] = imagesContextRelativeExtension + nextNum + ".jpg";

				return ret;
			} catch (FileNotFoundException ex) {
				writeValueToTracker(base, 1);
			}
		}
	}

	private static void writeValueToTracker(String base, long num) {
		while (true) {
			File file = new File(base + imagesFolderAbsolutePathExtension
					+ "tracker.config");

			file.getParentFile().mkdirs();
			try {
				PrintWriter writer = new PrintWriter(file);
				writer.println(num);
				writer.close();
				return;
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
	}
}
