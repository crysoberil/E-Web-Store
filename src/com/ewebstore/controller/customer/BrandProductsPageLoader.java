package com.ewebstore.controller.customer;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ewebstore.controller.admin.SimpleFeedbackPageLoader;
import com.ewebstore.entity.Product;
import com.ewebstore.model.BrandQueryModel;

/**
 * The BrandProductsPageLoader class is a servlet handling the loading of a
 * brand's page.
 * 
 * @author ewebstore.com
 *
 */
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
			SimpleFeedbackPageLoader.showCustomerSimpleFeedbackPage(req, resp,
					"Invalid brand id", "Inexistent brand",
					"No such brand exists");
		} catch (IOException | ServletException ex) {
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
