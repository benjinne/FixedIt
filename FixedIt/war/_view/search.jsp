<!DOCTYPE html>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<html>
<link rel="stylesheet" type="text/css" href="_view/stylesheets/styles.css" media="screen" />
<head>
<meta charset="UTF-8">
<title>FixedIt Scheduler Search</title>
<style type="text/css">
.selectBox{
	color:black;
}
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
	<link rel="stylesheet" type="text/css" href="_view/stylesheets/normalize.css" media="screen">
	<link href='https://fonts.googleapis.com/css?family=Open+Sans:400,700' rel='stylesheet' type='text/css'>
	<link rel="stylesheet" type="text/css" href="_view/stylesheets/stylesheet.css" media="screen">
	<link rel="stylesheet" type="text/css" href="_view/stylesheets/github-light.css" media="screen">
	<link rel="stylesheet" href="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap.min.css">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.0/jquery.min.js"></script>
    <script src="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/js/bootstrap.min.js"></script>
</head>
<body>
	<section class="styled-body">
		<h1 class="project-name">FixedIt</h1>
		<h2 class="project-tagline">CS320 Software Engineering Project: York College Scheduling App</h2>
		<form action="${pageContext.servletContext.contextPath}/search" method="post">
			<c:if test="${! empty errorMessage}">
				<div class="error">${errorMessage}</div>
			</c:if>
				<table align="center">
				<tr>
					<td class="alignTDTop" >
						Select Term:<br>
						<select class="selectBox" id="term" name="term" size="1">
							<option class="option"  VALUE="201610" ${"201610"==term ? 'selected="selected"' : ''}>Fall 2016</option>
							<option class="option"  VALUE="201590" ${"201590"==term ? 'selected="selected"' : ''}>Special Session 2016</option>
							<option class="option"  VALUE="201550" ${"201550"==term ? 'selected="selected"' : ''}>Summer II 2016</option>
							<option class="option"  VALUE="201540" ${"201540"==term ? 'selected="selected"' : ''}>Summer I 2016</option>
							<option class="option"  VALUE="201530" ${"201530"==term ? 'selected="selected"' : ''}>Mini-Mester 2016</option>
							<option class="option"  VALUE="201520" ${"201520"==term ? 'selected="selected"' : ''}>Spring 2016</option>
							<option class="option"  VALUE="201510" ${"201510"==term ? 'selected="selected"' : ''}>Fall 2015</option>
						</select>
					</td>
					<td class="alignTDTop" >
						Select Level:<br>
						<select class="selectBox" id="level" name="level" size="1">
							<option class="option"  VALUE="A" ${"A"==level ? 'selected="selected"' : ''}>All Undergraduate Classes</option>
							<option class="option"  VALUE="M" ${"M"==level ? 'selected="selected"' : ''}>All Graduate Classes</option>
							<option class="option"  VALUE="E" ${"E"==level ? 'selected="selected"' : ''}>Eve. &amp; Sat. Undergraduate Classes</option>
					</select></td>
					<td class="alignTDTop" >
						Select Department:<br>
						<select class="selectBox" id="dept" name="dept" size="1">
								<optgroup class="optgroupColor"  label="Behavioral Sciences:">
									<option class="option"  VALUE="ANT_01" ${"ANT_01"==dept ? 'selected="selected"' : ''}>&nbsp;&nbsp;&nbsp;Anthropology
									</option>
									<option class="option"  VALUE="BEH_01" ${"BEH_01"==dept ? 'selected="selected"' : ''}>&nbsp;&nbsp;&nbsp;Behavioral
										Sciences</option>
									<option class="option"  VALUE="CJA_01" ${"CJA_01"==dept ? 'selected="selected"' : ''}>&nbsp;&nbsp;&nbsp;Criminal
										Justice</option>
									<option class="option"  VALUE="GER_01" ${"GER_01"==dept ? 'selected="selected"' : ''}>&nbsp;&nbsp;&nbsp;Gerontology
									</option>
									<option class="option"  VALUE="HSV_01" ${"HSV_01"==dept ? 'selected="selected"' : ''}>&nbsp;&nbsp;&nbsp;Human
										Services</option>
									<option class="option"  VALUE="PSY_01" ${"PSY_01"==dept ? 'selected="selected"' : ''}>&nbsp;&nbsp;&nbsp;Psychology
									</option>
									<option class="option"  VALUE="SOC_01" ${"SOC_01"==dept ? 'selected="selected"' : ''}>&nbsp;&nbsp;&nbsp;Sociology
									</option>
								</optgroup>
								<optgroup class="optgroupColor"  label="Biological Sciences:">
									<option class="option"  VALUE="BIO_02" ${"BIO_02"==dept ? 'selected="selected"' : ''}>&nbsp;&nbsp;&nbsp;Biological
										Science</option>
									<option class="option"  VALUE="PMD_02" ${"PMD_02"==dept ? 'selected="selected"' : ''}>&nbsp;&nbsp;&nbsp;Pre-Med</option>
									<option class="option"  VALUE="RT_02" ${"RT_02"==dept ? 'selected="selected"' : ''}>&nbsp;&nbsp;&nbsp;Respiratory
										Therapy</option>
								</optgroup>
								<optgroup class="optgroupColor"  label="Business Administration:">
									<option class="option"  VALUE="ACC_03" ${"AC_03"==dept ? 'selected="selected"' : ''}>&nbsp;&nbsp;&nbsp;Accounting
									</option>
									<option class="option"  VALUE="ECO_03" ${"ECO_03"==dept ? 'selected="selected"' : ''}>&nbsp;&nbsp;&nbsp;Economics
									</option>
									<option class="option"  VALUE="ENT_03" ${"ENT_03"==dept ? 'selected="selected"' : ''}>&nbsp;&nbsp;&nbsp;Entrepreneurship
									</option>
									<option class="option"  VALUE="FIN_03" ${"FIN_03"==dept ? 'selected="selected"' : ''}>&nbsp;&nbsp;&nbsp;Finance</option>
									<option class="option"  VALUE="BUS_03" ${"BUS_03"==dept ? 'selected="selected"' : ''}>&nbsp;&nbsp;&nbsp;General
										Business Courses</option>
									<option class="option"  VALUE="IFS_03" ${"IFS_03"==dept ? 'selected="selected"' : ''}>&nbsp;&nbsp;&nbsp;Information
										Systems</option>
									<option class="option"  VALUE="IBS_03" ${"IBS_03"==dept ? 'selected="selected"' : ''}>&nbsp;&nbsp;&nbsp;International
										Business</option>
									<option class="option"  VALUE="MGT_03" ${"MGT_03"==dept ? 'selected="selected"' : ''}>&nbsp;&nbsp;&nbsp;Management
									</option>
									<option class="option"  VALUE="MKT_03" ${"MKT_03"==dept ? 'selected="selected"' : ''}>&nbsp;&nbsp;&nbsp;Marketing
									</option>
									<option class="option"  VALUE="QBA_03" ${"QBA_03"==dept ? 'selected="selected"' : ''}>&nbsp;&nbsp;&nbsp;Quantitative
										Business</option>
									<option class="option"  VALUE="SCM_03" ${"SCM_03"==dept ? 'selected="selected"' : ''}>&nbsp;&nbsp;&nbsp;Supply Chain
										Op Mgmt</option>
								</optgroup>
								<optgroup class="optgroupColor"  label="Communication &amp; the Arts:">
									<option class="option"  VALUE="ART_07" ${"ART_07"==dept ? 'selected="selected"' : ''}>&nbsp;&nbsp;&nbsp;Art</option>
									<option class="option"  VALUE="CM_07" ${"CM_07"==dept ? 'selected="selected"' : ''}>&nbsp;&nbsp;&nbsp;Communication
									</option>
									<option class="option"  VALUE="MUS_07" ${"MUS_02"==dept ? 'selected="selected"' : ''}>&nbsp;&nbsp;&nbsp;Music</option>
									<option class="option"  VALUE="THE_07" ${"THE_07"==dept ? 'selected="selected"' : ''}>&nbsp;&nbsp;&nbsp;Theatre</option>
								</optgroup>
								<optgroup class="optgroupColor"  label="Education:">
									<option class="option"  VALUE="ECH_04" ${"ECHO_04"==dept ? 'selected="selected"' : ''}>&nbsp;&nbsp;&nbsp;Early
										Childhood Education</option>
									<option class="option"  VALUE="EDU_04" ${"EDU_04"==dept ? 'selected="selected"' : ''}>&nbsp;&nbsp;&nbsp;Education
									</option>
									<option class="option"  VALUE="MLE_04" ${"MLE_04"==dept ? 'selected="selected"' : ''}>&nbsp;&nbsp;&nbsp;Middle Level
										Education</option>
									<option class="option"  VALUE="SE_04" ${"SE_04"==dept ? 'selected="selected"' : ''}>&nbsp;&nbsp;&nbsp;Secondary
										Education</option>
									<option class="option"  VALUE="SPE_04" ${"SPE_04"==dept ? 'selected="selected"' : ''}>&nbsp;&nbsp;&nbsp;Special
										Education</option>
								</optgroup>
								<optgroup class="optgroupColor"  label="Engineering &amp; Computer Science:">
									<option class="option"  VALUE="CS_12" ${"CS_12"==dept ? 'selected="selected"' : ''}>&nbsp;&nbsp;&nbsp;Computer
										Science</option>
									<option class="option"  VALUE="ECE_12" ${"ECE_12"==dept ? 'selected="selected"' : ''}>&nbsp;&nbsp;&nbsp;Electric/Computer
										Engineering</option>
									<option class="option"  VALUE="EGR_12" ${"EGR_12"==dept ? 'selected="selected"' : ''}>&nbsp;&nbsp;&nbsp;Engineering
									</option>
									<option class="option"  VALUE="ME_12" ${"ME_12"==dept ? 'selected="selected"' : ''}>&nbsp;&nbsp;&nbsp;Mechanical
										Engineering</option>
									<option class="option"  VALUE="PHY_12" ${"PHY_12"==dept ? 'selected="selected"' : ''}>&nbsp;&nbsp;&nbsp;Physics</option>
								</optgroup>
								<optgroup class="optgroupColor"  label="English &amp; Humanities:">
									<option class="option"  VALUE="CRW_05" ${"CRW_05"==dept ? 'selected="selected"' : ''}>&nbsp;&nbsp;&nbsp;Creative
										Writing</option>
									<option class="option"  VALUE="FLM_05" ${"FLM_05"==dept ? 'selected="selected"' : ''}>&nbsp;&nbsp;&nbsp;Film</option>
									<option class="option"  VALUE="FCO_05" ${"FCO_05"==dept ? 'selected="selected"' : ''}>&nbsp;&nbsp;&nbsp;Foundation
										Communication</option>
									<option class="option"  VALUE="FRN_05" ${"FRN_05"==dept ? 'selected="selected"' : ''}>&nbsp;&nbsp;&nbsp;French</option>
									<option class="option"  VALUE="GRM_05" ${"GRM_05"==dept ? 'selected="selected"' : ''}>&nbsp;&nbsp;&nbsp;German</option>
									<option class="option"  VALUE="HUM_05" ${"HUM_05"==dept ? 'selected="selected"' : ''}>&nbsp;&nbsp;&nbsp;Humanities
									</option>
									<option class="option"  VALUE="INT_05" ${"INT_05"==dept ? 'selected="selected"' : ''}>&nbsp;&nbsp;&nbsp;International
										Studies</option>
									<option class="option"  VALUE="ITL_05" ${"ITL_05"==dept ? 'selected="selected"' : ''}>&nbsp;&nbsp;&nbsp;Italian</option>
									<option class="option"  VALUE="LAT_05" ${"LAT_05"==dept ? 'selected="selected"' : ''}>&nbsp;&nbsp;&nbsp;Latin)</option>
									<option class="option"  VALUE="LIT_05" ${"LIT_05"==dept ? 'selected="selected"' : ''}>&nbsp;&nbsp;&nbsp;Literature
									</option>
									<option class="option"  VALUE="PHL_05" ${"PHL_O5"==dept ? 'selected="selected"' : ''}>&nbsp;&nbsp;&nbsp;Philosophy
									</option>
									<option class="option"  VALUE="REL_05" ${"REL_05"==dept ? 'selected="selected"' : ''}>&nbsp;&nbsp;&nbsp;Religious
										Studies</option>
									<option class="option"  VALUE="RUS_05" ${"RUS_05"==dept ? 'selected="selected"' : ''}>&nbsp;&nbsp;&nbsp;Russian</option>
									<option class="option"  VALUE="SPN_05" ${"SPN_05"==dept ? 'selected="selected"' : ''}>&nbsp;&nbsp;&nbsp;Spanish</option>
									<option class="option"  VALUE="WRT_05" ${"WRT_05"==dept ? 'selected="selected"' : ''}>&nbsp;&nbsp;&nbsp;Writing</option>
								</optgroup>
								<optgroup class="optgroupColor"  label="History &amp; Poli Sci:">
									<option class="option"  VALUE="G_06" ${"G_06"==dept ? 'selected="selected"' : ''}>&nbsp;&nbsp;&nbsp;Geography</option>
									<option class="option"  VALUE="HIS_06" ${"HIS_06"==dept ? 'selected="selected"' : ''}>&nbsp;&nbsp;&nbsp;History</option>
									<option class="option"  VALUE="IA_06" ${"IA_06"==dept ? 'selected="selected"' : ''}>&nbsp;&nbsp;&nbsp;Intelligence
										Analysis</option>
									<option class="option"  VALUE="INT_06" ${"INT_06"==dept ? 'selected="selected"' : ''}>&nbsp;&nbsp;&nbsp;International
										Studies</option>
									<option class="option"  VALUE="PS_06" ${"PS_06"==dept ? 'selected="selected"' : ''}>&nbsp;&nbsp;&nbsp;Political Sci
										&amp; Govt</option>
								</optgroup>
								<optgroup class="optgroupColor"  label="Hospitality, Recr &amp; Sport Mgmt:">
									<option class="option"  VALUE="HSP_11" ${"HSP_11"==dept ? 'selected="selected"' : ''}>&nbsp;&nbsp;&nbsp;Hospitality
										Management</option>
									<option class="option"  VALUE="PE_11" ${"PE_11"==dept ? 'selected="selected"' : ''}>&nbsp;&nbsp;&nbsp;Physical
										Education</option>
									<option class="option"  VALUE="REC_11" ${"REC_11"==dept ? 'selected="selected"' : ''}>&nbsp;&nbsp;&nbsp;Recreation
										&amp; Leisure Admin</option>
									<option class="option"  VALUE="SPM_11" ${"SPM_11"==dept ? 'selected="selected"' : ''}>&nbsp;&nbsp;&nbsp;Sport
										Management</option>
								</optgroup>
								<optgroup class="optgroupColor"  label="Interdisciplinary Programs:">
									<option class="option"  VALUE="FYS_10" ${"FYS_10"==dept ? 'selected="selected"' : ''}>&nbsp;&nbsp;&nbsp;First Year
										Seminar</option>
									<option class="option"  VALUE="WGS_10" ${"WGS_10"==dept ? 'selected="selected"' : ''}>&nbsp;&nbsp;&nbsp;Women's &amp;
										Gender Studies</option>
								</optgroup>
								<optgroup class="optgroupColor"  label="Nursing:">
									<option class="option"  VALUE="NUR_08" ${"NUR_08"==dept ? 'selected="selected"' : ''}>&nbsp;&nbsp;&nbsp;Nursing</option>
								</optgroup>
								<optgroup class="optgroupColor"  label="Physical Sciences:">
									<option class="option"  VALUE="CHM_09" ${"CHM_09"==dept ? 'selected="selected"' : ''}>&nbsp;&nbsp;&nbsp;Chemistry
									</option>
									<option class="option"  VALUE="ESS_09" ${"ESS_09"==dept ? 'selected="selected"' : ''}>&nbsp;&nbsp;&nbsp;Earth/Space
										Science</option>
									<option class="option"  VALUE="FCM_09" ${"FCM_09"==dept ? 'selected="selected"' : ''}>&nbsp;&nbsp;&nbsp;Forensic
										Chemistry</option>
									<option class="option"  VALUE="MAT_09" ${"MAT_09"==dept ? 'selected="selected"' : ''}>&nbsp;&nbsp;&nbsp;Mathematics
									</option>
									<option class="option"  VALUE="PSC_09" ${"PSC_09"==dept ? 'selected="selected"' : ''}>&nbsp;&nbsp;&nbsp;Physical
										Science</option>
									<option class="option"  VALUE="PHY_09" ${"PHY_09"==dept ? 'selected="selected"' : ''}>&nbsp;&nbsp;&nbsp;Physics</option>
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
	</section>
</body>
</html>