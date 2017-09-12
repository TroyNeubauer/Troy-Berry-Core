package com.troyberry.util.serialization;

import java.io.*;
import java.util.Arrays;

public class TroyBufferSafe extends TroyBuffer {

	byte[][] bytes;

	private static final int MAX_INDEX = Integer.MAX_VALUE;

	public TroyBufferSafe(long size) {
		super(size);
		ensureCapacity(size);
	}

	public TroyBufferSafe(long size, long capacity) {
		super(size, capacity);
		ensureCapacity(capacity);
	}

	@Override
	public void clear() {
	}

	@Override
	public void ensureCapacity(long maxSize) {
		int buffersToManage = (int) (maxSize / MAX_INDEX + 1);
		if (bytes == null) {
			bytes = new byte[buffersToManage][];
		} else if (bytes.length < buffersToManage) {
			byte[][] oldBytes = bytes;
			this.bytes = new byte[buffersToManage][];
			for (int i = 0; i < oldBytes.length; i++) {
				bytes[i] = oldBytes[i];
			}
			System.out.println("resized buffer to new size of " + bytes.length);
		}
		for (int i = 0; i < buffersToManage; i++) {
			int requiredLength = (i < (buffersToManage - 1)) ? MAX_INDEX : (int) (maxSize % MAX_INDEX);
			System.out.println("buffer " + i + " required size " + requiredLength);
			if (bytes[i].length < requiredLength) {
				bytes[i] = Arrays.copyOf(bytes[i], requiredLength);
			}
		}
	}

	@Override
	public void set(long offset, long length, byte b) {
		for (long i = offset; i < (offset + length); i++) {
			writeByteImpl(i, b);
		}
	}

	@Override
	protected void writeByteImpl(long index, byte b) {
		bytes[(int) (index / MAX_INDEX)][(int) (index % MAX_INDEX)] = b;// No byte ordering issues yay!
	}

	@Override
	protected void writeShortImpl(long index, short s) {
		bytes[(int) (index / MAX_INDEX)][(int) (index % MAX_INDEX)] = flipWrite ? (byte) ((s >> 0) & 0xFF) : (byte) ((s >> 8) & 0xFF);
		index++;
		bytes[(int) (index / MAX_INDEX)][(int) (index % MAX_INDEX)] = flipWrite ? (byte) ((s >> 8) & 0xFF) : (byte) ((s >> 0) & 0xFF);
	}

	@Override
	protected void writeCharImpl(long index, char c) {
		bytes[(int) (index / MAX_INDEX)][(int) (index % MAX_INDEX)] = flipWrite ? (byte) ((c >> 0) & 0xFF) : (byte) ((c >> 8) & 0xFF);
		index++;
		bytes[(int) (index / MAX_INDEX)][(int) (index % MAX_INDEX)] = flipWrite ? (byte) ((c >> 8) & 0xFF) : (byte) ((c >> 0) & 0xFF);
	}

	@Override
	protected void writeIntImpl(long index, int i) {
		//format:off
		bytes[(int) (index / MAX_INDEX)][(int) (index % MAX_INDEX)] = flipWrite ? (byte) ((i >> 0 ) & 0xFF) : (byte) ((i >> 24) & 0xFF);
		index++;
		bytes[(int) (index / MAX_INDEX)][(int) (index % MAX_INDEX)] = flipWrite ? (byte) ((i >> 8 ) & 0xFF) : (byte) ((i >> 16) & 0xFF);
		index++;
		bytes[(int) (index / MAX_INDEX)][(int) (index % MAX_INDEX)] = flipWrite ? (byte) ((i >> 16) & 0xFF) : (byte) ((i >> 8 ) & 0xFF);
		index++;
		bytes[(int) (index / MAX_INDEX)][(int) (index % MAX_INDEX)] = flipWrite ? (byte) ((i >> 24) & 0xFF) : (byte) ((i >> 0 ) & 0xFF);
		//format:on
	}

	@Override
	protected void writeLongImpl(long index, long l) {
		//format:off
		bytes[(int) (index / MAX_INDEX)][(int) (index % MAX_INDEX)] = flipWrite ? (byte) ((l >> 0 ) & 0xFF) : (byte) ((l >> 56) & 0xFF);
		index++;
		bytes[(int) (index / MAX_INDEX)][(int) (index % MAX_INDEX)] = flipWrite ? (byte) ((l >> 8 ) & 0xFF) : (byte) ((l >> 48) & 0xFF);
		index++;
		bytes[(int) (index / MAX_INDEX)][(int) (index % MAX_INDEX)] = flipWrite ? (byte) ((l >> 16) & 0xFF) : (byte) ((l >> 40) & 0xFF);
		index++;
		bytes[(int) (index / MAX_INDEX)][(int) (index % MAX_INDEX)] = flipWrite ? (byte) ((l >> 24) & 0xFF) : (byte) ((l >> 32) & 0xFF);
		index++;
		bytes[(int) (index / MAX_INDEX)][(int) (index % MAX_INDEX)] = flipWrite ? (byte) ((l >> 32) & 0xFF) : (byte) ((l >> 24) & 0xFF);
		index++;
		bytes[(int) (index / MAX_INDEX)][(int) (index % MAX_INDEX)] = flipWrite ? (byte) ((l >> 40) & 0xFF) : (byte) ((l >> 16) & 0xFF);
		index++;
		bytes[(int) (index / MAX_INDEX)][(int) (index % MAX_INDEX)] = flipWrite ? (byte) ((l >> 48) & 0xFF) : (byte) ((l >> 8 ) & 0xFF);
		index++;
		bytes[(int) (index / MAX_INDEX)][(int) (index % MAX_INDEX)] = flipWrite ? (byte) ((l >> 56) & 0xFF) : (byte) ((l >> 0 ) & 0xFF);
		//format:on
	}

	@Override
	protected byte readByteImpl(long index) {
		return bytes[(int) (index / MAX_INDEX)][(int) (index % MAX_INDEX)];
	}

	@Override
	protected short readShortImpl(long index) {
		short s = (short) (((int) readByteImpl(index) << 8) | ((int) readByteImpl(index)));
		return flipRead ? Short.reverseBytes(s) : s;
	}

	@Override
	protected char readCharImpl(long index) {
		char c = (char) (((int) readByteImpl(index) << 8) | ((int) readByteImpl(index)));
		return flipRead ? Character.reverseBytes(c) : c;
	}

	@Override
	protected int readIntImpl(long index) {

		return 0;
	}

	@Override
	protected long readLongImpl(long index) {

		return 0;
	}

	@Override
	public void writeToFile(File file) throws IOException {
	}

	@Override
	public byte[] getBytes(long offset, int length) {

		return null;
	}

	@Override
	public void free() {
	}

	@Override
	public void copyFrom(TroyBuffer src, long srcOffset, long destOffset, long bytes) {
	}

	@Override
	public long address() {

		return 0;
	}

	@Override
	public String getElements() {

		return null;
	}

	@Override
	public void setFromArray(byte[] readBuffer) {
		clear();
		if (bytes == null) {
			bytes = new byte[1][];
		}
		if (bytes[0] == null) {
			bytes[0] = new byte[readBuffer.length];
		}
		System.arraycopy(readBuffer, 0, bytes[0], 0, readBuffer.length);
	}

}
