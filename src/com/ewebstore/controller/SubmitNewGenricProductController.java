package com.ewebstore.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import com.ewebstore.entity.ProductCategory;
import com.ewebstore.linkgenerators.ImageLinkGenerator;
import com.ewebstore.model.ProductCategoryQueryModel;
import com.ewebstore.model.ProductQueryModel;

public class SubmitNewGenricProductController extends CheckedHttpServlet {

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
				// validate data, then push to database
				String productName = req.getParameter("productname");
				if (productName == null || productName.trim().length() == 0)
					throw new IllegalArgumentException(
							"Product Name field is empty");
				else
					productName = productName.trim();

				String brandName = req.getParameter("brand");
				if (brandName == null || brandName.trim().length() == 0)
					throw new IllegalArgumentException(
							"Brand Name field is empty");
				else
					brandName = brandName.trim().toLowerCase();

				String description = req.getParameter("description");

				double price = 0;

				try {
					price = Double.valueOf(req.getParameter("price"));
				} catch (NullPointerException | NumberFormatException ex) {
					throw new IllegalArgumentException("Invalid price");
				}

				// Handle category and image
				ArrayList<String> selectedCategoryIDs = new ArrayList<String>();

				ArrayList<ProductCategory> categories = ProductCategoryQueryModel
						.getAllProductCategories();

				for (ProductCategory category : categories)
					if (req.getParameter(category.getCategoryCheckBoxName()) != null) // then
																						// checked
						selectedCategoryIDs.add(category.getCategoryID());

				Part imageFilePart = req.getPart("displayimage");

				String[] imageLinks = new String[2];

				if (imageFilePart != null) {
					InputStream stream = imageFilePart.getInputStream();
					imageLinks = ImageLinkGenerator
							.getNewImagePath(getWebContextRootAddress());
					putImageFileToDisk(imageLinks[0], stream);
				}

				ProductQueryModel.addGenericProduct(productName, brandName,
						description, price, selectedCategoryIDs, imageLinks[1]);
				SimpleFeedbackPageLoader.showSimpleFeedbackPage(req, resp,
						"Success", "New Product Added",
						"New product infromtaion added.");

			} catch (IllegalArgumentException ex) {
				SimpleFeedbackPageLoader.showSimpleFeedbackPage(req, resp,
						"Error", "Invalid Input", ex.getMessage());
			} catch (SQLException | ServletException | IOException ex) {
				SimpleFeedbackPageLoader
						.showAdminOperationFailedPage(req, resp);
			}
		}
	}

	private void putImageFileToDisk(String imageLink, InputStream inputStream)
			throws IOException {
		FileOutputStream outputStream = new FileOutputStream(
				new File(imageLink));

		byte[] buffer = new byte[64 * 1024];
		int bytesRead;

		while (true) {
			bytesRead = inputStream.read(buffer);

			if (bytesRead == -1) // EOF
				break;

			outputStream.write(buffer, 0, bytesRead);
		}

		inputStream.close();
		outputStream.flush();
		outputStream.close();
	}
}
