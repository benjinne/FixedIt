package tests;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import fixedIt.controllers.QueryController;
import fixedIt.modelComponents.Authenticator;
import fixedIt.modelComponents.Course;
import fixedIt.modelComponents.Query;

public class AuthenticatorTest {
	private Authenticator a;
	private String validPass;
	private String validPass2;
	private String invalidPass;
	private QueryController q;
	//private String search1;
	//private String search2;
	
	
	
	@Before
	public void setUp(){
		validPass="ThisIsAPassword!-._";
		invalidPass="ThisIsAnInvalidPass/?@#";
		validPass2="ThisIsAlsoAPassword!!!---...___";
		a=new Authenticator();
	}
	
	@Test
	public void testRequestPasswordReset(){
		a.requestPasswordReset("cs320fixedit@mailinator.com");
	}

	@Test
	public void testIsValidPassword() {
		
		assertTrue(a.isValidPassword(validPass));
		assertFalse(a.isValidPassword(invalidPass));
	}

	@Test
	public void testIsValidEmailAddress() {
		String validEmail1="email@domain.com";
		String validEmail2="johnny.appleseed@dadjokes.net";
		String invalidEmail1="superFakeEmailAddress";
		String invalidEmail2="Screw email conventions!";
		
		assertTrue(a.isValidEmailAddress(validEmail1));
		assertTrue(a.isValidEmailAddress(validEmail2));
		assertFalse(a.isValidEmailAddress(invalidEmail1));
		assertFalse(a.isValidEmailAddress(invalidEmail2));
	}

	@Test
	public void testSaltHashPassword() {
		String hash1=a.saltHashPassword(validPass);
		String hash2=a.saltHashPassword(validPass2);
		
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
	
		assertTrue(a.credentialsMatch(validPass, hash1));
		assertTrue(a.credentialsMatch(validPass2, hash2));
	}
	@Test
	public void testQueryController(){
		String search1="ANT201.106";
		String search2="PE102.50";
		String search3="PE125.802";
		
		
		assertTrue(q.getQuery().equals(search1));
		assertTrue(q.getQuery().equals(search3));
		assertFalse(q.getQuery().equals(search2));
		
		
	}

	
}
