package com.ewebstore.controller;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ewebstore.entity.Branch;
import com.ewebstore.entity.BriefOrder;
import com.ewebstore.model.BranchManagerQueryModel;
import com.ewebstore.model.BranchQueryModel;
import com.ewebstore.model.OrderQueryModel;

// admin dashboard loader
public class AdminHomePageLoader extends CheckedHttpServlet {

	@Override
	protected void checkedDoGet(HttpServletRequest req, HttpServletResponse resp) {
		if (isAdmin(req)) {
			// TODO load admin home
			try {
				String managerID = (String) req.getSession().getAttribute(
						"adminid");
				String branchID = BranchManagerQueryModel
						.getBranchID(managerID);

				if (branchID == null)
					throw new SQLException();

				ArrayList<BriefOrder> briefOrders = OrderQueryModel
						.getOnDeliveryOrders(branchID);
				req.setAttribute("ongoingdeliveries", briefOrders);

				// attach some stats

				String managerName = (String) req.getSession().getAttribute(
						"adminname");
				Branch branch = BranchQueryModel.getBranch(branchID);
				
				int deliveriesCompleted = BranchQueryModel
						.numberOfCompletedDeliveries(branchID, 30);
				double transaction = BranchQueryModel.totalTransaction(
						branchID, 30);
				
				req.setAttribute("managerid", managerID);
				req.setAttribute("managername", managerName);
				req.setAttribute("branch", branch);
				req.setAttribute("deliveriesonlastmonth", deliveriesCompleted);
				req.setAttribute("transactiononlastmonth", transaction);

				req.getRequestDispatcher("/WEB-INF/admin/dashboard.jsp")
						.forward(req, resp);
			} catch (SQLException | ServletException | IOException ex) {
				SimpleFeedbackPageLoader.showOperationFailedPage(req, resp);
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
