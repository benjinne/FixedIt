package fixedIt.modelComponents;

import java.sql.SQLException;
import java.util.Calendar;
import java.util.UUID;

public class PasswordResetPage implements EmailSender {
	private String emailAddress;
	private Calendar expirationDate;
	private Authenticator auth;
	private String url;
	private UUID uuid;
	private String webContext;
	
	public PasswordResetPage(Authenticator auth, String emailAddress, Calendar expirationDate, String webContext, UUID uuid){
		this.emailAddress=emailAddress;
		this.expirationDate=expirationDate;
		this.auth=auth;
		this.webContext=webContext;
		this.uuid=uuid;
	}
	
	public boolean resetPassword(String password) throws SQLException{
		if(!isExpired()){
			auth.setPasswordForUser(emailAddress, password);
			return true;
		}
		else{
			System.out.println("This PasswordResetPage has expired!");
			return false;
		}
	}
	
	public boolean isExpired(){
		if(expirationDate.after(Calendar.getInstance())){
			return false;
		} else{
			return true;
		}
	}
	
	public void renew(){
		auth.requestPasswordReset(emailAddress, webContext, uuid);
	}
	
	public String buildEmail(String firstHalf, String secondHalf){
		return firstHalf + "<h2><u><font color=\"blue\"><a href=\"" + url + "\">Reset Password</a></font></u></h2>" 
				+ secondHalf;
	}
	
	public void generateAndSetURL(){
		this.url=webContext;
		if(!url.endsWith("/")){
			url=url + "?uuid=" + uuid + "&emailAddress=" + emailAddress;
		} else{
			url=url.substring(0, url.length()-2) + "?uuid=" +  uuid + "&emailAddress=" + emailAddress;
		}
		System.out.println(url);
	}
}
