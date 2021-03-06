package com.ewebstore.controller.admin;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ewebstore.model.ProductQueryModel;

/**
 * The AdminProductSearchController class is a servlet handling the loading of
 * the products' search page for admins.
 * 
 * @author ewebstore.com
 *
 */
public class AdminProductSearchController extends CheckedHttpServlet {

	@Override
	protected void checkedDoGet(HttpServletRequest req, HttpServletResponse resp) {
		// Should be do post
		adminProductSearch(req, resp);
	}

	@Override
	protected void checkedDoPost(HttpServletRequest req,
			HttpServletResponse resp) {
		adminProductSearch(req, resp);
	}

	private void adminProductSearch(HttpServletRequest req,
			HttpServletResponse resp) {
		if (!isAdmin(req))
			SimpleFeedbackPageLoader.showInvalidAccessPage(req, resp);
		else {
			String productNameSubstring = req.getParameter("searchkey");

			if (productNameSubstring != null)
				productNameSubstring = productNameSubstring.trim()
						.toLowerCase();
			else
				productNameSubstring = "";

			ArrayList<String> productIDs = new ArrayList<String>();
			ArrayList<String> productNames = new ArrayList<String>();
			ArrayList<String> productBrands = new ArrayList<String>();

			req.setAttribute("productids", productIDs);
			req.setAttribute("productnames", productNames);
			req.setAttribute("productbrands", productBrands);

			try {
				if (productNameSubstring != null) {
					try {
						productIDs = ProductQueryModel
								.getAllProductIDsWithNameSubstring(productNameSubstring);

						for (String productID : productIDs) {
							productNames.add(ProductQueryModel
									.getProductName(productID));
							productBrands.add(ProductQueryModel
									.getProductBrandName(productID));
						}
					} catch (SQLException ex) {
						throw ex;
					}
				}

				req.setAttribute("productids", productIDs);
				req.setAttribute("productnames", productNames);
				req.setAttribute("productbrands", productBrands);

				req.getRequestDispatcher("/WEB-INF/admin/productsearch.jsp")
						.forward(req, resp);
			} catch (ServletException | IOException | SQLException ex3) {
				SimpleFeedbackPageLoader
						.showAdminOperationFailedPage(req, resp);
			}
		}
	}
}