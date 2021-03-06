package com.ewebstore.controller.admin;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ewebstore.entity.ProductCategory;
import com.ewebstore.model.BranchInventoryQueryModel;
import com.ewebstore.model.ProductCategoryQueryModel;
import com.ewebstore.model.ProductQueryModel;

/**
 * The SubmitStockProductAdditionController class is a servlet handling the
 * addition of a product to a branch stock.
 * 
 * @author ewebstore.com
 *
 */
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

				String productName = ProductQueryModel
						.getProductName(productID);
				SimpleFeedbackPageLoader.showAdminSimpleFeedbackPage(req, resp,
						"Product Added", "Product Added to Inventory", String
								.format("%d products of %s added to inventory",
										quantity, productName));
			} catch (IllegalArgumentException ex) {
				SimpleFeedbackPageLoader.showAdminSimpleFeedbackPage(req, resp,
						"Error", "Invalid Input", ex.getMessage());
			} catch (SQLException ex) {
				SimpleFeedbackPageLoader
						.showAdminOperationFailedPage(req, resp);
			}
		}
	}
}
