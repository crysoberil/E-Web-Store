package com.ewebstore.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * The AddProductToStockPageLoader class is a servlet handling the loading of
 * the add new product to stock page.
 * 
 * @author ewebstore.com
 *
 */
public class AddProductToStockPageLoader extends CheckedHttpServlet {

	@Override
	protected void checkedDoGet(HttpServletRequest req, HttpServletResponse resp) {
		if (!isAdmin(req))
			SimpleFeedbackPageLoader.showInvalidAccessPage(req, resp);
		else {
			try {
				req.getRequestDispatcher("/WEB-INF/admin/addproducttostock.jsp")
						.forward(req, resp);
			} catch (ServletException | IOException ex) {
				SimpleFeedbackPageLoader
						.showAdminOperationFailedPage(req, resp);
			}
		}
	}

	@Override
	protected void checkedDoPost(HttpServletRequest req,
			HttpServletResponse resp) {
	}

}
