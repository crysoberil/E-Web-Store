package com.ewebstore.entity;

import java.sql.Date;

public class BranchManager {
	private String managerID;
	private String branchID;
	private String branchName;
	private String name;
	private boolean gender;
	private String email;
	private String address;
	private Date dob;
	private String contactNumber;

	public BranchManager(String managerID, String branchID, String branchName,
			String name, boolean gender, String email, String address,
			Date dob, String contactNumber) {
		super();
		this.managerID = managerID;
		this.branchID = branchID;
		this.branchName = branchName;
		this.name = name;
		this.gender = gender;
		this.email = email;
		this.address = address;
		this.dob = dob;
		this.contactNumber = contactNumber;
	}

	public String getManagerID() {
		return managerID;
	}

	public String getBranchID() {
		return branchID;
	}

	public String getBranchName() {
		return branchName;
	}

	public String getName() {
		return name;
	}

	public boolean isGender() {
		return gender;
	}

	public String getEmail() {
		return email;
	}

	public String getAddress() {
		return address;
	}

	public Date getDob() {
		return dob;
	}

	public String getContactNumber() {
		return contactNumber;
	}
}
