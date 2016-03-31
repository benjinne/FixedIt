package fixedIt.servlets;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import fixedIt.controllers.QueryController;
import fixedIt.modelComponents.Course;
import fixedIt.modelComponents.Query;
import fixedIt.modelComponents.Schedule.ConflictException;
import fixedIt.sql.database.SQLWriter;

//NEEDS THE CONTROLLER TO IMPLEMENT

public class SearchServlet extends HttpServlet {			
	
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		resp.setHeader("Cache-Control","no-cache");
		resp.setHeader("Cache-Control","no-store");
		if((fixedIt.modelComponents.Session) req.getSession().getAttribute("userSession")==null){
			resp.sendRedirect("login");
			return;
		}
		req.setAttribute("returnedCourses", null);
		req.getRequestDispatcher("/_view/search.jsp").forward(req, resp);
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		fixedIt.modelComponents.Session session=(fixedIt.modelComponents.Session) req.getSession().getAttribute("userSession");
		// Decode form parameters and dispatch to controller
		String errorMessage = null;
		String dept = req.getParameter("dept");
		String level = req.getParameter("level");
		String term = req.getParameter("term");
		QueryController controller=new QueryController(new Query(Integer.parseInt(term), level, dept), session.getCurrentUser());
		System.out.println(session.getCurrentUser());
		
		
		String returnedCourses="<tr><td>CRN</td><td>Course</td><td>Title</td>" +
				"<td>Credits</td><td>Type</td><td>Days</td><td>Time</td><td>Location 1</td>" +
				"<td>Location 2</td><td>Instructor 1</td><td>Instructor 2</td><td>Capacity</td> " +
				"<td>Seats Open</td><td>Enrolled</td><td>Begin-End</td><td>Add to Schedule</td></tr>";
		ArrayList<Course> courses=controller.getCourses();
		try {
			addCoursesToDB(courses);
		} catch (ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		for(Course c : courses){
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
			System.out.println(c.getCRN());
			if(req.getParameter("" + c.getCRN())!=null){
				try {
					controller.addToSchedule(c.getCRN());
					errorMessage="Course added successfully";
				} catch (ConflictException e) {
					errorMessage="Course conflicts with one on your schedule.";
					e.printStackTrace();
				}
				try {
					session.getAuth().saveExistingUserNewDataToDB(session.getCurrentUser());
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}	
			}
			req.setAttribute("" + c.getCRN(), null);
		}
		
		
		// Add parameters as request attributes
		req.setAttribute("dept", dept);
		req.setAttribute("level", level);
		req.setAttribute("term", term);
		req.setAttribute("returnedCourses", returnedCourses);
				
		// Add result objects as request attributes
		req.setAttribute("errorMessage", errorMessage);
		
		// Forward to view to render the result HTML document
		req.getRequestDispatcher("/_view/search.jsp").forward(req, resp);
	}
	
	public void addCoursesToDB(ArrayList<Course> courses) throws ClassNotFoundException, SQLException {
		Connection conn = null;
		try {
			Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
			conn = DriverManager.getConnection("jdbc:derby:test.db;create=true");
			conn.setAutoCommit(true);
		} catch (SQLException e) {
			System.out.println("Error: " + e.getMessage());
		}
		
		String sqlDelete="delete from courses where 1=1";
		SQLWriter.executeDBCommand(conn, sqlDelete);
		
		for (Course c : courses) {
			System.out.println(c.getCRN());
			String sql="insert into courses \n" +
					"(CRN, courseAndSection, title, credits, type, days, time, location_one, location_two, instructor_one, instructor_two, capacity, seatsRemain, seatsFilled, beginEnd) \n" +
					"values (\n'" +
					c.getCRN() + "', \n'" + c.getCourseAndSection() + "', \n'" +
					c.getTitle().replace("'", "''") + "', \n'" + c.getCredits() + "', \n'" + c.getType().replace("'", "''") + "', \n'" +
					c.getDays() + "', \n'" + c.getTime() + "', \n";
			if(c.getLocation().size()<2){
				sql=sql+"'" + c.getLocation().get(0) + "', \n" + "'null', \n";
			}
			else{
				sql=sql+"'" + c.getLocation().get(0) + "', \n'" + c.getLocation().get(1) + "', \n";
			}
			if(c.getInstructors().size()<2){
				sql=sql+"'" + c.getInstructors().get(0).replace("'", "''") + "', \n" + "'null', \n";
			}
			else{
				sql=sql+"'" + c.getInstructors().get(0).replace("'", "''") + "', \n'" + c.getInstructors().get(1).replace("'", "''") + "', \n";
			}
			sql=sql +"'" + c.getCapacity() + "', \n'" + c.getSeatsRemain() + "', \n'" + c.getSeatsFilled()
				+ "', \n'" + c.getBeginEnd() + "'" + ")";
			SQLWriter.executeDBCommand(conn, sql);
		}
	}
}