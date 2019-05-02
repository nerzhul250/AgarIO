package gameServer;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

import javax.sound.sampled.UnsupportedAudioFileException;

public class ThreadToListenAChangeOfSong extends Thread {
	/**
	 * Puerto por donde se va a prestar el servicio de sonido
	 */
	public final static int SONGS_PORT=54389;
	/**
	 * DatagramSocket
	 */
	private DatagramSocket socketSongs;
	/**
	 * Audio del servidor
	 */
	private ThreadAudioServerUDP threadAudioServerUDP;
	/**
	 * Constructor de la clase
	 */
	public ThreadToListenAChangeOfSong(ThreadAudioServerUDP thread) {
		threadAudioServerUDP= thread;
		try {
			socketSongs= new DatagramSocket();
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * obtener el puerto del cliente
	 */
	public int getSongPort() {
		return socketSongs.getLocalPort();
	}
	/**
	 * Iniciar el cambio de musica
	 */
	public void run() {
		listenToChangeSong();
	}
	/**
	 * Metodo que esta pendiente si el usuario quiere cambiar, pausar o reanudar la cancion
	 */
	public void listenToChangeSong() {
		while(true) {
			byte[] song= new byte[1024];
			DatagramPacket receiveP= new DatagramPacket(song, song.length);
			try {
				socketSongs.receive(receiveP);
				String[] order = new String(receiveP.getData()).trim().split(" ");
//				System.out.println(new String(receiveP.getData()));
				if(order[0].contentEquals("c")) {
					if(order.length==2) {
						threadAudioServerUDP.changeAudio(order[1]);								
					}
				}else if(order[0].contentEquals("p")) {
					threadAudioServerUDP.pauseMusic();
				}else if(order[0].contentEquals("r")) {
					threadAudioServerUDP.continueMusic();
				}
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
