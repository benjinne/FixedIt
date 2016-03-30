package fixedIt.controllers;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import fixedIt.modelComponents.Course;
import fixedIt.modelComponents.Query;
import fixedIt.modelComponents.Registrar;
import fixedIt.modelComponents.User;
//HAVE TO BE ABLE TO GRAB VARIABLES FROM JSP
import fixedIt.sql.database.SQLWriter;

public class QueryController {
	private Query query;
	private User user;
	ArrayList <String> depts;
	public QueryController(){
		query= new Query();
	}
	
	public QueryController(Query query, User user){
		this.query=query;
		this.user=user;
	}
	
	public void setQuery(Query query){
		this.query=query;
	}
	
	public Query getQuery(){
		return query;
	}
	
	public ArrayList<Course> getCourses(){
		Registrar registrar = query.createRegistrar();
		try {
			return registrar.fetch();
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
	public void searchSetUp(){
		depts=new ArrayList<String>();
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
	
	
	public void convertSearch(String course, String section , String level){
		
		if (level== "undergraduate"){
			level = Query.LEVEL_UNDERGRAD;
		}
		else if (level == "graduate"){
			level= Query.LEVEL_GRADUATE;
		}
		else{
			level = Query.LEVEL_EVENING_AND_SATURDAY_UNDERGRAD;
			
		}
		
		if (section == "fall2016"){
			section = "" + Query.FALL_2016;
		}
		else if(section == "special session"){
			section = "" + Query.SPECIAL_SESSION_2016;
		}
		else if (section == "summer I"){
		section = ""+ Query.SUMMER_I_2016;
		}
		else if (section == "summer II"){
			section = "" + Query.SUMMER_II_2016;
			
		}
		else if(section == "spring 2016"){
			section = ""+ Query.SPRING_2016;
			
		}
		else{
			section = "" + Query.MINIMESTER_2016;
		}
	}
	
	//not implemented yet.
	private Course getCourse(int CRN){
		Connection conn=null;
		File dbPath=new File("test.db");
		try {
			Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
			conn = DriverManager.getConnection("jdbc:derby:" + dbPath.getAbsolutePath() + ";create=true");
			conn.setAutoCommit(false);
		} catch (SQLException | ClassNotFoundException e) {
			System.out.println("Error: " + e.getMessage());
			conn=null;
		}
		
		String sql="select * from courses where crn like " + CRN;
		ResultSet rs=null;
		try {
			rs=SQLWriter.executeDBCommand(conn, sql);
			rs.absolute(1);
			Course c=new Course();
			c.setCRN(Integer.parseInt(rs.getString(1)));
			c.setCourseAndSection(rs.getString(2));
			c.setTitle(rs.getString(3));
			c.setCredits(Double.parseDouble(rs.getString(4)));
			c.setType(rs.getString(5));
			c.setDays(rs.getString(6));
			c.setTime(rs.getString(7));
			c.addLocation(rs.getString(8));
			if(!rs.getString(9).contains("null")){
				c.addLocation(rs.getString(9));
			}
			c.addInstructor(rs.getString(10));
			if(!rs.getString(11).contains("null")){
				c.addInstructor(rs.getString(11));
			}
			c.setCapacity(Integer.parseInt(rs.getString(12)));
			c.setSeatsRemain(Integer.parseInt(rs.getString(13)));
			c.setSeatsFilled(Integer.parseInt(rs.getString(14)));
			c.setBeginEnd(rs.getString(15));
			return c;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	//potentially more methods for fetching course data
	
	public void addToSchedule(int CRN){
		if(user.getSchedules().isEmpty()){
			user.createSchedule("testSchedule");
		}
		Course c=getCourse(CRN);
		user.getSchedule("testSchedule").addCourse(c);
	}
}
