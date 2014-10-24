package com.ewebstore.controller.customer;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ewebstore.controller.SimpleFeedbackPageLoader;
import com.ewebstore.entity.CartItem;
import com.ewebstore.entity.ShoppingCart;
import com.ewebstore.entity.ShoppingCartDisplayInformation;
import com.ewebstore.model.ProductQueryModel;
import com.ewebstore.model.SharedData;
import com.ewebstore.model.ShoppingCartQueryModel;

/**
 * The CartPageLoader class is a servlet handling the loading of a cart's page.
 * 
 * @author ewebstore.com
 *
 */
public class CartPageLoader extends CheckedCustomerPanelServlet {

	@Override
	protected void customerPanelDoGet(HttpServletRequest req,
			HttpServletResponse resp) {
		try {
			ShoppingCart cart = (ShoppingCart) req.getSession().getAttribute(
					"cart");
			ArrayList<CartItem> cartItems = cart.getCartItems();
			HashMap<String, ShoppingCartDisplayInformation> cartItemsInfo = new HashMap<String, ShoppingCartDisplayInformation>();
			HashMap<String, Double> cartItemsPrice = new HashMap<String, Double>();
			double totalOrderingCost = ShoppingCartQueryModel
					.getTotalOrderingCost(cart);
			double shippingCost = (totalOrderingCost > 0 ? SharedData
					.getShippingCost() : 0);

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

				cartItemsPrice.put(productID,
						ShoppingCartQueryModel.getCartItemCost(cartItem));

			}

			req.setAttribute("cartItems", cartItems);
			req.setAttribute("cartItemsInfo", cartItemsInfo);
			req.setAttribute("cartItemsPrice", cartItemsPrice);
			req.setAttribute("totalOrderingCost", totalOrderingCost);
			req.setAttribute("shippingCost", shippingCost);

			req.getRequestDispatcher("/WEB-INF/customer/cart.jsp").forward(req,
					resp);
		} catch (ServletException | IOException | SQLException ex) {
			String errorMessage = "Service not available. We are trying to get a fix on this as soon as possible. Please try again later.";

			SimpleFeedbackPageLoader.showCustomerOperationFailedPage(req, resp,
					errorMessage);
		}
	}

	@Override
	protected void customerPanelDoPost(HttpServletRequest req,
			HttpServletResponse resp) {
		try {
			ShoppingCart cart = (ShoppingCart) req.getSession().getAttribute(
					"cart");
			String productID = req.getParameter("productid");

			if (req.getParameter("delete") != null)
				cart.removeProduct(productID);
			else if (req.getParameter("change").equals("increment"))
				cart.addToCart(productID);
			else
				cart.removeFromCart(productID);

			ArrayList<CartItem> cartItems = cart.getCartItems();
			HashMap<String, ShoppingCartDisplayInformation> cartItemsInfo = new HashMap<String, ShoppingCartDisplayInformation>();
			HashMap<String, Double> cartItemsPrice = new HashMap<String, Double>();
			double totalOrderingCost = ShoppingCartQueryModel
					.getTotalOrderingCost(cart);
			double shippingCost = (totalOrderingCost > 0 ? SharedData
					.getShippingCost() : 0);

			for (CartItem cartItem : cartItems) {
				String productID2 = cartItem.getProductID();

				String productName = ProductQueryModel
						.getProductName(productID2);

				String productImageLink = ProductQueryModel
						.getProductImageLink(productID2);

				double productPrice = ProductQueryModel
						.getProductPrice(productID2);

				cartItemsInfo.put(productID2,
						new ShoppingCartDisplayInformation(productName,
								productImageLink, productPrice));

				cartItemsPrice.put(productID2,
						ShoppingCartQueryModel.getCartItemCost(cartItem));

			}

			req.setAttribute("cartItems", cartItems);
			req.setAttribute("cartItemsInfo", cartItemsInfo);
			req.setAttribute("cartItemsPrice", cartItemsPrice);
			req.setAttribute("totalOrderingCost", totalOrderingCost);
			req.setAttribute("shippingCost", shippingCost);

			req.getRequestDispatcher("/WEB-INF/customer/cart.jsp").forward(req,
					resp);

		} catch (ServletException | IOException | SQLException ex) {
			String errorMessage = "Service not available. We are trying to get a fix on this as soon as possible. Please try again later.";

			SimpleFeedbackPageLoader.showCustomerOperationFailedPage(req, resp,
					errorMessage);
		}
	}

}
