package com.ewebstore.linkgenerators;

public class LinkGenerator {
	private static final String home = "/E-Web-Store/";

	public static String getHomeLink() {
		return home;
	}

	public static String getAdminHomeLink() {
		return home + "admin";
	}

	public static String getAdminProfileLink() {
		return home + "adminprofile";
	}

	public static String getProductLink(String productID) {
		return home + "product?productid=" + productID;
	}

	public static String getOrderLink(String orderID) {
		return home + "order?orderid=" + orderID;
	}

	public static String getSalesEmployeeProfileLink(String employeeID) {
		return home + "employee?employeeid=" + employeeID;
	}

	public static String logInSubmissionLink() {
		return home + "login.do";
	}

	public static String logOutSubmissionLink() {
		return home + "logout.do";
	}

	public static String adminProfileLink() {
		return home + "adminprofile";
	}

	public static String employeeProfileLink(String employeeID) {
		return home + "employee?employeeid=" + employeeID;
	}

	public static String customerProfileLink(String customerID) {
		return home + "customer?customerid=" + customerID;
	}

	public static String queuedOrdersPageLink() {
		return home + "queuedorders";
	}

	public static String adminDashBoardLink() {
		return home + "dashboard";
	}

	public static String addProductsToStockLink() {
		return home + "addproducttostock";
	}

	public static String addGenericProductPageLink() {
		return home + "addgenericproduct";
	}

	public static String genericProductSearchPageLink() {
		return home + "searchgenericproduct";
	}

	public static String dispatchOrderPageLink() {
		return home + "dispatchorders";
	}

	public static String onDeliveryOrdersPageLink() {
		return home + "ondeliveryorders";
	}

	public static String confirmOrderDeliveryPageLink() {
		return home + "confirmdelivery";
	}

	public static String deliveredOrdersPageLink() {
		return home + "completedeliveries";
	}

	public static String addEmployeePageLink() {
		return home + "addemployee";
	}

	public static String editEmployeePageLink() {
		return home + "editemployeeinformation";
	}

	public static String removeEmployeePageLink() {
		return home + "removeemployee";
	}

	public static String availableEmployeesPageLink() {
		return home + "availableemployees";
	}

	public static String allEmployeesPageLink() {
		return home + "allemployees";
	}

	public static String productsSendRequestsPageLink() {
		return home + "productssendrequests";
	}

	public static String productsReceiveRequestsPageLink() {
		return home + "productsreceiverequests";
	}
	
	public static String submitNewProductFormLink() {
		return home + "addgenericproduct.do";
	}
}