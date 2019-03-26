package registrationManagement;

import java.util.HashMap;
import java.util.HashSet;

public class DataBaseManager {
	HashMap<String, UserRegistered> usersRegistered;
	HashSet<String> nicksInUse;
	
	public DataBaseManager() {
		usersRegistered = new HashMap<String, UserRegistered>();
		nicksInUse = new HashSet<String>();
	}

	public synchronized void saveNewUser(String email, String nickname, String password) throws Exception {
		if (usersRegistered.containsKey(email)) throw new Exception("Ya existe un usuario con el correo registrado");
		if (nicksInUse.contains(nickname)) throw new Exception("Ya existe un usuario registrado con ese nickname");
		if (!isValidEmailAddress(email)) throw new Exception("Ingrese una dirección de correo válida");
		UserRegistered newUser = new UserRegistered(nickname, email, password);
		usersRegistered.put(email, newUser);
		nicksInUse.add(nickname);
	}

	public synchronized UserRegistered checkUser(String email, String password) throws Exception{
		UserRegistered user = usersRegistered.get(email);
		if (user == null) throw new Exception("No existe ningún usuario registrado con el correo indicado");
		boolean correctPass = user.checkPassword(password);
		if (!correctPass) throw new Exception("La contraseña no coincide con la del usuario registrado");
		return user;
		
	}
	
	//Took from https://stackoverflow.com/questions/624581/what-is-the-best-java-email-address-validation-method
	public boolean isValidEmailAddress(String email) {
        String ePattern = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
        java.util.regex.Pattern p = java.util.regex.Pattern.compile(ePattern);
        java.util.regex.Matcher m = p.matcher(email);
        return m.matches();
 }

}
