package com.ewebstore.entity;

import com.ewebstore.linkgenerators.LinkGenerator;

public class ProductCategory {
	String categoryID;
	String categoryName;

	public ProductCategory(String categoryID, String categoryName) {
		this.categoryID = categoryID;
		this.categoryName = categoryName;
	}

	public String getCategoryID() {
		return categoryID;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public String getCategoryCheckBoxName() {
		return "cat" + categoryID;
	}

	public String getCategoryPageLink() {
		return LinkGenerator.getCategoryPageLink(categoryID);
	}
}
