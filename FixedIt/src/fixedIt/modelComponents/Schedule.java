package fixedIt.modelComponents;

import java.util.ArrayList;

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
	
	public class ConflictException extends Exception {
		private static final long serialVersionUID = 8792549347561965629L;
		public ConflictException(String message){
			super(message);
		}
	}
	
	public void addCourse(Course course) throws ConflictException {
		if(conflictsWithCourse(course)){
			throw new RuntimeException("Course conflicts with schedule.");
		}
		courses.add(course);
	}
	
	public void addCourses(Course... coursesToAdd) throws ConflictException{
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
		if(timeConflicts(course)){
			for(Course c : courses){
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
	public boolean timeConflicts(Course course){
		TimeInterval courseTime=course.getTimeAsTimeInverval();
		for(Course c : courses){
			TimeInterval cTime=c.getTimeAsTimeInverval();
			if(courseTime.getStart()==cTime.getStart() || courseTime.getEnd()==cTime.getEnd()){
				return true;
			}
			else if(courseTime.getStart()<cTime.getStart()){
				if(courseTime.getEnd()>=cTime.getStart()){
					return true;
				}
			}
			else if(cTime.getStart()<courseTime.getStart()){
				if(cTime.getEnd()>=courseTime.getStart()){
					return true;
				}
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
