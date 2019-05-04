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
	/**
	 * DatagramSocket 
	 */
	private DatagramSocket socketAudio;
	/**
	 * DatagramSocket 
	 */
	private DatagramSocket socketInfo;
	/**
	 * InetAddress of the client 
	 */
	private InetAddress clientAddres;
	/**
	 * TargetDataLine to read the music
	 */
	private TargetDataLine targetDataLine;
	
	/**
	 * Port of the client where he is going to listen the music
	 */
	private int clientPortAudio;
	/**
	 * Port where the client is going to read the music
	 */
	private int clientFormatPort;
	/**
	 * bytes that read the music
	 */
	private final byte audioBuffer[] = new byte[60000];
	/**
	 * File where is the music
	 */
	private File soundFile;
	/**
	 * Name of the file where is the music
	 */
	private String fileName;
	/**
	 * Audio Stream
	 */
	private AudioInputStream audioStream;
	/**
	 * boolean to pause or reanudate the music
	 */
	private boolean pause;
	/**
	 * Constructor
	 */
	public ThreadAudioServerUDP(InetAddress inet, int portAudio, int portFormat, String fileN)
			throws UnsupportedAudioFileException, IOException {
		clientAddres = inet;
		clientPortAudio = portAudio;
		socketAudio = new DatagramSocket();
		socketInfo = new DatagramSocket();
		clientFormatPort = portFormat;
		fileName = fileN;

		changeAudio(fileName);
		if (fileN != null && !fileN.contentEquals(""))
			pause = false;
		else
			pause = true;
	}
	/**
	 * Method that change the music that's playing
	 */
	public void changeAudio(String fileN) throws UnsupportedAudioFileException, IOException {
		pause = true;
		if (fileN != null && !fileN.contentEquals("")) {
			pause = false;
			soundFile = new File("./songs/" + fileN.trim() + ".wav");
//			soundFile=new File("C:\\Users\\Steven\\Desktop\\dataset\\"+fileN.trim()+".wav");

			audioStream = AudioSystem.getAudioInputStream(soundFile);
			fileName = fileN;
			if (targetDataLine != null) {
				
				
				targetDataLine.close();
				System.out.println("targetData: closed");
			}
			setupAudio();

		} else {

			soundFile = new File("./songs/malu.wav");
			audioStream = AudioSystem.getAudioInputStream(soundFile);

		}

	}
	/**
	 * Get the audio format
	 */
	public AudioFormat getAudioFormat() {
		return audioStream.getFormat();
	}
	/**
	 * Get the port of the client
	 */
	public int getClientPort() {
		return clientPortAudio;
	}
	/**
	 * Sends the music to the client
	 */
	public void run() {
		while (true) {

			try {

				sendAudio();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}
	/**
	 * Pause the music
	 */
	public void pauseMusic() {
		pause = true;
	}
	/**
	 * Continue the music
	 */
	public void continueMusic() {
		pause = false;
	}
	/**
	 * Methof that send the audio to the client and viewers
	 */
	public void sendAudio() throws IOException, LineUnavailableException, InterruptedException {

		while (true) {
			int length = 0;
			if (!pause)
				length = audioBuffer.length;
			int count = audioStream.read(audioBuffer, 0, length);
			AudioFormat aud = getAudioFormat();
			String info = aud.getSampleRate() + " " + aud.getSampleSizeInBits() + " " + aud.getChannels();
//			System.out.println(info);
			byte[] convertedInfo = info.getBytes();
			if (count > 0 && length != 0) {
				DatagramPacket packetInfo = new DatagramPacket(convertedInfo, convertedInfo.length, clientAddres,
						clientFormatPort);
				socketInfo.send(packetInfo);
//				System.out.println("a");
				DatagramPacket packet = new DatagramPacket(audioBuffer, length, clientAddres, clientPortAudio);
				socketAudio.send(packet);
				Thread.sleep(300);
			}
		}
	}
	/**
	 * Method that return the audio
	 */
	public AudioFormat setupAudio() {
		try {
			AudioFormat audioFormat = getAudioFormat();
			DataLine.Info info = new DataLine.Info(TargetDataLine.class, audioFormat);

			
			targetDataLine = (TargetDataLine) AudioSystem.getLine(info);

			try {
				
				targetDataLine.open(audioFormat);
				
			}catch (Exception e) {
				// TODO: handle exception
			}
			
			targetDataLine.start();
			return audioFormat;
		} catch (Exception ex) {
			ex.printStackTrace();
			System.exit(0);
		}

		return null;
	}

}
