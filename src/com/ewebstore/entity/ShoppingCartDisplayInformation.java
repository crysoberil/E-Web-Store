package com.ewebstore.entity;

public class ShoppingCartDisplayInformation {
	private String productName;
	private String productImageLink;
	private double productPrice;

	public ShoppingCartDisplayInformation(String productName,
			String productImageLink, double productPrice) {
		super();
		this.productName = productName;
		this.productImageLink = productImageLink;
		this.productPrice = productPrice;
	}

	public String getProductName() {
		return productName;
	}

	public String getProductImageLink() {
		return productImageLink;
	}

	public double getProductPrice() {
		return productPrice;
	}
}
