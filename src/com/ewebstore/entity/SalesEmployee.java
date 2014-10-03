package com.ewebstore.entity;

import java.sql.Date;

import com.ewebstore.linkgenerators.LinkGenerator;

public class SalesEmployee {
	private String employeeID;
	private String name;
	private boolean gender;
	private String email;
	private String contactNumber;
	private Date dob;
	private Date joinDate;
	private String address;
	private String branchID;
	private String branchName;
	private boolean currentlyEmployed;

	public SalesEmployee(String employeeID, String name, boolean gender,
			String email, String contactNumber, Date dob, Date joinDate,
			String address, String branchID, String branchName,
			boolean currentlyEmployed) {
		this.employeeID = employeeID;
		this.name = name;
		this.gender = gender;
		this.email = email;
		this.contactNumber = contactNumber;
		this.dob = dob;
		this.joinDate = joinDate;
		this.address = address;
		this.branchID = branchID;
		this.branchName = branchName;
		this.currentlyEmployed = currentlyEmployed;
	}

	public String getEmployeeID() {
		return employeeID;
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

	public String getContactNumber() {
		return contactNumber;
	}

	public Date getDob() {
		return dob;
	}

	public Date getJoinDate() {
		return joinDate;
	}

	public String getAddress() {
		return address;
	}

	public String getBranchID() {
		return branchID;
	}

	public String getBranchName() {
		return branchName;
	}

	public boolean isCurrentlyEmployed() {
		return currentlyEmployed;
	}

	public String getSalesEmployeeProfileLink() {
		return LinkGenerator.getSalesEmployeeProfileLink(employeeID);
	}
}
