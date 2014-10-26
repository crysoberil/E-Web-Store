package com.ewebstore.controller.customer;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.ewebstore.controller.admin.CheckedHttpServlet;
import com.ewebstore.controller.admin.SimpleFeedbackPageLoader;
import com.ewebstore.entity.Brand;
import com.ewebstore.entity.Product;
import com.ewebstore.entity.ProductCategory;
import com.ewebstore.model.BrandQueryModel;
import com.ewebstore.model.ProductCategoryQueryModel;
import com.ewebstore.model.ProductQueryModel;
import com.ewebstore.model.SharedData;

/**
 * The CustomerHomePageLoader class is a servlet handling the loading of the
 * customer home page.
 * 
 * @author ewebstore.com
 *
 */
public class CustomerHomePageLoader extends CheckedCustomerPanelServlet {

	@Override
	protected void customerPanelDoGet(HttpServletRequest req,
			HttpServletResponse resp) {
		try {
			HttpSession session = req.getSession();

			ArrayList<Product> popularProducts = ProductQueryModel
					.getPopularProducts(SharedData.getPopularProductCount());

			ArrayList<Product> recommendedProducts = null;
			if (isLoggedInAsCustomer(req)) {
				recommendedProducts = ProductQueryModel.getRecommendedProducts(
						session.getAttribute("customerid").toString(),
						SharedData.getRecommendedProductCount());
			}

			req.setAttribute("popularProducts", popularProducts);
			req.setAttribute("recommendedProducts", recommendedProducts);

			req.getRequestDispatcher("/WEB-INF/customer/index.jsp").forward(
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
		// TODO Auto-generated method stub

	}

}
