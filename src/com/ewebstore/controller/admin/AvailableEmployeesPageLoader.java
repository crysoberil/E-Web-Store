package com.ewebstore.controller.admin;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.ewebstore.model.BranchQueryModel;
import com.ewebstore.model.SalesEmployeeQueryModel;

/**
 * The AvailableEmployeesPageLoader class is a servlet handling the loading of
 * the available employees information page.
 * 
 * @author ewebstore.com
 *
 */
public class AvailableEmployeesPageLoader extends CheckedHttpServlet {

	@Override
	protected void checkedDoGet(HttpServletRequest req, HttpServletResponse resp) {
		if (isAdmin(req)) {
			try {
				HttpSession session = req.getSession();
				String adminID = session.getAttribute("adminid").toString();
				String branchID = BranchQueryModel.getBranchID(adminID);

				ArrayList<String> availableEmployeeIDs = SalesEmployeeQueryModel
						.getAvailableSalesEmployeeIDs(branchID);

				ArrayList<String> availableEmployeeNames = new ArrayList<String>();
				ArrayList<String> availableEmployeeContactNumbers = new ArrayList<String>();

				for (String availableEmployeeID : availableEmployeeIDs) {
					String availableEmployeeName = SalesEmployeeQueryModel
							.getSalesEmployeeName(availableEmployeeID);
					availableEmployeeNames.add(availableEmployeeName);

					String availableEmployeeContactNumber = SalesEmployeeQueryModel
							.getSalesEmployeeContactNumber(availableEmployeeID);
					availableEmployeeContactNumbers
							.add(availableEmployeeContactNumber);
				}

				req.setAttribute("availableEmployeeIDs", availableEmployeeIDs);
				req.setAttribute("availableEmployeeNames",
						availableEmployeeNames);
				req.setAttribute("availableEmployeeContactNumbers",
						availableEmployeeContactNumbers);

				req.getRequestDispatcher(
						"/WEB-INF/admin/availableemployees.jsp").forward(req,
						resp);
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
