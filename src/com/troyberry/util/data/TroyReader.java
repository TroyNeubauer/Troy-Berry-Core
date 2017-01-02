package com.troyberry.util.data;

import com.troyberry.util.data.*;

public class TroyReader {
	
	private byte[] data;
	private int pointer;

	public TroyReader(byte[] data) {
		this.data = data;
	}
	
	public TroyReader(TroyWriter writer) {
		data = writer.getBytes();
	}
	
	public TroyReader(TroyReader reader) {
		this.data = new byte[reader.data.length];
		System.arraycopy(reader.data, 0, this.data, 0, reader.data.length);
		this.pointer = reader.pointer;
	}
	
	public byte readByte(){
		byte result = data[pointer];
		pointer++;
		return result;
	}
	
	
	public short readShort(){
		short result = (short)((((0xff & data[pointer + 0]) << 8) | 
							  ((  0xff & data[pointer + 1]) << 0)) & 0x0000ffff);
		pointer += Short.BYTES;
		return result;
	}
	
	public int readInt(){
		int result = ((0xff & data[pointer + 0]) << 24) | ((0xff & data[pointer + 1]) << 16) |
				     ((0xff & data[pointer + 2]) << 8 ) | ((0xff & data[pointer + 3]) << 0 );
		pointer += Integer.BYTES;
		return result;
	}
	
	public long readLong(){
		long result = 
				((0xff & data[pointer + 0]) << 56L) | ((0xff & data[pointer + 1]) << 48L) | 
				((0xffL & data[pointer + 2]) << 40L) | ((0xffL & data[pointer + 3]) << 32L) | 
				((0xffL & data[pointer + 4]) << 24L) | ((0xffL & data[pointer + 5]) << 16L) |
				((0xffL & data[pointer + 6]) << 8L ) | ((0xffL & data[pointer + 7]) << 0L );
		pointer += Integer.BYTES;
		return result;
	}
	
	public double readDouble(){
		return Double.longBitsToDouble(readLong());
	}
	
	public int getPointer() {
		return pointer;
	}
	
	public void setPointer(int newPointer){
		this.pointer = newPointer;
	}

}
