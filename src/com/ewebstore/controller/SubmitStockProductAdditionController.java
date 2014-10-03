package com.ewebstore.controller;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ewebstore.entity.ProductCategory;
import com.ewebstore.model.BranchInventoryQueryModel;
import com.ewebstore.model.ProductCategoryQueryModel;

public class SubmitStockProductAdditionController extends CheckedHttpServlet {

	@Override
	protected void checkedDoGet(HttpServletRequest req, HttpServletResponse resp) {
	}

	@Override
	protected void checkedDoPost(HttpServletRequest req,
			HttpServletResponse resp) {
		if (!isAdmin(req))
			SimpleFeedbackPageLoader.showInvalidAccessPage(req, resp);
		else {
			try {
				String productID = req.getParameter("productid");

				if (productID == null || productID.trim().length() == 0)
					throw new IllegalArgumentException(
							"Invalid entry for product id");
				else {
					productID = productID.trim();
					try {
						Long.parseLong(productID);
					} catch (NumberFormatException ex) {
						throw new IllegalArgumentException(
								"Invalid entry for product id");
					}
				}

				String quantityStr = req.getParameter("quantity");
				int quantity = 0;

				if (quantityStr == null || quantityStr.trim().length() == 0)
					throw new IllegalArgumentException(
							"Invalid entry for product quantity");
				else {
					quantityStr = quantityStr.trim();
					try {
						quantity = Integer.parseInt(quantityStr);
					} catch (NumberFormatException ex) {
						throw new IllegalArgumentException(
								"Invalid entry for product id");
					}
				}

				String branchManagerID = (String) req.getSession()
						.getAttribute("adminid");

				BranchInventoryQueryModel.addProductToInventory(
						branchManagerID, productID, quantity);

				SimpleFeedbackPageLoader
						.showSimpleFeedbackPage(
								req,
								resp,
								"Product Added",
								"Product Added to Inventory",
								String.format(
										"%d products of productid %s added to inventory",
										quantity, productID));
			} catch (IllegalArgumentException ex) {
				SimpleFeedbackPageLoader.showSimpleFeedbackPage(req, resp,
						"Error", "Invalid Input", ex.getMessage());
			} catch (SQLException ex) {
				SimpleFeedbackPageLoader.showOperationFailedPage(req, resp);
			}
		}
	}
}
