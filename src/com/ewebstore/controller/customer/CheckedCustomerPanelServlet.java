package com.ewebstore.controller.customer;

import java.sql.SQLException;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.ewebstore.controller.CheckedHttpServlet;
import com.ewebstore.entity.Brand;
import com.ewebstore.entity.ProductCategory;
import com.ewebstore.model.BrandQueryModel;
import com.ewebstore.model.ProductCategoryQueryModel;
import com.ewebstore.model.SharedData;

public abstract class CheckedCustomerPanelServlet extends CheckedHttpServlet {
	@Override
	protected final void checkedDoGet(HttpServletRequest req,
			HttpServletResponse resp) {
		try {
			HttpSession session = req.getSession();

			boolean loggedIn = isLoggedInAsCustomer(req);

			ArrayList<ProductCategory> categories = ProductCategoryQueryModel
					.getAllProductCategories();

			ArrayList<Brand> popularBrands = BrandQueryModel
					.getPopularBrands(SharedData.getPopularBrandCount());

			req.setAttribute("loggedIn", loggedIn);
			req.setAttribute("categories", categories);
			req.setAttribute("popularBrands", popularBrands);

			customerPanelDoGet(req, resp);
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
	}

	@Override
	protected final void checkedDoPost(HttpServletRequest req,
			HttpServletResponse resp) {
		try {
			HttpSession session = req.getSession();

			boolean loggedIn = isLoggedInAsCustomer(req);

			ArrayList<ProductCategory> categories = ProductCategoryQueryModel
					.getAllProductCategories();

			ArrayList<Brand> popularBrands = BrandQueryModel
					.getPopularBrands(SharedData.getPopularBrandCount());

			req.setAttribute("loggedIn", loggedIn);
			req.setAttribute("categories", categories);
			req.setAttribute("popularBrands", popularBrands);

			customerPanelDoPost(req, resp);
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
	}

	protected boolean isLoggedInAsCustomer(HttpServletRequest req) {
		HttpSession session = req.getSession();

		return session.getAttribute("loggedin") != null
				&& session.getAttribute("customerid") != null
				&& (Boolean) session.getAttribute("loggedin");
	}

	protected abstract void customerPanelDoGet(HttpServletRequest req,
			HttpServletResponse resp);

	protected abstract void customerPanelDoPost(HttpServletRequest req,
			HttpServletResponse resp);
}