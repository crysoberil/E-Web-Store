package com.ewebstore.controller.customer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ewebstore.controller.SimpleFeedbackPageLoader;
import com.ewebstore.entity.ShoppingCart;
import com.ewebstore.model.CustomerQueryModel;
import com.ewebstore.model.ShoppingCartQueryModel;

public class PlaceOrderController extends CheckedCustomerPanelServlet {

	@Override
	protected void customerPanelDoGet(HttpServletRequest req,
			HttpServletResponse resp) {
	}

	@Override
	protected void customerPanelDoPost(HttpServletRequest req,
			HttpServletResponse resp) {
		if (isCustomer(req.getSession())) {
			ShoppingCart cart = (ShoppingCart) req.getSession().getAttribute(
					"cart");

			if (cart.isEmpty()) {
				SimpleFeedbackPageLoader.showSimpleFeedbackPage(req, resp,
						"Empty Cart", "Empty Cart",
						"Your shopping cart is empty.");
				return;
			}

			try {
				String email = req.getParameter("email").trim().toLowerCase();
				String password = req.getParameter("password");
				String reTypedPassword = req.getParameter("confirmpassword");
				String deliveryLocation = req.getParameter("deliverylocation")
						.trim();
				String districtID = req.getParameter("districtid").trim();

				try {
					Long.parseLong(districtID);
				} catch (Exception ex) {
					throw new IllegalArgumentException(
							"You must select delivery district");
				}

				if (password == null || reTypedPassword == null
						|| !password.equals(reTypedPassword))
					throw new IllegalArgumentException(
							"Passwords don't match. Please try again.");

				String customerID = CustomerQueryModel.getCustomerID(email,
						password);

				String sessionCustomerID = (String) req.getSession()
						.getAttribute("customerid");
				String sessionCustomerName = (String) req.getSession()
						.getAttribute("customername");

				if (!customerID.equals(sessionCustomerID))
					throw new Exception();

				if (!sessionCustomerID.equals(cart.getCustomerID())
						|| !sessionCustomerName.equals(cart.getCustomerName()))
					throw new IllegalArgumentException("Invalid shopping cart");

				// validation done

				boolean orderPlaced = ShoppingCartQueryModel.placeOrder(cart,
						deliveryLocation, districtID);

				if (!orderPlaced) {
					SimpleFeedbackPageLoader
							.showSimpleFeedbackPage(
									req,
									resp,
									"Order Not Place",
									"Order Could Not Be Placed",
									"Order could not be placed. Some products are out of stock. Please try again later");
				} else {
					SimpleFeedbackPageLoader
							.showSimpleFeedbackPage(
									req,
									resp,
									"Order Placed",
									"Order Placed",
									"Your order has been placed successfully. Products will be delivered within 2 days");
				}

			} catch (IllegalArgumentException ex) {
				SimpleFeedbackPageLoader.showSimpleFeedbackPage(req, resp,
						"Error", "Order Could Not Be Placed", ex.getMessage());
			} catch (Exception ex) {
				SimpleFeedbackPageLoader.showSimpleFeedbackPage(req, resp,
						"Error", "Order Could Not Be Placed",
						"Invalid Parameters");
			}
		} else {
			SimpleFeedbackPageLoader.showInvalidAccessPage(req, resp);
		}
	}
}
