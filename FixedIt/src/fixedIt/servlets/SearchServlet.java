package fixedIt.servlets;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
		String searchBySubject = getStringFromParameter(req.getParameter("course"));
		String searchBySection = getStringFromParameter(req.getParameter("section"));
		String searchByLevel = getStringFromParameter(req.getParameter("level"));
		
		
		// Add parameters as request attributes
		req.setAttribute("searchBySubject", req.getParameter("course"));
		req.setAttribute("searchBySection", req.getParameter("section"));
		req.setAttribute("searchByLevel", req.getParameter("level"));
				
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

