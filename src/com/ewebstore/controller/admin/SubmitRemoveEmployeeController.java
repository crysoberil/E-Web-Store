package com.ewebstore.controller.admin;

import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ewebstore.model.SalesEmployeeQueryModel;

/**
 * The SubmitRemoveEmployeeController class is a servlet handling the removal of
 * an employee.
 * 
 * @author ewebstore.com
 *
 */
public class SubmitRemoveEmployeeController extends CheckedHttpServlet {

	@Override
	protected void checkedDoGet(HttpServletRequest req, HttpServletResponse resp) {
	}

	@Override
	protected void checkedDoPost(HttpServletRequest req,
			HttpServletResponse resp) {
		if (!isAdmin(req))
			SimpleFeedbackPageLoader.showInvalidAccessPage(req, resp);
		else {
			try {
				String employeeID = req.getParameter("employeeid");

				if (!SalesEmployeeQueryModel.isCurrentlyEmployed(employeeID))
					throw new IllegalArgumentException("No such sales employee");

				SalesEmployeeQueryModel.removeSalesEmployee(employeeID);

				SimpleFeedbackPageLoader.showAdminSimpleFeedbackPage(req, resp,
						"Success", "Employee Removed",
						"Employee information removed.");

			} catch (IllegalArgumentException e) {
				SimpleFeedbackPageLoader.showAdminSimpleFeedbackPage(req, resp,
						"Error", "Invalid Input", e.getMessage());

			} catch (SQLException e) {
				SimpleFeedbackPageLoader
						.showAdminOperationFailedPage(req, resp);
			}
		}
	}

}
