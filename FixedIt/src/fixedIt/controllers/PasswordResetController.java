package fixedIt.controllers;

import java.sql.SQLException;
import java.util.UUID;

import fixedIt.modelComponents.Authenticator;

public class PasswordResetController {
	Authenticator auth;
	String emailAddress;
	
	public PasswordResetController(String emailAddress){
		this.auth=new Authenticator();
		this.emailAddress=emailAddress;
		
	}
	
	public void requestPasswordReset(String email, String webContext, UUID uuid){
		auth.requestPasswordReset(email, webContext, uuid);
	}
	
	public boolean resetPassword(String sessionUUID, String urlUUID, String newPassword){
		if(sessionUUID.equals(urlUUID)){
			try {
				return auth.setPasswordForUser(emailAddress, newPassword);
			} catch (SQLException e) {
				e.printStackTrace();
				return false;
			}
		} else{
			return false;
		}
	}
	
	public boolean userExists(){
		return auth.userExists(emailAddress);
	}
}
