package tests;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import Model.Authenticator;

public class EmailSenderTest {
	Authenticator a;
	@Before
	public void setUp() throws Exception {
		a=new Authenticator();
	}

	@Test
	public void testSendMail() {
		assertTrue(a.sendMail("mjones44@ycp.edu", ""));
	}
}
