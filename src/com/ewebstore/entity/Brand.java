package com.ewebstore.entity;

import com.ewebstore.linkgenerators.LinkGenerator;

/**
 * The Brand class is an entity encapsulating necessary information on brands.
 * 
 * @author ewebstore.com
 *
 */
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
