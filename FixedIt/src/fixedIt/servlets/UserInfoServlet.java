package fixedIt.servlets;
import java.io.IOException;
import java.util.Map.Entry;
import java.util.regex.Pattern;
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
		String activeSchedule="";
		if(controller.getUser().getActiveSchedule()!=null){
			activeSchedule=controller.getUser().getActiveSchedule().getName();
		} else{
			controller.getUser().setActiveSchedule(controller.getUser().getSchedules().firstEntry().getValue());
		}
		//String newSchedule = req.getParameter("newSchedule");
		
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
		for(Entry<String, Schedule> e : controller.getUser().getSchedules().entrySet()){
			Schedule s=e.getValue();
			//System.out.println(s.getName());
			if(controller.getUser().getActiveSchedule().equals(controller.getUser().getSchedule(e.getKey()))){
				scheduleList=scheduleList + "<option class=\"option\"  VALUE=\"" + s.getName().toUpperCase() + 
					"\" selected=\"selected\">" + s.getName() + "</option> \n";
			} else{
				scheduleList=scheduleList + "<option class=\"option\"  VALUE=\"" + s.getName().toUpperCase() + 
					"\">" + s.getName() + "</option> \n";
			}
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
		Session session=null;
		if((fixedIt.modelComponents.Session) req.getSession().getAttribute("userSession")==null){
			resp.sendRedirect("login");
			return;
		} else{
			session=(fixedIt.modelComponents.Session) req.getSession().getAttribute("userSession");
		}
		
		String selectedForDelete=req.getParameter("deleteScheduleList");
		String errorMessage ="";
		UserInfoController controller=new UserInfoController(session);
		controller.setUser(session.getCurrentUser());
		String activeSchedule=req.getParameter("scheduleList");

		if (req.getParameter("newSchedule")!=null) {
			if(controller.getUser().getNumSchedules()<6){
				if(req.getParameter("scheduleName")!= null && !req.getParameter("scheduleName").isEmpty()){
					if(isAlphaNumeric(req.getParameter("scheduleName"))){
						controller.getUser().createSchedule(req.getParameter("scheduleName").toUpperCase());
						errorMessage="Schedule created successfully.";
						try {
							session.getAuth().saveExistingUserNewDataToDB(controller.getUser());
						} catch (SQLException e1) {
							errorMessage="Database write error; please try again.";
							e1.printStackTrace();
						}
					} else{
						errorMessage="Schedule names may contain only letters and numbers.";
					}
				}
				else{
					errorMessage="Please Name This Schedule";
				}
			}
			else{
				errorMessage= "Max number of schedules created";
			}
		}
		else if(req.getParameter("selectSchedule")!= null){
			if(activeSchedule!=null){
				if(controller.getUser().getSchedule(activeSchedule)!= null){
					controller.getUser().setActiveSchedule(controller.getUser().getSchedule(activeSchedule));
					errorMessage="Successfully changed active schedule";
				} else{
					errorMessage ="Schedule does not exist.";
				}
			} else{
				errorMessage="You must create a new schedule first.";
			}
		}
		
		String scheduleList="<select class=\"selectBox\" name=\"scheduleList\" size=\"1\">";
		for(Entry<String, Schedule> e : controller.getUser().getSchedules().entrySet()){
			Schedule s=e.getValue();
			System.out.println(s.getName());
			if(controller.getUser().getActiveSchedule().equals(controller.getUser().getSchedule(e.getKey()))){
				scheduleList=scheduleList + "<option class=\"option\"  VALUE=\"" + s.getName().toUpperCase() + 
					"\" selected=\"selected\">" + s.getName() + "</option> \n";
			} else{
				scheduleList=scheduleList + "<option class=\"option\"  VALUE=\"" + s.getName().toUpperCase() + 
					"\">" + s.getName() + "</option> \n";
			}
		}
		scheduleList=scheduleList + "</select>";
		
		req.getSession().setAttribute("userSession", session);
		req.setAttribute("selectSchedule", null);
		req.setAttribute("newSchedule", null);
		req.setAttribute("emailAddress",controller.getUser().getEmailAddress());
		req.setAttribute("numSchedules", controller.getUser().getNumSchedules());
		req.setAttribute("studentStatus", controller.getUser().getStudentStatus());
		req.setAttribute("scheduleList", scheduleList);
		req.setAttribute("newSchedule", null);
		req.setAttribute("selectSchedule", null);
		req.setAttribute("errorMessage", errorMessage);	
		
		// Forward to view to render the result HTML document
		req.getRequestDispatcher("/_view/userInfo.jsp").forward(req, resp);
	}
	
	public boolean isAlphaNumeric(String name) {
		Pattern p=Pattern.compile("[^a-zA-Z0-9]");
		return !p.matcher(name).find();
	}
}

