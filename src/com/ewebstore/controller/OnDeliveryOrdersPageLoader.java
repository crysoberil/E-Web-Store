package com.ewebstore.controller;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ewebstore.entity.BriefOrder;
import com.ewebstore.model.BranchManagerQueryModel;
import com.ewebstore.model.OrderQueryModel;

public class OnDeliveryOrdersPageLoader extends CheckedHttpServlet {

	@Override
	protected void checkedDoGet(HttpServletRequest req, HttpServletResponse resp) {
		if (!isAdmin(req))
			SimpleFeedbackPageLoader.showInvalidAccessPage(req, resp);
		else {
			try {
				String managerID = (String) req.getSession().getAttribute(
						"adminid");
				String branchID = BranchManagerQueryModel
						.getBranchID(managerID);
				
				if (branchID == null)
					throw new SQLException();
				
				ArrayList<BriefOrder> briefOrders = OrderQueryModel
						.getOnDeliveryOrders(branchID);
				req.setAttribute("brieforders", briefOrders);
				req.setAttribute("pagetype", 1); // represents on delivery
													// orders
				req.setAttribute("pagetitle", "On Delivery Orders");
				req.getRequestDispatcher("/WEB-INF/admin/showbrieforders.jsp")
						.forward(req, resp);
			} catch (SQLException | ServletException | IOException ex) {
				SimpleFeedbackPageLoader.showOperationFailedPage(req, resp);
			}
		}
	}

	@Override
	protected void checkedDoPost(HttpServletRequest req,
			HttpServletResponse resp) {
	}
}
