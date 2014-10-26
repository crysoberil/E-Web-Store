package com.ewebstore.controller.customer;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ewebstore.controller.admin.SimpleFeedbackPageLoader;
import com.ewebstore.entity.CartItem;
import com.ewebstore.entity.District;
import com.ewebstore.entity.ShoppingCart;
import com.ewebstore.entity.ShoppingCartDisplayInformation;
import com.ewebstore.linkgenerators.LinkGenerator;
import com.ewebstore.model.DistrictQueryModel;
import com.ewebstore.model.ProductQueryModel;
import com.ewebstore.model.SharedData;
import com.ewebstore.model.ShoppingCartQueryModel;

/**
 * The CheckoutFormLoader class is a servlet handling the loading of the
 * checkout form's page.
 * 
 * @author ewebstore.com
 *
 */
public class CheckoutFormLoader extends CheckedCustomerPanelServlet {
	@Override
	protected void customerPanelDoGet(HttpServletRequest req,
			HttpServletResponse resp) {
		if (!isCustomer(req.getSession())) {
			try {
				resp.sendRedirect(LinkGenerator.customerLoginPageLink());
			} catch (IOException e) {
				SimpleFeedbackPageLoader.showCustomerSimpleFeedbackPage(req,
						resp, "Please Log In", "Log in to continue",
						"You need to log in to continue with checkout");
			}

		} else {
			ShoppingCart cart = (ShoppingCart) req.getSession().getAttribute(
					"cart");

			if (!cart.isEmpty()) {
				try {
					ArrayList<CartItem> cartItems = cart.getCartItems();
					HashMap<String, ShoppingCartDisplayInformation> cartItemsInfo = new HashMap<String, ShoppingCartDisplayInformation>();
					HashMap<String, Double> cartItemsPrice = new HashMap<String, Double>();
					double totalOrderingCost = ShoppingCartQueryModel
							.getTotalOrderingCost(cart);
					double shippingCost = (totalOrderingCost > 0 ? SharedData
							.getShippingCost() : 0);

					ArrayList<District> districts = DistrictQueryModel
							.getAllDistricts();

					for (CartItem cartItem : cartItems) {
						String productID = cartItem.getProductID();

						String productName = ProductQueryModel
								.getProductName(productID);

						String productImageLink = ProductQueryModel
								.getProductImageLink(productID);

						double productPrice = ProductQueryModel
								.getProductPrice(productID);

						cartItemsInfo.put(productID,
								new ShoppingCartDisplayInformation(productName,
										productImageLink, productPrice));

						cartItemsPrice.put(productID, ShoppingCartQueryModel
								.getCartItemCost(cartItem));

					}

					req.setAttribute("cartItems", cartItems);
					req.setAttribute("cartItemsInfo", cartItemsInfo);
					req.setAttribute("cartItemsPrice", cartItemsPrice);
					req.setAttribute("totalOrderingCost", totalOrderingCost);
					req.setAttribute("shippingCost", shippingCost);
					req.setAttribute("districts", districts);

					req.getRequestDispatcher(
							"/WEB-INF/customer/checkoutform.jsp").forward(req,
							resp);
				} catch (ServletException | IOException e) {
					String errorMessage = "Service not available. We are trying to get a fix on this as soon as possible. Please try again later.";

					SimpleFeedbackPageLoader.showCustomerOperationFailedPage(
							req, resp, errorMessage);
				} catch (SQLException ex) {
					ex.printStackTrace();
				}
			} else
				SimpleFeedbackPageLoader.showCustomerSimpleFeedbackPage(req,
						resp, "Empty Cart", "Empty Cart",
						"Your shopping cart is empty.");
		}
	}

	@Override
	protected void customerPanelDoPost(HttpServletRequest req,
			HttpServletResponse resp) {
	}
}
