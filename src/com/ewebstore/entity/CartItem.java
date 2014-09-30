package com.ewebstore.entity;

public class CartItem {
	private String productID;
	private int quantity;
	
	public CartItem(String productID, int quantity) {
		this.productID = productID;
		this.quantity = quantity;
	}

	public String getProductID() {
		return productID;
	}

	public int getQuantity() {
		return quantity;
	}
}
