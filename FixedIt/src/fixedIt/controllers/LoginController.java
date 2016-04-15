package fixedIt.controllers;

import java.sql.SQLException;

import fixedIt.modelComponents.*;

public class LoginController {
	private Authenticator auth;
	
	public LoginController(){
		auth=new Authenticator();
	}
	
	public LoginController(Authenticator auth){
		this.auth=auth;
	}
	
	public void setAuth(Authenticator auth){
		this.auth=auth;
	}
	
	public Authenticator getAuth(){
		return auth;
	}
	public Session DebugMode(){
		try {
			return auth.authorizeUser("FakeUser@ycp.edu","fakeUser");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace(); 
			return null;
			
		}
		
		
		
		
		
	}
}
