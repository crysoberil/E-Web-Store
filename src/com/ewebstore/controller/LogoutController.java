package com.ewebstore.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class LogoutController extends CheckedHttpServlet {

	@Override
	public void checkedDoGet(HttpServletRequest req, HttpServletResponse resp) {

		forceLogOut(req, resp);

		SimpleFeedbackPageLoader.showSimpleFeedbackPage(req, resp,
				"Logged Out", "Logged Out", "User logged out");

		// TODO

		// try {
		// resp.sendRedirect(LinkGenerator.getHomeLink());
		// } catch (IOException e) {
		// System.err.println("could not redirect");
		// e.printStackTrace();
		// }
	}

	@Override
	public void checkedDoPost(HttpServletRequest req, HttpServletResponse resp) {

	}
}
