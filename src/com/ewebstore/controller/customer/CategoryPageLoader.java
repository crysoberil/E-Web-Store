package com.ewebstore.controller.customer;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ewebstore.controller.SimpleFeedbackPageLoader;
import com.ewebstore.entity.Product;
import com.ewebstore.model.ProductCategoryQueryModel;
import com.ewebstore.model.ProductQueryModel;

public class CategoryPageLoader extends CheckedCustomerPanelServlet {

	@Override
	protected void customerPanelDoGet(HttpServletRequest req,
			HttpServletResponse resp) {
		try {
			String categoryID = req.getParameter("categoryid");

			String categoryName = ProductCategoryQueryModel
					.getCategoryName(categoryID);
			ArrayList<Product> products = ProductQueryModel
					.getProductsByCategory(categoryID);

			req.setAttribute("categoryName", categoryName);
			req.setAttribute("products", products);

			req.getRequestDispatcher("/WEB-INF/customer/category.jsp").forward(
					req, resp);
		} catch (ServletException | IOException | SQLException ex) {
			String errorMessage = "Service not available. We are trying to get a fix on this as soon as possible. Please try again later.";

			SimpleFeedbackPageLoader.showCustomerOperationFailedPage(req, resp,
					errorMessage);
		}
	}

	@Override
	protected void customerPanelDoPost(HttpServletRequest req,
			HttpServletResponse resp) {
		// TODO Auto-generated method stub

	}
}
