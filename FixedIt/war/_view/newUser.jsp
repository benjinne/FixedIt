<!DOCTYPE html>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<html>
	<head>
	<meta charset="UTF-8">
		<title>FixedIt Scheduler - Register</title>
		<style type="text/css">
		.error {
			color: red;
		}
		
		td.label {
			text-align: right;
		}		
		</style>
	<meta name="viewport" content="width=device-width, initial-scale=1">
	<link rel="stylesheet" type="text/css" href="_view/stylesheets/styles.css" media="screen" />
    <link href='https://fonts.googleapis.com/css?family=Open+Sans:400,700' rel='stylesheet' type='text/css'>
    <link rel="stylesheet" type="text/css" href="_view/stylesheets/stylesheet.css" media="screen">
	</head>

	<body>
	
	<section class="page-header">
      <h1 class="project-name">Fixedit</h1>
      <h2 class="project-tagline">CS320 Software Engineering Project: York College Scheduling App</h2>
		<center>
		<form action="${pageContext.servletContext.contextPath}/register" method="post">
		<c:if test="${! empty errorMessage}">
			<div class="error">${errorMessage}</div>
		</c:if>
		<c:if test="${empty waitingForConfirm}">
			<table>
				<tr>
					<td class="label">Email Address: </td>
					<td><input class="textInput" type="text" name="emailAddress" size="12" value="${emailAddress}" /></td>
				</tr>
				<tr>
					<td class="label">Password: </td>
					<td><input class="textInput" type="password" name="password" size="12" value="${password}" /></td>
				</tr>
				<tr>
					<td class="label">Confirm Password: </td>
					<td><input class="textInput" type="password" name="passwordConfirm" size="12" value="${passwordConfirm}" /></td>
				</tr>
			</table>
			<input class="btn" type="Submit" name="submit" value="Create Account">
			<br>
		</c:if>
		<c:if test="${! empty waitingForConfirm}">
			<h2>Please check your email to confirm your account.</h2>
		</c:if>
		</form>
		</center>
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