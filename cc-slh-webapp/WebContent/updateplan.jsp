<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>

<meta name="viewport" content="width=device-width, initial-scale=1.0">
<link href="css/bootstrap.min.css" rel="stylesheet" media="screen">
<link href="css/bootstrap-responsive.css" rel="stylesheet">
<script type="text/javascript"
	src="//cdnjs.cloudflare.com/ajax/libs/jquery/1.8.3/jquery.min.js"></script>
<script>
	function selected_tab(arg) {
		document.getElementById("hsec").value = arg;
	}
	
	function go() {
		  var x = document.getElementById("categ").value; 
		  document.getElementById("hcat").value=x;
	}
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
		<div class="span2 pull-left">
			<ul class="nav nav-tabs nav-stacked">
				<li><a href="cplan.do">Current Floor Plan</a></li>
				<li class="active"><a href="#">Update Floor Plan</a></li>
				<li><a href="logout.do">Logout</a></li>
			</ul>
		</div>
		<div class="span10 pull- left">
			
			<div >Select the section to update!
			</div>
			<c:choose>
			<c:when test="${ (!empty errors) }">
				<div class="alert alert-success">
				<c:forEach var="error" items="${errors}">
					${error} <br/>
				</c:forEach>
				</div>
			</c:when>
			</c:choose>
			<div
				style="text-align:center; width:${plan.width }; margin-left:auto; margin-right:auto;">
				<img id="Image-Maps_5201306131633309" src="${plan.path }"
					usemap="#Image-Maps_5201306131633309" border="0" width="1280"
					height="728" alt="" />
				<map id="_Image-Maps_5201306131633309"
					name="Image-Maps_5201306131633309">
					<c:forEach var="spot" items="${hotspot}">
						<area shape="rect" coords="${spot.coordinates}" data-toggle="modal" href="#myModal"
							alt="" title="" onclick="selected_tab('${spot.coordinates}')" />
					</c:forEach>
				</map>
			</div>

		</div>
	</div>
<div id="myModal" class="modal hide fade">
	<div class="modal-header">
    	<button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
    	
    	<h4>Update Section</h4>	
	</div>
	<div class="modal-body">
      <div class="row-fluid">
			<div class="tab-content">
				<div class="tab-pane active" id="date">
				  	<form action="uplan.do" method="POST">
                      <div class="controls controls-row">
		              	<select id="categ" onchange="go();">
		              	<c:forEach var="ucat" items="${categories}">
		              		<option value="${ucat.sid}">${ucat.name}</option>
		              	</c:forEach>
						</select>
                      </div>
                      <input type="hidden" name="hdnsec" id="hsec">
                       <input type="hidden" name="hdncat" id="hcat">
  					<input type="submit" name="action" value="Update"
								class="btn btn-primary">
				  	</form>
			  	</div>
	  		</div>				  
      </div>
	</div>
</div>
	<script src="http://code.jquery.com/jquery.js"></script>
	<script src="js/bootstrap.min.js"></script>
	
		<script type="text/javascript" src="js/index.js"></script>
		<script src="js/bootstrap-modal.js"></script>
		<script
			src="//cdnjs.cloudflare.com/ajax/libs/respond.js/1.1.0/respond.min.js"></script>
		<script type="text/javascript" src="js/jquery-1.9.1.min.js">
    </script>
		<script type="text/javascript" src="js/bootstrap.min.js">
    </script>
</body>
</html>