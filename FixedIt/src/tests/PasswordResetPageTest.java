package tests;

import static org.junit.Assert.*;

import java.util.Calendar;

import org.junit.Before;
import org.junit.Test;

import fixedIt.modelComponents.Authenticator;
import fixedIt.modelComponents.FakeDatabase;
import fixedIt.modelComponents.PasswordResetPage;

public class PasswordResetPageTest {
	private PasswordResetPage pwReset;
	private Authenticator auth;
	private FakeDatabase db;

	@Before
	public void setUp() throws Exception {
		db=new FakeDatabase();
		auth=new Authenticator(db);
		Calendar cal=Calendar.getInstance();
		cal.add(Calendar.DATE, -4);
		pwReset=new PasswordResetPage(auth, "cs320fixedit@mailinator.com", cal);
	}

	@Test
	public void testResetPassword() {
		assertFalse(pwReset.resetPassword("password"));
		Calendar cal=Calendar.getInstance();
		cal.add(Calendar.DATE, 4);
		pwReset=new PasswordResetPage(auth, "cs320fixedit@mailinator.com", cal);
		assertTrue(pwReset.resetPassword("password"));
	}

}
