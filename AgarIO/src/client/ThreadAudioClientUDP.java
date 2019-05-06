package client;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.SocketException;
import java.util.ArrayList;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.TargetDataLine;

import gameServer.ThreadAudioServerUDP;
import gameServer.ThreadToListenAChangeOfSong;

public class ThreadAudioClientUDP extends Thread {
	/**
	 * Audio Socket
	 */
	public DatagramSocket socketAudio;
	/**
	 * Socket Format
	 */
	public DatagramSocket socketFormat;
	/**
	 * Socket Songs
	 */
	public DatagramSocket socketSongs;
	/**
	 * Input Audio
	 */
	private AudioInputStream audioStream;
	/**
	 * Output Audio
	 */
	private AudioFormat audioFormat;
	/**
	 * Source Line
	 */
	private SourceDataLine sourceLine;
	/**
	 * Port of the audio
	 */
	public final static int AUDIO_PORT =54321;
	/**
	 * Port of the format audio
	 */
	public final static int FORMAT_PORT= 54325;
	/**
	 * Constant
	 */
	public final static int CONST=60000;
	/**
	 * Change socket port
	 */
	private int changingSocketPort;
	/**
	 * Constructor class
	 */
	public ThreadAudioClientUDP()  {
		
		try {
			socketAudio= new DatagramSocket();
			socketFormat= new DatagramSocket();
			socketSongs= new DatagramSocket();
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * Play Audio
	 */
	public void run() {
		InitiateAudio();
		playAudio();
	}
	/**
	 * Metod that change the socket port
	 */
	public void setChangingSocketPort(int port) {
		changingSocketPort=port;
	}
	/**
	 * Get the audio port
	 */
	public int getAudioPort() {
		return socketAudio.getLocalPort();
	}
	/**
	 * Get format port
	 */
	public int getFormatPort() {
		return socketFormat.getLocalPort();
	}
	/**
	 * Change the song
	 */
	public void changeSong(String song) {
		String songName = "c "+song; 
		byte[] b= songName.getBytes();
		try {
			
			socketSongs.send(new DatagramPacket(b, b.length, InetAddress.getByName(Controller.IP_DIRECTION),changingSocketPort));
			System.out.println("sent");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * Pause the music
	 */
	public void pauseMusic() {
		String m= "p";
		byte[] b= m.getBytes();
		try {
			
			socketSongs.send(new DatagramPacket(b, b.length, InetAddress.getByName(Controller.IP_DIRECTION),changingSocketPort));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * Resume the music
	 */
	public void resumeMusic() {
		String m= "r";
		byte[] b= m.getBytes();
		try {
			
			socketSongs.send(new DatagramPacket(b, b.length, InetAddress.getByName(Controller.IP_DIRECTION),changingSocketPort));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * Get port
	 */
	public int getPort() {
		return socketAudio.getLocalPort();
	}
	/**
	 * Start to play the music
	 */
	private void playAudio() {
		byte[] buffer = new byte[CONST];
		try {
			int count;

			while ((count = audioStream.read(buffer, 0, buffer.length)) != -1) {
				if (count > 0) {
//					System.out.println("b");
					
					sleep(300);
					sourceLine.write(buffer, 0, count);

				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Initiate the audio
	 */
	public void InitiateAudio() {

		try {

			byte[] audioBuffer = new byte[CONST];
			byte[] formatInfo= new byte[1024];
			// ...

			while (true) {
				DatagramPacket packet = new DatagramPacket(audioBuffer, audioBuffer.length);
				socketAudio.receive(packet);
				DatagramPacket packetInfo = new DatagramPacket(formatInfo, formatInfo.length);
				socketFormat.receive(packetInfo);
				String[] info= new String(packetInfo.getData()).trim().split(" ");
				// ...
				System.out.println(new String(packetInfo.getData()));
				try {

					byte audioData[] = packet.getData();
					InputStream byteInputStream = new ByteArrayInputStream(audioData);
					audioFormat = new AudioFormat(Float.parseFloat(info[0]), Integer.parseInt(info[1]), Integer.parseInt(info[2]), true, false);
					audioStream = new AudioInputStream(byteInputStream, audioFormat,
							audioData.length / audioFormat.getFrameSize());

					//					audioStream = new Audio

					DataLine.Info dataLineInfo = new DataLine.Info(SourceDataLine.class, audioStream.getFormat());


					sourceLine = (SourceDataLine) AudioSystem.getLine(dataLineInfo);
					sourceLine.open(audioStream.getFormat());

					sourceLine.start();
					playAudio();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
