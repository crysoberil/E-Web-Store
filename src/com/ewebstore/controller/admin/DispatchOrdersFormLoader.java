package com.ewebstore.controller.admin;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * The DispatchOrdersFormLoader class is a servlet handling the loading of the
 * order dispatch form.
 * 
 * @author ewebstore.com
 *
 */
public class DispatchOrdersFormLoader extends CheckedHttpServlet {

	@Override
	protected void checkedDoGet(HttpServletRequest req, HttpServletResponse resp) {
		if (!isAdmin(req))
			SimpleFeedbackPageLoader.showInvalidAccessPage(req, resp);
		else {
			try {
				req.getRequestDispatcher("/WEB-INF/admin/dispatchorderpage.jsp")
						.forward(req, resp);
			} catch (ServletException | IOException ex) {
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