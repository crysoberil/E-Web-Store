package com.ewebstore.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

// admin dashboard loader
public class AdminHomePageLoader extends CheckedHttpServlet {

	@Override
	protected void checkedDoGet(HttpServletRequest req, HttpServletResponse resp) {
		if (isAdmin(req)) {
			// TODO load admin home
			try {
				req.getRequestDispatcher("/WEB-INF/admin/blankpage.jsp")
						.forward(req, resp);
			} catch (ServletException | IOException ex) {
				ex.printStackTrace();
			}
		} else {
			try {
				req.getRequestDispatcher("/WEB-INF/admin/login.jsp").forward(
						req, resp);
			} catch (IOException | ServletException ex) {
				SimpleFeedbackPageLoader
						.showOperationFailedPage(req, resp);
			}
		}
	}

	@Override
	protected void checkedDoPost(HttpServletRequest req,
			HttpServletResponse resp) {
	}

}
