package fixedIt.modelComponents;

import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

import fixedIt.sql.database.*;

public class Authenticator implements EmailSender {
	public static final String ALLOWED_CHARS="0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ!.-_";
	public static final String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
												+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
	private Connection conn;

	public Authenticator(){
		conn = null;
		try {
			Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
			conn = DriverManager.getConnection("jdbc:derby:test.db;create=true");
			conn.setAutoCommit(true);
		} catch (SQLException | ClassNotFoundException e) {
			System.out.println("Error: " + e.getMessage());
		} finally {
			DBUtil.closeQuietly(conn);
		}
	}
	/**
	 * adds a new user to the database.
	 * @param user the user to add to the database.
	 * @return true if user is added successfully, false otherwise.
	 */
	public boolean addNewUserToDB(User user){
		String sql="insert into users values ( '" + user.getEmailAddress() + "', '" + user.getPasswordHash() + "', 0, 0 ) ;";
		try {
			SQLWriter.executeDBCommand(conn, sql);
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
	/**
	 * Checks if a user already exists in the database
	 * associated with the given email address.
	 * @param emailAddress email address to check
	 * @return true if a user is found, false otherwise.
	 * @throws SQLException
	 */
	public boolean userExists(String emailAddress) throws SQLException{
		String sql="select * from users where emailaddress='" + emailAddress + "'";
		ResultSet rs=SQLWriter.executeDBCommand(conn, sql);
		if(rs.getString("emailaddress").contains(emailAddress)){
			return true;
		}
		else{
			return false;
		}
	}
	/**
	 * Populates a user object with data from database, if
	 * a user with the given email address exists.
	 * NOT FINISHED; HAVE TO POPULATE SCHEDULE TREEMAP
	 * 
	 * @param emailAddress user's email address to lookup user by
	 * @return user the user object associated with the given email address, if one exists.
	 * @throws SQLException
	 */
	public User getUser(String emailAddress) throws SQLException{
		User user=new User();
		if(this.userExists(emailAddress)){
			String sql="select * from users where emailaddress='" + emailAddress + "'";
			ResultSet rs=SQLWriter.executeDBCommand(conn, sql);
			user.setEmailAddress(emailAddress);
			user.setPasswordHash(rs.getString("passwordhash"));
			user.setStudentStatus(Integer.parseInt(rs.getString("studentstatus")));
			
			return user;
		}
		else return null;
	}
	
	public void saveExistingUserNewDataToDB(User usr){
		System.out.println("Authenticator::Method: saveExistingUserNewDataToDB(User usr) not implemented yet!");
	}
	
	public boolean setPasswordForUser(String emailAddress, String password){
		return false;
	}
	
	public boolean deleteUser(User user){
		String sql="delete from users where emailaddress='" + user.getEmailAddress() + "';";
		try {
			SQLWriter.executeDBCommand(conn, sql);
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	/**
	 * Checks if the password contains any illegal characters
	 * and is long enough.
	 * @param password the password to check
	 * @return true if password meets requirements, false otherwise
	 */
	public boolean isValidPassword(String password){
		if(password.equals("password")){ //LOL
			return false;
		}
		if(password.length()>=8){
			for(int i=0; i<password.length(); i++){
				if(!ALLOWED_CHARS.contains(Character.toString(password.charAt(i)))){
					return false;
				}
			}
			return true;
		}
		else{
			return false;
		}
	}
	
	/**
	 * Checks the given email address against the accepted
	 * email address format to make sure that the email is
	 * in the correct format
	 * @param emailAddress the address to check
	 * @return true if it is in email format, false otherwise
	 */
	public boolean isValidEmailAddress(String emailAddress){
		Pattern emailPattern=Pattern.compile(EMAIL_PATTERN);
		Matcher emailMatcher=emailPattern.matcher(emailAddress);
		return emailMatcher.matches();
	}
	public void requestPasswordReset(String email){
		Calendar cal=Calendar.getInstance();
		cal.setTime(new Date());
		cal.add(Calendar.DATE, 7);
		PasswordResetPage resetPage=new PasswordResetPage(this, email, cal);
		String message=resetPage.buildEmail(MESSAGE_FIRST_HALF, MESSAGE_SECOND_HALF);
		sendMail(email, message);
	}
	
	/**
	 * salts and hashes password; built using tutorial at
	 * http://howtodoinjava.com/security/how-to-generate-secure-password-hash-md5-sha-pbkdf2-bcrypt-examples/#PBKDF2WithHmacSHA1
	 * salt+hash algorithm uses 20,000 iterations.
	 * @param password password to salt and hash
	 * @return salted hashed password
	 * @throws NoSuchAlgorithmException
	 * @throws InvalidKeySpecException
	 */
	public String saltHashPassword(String password){
		int iterations=20000;
		char[] chars = password.toCharArray();
		byte[] salt;
		try {
			salt = getSalt().getBytes();
			PBEKeySpec spec = new PBEKeySpec(chars, salt, iterations, 64 * 8);
			SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
			byte[] hash = skf.generateSecret(spec).getEncoded();
			return iterations + ":" + toHex(salt) + ":" + toHex(hash);
		} catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
			e.printStackTrace();
			return null;
		}		
	}
	/**
	 * used for salt+hash; built using tutorial at
	 * http://howtodoinjava.com/security/how-to-generate-secure-password-hash-md5-sha-pbkdf2-bcrypt-examples/#PBKDF2WithHmacSHA1
	 * @return salt
	 * @throws NoSuchAlgorithmException
	 */
	private String getSalt() throws NoSuchAlgorithmException{
        SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
        byte[] salt = new byte[16];
        sr.nextBytes(salt);
        return salt.toString();
    }
	/**
	 * translate to hex; built using tutorial at
	 * http://howtodoinjava.com/security/how-to-generate-secure-password-hash-md5-sha-pbkdf2-bcrypt-examples/#PBKDF2WithHmacSHA1
	 * @param array byte array to translate
	 * @return hex representation of byte array
	 * @throws NoSuchAlgorithmException
	 */
	private String toHex(byte[] array) throws NoSuchAlgorithmException{
        BigInteger bi = new BigInteger(1, array);
        String hex = bi.toString(16);
        int paddingLength = (array.length * 2) - hex.length();
        if(paddingLength > 0){
            return String.format("%0"  +paddingLength + "d", 0) + hex;
		}
        else{
		    return hex;
		}
	}
	
	/**
	 * Validates the user's salted+hashed password with the one entered by the user
	 * at login; built using tutorial at 
	 * http://howtodoinjava.com/security/how-to-generate-secure-password-hash-md5-sha-pbkdf2-bcrypt-examples/#PBKDF2WithHmacSHA1
	 * 
	 * @param originalPassword the password entered by the user at login
	 * @param storedPassword the user's password as stored in the database
	 * @return	boolean true if valid, false if not
	 */
	public boolean validatePassword(String originalPassword, String storedPassword){
		String[] parts = storedPassword.split(":");
		int iterations = Integer.parseInt(parts[0]);
		byte[] salt = fromHex(parts[1]);
		byte[] hash = fromHex(parts[2]);
		byte[] testHash=null;

		PBEKeySpec spec = new PBEKeySpec(originalPassword.toCharArray(), salt, iterations, hash.length * 8);
		SecretKeyFactory skf;
		try {
			skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
			testHash = skf.generateSecret(spec).getEncoded();
		} catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
			e.printStackTrace();
		}
		

		int diff = hash.length ^ testHash.length;
		for (int i = 0; i < hash.length && i < testHash.length; i++) {
			diff |= hash[i] ^ testHash[i];
		}
		return diff == 0;
	}

	/**
	 * translates byte array from hex; built using tutorial at
	 * http://howtodoinjava.com/security/how-to-generate-secure-password-hash-md5-sha-pbkdf2-bcrypt-examples/#PBKDF2WithHmacSHA1
	 * @param hex the hex to decode
	 * @return bytes the translated byte array
	 */
	private byte[] fromHex(String hex) {
		byte[] bytes = new byte[hex.length() / 2];
		for (int i = 0; i < bytes.length; i++) {
			bytes[i] = (byte) Integer.parseInt(hex.substring(2 * i, 2 * i + 2), 16);
		}
		return bytes;
	}
	
	public Session authorizeUser(String email, String password){
		if(credentialsMatch(email, password)){
			return null; //implement!!
		}
		else{
			return null;
		}
	}
	
	//implement with database
	public boolean credentialsMatch(String email, String password){
		return false;
	}
	private Session createSession(User user){
		return new Session(user, this);
	}
	
	public static final String MESSAGE_FIRST_HALF=
			"<h2>Dear FixedIt User,</h2>" +
			"<h2>&nbsp; &nbsp; &nbsp; You have requested a password reset. " + 
			"If this is an error or you did not request a password reset, you " + 
			"can simply ignore this email and your password will remain unchanged. " +
			"To reset your password, you can click the link below and follow the " +
			"instructions on your password reset page.</h2>" +
			"<p>&nbsp;</p>" +
			"<p>&nbsp;</p>";
	public static final String MESSAGE_SECOND_HALF=
			"<p>&nbsp;</p>" +
			"<p>&nbsp;</p>" +
			"<center><img src=\"http://s11.postimg.org/97dahnc2r/fixedit_logo.jpg\"/></center>";
}