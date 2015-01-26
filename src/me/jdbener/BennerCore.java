package me.jdbener;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.net.URL;
import java.util.Scanner;
/**
 * All of the utility functions i have accumulated over the years
 * @author Jdbener
 */
public class BennerCore {
	/**
	 * A simple function that capitalizes the first letter of the string pasted to it
	 * @param line the string to be capitalized
	 * @return the capitalized string
	 */
	public static String capitalize(String line){
		return Character.toUpperCase(line.charAt(0)) + line.substring(1);
	}
	/**
	 * Removes the last character from a string
	 * @param str the string to have the last character removed from
	 * @return the string with the last character removed
	 */
	public static String removeLastChar(String str) {
        return str.substring(0,str.length()-1);
    }
	/**
	 * This function will convert an input stream into a string
	 * @param is the input steam
	 * @return the string
	 */
	public static String StreamToString(java.io.InputStream is) {
	    @SuppressWarnings("resource")
		Scanner s = new Scanner(is).useDelimiter("\\A");
	    return s.hasNext() ? s.next() : "";
	}
	/**
	 * This function filters all non English symbols with the specified replacement value
	 * @param utf the string to have it's values replaced
	 * @param replacement what to replace them with
	 * @return the replaced string
	 */
	public static String filterUTF8(String utf, String replacement){
		return utf.replaceAll("[^ -~]", replacement);
	}
	/**
	 * This function filters all non English symbols and replaces them with a question mark (?)
	 * @param utf the string to have its values replaced
	 * @return the replaced string
	 */
	public static String filterUTF8(String utf){
		return filterUTF8(utf, "?");
	}
	/**
	 * this function appends a string to a file
	 * @param out the string to be appended
	 * @param f the file to append the string to
	 * @param display this makes sure not to display the text your are writing to a file
	 */
	public static void appendToFile(String out, File f){
		try {
			FileWriter writer = new FileWriter(f, true);
			writer.append(out);
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	/**
	 * this function appends a string to a file
	 * @param out the string to be appended
	 * @param f the file to append the string to
	 * @param display this makes sure not to display the text your are writing to a file
	 */
	public static void overwriteFile(String out, File f){
		try {
			f.delete();
			FileWriter writer = new FileWriter(f, true);
			writer.append(out);
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	/**
	 * returns the path to the file object passed
	 * @param f the file to be translated
	 * @return the path to the file
	 */
	public static String getPath(File f){
		try {
			return f.toURI().toURL().toString();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * returns the URL of the file object passed
	 * @param f the file to be translated
	 * @return the path to the file
	 */
	public static URL getURL(File f){
		try {
			return f.toURI().toURL();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * returns the URI of the file object passed
	 * @param f the file to be translated
	 * @return the path to the file
	 */
	public static URI getURI(File f){
		return f.toURI();
	}
	/**
	 * returns the content of the path specified
	 * @param path the path to the file
	 * @return the contents of the file
	 */
	public static String readFile(String path) {
		try {
			return StreamToString(new File(path).toURI().toURL().openStream());
		} catch (IOException e) {e.printStackTrace();}
		return null;
	}
	/**
	 * returns the content of the file specified
	 * @param path the file object of the file you want to read
	 * @return the contents of the file
	 */
	public static String readFile(File f) {
		try {
			return StreamToString(f.toURI().toURL().openStream());
		} catch (IOException e) {e.printStackTrace();}
		return null;
	}
	/**
	 * This class acts as a normal output stream, however it redirects to several different output streams
	 * @Author unknown
	 */
	public class MultiOutputStream extends OutputStream{
		OutputStream[] outputStreams;
		
		public MultiOutputStream(OutputStream... outputStreams){
			this.outputStreams= outputStreams; 
		}
		
		@Override
		public void write(int b) throws IOException{
			for (OutputStream out: outputStreams)
				out.write(b);			
		}
		
		@Override
		public void write(byte[] b) throws IOException{
			for (OutputStream out: outputStreams)
				out.write(b);
		}
	 
		@Override
		public void write(byte[] b, int off, int len) throws IOException{
			for (OutputStream out: outputStreams)
				out.write(b, off, len);
		}
	 
		@Override
		public void flush() throws IOException{
			for (OutputStream out: outputStreams)
				out.flush();
		}
	 
		@Override
		public void close() throws IOException{
			for (OutputStream out: outputStreams)
				out.close();
		}
	}
}
