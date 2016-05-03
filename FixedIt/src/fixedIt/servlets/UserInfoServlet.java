package fixedIt.servlets;
import java.io.IOException;
import java.util.Map.Entry;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import fixedIt.controllers.UserInfoController;
import fixedIt.modelComponents.Schedule;
import fixedIt.modelComponents.Session;


public class UserInfoServlet extends HttpServlet {			
	
	private static final long serialVersionUID = 1L;

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
		String errorMessage = null;
		UserInfoController controller=new UserInfoController((fixedIt.modelComponents.Session) req.getSession().getAttribute("userSession"));
		String emailAddress="";
		String numSchedules="";
		String studentStatus="";
		String activeSchedule=req.getParameter("scheduleList");	
		
		//controller.getUser().setActiveSchedule(controller.getUser().getSchedule(activeSchedule));
		
		if(controller.isSessionNull()){
			errorMessage="Error loading session.";
		}
		else{
			emailAddress=controller.getUser().getEmailAddress();
			numSchedules="" + controller.getUser().getNumSchedules();
			
			if(controller.getUser().getStudentStatus()==0){
				studentStatus="Full Time";
			}
			else{
				studentStatus="Part Time";
			}
		}
		
		String scheduleList="<select class=\"selectBox\" name=\"scheduleList\" size=\"1\">";
		int index=0;
		for(Entry<String, Schedule> e : controller.getUser().getSchedules().entrySet()){
			Schedule s=e.getValue();
			System.out.println(s.getName());
			if(index==0){
				scheduleList=scheduleList + "<option class=\"option\"  VALUE=\"" + s.getName() + 
					"\" selected=\"selected\">" + s.getName() + "</option> \n";
			} else{
				scheduleList=scheduleList + "<option class=\"option\"  VALUE=\"" + s.getName() + 
					"\">" + s.getName() + "</option> \n";
			}
			index++;
		}
		scheduleList=scheduleList + "</select>";
		
		req.setAttribute("emailAddress", emailAddress);
		req.setAttribute("numSchedules", numSchedules);
		req.setAttribute("studentStatus", studentStatus);
		req.setAttribute("errorMessage", errorMessage);	
		req.setAttribute("scheduleList", scheduleList);
		req.getRequestDispatcher("/_view/userInfo.jsp").forward(req, resp);
	
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		String errorMessage ="Successfully changed active schedule";
		UserInfoController controller=new UserInfoController((fixedIt.modelComponents.Session) req.getSession().getAttribute("userSession"));
		String activeSchedule=req.getParameter("scheduleList");		
		
		controller.getUser().setActiveSchedule(controller.getUser().getSchedule(activeSchedule));
	
		req.setAttribute("errorMessage", errorMessage);	
		req.setAttribute("activeSchedule", activeSchedule);
		
		// Forward to view to render the result HTML document
		req.getRequestDispatcher("/_view/userInfo.jsp").forward(req, resp);
	}
}

