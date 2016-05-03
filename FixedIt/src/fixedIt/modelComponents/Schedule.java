package fixedIt.modelComponents;

import java.util.ArrayList;

import org.joda.time.DateTime;
import org.joda.time.Interval;
import org.joda.time.LocalTime;
import org.joda.time.format.DateTimeFormat;

public class Schedule {
	
	public ArrayList<Course> courses;
	public String name;
	//Set to one of the Query.Session constants
	public String term;
	//Set to one of the Query.Level constants
	public String level;
	
	public Schedule(String name, ArrayList<Course> courses, String term, String level){
		this.name=name;
		this.courses=courses;
		this.term=term;
		this.level=level;
	}
	
	public Schedule(String name){
		this.name=name;
		courses=new ArrayList<Course>();
	}
	
	public Schedule(Schedule s){
		this.name="" + s.getName();
		this.courses=new ArrayList<Course>();
		this.courses.addAll(s.getCourses());
		this.term="" + s.getTerm();
		this.level="" + s.getLevel();
	}
	
	
	public boolean addCourse(Course course)  {
		if(conflictsWithCourse(course)){
			return false;
		}
		courses.add(course);
		return true;
	}
	
	public void addCourses(Course... coursesToAdd) {
		for(Course c : coursesToAdd){
			addCourse(c);
		}
	}
	
	public void deleteCourse(int CRN){
		for(int i=0; i<courses.size(); i++){
			if(courses.get(i).getCRN()==CRN){
				courses.remove(i);
			}
		}
	}
	
	public int getCredits(){
		int credits=0;
		for(Course c : courses){
			credits+=c.getCredits();
		}
		return credits;
	}
	
	/**
	 * Checks whether a course has a time/day conflict
	 * with any courses currently on the schedule
	 * @param course the course for which to check for conflicts
	 * @return boolean
	 */
	public boolean conflictsWithCourse(Course course){
		for(Course c : courses){
			if(timeConflicts(course, c)){
				char[] cDays=c.getDays().toCharArray();
				for(int i=0; i<cDays.length; i++){
					if(Character.isLetter(cDays[i])){
						if(course.getDays().contains(Character.toString(cDays[i]))){
							return true;
						}
					}
				}
			}
		}
		return false;
	}
	
	/**
	 * checks strictly for time conflicts; DOES NOT CHECK FOR DAY CONFLICTS
	 * @param course the course for which to check for conflicts
	 * @return boolean
	 */
	public boolean timeConflicts(Course course, Course c){
		String pattern="hh:mmaa";
		DateTime courseStart=DateTime.parse(course.getTime().substring(0, course.getTime().indexOf('-')), DateTimeFormat.forPattern(pattern));
		DateTime courseEnd=DateTime.parse(course.getTime().substring(course.getTime().indexOf('-')+1), DateTimeFormat.forPattern(pattern));
		Interval courseInterval=new Interval(courseStart, courseEnd);
		
		DateTime cStart=DateTime.parse(c.getTime().substring(0, c.getTime().indexOf('-')), DateTimeFormat.forPattern(pattern));
		DateTime cEnd=DateTime.parse(c.getTime().substring(c.getTime().indexOf('-')+1), DateTimeFormat.forPattern(pattern));		
		Interval cInterval=new Interval(cStart, cEnd);
		
		if(courseInterval.overlaps(cInterval)){
			return true;
		}
		return false;
	}
	
	public static boolean timeConflict(Course c, Course c2){
		String t=c2.getTime().substring(0, c2.getTime().indexOf(':')+3);
		LocalTime courseStart=new LocalTime(Integer.parseInt(t.substring(0, t.indexOf(':'))), Integer.parseInt(t.substring(t.indexOf(':')+1)));
		t=c2.getTime().substring(c2.getTime().indexOf('-')+1);
		t=t.substring(0, t.indexOf(':')+3);
		LocalTime courseEnd=new LocalTime(Integer.parseInt(t.substring(0, t.indexOf(':'))), Integer.parseInt(t.substring(t.indexOf(':')+1)));
		
			t=c.getTime().substring(0, c.getTime().indexOf(':')+3);
			LocalTime cStart=new LocalTime(Integer.parseInt(t.substring(0, t.indexOf(':'))), Integer.parseInt(t.substring(t.indexOf(':')+1)));
			t=c.getTime().substring(c.getTime().indexOf('-')+1);
			t=t.substring(0, t.indexOf(':')+3);
			LocalTime cEnd=new LocalTime(Integer.parseInt(t.substring(0, t.indexOf(':'))), Integer.parseInt(t.substring(t.indexOf(':')+1)));
			
			if(courseStart.equals(cStart) || courseEnd.equals(cEnd)){
				return true;
			}else if(courseStart.isBefore(cStart)){
				if(courseEnd.isAfter(cStart)){
					return true;
				}
			}
			else if(cStart.isBefore(courseStart)){
				if(cEnd.isAfter(courseStart)){
					return true;
				}
			}
		return false;
	}
	
	public String getTerm(){
		return term;
	}
	/**
	 * Use one the Query constants
	 * @param term one of the Query term constants
	 */
	public void setTerm(String term){
		this.term=term;
	}
	public String getLevel(){
		return level;
	}
	/**
	 * Use one of the Query constants here
	 * @param level one of the Query level constants
	 */
	public void setLevel(String level){
		this.level=level;
	}
	
	public ArrayList<Course> getCourses() {
		return courses;
	}
	public void setCourses(ArrayList<Course> courses) {
		this.courses = courses;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
}
