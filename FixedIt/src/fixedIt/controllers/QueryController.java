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
import fixedIt.modelComponents.Schedule.ConflictException;
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
		
		String sql="select * from courses where crn like '" + CRN + "'";
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
	
	public void addToSchedule(int CRN) throws ConflictException{
		if(user.getSchedules().isEmpty()){
			user.createSchedule("testSchedule");
		}
		Course c=getCourse(CRN);
		user.getSchedule("testSchedule").addCourse(c);
	}
}