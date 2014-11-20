package Launcher;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.Map;
import java.util.Scanner;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import javax.swing.JOptionPane;

import org.ho.yaml.Yaml;

public class Launcher {
	@SuppressWarnings("unchecked")
	public static void main (String[] args){
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
			System.out.println("Loaded settings");
			
			//if the revision ID of the latest build is not the same as the revision ID of the current build
			if(!latest.get("ID").toString().equalsIgnoreCase(current.get("ID").toString()))
				load = true;
			
			//if new info is to be loaded check if the user would like to download the new version
			if(load == true){
				int awnser = JOptionPane.showConfirmDialog(null, "<html><body>Would you like to download BennerBot version "+latest.get("Version").toString()+"<p><b>Changes:</b> "+latest.get("Changelog").toString()+"</p></body></html>","BennerBot ~ Update "+latest.get("Version").toString()+" Avaliable", JOptionPane.YES_NO_OPTION);
				if(awnser == JOptionPane.OK_OPTION){
					System.out.println("Awnsered yes, begining download");
					dl = true;
				}
			}
			
			//download the new info
			if(dl == true){		
			    UnzipUtility unzipper = new UnzipUtility();
			    try {
			    	System.out.println("Unziping file");
			    	unzipper.unzip(new URL(latest.get("link").toString()), new File("").getAbsolutePath());
			    	System.out.println("Successfully unziped file");
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
			    System.out.println("Successfully renamed the file");
			    
			    //update the current file
			    FileWriter r = new FileWriter(new File("current.yml"));
				r.write(StreamToString(new URL("https://raw.githubusercontent.com/jdbener/BennerBot/master/resource/latest.yml").openStream()));
				r.close();
			    JOptionPane.showMessageDialog(null, "Sucessfully Updated BennerBot");
			}
			
			//start the bot
			System.out.println("Continuing with bot loading sequence");
			//Runtime.getRuntime().exec("java -jar bot/bennerbot.jar");
			Runtime.getRuntime().exec("java -jar bennerbot.jar", null, new File("bot/"));
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