package fixedIt.controllers;

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
}
