package Model;

import java.util.ArrayList;
import java.util.TreeMap;

public class FakeDatabase {
	private ArrayList<User> users;
	
	public FakeDatabase(){
		users=new ArrayList<User>();
	}
	
	public FakeDatabase(ArrayList<User> initializeWithUsers){
		
	}
	
	public TreeMap<String, Schedule> getUserSchedules(User usr){
		System.out.println(this.getClass() + ": A method is not implemented yet");
		return null;
	}
	
	public int getStudentStatus(User usr){
		System.out.println(this.getClass() + ": A method is not implemented yet");
		return 0;
	}
	
	public void deleteUser(User usr){
		System.out.println(this.getClass() + ": A method is not implemented yet");
	}
	
	public void setPassword(String pw){
		System.out.println(this.getClass() + ": A method is not implemented yet");
	}

}
