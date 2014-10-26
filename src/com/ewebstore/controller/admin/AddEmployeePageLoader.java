package com.ewebstore.controller.admin;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * The AddEmployeePageLoader class is a servlet handling the loading of the new
 * employee page.
 * 
 * @author ewebstore.com
 *
 */
public class AddEmployeePageLoader extends CheckedHttpServlet {
	@Override
	protected void checkedDoGet(HttpServletRequest req, HttpServletResponse resp) {
		if (isAdmin(req)) {
			try {
				req.getRequestDispatcher("/WEB-INF/admin/addemployee.jsp")
						.forward(req, resp);
			} catch (ServletException | IOException ex) {
				ex.printStackTrace();
			}
		} else {
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
	}
}
