package com.ewebstore.controller.customer;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ewebstore.controller.SimpleFeedbackPageLoader;
import com.ewebstore.entity.Product;
import com.ewebstore.entity.ShoppingCart;
import com.ewebstore.model.ProductQueryModel;

/**
 * The ProductPageLoader class is a servlet handling the loading of a product's
 * page.
 * 
 * @author ewebstore.com
 *
 */
public class ProductPageLoader extends CheckedCustomerPanelServlet {

	@Override
	protected void customerPanelDoGet(HttpServletRequest req,
			HttpServletResponse resp) {

		try {
			String productID = req.getParameter("productid");

			Product product = ProductQueryModel.getProduct(productID);

			req.setAttribute("product", product);

			req.getRequestDispatcher("/WEB-INF/customer/product.jsp").forward(
					req, resp);
		} catch (ServletException | IOException ex) {
			String errorMessage = "Service not available. We are trying to get a fix on this as soon as possible. Please try again later.";

			SimpleFeedbackPageLoader.showCustomerOperationFailedPage(req, resp,
					errorMessage);
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("ERROR");
		}
	}

	@Override
	protected void customerPanelDoPost(HttpServletRequest req,
			HttpServletResponse resp) {
		try {
			ShoppingCart cart = (ShoppingCart) req.getSession().getAttribute(
					"cart");
			String productID = req.getParameter("productid");

			cart.addToCart(productID);

			Product product = ProductQueryModel.getProduct(productID);

			req.setAttribute("product", product);
			req.setAttribute("addedToCart", true);

			req.getRequestDispatcher("/WEB-INF/customer/product.jsp").forward(
					req, resp);
		} catch (ServletException | IOException ex) {
			ex.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("ERROR");
		}
	}

}
