package fixedIt.servlets;
import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import fixedIt.controllers.UserInfoController;



public class EditUserInfoServlet extends HttpServlet {			
	
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
		else req.getRequestDispatcher("/_view/editUserInfo.jsp").forward(req, resp);
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
			
		// Decode form parameters and dispatch to controller	
		String errorMessage = null;
		UserInfoController controller=new UserInfoController((fixedIt.modelComponents.Session) req.getSession().getAttribute("userSession"));
		String emailAddress = getStringFromParameter(req.getParameter("emailAddress"));		
		String studentStatus= req.getParameter("studentStatus");	
		
		fixedIt.modelComponents.Session s = (fixedIt.modelComponents.Session)req.getSession().getAttribute("userSession");  
		if(controller.isSessionNull()){
			errorMessage="Error loading session.";
		}
		else{
			emailAddress=controller.getUser().getEmailAddress();
			if(studentStatus.toLowerCase().contains("full")){
				controller.getUser().setStudentStatus(0);     
			}
			else if(studentStatus.toLowerCase().contains("part")){
				controller.getUser().setStudentStatus(1);   
			}		
			else {
				errorMessage="studentStatus was not read correctly!";
			}
		
			
			if(!emailAddress.isEmpty()) {
				//make Authenticator method to change email
			} 
			
		try {
			s.getAuth().saveExistingUserNewDataToDB(controller.getUser());
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
		
		// Add parameters as request attributes			
		req.setAttribute("emailAddress", emailAddress);
		req.setAttribute("studentStatus", studentStatus);
		req.setAttribute("errorMessage", errorMessage);	
		
		// Forward to view to render the result HTML document
		if(errorMessage!=null){
			req.getRequestDispatcher("/_view/editUserInfo.jsp").forward(req, resp);
			return;
		}
		resp.sendRedirect("userInfo");
	}

	private String getStringFromParameter(String s) {
		if (s == null || s.equals("")) {
			return null;
		} else {
			return s;
		}
	}
}
