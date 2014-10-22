package com.ewebstore.controller.customer;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ewebstore.controller.SimpleFeedbackPageLoader;

public class CustomerLoginSignupPageLoader extends CheckedCustomerPanelServlet {

	@Override
	protected void customerPanelDoGet(HttpServletRequest req,
			HttpServletResponse resp) {
		try {
			req.getRequestDispatcher("/WEB-INF/customer/loginsignup.jsp")
					.forward(req, resp);
		} catch (ServletException | IOException ex) {
			SimpleFeedbackPageLoader.showOperationFailedPage(req, resp);
		}
	}

	@Override
	protected void customerPanelDoPost(HttpServletRequest req,
			HttpServletResponse resp) {
		// TODO Auto-generated method stub

	}

}
