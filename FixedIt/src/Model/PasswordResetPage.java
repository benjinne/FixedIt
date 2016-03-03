package Model;

import java.util.Calendar;

public class PasswordResetPage extends EmailSender {
	private String emailAddress;
	private Calendar expirationDate;
	private Authenticator auth;
	private String url;
	private FakeDatabase db;
	
	public PasswordResetPage(Authenticator auth, String emailAddress, Calendar expirationDate, FakeDatabase db){
		this.emailAddress=emailAddress;
		this.expirationDate=expirationDate;
		this.auth=auth;
		this.db=db;
		generateAndSetURL();
	}
	
	public void resetPassword(String password){
		if(!expirationDate.after(Calendar.getInstance())){
			db.setPassword(password);
		}
		else{
			throw new RuntimeException("This passwordResetPage is expired!");
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
