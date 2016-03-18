package fixedIt.controllers;


import fixedIt.modelComponents.Session;
import fixedIt.modelComponents.User;
//HAVE TO BE ABLE TO GRAB VARIABLES FROM JSP

public class UserInfoController {
	private User user;
	private Session session;
	
	public UserInfoController(){
		user= new User();
		session= new Session(user, null);
	}
	
	public UserInfoController(User user){
		this.user=user;
	}
	
	public User getUser(){
		this.user= session.getCurrentUser();
		return user;
	}
	
	public void setSession(Session session){
		this.session = session;
	}
}
