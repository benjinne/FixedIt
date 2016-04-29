package fixedIt.servlets;

import java.io.IOException;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import fixedIt.controllers.PasswordResetController;

public class PasswordResetServlet extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		System.out.println(req.getQueryString());
		if(req.getQueryString()!=null){
			String sessionId=req.getQueryString().substring(req.getQueryString().indexOf('=')+1, req.getQueryString().indexOf('&'));
			String emailAddress=req.getQueryString().substring(req.getQueryString().indexOf('&'));
			emailAddress=emailAddress.substring(emailAddress.indexOf('='));
			req.getSession().setAttribute("sessionId", sessionId);
			req.getSession().setAttribute("emailAddress", emailAddress);
		}
		req.getRequestDispatcher("/_view/PasswordReset.jsp").forward(req, resp);
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String emailAddress=(String) req.getSession().getAttribute("emailAddress");
		String errorMessage=req.getParameter("errorMessage");
		PasswordResetController controller=new PasswordResetController(emailAddress);
		
		UUID resetId=null;
		if(req.getSession().getAttribute("uuid")==null){
			resetId=UUID.randomUUID();
			req.getSession().setAttribute("uuid", resetId);
			controller.requestPasswordReset(emailAddress, req.getRequestURL().toString(), resetId);
		} else{
			resetId=(UUID)req.getSession().getAttribute("uuid");
		}
		UUID sessionId=null;
		System.out.println("QueryString: " + req.getQueryString());
		System.out.println("req.getSession().getAttribute(\"sessionId\": " + req.getSession().getAttribute("sessionId"));
		if(req.getQueryString()!=null && req.getQueryString()!="null"){
			sessionId=UUID.fromString(req.getQueryString().substring(req.getQueryString().indexOf('=')+1));
			req.getSession().setAttribute("sessionId", sessionId);
		} else if(req.getSession().getAttribute("sessionId")!=null && req.getSession().getAttribute("sessionId")!="null"){
			sessionId=UUID.fromString((String)req.getSession().getAttribute("sessionId"));
		}
		
		System.out.println(sessionId);
		
		String password=req.getParameter("password");
		String passwordConfirm=req.getParameter("passwordConfirm");
		String success="";
		
		if(sessionId!=null){
			if(controller.userExists()){
				if(password!=null && passwordConfirm!=null){
					if(password.equals(passwordConfirm)){
						boolean isValidPass=controller.resetPassword("" + resetId, "" + sessionId, password);
						if(!isValidPass){
							errorMessage="Password does not conform to password rules: password must be at least 8 characters and may use the following characters: \n" +
									"0-9a-zA-Z ! . - _";
						} else{
							errorMessage="Password reset successfully.";
							success="true";
						}
					} else{
						errorMessage="Passwords do not match.";
					}
				} else{
					errorMessage="Please type and confirm a password.";
				}
			} else{
				errorMessage="Email address does not match any accounts on record.";
			}
		} else{
			if(emailAddress!=null){
				errorMessage="Please check your email for instructions to reset your password.";
			} else{
				errorMessage="Please enter an email address.";
			}
		}
		
		req.setAttribute("errorMessage", errorMessage);
		req.setAttribute("emailAddress", emailAddress);
		req.setAttribute("sessionId", sessionId);
		req.getSession().setAttribute("uuid", resetId);
		req.setAttribute("success", success);
		
		req.getRequestDispatcher("/_view/PasswordReset.jsp").forward(req, resp);
	}
}
