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

	public static String addProductsToStockPageLink() {
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
		return home + "editemployee";
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

	public static String submitProductAdditionToInventory() {
		return home + "addproducttostock.do";
	}

	public static String submitNewEmployeeFormLink() {
		return home + "addemployee.do";
	}

	public static String submitEditEmployeeLink() {
		return home + "editemployeeform.do";
	}

	public static String submitEditEmployeeFormLink() {
		return home + "editemployee.do";
	}

	public static String submitRemoveEmployeeFormLink() {
		return home + "removeemployee.do";
	}

	public static String dispatchOrderSubmissionLink() {
		return home + "dispatchorder.do";
	}

	public static String orderDeliveryConfirmationSubmissionLink() {
		return home + "confirmdelivery.do";
	}

	public static String dispatchInventoryTransferSubmissionLink() {
		return home + "dispatchinventorytransfer.do";
	}

	public static String receiveInventoryTransferSubmissionLink() {
		return home + "receiveinventorytransfer.do";
	}

	// Customer Pages

	public static String customerHomePageLink() {
		return home + "home";
	}

	public static String customerAccountPageLink() {
		return home + "account";
	}

	public static String checkoutFormLink() {
		return home + "checkout";
	}

	public static String cartPageLink() {
		return home + "cart";
	}

	public static String customerLoginPageLink() {
		return home + "customerlogin";
	}

	public static String productsPageLink() {
		return home + "shop";
	}

	public static String searchResultsPageLink() {
		return home + "search";
	}

	public static String getCategoryPageLink(String categoryID) {
		return home + "category?categoryid=" + categoryID;
	}

	public static String getBrandPageLink(String brandID) {
		return home + "brand?brandid=" + brandID;
	}

	public static String getProductPageLink(String productID) {
		return home + "product?productid=" + productID;

	}

	public static String addToCartLink(String productID) {
		return home + "addtocart?productid=" + productID;
	}

	public static String customerLogoutPageLink() {
		return home + "customerlogout.do";
	}
}