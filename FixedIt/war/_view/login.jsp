<!DOCTYPE html>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<html>
	<head>
		<title>Add Numbers</title>
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
	
		<form action="${pageContext.servletContext.contextPath}/login" method="post">
			<table>
				<tr>
					<td class="label">Email Address: </td>
					<td><input type="text" name="emailAddress" size="12" value="${emailAddress}" /></td>
				</tr>
				<tr>
					<td class="label">Password: </td>
					<td><input type="text" name="password" size="12" value="${password}" /></td>
				</tr>
				<tr>
					<td class="label">Result:</td>
					<td>${result}</td>
				</tr>
			</table>
			<input type="Submit" name="submit" value="Login">
			<c:if test="${true credentialsMatch}">
				<a href="userInfo.jsp">Go to User Home</a>
			</c:if>
		</form>
	</body>
</html>