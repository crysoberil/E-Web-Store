package com.ewebstore.controller.customer;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ewebstore.controller.CheckedHttpServlet;
import com.ewebstore.entity.Brand;
import com.ewebstore.entity.Product;
import com.ewebstore.model.BrandQueryModel;
import com.ewebstore.model.ProductCategoryQueryModel;
import com.ewebstore.model.ProductQueryModel;

public class CustomerHomePageLoader extends CheckedHttpServlet {
	private static int popularBrandCount = 7;
	private static int popularProductCount = 6;

	@Override
	protected void checkedDoGet(HttpServletRequest req, HttpServletResponse resp) {
		try {
			ArrayList<String> categoryNames = ProductCategoryQueryModel
					.getProductCategoryNames();

			ArrayList<Brand> popularBrands = BrandQueryModel
					.getPopularBrands(popularBrandCount);

			ArrayList<Product> popularProducts = ProductQueryModel
					.getPopularProducts(popularProductCount);

			req.setAttribute("categoryNames", categoryNames);
			req.setAttribute("popularBrands", popularBrands);
			req.setAttribute("popularProducts", popularProducts);

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
