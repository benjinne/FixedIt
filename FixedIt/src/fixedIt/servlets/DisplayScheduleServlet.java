package fixedIt.servlets;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
//		System.out.println(session.getCurrentUser().getSchedules().size());
		Schedule s=session.getCurrentUser().getSchedules().firstEntry().getValue();
		System.out.println(session.getCurrentUser().getSchedules().firstKey());
		for(Course c : s.getCourses()){
			if(req.getParameter("" + c.getCRN())!=null){
				s.deleteCourse(c.getCRN());
			}
		}
//		try {
//			session.getAuth().saveExistingUserNewDataToDB(session.getCurrentUser());
//		} catch (SQLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
//		System.out.print("Null Session: ");
//		System.out.println(session==null);
//		System.out.print("Null Schedule: ");
//		System.out.println(s==null);
		
		String html=generateHTMLScheduleTable(s);
		req.setAttribute("scheduleHTML", html);
		req.getRequestDispatcher("/_view/displaySchedule.jsp").forward(req, resp);
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		// Decode form parameters and dispatch to controller
		//String errorMessage = null;
		
		
		
		
		// Add parameters as request attributes
	
		
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
			String time="" + timeInt;
			String amPm;
			if(j>11){
				amPm="PM";
			}
			else{
				amPm="AM";
			}
			html=html + "<tr>" +
						"<td>" + time + ":00" + amPm + "</td>";
			
			for(int i=0; i<days.length; i++){
				html=html  +"<td> ";
				for(Course c : s.getCourses()){
					if(c.getTime().substring(0, c.getTime().indexOf('-')).contains(time) && c.getTime().substring(0, c.getTime().indexOf('-')).contains(amPm)){
						if(c.getDays().toLowerCase().contains(days[i])){
							html=html + c.getCourseAndSection() + "<br>" + c.getTime() + 
									"<br><input type=\"submit\" name=\"" + c.getCRN() + "\" value=\"Remove\" ";
						}
					}
				}
				html=html + "</td>";
			}
			html=html + "</tr>";
		}
		html=html + "</table>";
		
		return html;
	}
}
