package com.ewebstore.controller;

import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ewebstore.model.BranchInventoryQueryModel;
import com.ewebstore.model.BranchInventoryTransferModel;

public class DispatchInventoryTransferController extends CheckedHttpServlet {

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
				String inventoryTransferID = req
						.getParameter("inventorytransferid");
				try {
					inventoryTransferID = inventoryTransferID.trim();
					Long.parseLong(inventoryTransferID);
				} catch (Exception ex) {
					throw new IllegalArgumentException();
				}

				BranchInventoryTransferModel
						.dispatchInventoryTransfer(inventoryTransferID);

				SimpleFeedbackPageLoader.showSimpleFeedbackPage(req, resp,
						"Success", "Database Updated",
						"Inventory product transfer database updated");
			} catch (SQLException | IllegalArgumentException ex) {
				SimpleFeedbackPageLoader.showSimpleFeedbackPage(req, resp,
						"Error", "Invalid operation",
						"No such inventory transfer request");
			}
		}
	}
}
