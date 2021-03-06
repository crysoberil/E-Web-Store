package com.ewebstore.controller.admin;

import java.sql.Date;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ewebstore.model.BranchManagerQueryModel;
import com.ewebstore.model.SalesEmployeeQueryModel;

/**
 * The SubmitAddEmployeeController class is a servlet handling the submission of
 * a new employee information.
 * 
 * @author ewebstore.com
 *
 */
public class SubmitAddEmployeeController extends CheckedHttpServlet {

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
				Boolean gender = (req.getParameter("gender").equals("male"));

				String email = req.getParameter("email");
				if (email == null || email.trim().length() == 0)
					throw new IllegalArgumentException("Email field empty");

				String contactNumber = req.getParameter("contactNumber");
				if (contactNumber == null || contactNumber.trim().length() == 0)
					throw new IllegalArgumentException(
							"Contact Number field empty");

				String dateStr = req.getParameter("dob");
				SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
				java.util.Date utilDate = format.parse(dateStr);
				Date dob = new Date(utilDate.getTime());

				String address = req.getParameter("address");
				if (address == null || address.trim().length() == 0)
					throw new IllegalArgumentException("Address field empty");

				String adminID = req.getSession().getAttribute("adminid")
						.toString();
				String branchID = BranchManagerQueryModel.getBranchID(adminID);

				SalesEmployeeQueryModel.addSalesEmployee(name, gender, email,
						contactNumber, dob, address, branchID);

				SimpleFeedbackPageLoader.showAdminSimpleFeedbackPage(req, resp,
						"Success", "New Employee Added",
						"New employee information added.");

			} catch (IllegalArgumentException ex) {
				SimpleFeedbackPageLoader.showAdminSimpleFeedbackPage(req, resp,
						"Error", "Invalid Input", ex.getMessage());
			} catch (SQLException ex) {
				SimpleFeedbackPageLoader
						.showAdminOperationFailedPage(req, resp);
			} catch (ParseException ex) {
				ex.printStackTrace();
			}
		}
	}

}
