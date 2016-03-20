<!DOCTYPE html>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<html>
<link rel="stylesheet" type="text/css" href="_view/stylesheets/styles.css" media="screen" />
<head>
<meta charset="UTF-8">
<title>FixedIt Scheduler Search</title>
<style type="text/css">
.error {
	color: red;
	font-weight: bold;
}

td.label {
	text-align: left;
}
.optgroupColor {
	color: black;
	font-weight: bold;
}
.alignTDTop {
	vertical-align: top;
}
</style>
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="stylesheet" type="text/css" href="_view/stylesheets/normalize.css"
	media="screen">
<link href='https://fonts.googleapis.com/css?family=Open+Sans:400,700'
	rel='stylesheet' type='text/css'>
<link rel="stylesheet" type="text/css" href="_view/stylesheets/stylesheet.css"
	media="screen">
<link rel="stylesheet" type="text/css"
	href="_view/stylesheets/github-light.css" media="screen">
</head>
<body>
	<section class="styled-body">
		<h1 class="project-name">FixedIt</h1>
		<h2 class="project-tagline">CS320 Software Engineering Project: York College Scheduling App</h2>
		<form action="${pageContext.servletContext.contextPath}/search" method="post">
			<c:if test="${! empty errorMessage}">
				<div class="error">${errorMessage}</div> 
			</c:if>
			<div class="sideBar">
				<table class="sideBarTable">
					<tr>
						<td><input class="sideBarBtn" type="button" value="Search Courses" onclick="window.location='search';" /></td>
					</tr>
					<tr>
						<td><input class="sideBarBtn" type="button" value="My Account" onclick="window.location='userInfo';" /></td>
					</tr>
					<tr>
						<td><input class="sideBarBtn" type="button" value="Dummy Button"  /></td>
					</tr>
					<tr>
						<td><input class="sideBarBtn" type="button" value="Dummy Button"  /></td>
					</tr>
					<tr>
						<td><input class="sideBarBtn" type="button" value="Dummy Button"  /></td>
					</tr>
					<tr>
						<td><input class="sideBarBtn" type="button" value="Logout" onclick="window.location='login';" /></td>
					</tr>
				</table>
			</div>
				<table align="center">
					<tr>
					<td class="label">Search by Term: &nbsp;&nbsp;</td>
					<td class="label">Search by Level: &nbsp;&nbsp;</td>
					<td class="label">Search by Section: &nbsp;&nbsp;</td>
				</tr>
				<tr>
					<td class="alignTDTop" ><select id="term" name="term" size="7">
							<option class="option"  selected="selected" VALUE="201610">Fall 2016</option>
							<option class="option"  VALUE="201590">Special Session 2016</option>
							<option class="option"  VALUE="201550">Summer II 2016</option>
							<option class="option"  VALUE="201540">Summer I 2016</option>
							<option class="option"  VALUE="201530">Mini-Mester 2016</option>
							<option class="option"  VALUE="201520">Spring 2016</option>
							<option class="option"  VALUE="201510">Fall 2015</option>
						</select>
					</td>
					<td class="alignTDTop" ><select id="level" name="level" size="7">
							<option class="option"  selected="selected" VALUE="A">All Undergraduate Classes</option>
							<option class="option"  VALUE="M">All Graduate Classes</option>
							<option class="option"  VALUE="E">Eve. &amp; Sat. Undergraduate Classes</option>
					</select></td>
					<td class="alignTDTop" ><select id="dept" name="dept" size="7">
								<optgroup class="optgroupColor"  label="Behavioral Sciences:">
									<option class="option" selected="selected" VALUE="ANT_01">&nbsp;&nbsp;&nbsp;Anthropology
									</option>
									<option class="option"  VALUE="BEH_01">&nbsp;&nbsp;&nbsp;Behavioral
										Sciences</option>
									<option class="option"  VALUE="CJA_01">&nbsp;&nbsp;&nbsp;Criminal
										Justice</option>
									<option class="option"  VALUE="GER_01">&nbsp;&nbsp;&nbsp;Gerontology
									</option>
									<option class="option"  VALUE="HSV_01">&nbsp;&nbsp;&nbsp;Human
										Services</option>
									<option class="option"  VALUE="PSY_01">&nbsp;&nbsp;&nbsp;Psychology
									</option>
									<option class="option"  VALUE="SOC_01">&nbsp;&nbsp;&nbsp;Sociology
									</option>
								</optgroup>
								<optgroup class="optgroupColor"  label="Biological Sciences:">
									<option class="option"  VALUE="BIO_02">&nbsp;&nbsp;&nbsp;Biological
										Science</option>
									<option class="option"  VALUE="PMD_02">&nbsp;&nbsp;&nbsp;Pre-Med</option>
									<option class="option"  VALUE="RT_02">&nbsp;&nbsp;&nbsp;Respiratory
										Therapy</option>
								</optgroup>
								<optgroup class="optgroupColor"  label="Business Administration:">
									<option class="option"  VALUE="ACC_03">&nbsp;&nbsp;&nbsp;Accounting
									</option>
									<option class="option"  VALUE="ECO_03">&nbsp;&nbsp;&nbsp;Economics
									</option>
									<option class="option"  VALUE="ENT_03">&nbsp;&nbsp;&nbsp;Entrepreneurship
									</option>
									<option class="option"  VALUE="FIN_03">&nbsp;&nbsp;&nbsp;Finance</option>
									<option class="option"  VALUE="BUS_03">&nbsp;&nbsp;&nbsp;General
										Business Courses</option>
									<option class="option"  VALUE="IFS_03">&nbsp;&nbsp;&nbsp;Information
										Systems</option>
									<option class="option"  VALUE="IBS_03">&nbsp;&nbsp;&nbsp;International
										Business</option>
									<option class="option"  VALUE="MGT_03">&nbsp;&nbsp;&nbsp;Management
									</option>
									<option class="option"  VALUE="MKT_03">&nbsp;&nbsp;&nbsp;Marketing
									</option>
									<option class="option"  VALUE="QBA_03">&nbsp;&nbsp;&nbsp;Quantitative
										Business</option>
									<option class="option"  VALUE="SCM_03">&nbsp;&nbsp;&nbsp;Supply Chain
										Op Mgmt</option>
								</optgroup>
								<optgroup class="optgroupColor"  label="Communication &amp; the Arts:">
									<option class="option"  VALUE="ART_07">&nbsp;&nbsp;&nbsp;Art</option>
									<option class="option"  VALUE="CM_07">&nbsp;&nbsp;&nbsp;Communication
									</option>
									<option class="option"  VALUE="MUS_07">&nbsp;&nbsp;&nbsp;Music</option>
									<option class="option"  VALUE="THE_07">&nbsp;&nbsp;&nbsp;Theatre</option>
								</optgroup>
								<optgroup class="optgroupColor"  label="Education:">
									<option class="option"  VALUE="ECH_04">&nbsp;&nbsp;&nbsp;Early
										Childhood Education</option>
									<option class="option"  VALUE="EDU_04">&nbsp;&nbsp;&nbsp;Education
									</option>
									<option class="option"  VALUE="MLE_04">&nbsp;&nbsp;&nbsp;Middle Level
										Education</option>
									<option class="option"  VALUE="SE_04">&nbsp;&nbsp;&nbsp;Secondary
										Education</option>
									<option class="option"  VALUE="SPE_04">&nbsp;&nbsp;&nbsp;Special
										Education</option>
								</optgroup>
								<optgroup class="optgroupColor"  label="Engineering &amp; Computer Science:">
									<option class="option"  VALUE="CS_12">&nbsp;&nbsp;&nbsp;Computer
										Science</option>
									<option class="option"  VALUE="ECE_12">&nbsp;&nbsp;&nbsp;Electric/Computer
										Engineering</option>
									<option class="option"  VALUE="EGR_12">&nbsp;&nbsp;&nbsp;Engineering
									</option>
									<option class="option"  VALUE="ME_12">&nbsp;&nbsp;&nbsp;Mechanical
										Engineering</option>
									<option class="option"  VALUE="PHY_12">&nbsp;&nbsp;&nbsp;Physics</option>
								</optgroup>
								<optgroup class="optgroupColor"  label="English &amp; Humanities:">
									<option class="option"  VALUE="CRW_05">&nbsp;&nbsp;&nbsp;Creative
										Writing</option>
									<option class="option"  VALUE="FLM_05">&nbsp;&nbsp;&nbsp;Film</option>
									<option class="option"  VALUE="FCO_05">&nbsp;&nbsp;&nbsp;Foundation
										Communication</option>
									<option class="option"  VALUE="FRN_05">&nbsp;&nbsp;&nbsp;French</option>
									<option class="option"  VALUE="GRM_05">&nbsp;&nbsp;&nbsp;German</option>
									<option class="option"  VALUE="HUM_05">&nbsp;&nbsp;&nbsp;Humanities
									</option>
									<option class="option"  VALUE="INT_05">&nbsp;&nbsp;&nbsp;International
										Studies</option>
									<option class="option"  VALUE="ITL_05">&nbsp;&nbsp;&nbsp;Italian</option>
									<option class="option"  VALUE="LAT_05">&nbsp;&nbsp;&nbsp;Latin)</option>
									<option class="option"  VALUE="LIT_05">&nbsp;&nbsp;&nbsp;Literature
									</option>
									<option class="option"  VALUE="PHL_05">&nbsp;&nbsp;&nbsp;Philosophy
									</option>
									<option class="option"  VALUE="REL_05">&nbsp;&nbsp;&nbsp;Religious
										Studies</option>
									<option class="option"  VALUE="RUS_05">&nbsp;&nbsp;&nbsp;Russian</option>
									<option class="option"  VALUE="SPN_05">&nbsp;&nbsp;&nbsp;Spanish</option>
									<option class="option"  VALUE="WRT_05">&nbsp;&nbsp;&nbsp;Writing</option>
								</optgroup>
								<optgroup class="optgroupColor"  label="History &amp; Poli Sci:">
									<option class="option"  VALUE="G_06">&nbsp;&nbsp;&nbsp;Geography</option>
									<option class="option"  VALUE="HIS_06">&nbsp;&nbsp;&nbsp;History</option>
									<option class="option"  VALUE="IA_06">&nbsp;&nbsp;&nbsp;Intelligence
										Analysis</option>
									<option class="option"  VALUE="INT_06">&nbsp;&nbsp;&nbsp;International
										Studies</option>
									<option class="option"  VALUE="PS_06">&nbsp;&nbsp;&nbsp;Political Sci
										&amp; Govt</option>
								</optgroup>
								<optgroup class="optgroupColor"  label="Hospitality, Recr &amp; Sport Mgmt:">
									<option class="option"  VALUE="HSP_11">&nbsp;&nbsp;&nbsp;Hospitality
										Management</option>
									<option class="option"  VALUE="PE_11">&nbsp;&nbsp;&nbsp;Physical
										Education</option>
									<option class="option"  VALUE="REC_11">&nbsp;&nbsp;&nbsp;Recreation
										&amp; Leisure Admin</option>
									<option class="option"  VALUE="SPM_11">&nbsp;&nbsp;&nbsp;Sport
										Management</option>
								</optgroup>
								<optgroup class="optgroupColor"  label="Interdisciplinary Programs:">
									<option class="option"  VALUE="FYS_10">&nbsp;&nbsp;&nbsp;First Year
										Seminar</option>
									<option class="option"  VALUE="WGS_10">&nbsp;&nbsp;&nbsp;Women's &amp;
										Gender Studies</option>
								</optgroup>
								<optgroup class="optgroupColor"  label="Nursing:">
									<option class="option"  VALUE="NUR_08">&nbsp;&nbsp;&nbsp;Nursing</option>
								</optgroup>
								<optgroup class="optgroupColor"  label="Physical Sciences:">
									<option class="option"  VALUE="CHM_09">&nbsp;&nbsp;&nbsp;Chemistry
									</option>
									<option class="option"  VALUE="ESS_09">&nbsp;&nbsp;&nbsp;Earth/Space
										Science</option>
									<option class="option"  VALUE="FCM_09">&nbsp;&nbsp;&nbsp;Forensic
										Chemistry</option>
									<option class="option"  VALUE="MAT_09">&nbsp;&nbsp;&nbsp;Mathematics
									</option>
									<option class="option"  VALUE="PSC_09">&nbsp;&nbsp;&nbsp;Physical
										Science</option>
									<option class="option"  VALUE="PHY_09">&nbsp;&nbsp;&nbsp;Physics</option>
								</optgroup>
						</select></td>
					</tr>
				</table>
			<input class="btn" type="Submit" value="Search">
		<br>
		<div id="courseChart">
		</div>
		<c:if test="${! empty returnedCourses}">
			<table class="courseTable">
				<c:out value="${returnedCourses}" escapeXml="false"/>
			</table>
		</c:if>
		</form>
	</section>
</body>
</html>