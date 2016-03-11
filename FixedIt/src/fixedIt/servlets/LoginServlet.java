package fixedIt.servlets;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import fixedIt.controllers.LoginController;


public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		req.getRequestDispatcher("/_view/login").forward(req, resp);
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		// Decode form parameters and dispatch to controller
		String errorMessage = null;
		boolean credentialsMatch=false;
		String emailAddress = getStringFromParameter(req.getParameter("emailAddress"));
		String password = getStringFromParameter(req.getParameter("password"));

		if (emailAddress == null || password == null) {
			errorMessage = "Please enter an email address and password.";
		} else {
			LoginController controller=new LoginController(emailAddress, password);
			if(controller.getAuth().userExists(emailAddress)){
				credentialsMatch=controller.getAuth().credentialsMatch(emailAddress, password);
			}
			else{
				errorMessage="No account exists for this email address.";
			}
		}
		
		// Add parameters as request attributes
		req.setAttribute("emailAddress", req.getParameter("emailAddress"));
		req.setAttribute("password", req.getParameter("password"));
		
		// Add result objects as request attributes
		req.setAttribute("errorMessage", errorMessage);
		req.setAttribute("result", credentialsMatch);
		
		// Forward to view to render the result HTML document
		req.getRequestDispatcher("/_view/login.jsp").forward(req, resp);
	}

	private String getStringFromParameter(String s) {
		if (s == null || s.equals("")) {
			return null;
		} else {
			return s;
		}
	}
}
