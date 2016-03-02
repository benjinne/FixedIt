package Model;

import java.util.Calendar;

public class PasswordResetPage extends EmailSender {
	private String emailAddress;
	private Calendar expirationDate;
	private Authenticator a;
	private String url;
	private FakeDatabase db;
	
	public PasswordResetPage(Authenticator a, String emailAddress, Calendar expirationDate, FakeDatabase db){
		this.emailAddress=emailAddress;
		this.expirationDate=expirationDate;
		this.a=a;
		this.db=db;
		generateAndSetURL();
	}
	
	public void resetPassword(String password){
		db.setPassword(password);
	}
	
	public void renew(){
		a.requestPasswordReset(emailAddress);
	}
	
	public String buildEmail(String firstHalf, String secondHalf){
		return firstHalf + "<h2><u><font color=\"blue\"><a href=\"" + url + "\">Reset Password</a></font></u></h2>" 
				+ secondHalf;
	}
	
	private void generateAndSetURL(){
		//not implemented yet
	}
}
