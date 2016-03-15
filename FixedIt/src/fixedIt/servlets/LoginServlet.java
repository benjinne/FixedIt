package fixedIt.servlets;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import fixedIt.controllers.LoginController;
import fixedIt.modelComponents.Course;
import fixedIt.modelComponents.Registrar;
import fixedIt.sql.database.DBUtil;
import fixedIt.sql.database.SQLWriter;


public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	boolean credentialsMatch=false;

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
		String emailAddress = getStringFromParameter(req.getParameter("emailAddress"));
		String password = getStringFromParameter(req.getParameter("password"));
		LoginController controller=new LoginController();
		if (emailAddress == null || password == null) {
			errorMessage = "Please enter an email address and password.";
		} else {
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
		req.setAttribute("credentialsMatch", credentialsMatch);
		
		// Forward to view to render the result HTML document
		req.getRequestDispatcher("/_view/login.jsp").forward(req, resp);
		try {
			initializeCoursesTable();
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static void initializeCoursesTable() throws SQLException, IOException, ClassNotFoundException{
		Connection conn = null;
		try {
			Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
			conn = DriverManager.getConnection("jdbc:derby:test.db;create=true");
			conn.setAutoCommit(true);
		} catch (SQLException e) {
			System.out.println("Error: " + e.getMessage());
		} finally {
			DBUtil.closeQuietly(conn);
		}
		
		ArrayList<String> depts=new ArrayList<String>();
		addAllDepts(depts);
		String sqlDelete="delete from courses where 1=1";
		SQLWriter.executeDBCommand(conn, sqlDelete);
		for(String s : depts){
			Registrar r=new Registrar("http://ycpweb.ycp.edu/schedule-of-classes/index.html?term=201520" + "&stype=A&dmode=D&dept=" + s);
			for (Course c : r.fetch()) {
				String sql="insert into courses \n" +
						"(CRN, courseAndSection, title, credits, type, days, time, location_one, location_two, instructor_one, instructor_two, capacity, seatsRemain, seatsFilled, beginEnd) \n" +
						"values (\n'" +
						c.getCRN() + "', \n'" + c.getCourseAndSection() + "', \n'" +
						c.getTitle().replace("'", "''") + "', \n'" + c.getCredits() + "', \n'" + c.getType().replace("'", "''") + "', \n'" +
						c.getDays() + "', \n'" + c.getTime() + "', \n";
				if(c.getLocation().size()<2){
					sql=sql+"'" + c.getLocation().get(0) + "', \n" + "'null', \n";
				}
				else{
					sql=sql+"'" + c.getLocation().get(0) + "', \n'" + c.getLocation().get(1) + "', \n";
				}
				if(c.getInstructors().size()<2){
					sql=sql+"'" + c.getInstructors().get(0).replace("'", "''") + "', \n" + "'null', \n";
				}
				else{
					sql=sql+"'" + c.getInstructors().get(0).replace("'", "''") + "', \n'" + c.getInstructors().get(1).replace("'", "''") + "', \n";
				}
				sql=sql +"'" + c.getCapacity() + "', \n'" + c.getSeatsRemain() + "', \n'" + c.getSeatsFilled()
					+ "', \n'" + c.getBeginEnd() + "'" + ")";
				SQLWriter.executeDBCommand(conn, sql);
			}
		}
	}
	
	public static void addAllDepts(ArrayList<String> depts){
		depts.add("ANT_01");
		depts.add("BEH_01");
		depts.add("CJA_01");
		depts.add("GER_01");
		depts.add("HSV_01");
		depts.add("PSY_01");
		depts.add("SOC_01");
		depts.add("BIO_02");
		depts.add("PMD_02");
		depts.add("RT_02");
		depts.add("ACC_03");
		depts.add("ECO_03");
		depts.add("ENT_03");
		depts.add("FIN_03");
		depts.add("BUS_03");
		depts.add("IFS_03");
		depts.add("IBS_03");
		depts.add("MGT_03");
		depts.add("MKT_03");
		depts.add("QBA_03");
		depts.add("SCM_03");
		depts.add("ART_07");
		depts.add("CM_07");
		depts.add("MUS_07");
		depts.add("THE_07");
		depts.add("ECH_04");
		depts.add("EDU_04");
		depts.add("MLE_04");
		depts.add("SE_04");
		depts.add("SPE_04");
		depts.add("CS_12");
		depts.add("ECE_12");
		depts.add("EGR_12");
		depts.add("ME_12");
		depts.add("PHY_12");
		depts.add("FLM_05");
		depts.add("FCO_05");
		depts.add("FRN_05");
		depts.add("GRM_05");
		depts.add("HUM_05");
		depts.add("INT_05");
		depts.add("ITL_05");
		depts.add("LAT_05");
		depts.add("LIT_05");
		depts.add("PHL_05");
		depts.add("REL_05");
		depts.add("RUS_05");
		depts.add("SPN_05");
		depts.add("WRT_05");
		depts.add("G_06");
		depts.add("HIS_06");
		depts.add("IA_06");
		depts.add("INT_06");
		depts.add("PS_06");
		depts.add("HSP_11");
		depts.add("PE_11");
		depts.add("REC_11");
		depts.add("SPM_11");
		depts.add("FYS_10");
		depts.add("SES_10");
		depts.add("WGS_10");
		depts.add("NUR_08");
		depts.add("CHM_09");
		depts.add("ESS_09");
		depts.add("FCM_09");
		depts.add("MAT_09");
		depts.add("PSC_09");
		depts.add("PHY_09");
	}

	private String getStringFromParameter(String s) {
		if (s == null || s.equals("")) {
			return null;
		} else {
			return s;
		}
	}
}
