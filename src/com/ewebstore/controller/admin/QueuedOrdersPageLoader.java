package com.ewebstore.controller.admin;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ewebstore.entity.BriefOrder;
import com.ewebstore.model.BranchManagerQueryModel;
import com.ewebstore.model.OrderQueryModel;

/**
 * The QueuedOrdersPageLoader class is a servlet handling the loading of the
 * queued orders page.
 * 
 * @author ewebstore.com
 *
 */
public class QueuedOrdersPageLoader extends CheckedHttpServlet {

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
						.getOrdersToDispatch(branchID);
				req.setAttribute("brieforders", briefOrders);
				req.setAttribute("pagetype", 0); // represents unhandled orders
				req.setAttribute("pagetitle", "Queued Orders");
				req.getRequestDispatcher("/WEB-INF/admin/showbrieforders.jsp")
						.forward(req, resp);
			} catch (SQLException | ServletException | IOException ex) {
				SimpleFeedbackPageLoader
						.showAdminOperationFailedPage(req, resp);
			}
		}
	}

	@Override
	protected void checkedDoPost(HttpServletRequest req,
			HttpServletResponse resp) {
	}
}
