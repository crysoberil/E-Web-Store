package com.ewebstore.entity;

import java.sql.Date;
import java.util.HashMap;

public class OrderDisplayInformation {
	private String orderID;
	private String customerID;
	private HashMap<String, Integer> orderProducts;
	private Date orderDate;
	private String detailedDeliveryLocation;
	private String orderStatus;
	private double totalOrderingCost;

	public OrderDisplayInformation(String orderID, String customerID,
			HashMap<String, Integer> orderProducts, Date orderDate,
			String detailedDeliveryLocation, String orderStatus,
			double totalOrderingCost) {
		super();
		this.orderID = orderID;
		this.customerID = customerID;
		this.orderProducts = orderProducts;
		this.orderDate = orderDate;
		this.detailedDeliveryLocation = detailedDeliveryLocation;
		this.orderStatus = orderStatus;
		this.totalOrderingCost = totalOrderingCost;
	}

	public String getOrderID() {
		return orderID;
	}

	public String getCustomerID() {
		return customerID;
	}

	public HashMap<String, Integer> getOrderProducts() {
		return orderProducts;
	}

	public Date getOrderDate() {
		return orderDate;
	}

	public String getDetailedDeliveryLocation() {
		return detailedDeliveryLocation;
	}

	public String getOrderStatus() {
		return orderStatus;
	}

	public double getTotalOrderingCost() {
		return totalOrderingCost;
	}
}
