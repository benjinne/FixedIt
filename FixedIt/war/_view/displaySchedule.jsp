<!DOCTYPE html>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<html>
	<head>
	<meta charset="UTF-8">
		<title>FixedIt Scheduler - My Account</title>
		<style type="text/css">
		.error {
			color: red;
		}
		
		td.label {
			text-align: left;
		}
		</style>
	<meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="stylesheet" type="text/css" href="_view/stylesheets/normalize.css" media="screen">
    <link href='https://fonts.googleapis.com/css?family=Open+Sans:400,700' rel='stylesheet' type='text/css'>
    <link rel="stylesheet" type="text/css" href="_view/stylesheets/stylesheet.css" media="screen">
    <link rel="stylesheet" type="text/css" href="_view/stylesheets/github-light.css" media="screen">
    <link rel="stylesheet" type="text/css" href="_view/stylesheets/styles.css" media="screen" />
    <link rel="stylesheet" href="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap.min.css">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.0/jquery.min.js"></script>
    <script src="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/js/bootstrap.min.js"></script>
	</head>
	
	<div class="styled-body">
      <h1 class="project-name">FixedIt</h1>
      <h2 class="project-tagline">CS320 Software Engineering Project: York College Scheduling App</h2>
      <form action="${pageContext.servletContext.contextPath}/schedule" method="post">
      	<c:if test="${! empty errorMessage}">
			<div class="error">${errorMessage}</div>
		</c:if>
		<input class="btn" type="submit" name="dlAsCSV" value="Download Schedule as CSV" />
		<input class="btn" type="submit" name="dlAsHtml" value="Dowload Schedule as HTML View" />	
		
		<c:if test="${! empty scheduleHTML}">
			<div class="course-table" id="courses">
				<c:out value="${scheduleHTML}" escapeXml="false"/>
			</div>
		</c:if>
		</form>
		<div class="sideBar">
			<button class="sideBarBtn" data-toggle="collapse" data-target="#menu">Navigation</button>
			<div id="menu" class="collapse">
			<table class="sideBarTable">
				<tr>
					<td><input class="sideBarBtn" type="button" value="Search Courses" onclick="window.location='search';" /></td>
				</tr>
				<tr>
					<td><input class="sideBarBtn" type="button" value="My Account" onclick="window.location='userInfo';" /></td>
				</tr>
				<tr>
					<td><input class="sideBarBtn" type="button" value="Edit Account"  onclick="window.location='editUserInfo';"  /></td>
				</tr>
				<tr>
					<td><input class="sideBarBtn" type="button" value="View Current Schedule" onclick="window.location='schedule';"  /></td>
				</tr>
				<tr>
					<td><input class="sideBarBtn" type="button" value="Dummy Button"  /></td>
				</tr>
				<tr>
					<td><input class="sideBarBtn" type="button" value="Logout" onclick="window.location='login';" /></td>
				</tr>
			</table>
			</div>
		</div>
    </div>
</html>