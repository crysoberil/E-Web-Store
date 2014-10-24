package com.ewebstore.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * The SimpleFeedbackPageLoader class is a servlet handling the loading of the
 * various feedback pages.
 * 
 * @author ewebstore.com
 *
 */
public class SimpleFeedbackPageLoader {
	public static void showSimpleFeedbackPage(HttpServletRequest req,
			HttpServletResponse resp, String title, String header, String body) {
		req.setAttribute("title", title);
		req.setAttribute("header", header);
		req.setAttribute("body", body);

		try {
			req.getRequestDispatcher("/WEB-INF/simple_feedback.jsp").forward(
					req, resp);
		} catch (ServletException | IOException e) {
			e.printStackTrace();
		}
	}

	public static void showOperationFailedPage(HttpServletRequest req,
			HttpServletResponse resp) {
		showSimpleFeedbackPage(
				req,
				resp,
				"Error",
				"Operation failed",
				"Service not available. We are trying to get a fix on this as soon as possible. Please try again later.");
	}

	public static void showInvalidAccessPage(HttpServletRequest req,
			HttpServletResponse resp) {
		showSimpleFeedbackPage(req, resp, "Error", "Invalid access",
				"You don't have persmission to view this page.");
	}

	public static void showCustomerSimpleFeedbackPage(HttpServletRequest req,
			HttpServletResponse resp, String title, String header, String body) {

		req.setAttribute("title", title);
		req.setAttribute("header", header);
		req.setAttribute("body", body);

		try {
			req.getRequestDispatcher("/WEB-INF/customer/simple_feedback.jsp")
					.forward(req, resp);
		} catch (ServletException | IOException e) {
			e.printStackTrace();
		}
	}

	public static void showCustomerOperationFailedPage(HttpServletRequest req,
			HttpServletResponse resp, String errorMessage) {

		req.setAttribute("errorMessage", errorMessage);

		try {
			req.getRequestDispatcher("/WEB-INF/customer/404.jsp").forward(req,
					resp);
		} catch (ServletException | IOException e) {
			e.printStackTrace();
		}
	}

	public static void showCustomerInvalidAccessPage(HttpServletRequest req,
			HttpServletResponse resp) {
		showCustomerSimpleFeedbackPage(req, resp, "Error", "Invalid access",
				"You don't have persmission to view this page.");
	}

	public static void showAdminSimpleFeedbackPage(HttpServletRequest req,
			HttpServletResponse resp, String title, String header, String body) {

		req.setAttribute("title", title);
		req.setAttribute("header", header);
		req.setAttribute("body", body);

		try {
			req.getRequestDispatcher("/WEB-INF/admin/simple_feedback_admin.jsp")
					.forward(req, resp);
		} catch (ServletException | IOException e) {
			e.printStackTrace();
		}
	}

	public static void showAdminOperationFailedPage(HttpServletRequest req,
			HttpServletResponse resp) {
		showAdminSimpleFeedbackPage(
				req,
				resp,
				"Error",
				"Operation failed",
				"Service not available. We are trying to get a fix on this as soon as possible. Please try again later.");
	}

	public static void showAdminInvalidAccessPage(HttpServletRequest req,
			HttpServletResponse resp) {
		showAdminSimpleFeedbackPage(req, resp, "Error", "Invalid access",
				"You don't have persmission to view this page.");
	}
}
