package com.ewebstore.controller.customer;

import java.sql.Date;
import java.sql.SQLException;
import java.text.SimpleDateFormat;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ewebstore.controller.CheckedHttpServlet;
import com.ewebstore.controller.SimpleFeedbackPageLoader;
import com.ewebstore.model.CustomerQueryModel;

public class CustomerSignupController extends CheckedHttpServlet {

	@Override
	protected void checkedDoGet(HttpServletRequest req, HttpServletResponse resp) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void checkedDoPost(HttpServletRequest req,
			HttpServletResponse resp) {
		forceLogOut(req, resp);

		try {
			String customerName = req.getParameter("name").trim();
			String email = req.getParameter("email").trim();
			String password = req.getParameter("password");
			String genderStr = req.getParameter("gender").trim().toLowerCase();
			String dateStr = req.getParameter("dob");
			String address = req.getParameter("address").trim();
			String contactNumber = req.getParameter("contactnumber");

			boolean isMale;

			if (genderStr.startsWith("m"))
				isMale = true;
			else if (genderStr.startsWith("f"))
				isMale = false;
			else
				throw new IllegalArgumentException("Invalid entry for gender");

			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			java.util.Date utilDate = format.parse(dateStr);
			Date dob = new Date(utilDate.getTime());

			CustomerQueryModel.addCustomer(customerName, email, password,
					isMale, dob, address, contactNumber);

			SimpleFeedbackPageLoader.showSimpleFeedbackPage(req, resp,
					"Account created", "Registration complete",
					"Registration complete. Please log in to continue.");

		} catch (SQLException ex) {
			SimpleFeedbackPageLoader.showSimpleFeedbackPage(req, resp, "Error",
					"Account not created",
					"Invalid entry or email already registered");
		} catch (IllegalArgumentException ex) {
			SimpleFeedbackPageLoader.showSimpleFeedbackPage(req, resp, "Error",
					"Account not created", ex.getMessage());
		} catch (Exception ex) {
			SimpleFeedbackPageLoader.showSimpleFeedbackPage(req, resp, "Error",
					"Account not created", "Invalid entry. Please try again.");
		}
	}
}