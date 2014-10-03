<!DOCTYPE html>
<%@page import="com.ewebstore.entity.BranchManager"%>
<%@page import="com.ewebstore.linkgenerators.LinkGenerator"%>
<html lang="en">

<head>

<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta name="description" content="">
<meta name="author" content="">

<%
	BranchManager branchManager = (BranchManager) request
			.getAttribute("branchmanager");
%>

<title><%=branchManager.getName()%></title>

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
					<div class="btn-group">
						<button class="btn btn-transparent btn-lg dropdown-toggle"
							type="button" data-toggle="dropdown">
							<%=session.getAttribute("adminname").toString()%>
							<span class="caret"></span>
						</button>
						<ul class="dropdown-menu" role="menu">
							<li><a href="<%=LinkGenerator.getAdminProfileLink()%>">Profile</a></li>
							<li><a href="<%=LinkGenerator.logOutSubmissionLink()%>">Log
									Out</a></li>
						</ul>
					</div>
				</div>
			</ul>

			<div class="navbar-default sidebar" role="navigation">
				<div class="sidebar-nav navbar-collapse">
					<ul class="nav" id="side-menu">
						<li><a href="<%=LinkGenerator.adminDashBoardLink()%>"><i
								class="fa fa-dashboard fa-fw"></i> Dashboard</a></li>

						<li><a href="<%=LinkGenerator.queuedOrdersPageLink()%>"><i
								class="fa fa-edit fa-fw"></i>Queued Orders</a></li>

						<li><a href="<%=LinkGenerator.addProductsToStockPageLink()%>"><i
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
								<li><a href="<%=LinkGenerator.addProductsToStockPageLink()%>">Add
										Products to Stock</a></li>
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

						<script>
							function reqFunction() {
								var hasCollapsedClass = (" "
										+ document
												.getElementById("requestslist").className + " ")
										.indexOf(" nav nav-second-level collapse in ") > -1;

								if (hasCollapsedClass) {
									document.getElementById("requestslist").className = "nav nav-second-level collapse";
								} else {
									document.getElementById("requestslist").className = "nav nav-second-level collapse in";
								}
							}
						</script>

						<li><a href="#" onclick="reqFunction()"><i
								class="fa fa-sitemap fa-fw"></i>Products Transfer Requests<span
								class="fa arrow"></span></a>
							<ul id="requestslist" class="nav nav-second-level collapse">
								<li><a
									href="<%=LinkGenerator.productsSendRequestsPageLink()%>">Products
										Send Requests</a></li>
								<li><a
									href="<%=LinkGenerator.productsReceiveRequestsPageLink()%>">Products
										Receive Requests</a></li>
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
					<h1 class="page-header">Profile</h1>

					<div
						class="col-xs-12 col-sm-12 col-md-6 col-lg-6 col-xs-offset-0 col-sm-offset-0 col-md-offset-3 col-lg-offset-3 toppad">


						<div class="panel panel-info">
							<div class="panel-heading">
								<h3 class="panel-title"><%=branchManager.getName()%></h3>
							</div>
							<div class="panel-body">
								<div class="row">
									<div class="col-md-3 col-lg-3 " align="center">
										<img alt="User Pic"
											src="https://lh5.googleusercontent.com/-b0-k99FZlyE/AAAAAAAAAAI/AAAAAAAAAAA/eu7opA4byxI/photo.jpg?sz=100"
											class="img-circle">
									</div>


									<div class=" col-md-9 col-lg-9 ">
										<table class="table table-user-information">
											<tbody>
												<tr>
													<td>Manager ID</td>
													<td><%=branchManager.getManagerID()%></td>
												</tr>
												<tr>
													<td>Branch ID</td>
													<td><%=branchManager.getBranchID()%></td>
												</tr>
												<tr>
													<td>Branch Name</td>
													<td><%=branchManager.getBranchName()%></td>
												</tr>
												<tr>
													<td>Gender</td>
													<td><%=branchManager.isGender() ? "Male" : "Female"%>
													</td>
												</tr>
												<tr>
													<td>Email</td>
													<td><%=branchManager.getEmail()%></td>
												</tr>
												<tr>
													<td>Address</td>
													<td><%=branchManager.getAddress()%></td>
												</tr>
												<tr>
													<td>Date of Birth</td>
													<td><%=branchManager.getDob()%></td>
												</tr>
												<tr>
													<td>Contact Number</td>
													<td><%=branchManager.getContactNumber()%></td>
												</tr>

											</tbody>
										</table>
									</div>
								</div>
							</div>

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