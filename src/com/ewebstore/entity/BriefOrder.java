package com.ewebstore.entity;

import java.sql.Date;

import com.ewebstore.linkgenerators.LinkGenerator;

public class BriefOrder {
	private String orderID;
	private String customerID;
	private Date orderDate;
	private String deliveryLocation;
	private String associatedEmployeeID;
	
	public BriefOrder(String orderID, String customerID, Date orderDate,
			String deliveryLocation, String associatedEmployeeID) {
		this.orderID = orderID;
		this.customerID = customerID;
		this.orderDate = orderDate;
		this.deliveryLocation = deliveryLocation;
		this.associatedEmployeeID = associatedEmployeeID;
	}
	
	public String getOrderID() {
		return orderID;
	}

	public String getCustomerID() {
		return customerID;
	}

	public Date getOrderDate() {
		return orderDate;
	}

	public String getDeliveryLocation() {
		return deliveryLocation;
	}

	public String getAssociatedEmployeeID() {
		return associatedEmployeeID;
	}
	
	public String getOrderPageLink() {
		return LinkGenerator.getOrderLink(orderID);
	}
}
