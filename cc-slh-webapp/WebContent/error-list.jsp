

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>


<link href="css/bootstrap.css" rel="stylesheet">
<link href="css/bootstrap-responsive.css" rel="stylesheet">
		<c:choose>
			<c:when test="${ (!empty errors) }">
				<div class="alert alert-error">
				<c:forEach var="error" items="${errors}">
					${error} <br/>
				</c:forEach>
				</div>
			</c:when>
			</c:choose>
