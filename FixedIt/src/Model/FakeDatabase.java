package Model;

import java.util.TreeMap;

public class FakeDatabase {
	private TreeMap<String, String> usernamePasswordPairings;
	private TreeMap<String, User> users;
	
	public FakeDatabase(){
		users=new TreeMap<String, User>();
		usernamePasswordPairings=new TreeMap<String, String>();
	}
	
	public FakeDatabase(TreeMap<String, User> users){
		this.users=users;
	}
	
	public User getUserByEmailAddress(String emailAddress){
		return users.get(emailAddress);
	}
	
	public void setPasswordForUser(String emailAddress, String password){
		usernamePasswordPairings.put(emailAddress, password);
	}
	
	public void deleteUser(String emailAddress){
		users.remove(emailAddress);
	}
}
