package tests;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import Model.Course;
import Model.Registrar;

public class RegistrarTest {
	Registrar r;
	
	@Before
	public void setUp() throws Exception {
		r=new Registrar("http://ycpweb.ycp.edu/schedule-of-classes/index.html?term=201520&stype=A&dmode=D&dept=CS_12");
	}

	@Test
	public void testFetch() {
		try {
			for(Course c : r.fetch()){
				System.out.println(c.toCSVLine());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
