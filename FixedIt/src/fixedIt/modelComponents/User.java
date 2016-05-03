package fixedIt.modelComponents;

import java.sql.SQLException;
import java.util.TreeMap;

public class User{
	public static final int STATUS_FULL_TIME=0;
	public static final int STATUS_PART_TIME=1;
	
	private String emailAddress, passwordHash;
	private TreeMap<String, Schedule> schedules;
	private int studentStatus, numSchedules;
	private Query currentQuery;
	private Authenticator auth;
	private Schedule activeSchedule;

	public User(){
		emailAddress=null;
		passwordHash=null;
		studentStatus=0;
		numSchedules=0;
		auth=null;
		schedules=new TreeMap<String, Schedule>();
	}
	
	public User(String emailAddress, String passwordHash, int studentStatus, int numSchedules, Authenticator auth){
		this.emailAddress=emailAddress;
		this.passwordHash=passwordHash;
		this.studentStatus=studentStatus;
		this.numSchedules=numSchedules;
		this.auth=auth;
		schedules=new TreeMap<String, Schedule>();
	}
	
	public void reInitializeUser() throws SQLException{
		User tmp=auth.getUser(emailAddress);
		emailAddress=tmp.getEmailAddress();
		schedules=tmp.getSchedules();
		studentStatus=tmp.getStudentStatus();
		numSchedules=tmp.getNumSchedules();
		tmp.dispose();
	}
	
	public void dispose(){
		emailAddress=null;
		passwordHash=null;
		schedules=null;
		currentQuery=null;
	}
	public void createSchedule(String name){
		if(numSchedules<5){
			schedules.put(name, new Schedule(name));
			numSchedules++;
		}
	}
	
	public void deleteAccount(){
		auth.deleteUser(this);
	}
	
	public Query newQuery(int term, String level, String dept){
		return new Query(term, level, dept);
	}
	
	/*
	 * Download a schedule
	 * @param schedule the schedule to download
	 * @param filepath the path in which to save the file (not including the filename itself)
	 * @return File a file for download
	 */
	public String getScheduleAsCSV(Schedule schedule){
		String csv="CRN, Course and Section, Title, Credits, Type, Days, Time, Locations, Instructors, " +
		"Capacity, Seats Open, Seats Filled, Begin/End Dates \n";
		for(Course c : schedule.getCourses()){
			csv=csv + c.getCRN() + ", " + c.getCourseAndSection() + ", " + c.getTitle() +
					", " + c.getCredits() + ", " + c.getType() + ", " + c.getDays() +
					", " + c.getTime() + ", " + c.getLocation().get(0);
			if(c.getLocation().size()>1){
				csv=csv + " and " + c.getLocation().get(1) + ", ";
			}
			csv=csv + c.getInstructors().get(0);
			if(c.getInstructors().size()>1){
				csv=csv + " and " + c.getInstructors().get(1) + ", ";
			}
			csv=csv + c.getCapacity() + ", " + c.getSeatsRemain() + ", " + c.getSeatsFilled() + ", " +
					c.getBeginEnd() + "\n";
		}
		return csv;
	}
	
	public Schedule getSchedule(String key){
		return schedules.get(key);
	}
	public void setAuth(Authenticator auth){
		this.auth=auth;
	}
	public int getNumSchedules(){
		return numSchedules;
	}
	public String getEmailAddress() {
		return emailAddress;
	}
	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}
	public TreeMap<String, Schedule> getSchedules() {
		return schedules;
	}
	public void setSchedules(TreeMap<String, Schedule> schedules) {
		this.schedules = schedules;
	}
	public int getStudentStatus() {
		return studentStatus;
	}
	public void setStudentStatus(int studentStatus) {
		this.studentStatus = studentStatus;
	}
	public Query getCurrentQuery() {
		return currentQuery;
	}
	public void setCurrentQuery(Query currentQuery) {
		this.currentQuery = currentQuery;
	}
	public String getPasswordHash(){
		return passwordHash;
	}
	public void setPasswordHash(String newHash){
		passwordHash=newHash;
	}
	public Schedule getActiveSchedule() {
		return activeSchedule;
	}
	public void setActiveSchedule(Schedule activeSchedule) {
		this.activeSchedule = activeSchedule;
	}
}
