package fixedIt.modelComponents;

import java.io.File;
import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

import fixedIt.sql.database.*;

public class Authenticator implements EmailSender {
	public static final String ALLOWED_CHARS="0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ!.-_";
	public static final String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
												+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

	public Authenticator(){
		
	}
	
	private Connection getConnection(){
		Connection conn=null;
		File dbPath=new File("test.db");
		try {
			Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
			conn = DriverManager.getConnection("jdbc:derby:" + dbPath.getAbsolutePath() + ";create=true");
			conn.setAutoCommit(false);
			return conn;
		} catch (SQLException | ClassNotFoundException e) {
			System.out.println("Error: " + e.getMessage());
			conn=null;
		}
		return conn;
	}
	
	/**
	 * adds a new basic user to the database.
	 * @param user the user to add to the database.
	 * @return true if user is added successfully, false otherwise.
	 */
	public boolean addNewUserToDB(User user){
		if(!userExists(user.getEmailAddress())){
			String sql="insert into users values ( '" + user.getEmailAddress().toLowerCase() + "', '" + user.getPasswordHash() + "', 0, 0 ) ";
			Connection conn=getConnection();
			try {
				SQLWriter.executeDBCommand(conn, sql);
				conn.commit();
				conn.close();
				conn=null;
				return true;
			} catch (SQLException e) {
				DBUtil.closeQuietly(conn);
				conn=null;
				e.printStackTrace();
				return false;
			}
		}
		else{
			return false;
		}
	}
	
