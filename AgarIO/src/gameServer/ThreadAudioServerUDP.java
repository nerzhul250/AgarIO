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
	 * InetAddress del cliente 
	 */
	private InetAddress clientAddres;
	/**
	 * TargetDataLine para leer la musica
	 */
	private TargetDataLine targetDataLine;
	
	/**
	 * Puerto del cliente por donde va a escuchar el audio
	 */
	private int clientPortAudio;
	/**
	 * Puerto por donde se va a leer el formato del audio
	 */
	private int clientFormatPort;
	/**
	 * bytes de lecturas de la musica
	 */
	private final byte audioBuffer[] = new byte[60000];
	/**
	 * Archivo donde esta la musica
	 */
	private File soundFile;
	/**
	 * Nombre del archivo donde esta la musica
	 */
	private String fileName;
	/**
	 * Especificacion del audio y su longitud
	 */
	private AudioInputStream audioStream;
	/**
	 * boolean para pausar y reanudar la musica
	 */
	private boolean pause;
	/**
	 * Constructor de la clase threadAudioServerUDP
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
	 * Metodo que cambia la cancion que se esta escuchando
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
	 * Obtener el formato del audio
	 */
	public AudioFormat getAudioFormat() {
		return audioStream.getFormat();
	}
	/**
	 * Obtener el puerto del cliente
	 */
	public int getClientPort() {
		return clientPortAudio;
	}
	/**
	 * Inicializa la musica enviandola a los usuarios
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
	 * Pausa la musica
	 */
	public void pauseMusic() {
		pause = true;
	}
	/**
	 * Continua la musica
	 */
	public void continueMusic() {
		pause = false;
	}
	/**
	 * Metodo que envia el audio a los clientes y espectadores
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
	 * Metodo que retorna el audio
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
