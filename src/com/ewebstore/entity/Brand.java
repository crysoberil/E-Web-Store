package com.ewebstore.entity;

import com.ewebstore.linkgenerators.LinkGenerator;

public class Brand {
	private String brandID;
	private String brandName;

	public Brand(String brandID, String brandName) {
		this.brandID = brandID;
		this.brandName = brandName;
	}

	public String getBrandID() {
		return brandID;
	}

	public String getBrandName() {
		return brandName;
	}

	public String getBrandPageLink() {
		return LinkGenerator.getBrandPageLink(brandID);
	}
}
