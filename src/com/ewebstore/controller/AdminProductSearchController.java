package com.ewebstore.controller;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ewebstore.model.ProductQueryModel;

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
			String productNamePrefix = req.getParameter("searchwordprefix");

			if (productNamePrefix != null) {
				productNamePrefix = productNamePrefix.trim().toLowerCase();
				
				if (productNamePrefix.length() == 0)
					productNamePrefix = null;
			}

			ArrayList<String> productIDs = new ArrayList<String>();
			ArrayList<String> productNames = new ArrayList<String>();
			ArrayList<String> productBrands = new ArrayList<String>();

			req.setAttribute("productids", productIDs);
			req.setAttribute("productnames", productNames);
			req.setAttribute("productbrands", productBrands);

			try {
				if (productNamePrefix != null) {
					try {
						productIDs = ProductQueryModel
								.getAllProductIDsWithNamePrefix(productNamePrefix);

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
				SimpleFeedbackPageLoader.showOperationFailedPage(req, resp);
			}
		}
	}
}