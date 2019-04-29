package gameServer;
import java.io.File;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.TargetDataLine;
import javax.sound.sampled.UnsupportedAudioFileException;

import client.ThreadAudioClientUDP;

public class ThreadAudioServerUDP extends Thread {
	private DatagramSocket socket;
	private InetAddress clientAddres;
	private TargetDataLine targetDataLine;
	private int clientPort;
	private final byte audioBuffer[] = new byte[60000];
	private File soundFile;
	private String fileName;
	private AudioInputStream audioStream;
	private boolean pause;
	private ThreadAudioClientUDP clientSocket;
	public ThreadAudioServerUDP(InetAddress inet, int port, String fileN, ThreadAudioClientUDP cl) throws UnsupportedAudioFileException, IOException {
		clientAddres= inet;
		clientPort= port;
		socket= new DatagramSocket();
		fileName= fileN;
		clientSocket= cl;
		changeAudio(fileName);
		if(fileN != null && !fileN.contentEquals(""))
			pause= false;
		else
			pause= true;
	}
	public void changeAudio(String fileN) throws UnsupportedAudioFileException, IOException {
		pause = true;
		if(fileN != null && !fileN.contentEquals("")) {
			pause= false;
			soundFile=new File("C:\\Users\\Steven\\Desktop\\dataset\\"+fileN+".wav");
			audioStream= AudioSystem.getAudioInputStream(soundFile);
			fileName= fileN;
			if(targetDataLine!=null)
				targetDataLine.close();
			setupAudio();

		}else {
			
			soundFile=new File("C:\\Users\\Steven\\Desktop\\dataset\\malu.wav");
			audioStream= AudioSystem.getAudioInputStream(soundFile);

		}

	}
	public AudioFormat getAudioFormat() {
		return audioStream.getFormat();
	}
	public int getClientPort() {
		return clientPort;
	}
	public void run() {
		while(true) {
			
			try {

				sendAudio();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		


	}
	public void pauseMusic()  {
		pause= true;
	}
	public void continueMusic() {
		pause= false;
	}
	public void sendAudio() throws IOException, LineUnavailableException, InterruptedException {

		while (true) {
			int length=0;
			if(!pause)
				length=audioBuffer.length;
			int count = audioStream.read(audioBuffer, 0, length);
			if (count > 0&& length != 0) {
//				System.out.println("a");
				DatagramPacket packet = new DatagramPacket(audioBuffer, length, clientAddres,clientPort);
				socket.send(packet);
				Thread.sleep(300);
			}
		}
	}

	public AudioFormat setupAudio() {

		try {


			AudioFormat audioFormat= getAudioFormat();
			DataLine.Info info = new DataLine.Info(TargetDataLine.class, audioFormat);


			targetDataLine = (TargetDataLine) AudioSystem.getLine(info);

			targetDataLine.open(audioFormat);
			targetDataLine.start();
			return audioFormat;
		} catch (Exception ex) {
			ex.printStackTrace();
			System.exit(0);
		}


		return null;
	}




}
