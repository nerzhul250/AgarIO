package registrationManagement;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.HashSet;

import javax.imageio.stream.FileImageInputStream;
/**
 * The data base
 * @author Steven
 *
 */
public class DataBaseManager {
	/**
	 * the path of the users
	 */
	public final static String FILE_PATH_USERS = "./data/users";
	/**
	 * the path of the nicknames
	 */
	public final static String FILE_PATH_NICKS = "./data/nicks";
	/**
	 * hash that contains the users
	 */
	HashMap<String, UserRegistered> usersRegistered;
	/**
	 * hashset that contains the nicknames
	 */
	HashSet<String> nicksInUse;
	/**
	 * Constructs the data base manager
	 */
	public DataBaseManager() {
		File f = new File(FILE_PATH_USERS);
		if (!f.exists()) {
			usersRegistered = new HashMap<String, UserRegistered>();
			nicksInUse = new HashSet<String>();
		} else {
			try {
				FileInputStream fin = new FileInputStream(f);
				ObjectInputStream in = new ObjectInputStream(fin);
				usersRegistered = (HashMap<String, UserRegistered>) in.readObject();
				
				f = new File(FILE_PATH_NICKS);
				fin = new FileInputStream(f);
				in = new ObjectInputStream(fin);
				nicksInUse = (HashSet<String>) in.readObject();
			} catch (IOException | ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	/**
	 * Saves a new user
	 * @param email
	 * @param nickname
	 * @param password
	 * @throws Exception
	 */
	public synchronized void saveNewUser(String email, String nickname, String password) throws Exception {
		if (usersRegistered.containsKey(email)) throw new Exception("Ya existe un usuario con el correo registrado");
		if (nicksInUse.contains(nickname)) throw new Exception("Ya existe un usuario registrado con ese nickname");
		if (!isValidEmailAddress(email)) throw new Exception("Ingrese una dirección de correo válida");
		UserRegistered newUser = new UserRegistered(nickname, email, password);
		usersRegistered.put(email, newUser);
		nicksInUse.add(nickname);
		
		File f = new File(FILE_PATH_USERS);
		FileOutputStream fout = new FileOutputStream(f);
		ObjectOutputStream out = new ObjectOutputStream(fout);
		out.writeObject(usersRegistered);
		
		f = new File(FILE_PATH_NICKS);
		fout = new FileOutputStream(f);
		out = new ObjectOutputStream(fout);
		out.writeObject(nicksInUse);
	}
	/**
	 * verifies if the user exist and if its password is correct
	 * @param email
	 * @param password
	 * @return
	 * @throws Exception
	 */
	public synchronized UserRegistered checkUser(String email, String password) throws Exception{
		UserRegistered user = usersRegistered.get(email);
		if (user == null) throw new Exception("No existe ningún usuario registrado con el correo indicado");
		boolean correctPass = user.checkPassword(password);
		if (!correctPass) throw new Exception("La contraseña no coincide con la del usuario registrado");
		return user;
		
	}
	
	//Took from https://stackoverflow.com/questions/624581/what-is-the-best-java-email-address-validation-method
	/**
	 * verifies if the email is valid in format
	 * @param email
	 * @return
	 */
	public boolean isValidEmailAddress(String email) {
        String ePattern = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
        java.util.regex.Pattern p = java.util.regex.Pattern.compile(ePattern);
        java.util.regex.Matcher m = p.matcher(email);
        return m.matches();
 }

}
