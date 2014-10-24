<!DOCTYPE html>
<%@page import="com.ewebstore.model.SharedData"%>
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
%>

<title>Login or Sign up | <%=SharedData.getShopName()%></title>
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




	<section id="form">
		<!--form-->
		<div class="container">
			<div class="row">
				<div class="col-sm-4 col-sm-offset-1">
					<div class="login-form">
						<!--login form-->
						<h2>Login to your account</h2>
						<form action="<%=LinkGenerator.logInSubmissionLink()%>"
							method="post">
							<input type="text" placeholder="Email Address" name="email">
							<input type="password" placeholder="Password" name="password">
							<input type="hidden" name="logintype" value="customer"> <span>
								<input type="checkbox" class="checkbox" name="staysignedin">
								Keep me signed in
							</span>
							<button type="submit" class="btn btn-default">Login</button>
						</form>
					</div>
					<!--/login form-->
				</div>


				<div class="col-sm-1">
					<h2 class="or">OR</h2>
				</div>


				<div class="col-sm-4">
					<div class="signup-form">
						<!--sign up form-->
						<h2>New User Signup!</h2>
						<form
							action="<%=LinkGenerator.customerRegistrationSubmissionLink()%>"
							method="post">
							<input type="text" placeholder="Name" name="name"> <input
								type="email" placeholder="Email" name="email"> <input
								type="password" placeholder="Password" name="password">

							<input type="text" placeholder="Gender(Male/Female)"
								name="gender"> <input type="date"
								placeholder="Date of birth" name="dob"> <input
								type="text" placeholder="Address" name="address"> <input
								type="text" placeholder="Contact Number" name="contactnumber">

							<button type="submit" class="btn btn-default">Signup</button>
						</form>
					</div>
					<!--/sign up form-->
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
					<p class="pull-left">Copyright © 2015 E-SHOPPER Inc. All rights
						reserved.</p>
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