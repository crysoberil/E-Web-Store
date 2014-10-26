package com.ewebstore.controller.customer;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ewebstore.controller.admin.SimpleFeedbackPageLoader;

/**
 * The CustomerLoginSignupPageLoader class is a servlet handling the loading of
 * the customer login and signup page.
 * 
 * @author ewebstore.com
 *
 */
public class CustomerLoginSignupPageLoader extends CheckedCustomerPanelServlet {

	@Override
	protected void customerPanelDoGet(HttpServletRequest req,
			HttpServletResponse resp) {
		try {
			req.getRequestDispatcher("/WEB-INF/customer/loginsignup.jsp")
					.forward(req, resp);
		} catch (ServletException | IOException ex) {
			String errorMessage = "Service not available. We are trying to get a fix on this as soon as possible. Please try again later.";

			SimpleFeedbackPageLoader.showCustomerOperationFailedPage(req, resp,
					errorMessage);
		}
	}

	@Override
	protected void customerPanelDoPost(HttpServletRequest req,
			HttpServletResponse resp) {
	}

}
