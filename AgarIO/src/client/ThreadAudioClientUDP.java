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

public class ThreadAudioClientUDP extends Thread {
	public DatagramSocket socket;
	private AudioInputStream audioStream;
	private AudioFormat audioFormat;
	private SourceDataLine sourceLine;

	public final static int CONST=60000;
	ThreadAudioServerUDP server;
	public ThreadAudioClientUDP() throws SocketException {
		socket= new DatagramSocket();

	}
	public void setServer(ThreadAudioServerUDP s) {
		server= s;
	}
	public void run() {
		InitiateAudio();
		playAudio();
	}
	public int getPort() {
		return socket.getLocalPort();
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
			// ...

			while (true) {
				DatagramPacket packet = new DatagramPacket(audioBuffer, audioBuffer.length);
				socket.receive(packet);

				// ...
				

				try {

					byte audioData[] = packet.getData();
					InputStream byteInputStream = new ByteArrayInputStream(audioData);
					audioFormat = server.getAudioFormat();
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
