package fixedIt.servlets;
import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import fixedIt.controllers.DisplayScheduleController;
import fixedIt.modelComponents.Course;
import fixedIt.modelComponents.Schedule;


public class DisplayScheduleServlet extends HttpServlet {			
	
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		resp.setHeader("Cache-Control","no-cache");
		resp.setHeader("Cache-Control","no-store");
		fixedIt.modelComponents.Session session=(fixedIt.modelComponents.Session) req.getSession().getAttribute("userSession");
		if(session==null){
			resp.sendRedirect("login");
			return;
		}
		Schedule s;
		DisplayScheduleController controller=new DisplayScheduleController(session.getCurrentUser());
		try{
			s=session.getCurrentUser().getSchedules().firstEntry().getValue();
			System.out.print(s.getName());
		}
		catch(NullPointerException e){
			controller.initializeSchedule();
			s=session.getCurrentUser().getSchedules().firstEntry().getValue();
			System.out.print(s.getName());
		}
		
		for(Course c : s.getCourses()){
			if(req.getParameter("" + c.getCRN())!=null){
				s.deleteCourse(c.getCRN());
			}
		}
		
		String html=generateHTMLScheduleTable(s);
		req.setAttribute("scheduleHTML", html);
		req.getRequestDispatcher("/_view/displaySchedule.jsp").forward(req, resp);
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		// Decode form parameters and dispatch to controller
		String errorMessage = null;
		fixedIt.modelComponents.Session session=(fixedIt.modelComponents.Session) req.getSession().getAttribute("userSession");
		if(session==null){
			resp.sendRedirect("login");
			return;
		}
		
		Schedule s;
		DisplayScheduleController controller=new DisplayScheduleController(session.getCurrentUser());
		try{
			s=session.getCurrentUser().getSchedules().firstEntry().getValue();
		}
		catch(NullPointerException e){
			controller.initializeSchedule();
			s=session.getCurrentUser().getSchedules().firstEntry().getValue();
		}
		
		if(req.getParameter("dlAsCSV")!=null){
			resp.setContentType("text/csv");
			resp.setHeader("Content-Disposition", "attachment; filename=\"schedule.csv\"");
			try{
				ServletOutputStream os=resp.getOutputStream();
				String csv=controller.getScheduleAsCSV(s);
				os.write(csv.getBytes());
				os.flush();
				os.close();
				return;
			}catch(IOException e){
				errorMessage="Download failed. Please try again.";
				e.printStackTrace();
			}
		}
		
		if(req.getParameter("dlAsHtml")!=null){
			resp.setContentType("text/html");
			resp.setHeader("Content-Disposition", "attachment; filename=\"schedule.html\"");
			try{
				ServletOutputStream os=resp.getOutputStream();
				String html=generateHTMLScheduleTableForDownload(s);
				os.write(html.getBytes());
				os.flush();
				os.close();
				return;
			}catch(IOException e){
				errorMessage="Download failed. Please try again.";
				e.printStackTrace();
			}
		}
		
		for(int i=0; i<s.getCourses().size(); i++){
			Course c=s.getCourses().get(i);
			if(req.getParameter("" + c.getCRN())!=null){
				session.getCurrentUser().getSchedules().firstEntry().getValue().deleteCourse(c.getCRN());
			}
		}
		
		try {
			session.getAuth().saveExistingUserNewDataToDB(session.getCurrentUser());
		} catch (SQLException e) {
			errorMessage="Error saving data.";
			e.printStackTrace();
		}
		
		// Add parameters as request attributes
		//req.getSession().setAttribute("userSession", session);
		String html=generateHTMLScheduleTable(s);
		req.setAttribute("scheduleHTML", html);
		req.setAttribute("errorMessage", errorMessage);
		
