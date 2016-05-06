package tests;

import fixedIt.modelComponents.Authenticator;
import fixedIt.modelComponents.User;

public class DeleteUserTest {
	public static void main(String[] args) {
		Authenticator auth=new Authenticator();
		String email="test@example.com";
		String passHash=auth.saltHashPassword("password");
		User user=new User(email, passHash, 0, auth);
		auth.deleteUser(user);
	}
}
