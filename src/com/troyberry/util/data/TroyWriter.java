package com.troyberry.util.data;

import java.io.*;
import java.util.*;

import com.troyberry.util.*;

/**
 * A class used for writing primitives to a binary array. The array can then be exported into a file. The size for a 
 * TroyWriter is unlimited, as the array makes itself larger as more bytes are being written.
 * @see ArrayList
 * @author Troy Neubauer
 *
 */
public class TroyWriter {
	
	private List<Byte> data;
	private int pointer;

	/**
	 * Creates a new TroyWriter that is ready for writing.
	 */
	public TroyWriter() {
		data = new ArrayList<Byte>(256);
		pointer = 0;
	}
	
	/**
	 * Writes a byte to the array, then increments the pointer
	 * @param b The byte to write
	 */
	public void writeByte(byte b){
		data.add(pointer, new Byte(b));
		pointer++;
	}
	
	/**
	 * Writes a short to the array, then increments the pointer by 2.\n
	 * <b>Note:</b> This method stores the most significant bytes closer to the front of the array
	 * @param s The short to write
	 */
	public void writeShort(short s){
		byte b = (byte)((s >> 8) & 0xff);
		data.add(pointer, new Byte(b));
		pointer++;
		b = (byte)((s >> 0) & 0xff);
		data.add(pointer, new Byte(b));
		pointer++;
	}
	
	/**
	 * Writes a integer to the array, then increments the pointer by 4.\n
	 * <b>Note:</b> This method stores the most significant bytes closer to the front of the array
	 * @param i The integer to write
	 */
	public void writeInt(int i){
		byte b;
		b = (byte)((i >> 24) & 0xffL);
		data.add(pointer, new Byte(b));
		pointer++;
		b = (byte)((i >> 16) & 0xffL);
		data.add(pointer, new Byte(b));
		pointer++;
		b = (byte)((i >> 8 ) & 0xffL);
		data.add(pointer, new Byte(b));
		pointer++;
		b = (byte)((i >> 0 ) & 0xffL);
		data.add(pointer, new Byte(b));
		pointer++;
	}
	
	/**
	 * Writes a long to the array, then increments the pointer by 8.\n
	 * <b>Note:</b> This method stores the most significant bytes closer to the front of the array
	 * @param l The long to write
	 */
	public void writeLong(long l){
		byte b;
		b = (byte)((l >> 56) & 0xffL);
		data.add(pointer, new Byte(b));
		pointer++;
		b = (byte)((l >> 48) & 0xffL);
		data.add(pointer, new Byte(b));
		pointer++;
		b = (byte)((l >> 40) & 0xffL);
		data.add(pointer, new Byte(b));
		pointer++;
		b = (byte)((l >> 32) & 0xffL);
		data.add(pointer, new Byte(b));
		pointer++;
		b = (byte)((l >> 24) & 0xffL);
		data.add(pointer, new Byte(b));
		pointer++;
		b = (byte)((l >> 16) & 0xffL);
		data.add(pointer, new Byte(b));
		pointer++;
		b = (byte)((l >> 8 ) & 0xffL);
		data.add(pointer, new Byte(b));
		pointer++;
		b = (byte)((l >> 0 ) & 0xffL);
		data.add(pointer, new Byte(b));
		pointer++;
	}
	
	/**
	 * Writes a double to the array, then increments the pointer by 8.\n
	 * <b>Note:</b> This method stores the most significant bytes closer to the front of the array
	 * @param d The double to write
	 */
	public void writeDouble(double d){
		writeLong(Double.doubleToLongBits(d));
	}
	
	/**
	 * Writes a float to the array, then increments the pointer by 4.\n
	 * <b>Note:</b> This method stores the most significant bytes closer to the front of the array
	 * @param f The float to writes
	 */
	public void writeFloat(float f){
		writeInt(Float.floatToIntBits(f));
	}
	
	/**
	 * Writes a string to the writer at the current position.<br>
	 * Strings are stored with 2 pices of data. An integer The pointer is updated accordingly so that it is ready to write to the next piece of data properly.
	 * @param str The String to write
	 */
	public void writeString(String str){
		byte[] bytes = str.getBytes();
		writeInt(bytes.length);
		for(byte b : bytes) {
			writeByte(b);
		}
	}
	
	/**
	 * Converts the list of bytes to an array and returns it
	 * @return An array representing all that has been written to this writer
	 */
	public byte[] getBytes(){
		byte[] result = new byte[data.size()];
		int i = 0;
		for(Byte b : data){
			result[i] = b.byteValue();
			i++;
		}
		return result;
	}
	
	/**
	 * Writes the current contents of this writer to the file specified.
	 * If the file exists, it <b>will not be appended to. </b>After this method is called, the file will have the 
	 * exact same contents as this writer.
	 * @param file The file to write to
	 * @throws IOException If an I/O error occurs.
	 */
	public void writeToFile(File file) throws IOException {
		if(file.getParentFile() != null && !file.getParentFile().exists()) {
			file.getParentFile().mkdirs();
		}
		if(file.exists()) {
			file.delete();
		}
		file.createNewFile();
		writeToFile(new FileOutputStream(file));
	}
	
	/**
	 * Writes the current contents of this writer to the file specified.
	 * If the file exists, it <b>will not be appended to. </b>After this method is called, the file will have the 
	 * exact same contents as this writer.
	 * @param file The file to write to
	 * @throws IOException If an I/O error occurs.
	 */
	public void writeToFile(OutputStream stream) throws IOException {
		stream.write(getBytes());
		stream.flush();
		stream.close();

	}

}
