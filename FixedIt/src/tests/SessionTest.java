package tests;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import fixedIt.modelComponents.Authenticator;
import fixedIt.modelComponents.User;

public class SessionTest {
	User user;
	Authenticator auth;
	
	@Before
	public void setUp() throws Exception {
		auth=new Authenticator();
		user=new User("dwayne@theRockJohnson.com", "xpf109", 0,  auth);
	}

	@Test
	public void testEndSessionAndLogout(){
		//test if user elements are in database;
		user.dispose();
		assertEquals(user.getEmailAddress(), null);
		assertEquals(user.getSchedules(), null);
		assertEquals(user.getPasswordHash(), null);
	}
}
