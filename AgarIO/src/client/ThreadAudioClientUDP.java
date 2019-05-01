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
	public DatagramSocket socketAudio;
	public DatagramSocket socketFormat;
	public DatagramSocket socketSongs;
	private AudioInputStream audioStream;
	private AudioFormat audioFormat;
	private SourceDataLine sourceLine;
	public final static int AUDIO_PORT =54321;
	public final static int FORMAT_PORT= 54325;
	public final static int CONST=60000;
	private int changingSocketPort;
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

	public void run() {
		InitiateAudio();
		playAudio();
	}
	public void setChangingSocketPort(int port) {
		changingSocketPort=port;
	}
	public int getAudioPort() {
		return socketAudio.getLocalPort();
	}
	public int getFormatPort() {
		return socketFormat.getLocalPort();
	}
	public void changeSong(String song) {
		String songName = "c "+song; 
		byte[] b= songName.getBytes();
		try {
			
			socketSongs.send(new DatagramPacket(b, b.length, InetAddress.getByName(Controller.IP_DIRECTION),changingSocketPort));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
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
	public int getPort() {
		return socketAudio.getLocalPort();
	}
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
