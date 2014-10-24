package com.ewebstore.entity;

import com.ewebstore.linkgenerators.LinkGenerator;

/**
 * The GenericProduct class is an entity encapsulating necessary information on
 * generic products.
 * 
 * @author ewebstore.com
 *
 */
public class GenericProduct {

	private String productID;
	private String productName;
	private String brand;
	private String categoryID;
	private String category;
	private String productDetail;
	private String productImageLink;

	public GenericProduct(String productID, String productName, String brand,
			String categoryID, String category, String productDetail,
			String productImageLink) {
		this.productID = productID;
		this.productName = productName;
		this.brand = brand;
		this.categoryID = categoryID;
		this.category = category;
		this.productDetail = productDetail;
		this.productImageLink = productImageLink;
	}

	public String getProductID() {
		return productID;
	}

	public String getProductName() {
		return productName;
	}

	public String getBrand() {
		return brand;
	}

	public String getCategoryID() {
		return categoryID;
	}

	public String getProductDetail() {
		return productDetail;
	}

	public String getProductImageLink() {
		return productImageLink;
	}

	public String getCategory() {
		return category;
	}

	public String getProductLink() {
		return LinkGenerator.getProductLink(productID);
	}
}
