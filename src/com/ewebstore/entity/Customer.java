package com.ewebstore.entity;

import java.sql.Date;

public class Customer {
	private String customerID;
	private String name;
	private Date dob;
	private boolean isMale;
	private String email;
	private String address;
	private String contactNumber;
	private Date registrationDate;
	private boolean isPremiumCustomer;

	public Customer(String customerID, String name, Date dob, boolean isMale,
			String email, String address, String contactNumber,
			Date registrationDate, boolean isPremiumCustomer) {
		super();
		this.customerID = customerID;
		this.name = name;
		this.dob = dob;
		this.isMale = isMale;
		this.email = email;
		this.address = address;
		this.contactNumber = contactNumber;
		this.registrationDate = registrationDate;
		this.isPremiumCustomer = isPremiumCustomer;
	}

	public String getCustomerID() {
		return customerID;
	}

	public String getName() {
		return name;
	}

	public Date getDob() {
		return dob;
	}

	public boolean isMale() {
		return isMale;
	}

	public String getEmail() {
		return email;
	}

	public String getAddress() {
		return address;
	}

	public String getContactNumber() {
		return contactNumber;
	}

	public Date getRegistrationDate() {
		return registrationDate;
	}

	public boolean isPremiumCustomer() {
		return isPremiumCustomer;
	}
}
