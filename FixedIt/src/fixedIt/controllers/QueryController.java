package fixedIt.controllers;

import java.util.ArrayList;

import fixedIt.modelComponents.Course;
import fixedIt.modelComponents.Query;
import fixedIt.modelComponents.Registrar;
//HAVE TO BE ABLE TO GRAB VARIABLES FROM JSP

public class QueryController {
	private Query query;
	
	public QueryController(){
		query= new Query();
	}
	
	public QueryController(Query query){
		this.query=query;
	}
	
	public void setQuery(Query query){
		this.query=query;
	}
	
	public Query getQuery(){
		return query;
	}
	
	public ArrayList<Course> getCourses(){
		Registrar registrar = query.createRegistrar();
		return registrar.fetch();
	}
	
	//potentially more methods for fetching course data
}
