package registrationManagement;

import java.io.Serializable;
/**
 * Represents a registered user
 * @author Steven
 *
 */
public class UserRegistered implements Serializable{
	/**
	 * The user name
	 */
	private String userName;
	/**
	 * the email of the user
	 */
	private String email;
	/**
	 * password of the user
	 */
	private String password;
	/**
	 * Constructor
	 * @param userName
	 * @param email
	 * @param password
	 */
	public UserRegistered (String userName, String email, String password) {
		this.userName = userName;
		this.email = email;
		this.password = password;
	}
	/**
	 * checks the password
	 * @param password
	 * @return
	 */
	public boolean checkPassword(String password) {
		return this.password.equals(password);
	}
	
	public String getUserName() {
		return userName;
	}
	
}
