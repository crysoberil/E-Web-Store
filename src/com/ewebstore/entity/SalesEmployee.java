package com.ewebstore.entity;

import java.sql.Date;

import com.ewebstore.model.LinkGenerator;

public class SalesEmployee {
	private String employeeID;
	private String name;
	private boolean gender;
	private String email;
	private String contactnumber;
	private Date dob;
	private Date joinDate;
	private String address;
	private String branchID;
	private String branchName;

	public SalesEmployee(String employeeID, String name, boolean gender,
			String email, String contactnumber, Date dob, Date joinDate,
			String address, String branchID, String branchName) {
		this.employeeID = employeeID;
		this.name = name;
		this.gender = gender;
		this.email = email;
		this.contactnumber = contactnumber;
		this.dob = dob;
		this.joinDate = joinDate;
		this.address = address;
		this.branchID = branchID;
		this.branchName = branchName;
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

	public String getContactnumber() {
		return contactnumber;
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

	public String getSalesEmployeeProfileLink() {
		return LinkGenerator.getSalesEmployeeProfileLink(employeeID);
	}
}
