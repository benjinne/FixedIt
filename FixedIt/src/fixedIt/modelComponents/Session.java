package fixedIt.modelComponents;

import java.util.ArrayList;

public class Session {
	private User user;
	private ArrayList<Query> sessionSearchHistory;
	private Authenticator auth;
	
	public Session(User user, Authenticator auth){
		this.user=user;
		sessionSearchHistory=new ArrayList<Query>();
		this.auth=auth;
	}
	
	public User getCurrentUser(){
		return user;
	}
	
	public ArrayList<Query> getSessionSearchHistory(){
		return sessionSearchHistory;
	}
	
	public void endSessionAndLogout(){
		auth.saveExistingUserNewDataToDB(user);
		user.dispose();
		sessionSearchHistory=null;
	}
}
