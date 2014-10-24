package com.ewebstore.entity;

/**
 * The ShoppingCartDisplayInformation class is an entity encapsulating necessary
 * information on shopping carts display.
 * 
 * @author ewebstore.com
 *
 */
public class ShoppingCartDisplayInformation {
	private String productName;
	private String productImageLink;
	private double productPrice;

	public ShoppingCartDisplayInformation(String productName,
			String productImageLink, double productPrice) {
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
