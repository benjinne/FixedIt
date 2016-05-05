<!DOCTYPE html>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<html>
<link rel="stylesheet" type="text/css" href="_view/stylesheets/styles.css" media="screen" />
	<head>
	<meta charset="UTF-8">
		<title>FixedIt Scheduler Login</title>
		<style type="text/css">
		.error{
			color:red;
		}
		td.label {
			text-align: right;
		}		
		</style>
	<script src='https://www.google.com/recaptcha/api.js'></script>
	<meta name="viewport" content="width=device-width, initial-scale=1">
    <link href='https://fonts.googleapis.com/css?family=Open+Sans:400,700' rel='stylesheet' type='text/css'>
    <link rel="stylesheet" type="text/css" href="_view/stylesheets/stylesheet.css" media="screen">
	</head>
	<body>
	<section class="page-header">
      <form id="form" action="${pageContext.servletContext.contextPath}/login" method="post">
      <h1 class="project-name">FixedIt</h1>
      <h2 class="project-tagline">CS320 Software Engineering Project: York College Scheduling App</h2>
      
			<center>
			<c:if test="${! empty errorMessage}">
				<div class="error">${errorMessage}</div>
			</c:if>
			<c:if test="${! empty recaptchaHTML}">
				<h1>Please confirm you are a human to continue. We hate robots.</h1>
				<c:out value="${recaptchaHTML}" escapeXml="false"></c:out>
			</c:if>
					<table>
						<tr>
							<td class="label" autofocus>Email Address: &nbsp;&nbsp;</td>
							<td><input class="textInput" type="text" name="emailAddress" size="12" value="${emailAddress}" /></td>
						</tr>
						<tr>
							<td class="label">Password:&nbsp;&nbsp;</td>
							<td><input class="textInput" type="password" name="password" size="12" value="${password}" /></td>
						</tr>
						<tr>
							<td></td>
							<td><input class="btn" type="Submit" name="submit" value="Login">&nbsp;&nbsp;<input class="btn" type="button" value="Register" onclick="window.location='register';"></td>
						</tr>
						<tr>
							<td></td>
							<td><input class="btn" type="Submit" name="passwordReset" value="Forgot Password?"></td>
						</tr>
					</table></center>
					 <!-- <div style="position:absolute;left:0px;top:0px;" text-align: left;"><input class="btn" type="Submit" name="debug" value="DebugMode"></div> -->
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
	<br>
	
	
		<input class="btn" type="Submit" name="debug" value="DebugMode">
    
    </section>
	</body>
</html>
