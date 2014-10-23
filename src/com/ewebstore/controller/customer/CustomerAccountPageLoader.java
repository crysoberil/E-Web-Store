package com.ewebstore.controller.customer;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ewebstore.entity.Customer;
import com.ewebstore.entity.OrderDisplayInformation;
import com.ewebstore.model.CustomerQueryModel;
import com.ewebstore.model.OrderQueryModel;

public class CustomerAccountPageLoader extends CheckedCustomerPanelServlet {

	@Override
	protected void customerPanelDoGet(HttpServletRequest req,
			HttpServletResponse resp) {
		try {
			if (req.getSession().getAttribute("customerid") == null) {
				req.setAttribute("errorMessage", "You aren't logged in!");
				req.getRequestDispatcher("/WEB-INF/customer/404.jsp").forward(
						req, resp);
			} else {
				String customerID = req.getSession().getAttribute("customerid")
						.toString();
				Customer customer = CustomerQueryModel.getCustomer(customerID);
				ArrayList<OrderDisplayInformation> displayInformation = OrderQueryModel
						.getOrderDisplayInformationByCustomerID(customerID);

				req.setAttribute("customer", customer);
				req.setAttribute("displayInformation", displayInformation);

				req.getRequestDispatcher("/WEB-INF/customer/account.jsp")
						.forward(req, resp);
			}
		} catch (ServletException | IOException ex) {
			ex.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("ERROR");
		}
	}

	@Override
	protected void customerPanelDoPost(HttpServletRequest req,
			HttpServletResponse resp) {
		// TODO Auto-generated method stub

	}

}
