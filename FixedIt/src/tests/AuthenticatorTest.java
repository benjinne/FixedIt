package tests;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import Model.Authenticator;

public class AuthenticatorTest {
	private Authenticator a;
	private String validPass;
	private String validPass2;
	private String invalidPass;
	
	@Before
	public void setUp() throws Exception {
		validPass="ThisIsAPassword!-._";
		invalidPass="ThisIsAnInvalidPass/?@#";
		validPass2="ThisIsAlsoAPassword!!!---...___";
		a=new Authenticator();
	}

	@Test
	public void testSendMail() {
		fail("Not yet implemented");
	}

	@Test
	public void testSendMailWithAttachment() {
		fail("Not yet implemented");
	}

	@Test
	public void testValidateNewUser() {
		fail("Not yet implemented");
	}

	@Test
	public void testAddNewUserToDB() {
		fail("Not yet implemented");
	}

	@Test
	public void testIsValidPassword() {
		
		assertTrue(a.isValidPassword(validPass));
		assertFalse(a.isValidPassword(invalidPass));
	}

	@Test
	public void testIsValidEmailAddress() {
		fail("Not yet implemented");
	}

	@Test
	public void testSaltHashPassword() {
		String hash1=a.saltHashPassword(validPass);
		String hash2=a.saltHashPassword(validPass2);
		
		System.out.println(validPass + " : " + hash1 + "\n" + validPass2 + " : " + hash2);
		
		assertFalse(validPass.equals(hash1));
		assertFalse(validPass.length()==hash1.length());
		assertFalse(validPass2.equals(hash2));
		assertFalse(validPass2.length()==hash2.length());
		assertFalse(hash1.equals(hash2));
	}
	
	@Test
	public void testValidatePassword(){
		String hash1=a.saltHashPassword(validPass);
		String hash2=a.saltHashPassword(validPass2);
		
		assertTrue(a.validatePassword(validPass, hash1));
		assertTrue(a.validatePassword(validPass2, hash2));
	}
}
