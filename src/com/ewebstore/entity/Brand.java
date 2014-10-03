package com.ewebstore.entity;

public class Brand {
	private String brandID;
	private String brandName;

	public Brand(String brandID, String brandName) {
		super();
		this.brandID = brandID;
		this.brandName = brandName;
	}

	public String getBrandID() {
		return brandID;
	}

	public String getBrandName() {
		return brandName;
	}

}
