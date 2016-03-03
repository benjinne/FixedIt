package Model;

import java.util.Calendar;

public class PasswordResetPage extends EmailSender {
	private String emailAddress;
	private Calendar expirationDate;
	private Authenticator auth;
	private String url;
	
	public PasswordResetPage(Authenticator auth, String emailAddress, Calendar expirationDate){
		this.emailAddress=emailAddress;
		this.expirationDate=expirationDate;
		this.auth=auth;
		generateAndSetURL();
	}
	
	public boolean resetPassword(String password){
		if(expirationDate.after(Calendar.getInstance())){
			auth.setPasswordForUser(emailAddress, password);
			return true;
		}
		else{
			System.out.println("This PasswordResetPage has expired!");
			return false;
		}
	}
	
	public void renew(){
		auth.requestPasswordReset(emailAddress);
	}
	
	public String buildEmail(String firstHalf, String secondHalf){
		return firstHalf + "<h2><u><font color=\"blue\"><a href=\"" + url + "\">Reset Password</a></font></u></h2>" 
				+ secondHalf;
	}
	
	private void generateAndSetURL(){
		//not implemented yet
	}
}
