package com.ewebstore.model;

/**
 * The SharedData class contains the constant data shared between classes in the
 * project.
 * 
 * @author ewebstore.org
 *
 */
public class SharedData {
	private final static String SHOP_NAME = "E-Web-Store";

	private final static int POPULAR_BRAND_COUNT = 7;
	private final static int POPULAR_PRODUCT_COUNT = 6;
	private final static int RECOMMENDED_PRODUCT_COUNT = 3;

	private final static int PRODUCT_IMAGE_HEIGHT = 100;
	private final static int PRODUCT_IMAGE_WIDTH = 100;

	private final static double SHIPPING_COST = 30.0; // in BDT

	public static int getPopularProductCount() {
		return POPULAR_PRODUCT_COUNT;
	}

	public static int getRecommendedProductCount() {
		return RECOMMENDED_PRODUCT_COUNT;
	}

	public static int getPopularBrandCount() {
		return POPULAR_BRAND_COUNT;
	}

	public static int getProductImageHeight() {
		return PRODUCT_IMAGE_HEIGHT;
	}

	public static int getProductImageWidth() {
		return PRODUCT_IMAGE_WIDTH;
	}

	public static double getShippingCost() {
		return SHIPPING_COST;
	}

	public static String getShopName() {
		return SHOP_NAME;
	}
}
