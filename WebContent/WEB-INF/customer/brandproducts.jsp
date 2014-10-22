<!DOCTYPE html>
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
	ArrayList<ProductCategory> categories = (ArrayList<ProductCategory>) request
			.getAttribute("categories");
	ArrayList<Brand> popularBrands = (ArrayList<Brand>) request
			.getAttribute("popularBrands");
	ArrayList<Product> brandProducts = (ArrayList<Product>) request
			.getAttribute("brandproducts");
	String brandName = (String) request.getAttribute("brandname");
%>

<title>Home | E-Shopper</title>
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
								<li><a href="<%=LinkGenerator.customerHomePageLink()%>"
									class="active">Home</a></li>

								<li class="dropdown"><a href="#">Shop<i
										class="fa fa-angle-down"></i></a>
									<ul role="menu" class="sub-menu">
										<li><a href="<%=LinkGenerator.productsPageLink()%>">Products</a></li>
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

	<section>
		<div class="container">
			<div class="row">
				<div class="col-sm-3">
					<div class="left-sidebar">
						<h2>Category</h2>
						<div class="panel-group category-products" id="accordian">
							<!--category-productsr-->

							<%
								for (ProductCategory category : categories) {
							%>
							<div class="panel panel-default">
								<div class="panel-heading">
									<h4 class="panel-title">
										<a href="<%=category.getCategoryPageLink()%>"><%=category.getCategoryName()%></a>
									</h4>
								</div>
							</div>
							<%
								}
							%>
						</div>
						<!--/category-products-->

						<div class="brands_products">
							<!--brands_products-->
							<h2>Brands</h2>
							<div class="brands-name">
								<ul class="nav nav-pills nav-stacked">
									<%
										for (Brand brand : popularBrands) {
									%>
									<li><a href="<%=brand.getBrandPageLink()%>"><%=brand.getBrandName()%>
									</a></li>
									<%
										}
									%>
								</ul>
							</div>
						</div>
						<!--/brands_products-->

						<div class="shipping text-center">
							<!--shipping-->
							<img src="images/home/freeshipping.jpg" alt="" />
						</div>
						<!--/shipping-->

					</div>
				</div>

				<div class="col-sm-9 padding-right">
					<div class="features_items">
						<!--features_items-->
						<h2 class="title text-center">
							Popular products for
							<%=brandName%></h2>
						<%
							for (Product product : brandProducts) {
						%>
						<div class="col-sm-4">
							<div class="product-image-wrapper">
								<div class="single-products">
									<div class="productinfo text-center">
										<img src="<%=product.getProductImageLink()%>" alt="" />
										<h2><%=String.format("BDT %.2f", product.getPrice())%></h2>
										<p><%=product.getProductName()%></p>
										<a
											href="<%=LinkGenerator.addToCartLink(product.getProductID())%>"
											class="btn btn-default add-to-cart"><i
											class="fa fa-shopping-cart"></i>Add to cart</a>
									</div>
								</div>
								<div class="choose">
									<ul class="nav nav-pills nav-justified">
										<li><a
											href="<%=LinkGenerator.getProductPageLink(product
						.getProductID())%>"><i
												class="fa"></i>Product Details</a></li>
									</ul>
								</div>
							</div>
						</div>
						<%
							}
						%>
					</div>
					<!--features_items-->

				</div>
			</div>
		</div>
	</section>

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