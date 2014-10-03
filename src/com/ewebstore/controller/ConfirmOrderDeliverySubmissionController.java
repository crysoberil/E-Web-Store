package com.ewebstore.controller;

import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ewebstore.model.OrderQueryModel;

public class ConfirmOrderDeliverySubmissionController extends
		CheckedHttpServlet {

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
				String orderID = null;
				boolean successfulDelivery = false;

				try {
					orderID = req.getParameter("orderid").trim();
					Long.parseLong(orderID);
					successfulDelivery = req.getParameter("deliverycompletion")
							.equals("successful");
				} catch (Exception ex) {
					throw new IllegalArgumentException("Invalid entry");
				}

				OrderQueryModel.confirmOrderDelivery(orderID,
						successfulDelivery);

				SimpleFeedbackPageLoader.showSimpleFeedbackPage(req, resp,
						"Success", "Order Delivery Status Updated", String
								.format("Order %s marked as %s delivery",
										orderID,
										successfulDelivery ? "successful"
												: "failed"));
			} catch (IllegalArgumentException | SQLException ex) {
				SimpleFeedbackPageLoader.showSimpleFeedbackPage(req, resp,
						"Error", "Invalid entry",
						"Invalid form entry. Try again.");
			}
		}
	}
}