	public void addCoursesToDB(ArrayList<Course> courses) throws SQLException {
		Connection conn = null;
		conn=getConnection();
		conn.setAutoCommit(false);
		//int count=0;
		for (Course c : courses) {
			//count++;
			System.out.println("	Deleting course " + c.getCRN() + "...");
			String sqlDelete="delete from courses where crn='" + c.getCRN() + "'";
			PreparedStatement preparedDelete=conn.prepareStatement(sqlDelete);
			preparedDelete.executeUpdate();
			//SQLWriter.executeDBCommand(conn, sqlDelete);
//			if(count==5){
//				System.out.println("		Committing DB changes...");
//				conn.commit();
//				System.out.println("		Done committing DB changes.");
//				count=0;
//			}
			System.out.println("		Done deleting " + c.getCRN() + ".");
			String sql="insert into courses \n" +
					"(CRN, courseAndSection, title, credits, type, days, time, location_one, location_two, instructor_one, instructor_two, capacity, seatsRemain, seatsFilled, beginEnd) \n" +
					"values (\n'" +
					c.getCRN() + "', \n'" + c.getCourseAndSection() + "', \n'" +
					c.getTitle().replace("'", "''") + "', \n'" + c.getCredits() + "', \n'" + c.getType().replace("'", "''") + "', \n'" +
					c.getDays() + "', \n'" + c.getTime() + "', \n";
			if(c.getLocation().size()<2){
				sql=sql+"'" + c.getLocation().get(0) + "', \n" + "'null', \n";
			}
			else{
				sql=sql+"'" + c.getLocation().get(0) + "', \n'" + c.getLocation().get(1) + "', \n";
			}
			if(c.getInstructors().size()<2){
				sql=sql+"'" + c.getInstructors().get(0).replace("'", "''") + "', \n" + "'null', \n";
			}
			else{
				sql=sql+"'" + c.getInstructors().get(0).replace("'", "''") + "', \n'" + c.getInstructors().get(1).replace("'", "''") + "', \n";
			}
			sql=sql +"'" + c.getCapacity() + "', \n'" + c.getSeatsRemain() + "', \n'" + c.getSeatsFilled()
				+ "', \n'" + c.getBeginEnd() + "'" + ")";
			SQLWriter.executeDBCommand(conn, sql);
		}
		conn.commit();
		conn.close();
		conn=null;
	}
	/**
	 * Checks if a user already exists in the database
	 * associated with the given email address.
	 * @param emailAddress email address to check
	 * @return true if a user is found, false otherwise.
	 * @throws SQLException
	 */
	public boolean userExists(String emailAddress){
		String sql="select * from users where emailaddress='" + emailAddress.toLowerCase() + "'";
		Connection conn=getConnection();
		ResultSet rs;
		try {
			rs = SQLWriter.executeDBCommand(conn, sql);
			rs.absolute(1);
			if(rs.getString("emailaddress").equals(emailAddress.toLowerCase())){
				conn.commit();
				conn.close();
				conn=null;
				return true;
			}
			else{
				conn.commit();
				conn.close();
				conn=null;
				return false;
			}
		} catch (SQLException e) {
			//e.printStackTrace();
			DBUtil.closeQuietly(conn);
			conn=null;
			return false;
		}
	}
	/**
	 * Populates a user object with data from database by email address, if
	 * a user with the given email address exists.
	 * 
	 * @param emailAddress user's email address to lookup user by
	 * @return user the user object associated with the given email address, if one exists.
	 * @throws SQLException
	 * @throws ConflictException 
	 */
	public User getUser(String emailAddress) throws SQLException{
		Connection conn=getConnection();
		User user=new User();
		if(this.userExists(emailAddress)){
			String sql="select * from users where emailaddress='" + emailAddress.toLowerCase() + "'";
			ResultSet rs=SQLWriter.executeDBCommand(conn, sql);
			rs.absolute(1);
			user.setPasswordHash(rs.getString("passwordhash"));
			user.setStudentStatus(Integer.parseInt(rs.getString("studentstatus")));
			int numSchedules=Integer.parseInt(rs.getString("numschedules"));
			user.setEmailAddress(emailAddress);
			sql="select * from sys.systables where tablename like '%" + emailAddress.toUpperCase().substring(0, emailAddress.indexOf("@")) + "%' ";
			//System.out.println(sql);
			Statement stmnt1=conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			rs=stmnt1.executeQuery(sql);  //gets all of the user's schedules as ResultSet
			TreeMap<String, Schedule> schedules=new TreeMap<String, Schedule>();
			if(numSchedules>0){
				while(rs.next()){	//loop through all schedules
					String scheduleName=rs.getString("tablename").substring("schedule".length(), rs.getString("tablename").indexOf(emailAddress.toUpperCase().substring(0, emailAddress.indexOf("@"))));
					Schedule s=new Schedule(scheduleName);
					sql="select * from " + rs.getString("tablename");
					Statement stmnt2=conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
					ResultSet courses=stmnt2.executeQuery(sql);  //get all courses in current schedule 
					//System.out.println(courses.isBeforeFirst());
					while(courses.next()){	//loop through all courses in current schedule 
						Course c=new Course();
						sql="select * from courses where crn='" + courses.getString("crn") + "'";
						ResultSet course=SQLWriter.executeDBCommand(conn, sql);
						course.absolute(1);
						c.setCRN(Integer.parseInt(course.getString("crn")));					//
						c.setCourseAndSection(course.getString("courseandsection"));			//
						c.setTitle(course.getString("title"));									//
						c.setCredits(Double.parseDouble(course.getString("credits")));			//
						c.setType(course.getString("type"));									//
						c.setDays(course.getString("days"));									//	add all course info
						c.setTime(course.getString("time"));									//	to a course object
						c.addLocation(course.getString("location_one"));						//
						c.addLocation(course.getString("location_two"));						//
						c.addInstructor(course.getString("instructor_one"));					//
						c.addInstructor(course.getString("instructor_two"));					//
						c.setCapacity(Integer.parseInt(course.getString("capacity")));			//
						c.setSeatsRemain(Integer.parseInt(course.getString("seatsremain")));	//
						c.setSeatsFilled(Integer.parseInt(course.getString("seatsfilled")));	//
						c.setBeginEnd(course.getString("beginend"));							//
						s.addCourse(c);		//add course to a schedule
					}
					schedules.put(scheduleName, s);	//add current schedule to schedules TreeMap
				}
				user.setSchedules(schedules); //add schedules TreeMap
			}	
			conn.commit();
			conn.close();
			conn=null;
			return user;		//return the generated user
		}
		conn.commit();
		conn.close();
		conn=null;
		return null;	//if the user does not exist, or if a database error occurs, return null
	}
	
