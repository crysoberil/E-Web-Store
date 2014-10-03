package com.ewebstore.controller;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ewebstore.entity.SalesEmployee;
import com.ewebstore.model.SalesEmployeeQueryModel;

public class EditEmployeeFormLoader extends CheckedHttpServlet {

	@Override
	protected void checkedDoGet(HttpServletRequest req, HttpServletResponse resp) {
		if (isAdmin(req)) {
			try {
				String employeeID = req.getParameter("employeeid");

				if (!SalesEmployeeQueryModel.isCurrentlyEmployed(employeeID))
					throw new IllegalArgumentException("No such sales employee");

				SalesEmployee salesEmployee = SalesEmployeeQueryModel
						.getSalesEmployee(employeeID);

				req.setAttribute("salesemployee", salesEmployee);

				req.getRequestDispatcher("/WEB-INF/admin/editemployeeform.jsp")
						.forward(req, resp);

			} catch (IllegalArgumentException e) {
				SimpleFeedbackPageLoader.showSimpleFeedbackPage(req, resp,
						"Error", "Invalid Input", e.getMessage());

			} catch (ServletException | IOException ex) {
				ex.printStackTrace();
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

	@Override
	protected void checkedDoPost(HttpServletRequest req,
			HttpServletResponse resp) {
	}
}
