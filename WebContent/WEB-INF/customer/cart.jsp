<!DOCTYPE html>
<%@page import="com.ewebstore.model.SharedData"%>
<%@page import="com.ewebstore.entity.ShoppingCartDisplayInformation"%>
<%@page import="java.util.HashMap"%>
<%@page import="com.ewebstore.model.ShoppingCartQueryModel"%>
<%@page import="com.ewebstore.entity.CartItem"%>
<%@page import="com.ewebstore.entity.ProductCategory"%>
<%@page import="com.ewebstore.entity.Product"%>
<%@page import="com.ewebstore.entity.Brand"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.ewebstore.linkgenerators.LinkGenerator"%>
<html lang="en">
<head>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta name="description" content="">
<meta name="author" content="">

<%
	boolean loggedIn = (boolean) request.getAttribute("loggedIn");
	ArrayList<CartItem> cartItems = (ArrayList<CartItem>) request
			.getAttribute("cartItems");
	HashMap<String, ShoppingCartDisplayInformation> cartItemsInfo = (HashMap<String, ShoppingCartDisplayInformation>) request
			.getAttribute("cartItemsInfo");
	HashMap<String, Double> cartItemsPrice = (HashMap<String, Double>) request
			.getAttribute("cartItemsPrice");
	double totalOrderingCost = (double) request
			.getAttribute("totalOrderingCost");
	double shippingCost = (double) request.getAttribute("shippingCost");
%>

<style>
form {
	display: inline;
}
</style>

<title>Cart | <%=SharedData.getShopName()%></title>
<link href="css/bootstrap.min.css" rel="stylesheet">
<link href="css/font-awesome.min.css" rel="stylesheet">
<link href="css/prettyPhoto.css" rel="stylesheet">
<link href="css/price-range.css" rel="stylesheet">
<link href="css/animate.css" rel="stylesheet">
<link href="css/main.css" rel="stylesheet">
<link href="css/responsive.css" rel="stylesheet">
<!--[if lt IE 9]>
    <script src="js/html5shiv.js"></script>
    <script src="js/respond.min.js"></script>
    <![endif]-->
<link rel="shortcut icon" href="images/ico/favicon.ico">
<link rel="apple-touch-icon-precomposed" sizes="144x144"
	href="images/ico/apple-touch-icon-144-precomposed.png">
<link rel="apple-touch-icon-precomposed" sizes="114x114"
	href="images/ico/apple-touch-icon-114-precomposed.png">
<link rel="apple-touch-icon-precomposed" sizes="72x72"
	href="images/ico/apple-touch-icon-72-precomposed.png">
<link rel="apple-touch-icon-precomposed"
	href="images/ico/apple-touch-icon-57-precomposed.png">
</head>
<!--/head-->

