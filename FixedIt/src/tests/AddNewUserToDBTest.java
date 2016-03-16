package tests;

import fixedIt.modelComponents.*;

public class AddNewUserToDBTest {

	public static void main(String[] args) {
		Authenticator auth=new Authenticator();
		String email="test@example.com";
		String passHash=auth.saltHashPassword("password");
		User user=new User(email, passHash, 0, 0, auth);
		auth.addNewUserToDB(user);
	}

}
