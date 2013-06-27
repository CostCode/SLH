<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>

<meta name="viewport" content="width=device-width, initial-scale=1.0">
<link href="css/bootstrap.min.css" rel="stylesheet" media="screen">
</head>
<body>
	<div class="page-header">

		<div class="row">
			<div class="span4 pull-left"
				style="margin-top: 4px; margin-left: 25px">
				<a href="/"><img alt="Logo-white" src="images/costco_logo.gif"
					style="width: 300px; height: 100px" /></a>
			</div>

			<div class="span8">
				<h1>
					Shopping List Helper <small>Floor Plan Manager</small>
				</h1>
			</div>
		</div>
	</div>
	<form action="home.do" method="POST">
		<div class="hero-unit center">
			<div class="container">
				<div class="row">
					<div class="span4 offset4 well">
						<jsp:include page="error-list.jsp" />
						<legend>Please Sign In</legend>
						<!-- <div class="alert alert-error">
	                <a class="close" data-dismiss="alert" href="#">×</a>Incorrect Username or Password!
	            </div> -->
						<form method="POST" action="" accept-charset="UTF-8">
							<input type="text" id="username" class="span4" name="uname"
								placeholder="Username"> <input type="password"
								id="password" class="span4" name="upass" placeholder="Password">

							<input type="submit" name="action" value="Sign In"
								class="btn btn-primary">
						</form>
					</div>
				</div>
			</div>
		</div>
	</form>
	<script src="http://code.jquery.com/jquery.js"></script>
	<script src="js/bootstrap.min.js"></script>
</body>
</html>