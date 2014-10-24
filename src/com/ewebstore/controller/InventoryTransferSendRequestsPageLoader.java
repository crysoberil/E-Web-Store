package com.ewebstore.controller;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ewebstore.entity.ProductTransferEntity;
import com.ewebstore.model.BranchInventoryQueryModel;
import com.ewebstore.model.BranchInventoryTransferModel;
import com.ewebstore.model.BranchManagerQueryModel;

public class InventoryTransferSendRequestsPageLoader extends CheckedHttpServlet {

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
						.getToSendTransferRequests(branchID);
				req.setAttribute("tosendrequests", transferEntities);
				req.setAttribute("norequest", transferEntities.isEmpty());
				req.getRequestDispatcher("/WEB-INF/admin/tosendrequests.jsp")
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
