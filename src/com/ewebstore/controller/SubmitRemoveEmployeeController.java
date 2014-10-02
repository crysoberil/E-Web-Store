package com.ewebstore.controller;

import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ewebstore.model.SalesEmployeeQueryModel;

public class SubmitRemoveEmployeeController extends CheckedHttpServlet {

	@Override
	protected void checkedDoGet(HttpServletRequest req, HttpServletResponse resp) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void checkedDoPost(HttpServletRequest req,
			HttpServletResponse resp) {
		// TODO Auto-generated method stub

		if (!isAdmin(req))
			SimpleFeedbackPageLoader.showInvalidAccessPage(req, resp);
		else {
			try {
				String employeeID = req.getParameter("employeeid");

				SalesEmployeeQueryModel.removeSalesEmployee(employeeID);

				SimpleFeedbackPageLoader.showSimpleFeedbackPage(req, resp,
						"Success", "Employee Removed",
						"Employee information removed.");

			} catch (IllegalArgumentException e) {
				SimpleFeedbackPageLoader.showSimpleFeedbackPage(req, resp,
						"Error", "Invalid Input", e.getMessage());

			} catch (SQLException e) {
				SimpleFeedbackPageLoader
						.showAdminOperationFailedPage(req, resp);
			}
		}
	}

}
