package registrationManagement;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
/**
 * Responsible of the login part
 * @author Steven
 *
 */
public class ClientAttendant implements Runnable {
	/**
	 * Represents the state to register
	 */
	public final static String REGISTER = "R";
	/**
	 * represents the state to login
	 */
	public final static String LOGIN = "L";
	/**
	 * Represents the accepted state
	 */
	public final static String ACCEPTED = "A";
	/**
	 * represents an error
	 */
	public final static String ERROR = "E";
	
	/**
	 * is the connection server
	 */
	private Server server;
	/**
	 * represents the client
	 */
	private Socket client;
	/**
	 * Constructor
	 * @param client
	 * @param s
	 */
	public ClientAttendant(Socket client, Server s) {
		server=s;
		this.client=client;
	}

	@Override
	public void run() {
		BufferedReader in;
		PrintWriter out;
		try {
			in = new BufferedReader(new InputStreamReader(client.getInputStream()));
			out= new PrintWriter(client.getOutputStream(), true);
			String requiredService=in.readLine();
			if(requiredService.equals(REGISTER)){
				String email=in.readLine();
				String nickname=in.readLine();
				String password=in.readLine();
				try {
					server.getDbm().saveNewUser(email,nickname,password);
					out.println(ACCEPTED);
				} catch (Exception e) {
					out.println(ERROR);
					//e.printStackTrace();
					out.println(e.getMessage());
				}
			}else if(requiredService.equals(LOGIN)) {
				String email=in.readLine();
				String password=in.readLine();
				try {
					String nickname=server.getDbm().checkUser(email,password).getUserName();
					int portGameHoster=server.getAvailableGameHoster();
					out.println(ACCEPTED);
					out.println(portGameHoster);
					out.println(nickname);
				} catch (Exception e) {
					out.println(ERROR);
					e.printStackTrace();
					out.println(e.getMessage());
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}