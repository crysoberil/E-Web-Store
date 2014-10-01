<!DOCTYPE html>
<%@page import="com.ewebstore.entity.Order"%>
<%@page import="com.ewebstore.entity.OrderProduct"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.ewebstore.linkgenerators.LinkGenerator"%>
<html lang="en">

<head>

<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta name="description" content="">
<meta name="author" content="">

<title>Admin Dashboard</title>

<!-- Bootstrap Core CSS -->
<link href="css/bootstrap.min.css" rel="stylesheet">

<!-- MetisMenu CSS -->
<link href="css/plugins/metisMenu/metisMenu.min.css" rel="stylesheet">

<!-- Timeline CSS -->
<link href="css/plugins/timeline.css" rel="stylesheet">

<!-- Custom CSS -->
<link href="css/sb-admin-2.css" rel="stylesheet">

<!-- Morris Charts CSS -->
<link href="css/plugins/morris.css" rel="stylesheet">

<!-- Custom Fonts -->
<link href="font-awesome-4.1.0/css/font-awesome.min.css"
	rel="stylesheet" type="text/css">

<!-- HTML5 Shim and Respond.js IE8 support of HTML5 elements and media queries -->
<!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
<!--[if lt IE 9]>
        <script src="https://oss.maxcdn.com/libs/html5shiv/3.7.0/html5shiv.js"></script>
        <script src="https://oss.maxcdn.com/libs/respond.js/1.4.2/respond.min.js"></script>
    <![endif]-->

</head>

