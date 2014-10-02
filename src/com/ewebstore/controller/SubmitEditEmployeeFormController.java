package com.ewebstore.controller;

import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.ewebstore.model.SalesEmployeeQueryModel;

public class SubmitEditEmployeeFormController extends CheckedHttpServlet {

	@Override
	protected void checkedDoGet(HttpServletRequest req, HttpServletResponse resp) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void checkedDoPost(HttpServletRequest req,
			HttpServletResponse resp) {
		if (isAdmin(req)) {
			try {
				String employeeID = req.getParameter("employeeid");

				String name = req.getParameter("name");
				if (name == null || name.trim().length() == 0)
					throw new IllegalArgumentException("Name field empty");

				if (req.getParameter("gender") == null)
					throw new IllegalArgumentException("Gender field is empty");
				Boolean gender = (req.getParameter("gender").equals("male"));

				String email = req.getParameter("email");
				if (email == null || email.trim().length() == 0)
					throw new IllegalArgumentException("Email field empty");

				String contactNumber = req.getParameter("contactNumber");
				if (contactNumber == null || contactNumber.trim().length() == 0)
					throw new IllegalArgumentException(
							"Contact Number field empty");

				Date dob = null;

				String address = req.getParameter("address");
				if (address == null || address.trim().length() == 0)
					throw new IllegalArgumentException("Address field empty");

				SalesEmployeeQueryModel.updateSalesEmployee(employeeID, name,
						gender, email, contactNumber, dob, address);

				SimpleFeedbackPageLoader.showSimpleFeedbackPage(req, resp,
						"Success", "Employee Updated",
						"Employee information updated.");

			} catch (IllegalArgumentException e) {
				SimpleFeedbackPageLoader.showSimpleFeedbackPage(req, resp,
						"Error", "Invalid Input", e.getMessage());
			} catch (SQLException e) {
				SimpleFeedbackPageLoader
						.showAdminOperationFailedPage(req, resp);
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

}
