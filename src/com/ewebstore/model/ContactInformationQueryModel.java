package com.ewebstore.model;

/**
 * The ContactInformationQueryModel class stores the contact information of the
 * shop
 * 
 * @author ewebstore.org
 *
 */
public class ContactInformationQueryModel {
	private final static String SHOP_NAME = "E-Web-Store";
	private final static String ADDRESS = "BUET, Dhaka";
	private final static String DISTRICT = "Dhaka, Bangladesh";
	private final static String MOBILE = "880-XXXXXXXXXX";
	private final static String FAX = "X-XXX-XXX-XXXX";
	private final static String EMAIL = "x@x.com";
	private final static String FACEBOOK_LINK = "https://www.facebook.com";
	private final static String TWITTER_LINK = "https://www.twitter.com";
	private final static String GOOGLEPLUS_LINK = "https://www.plus.google.com";
	private final static String YOUTUBE_LINK = "https://www.youtube.com";

	public static String getShopName() {
		return SHOP_NAME;
	}

	public static String getAddress() {
		return ADDRESS;
	}

	public static String getDistrict() {
		return DISTRICT;
	}

	public static String getMobile() {
		return MOBILE;
	}

	public static String getFax() {
		return FAX;
	}

	public static String getEmail() {
		return EMAIL;
	}

	public static String getFacebookLink() {
		return FACEBOOK_LINK;
	}

	public static String getTwitterLink() {
		return TWITTER_LINK;
	}

	public static String getGoogleplusLink() {
		return GOOGLEPLUS_LINK;
	}

	public static String getYoutubeLink() {
		return YOUTUBE_LINK;
	}

}
