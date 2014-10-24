package com.ewebstore.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.ewebstore.entity.ShoppingCart;

/**
 * The CheckedCustomerPanelServlet class is a servlet that is the superclass of
 * all the controllers of the project, providing the skeletal structure of the
 * classes.
 * 
 * @author ewebstore.com
 *
 */
public abstract class CheckedHttpServlet extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		forceNoCache(resp);
		loadLogInStatusInSession(req);
		ensureShoppingCartAttachment(req);
		checkedDoGet(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		forceNoCache(resp);
		loadLogInStatusInSession(req);
		ensureShoppingCartAttachment(req);
		checkedDoPost(req, resp);
	}

	private void ensureShoppingCartAttachment(HttpServletRequest req) {
		HttpSession session = req.getSession();

		if (isCustomer(session)) {
			String customerID = (String) req.getSession().getAttribute(
					"customerid");
			String customerName = (String) req.getSession().getAttribute(
					"customername");

			if (session.getAttribute("cart") == null) {
				session.setAttribute("cart", new ShoppingCart(customerID,
						customerName));
			} else {
				ShoppingCart cart = (ShoppingCart) session.getAttribute("cart");
				cart.setCustomerID(customerID);
				cart.setCustomerName(customerName);
			}
		} else if (session.getAttribute("cart") == null) {
			session.setAttribute("cart", new ShoppingCart());
		}
	}

	private void forceNoCache(HttpServletResponse resp) {
		// Set standard HTTP/1.1 no-cache headers.
		resp.setHeader("Cache-Control",
				"private, no-store, no-cache, must-revalidate");

		// Set standard HTTP/1.0 no-cache header.
		resp.setHeader("Pragma", "no-cache");

		resp.setDateHeader("Expires", 0);
	}

	protected abstract void checkedDoGet(HttpServletRequest req,
			HttpServletResponse resp);

	protected abstract void checkedDoPost(HttpServletRequest req,
			HttpServletResponse resp);

	protected void loadLogInStatusInSession(HttpServletRequest req) {

		HttpSession session = req.getSession();

		if (session.getAttribute("loggedin") == null) // session contains no
														// info on login
			loadLogInInfoToSessionFromCookies(req);
	}

	protected void loadLogInInfoToSessionFromCookies(HttpServletRequest req) {
		String customerID = null;
		String customerName = null;
		String adminID = null;
		String adminName = null;
		Boolean isAdmin = null;

		Cookie[] cookies = req.getCookies();

		if (cookies != null)
			for (Cookie cookie : cookies) {
				String cookieName = cookie.getName();

				if (cookieName.equals("customerid"))
					customerID = cookie.getValue();
				else if (cookieName.equals("customername"))
					customerName = cookie.getValue();
				else if (cookieName.equals("isadmin"))
					isAdmin = Boolean.parseBoolean(cookie.getValue());
				else if (cookieName.equals("adminid"))
					adminID = cookie.getValue();
				else if (cookieName.equals("adminname"))
					adminName = cookie.getValue();
			}

		HttpSession session = req.getSession();

		session.setAttribute("customername", customerName);
		session.setAttribute("customerid", customerID);
		session.setAttribute("isadmin", isAdmin);
		session.setAttribute("adminid", adminID);
		session.setAttribute("adminname", adminName);

		if (customerName == null && adminName == null) // log in info not in
														// cookie
			session.setAttribute("loggedin", false);
		else
			session.setAttribute("loggedin", true);
	}

	protected void forceLogOut(HttpServletRequest req, HttpServletResponse resp) {
		HttpSession session = req.getSession();

		session.removeAttribute("loggedin");
		session.removeAttribute("customerid");
		session.removeAttribute("customername");
		session.removeAttribute("isadmin");
		session.removeAttribute("adminid");
		session.removeAttribute("adminname");
		session.removeAttribute("cart");

		Cookie[] cookies = req.getCookies();

		if (cookies != null)
			for (int i = 0; i < cookies.length; i++) {
				cookies[i].setValue(null);
				cookies[i].setPath(null);
				cookies[i].setMaxAge(0);
				resp.addCookie(cookies[i]);
			}
	}

	protected boolean isLoggedIn(HttpServletRequest req) {
		return isLoggedIn(req.getSession());
	}

	protected boolean isAdmin(HttpServletRequest req) {
		return isLoggedIn(req.getSession())
				&& (Boolean) req.getSession().getAttribute("isadmin");
	}

	protected boolean isLoggedIn(HttpSession session) {
		return (Boolean) session.getAttribute("loggedin");
	}

	protected boolean isAdmin(HttpSession session) {
		return isLoggedIn(session) && (Boolean) session.getAttribute("isadmin");
	}

	protected boolean isCustomer(HttpSession session) {
		return isLoggedIn(session)
				&& !((Boolean) session.getAttribute("isadmin"));
	}

	protected String getWebContextRootAddress() {
		return getServletContext().getRealPath("");
	}
}
