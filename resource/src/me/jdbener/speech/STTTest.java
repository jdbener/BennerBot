/*
 * This file is a work in progress if anybody would like to help out with it, speach to text would be a truly amazing feature!!
 */

package me.jdbener.speech;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import javaFlacEncoder.FLAC_FileEncoder;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.TargetDataLine;

import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;

public class STTTest implements NativeKeyListener {
		private static boolean pressed = false;
		private static String TalkKey = "b";
		private final File OUTPUTFILE = new File("audio/out.flac");
		Recorder r = new Recorder();
	
		public void nativeKeyPressed(NativeKeyEvent e) {
                //System.out.println("Key Pressed: " + NativeKeyEvent.getKeyText(e.getKeyCode()));

                if (NativeKeyEvent.getKeyText(e.getKeyCode()).equalsIgnoreCase(TalkKey)) {
                	System.out.println(TalkKey);
                	if(pressed == false){
                		try {
							r.beginRecording(OUTPUTFILE);
						} catch (IOException | LineUnavailableException e1) {
							e1.printStackTrace();
						}
                	}
                	pressed = true;
                }
        }

        public void nativeKeyReleased(NativeKeyEvent e) {
                //System.out.println("Key Released: " + NativeKeyEvent.getKeyText(e.getKeyCode()));
        	if (NativeKeyEvent.getKeyText(e.getKeyCode()).equalsIgnoreCase(TalkKey)) {
        		System.out.println("un"+TalkKey);
        		r.endRecording();
        		try {
					Thread.sleep(100);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
				String out = Flac2JSON(OUTPUTFILE);
        		System.out.println(out);
            	pressed = false;
            }
        }

        public void nativeKeyTyped(NativeKeyEvent e) {}

        public static void main(String[] args) {
        		//hide the console spam!
        		System.setErr(new PrintStream(new OutputStream(){
					@Override
					public void write(int arg0) throws IOException {
						//Do Nothing!
					}
				}));
        		
                try {
                        GlobalScreen.registerNativeHook();
                }
                catch (NativeHookException ex) {
                        System.err.println("There was a problem registering the native hook.");
                        System.err.println(ex.getMessage());

                        System.exit(1);
                }

                //Construct the example object and initialze native hook.
                GlobalScreen.getInstance().addNativeKeyListener(new STTTest());
        }
        private String Flac2JSON(File f){
        	try{
        		Path path = Paths.get("audio/out.flac");
        		byte[] data = Files.readAllBytes(path);

        		String request = "https://www.google.com/speech-api/v2/recognize?output=json&lang=en-us&key=AIzaSyBbLxCwkdJLm19XjkxU76pr1ateWxjamWo";
        		URL url = new URL(request);
        		HttpURLConnection connection = (HttpURLConnection) url.openConnection();          
        		connection.setDoOutput(true);
        		connection.setDoInput(true);
        		connection.setInstanceFollowRedirects(false);
        		connection.setRequestMethod("POST");
        		connection.setRequestProperty("Content-Type", "audio/x-flac; rate=16000");
        		connection.setRequestProperty("User-Agent", "speech2text");
        		connection.setConnectTimeout(60000);
        		connection.setUseCaches (false);

        		DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
        		wr.write(data);
        		wr.flush();
        		wr.close();
        		//connection.disconnect();

        		System.out.println("Done");

        		BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        		String decodedString;
        		String out = "";
        		while ((decodedString = in.readLine()) != null) {
        			System.out.println(decodedString);
        			out+=decodedString;
        		}
        		connection.disconnect();
        		return out;
        	}catch (IOException e){
        		e.printStackTrace();
        	}
        	return f.toString();
        }
}

/*Path path = Paths.get("audio/out.flac");
byte[] data = Files.readAllBytes(path);

String request = "https://www.google.com/speech-api/v2/recognize?output=json&lang=en-us&key=AIzaSyBbLxCwkdJLm19XjkxU76pr1ateWxjamWo";
URL url = new URL(request);
HttpURLConnection connection = (HttpURLConnection) url.openConnection();          
connection.setDoOutput(true);
connection.setDoInput(true);
connection.setInstanceFollowRedirects(false);
connection.setRequestMethod("POST");
connection.setRequestProperty("Content-Type", "audio/x-flac; rate=16000");
connection.setRequestProperty("User-Agent", "speech2text");
connection.setConnectTimeout(60000);
connection.setUseCaches (false);

DataOutputStream wr = new DataOutputStream(connection.getOutputStream ());
wr.write(data);
wr.flush();
wr.close();
connection.disconnect();

System.out.println("Done");

BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
String decodedString;
while ((decodedString = in.readLine()) != null) {
	System.out.println(decodedString);
}*/

class Recorder {
	private AudioFileFormat.Type fileType = AudioFileFormat.Type.WAVE;
	//private AudioFileFormat.Type fileType = FLACFileWriter.FLAC;
	private AudioFormat format = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, 44100, 16, 1, 2, 44100, true);
	private TargetDataLine microphone;
	private Thread session;

	/**
	 * Starts recording to the given file.
	 * @param soundFile the new .wav file in which to save the sound clip
	 * @throws IOException when the file cannot not be written
	 * @throws LineUnavailableException when the microphone is not accessible
	 */
	public synchronized void beginRecording(final File soundFile) throws IOException, LineUnavailableException {
		DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);
		if (!AudioSystem.isLineSupported(info))
			throw new IOException("Line type not supported: " + info);
		microphone = (TargetDataLine) AudioSystem.getLine(info);
		microphone.open(format, microphone.getBufferSize());
		session = new Thread() {
			public void run() {
				AudioInputStream sound = new AudioInputStream(microphone);
				microphone.start();
				try {
					File out = new File("temp.wav");
					AudioSystem.write(sound, fileType, out);
					
					FLAC_FileEncoder flacEncoder = new FLAC_FileEncoder();
			        File inputFile = out;
			        File outputFile = soundFile;

			        flacEncoder.encode(inputFile, outputFile);
			        out.delete();
				} catch (IOException e) {
					e.printStackTrace();
					System.out.println(e);
				}
			}
		};
		session.start();
	}

	/**
	 * Terminates the current recording session.
	 */
	public synchronized void endRecording() {
		microphone.stop();
		microphone.close();
		if (session != null) {
		   try {
		       session.join();
		   } catch (InterruptedException e) {
		   }
		   session = null;
		}
		notify();
	}

	/**
	 * Waits until the current recording session, if any, has terminated.
	 */
	public synchronized void waitForEnd() {
		while (session != null)
			try {
				wait();
			} catch (InterruptedException e) {}
	}
}