	/**
	 * Erases all user data and writes new/current user data to database.
	 * @param usr the user for which to write the data
	 * @throws SQLException
	 */
	public void saveExistingUserNewDataToDB(User usr) throws SQLException{
		Connection conn=getConnection();
		String sql="delete from users where emailaddress='" + usr.getEmailAddress().toLowerCase() + "'";
		SQLWriter.executeDBCommand(conn, sql);
		sql="insert into users values ( '" +usr.getEmailAddress() + "', '" + usr.getPasswordHash() + "', " +
				usr.getStudentStatus() + ", " + usr.getNumSchedules() + " ) ";
		SQLWriter.executeDBCommand(conn, sql);
		sql="select * from sys.systables where tablename like '%SCHEDULE" + usr.getEmailAddress().toUpperCase().substring(0, usr.getEmailAddress().indexOf("@")) + "%' ";
		Statement stmnt1=conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
		ResultSet rs=stmnt1.executeQuery(sql);
		while(rs.next()){
			sql="drop table " + rs.getString("tablename").toUpperCase();
			SQLWriter.executeDBCommand(conn, sql);
		}
		for(Schedule s : usr.getSchedules().values()){
			sql="create table schedule" + usr.getEmailAddress().substring(0, usr.getEmailAddress().indexOf("@")).toLowerCase() + s.getName() + "( crn varchar(20) )";
			SQLWriter.executeDBCommand(conn, sql);
			for(Course c : s.getCourses()){
				sql="insert into SCHEDULE" + usr.getEmailAddress().toUpperCase().substring(0, usr.getEmailAddress().indexOf("@")) + s.getName() + " VALUES ( '" + c.getCRN() + "' ) ";
				SQLWriter.executeDBCommand(conn, sql);
			}
		}
		conn.commit();
		conn.close();
		conn=null;
	}
	/**
	 * updates the user's password, if the password is valid
	 * @param emailAddress the email address of the user for which to update the password
	 * @param password the salt+hash of the desired password
	 * @return true if password is updated successfully, false otherwise.
	 * @throws SQLException
	 */
	public boolean setPasswordForUser(String emailAddress, String password) throws SQLException{
		Connection conn=getConnection();
		String sql="update users set passwordhash='" + password + "' where emailaddress='" + emailAddress.toLowerCase() + "' ";
		if(isValidPassword(password)){
			SQLWriter.executeDBCommand(conn, sql);
			conn.commit();
			conn.close();
			conn=null;
			return true;
		}
		conn.commit();
		conn.close();
		conn=null;
		return false;
	}	
	/**
	 * Deletes a user from the database. BE CAREFUL WITH THIS.
	 * @param user user to delete
	 * @return true if user is removed successfully, false otherwise
	 */
	public boolean deleteUser(User user){
		Connection conn=getConnection();
		String sql="delete from users where emailaddress='" + user.getEmailAddress().toLowerCase() + "'";
		try {
			SQLWriter.executeDBCommand(conn, sql);
			conn.commit();
			conn.close();
			conn=null;
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			DBUtil.closeQuietly(conn);
			conn=null;
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
		if(password.equals("password") || password.equals("password123")){ //LOL
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
	/**
	 * Request a password reset for the user associated with the given
	 * email address.
	 * @param email address for which to lookup user
	 */
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
	private boolean validatePassword(String originalPassword, String storedPassword){
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
	/**
	 * Authorize a user and create a session,
	 * if the credentials are valid and a user
	 * exists. All read/write operations that
	 * need to be done from other classes
	 * should be done THROUGH a Session object
	 * created by this method.
	 * 
	 * @param email email address for desired user
	 * @param password password to check against email address
	 * @return Session a Session object for the desired user, if
	 * the user is authorized, null if credentials do not match or
	 * an SQL error occurs.
	 * @throws SQLException
	 * @throws ConflictException 
	 */
	public Session authorizeUser(String email, String password) throws SQLException{
		if(credentialsMatch(email, password)){
			return createSession(getUser(email));
		}
		else{
			return null;
		}
	}
	/**
	 * Checks whether a user exists that is
	 * associated with the given email address;
	 * if so, checks whether the password hash
	 * matches the password hash for that user. 
	 * @param email email address by which to lookup user
	 * @param password password to hash and check against email address
	 * @return true if such a user exists and the credentials are valid, false otherwise
	 * @throws SQLException
	 */
	public boolean credentialsMatch(String email, String password){
		Connection conn=getConnection();
		String sql="select passwordhash from users where emailaddress='" + email.toLowerCase() + "'";
		ResultSet rs;
		String storedHash="";
		try {
			rs = SQLWriter.executeDBCommand(conn, sql);
			rs.absolute(1);
			storedHash=rs.getString("passwordhash");
			conn.commit();
			conn.close();
			conn=null;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		//System.out.println(validatePassword(password, storedHash));
		return validatePassword(password, storedHash);
	}
	
	/**
	 * Creates a new Session object for the given user.
	 * @param user the User for which to create a Session
	 * @return Session a Session for the given User
	 */
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