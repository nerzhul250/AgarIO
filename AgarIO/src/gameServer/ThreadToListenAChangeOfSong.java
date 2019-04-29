package gameServer;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

import javax.sound.sampled.UnsupportedAudioFileException;

public class ThreadToListenAChangeOfSong extends Thread {
	public final static int SONGS_PORT=54389;
	private DatagramSocket socketSongs;
	private ThreadAudioServerUDP threadAudioServerUDP;
	public ThreadToListenAChangeOfSong(ThreadAudioServerUDP thread) {
		threadAudioServerUDP= thread;
		try {
			socketSongs= new DatagramSocket(SONGS_PORT);
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void run() {
		listenToChangeSong();
	}
	public void listenToChangeSong() {
		while(true) {
			byte[] song= new byte[1024];
			DatagramPacket receiveP= new DatagramPacket(song, song.length);
			try {
				socketSongs.receive(receiveP);
				String songName = new String(receiveP.getData());
				threadAudioServerUDP.changeAudio(songName);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (UnsupportedAudioFileException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
