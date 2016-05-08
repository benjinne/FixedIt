package fixedIt.servlets;
import java.io.IOException;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import fixedIt.controllers.LoginController;
import fixedIt.modelComponents.Authenticator;


public class NewUserServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	Authenticator auth;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String errorMessage = req.getParameter("errorMessage");
		req.setAttribute("errorMessage", errorMessage);
		req.getRequestDispatcher("/_view/newUser.jsp").forward(req, resp);
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		// Decode form parameters and dispatch to controller
		String errorMessage = req.getParameter("errorMessage");
		String emailAddress = getStringFromParameter(req.getParameter("emailAddress"));
		String password = getStringFromParameter(req.getParameter("password"));
		String passwordConfirm=getStringFromParameter(req.getParameter("passwordConfirm"));
		LoginController controller=new LoginController();
		String waitingForConfirm="";
		
		if (emailAddress == null || password == null) {
			errorMessage = "Please enter an email address and password.";
		}
		else if(!controller.getAuth().isValidEmailAddress(emailAddress)){
			errorMessage="Email address is not recognized as email address format: address@example.com";
		}
		else if(!password.equals(passwordConfirm)){
			errorMessage="Passwords do not match.";
		}
		else if(!controller.getAuth().isValidPassword(password)){
			errorMessage="Password does not conform to password rules: password must be at least 8 characters and may use the following characters: \n" +
						"0-9a-zA-Z ! . - _";
		}
		else {
			boolean userExists=controller.getAuth().userExists(emailAddress);
			if(userExists){
				errorMessage="An account already exists associated with this email address.";
			}
			else{
				UUID uuid=UUID.randomUUID();
				req.getSession().setAttribute("uuid", uuid);
				req.getSession().setAttribute("passHash", controller.getAuth().saltHashPassword(password));
				String webContext=req.getRequestURL().toString().replace("register", "confirm");
				controller.getAuth().sendConfirmEmail(emailAddress, webContext, uuid);
				waitingForConfirm="true";
			}
		}
		
		// Add parameters as request attributes
		req.setAttribute("emailAddress", req.getParameter("emailAddress"));
		req.setAttribute("password", req.getParameter("password"));
		req.setAttribute("passwordConfirm", req.getParameter("passwordConfirm"));
		
		// Add result objects as request attributes
		req.setAttribute("errorMessage", errorMessage);
		req.setAttribute("waitingForConfirm", waitingForConfirm);
		
		// Forward to view to render the result HTML document
		req.getRequestDispatcher("/_view/newUser.jsp").forward(req, resp);
	}
	
	public void setAuth(Authenticator auth){
		this.auth=auth;
	}

	private String getStringFromParameter(String s) {
		if (s == null || s.equals("")) {
			return null;
		} else {
			return s;
		}
	}
}
