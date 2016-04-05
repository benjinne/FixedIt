package tests;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import fixedIt.modelComponents.Authenticator;
import fixedIt.modelComponents.Course;
import fixedIt.modelComponents.Query;
import fixedIt.modelComponents.User;

public class UserTest {
	User user;
	Authenticator auth;
	
	@Before
	public void setUp() throws Exception {
		auth=new Authenticator();
		user=new User("dwayne@theRockJohnson.com", "xpf109", 0, 0,  auth);
		ArrayList<Course> cs=user.newQuery(Query.SPRING_2016, Query.LEVEL_UNDERGRAD, Query.CS_12).createRegistrar().fetch();
		user.createSchedule("Dwayne \"The Rock\" Johnson's Schedule");
		user.getSchedule("Dwayne \"The Rock\" Johnson's Schedule").addCourses(cs.get(0), cs.get(1), cs.get(2), cs.get(4));
	}

	@Test
	public void testDispose() {
		user.dispose();
		assertEquals(user.getCurrentQuery(), null);
		assertEquals(user.getEmailAddress(), null);
		assertEquals(user.getSchedules(), null);
	}

	@Test
	public void testCreateSchedule() {
		assertEquals(1, user.getNumSchedules());
		user.createSchedule("New Test Schedule");
		assertEquals(2, user.getNumSchedules());
	}

	@Test
	public void testDeleteAccount() {
		fail("Not yet implemented");
	}

	@Test
	public void testNewQuery() {
		Query q=null;
		q=user.newQuery(201520, Query.LEVEL_UNDERGRAD, Query.CS_12);
		assertTrue(q!=null);
	}

	@Test
	public void testDownloadSchedule() {
		//not implemented yet
	}

}
