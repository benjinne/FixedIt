package fixedIt.servlets;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import fixedIt.controllers.LoginController;
import fixedIt.modelComponents.Session;
import fixedIt.webUtils.CaptchaResponse;


public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	boolean credentialsMatch=false;
	private String secretKey="6Ldd2hwTAAAAAFUWTCeAgUkdrRWa4dX5FJjxsX8y";

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		resp.setHeader("Cache-Control","no-cache");
		resp.setHeader("Cache-Control","no-store");
		req.setAttribute("loginAttempts", 0);
		req.getSession().setAttribute("userSession", null);
		req.getRequestDispatcher("/_view/login.jsp").forward(req, resp);
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		if(req.getParameter("passwordReset")!=null){
			resp.sendRedirect("passwordReset");
			return;
		}
		
		// Decode form parameters and dispatch to controller
		String errorMessage = null;
		String recaptchaHTML=null;
		String emailAddress = getStringFromParameter(req.getParameter("emailAddress"));
		String password = getStringFromParameter(req.getParameter("password"));
		String recap = req.getParameter("g-recaptcha-response");
		Integer loginAttempts=(Integer) req.getSession().getAttribute("loginAttempts");
		if(loginAttempts==null){
			loginAttempts=0;
		}
		System.out.println(loginAttempts);
		
		LoginController controller=new LoginController();
		Session userSession=null;
		
		
	
		
		if (emailAddress == null || password == null) {
			errorMessage = "Please enter an email address and password.";
		} else {
			if(loginAttempts<3){
				if(controller.getAuth().userExists(emailAddress)){
					credentialsMatch=controller.getAuth().credentialsMatch(emailAddress, password);
					if(!credentialsMatch){
						errorMessage="Email address and password do not match.";
						loginAttempts++;
					}
					else{
						try {
							if(emailAddress != null && password != null && req.getParameter("debug")== null ){
							userSession=controller.getAuth().authorizeUser(emailAddress, password);
							req.getSession().setAttribute("userSession", userSession);
								if(userSession!=null){
									if(userSession.getCurrentUser().getSchedules().size()!=0){
										if(userSession.getCurrentUser().getSchedules().firstEntry().getValue()!=null){
											userSession.getCurrentUser().setActiveSchedule(userSession.getCurrentUser().getSchedules().firstEntry().getValue());
										}
									}
									resp.sendRedirect("userInfo");
									return;
							}
							else{
								errorMessage="Failed to populate User object!";
								loginAttempts++;
							}
							}
						} catch (SQLException  e) {
							loginAttempts++;
							e.printStackTrace();
						}
					
					}
				}
				else{
					loginAttempts++;
					errorMessage="No account exists for this email address.";
				}
			}else{
				recaptchaHTML="<div class=\"g-recaptcha\" data-sitekey=\"6Ldd2hwTAAAAAGJwO9Gwe-sXAtjnM8zSdoNr0hMB\"></div>";
				req.setAttribute("recaptcha", recaptchaHTML);
				if(recap!=null){
					URL googleVerifyUrl = new URL("https://www.google.com/recaptcha/api/siteverify?secret="+secretKey+"&response="+recap+"&remoteip="+req.getRemoteAddr());
					HttpURLConnection conn = (HttpURLConnection) googleVerifyUrl.openConnection();
					conn.setRequestMethod("GET");
					String line;
					String outputString = "";
					BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
					while ((line = reader.readLine()) != null) {
					    outputString += line;
					}
					System.out.println(outputString);
					CaptchaResponse capRes = new Gson().fromJson(outputString, CaptchaResponse.class);
					if(capRes.isSuccess()) {
						loginAttempts=0;
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
										recaptchaHTML=null;
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
					} else {
					    errorMessage="We couldn't verify that you're a human. Do with that what you will.";
					    loginAttempts=0;
					    recaptchaHTML=null;
					}
				}
			}
		}
		//debug mode allows us to enter the scheduler with a fake account at the click of a button
		//no need to enter a user name and password each time 
		//thus allowing us to test new features that we have implemented
			if(req.getParameter("debug")!= null && emailAddress !=null && password!=null){
				emailAddress =null;
				password=null;
				req.setAttribute("debug", null);
			
			userSession= controller.DebugMode();
			req.getSession().setAttribute("userSession", userSession);
			resp.sendRedirect("userInfo");
			return;
			}
			else if(req.getParameter("debug")!=null){
				req.setAttribute("debug", null);
				
				userSession= controller.DebugMode();
				req.getSession().setAttribute("userSession", userSession);
				resp.sendRedirect("userInfo");
				return;
			}
		
		// Add parameters as request attributes
		req.setAttribute("emailAddress", req.getParameter("emailAddress"));
		req.setAttribute("password", req.getParameter("password"));
		
		// Add result objects as request attributes
		req.setAttribute("debug", null);
		req.setAttribute("errorMessage", errorMessage);
		req.setAttribute("credentialsMatch", credentialsMatch);
		req.getSession().setAttribute("loginAttempts", loginAttempts);
		req.setAttribute("recaptchaHTML", recaptchaHTML);
		
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
