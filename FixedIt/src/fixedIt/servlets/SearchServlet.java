package fixedIt.servlets;
import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import fixedIt.controllers.QueryController;
import fixedIt.modelComponents.Course;
import fixedIt.modelComponents.Query;
import fixedIt.modelComponents.Session;

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
		
		
		String returnedCourses="<tr><td>CRN</td><td>Course</td><td>Title</td>" +
				"<td>Credits</td><td>Type</td><td>Days</td><td>Time</td><td>Location 1</td>" +
				"<td>Location 2</td><td>Instructor 1</td><td>Instructor 2</td><td>Capacity</td> " +
				"<td>Seats Open</td><td>Enrolled</td><td>Begin-End</td><td>Add to Schedule</td></tr>";
		try{
			ArrayList<Course> courses=controller.getCourses();
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
				if(req.getParameter("" + c.getCRN())!=null){
					controller.addToSchedule(c.getCRN());
					errorMessage="Course added successfully";
				}
				req.setAttribute("" + c.getCRN(), null);
			}
		}
		catch(Exception e){
			e.printStackTrace();
			returnedCourses=null;
			errorMessage="Invalid combination of search parameters. Try another search.";
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
}