<!DOCTYPE html>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<html>
	<head>
		<title>FixedIt Scheduler Registration</title>
		<style type="text/css">
		.error {
			color: red;
		}
		
		td.label {
			text-align: right;
		}
		</style>
	</head>

	<body>
		<c:if test="${! empty errorMessage}">
			<div class="error">${errorMessage}</div>
		</c:if>
	
		<form action="${pageContext.servletContext.contextPath}/register" method="post">
			<table>
				<tr>
					<td class="label">Email Address: </td>
					<td><input type="text" name="emailAddress" size="12" value="${emailAddress}" /></td>
				</tr>
				<tr>
					<td class="label">Password: </td>
					<td><input type="password" name="password" size="12" value="${password}" /></td>
				</tr>
				<tr>
					<td class="label">Confirm Password: </td>
					<td><input type="password" name="passwordConfirm" size="12" value="${passwordConfirm}" /></td>
				</tr>
			</table>
			<input type="Submit" name="submit" value="Create Account">
			<br>
			<c:set var="accountCreated" value="${accountCreated}"/>
			<c:if test="${accountCreated}">
				<a href="_view/login.jsp">Login to your new account!</a>
			</c:if>
		</form>
	</body>
</html>