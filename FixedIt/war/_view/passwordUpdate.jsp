<!DOCTYPE html>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<html>
	<head>
	<meta charset="UTF-8">
		<title>FixedIt Scheduler - Change Password</title>
		<style type="text/css">
		td.label {
			text-align: left;
		}		
		</style>
	<meta name="viewport" content="width=device-width, initial-scale=1">
    <link href='https://fonts.googleapis.com/css?family=Open+Sans:400,700' rel='stylesheet' type='text/css'>
    <link rel="stylesheet" type="text/css" href="_view/stylesheets/stylesheet.css" media="screen">
    <link rel="stylesheet" type="text/css" href="_view/stylesheets/styles.css" media="screen" />
    <link rel="stylesheet" href="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap.min.css">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.0/jquery.min.js"></script>
    <script src="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/js/bootstrap.min.js"></script>
	</head>
	
	<section class="styled-body">
      <h1 class="project-name">FixedIt</h1>
      <h2 class="project-tagline">CS320 Software Engineering Project: York College Scheduling App</h2>
      <form action="${pageContext.servletContext.contextPath}/passwordUpdate" method="post">
      	<c:if test="${! empty errorMessage}">
			<script type="text/javascript">alert("${errorMessage}")</script>
		</c:if>
				<table align="center">
					<tr>
					<tr>
						<td class="label"> Current Password: </td>
						<td><input class="textInput" type="password" name="password" size="12" value="${password}" /></td>
					</tr>
					<tr>
						<td class="label"> Enter New Password: </td>
						<td><input class="textInput" type="password" name="newPassword" size="12" value="${newPassword}" /></td>
					</tr>
					<tr>
						<td class="label"> Confirm New Password: </td>
						<td><input class="textInput" type="password" name="newPasswordConfirm" size="12" value="${newPasswordConfirm}" /></td>
					</tr>
			</table>
			<input class="btn" type="Submit" value="Submit">
		</form>
		
			
		<div class="sideBar">
			<button class="sideBarBtn" data-toggle="collapse" data-target="#menu">Navigation</button>
			<div id="menu" class="collapse">
			<table class="sideBarTable">
				<tr>
					<td><input class="sideBarBtn" type="button" value="Search Courses" onclick="window.location='search';" /></td>
				</tr>
				<tr>
					<td><input class="sideBarBtn" type="button" value="View Current Schedule" onclick="window.location='schedule';"  /></td>
				</tr>
				<tr>
					<td><input class="sideBarBtn" type="button" value="My Account" onclick="window.location='userInfo';" /></td>
				</tr>
				<tr>
					<td><input class="sideBarBtn" type="button" value="Logout" onclick="window.location='login';" /></td>
				</tr>
			</table>
			</div>
		</div>
    </section>
</html>