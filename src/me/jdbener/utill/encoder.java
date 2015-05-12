package me.jdbener.utill;

public class encoder{
	public static void main (String[] args){
		System.out.println(encode("jdbener", 1000, 1000000000));
	}
	public static String encode (String in, int con, int length){
		String out = in;
		for(int i = 0; i < con; i++){
			out = new String(encode((out+i+con).getBytes()));
			//System.out.println(i+": "+out);
		}
		int value = Math.abs((con/2)-(length+1));
		if(value>out.length()||value<0)value = 0;
		if(length>out.length())length=out.length()-1;
	    return out.substring(value, value+length).trim();
	}
	private static byte[] encode(byte[] in){
		final byte[] secret = "`~1!2@3#4$5%6^7&8*9(0)-_=+qQwWeErRtTyYuUiIoOpP[{]}aAsSdDfFgGhHjJkKlL;:'zZxXcCvVbBnNmM".getBytes();
		final byte[] output = new byte[in.length];
		int spos = 0;
		for (int pos = 0; pos < in.length; ++pos) {
			output[pos] = (byte) (in[pos] ^ secret[spos]);
			++spos;
			if (spos >= secret.length) {
				spos = 0;
			}
		}
		return output;
	}
}