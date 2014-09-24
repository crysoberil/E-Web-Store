package com.ewebstore.entity;

import java.util.ArrayList;

public class Product {

	private String productID;
	private String productName;
	private String brandID;
	private String brandName;
	private String productDetail;
	private String productImageLink;
	private double price;
	ArrayList<String> categories;

	public Product(String productID, String productName, String brandID,
			String brandName, String productDetail, String productImageLink,
			double price, ArrayList<String> categories) {
		this.productID = productID;
		this.productName = productName;
		this.brandID = brandID;
		this.brandName = brandName;
		this.productDetail = productDetail;
		this.productImageLink = productImageLink;
		this.price = price;
		this.categories = categories;
	}

	public String getProductID() {
		return productID;
	}

	public String getProductName() {
		return productName;
	}

	public String getBrandID() {
		return brandID;
	}

	public String getBrandName() {
		return brandName;
	}

	public String getProductDetail() {
		return productDetail;
	}

	public String getProductImageLink() {
		return productImageLink;
	}

	public double getPrice() {
		return price;
	}

	public ArrayList<String> getCategory() {
		return categories;
	}
}
