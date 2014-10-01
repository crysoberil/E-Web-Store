package com.ewebstore.controller;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import com.ewebstore.entity.ProductCategory;
import com.ewebstore.model.ProductCategoryQueryModel;

@MultipartConfig
public class AddGenericProductPageLoader extends CheckedHttpServlet {

	@Override
	protected void checkedDoGet(HttpServletRequest req, HttpServletResponse resp) {
		if (!isAdmin(req))
			SimpleFeedbackPageLoader.showInvalidAccessPage(req, resp);
		else {
			try {
				ArrayList<ProductCategory> categories = ProductCategoryQueryModel
						.getAllProductCategories();
				
				req.setAttribute("categories", categories);

				req.getRequestDispatcher("/WEB-INF/admin/addproductform.jsp")
						.forward(req, resp);
			} catch (IOException | ServletException ex) {
				SimpleFeedbackPageLoader.showOperationFailedPage(req, resp);
			}
		}
	}

	@Override
	protected void checkedDoPost(HttpServletRequest req,
			HttpServletResponse resp) {
	}

}
