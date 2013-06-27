<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>

<meta name="viewport" content="width=device-width, initial-scale=1.0">
<link href="css/bootstrap.min.css" rel="stylesheet" media="screen">
<script>

</script>
</head>
<body>
	<div class="page-header">

		<div class="row">
			<div class="span4 pull-left"
				style="margin-top: 4px; margin-left: 25px">
				<a href="/"><img alt="Logo-white" src="images/costco_logo.gif"
					style="width: 300px; height: 50px" /></a>
			</div>

			<div class="span8">
				<h1>
					Shopping List Helper <small>Floor Plan Manager</small>
				</h1>
			</div>
		</div>
	</div>

	<div class="row">
		<div class="span3 pull-left">
			<ul class="nav nav-tabs nav-stacked">
				<li class="active"><a href="#">Current Floor Plan</a></li>
				<li><a href="uplan.do">Update Floor Plan</a></li>
				<li><a href="logout.do">Logout</a></li>
			</ul>
		</div>
		<div class="span9 pull- left">
				<h4>
					Welcome ${manager } !<br><br>  
					<small>Warehouse Manager: ${warehouse }</small>
				</h4>
			<div style="text-align:center; width:${plans.width }; margin-left:auto; margin-right:auto;">
<img id="Image-Maps_5201306131633309" src="${plans.path }" usemap="#Image-Maps_5201306131633309" border="0" width="1280" height="728" alt="" />
<map id="_Image-Maps_5201306131633309" name="Image-Maps_5201306131633309">
<c:forEach var="spot" items="${hotspots}">
<area shape="rect" coords="${spot.coordinates}" href="#" alt="" title=""  />
</c:forEach>
</map>
</div>
            
		</div>
	</div>

	<script src="http://code.jquery.com/jquery.js"></script>
	<script src="js/bootstrap.min.js"></script>
</body>
</html>