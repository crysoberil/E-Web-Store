package com.ewebstore.controller;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ewebstore.entity.ProductTransferEntity;
import com.ewebstore.model.BranchInventoryTransferModel;
import com.ewebstore.model.BranchManagerQueryModel;

public class InventoryTransferReceiveRequestsPageLoader extends
		CheckedHttpServlet {

	@Override
	protected void checkedDoGet(HttpServletRequest req, HttpServletResponse resp) {
		if (!isAdmin(req))
			SimpleFeedbackPageLoader.showInvalidAccessPage(req, resp);
		else {
			try {
				String managerID = (String) req.getSession().getAttribute(
						"adminid");
				String branchID = BranchManagerQueryModel
						.getBranchID(managerID);
				ArrayList<ProductTransferEntity> transferEntities = BranchInventoryTransferModel
						.getToReceiveTransferRequests(branchID);
				req.setAttribute("toreceiverequests", transferEntities);
				req.setAttribute("norequest", transferEntities.isEmpty());
				req.getRequestDispatcher("/WEB-INF/admin/toreceiverequests.jsp")
						.forward(req, resp);

			} catch (ServletException | IOException ex) {
				SimpleFeedbackPageLoader.showOperationFailedPage(req, resp);
			}
		}
	}

	@Override
	protected void checkedDoPost(HttpServletRequest req,
			HttpServletResponse resp) {
	}
}
