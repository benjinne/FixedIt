package fixedIt.servlets;
import java.io.IOException;
import java.sql.SQLException;
import java.util.TreeMap;

import javax.servlet.RequestDispatcher;
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
		}
		catch(NullPointerException e){
			controller.initializeSchedule();
			s=session.getCurrentUser().getSchedules().firstEntry().getValue();
			//System.out.print(s.getName());
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
			if(req.getParameter(c.getCRN() + "View")!=null){
				String returnedCourses="<tr><td>CRN</td><td>Course</td><td>Title</td>" +
						"<td>Credits</td><td>Type</td><td>Days</td><td>Time</td><td>Location 1</td>" +
						"<td>Location 2</td><td>Instructor 1</td><td>Instructor 2</td><td>Capacity</td> " +
						"<td>Seats Open</td><td>Enrolled</td><td>Begin-End</td><td>Add to Schedule</td></tr>";
				returnedCourses=returnedCourses+("<tr><td>" + c.getCRN() + "</td>");
				returnedCourses=returnedCourses+("<td>" + c.getCourseAndSection() + "</td>");
				returnedCourses=returnedCourses+("<td>" + c.getTitle() + "</td>");
				returnedCourses=returnedCourses+("<td>" + c.getCredits() + "</td>");
				returnedCourses=returnedCourses+("<td>" + c.getType() + "</td>");
				returnedCourses=returnedCourses+("<td>" + c.getDays() + "</td>");
				returnedCourses=returnedCourses+("<td>" + c.getTime() + "</td>");
				returnedCourses=returnedCourses+("<td>" + c.getLocation().get(0) + "</td>");
				if(c.getLocation().size()>1){
					returnedCourses=returnedCourses+("<td>" + c.getLocation().get(1) + "</td>");
				}
				else{
					returnedCourses=returnedCourses+("<td> </td>");
				}
				returnedCourses=returnedCourses+("<td>" + c.getInstructors().get(0) + "</td>");
				if(c.getInstructors().size()>1){
					returnedCourses=returnedCourses+("<td>" + c.getInstructors().get(1) + "</td>");
				}
				else{
					returnedCourses=returnedCourses+("<td> </td>");
				}
				returnedCourses=returnedCourses+("<td>" + c.getCapacity() + "</td>");
				returnedCourses=returnedCourses+("<td>" + c.getSeatsRemain() + "</td>");
				returnedCourses=returnedCourses+("<td>" + c.getSeatsFilled() + "</td>");
				returnedCourses=returnedCourses+("<td>" + c.getBeginEnd() + "</td>");
				returnedCourses=returnedCourses+"<td><input type=\"submit\" name=\"" 
						+ c.getCRN() + "\" value=\"Add to Schedule\"</tr>";
				
				req.setAttribute("returnedCourses", returnedCourses);
				RequestDispatcher rd=req.getRequestDispatcher("search");
				rd.forward(req, resp);
				return;
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
		TreeMap<Integer, String> colors=mapCoursesToColors(s);
		String html="<table class=\"scheduleTable\">" + 
				"<tr>" + 
				"	<th>Time</th>" + 
				"	<th>Monday</th>" + 
				"	<th>Tuesday</th>" + 
				"	<th>Wednesday</th>" + 
				"	<th>Thursday</th>" + 
				"	<th>Friday</th>" + 
				"</tr>";
		String[] days={"m", "t", "w", "r", "f"};
		
		for(int j=8; j<=22; j++){
			int timeInt=j%12;
			if(timeInt==0){
				timeInt=12;
			}
			String timeHr="" + timeInt;
			
			//System.out.println(timeHr);
			
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
					html=html  +"<td ";
					for(Course c : s.getCourses()){
						if((c.getTime().substring(0, c.getTime().indexOf(':')).equals(timeHr) || c.getTime().substring(0, c.getTime().indexOf(':')).equals("0" + timeHr)) && c.getTime().substring(0, c.getTime().indexOf('-')).contains(timeMin) && c.getTime().substring(0, c.getTime().indexOf('-')).contains(amPm)){
							if(c.getDays().toLowerCase().contains(days[i])){
								String currentColor=colors.get(c.getCRN());
								String timeLastHalf=c.getTime().substring(c.getTime().indexOf('-')+1);
								int startHr=Integer.parseInt(timeHr);
								int endHr=Integer.parseInt(timeLastHalf.substring(0, timeLastHalf.indexOf(':')));
								int numCells=endHr-startHr;
								numCells=numCells*2;
								if(!c.getTime().substring(0, c.getTime().indexOf('-')).contains("00")){
									if(timeLastHalf.contains("00")){
										numCells=numCells-1;
									}
								}
								else{
									if(!timeLastHalf.contains("00")){
										numCells=numCells+1;
									}
								}
								if(numCells<2){
									numCells=2;
								}
								
								html=html + " style=\"background:" + currentColor + ";\" rowspan=\"" + numCells + "\">" + c.getTitle() + "<br>" +  c.getCourseAndSection() + "<br>" + c.getTime() + "<br>" + 
										"<input class=\"btn\" type=\"submit\" name=\"" + c.getCRN() + "View\" value=\"More Info\"/> <input class=\"btn\" type=\"submit\" name=\"" + c.getCRN() + "\" value=\"Remove\"/>";
								System.out.println(true + " for "  + c.getCRN() + " on " + days[i]);
							}
						}
					}
					html=html + "</td>";
				}
				html=html + "</tr>";
			}
		}
		html=html + "</table>";
		
		System.out.println(html);
		
		return html;
	}
	
	public String generateHTMLScheduleTableForDownload(Schedule s){
		String css=".scheduleTable {\n" + 
				"	font-family:Arial;\n" + 
				"	width: 95%;\n" + 
				"	text-align:center;\n" + 
				"	margin:0 auto;\n" + 
				"}\n" + 
				".scheduleTable td{\n" + 
				"	line-height:20px;\n" + 
				"	font-family:Arial;\n" + 
				"	width:18%;\n" + 
				"	padding:0px;\n" + 
				"}\n" + 
				".scheduleTable td:first-child{\n" + 
				"	line-height:20px;\n" + 
				"	border-style: solid;\n" + 
				"	border-width:1px;\n" + 
				"	border-color:#000000;\n" + 
				"	font-family:Arial;\n" + 
				"	width: 10%;\n" + 
				"	padding:10px;\n" + 
				"}\n" + 
				".scheduleTable th{\n" + 
				"	text-align: center;\n" + 
				"	background: #008080;\n" + 
				"}";
		
		TreeMap<Integer, String> colors=mapCoursesToColors(s);
		String html="<style>" + css + "</style>";
		html=html+"<table class=\"scheduleTable\">" + 
				"<tr>" + 
				"	<th>Time</th>" + 
				"	<th>Monday</th>" + 
				"	<th>Tuesday</th>" + 
				"	<th>Wednesday</th>" + 
				"	<th>Thursday</th>" + 
				"	<th>Friday</th>" + 
				"</tr>";
		String[] days={"m", "t", "w", "r", "f"};
		
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
					for(Course c : s.getCourses()){
						String currentColor=colors.get(c.getCRN());
						if((c.getTime().substring(0, c.getTime().indexOf(':')).equals(timeHr) || c.getTime().substring(0, c.getTime().indexOf(':')).equals("0" + timeHr)) && c.getTime().substring(0, c.getTime().indexOf('-')).contains(timeMin) && c.getTime().substring(0, c.getTime().indexOf('-')).contains(amPm)){
							if(c.getDays().toLowerCase().contains(days[i])){
								String timeLastHalf=c.getTime().substring(c.getTime().indexOf('-')+1);
								int startHr=Integer.parseInt(timeHr);
								int endHr=Integer.parseInt(timeLastHalf.substring(0, timeLastHalf.indexOf(':')));
								int numCells=endHr-startHr;
								numCells=numCells*2;
								if(c.getTime().substring(0, c.getTime().indexOf('-')).contains("30")){
									if(!timeLastHalf.contains("30")){
										numCells=numCells-1;
									}
								}
								else{
									if(timeLastHalf.contains("30")){
										numCells=numCells+1;
									}
								}
								if(numCells<1){
									numCells=2;
								}
								
								html=html + "<td style=\"background:" + currentColor + ";\" rowspan=\"" + numCells + "\">" + c.getTitle() + "<br>" +  c.getCourseAndSection() + "<br>" + c.getTime() + "<br>";
								html=html + "</td>";
							}
							else{
								html=html+"<td></td>";
							}
						}
					}
					
				}
				html=html + "</tr>";
			}
		}
		html=html + "</table>";
		
		html=css+html;
		
		return html;
	}
	
	public TreeMap<Integer, String> mapCoursesToColors(Schedule s){
		String[] colors={"#FF5733", "#FFBD33", "#33FF57", "#33FFBD", "#71FF33",
				 		 "#5B33FF", "#C133FF", "#FF5B33", "#FFC300"};
		TreeMap<Integer, String> result=new TreeMap<Integer, String>();
		int i=0;
		for(Course c : s.getCourses()){
			result.put(c.getCRN(), colors[i]);
			i++;
		}
		return result;
	}
}
