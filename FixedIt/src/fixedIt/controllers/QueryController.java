package fixedIt.controllers;

import fixedIt.modelComponents.Query;
//HAVE TO BE ABLE TO GRAB VARIABLES FROM JSP

public class QueryController {
	private Query query;
	
	public QueryController(){
	}
	
	public QueryController(Query query){
		this.query=query;
	}
	
	public void setAuth(Query query){
		this.query=query;
	}
	
	public Query getQuery(){
		return query;
	}
}
