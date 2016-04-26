package fixedIt.servlets;

import java.io.IOException;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import fixedIt.controllers.LoginController;
import fixedIt.modelComponents.User;

public class AccountConfirmationServlet extends HttpServlet{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
		String emailAddress="";
		UUID sessionId=null;
		UUID uuid=(UUID) req.getSession().getAttribute("uuid");
		String errorMessage=(String) req.getAttribute("errorMessage");
		
		LoginController controller=new LoginController();
		
		String confirmed="";
		
		if(req.getQueryString()!=null){
			emailAddress=req.getQueryString().substring(req.getQueryString().indexOf('=')+1, req.getQueryString().indexOf('&'));
			String tmpId=req.getQueryString().substring(req.getQueryString().indexOf('&'));
			tmpId=tmpId.substring(tmpId.indexOf('=')+1);
			sessionId=UUID.fromString(tmpId);
		}
		if(uuid.equals(sessionId)){
			User user=new User(emailAddress, (String) req.getSession().getAttribute("passHash"), 0, 0, controller.getAuth());
			boolean success=controller.getAuth().addNewUserToDB(user);
			if(success){
				confirmed="true";
				errorMessage="Account confirmed successfully.";
			}
		} else{
			errorMessage="Invalid session ID.";
		}
		
		req.setAttribute("confirmed", confirmed);
		req.setAttribute("errorMessage", errorMessage);
		
		req.getRequestDispatcher("/_view/accountConfirmation.jsp").forward(req, resp);
	}
}
