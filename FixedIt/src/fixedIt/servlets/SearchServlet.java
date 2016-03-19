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

//NEEDS THE CONTROLLER TO IMPLEMENT

public class SearchServlet extends HttpServlet {			
	
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		req.getRequestDispatcher("/_view/search.jsp").forward(req, resp);
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		// Decode form parameters and dispatch to controller
		String errorMessage = null;
		String dept = req.getParameter("dept");
		String level = req.getParameter("level");
		String term = req.getParameter("term");
		String returnedCourses="<table class=\"courseTable\"><tr><td>CRN</td><td>Course</td><td>Title</td>" +
				"<td>Credits</td><td>Type</td><td>Days</td><td>Time</td><td>Location 1</td>" +
				"<td>Location 2</td><td>Instructor 1</td><td>Instructor 2</td><td>Capacity</td> " +
				"<td>Seats Open</td><td>Seats Filled</td><td>Begin-End</td></tr>";
		QueryController controller=new QueryController(new Query(Integer.parseInt(term), level, dept));
		for(Course c : controller.getCourses()){
			returnedCourses.concat("<tr><td>" + c.getCRN() + "</td>");
			returnedCourses.concat("<td>" + c.getCourseAndSection() + "</td>");
			returnedCourses.concat("<td>" + c.getTitle() + "</td>");
			returnedCourses.concat("<td>" + c.getCredits() + "</td>");
			returnedCourses.concat("<td>" + c.getType() + "</td>");
			returnedCourses.concat("<td>" + c.getDays() + "</td>");
			returnedCourses.concat("<td>" + c.getTime() + "</td>");
			returnedCourses.concat("<td>" + c.getLocation().get(0) + "</td>");
			if(c.getLocation().size()>1){
				returnedCourses.concat("<td>" + c.getLocation().get(1) + "</td>");
			}
			else{
				returnedCourses.concat("<td> </td>");
			}
			returnedCourses.concat("<td>" + c.getInstructors().get(0) + "</td>");
			if(c.getInstructors().size()>1){
				returnedCourses.concat("<td>" + c.getInstructors().get(1) + "</td>");
			}
			else{
				returnedCourses.concat("<td> </td>");
			}
			returnedCourses.concat("<td>" + c.getCapacity() + "</td>");
			returnedCourses.concat("<td>" + c.getSeatsRemain() + "</td>");
			returnedCourses.concat("<td>" + c.getSeatsFilled() + "</td>");
			returnedCourses.concat("<td>" + c.getBeginEnd() + "</td></tr>");
		}
		
		// Add parameters as request attributes
		req.setAttribute("dept", req.getParameter("dept"));
		req.setAttribute("level", req.getParameter("level"));
		req.setAttribute("term", req.getParameter("term"));
		req.setAttribute("returnedCourses", returnedCourses);
				
		// Add result objects as request attributes
		req.setAttribute("errorMessage", errorMessage);
		
		// Forward to view to render the result HTML document
		req.getRequestDispatcher("/_view/search.jsp").forward(req, resp);
	}

	private String getStringFromParameter(String s) {
		if (s == null || s.equals("")) {
			return null;
		} else {
			return s;
		}
	}
}

