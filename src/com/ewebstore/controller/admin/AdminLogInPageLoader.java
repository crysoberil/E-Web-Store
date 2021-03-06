package com.ewebstore.controller.admin;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * The AdminLogInPageLoader class is a servlet handling the loading of the admin
 * login page.
 * 
 * @author ewebstore.com
 *
 */
public class AdminLogInPageLoader extends CheckedHttpServlet {

	@Override
	protected void checkedDoGet(HttpServletRequest req, HttpServletResponse resp) {
		if (isAdmin(req))
			SimpleFeedbackPageLoader.showAdminSimpleFeedbackPage(req, resp,
					"Log In", "Already logged in",
					"Branch manager is already logged in");
		else {
			forceLogOut(req, resp);

			try {
				req.getRequestDispatcher("/WEB-INF/admin/login.jsp").forward(
						req, resp);
			} catch (IOException | ServletException ex) {
				SimpleFeedbackPageLoader.showOperationFailedPage(req, resp);
			}
		}
	}

	@Override
	protected void checkedDoPost(HttpServletRequest req,
			HttpServletResponse resp) {
		// TODO Auto-generated method stub

	}

}
