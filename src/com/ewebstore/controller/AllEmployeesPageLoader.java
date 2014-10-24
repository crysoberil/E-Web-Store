package com.ewebstore.controller;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.ewebstore.entity.SalesEmployee;
import com.ewebstore.model.BranchQueryModel;
import com.ewebstore.model.SalesEmployeeQueryModel;

/**
 * The AllEmployeesPageLoader class is a servlet handling the loading of the all
 * employees information page.
 * 
 * @author ewebstore.com
 *
 */
public class AllEmployeesPageLoader extends CheckedHttpServlet {

	@Override
	protected void checkedDoGet(HttpServletRequest req, HttpServletResponse resp) {
		if (isAdmin(req)) {
			try {
				HttpSession session = req.getSession();
				String adminID = session.getAttribute("adminid").toString();
				String branchID = BranchQueryModel.getBranchID(adminID);

				ArrayList<String> allEmployeeIDs = SalesEmployeeQueryModel
						.getAllSalesEmployeeIDs(branchID);

				ArrayList<SalesEmployee> allEmployees = new ArrayList<SalesEmployee>();

				for (String employeeID : allEmployeeIDs)
					allEmployees.add(SalesEmployeeQueryModel
							.getSalesEmployee(employeeID));

				req.setAttribute("allEmployees", allEmployees);

				req.getRequestDispatcher("/WEB-INF/admin/allemployees.jsp")
						.forward(req, resp);
			} catch (ServletException | IOException | SQLException ex) {
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
		// TODO Auto-generated method stub

	}

}
