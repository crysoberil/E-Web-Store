package com.ewebstore.controller.admin;

import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ewebstore.model.BranchInventoryTransferModel;

/**
 * The DispatchInventoryTransferController class is a servlet handling the
 * dispatching of inventory transfers.
 * 
 * @author ewebstore.com
 *
 */
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

				SimpleFeedbackPageLoader.showAdminSimpleFeedbackPage(req, resp,
						"Success", "Database Updated",
						"Inventory product transfer database updated");
			} catch (SQLException | IllegalArgumentException ex) {
				SimpleFeedbackPageLoader.showAdminSimpleFeedbackPage(req, resp,
						"Error", "Invalid operation",
						"No such inventory transfer request");
			}
		}
	}
}
