package com.ewebstore.model;

public class SharedData {
	private final static int POPULAR_BRAND_COUNT = 7;
	private final static int POPULAR_PRODUCT_COUNT = 6;
	private final static int recommendedProductCount = 3;

	private final static int PRODUCT_IMAGE_HEIGHT = 100;
	private final static int PRODUCT_IMAGE_WIDTH = 100;

	public static int getPopularProductCount() {
		return POPULAR_PRODUCT_COUNT;
	}

	public static int getRecommendedProductCount() {
		return recommendedProductCount;
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
}
