package com.ewebstore.entity;

/**
 * The Branch class is an entity encapsulating necessary information on
 * branches.
 * 
 * @author ewebstore.com
 *
 */
public class Branch {
	private String branchID;
	private String branchName;
	private String branchLocation;
	private String branchDistrictID;
	private String branchDistrictName;

	public Branch(String branchID, String branchName, String branchLocation,
			String branchDistrictID, String branchDistrictName) {
		this.branchID = branchID;
		this.branchName = branchName;
		this.branchLocation = branchLocation;
		this.branchDistrictID = branchDistrictID;
		this.branchDistrictName = branchDistrictName;
	}

	public String getBranchID() {
		return branchID;
	}

	public String getBranchName() {
		return branchName;
	}

	public String getBranchLocation() {
		return branchLocation;
	}

	public String getBranchDistrictID() {
		return branchDistrictID;
	}

	public String getBranchDistrictName() {
		return branchDistrictName;
	}
}
