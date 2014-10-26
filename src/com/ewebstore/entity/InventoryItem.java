package com.ewebstore.entity;

/**
 * This class represents in-stock inventory item of a branch
 * 
 * @author ewebstore.com
 *
 */
public class InventoryItem {
	private String productID;
	private String productName;
	private String brandName;
	private int quantity;

	public InventoryItem(String productID, String productName,
			String brandName, int quantity) {
		this.productID = productID;
		this.productName = productName;
		this.brandName = brandName;
		this.quantity = quantity;
	}

	public String getProductID() {
		return productID;
	}

	public String getProductName() {
		return productName;
	}

	public String getBrandName() {
		return brandName;
	}

	public int getQuantity() {
		return quantity;
	}
}
