package fixedIt.main;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.util.thread.QueuedThreadPool;
import org.eclipse.jetty.webapp.WebAppContext;

import fixedIt.modelComponents.Course;
import fixedIt.modelComponents.Registrar;
import fixedIt.sql.database.DBUtil;
import fixedIt.sql.database.SQLWriter;

public class Main {

	public static void main(String[] args) throws Exception{
		Connection conn = null;
		try {
			Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
			conn = DriverManager.getConnection("jdbc:derby:test.db;create=true");
			conn.setAutoCommit(true);
			initializeCoursesTable(conn);
		} catch (SQLException e) {
			System.out.println("Error: " + e.getMessage());
		} finally {
			DBUtil.closeQuietly(conn);
		}
		
		
		
		Server server = new Server(8081);

		// Create and register a webapp context
		WebAppContext handler = new WebAppContext();
		handler.setContextPath("/FixedIt");
		handler.setWar("./war"); // web app is in the war directory of the project
		server.setHandler(handler);
		
		// Use 20 threads to handle requests
		server.setThreadPool(new QueuedThreadPool(20));
		
		// Start the server
		server.start();
		
		// Wait for the user to type "quit"
		System.out.println("Web server started, type quit to shut down");
		Scanner keyboard = new Scanner(System.in);
		while (keyboard.hasNextLine()) {
			String line = keyboard.nextLine();
			if (line.trim().toLowerCase().equals("quit")) {
				break;
			}
		}
		keyboard.close();
		System.out.println("Shutting down...");
		server.stop();
		server.join();
		System.out.println("Server has shut down, exiting");
	}
	
	public static void initializeCoursesTable(Connection conn) throws SQLException, IOException{
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
				//System.out.println(sql);
				SQLWriter.executeDBCommand(conn, sql);
			}
		}
		//SQLWriter.executeSQL(conn, "select * from courses ");
	}
	
//	CRN + ", " + courseAndSection + ", " + title + ", " + credits + ", " + type + ", " + days + ", " +
//	time + ", " + locations + ", " + instructors + ", " + capacity + ", " + seatsRemain
//	+ ", " + seatsFilled + ", " + beginEnd
	
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
}