<body>

	<div id="wrapper">

		<!-- Navigation -->
		<nav class="navbar navbar-default navbar-static-top" role="navigation"
			style="margin-bottom: 0">

			<ul class="nav navbar-top-links navbar-right">
				<div class="navbar-header">

					<a class="navbar-brand"
						href="<%=LinkGenerator.getAdminProfileLink()%>"><%=session.getAttribute("adminname").toString()%></a>
				</div>
			</ul>

			<div class="navbar-default sidebar" role="navigation">
				<div class="sidebar-nav navbar-collapse">
					<ul class="nav" id="side-menu">
						<li><a class="active"
							href="<%=LinkGenerator.adminDashBoardLink()%>"><i
								class="fa fa-dashboard fa-fw"></i> Dashboard</a></li>

						<li><a href="<%=LinkGenerator.queuedOrdersPageLink()%>"><i
								class="fa fa-edit fa-fw"></i>Queued Orders</a></li>

						<li><a href="<%=LinkGenerator.addProductsToStockLink()%>"><i
								class="fa fa-edit fa-fw"></i>Add Products to Stock</a></li>


						<script>
							function ordFunction() {
								var hasCollapsedClass = (" "
										+ document.getElementById("orderslist").className + " ")
										.indexOf(" nav nav-second-level collapse in ") > -1;

								if (hasCollapsedClass) {
									document.getElementById("orderslist").className = "nav nav-second-level collapse";
								} else {
									document.getElementById("orderslist").className = "nav nav-second-level collapse in";
								}
							}
						</script>
						<li><a href="#" onclick="ordFunction()"><i
								class="fa fa-sitemap fa-fw"></i>Orders<span class="fa arrow"></span></a>
							<ul id="orderslist" class="nav nav-second-level collapse">
								<li><a href="<%=LinkGenerator.queuedOrdersPageLink()%>">Queued
										Orders</a></li>
								<li><a href="<%=LinkGenerator.dispatchOrderPageLink()%>">Dispatch
										Order</a></li>
								<li><a href="<%=LinkGenerator.onDeliveryOrdersPageLink()%>">On
										Delivery Orders</a></li>
								<li><a
									href="<%=LinkGenerator.confirmOrderDeliveryPageLink()%>">Confirm
										Order Delivery</a></li>
								<li><a href="<%=LinkGenerator.deliveredOrdersPageLink()%>">Delivered
										Orders</a></li>
							</ul> <!-- /.nav-second-level --></li>


						<script>
							function prodFunction() {
								var hasCollapsedClass = (" "
										+ document
												.getElementById("productslist").className + " ")
										.indexOf(" nav nav-second-level collapse in ") > -1;

								if (hasCollapsedClass) {
									document.getElementById("productslist").className = "nav nav-second-level collapse";
								} else {
									document.getElementById("productslist").className = "nav nav-second-level collapse in";
								}
							}
						</script>
						<li><a href="#" onclick="prodFunction()"><i
								class="fa fa-sitemap fa-fw"></i>Products<span class="fa arrow"></span></a>
							<ul id="productslist" class="nav nav-second-level collapse">
								<li><a
									href="<%=LinkGenerator.addGenericProductPageLink()%>">New
										Product</a></li>
								<li><a href="<%=LinkGenerator.addProductsToStockLink()%>">Add
										Product to Stock</a></li>
								<li><a
									href="<%=LinkGenerator.genericProductSearchPageLink()%>">Product
										Search</a></li>
							</ul> <!-- /.nav-second-level --></li>


						<script>
							function empFunction() {
								var hasCollapsedClass = (" "
										+ document
												.getElementById("employeelist").className + " ")
										.indexOf(" nav nav-second-level collapse in ") > -1;

								if (hasCollapsedClass) {
									document.getElementById("employeelist").className = "nav nav-second-level collapse";
								} else {
									document.getElementById("employeelist").className = "nav nav-second-level collapse in";
								}
							}
						</script>

						<li><a href="#" onclick="empFunction()"><i
								class="fa fa-sitemap fa-fw"></i>Sales Employee<span
								class="fa arrow"></span></a>
							<ul id="employeelist" class="nav nav-second-level collapse">
								<li><a href="<%=LinkGenerator.addEmployeePageLink()%>">Add
										Employee</a></li>
								<li><a href="<%=LinkGenerator.editEmployeePageLink()%>">Edit
										Employee Profile</a></li>
								<li><a href="<%=LinkGenerator.removeEmployeePageLink()%>">Remove
										Employee</a></li>
								<li><a
									href="<%=LinkGenerator.availableEmployeesPageLink()%>">Available
										Employees</a></li>
								<li><a href="<%=LinkGenerator.allEmployeesPageLink()%>">All
										Employees</a></li>
							</ul> <!-- /.nav-second-level --></li>

					</ul>
				</div>
				<!-- /.sidebar-collapse -->
			</div>
			<!-- /.navbar-static-side -->
		</nav>

		<div id="page-wrapper">
			<div class="row">
				<div class="col-lg-12">
					<div class="col-lg-12">
						<h1 class="page-header">Queued Orders</h1>

						<!-- Table -->
						<table class="table table-striped table-bordered table-hover">
							<tr>
								<th>Order ID</th>
								<th>Customer Name</th>
								<th>Order Date</th>
								<th>Delivery Location</th>
								<th>Order</th>
							</tr>

							<%
								ArrayList<Order> orders = (ArrayList<Order>) request
										.getAttribute("Orders");
								for (Order order : orders) {
							%>
							<tr>
								<td><%=order.getOrderID()%></td>
								<td><%=order.getCustomerName()%></td>
								<td><%=order.getOrderDate()%></td>
								<td><%=order.getDeliveryLocation()%></td>
								<td>
									<ul>
										<%
											ArrayList<OrderProduct> orderProducts = order
														.getOrderProducts();

												for (OrderProduct orderProduct : orderProducts) {
										%>
										<li><%=orderProduct.getProductName() + " - "
							+ orderProduct.getOrderQuantity()%></li>
										<%
											}
										%>
									</ul>
								</td>
							</tr>
							<%
								}
							%>
						</table>
					</div>
				</div>
				<!-- /.col-lg-12 -->
			</div>
			<!-- /.row -->

		</div>
		<!-- /#page-wrapper -->

	</div>
	<!-- /#wrapper -->

	<!-- jQuery Version 1.11.0 -->
	<script src="js/jquery-1.11.0.js"></script>

	<!-- Bootstrap Core JavaScript -->
	<script src="js/bootstrap.min.js"></script>

	<!-- Metis Menu Plugin JavaScript -->
	<script src="js/plugins/metisMenu/metisMenu.min.js"></script>

	<!-- Morris Charts JavaScript -->
	<script src="js/plugins/morris/raphael.min.js"></script>
	<script src="js/plugins/morris/morris.min.js"></script>
	<script src="js/plugins/morris/morris-data.js"></script>

	<!-- Custom Theme JavaScript -->
	<script src="js/sb-admin-2.js"></script>

</body>

</html>