package Model;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.TreeMap;

public class User {
	public static final int STATUS_FULL_TIME=0;
	public static final int STATUS_PART_TIME=1;
	
	private String emailAddress;
	private TreeMap<String, Schedule> schedules;
	private int studentStatus, numSchedules;
	private Query currentQuery;
	private FakeDatabase db;
	
	public User(String emailAddress, FakeDatabase db){
		this.emailAddress=emailAddress;
		this.schedules=lookupSchedules();
		this.studentStatus=lookupStudentStatus();
		this.numSchedules=schedules.size();
		this.db=db;
	}
	
	private TreeMap<String, Schedule> lookupSchedules(){
		return db.getUserSchedules(this);
	}
	
	private int lookupStudentStatus(){
		return db.getStudentStatus(this);
	}
	
	public void dispose(){
		emailAddress=null;
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
		db.deleteUser(this);
	}
	
	public Query newQuery(int term, String level, String dept){
		return new Query(term, level, dept);
	}
	
	/**
	 * Download a schedule
	 * @param schedule the schedule to download
	 * @param filepath the path in which to save the file (not including the filename itself)
	 * @return File a file for download
	 */
	public void downloadSchedule(Schedule schedule, String filepath) throws IOException{
		File file=new File(filepath+schedule.getName().replaceAll("[^a-zA-Z0-9.-]", "_")+".csv");
		if(!file.exists()){
			file.createNewFile();
		}
		System.out.println(file.getAbsolutePath());
		FileWriter fw=new FileWriter(file.getAbsoluteFile());
		BufferedWriter bw=new BufferedWriter(fw);
		bw.write("CRN, Course and Section, Title, Credits, Type, Days, Time, Locations, Instructors, " +
		"Capacity, Seats Open, Seats Filled, Begin/End Dates");
		bw.newLine();
		for(Course c : schedule.getCourses()){
			bw.write(c.toCSVLine());
			bw.newLine();
		}
		bw.close();
		fw.close();
	}
	
	public Schedule getSchedule(String key){
		return schedules.get(key);
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
}
