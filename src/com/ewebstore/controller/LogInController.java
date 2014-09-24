package com.ewebstore.controller;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.ewebstore.model.BranchManagerQueryModel;
import com.ewebstore.model.CustomerQueryModel;
import com.ewebstore.model.LinkGenerator;

public class LogInController extends CheckedHttpServlet {
	@Override
	public void checkedDoGet(HttpServletRequest req, HttpServletResponse resp) {
		// TODO Auto-generated method stub
		// Impossible case scenario
	}

	@Override
	public void checkedDoPost(HttpServletRequest req, HttpServletResponse resp) {
		if (isLoggedIn(req.getSession()))
			forceLogOut(req, resp);

		String logInType = req.getParameter("logintype");

		if (logInType != null && logInType.equals("customer"))
			customerLogIn(req, resp);
		else if (logInType != null && logInType.equals("admin"))
			branchManagerLogIn(req, resp);
		else
			SimpleFeedbackPageLoader.showSimpleFeedbackPage(req, resp, "Error",
					"Failed to log in", "Invalid input");
	}

	private void branchManagerLogIn(HttpServletRequest req,
			HttpServletResponse resp) {
		String email = req.getParameter("email").toLowerCase();
		String password = req.getParameter("password").toLowerCase();

		String managerID = null;

		try {
			managerID = BranchManagerQueryModel.getManagerId(email, password);
		} catch (SQLException ex) {
			System.err.println("could not log in");
			ex.printStackTrace();
		}

		if (managerID == null) {
			SimpleFeedbackPageLoader.showSimpleFeedbackPage(req, resp, "Error",
					"Branch manager not found", "Email or password is wrong");
		} else {

			String managerName = BranchManagerQueryModel
					.getManagerName(managerID);

			Cookie adminIdCookie = new Cookie("adminid", managerID);
			Cookie adminNameCookie = new Cookie("adminname", managerName);
			Cookie adminStatusID = new Cookie("isadmin", "true");

			adminIdCookie.setMaxAge(604800);
			adminNameCookie.setMaxAge(604800);
			adminStatusID.setMaxAge(604800);

			resp.addCookie(adminIdCookie);
			resp.addCookie(adminNameCookie);
			resp.addCookie(adminStatusID);

			HttpSession session = req.getSession();

			session.setAttribute("isadmin", true);
			session.setAttribute("adminid", managerID);
			session.setAttribute("adminname", managerName);
			session.setAttribute("loggedin", true);

			try {
				resp.sendRedirect(LinkGenerator.getAdminHomeLink());
			} catch (IOException e) {
				System.err.println("could not redirect");
				e.printStackTrace();
			}
		}
	}

	private void customerLogIn(HttpServletRequest req, HttpServletResponse resp) {
		String email = req.getParameter("email").toLowerCase();
		String password = req.getParameter("password").toLowerCase();

		String customerID = null;

		try {
			customerID = CustomerQueryModel.getCustomerID(email, password);
		} catch (SQLException ex) {
			System.err.println("could not log in");
			ex.printStackTrace();
		}

		if (customerID == null) {
			SimpleFeedbackPageLoader.showSimpleFeedbackPage(req, resp, "Error",
					"Customer not found", "Email or password is wrong");
		} else {

			String customerName = null;

			try {
				customerName = CustomerQueryModel.getCustomerName(customerID);
			} catch (SQLException ex) {
			}

			Cookie customerIdCookie = new Cookie("customerid", customerID);
			Cookie customerNameCookie = new Cookie("customername", customerName);
			Cookie adminStatusID = new Cookie("isadmin", "false");

			customerIdCookie.setMaxAge(604800);
			customerNameCookie.setMaxAge(604800);
			adminStatusID.setMaxAge(604800);

			resp.addCookie(customerIdCookie);
			resp.addCookie(customerNameCookie);
			resp.addCookie(adminStatusID);

			HttpSession session = req.getSession();

			session.setAttribute("customername", customerName);
			session.setAttribute("customerid", customerID);
			session.setAttribute("isadmin", false);
			session.setAttribute("loggedin", true);

			try {
				resp.sendRedirect(LinkGenerator.getHomeLink());
			} catch (IOException e) {
				System.err.println("could not redirect");
				e.printStackTrace();
			}
		}
	}
}
