package fixedIt.servlets;
import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import fixedIt.controllers.UserInfoController;
import fixedIt.controllers.LoginController;
import fixedIt.modelComponents.Authenticator;
import fixedIt.modelComponents.Session;


public class PasswordUpdateServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	boolean accountCreated=false;
	Authenticator auth;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		if((fixedIt.modelComponents.Session) req.getSession().getAttribute("userSession")!=null){
			try {
				((Session) req.getSession().getAttribute("userSession")).getAuth().saveExistingUserNewDataToDB(((Session) req.getSession().getAttribute("userSession")).getCurrentUser());
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		resp.setHeader("Cache-Control","no-cache");
		resp.setHeader("Cache-Control","no-store");
		if((fixedIt.modelComponents.Session) req.getSession().getAttribute("userSession")==null){
			resp.sendRedirect("login");
			return;
		}
		else req.getRequestDispatcher("/_view/passwordUpdate.jsp").forward(req, resp);
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		// Decode form parameters and dispatch to userInfoController
		String errorMessage = null;
		String password = getStringFromParameter(req.getParameter("password"));
		String newPassword = getStringFromParameter(req.getParameter("newPassword"));
		String newPasswordConfirm = getStringFromParameter(req.getParameter("newPasswordConfirm"));
		UserInfoController userInfoController=new UserInfoController((fixedIt.modelComponents.Session) req.getSession().getAttribute("userSession"));
		LoginController loginController=new LoginController();
		//if (emailAddress == null || password == null) {
			//errorMessage = "Please enter an email address and password.";
		//} 
		if(!loginController.getAuth().credentialsMatch(userInfoController.getUser().getEmailAddress(), password)){
			errorMessage="The current password does not match.";
		}
		else if(!newPassword.equals(newPasswordConfirm)) {
			errorMessage="The new passwords do not match.";
		}
		else if(!loginController.getAuth().isValidPassword(newPassword)){
			errorMessage="Password does not conform to password rules: password must be at least 8 characters and may use the following characters: \n" +
						"0-9a-zA-Z ! . - _";
		}
		else if(loginController.getAuth().isValidPassword(newPassword) && newPassword.equals(newPasswordConfirm) && loginController.getAuth().credentialsMatch(userInfoController.getUser().getEmailAddress(), password)) {
			try {
				userInfoController.getUser().setPasswordHash(loginController.getAuth().saltHashPassword(newPassword));
				loginController.getAuth().saveExistingUserNewDataToDB(userInfoController.getUser());
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		// Add parameters as request attributes
		req.setAttribute("password", password);
		req.setAttribute("newPassword", newPassword);
		req.setAttribute("newPasswordConfirm", newPasswordConfirm);
		
		// Add result objects as request attributes
		req.setAttribute("errorMessage", errorMessage);		
		
		// Forward to view to render the result HTML document
		if(errorMessage!=null){
			req.getRequestDispatcher("/_view/passwordUpdate.jsp").forward(req, resp);
			return;
		}
		resp.sendRedirect("userInfo");
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
