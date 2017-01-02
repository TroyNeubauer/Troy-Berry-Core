package com.troyberry.util.data;

import java.util.*;

public class TroyWriter {
	
	private List<Byte> data;
	private int pointer;

	public TroyWriter() {
		data = new ArrayList<Byte>(256);
		pointer = 0;
	}
	
	public void writeByte(byte b){
		data.add(pointer, new Byte(b));
		pointer++;
	}
	
	public void writeShort(short s){
		byte b = (byte)((s >> 8) & 0xff);
		data.add(pointer, new Byte(b));
		pointer++;
		b = (byte)((s >> 0) & 0xff);
		data.add(pointer, new Byte(b));
		pointer++;
	}
	
	public void writeLong(long i){
		byte b;
		b = (byte)((i >> 56) & 0xffL);
		data.add(pointer, new Byte(b));
		pointer++;
		b = (byte)((i >> 48) & 0xffL);
		data.add(pointer, new Byte(b));
		pointer++;
		b = (byte)((i >> 40) & 0xffL);
		data.add(pointer, new Byte(b));
		pointer++;
		b = (byte)((i >> 32) & 0xffL);
		data.add(pointer, new Byte(b));
		pointer++;
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
	
	public void writeDouble(double d){
		writeLong(Double.doubleToLongBits(d));
	}
	
	public byte[] getBytes(){
		byte[] result = new byte[data.size()];
		int i = 0;
		for(Byte b : data){
			result[i] = b.byteValue();
			i++;
		}
		return result;
	}

}
