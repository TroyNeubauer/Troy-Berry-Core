package com.troyberry.util.data;

import java.awt.image.*;
import java.io.*;
import java.nio.*;

import com.troyberry.util.*;

public class TroyReader {
	
	private ByteBuffer buffer;
	
	public void set(byte[] data) {
		buffer = ByteBuffer.wrap(data);
	}

	public TroyReader(byte[] data) {
		set(data);
	}
	
	public TroyReader(InputStream stream) throws IOException {
		byte[] data = new byte[stream.available()];
		stream.read(data);
		stream.close();
		set(data);
	}
	
	public TroyReader(File file) throws IOException {
		byte[] data = new byte[(int) file.length()];
		FileInputStream stream = new FileInputStream(file);
		stream.read(data);
		set(data);
	}
	
	public TroyReader(TroyWriter writer) {
		set(writer.getBytes());
	}
	
	public byte readByte(){
		return buffer.get();
	}
	
	
	public short readShort(){
		return buffer.getShort();
	}
	
	public int readInt(){
		return buffer.getInt();
	}
	
	public long readLong() {
		return buffer.getLong();
	}
	
	public double readDouble() {
		return buffer.getDouble();
	}
	
	/**
	 * Skips the reader forward by x amount of bytes.<br>
	 * If the new position is larger than the reader's size, an IllegalArgumentException will be thrown
	 * @param bytes The amount of bytes to skip
	 * @throws IllegalArgumentException Is the new position is larger than the size
	 */
	public void skip(int bytes) {
		buffer.position(buffer.position() + bytes);
	}
	
	/**
	 * Skips the reader backward by x amount of bytes.<br>
	 * If the new position is less than than 0, an IllegalArgumentException will be thrown
	 * @param bytes The amount of bytes to skip
	 * @throws IllegalArgumentException Is the new position is larger than the size
	 */
	public void rewind(int bytes) {
		buffer.position(buffer.position() - bytes);
	}
	
    /**
     * Tells whether there are any elements between the current position and
     * the limit.
     *
     * @return  <tt>true</tt> if, and only if, there is at least one element
     *          remaining in this buffer
     */
	public boolean hasRemaining() {
		return buffer.hasRemaining();
	}
	
	/**
	 * Returns the number of elements between the current position and the limit.
	 * @return The number of elements remaining in this reader
	 */
	public int remaining() {
		return buffer.remaining();
	}
	
	/**
	 * Reads a string from the current position in the reader. <br>
	 * First, an integer is read, this should be the length of the string.
	 * Next the reader reads the length of the string, converts it to a string and returns.<br>
	 * Problems arise if the where buffer's current position was never where a string was written to. 
	 * In which case, an IllegalStateException will be thrown indicating that the string attempting to be read 
	 * is not a valid string. Make sure that there is a string written to the same position as the current offset.
	 * @return The read string
	 * @see TroyWriter#writeString(String)
	 */
	public String readString() {
		int bytes = readInt();
		if((long)bytes + buffer.position() >= buffer.capacity()) throw new IllegalStateException
		("The \"String\" being read is longer than the buffer!\nThe String should've started at " + (buffer.position() - 4) + " string length " + bytes);
		byte[] data = new byte[bytes];
		for(int i = 0; i < bytes; i++) {
			data[i] = readByte();
		}
		return new String(data);
	}
	
	/**
	 * Returns how far the reader is into the data
	 * @return The offset in bytes from the start
	 */
	public int getPointer() {
		return buffer.position();
	}
	
	/**
	 * Moves the pointer of the reader to the desired position. All read calls after this will read from this new pointer.
	 * @param newPointer The new pointer to use
	 */
	public void setPointer(int newPointer){
		buffer.position(newPointer);
	}

}
