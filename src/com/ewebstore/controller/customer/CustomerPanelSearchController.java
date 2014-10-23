package com.ewebstore.controller.customer;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ewebstore.controller.SimpleFeedbackPageLoader;
import com.ewebstore.entity.Brand;
import com.ewebstore.entity.Product;
import com.ewebstore.model.BrandQueryModel;
import com.ewebstore.model.ProductQueryModel;

public class CustomerPanelSearchController extends CheckedCustomerPanelServlet {

	@Override
	protected void customerPanelDoGet(HttpServletRequest req,
			HttpServletResponse resp) {
		try {
			String searchKey = req.getParameter("searchkey");

			if (searchKey != null) {
				searchKey = searchKey.trim().toLowerCase();

				if (searchKey.length() == 0)
					searchKey = null;
			}

			ArrayList<Product> products = new ArrayList<>();
			ArrayList<Brand> brands = new ArrayList<>();

			if (searchKey != null) {
				products = ProductQueryModel.searchProducts(searchKey);
				brands = BrandQueryModel.searchBrands(searchKey);
				req.setAttribute("searchkey", searchKey);
			}

			req.setAttribute("matchedproducts", products);
			req.setAttribute("matchedbrands", brands);

			req.getRequestDispatcher("/WEB-INF/customer/searchresults.jsp")
					.forward(req, resp);
		} catch (ServletException | IOException ex) {
			SimpleFeedbackPageLoader.showOperationFailedPage(req, resp);
		}
	}

	@Override
	protected void customerPanelDoPost(HttpServletRequest req,
			HttpServletResponse resp) {
	}
}
