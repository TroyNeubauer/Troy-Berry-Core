package com.troyberry.util.serialization;

import java.io.*;
import java.util.Arrays;

public class BufferOutputStream extends OutputStream {

	private AbstractTroyBuffer buffer;

	public BufferOutputStream(AbstractTroyBuffer buffer) {
		this.buffer = buffer;
	}

	@Override
	public void write(int b) throws IOException {
		if (buffer == null)
			throw new IllegalStateException("Output stream has already been closed!");
		System.out.println("writing byte " + b);
		buffer.writeByte((byte) b);
	}

	@Override
	public void write(byte[] b, int off, int len) throws IOException {
		if (buffer == null)
			throw new IllegalStateException("Output stream has already been closed!");
		System.out.println("writing array " + Arrays.toString(b));
		buffer.writeBytesRaw(off, len, b);
	}

	@Override
	public void close() throws IOException {
		buffer = null;
	}
	
}
