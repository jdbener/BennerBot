package me.jdbener.launcher;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Map;
import java.util.Scanner;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import javax.swing.JOptionPane;
import javax.swing.UIManager;

import org.ho.yaml.Yaml;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Launcher {
	public static Logger logger = LoggerFactory.getLogger(Launcher.class);	
	@SuppressWarnings("unchecked")
	public static void main (String[] args){
		try {
            UIManager.setLookAndFeel (UIManager.getSystemLookAndFeelClassName ());
        } catch (Exception e) {
        	e.printStackTrace();
        }
		try {
			boolean load = false, dl = false;
		
			//if the file hasent been run yet, download the latest information
			if(!new File("current.yml").exists()){
				load = true;
				FileWriter r = new FileWriter(new File("current.yml"));
				r.write(StreamToString(new URL("https://raw.githubusercontent.com/jdbener/BennerBot/master/resource/latest.yml").openStream()));
				r.close();
			}
			
			//load the latest information
			Map<String, Object> latest = (Map<String, Object>) Yaml.load(new URL("https://raw.githubusercontent.com/jdbener/BennerBot/master/resource/latest.yml").openStream());
			//load current information
			Map<String, Object> current = (Map<String, Object>) Yaml.load(new File("current.yml").toURI().toURL().openStream());
			logger.info("Loaded settings");
			
			//if the revision ID of the latest build is not the same as the revision ID of the current build
			if(!latest.get("ID").toString().equalsIgnoreCase(current.get("ID").toString()))
				load = true;
			
			//if new info is to be loaded check if the user would like to download the new version
			if(load == true){
				int awnser = JOptionPane.showConfirmDialog(null, "<html><body>Would you like to download BennerBot version "+latest.get("Version").toString()+"<br><b>THIS WILL OVERRIGHT ANY CONFIG CHANGES/PLUGINS YOU HAVE MAKE SURE TO BACK THOSE UP BEFORE UPDATING</b><br><p><b>Changes:</b> "+latest.get("Changelog").toString()+"</p></body></html>","BennerBot ~ Update "+latest.get("Version").toString()+" Avaliable", JOptionPane.YES_NO_OPTION);
				if(awnser == JOptionPane.OK_OPTION)
					dl = true;
			}
			
			//download the new info
			if(dl == true || !(new File("bot").exists())){
				logger.info("begining download");
			    UnzipUtility unzipper = new UnzipUtility();
			    try {
			    	logger.info("Unziping file");
			    	unzipper.unzip(new URL(latest.get("link").toString()), new File("").getAbsolutePath());
			    	logger.info("Successfully unziped file");
			    } catch (Exception ex) {
			    	// some errors occurred
			    	ex.printStackTrace();
			    }
			    
			    //rename the file
			    File temp = new File("");
			    File base = new File(temp.getAbsolutePath());
			    String[] names = base.list();
			    for(String name : names){
					temp = new File(base.toString() + "/" + name);
					if (temp.isDirectory() && name.contains("BennerBot")){
						temp.renameTo(new File("bot"));
					}
			    }
			    logger.info("Successfully renamed the file");
			    
			    //update the current file
			    FileWriter r = new FileWriter(new File("current.yml"));
				r.write(StreamToString(new URL("https://raw.githubusercontent.com/jdbener/BennerBot/master/resource/latest.yml").openStream()));
				r.close();
			    JOptionPane.showMessageDialog(null, "Sucessfully Updated BennerBot");
			}
			
			//start the bot
			logger.info("Continuing with bot loading sequence");
			ProcessBuilder pb = new ProcessBuilder("java", "-jar", "bennerbot.jar");
			pb.directory(new File("bot"));
			try {
	            Process p = pb.start();
	            LogStreamReader lsr = new LogStreamReader(p.getInputStream());
	            Thread thread = new Thread(lsr, "LogStreamReader");
	            thread.start();
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static String StreamToString(java.io.InputStream is) {
	    @SuppressWarnings("resource")
		Scanner s = new Scanner(is).useDelimiter("\\A");
	    return s.hasNext() ? s.next() : "";
	}

}
/**
 * This utility extracts files and directories of a standard zip file to
 * a destination directory.
 * @author www.codejava.net
 *
 */
class UnzipUtility {
    /**
     * Size of the buffer to read/write data
     */
    private static final int BUFFER_SIZE = 4096;
    /**
     * Extracts a zip file specified by the zipFilePath to a directory specified by
     * destDirectory (will be created if does not exists)
     * @param zipFilePath
     * @param destDirectory
     * @throws IOException
     */
    public void unzip(URL zipFilePath, String destDirectory) throws IOException {
        File destDir = new File(destDirectory);
        if (!destDir.exists()) {
            destDir.mkdir();
        }
        ZipInputStream zipIn = new ZipInputStream(zipFilePath.openStream());
        ZipEntry entry = zipIn.getNextEntry();
        // iterates over entries in the zip file
        while (entry != null) {
            String filePath = destDirectory + File.separator + entry.getName();
            if (!entry.isDirectory()) {
                // if the entry is a file, extracts it
                extractFile(zipIn, filePath);
            } else {
                // if the entry is a directory, make the directory
                File dir = new File(filePath);
                dir.mkdir();
            }
            zipIn.closeEntry();
            entry = zipIn.getNextEntry();
        }
        zipIn.close();
    }
    /**
     * Extracts a zip entry (file entry)
     * @param zipIn
     * @param filePath
     * @throws IOException
     */
    private void extractFile(ZipInputStream zipIn, String filePath) throws IOException {
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(filePath));
        byte[] bytesIn = new byte[BUFFER_SIZE];
        int read = 0;
        while ((read = zipIn.read(bytesIn)) != -1) {
            bos.write(bytesIn, 0, read);
        }
        bos.close();
    }
}
class LogStreamReader implements Runnable {

    private BufferedReader reader;

    public LogStreamReader(InputStream is) {
        this.reader = new BufferedReader(new InputStreamReader(is));
    }

    public void run() {
        try {
            String line = reader.readLine();
            while (line != null) {
                System.out.println(line);
                line = reader.readLine();
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}