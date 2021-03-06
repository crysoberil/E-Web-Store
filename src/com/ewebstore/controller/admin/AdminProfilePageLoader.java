package com.ewebstore.controller.admin;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.ewebstore.entity.BranchManager;
import com.ewebstore.model.BranchManagerQueryModel;

/**
 * The AdminProfilePageLoader class is a servlet handling the loading of the
 * admin's profile page.
 * 
 * @author ewebstore.com
 *
 */
public class AdminProfilePageLoader extends CheckedHttpServlet {

	@Override
	protected void checkedDoGet(HttpServletRequest req, HttpServletResponse resp) {
		if (isAdmin(req)) {
			try {
				HttpSession session = req.getSession();
				BranchManager branchManager = BranchManagerQueryModel
						.getBranchManager(session.getAttribute("adminid")
								.toString());

				req.setAttribute("branchmanager", branchManager);
				req.getRequestDispatcher("/WEB-INF/admin/adminprofile.jsp")
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
		// TODO Auto-generated method stub
	}

}
