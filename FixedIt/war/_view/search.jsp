<!DOCTYPE html>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<html>
	<head>
		<title>FixedIt Scheduler Search</title>
		<style type="text/css">
		.error {
			color: red;
		}
		
		td.label {
			text-align: left;
		}
		</style>
	</head>

	<body>
		<c:if test="${! empty errorMessage}">
			<div class="error">${errorMessage}</div>
		</c:if>
	
		<form action="${pageContext.servletContext.contextPath}/search" method="post">
			<table>
				<tr>
					<td class="label">Search by Subject: </td>
					<td><input type="search" name="searchBySubject" size="12" value="${searchBySubject}" /></td>
				</tr>
				<tr>
					<td class="label">Search by Section: </td>
					<td><input type="search" name="searchBySection" size="12" value="${searchBySection}" /></td>
				</tr>
				<tr>
					<td class="label">Search by Level: </td>
					<td><input type="search" name="searchByLevel" size="12" value="${searchByLevel}" /></td>
				</tr>
			</table>
			<input type="Submit" name="search" value="Search">
			<br>
		</form>
	</body>
</html>