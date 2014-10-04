package com.ewebstore.controller.customer;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.ewebstore.controller.CheckedHttpServlet;
import com.ewebstore.entity.Brand;
import com.ewebstore.entity.Product;
import com.ewebstore.entity.ProductCategory;
import com.ewebstore.model.BrandQueryModel;
import com.ewebstore.model.ProductCategoryQueryModel;
import com.ewebstore.model.ProductQueryModel;

public class CustomerHomePageLoader extends CheckedHttpServlet {
	private static int popularBrandCount = 7;
	private static int popularProductCount = 6;
	private static int recommendedProductCount = 3;

	@Override
	protected void checkedDoGet(HttpServletRequest req, HttpServletResponse resp) {
		try {
			HttpSession session = req.getSession();

			boolean loggedIn = false;

			ArrayList<ProductCategory> categories = ProductCategoryQueryModel
					.getAllProductCategories();

			ArrayList<Brand> popularBrands = BrandQueryModel
					.getPopularBrands(popularBrandCount);

			ArrayList<Product> popularProducts = ProductQueryModel
					.getPopularProducts(popularProductCount);

			ArrayList<Product> recommendedProducts = null;
			if (session.getAttribute("loggedin") != null
					&& (Boolean) session.getAttribute("loggedin") == true) {
				loggedIn = true;

				recommendedProducts = ProductQueryModel.getRecommendedProducts(
						session.getAttribute("customerid").toString(),
						recommendedProductCount);
			}

			req.setAttribute("loggedIn", loggedIn);
			req.setAttribute("categories", categories);
			req.setAttribute("popularBrands", popularBrands);
			req.setAttribute("popularProducts", popularProducts);
			req.setAttribute("recommendedProducts", recommendedProducts);

			req.getRequestDispatcher("/WEB-INF/customer/index.jsp").forward(
					req, resp);
		} catch (ServletException | IOException ex) {
			ex.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("ERROR");
		}
	}

	@Override
	protected void checkedDoPost(HttpServletRequest req,
			HttpServletResponse resp) {
		// TODO Auto-generated method stub

	}

}
