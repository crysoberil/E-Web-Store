package com.ewebstore.controller.customer;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ewebstore.controller.CheckedHttpServlet;
import com.ewebstore.controller.SimpleFeedbackPageLoader;
import com.ewebstore.entity.Product;
import com.ewebstore.model.BrandQueryModel;

public class BrandProductsPageLoader extends CheckedCustomerPanelServlet {
	@Override
	protected void customerPanelDoGet(HttpServletRequest req,
			HttpServletResponse resp) {
		try {
			String brandID = req.getParameter("brandid");

			try {
				brandID = brandID.trim();
				Long.parseLong(brandID);
			} catch (Exception ex) {
				throw new IllegalArgumentException();
			}

			ArrayList<Product> brandProducts = BrandQueryModel
					.getPopularBrandProducts(brandID);
			
			String brandName = BrandQueryModel.getBrandName(brandID);

			req.setAttribute("brandname", brandName);
			req.setAttribute("brandproducts", brandProducts);

			req.getRequestDispatcher("/WEB-INF/customer/brandproducts.jsp")
					.forward(req, resp);
		} catch (IllegalArgumentException | SQLException ex) {
			SimpleFeedbackPageLoader.showSimpleFeedbackPage(req, resp,
					"Invalid brand id", "Inexistent brand",
					"No such brand exists");
		} catch (IOException | ServletException ex) {
			SimpleFeedbackPageLoader.showOperationFailedPage(req, resp);
		}
	}

	@Override
	protected void customerPanelDoPost(HttpServletRequest req,
			HttpServletResponse resp) {
		// TODO Auto-generated method stub

	}

}
