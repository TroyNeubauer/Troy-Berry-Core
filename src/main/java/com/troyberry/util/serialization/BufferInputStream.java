package com.troyberry.util.serialization;

import java.io.*;

public class BufferInputStream extends InputStream {
	
	private TroyBuffer buffer;
	private long index, mark = 0;

	public BufferInputStream(TroyBuffer buffer) {
		this.buffer = buffer;
		this.index = 0;
	}

	@Override
	public int read() throws IOException {
		if(index > buffer.limit) return -1;
		return buffer.readByteImpl(index++);
	}
	
	@Override
	public int read(byte[] b, int off, int len) throws IOException {
		if(len == 0) return 0;
		int realLen = (int) Math.min(len, buffer.limit - index);
		if(realLen == 0) {
			return -1;
		}
		buffer.readBytesImpl(index, b, off, realLen);
		index += len;
		return realLen;
	}
	
	@Override
	public int available() throws IOException {
		long avilable = buffer.limit - index;
		return avilable > (long) Integer.MAX_VALUE ? Integer.MAX_VALUE : (int) avilable;
	}
	
	@Override
	public synchronized void reset() throws IOException {
		this.index = mark;
	}
	
	@Override
	public boolean markSupported() {
		return true;
	}
	
	@Override
	public long skip(long n) throws IOException {
		long skipable = Math.min(n, buffer.limit - index);
		index += skipable;
		return skipable;
	}
	@Override
	public int hashCode() {
		return buffer.hashCode();
	}

	@Override
	public synchronized void mark(int readlimit) {
		this.mark = index;
	}
	
}
