package tests;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import fixedIt.modelComponents.Course;
import fixedIt.modelComponents.Registrar;
import fixedIt.modelComponents.Schedule;

public class ScheduleTest {
	private Schedule s;
	private Course base, conflicting1, conflicting2, noconflict1, noconflict2;
	private Registrar r;

	@Before
	public void setUp() throws Exception {
		s=new Schedule("s");
		conflicting2=new Course();
		
		r=new Registrar("http://ycpweb.ycp.edu/schedule-of-classes/index.html?term=201520&stype=A&dmode=D&dept=CS_12");
		ArrayList<Course> o=r.fetch();
		base=o.get(2);
		conflicting1=o.get(3);
		conflicting2.setTime("12:00PM-12:50PM");
		conflicting2.setDays("TR");
		noconflict1=o.get(1);
		noconflict2=o.get(8);
		s.addCourses(base, noconflict1);
	}

	@Test
	public void testGetCredits() {
		assertEquals(6, s.getCredits());
	}
	
	@Test
	public void testConflictsWithCourse() {
		assertFalse(s.conflictsWithCourse(noconflict2));
		assertTrue(s.conflictsWithCourse(conflicting1));
		assertTrue(s.conflictsWithCourse(conflicting2));
	}
}
