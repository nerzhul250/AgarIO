package registrationManagement;

public class UserRegistered {
	
	private String userName;
	private String email;
	private String password;
	
	public UserRegistered (String userName, String email, String password) {
		this.userName = userName;
		this.email = email;
		this.password = password;
	}
	
	public boolean checkPassword(String password) {
		return this.password.equals(password);
	}
	
	public String getUserName() {
		return userName;
	}
	
}
