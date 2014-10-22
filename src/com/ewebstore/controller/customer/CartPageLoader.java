package com.ewebstore.controller.customer;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ewebstore.entity.CartItem;
import com.ewebstore.entity.ShoppingCart;
import com.ewebstore.entity.ShoppingCartDisplayInformation;
import com.ewebstore.model.ProductQueryModel;
import com.ewebstore.model.ShoppingCartQueryModel;

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

			req.getRequestDispatcher("/WEB-INF/customer/cart.jsp").forward(req,
					resp);
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
