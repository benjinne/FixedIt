package fixedIt.controllers;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

import fixedIt.modelComponents.Course;
import fixedIt.modelComponents.Query;
import fixedIt.modelComponents.Registrar;
import fixedIt.modelComponents.Session;
import fixedIt.modelComponents.User;

public class QueryController {
	private Query query;
	private User user;
	private Session session;
	ArrayList <String> depts;
	public QueryController(){
		query= new Query();
	}
	
	public QueryController(Query query, Session session){
		this.query=query;
		this.session=session;
		this.user=session.getCurrentUser();
	}
	
	public void setQuery(Query query){
		this.query=query;
	}
	
	public Query getQuery(){
		return query;
	}
	
	public ArrayList<Course> getCourses() throws IOException{
		Registrar registrar = query.createRegistrar();
		return registrar.fetch();
	}
	
	private Course getCourse(int CRN) throws IOException{
		Course course=null;
		for(Course c : getCourses()){
			if(c.getCRN()==CRN){
				course=c;
				return course;
			}
		}
		return course;
	}
	
	public User getUser(){
		return user;
	}
	
	public boolean addToSchedule(int CRN) throws IOException{
		Course c=getCourse(CRN);
		boolean success=user.getActiveSchedule().addCourse(c);
		try {
			session.getAuth().saveExistingUserNewDataToDB(user);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return success;
	}
}