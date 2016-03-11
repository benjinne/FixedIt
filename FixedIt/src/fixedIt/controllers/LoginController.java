package fixedIt.controllers;

import fixedIt.modelComponents.*;

public class LoginController {
	private String emailAddress, password;
	private Authenticator auth;
	FakeDatabase db;
	
	public LoginController(String emailAddress, String password){
		this.emailAddress=emailAddress;
		this.password=password;
		db=new FakeDatabase();
		auth=new Authenticator(db);
	}
	
	public Authenticator getAuth(){
		return auth;
	}
}
