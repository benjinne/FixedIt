<!DOCTYPE html>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<html>
<link rel="stylesheet" type="text/css" href="_view/stylesheets/styles.css" media="screen" />
	<head>
	<meta charset="UTF-8">
		<title>FixedIt Scheduler Password Reset</title>
		<style type="text/css">
		td.label {
			text-align: right;
			font-size:10pt;
		}		
		</style>
	<script src='https://www.google.com/recaptcha/api.js'></script>
	<meta name="viewport" content="width=device-width, initial-scale=1">
    <link href='https://fonts.googleapis.com/css?family=Open+Sans:400,700' rel='stylesheet' type='text/css'>
    <link rel="stylesheet" type="text/css" href="_view/stylesheets/stylesheet.css" media="screen">
	</head>
	<body>
	<section class="page-header">
      <h1 class="project-name">FixedIt</h1>
      <h2 class="project-tagline">CS320 Software Engineering Project: York College Scheduling App</h2>
      <form id="form" action="${pageContext.servletContext.contextPath}/passwordReset" method="post">
			<center>
			<c:if test="${! empty errorMessage}">
				<script type="text/javascript">alert("${errorMessage}")</script>
			</c:if>
				
				<c:if test="${empty sessionId}">
					<c:if test="${empty errorMessage}">
						<table>
							<tr>
								<td class="label">Email Address: </td>
								<td><input class="textInput" type="text" name="emailAddress"></td>
							</tr>
							<tr>
								<td></td>
								<td>
									<input class="btn" type="submit" name="passwordReset" value="Request Password Reset">
								</td>
							</tr>
						</table>
					</c:if>
				</c:if>
				
				<c:if test="${! empty sessionId}">
					<c:if test="${empty errorMessage}">
						<table>
							<tr>
								<td class="label" autofocus>New Password: </td>
								<td><input class="textInput" type="password" name="password" size="12" /></td>
							</tr>
							<tr>
								<td class="label" autofocus>Confirm Password: </td>
								<td><input class="textInput" type="password" name="passwordConfirm" size="12" /></td>
							</tr>
							<tr>
								<td></td>
								<td><input class="btn" type="submit" name="passwordReset" value="Reset Password"></td>
							</tr>
						</table>
					</c:if>
				</c:if>
				<c:if test="${! empty success}">
					<br>
					<input class="btn" type="button" value="Login" onclick="window.location='login'" />
				</c:if>
						
			</center>
					<br>
				</form>
    </section>
    <section class="main-content">
      <h3>
<a id="fixedit-scheduler" class="anchor" href="#fixedit-scheduler" aria-hidden="true"><span aria-hidden="true" class="octicon octicon-link"></span></a>FixedIt Scheduler</h3>

<p>FixedIt Scheduler is a web-app for students of York College of Pennsylvania to make course scheduling seamless and painless.</p>

<h3>
<a id="development-team" class="anchor" href="#development-team" aria-hidden="true"><span aria-hidden="true" class="octicon octicon-link"></span></a>Development Team</h3>

<p>Mat Jones - <a href="https://github.com/mrjones2014" class="user-mention">@mrjones2014</a></p>

<p>Garrett Ghafir - <a href="https://github.com/gghafir" class="user-mention">@gghafir</a></p>

<p>Mike Skurla - <a href="https://github.com/mskur1" class="user-mention">@mskur1</a></p>
    </section>
	</body>
</html>