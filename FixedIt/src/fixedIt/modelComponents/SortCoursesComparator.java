package fixedIt.modelComponents;

import java.util.Comparator;

public class SortCoursesComparator implements Comparator<Course>{
	public SortCoursesComparator(){
		
	}

	@Override
	public int compare(Course c1, Course c2) {
		Integer c1Start=Integer.parseInt(c1.getTime().substring(0, c1.getTime().indexOf(':')));
		if(c1.getTime().substring(0, c1.getTime().indexOf('-')).toLowerCase().contains("pm") && !c1.getTime().substring(0, c1.getTime().indexOf('-')).contains("12")){
			c1Start+=12;
		}
		Integer c2Start=Integer.parseInt(c2.getTime().substring(0, c2.getTime().indexOf(':')));
		if(c2.getTime().substring(0, c2.getTime().indexOf('-')).toLowerCase().contains("pm") && !c2.getTime().substring(0, c2.getTime().indexOf('-')).contains("12")){
			c2Start+=12;
		}
		int hrResult=c1Start.compareTo(c2Start);
		if(hrResult!=0){
			return hrResult;
		} else{
			Integer c1Min=Integer.parseInt(c1.getTime().substring(c1.getTime().indexOf(':')+1, c1.getTime().indexOf(':')+3));
			Integer c2Min=Integer.parseInt(c2.getTime().substring(c2.getTime().indexOf(':')+1, c2.getTime().indexOf(':')+3));
			return c1Min.compareTo(c2Min);
		}
	}

}
