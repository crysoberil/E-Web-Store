package com.ewebstore.linkgenerators;

public class ImageLinkGenerator {
	private static String base = "/home/crysoberil/images/";
	private static long counter = 0;

	public synchronized static String getNewImageName() {
		counter++;
		return base + counter + ".jpg";
	}
}
