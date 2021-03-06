package com.ewebstore.entity;

import java.sql.Date;
import java.util.ArrayList;

import com.ewebstore.linkgenerators.LinkGenerator;

/**
 * The Order class is an entity encapsulating necessary information on orders.
 * 
 * @author ewebstore.com
 *
 */
public class Order {
	private String orderID;
	private String customerID;
	private String customerName;
	private Date orderDate;
	private String deliveryLocation;
	private String orderStatusID;
	private String orderStatus;
	private String orderBranchID;
	private String orderBranchName;
	private String associatedEmployeeID;
	private String associatedEmployeeName;
	private double totalOrderingCost;

	private ArrayList<OrderProduct> orderProducts;

	public Order(String orderID, String customerID, String customerName,
			Date orderDate, String deliveryLocation, String orderStatusID,
			String orderStatus, String orderBranchID, String orderBranchName,
			String associatedEmployeeID, String associatedEmployeeName,
			double totalOrderingCost, ArrayList<OrderProduct> orderProducts) {
		super();
		this.orderID = orderID;
		this.customerID = customerID;
		this.customerName = customerName;
		this.orderDate = orderDate;
		this.deliveryLocation = deliveryLocation;
		this.orderStatusID = orderStatusID;
		this.orderStatus = orderStatus;
		this.orderBranchID = orderBranchID;
		this.orderBranchName = orderBranchName;
		this.associatedEmployeeID = associatedEmployeeID;
		this.associatedEmployeeName = associatedEmployeeName;
		this.totalOrderingCost = totalOrderingCost;
		this.orderProducts = orderProducts;
	}

	public String getOrderID() {
		return orderID;
	}

	public String getCustomerID() {
		return customerID;
	}

	public String getCustomerName() {
		return customerName;
	}

	public Date getOrderDate() {
		return orderDate;
	}

	public String getDeliveryLocation() {
		return deliveryLocation;
	}

	public String getOrderStatusID() {
		return orderStatusID;
	}

	public String getOrderStatus() {
		return orderStatus;
	}

	public String getOrderBranchID() {
		return orderBranchID;
	}

	public String getOrderBranchName() {
		return orderBranchName;
	}

	public String getAssociatedEmployeeID() {
		return associatedEmployeeID;
	}

	public String getAssociatedEmployeeName() {
		return associatedEmployeeName;
	}

	public double getTotalOrderingCost() {
		return totalOrderingCost;
	}

	public ArrayList<OrderProduct> getOrderProducts() {
		return orderProducts;
	}

	public String getOrderPageLink() {
		return LinkGenerator.getOrderLink(orderID);
	}
}
