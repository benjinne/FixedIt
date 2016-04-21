package tests;

import static org.junit.Assert.*;

import java.sql.SQLException;
import java.util.Calendar;
import java.util.UUID;

import org.junit.Before;
import org.junit.Test;

import fixedIt.modelComponents.Authenticator;
import fixedIt.modelComponents.PasswordResetPage;

public class PasswordResetPageTest {
	private PasswordResetPage pwReset;
	private Authenticator auth;

	@Before
	public void setUp() throws Exception {
		auth=new Authenticator();
		Calendar cal=Calendar.getInstance();
		cal.add(Calendar.DATE, -4);
		pwReset=new PasswordResetPage(auth, "cs320fixedit@mailinator.com", cal, "localhost:8081/FixedIt/passwordReset/", UUID.randomUUID());
	}

	@Test
	public void testResetPassword() {
		try {
			assertFalse(pwReset.resetPassword("password"));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Calendar cal=Calendar.getInstance();
		cal.add(Calendar.DATE, 4);
		pwReset=new PasswordResetPage(auth, "cs320fixedit@mailinator.com", cal, "localhost:8081/FixedIt/passwordReset/", UUID.randomUUID());
		try {
			assertTrue(pwReset.resetPassword("password"));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
