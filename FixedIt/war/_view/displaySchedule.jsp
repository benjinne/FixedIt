<!DOCTYPE html>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<html>
	<head>
		<title>FixedIt Scheduler displaySchedule</title>
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
	
		<form action="${pageContext.servletContext.contextPath}/displaySchedule" method="post">
			<table>
				<tr>
					<td class="label">Schedule(button): </td>
				</tr>
			</table>
			<br>
		</form>
	</body>
</html>