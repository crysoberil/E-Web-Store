package com.ewebstore.entity;

/**
 * The District class is an entity encapsulating necessary information on
 * districts.
 * 
 * @author ewebstore.com
 *
 */
public class District {
	private String districtName;
	private String districtID;

	public District(String districtID, String districtName) {
		this.districtName = districtName;
		this.districtID = districtID;
	}

	public String getDistrictName() {
		return districtName;
	}

	public String getDistrictID() {
		return districtID;
	}
}
