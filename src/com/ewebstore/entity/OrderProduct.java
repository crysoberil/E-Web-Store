package com.ewebstore.entity;

/**
 * The OrderProduct class is an entity encapsulating necessary information on
 * ordered products.
 * 
 * @author ewebstore.com
 *
 */
public class OrderProduct {
	private String productID;
	private String productName;
	private int orderQuantity;

	public OrderProduct(String productID, String productName, int orderQuantity) {
		this.productID = productID;
		this.productName = productName;
		this.orderQuantity = orderQuantity;
	}

	public String getProductID() {
		return productID;
	}

	public String getProductName() {
		return productName;
	}

	public int getOrderQuantity() {
		return orderQuantity;
	}
}
