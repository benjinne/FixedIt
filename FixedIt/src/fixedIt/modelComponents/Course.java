package fixedIt.modelComponents;

import java.util.ArrayList;

public class Course {
	private int CRN, capacity, seatsRemain, seatsFilled;
	private double credits;
	private String courseAndSection, title, type, days, time, beginEnd;
	private ArrayList<String> instructors, locations;
	
	public Course(int CRN, String courseAndSection, String title, double credits, String type, String days, 
			String time, String location, String instructor, int capacity, int seatsRemain, 
			int seatsFilled, String beginEnd){
		this.CRN=CRN;
		this.credits=credits;
		this.capacity=capacity;
		this.seatsRemain=seatsRemain;
		this.seatsFilled=seatsFilled;
		this.courseAndSection=courseAndSection;
		this.title=title;
		this.type=type;
		this.days=days.replaceAll(" ", "");
		this.time=time;
		instructors=new ArrayList<String>();
		instructors.add(instructor);
		locations=new ArrayList<String>();
		this.locations.add(location);
		this.beginEnd=beginEnd;
	}
	
	public Course(){
		locations=new ArrayList<String>();
		instructors=new ArrayList<String>();
	}
	
	@Override
	public String toString() {
		return "Course [CRN=" + CRN + ", courseAndSection=" + courseAndSection + ", title=" + title + ", credits="
				+ credits + ", type=" + type + ", days=" + days + ", time=" + time + ", locations=" + locations
				+ ", instructors=" + instructors + ", capacity=" + capacity + ", seatsRemain=" + seatsRemain
				+ ", seatsFilled=" + seatsFilled + ", beginEnd=" + beginEnd + "]";
	}
	
	/**
	 * Populates a String with this Course's data in CSV format
	 * for writing to a CSV file
	 * @return String in CSV format of the Course's data
	 */
	public String toCSVLine(){
		return CRN + ", " + courseAndSection + ", " + title + ", " + credits + ", " + type + ", " + days + ", " +
				time + ", " + locations + ", " + instructors + ", " + capacity + ", " + seatsRemain
				+ ", " + seatsFilled + ", " + beginEnd;
	}
	
	/**
	 * Calculate the time interval of this course,
	 * expressed as the number of minutes away
	 * from midnight (for easy overlap checking)
	 * @return TimeInterval the TimeInterval of this class
	 */
	public TimeInterval getTimeAsTimeInverval(){
		String startAsString=this.time.split("-")[0];
		String endAsString=this.time.split("-")[1];
		boolean startIsPm=(startAsString.contains("PM") || startAsString.contains("pm"));
		boolean endIsPm=(endAsString.contains("PM") || endAsString.contains("pm"));
		int start, end;
		if(startIsPm && Integer.parseInt(startAsString.substring(0, 2))>12){
			start=((Integer.parseInt(startAsString.substring(0, 2))+12)*60)+Integer.parseInt(startAsString.substring(3, 5));
		}
		else{
			start=(Integer.parseInt(startAsString.substring(0, 2))*60)+Integer.parseInt(startAsString.substring(3, 5));
		}
		if(endIsPm && Integer.parseInt(endAsString.substring(0, 2))>12){
			end=((Integer.parseInt(endAsString.substring(0, 2))+12)*60)+Integer.parseInt(endAsString.substring(3, 5));
		}
		else{
			end=(Integer.parseInt(endAsString.substring(0, 2))*60)+Integer.parseInt(endAsString.substring(3, 5));
		}
		return new TimeInterval(start, end);
	}
	
	public boolean startsBefore (Course other){
		if(Integer.parseInt(time.substring(0, time.indexOf(':')))<Integer.parseInt(other.getTime().substring(0, other.getTime().indexOf(':')))){
			return true;
		} else{
			String timeMin=time.substring(time.indexOf(':')+1);
			timeMin=timeMin.substring(0, 2);
			String otherMin=other.getTime().substring(other.getTime().indexOf(':')+1);
			otherMin=otherMin.substring(0, 2);
			if(Integer.parseInt(timeMin)<Integer.parseInt(otherMin)){
				return true;
			} else{
				return false;
			}
		}
	}
	
	public void addInstructor(String instructor){
		instructors.add(instructor);
	}
	public void addLocation(String location){
		locations.add(location);
	}
	public String getBeginEnd() {
		return beginEnd;
	}
	public void setBeginEnd(String beginEnd) {
		this.beginEnd = beginEnd;
	}
	public ArrayList<String> getInstructors() {
		return instructors;
	}
	public void setInstructors(ArrayList<String> instructors) {
		this.instructors = instructors;
	}
	public int getCRN() {
		return CRN;
	}
	public void setCRN(int CRN) {
		this.CRN = CRN;
	}
	public double getCredits() {
		return credits;
	}
	public void setCredits(double d) {
		this.credits = d;
	}
	public int getCapacity() {
		return capacity;
	}
	public void setCapacity(int capacity) {
		this.capacity = capacity;
	}
	public int getSeatsRemain() {
		return seatsRemain;
	}
	public void setSeatsRemain(int seatsRemain) {
		this.seatsRemain = seatsRemain;
	}
	public int getSeatsFilled() {
		return seatsFilled;
	}
	public void setSeatsFilled(int seatsFilled) {
		this.seatsFilled = seatsFilled;
	}
	public String getCourseAndSection() {
		return courseAndSection;
	}
	public void setCourseAndSection(String courseAndSection) {
		this.courseAndSection = courseAndSection;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public ArrayList<String> getLocation() {
		return locations;
	}
	public void setLocation(ArrayList<String> locations) {
		this.locations = locations;
	}
	public String getDays() {
		return days;
	}
	public void setDays(String days) {
		this.days = days.replaceAll(" ", "");
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
}
