package model;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ClientAttendant implements Runnable {
	
	private Server server;
	private Socket client;
	
	public ClientAttendant(Socket client, Server s) {
		server=s;
		this.client=client;
	}

	@Override
	public void run() {
		DataInputStream in;
		DataOutputStream out;
		try {
			in = new DataInputStream(client.getInputStream());
			out= new DataOutputStream(client.getOutputStream());
			String requiredService=in.readUTF();
			if(requiredService.equals("R")){
				String email=in.readUTF();
				String nickname=in.readUTF();
				String password=in.readUTF();
				try {
					server.getDbm().saveNewUser(email,nickname,password);
					out.writeUTF("A");
				} catch (Exception e) {
					e.printStackTrace();
					out.writeUTF("E");
				}
			}else if(requiredService.equals("L")) {
				String email=in.readUTF();
				String password=in.readUTF();
				try {
					String nickname=server.getDbm().checkUser(email,password);
					int portGameHoster=server.getAvailableGameHoster();
					out.writeUTF(portGameHoster+":"+nickname);
				} catch (Exception e) {
					e.printStackTrace();
					out.writeUTF("E");
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}