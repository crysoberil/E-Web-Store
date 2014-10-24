package com.ewebstore.entity;

import com.ewebstore.linkgenerators.LinkGenerator;

/**
 * The CartItem class is an entity encapsulating necessary information on brief
 * cart items.
 * 
 * @author ewebstore.com
 *
 */
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

	public String getCartProductPageLink() {
		return LinkGenerator.getProductPageLink(productID);
	}

	@Override
	public String toString() {
		return "ID = " + productID + ", quantity = " + quantity;
	}
}
