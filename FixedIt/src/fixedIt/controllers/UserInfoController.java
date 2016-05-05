package fixedIt.controllers;

import fixedIt.modelComponents.Session;
import fixedIt.modelComponents.User;

public class UserInfoController {
	private User user;
	private Session session;
	
	public UserInfoController(Session session){
		this.session=session; 
		this.user=this.session.getCurrentUser();
	}
	
	public boolean isSessionNull(){
		return session==null;
	}
	
	public User getUser(){
		return user;
	}
	
	public void setUser(User user){
		this.user=user;
	}
	
	public void setSession(Session session){
		this.session = session;
		this.user=this.session.getCurrentUser();
	}
}