<body>
	<header id="header">
		<!--header-->


		<div class="header-middle">
			<!--header-middle-->
			<div class="container">
				<div class="row">
					<div class="col-sm-4">
						<div class="logo pull-left">
							<a href="<%=LinkGenerator.customerHomePageLink()%>"><img
								src="images/home/logo.png" alt="" /></a>
						</div>

					</div>
					<div class="col-sm-8">
						<div class="shop-menu pull-right">
							<ul class="nav navbar-nav">
								<li><a href="<%=LinkGenerator.customerAccountPageLink()%>"><i
										class="fa fa-user"></i> Account</a></li>
								<li><a href="<%=LinkGenerator.cartPageLink()%>"><i
										class="fa fa-shopping-cart"></i> Cart</a></li>
								<li><a
									href="<%=loggedIn ? LinkGenerator.logOutSubmissionLink()
					: LinkGenerator.customerLoginPageLink()%>"><i
										class="fa fa-lock"></i><%=loggedIn ? "Logout" : "Login"%></a></li>
							</ul>
						</div>
					</div>
				</div>
			</div>
		</div>
		<!--/header-middle-->

		<div class="header-bottom">
			<!--header-bottom-->
			<div class="container">
				<div class="row">
					<div class="col-sm-9">
						<div class="navbar-header">
							<button type="button" class="navbar-toggle"
								data-toggle="collapse" data-target=".navbar-collapse">
								<span class="sr-only">Toggle navigation</span> <span
									class="icon-bar"></span> <span class="icon-bar"></span> <span
									class="icon-bar"></span>
							</button>
						</div>
						<div class="mainmenu pull-left">
							<ul class="nav navbar-nav collapse navbar-collapse">
								<li><a href="<%=LinkGenerator.customerHomePageLink()%>">Home</a></li>

								<li class="dropdown"><a href="#">Shop<i
										class="fa fa-angle-down"></i></a>
									<ul role="menu" class="sub-menu">
										<li><a href="<%=LinkGenerator.cartPageLink()%>">Cart</a></li>
										<li><a
											href="<%=loggedIn ? LinkGenerator.customerLogoutPageLink()
					: LinkGenerator.customerLoginPageLink()%>"><%=loggedIn ? "Logout" : "Login"%></a></li>
									</ul></li>

								<li><a href="<%=LinkGenerator.contactPageLink()%>">Contact</a></li>
							</ul>
						</div>
					</div>
					<div class="col-sm-3">
						<div class="search_box pull-right">
							<form method="get"
								action="<%=LinkGenerator.searchResultsPageLink()%>">
								<input type="search" name="searchkey" placeholder="Search">
							</form>
						</div>
					</div>
				</div>
			</div>
		</div>
		<!--/header-bottom-->
	</header>
	<!--/header-->

	<section id="cart_items">
		<div class="container">
			<div class="breadcrumbs">
				<ol class="breadcrumb">
					<li class="active"><h2>Shopping Cart</h2></li>
				</ol>
			</div>

			<div class="table-responsive cart_info">
				<table class="table table-condensed">
					<thead>
						<tr class="cart_menu">
							<td class="image">Item</td>
							<td class="description">Name</td>
							<td class="price">Price</td>
							<td class="quantity">Quantity</td>
							<td class="total">Total</td>
							<td></td>
						</tr>
					</thead>
					<tbody>

						<%
							for (CartItem cartItem : cartItems) {
								ShoppingCartDisplayInformation cartDisplayInformation = cartItemsInfo
										.get(cartItem.getProductID());
						%>
						<tr>
							<td class="cart_product"><a
								href="<%=cartItem.getCartProductPageLink()%>"><img
									src="<%=cartDisplayInformation.getProductImageLink()%>" alt=""
									height="<%=SharedData.getCartProductImageHeight()%>"
									width="<%=SharedData.getCartProductImageWidth()%>"></a></td>

							<td class="cart_description">
								<h4>
									<a href="<%=cartItem.getCartProductPageLink()%>"><%=cartDisplayInformation.getProductName()%></a>
								</h4>
							</td>

							<td class="cart_price">
								<p><%=String.format("BDT %.2f",
						cartDisplayInformation.getProductPrice())%></p>
							</td>

							<td class="cart_quantity">
								<div class="cart_quantity_button">
									<form role="form" method="post"
										action="<%=LinkGenerator.submitChangeCartProductQuantityLink()%>">
										<input type="hidden" name="productid"
											value="<%=cartItem.getProductID()%>"> <input
											type="hidden" name="change" value="increment">
										<button type="submit" class="btn btn-default cart_quantity_up">
											+</button>
									</form>

									<label><%=cartItem.getQuantity()%></label>

									<form role="form" method="post"
										action="<%=LinkGenerator.submitChangeCartProductQuantityLink()%>">
										<input type="hidden" name="productid"
											value="<%=cartItem.getProductID()%>"> <input
											type="hidden" name="change" value="decrement">

										<button type="submit" class="btn btn-default cart_quantity_up">
											-</button>
									</form>
								</div>
							</td>

							<td class="cart_total">
								<p class="cart_total_price"><%=String.format("BDT %.2f",
						cartItemsPrice.get(cartItem.getProductID()))%></p>
							</td>

							<td class="cart_delete">
								<form role="form" method="post"
									action="<%=LinkGenerator.submitChangeCartProductQuantityLink()%>">
									<input type="hidden" name="productid"
										value="<%=cartItem.getProductID()%>"> <input
										type="hidden" name="delete" value="true">

									<button type="submit"
										class="btn btn-default cart_quantity_delete">
										<i class="fa fa-times"></i>
									</button>
								</form>
							</td>

						</tr>
						<%
							}
						%>

					</tbody>
				</table>
			</div>
		</div>

	</section>
	<!--/#cart_items-->

	<section id="do_action">
		<div class="container">
			<div class="heading">
				<h3>What would you like to do next?</h3>
			</div>
			<div class="row">
				<div class="col-sm-6">
					<div class="total_area">
						<ul>
							<li>Cart Sub Total <span><%=String.format("BDT %.2f", totalOrderingCost
					- shippingCost)%></span></li>
							<li>Shipping Cost <span><%=shippingCost > 0 ? String
					.format("BDT %.2f", shippingCost) : "Free"%></span></li>
							<li>Total <span><%=String.format("BDT %.2f", totalOrderingCost)%></span></li>
						</ul>
						<a class="btn btn-default update"
							href="<%=LinkGenerator.customerHomePageLink()%>">Continue
							Shopping</a> <a class="btn btn-default check_out"
							href="<%=LinkGenerator.checkoutFormLink()%>">Checkout</a>
					</div>
				</div>
			</div>
		</div>
	</section>
	<!--/#do_action-->

	<footer id="footer">
		<!--Footer-->
		<div class="footer-top">
			<div class="container">
				<div class="row">
					<div class="col-sm-2">
						<div class="companyinfo">
							<h2>
								<span>e</span>-web-store
							</h2>
							<p>An online market for your needs</p>
						</div>
					</div>
					<div class="col-sm-7"></div>
					<div class="col-sm-3">
						<div class="address">
							<img src="images/home/map.png" alt="" />
							<p>Dhaka, Bangladesh</p>
						</div>
					</div>
				</div>
			</div>
		</div>

		<div class="footer-bottom">
			<div class="container">
				<div class="row">
					<p class="pull-left">Copyright © 2015 E-SHOPPER Inc. All
						rights reserved.</p>
				</div>
			</div>
		</div>
	</footer>
	<!--/Footer-->



	<script src="js/jquery.js"></script>
	<script src="js/bootstrap.min.js"></script>
	<script src="js/jquery.scrollUp.min.js"></script>
	<script src="js/price-range.js"></script>
	<script src="js/jquery.prettyPhoto.js"></script>
	<script src="js/main.js"></script>
</body>
</html>