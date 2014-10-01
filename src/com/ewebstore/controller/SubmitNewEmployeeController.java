package com.ewebstore.controller;

import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ewebstore.model.BranchManagerQueryModel;
import com.ewebstore.model.BranchQueryModel;
import com.ewebstore.model.SalesEmployeeQueryModel;

public class SubmitNewEmployeeController extends CheckedHttpServlet {

	@Override
	protected void checkedDoGet(HttpServletRequest req, HttpServletResponse resp) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void checkedDoPost(HttpServletRequest req,
			HttpServletResponse resp) {

		if (!isAdmin(req))
			SimpleFeedbackPageLoader.showInvalidAccessPage(req, resp);
		else {
			try {
				String name = req.getParameter("name");
				if (name == null || name.trim().length() == 0)
					throw new IllegalArgumentException("Name field empty");

				if (req.getParameter("gender") == null)
					throw new IllegalArgumentException("Gender field is empty");
				Boolean gender = (req.getParameter("gender") == "male");

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

				String adminID = req.getSession().getAttribute("adminid")
						.toString();
				String branchID = BranchManagerQueryModel.getBranchID(adminID);
				String branchName = BranchQueryModel
						.getBranchNameByID(branchID);

				SalesEmployeeQueryModel.addSalesEmployee(name, gender, email,
						contactNumber, dob, address, branchID, branchName);

				SimpleFeedbackPageLoader.showSimpleFeedbackPage(req, resp,
						"Success", "New Employee Added",
						"New employee infromtaion added.");

			} catch (IllegalArgumentException ex) {
				SimpleFeedbackPageLoader.showSimpleFeedbackPage(req, resp,
						"Error", "Invalid Input", ex.getMessage());
			} catch (SQLException ex) {
				SimpleFeedbackPageLoader
						.showAdminOperationFailedPage(req, resp);
			}
		}
	}

}
