package fixedIt.servlets;
import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import fixedIt.controllers.LoginController;
import fixedIt.modelComponents.Session;


public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	boolean credentialsMatch=false;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		resp.setHeader("Cache-Control","no-cache");
		resp.setHeader("Cache-Control","no-store");
		req.getSession().setAttribute("userSession", null);
		req.getRequestDispatcher("/_view/login.jsp").forward(req, resp);
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		// Decode form parameters and dispatch to controller
		String errorMessage = null;
		String emailAddress = getStringFromParameter(req.getParameter("emailAddress"));
		String password = getStringFromParameter(req.getParameter("password"));
		LoginController controller=new LoginController();
		Session userSession=null;
		if (emailAddress == null || password == null) {
			errorMessage = "Please enter an email address and password.";
		} else {
			if(controller.getAuth().userExists(emailAddress)){
				credentialsMatch=controller.getAuth().credentialsMatch(emailAddress, password);
				if(!credentialsMatch){
					errorMessage="Email address and password do not match.";
				}
				else{
					try {
						userSession=controller.getAuth().authorizeUser(emailAddress, password);
						req.getSession().setAttribute("userSession", userSession);
						if(userSession!=null){
							resp.sendRedirect("userInfo");
							return;
						}
						else{
							errorMessage="Failed to populate User object!";
						}
					} catch (SQLException  e) {
						e.printStackTrace();
					}
				}
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
		req.setAttribute("credentialsMatch", credentialsMatch);
		
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
