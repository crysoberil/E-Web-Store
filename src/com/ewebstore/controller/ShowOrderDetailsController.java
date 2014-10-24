package com.ewebstore.controller;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ewebstore.entity.Order;
import com.ewebstore.model.OrderQueryModel;

public class ShowOrderDetailsController extends CheckedHttpServlet {

	@Override
	protected void checkedDoGet(HttpServletRequest req, HttpServletResponse resp) {
		if (!isAdmin(req))
			SimpleFeedbackPageLoader.showInvalidAccessPage(req, resp);
		else {
			String orderID = req.getParameter("orderid");

			try {
				Order order = OrderQueryModel.getOrderByID(orderID);

				if (order == null)
					throw new SQLException();

				req.setAttribute("order", order);

				req.getRequestDispatcher("/WEB-INF/admin/showorder.jsp")
						.forward(req, resp);
			} catch (SQLException ex) {
				SimpleFeedbackPageLoader.showAdminSimpleFeedbackPage(req, resp,
						"Invalid Order", "Invalid Order ID",
						"No such order exists");
			} catch (ServletException | IOException ex) {
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