		// Forward to view to render the result HTML document
		req.getRequestDispatcher("/_view/displaySchedule.jsp").forward(req, resp);
	}

	/*private String getStringFromParameter(String s) {
		if (s == null || s.equals("")) {
			return null;
		} else {
			return s;
		}
	}*/
	
	public String generateHTMLScheduleTable(Schedule s){
		String html="<table class=\"courseTable\" align=\"left\" >" + 
				"<tr>" + 
				"	<td>Time</td>" + 
				"	<td>Monday</td>" + 
				"	<td>Tuesday</td>" + 
				"	<td>Wednesday</td>" + 
				"	<td>Thursday</td>" + 
				"	<td>Friday</td>" + 
				"	<td>Saturday</td>" + 
				"</tr>";
		String[] days={"m", "t", "w", "r", "f", "s"};
		
		for(int j=8; j<=22; j++){
			int timeInt=j%12;
			if(timeInt==0){
				timeInt=12;
			}
			String timeHr="" + timeInt;
			String amPm;
			if(j>11){
				amPm="PM";
			}
			else{
				amPm="AM";
			}
			for(int k=0; k<2; k++){
				String timeMin="";
				if(k==0){
					timeMin="00";
					
				}
				else{
					timeMin="30";
				}
				html=html + "<tr>" +
						"<td>" + timeHr + ":" + timeMin + amPm + "</td>";
				
				for(int i=0; i<days.length; i++){
					html=html  +"<td> ";
					for(Course c : s.getCourses()){
						if((c.getTime().substring(0, c.getTime().indexOf(':')).equals(timeHr) || c.getTime().substring(0, c.getTime().indexOf(':')).equals("0" + timeHr)) && c.getTime().substring(0, c.getTime().indexOf('-')).contains(timeMin) && c.getTime().substring(0, c.getTime().indexOf('-')).contains(amPm)){
							if(c.getDays().toLowerCase().contains(days[i])){
								html=html + c.getCourseAndSection() + "&nbsp;&nbsp;&nbsp;<input type=\"submit\" name=\"" + c.getCRN() + "\" value=\"Remove\"/>" + "<br>" + c.getTime();
							}
						}
					}
					html=html + "</td>";
				}
				html=html + "</tr>";
			}
		}
		html=html + "</table>";
		
		return html;
	}
	
	public String generateHTMLScheduleTableForDownload(Schedule s){
		String css="<style type=\"text/css\">.courseTable {" + 
				"	font-family: arial;" + 
				"	margin:0px;" + 
				"	padding:5px;" + 
				"	width:100%;" + 
				"	box-shadow: 10px 10px 5px #888888;" + 
				"	border:1px solid #000000;" + 
				"	" + 
				"	-moz-border-radius-bottomleft:0px;" + 
				"	-webkit-border-bottom-left-radius:0px;" + 
				"	border-bottom-left-radius:0px;" + 
				"	" + 
				"	-moz-border-radius-bottomright:0px;" + 
				"	-webkit-border-bottom-right-radius:0px;" + 
				"	border-bottom-right-radius:0px;" + 
				"	" + 
				"	-moz-border-radius-topright:0px;" + 
				"	-webkit-border-top-right-radius:0px;" + 
				"	border-top-right-radius:0px;" + 
				"	" + 
				"	-moz-border-radius-topleft:0px;" + 
				"	-webkit-border-top-left-radius:0px;" + 
				"	border-top-left-radius:0px;" + 
				"}.courseTable table{" + 
				"    border-collapse: collapse;" + 
				"    border-spacing: 0;" + 
				"    text-align: center;" + 
				"	width:100%;" + 
				"	height:100%;" + 
				"	margin:0px;padding:0px;" + 
				"}.courseTable tr:last-child td:last-child {" + 
				"	-moz-border-radius-bottomright:0px;" + 
				"	-webkit-border-bottom-right-radius:0px;" + 
				"	border-bottom-right-radius:0px;" + 
				"}" + 
				".courseTable table tr:first-child td:first-child {" + 
				"	-moz-border-radius-topleft:0px;" + 
				"	-webkit-border-top-left-radius:0px;" + 
				"	border-top-left-radius:0px;" + 
				"}" + 
				".courseTable table tr:first-child td:last-child {" + 
				"	-moz-border-radius-topright:0px;" + 
				"	-webkit-border-top-right-radius:0px;" + 
				"	border-top-right-radius:0px;" + 
				"}.courseTable tr:last-child td:first-child{" + 
				"	-moz-border-radius-bottomleft:0px;" + 
				"	-webkit-border-bottom-left-radius:0px;" + 
				"	border-bottom-left-radius:0px;" + 
				"}.courseTable tr:hover td{" + 
				"	" + 
				"}" + 
				".courseTable tr:nth-child(odd){ background-color:#aad4ff; }" + 
				".courseTable tr:nth-child(even)    { background-color:#ffffff; }.courseTable td{" + 
				"	vertical-align:middle;" + 
				"	" + 
				"	" + 
				"	border:1px solid #000000;" + 
				"	border-width:0px 1px 1px 0px;" + 
				"	text-align:left;" + 
				"	padding:7px;" + 
				"	font-size:10px;" + 
				"	font-family:Arial;" + 
				"	font-weight:normal;" + 
				"	color:#000000;" + 
				"}.courseTable tr:last-child td{" + 
				"	border-width:0px 1px 0px 0px;" + 
				"}.courseTable tr td:last-child{" + 
				"	border-width:0px 0px 1px 0px;" + 
				"}.courseTable tr:last-child td:last-child{" + 
				"	border-width:0px 0px 0px 0px;" + 
				"}" + 
				".courseTable tr:first-child td{" + 
				"		background:-o-linear-gradient(bottom, #005fbf 5%, #003f7f 100%);	background:-webkit-gradient( linear, left top, left bottom, color-stop(0.05, #005fbf), color-stop(1, #003f7f) );" + 
				"	background:-moz-linear-gradient( center top, #005fbf 5%, #003f7f 100% );" + 
				"	filter:progid:DXImageTransform.Microsoft.gradient(startColorstr=\"#005fbf\", endColorstr=\"#003f7f\");	background: -o-linear-gradient(top,#005fbf,003f7f);" + 
				"" + 
				"	background-color:#005fbf;" + 
				"	border:0px solid #000000;" + 
				"	text-align:center;" + 
				"	border-width:0px 0px 1px 1px;" + 
				"	font-size:14px;" + 
				"	font-family:Arial;" + 
				"	font-weight:bold;" + 
				"	color:#ffffff;" + 
				"}" + 
				".courseTable tr:first-child:hover td{" + 
				"	background:-o-linear-gradient(bottom, #005fbf 5%, #003f7f 100%);	background:-webkit-gradient( linear, left top, left bottom, color-stop(0.05, #005fbf), color-stop(1, #003f7f) );" + 
				"	background:-moz-linear-gradient( center top, #005fbf 5%, #003f7f 100% );" + 
				"	filter:progid:DXImageTransform.Microsoft.gradient(startColorstr=\"#005fbf\", endColorstr=\"#003f7f\");	background: -o-linear-gradient(top,#005fbf,003f7f);" + 
				"" + 
				"	background-color:#005fbf;" + 
				"}" + 
				".courseTable tr:first-child td:first-child{" + 
				"	border-width:0px 0px 1px 0px;" + 
				"}" + 
				".courseTable tr:first-child td:last-child{" + 
				"	border-width:0px 0px 1px 1px;" + 
				"}</style>";
		
		String html="<table class=\"courseTable\" align=\"left\" >" + 
				"<tr>" + 
				"	<td>Time</td>" + 
				"	<td>Monday</td>" + 
				"	<td>Tuesday</td>" + 
				"	<td>Wednesday</td>" + 
				"	<td>Thursday</td>" + 
				"	<td>Friday</td>" + 
				"	<td>Saturday</td>" + 
				"</tr>";
		String[] days={"m", "t", "w", "r", "f", "s"};
		
		for(int j=8; j<=22; j++){
			int timeInt=j%12;
			if(timeInt==0){
				timeInt=12;
			}
			String timeHr="" + timeInt;
			String amPm;
			if(j>11){
				amPm="PM";
			}
			else{
				amPm="AM";
			}
			for(int k=0; k<2; k++){
				String timeMin="";
				if(k==0){
					timeMin="00";
					
				}
				else{
					timeMin="30";
				}
				html=html + "<tr>" +
						"<td>" + timeHr + ":" + timeMin + amPm + "</td>";
				
				for(int i=0; i<days.length; i++){
					html=html  +"<td> ";
					for(Course c : s.getCourses()){
						if(c.getTime().substring(0, c.getTime().indexOf('-')).contains(timeHr) && c.getTime().substring(0, c.getTime().indexOf('-')).contains(timeMin) && c.getTime().substring(0, c.getTime().indexOf('-')).contains(amPm)){
							if(c.getDays().toLowerCase().contains(days[i])){
								html=html + c.getCourseAndSection() + "&nbsp;&nbsp;&nbsp; <br>" + c.getTime();
							}
						}
					}
					html=html + "</td>";
				}
				html=html + "</tr>";
			}
		}
		html=html + "</table>";
		html=css+html;
		
		return html;
	}
}
