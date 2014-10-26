package com.ewebstore.controller.admin;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * The ConfirmOrderDeliveryPageLoader class is a servlet handling the loading of
 * the delivery confirmation page.
 * 
 * @author ewebstore.com
 *
 */
public class ConfirmOrderDeliveryPageLoader extends CheckedHttpServlet {

	@Override
	protected void checkedDoGet(HttpServletRequest req, HttpServletResponse resp) {
		if (!isAdmin(req))
			SimpleFeedbackPageLoader.showInvalidAccessPage(req, resp);
		else {
			try {
				req.getRequestDispatcher(
						"/WEB-INF/admin/orderdeliveryconfirmationpage.jsp")
						.forward(req, resp);
			} catch (IOException | ServletException ex) {
				SimpleFeedbackPageLoader
						.showAdminOperationFailedPage(req, resp);
			}
		}
	}

	@Override
	protected void checkedDoPost(HttpServletRequest req,
			HttpServletResponse resp) {
	}
}
