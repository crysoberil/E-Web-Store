package com.ewebstore.controller;

import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ewebstore.model.OrderQueryModel;

public class SubmitDispatchOrderController extends CheckedHttpServlet {

	@Override
	protected void checkedDoGet(HttpServletRequest req, HttpServletResponse resp) {
	}

	@Override
	protected void checkedDoPost(HttpServletRequest req,
			HttpServletResponse resp) {
		if (!isAdmin(req))
			SimpleFeedbackPageLoader.showInvalidAccessPage(req, resp);
		else {
			try {
				String orderID = req.getParameter("orderid");
				String employeeID = req.getParameter("employeeid");

				try {
					orderID = orderID.trim();
					employeeID = employeeID.trim();
					Long.parseLong(orderID);
					Long.parseLong(employeeID);

				} catch (Exception ex) {
					throw new IllegalArgumentException();
				}

				OrderQueryModel.dispatchOrder(orderID, employeeID);

				SimpleFeedbackPageLoader.showAdminSimpleFeedbackPage(req, resp,
						"Success", "Database Updated", String.format(
								"Employee %s is associated with order %s",
								employeeID, orderID));
			} catch (SQLException ex) {
				SimpleFeedbackPageLoader
						.showAdminOperationFailedPage(req, resp);
			} catch (IllegalArgumentException ex) {
				SimpleFeedbackPageLoader.showAdminSimpleFeedbackPage(req, resp,
						"Error", "Invalid Entry",
						"Invalid entry for order id or employee id");
			}
		}
	}
}