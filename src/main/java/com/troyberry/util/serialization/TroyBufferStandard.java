package com.troyberry.util.serialization;

import java.io.*;
import java.util.Arrays;

public class TroyBufferStandard extends AbstractTroyBuffer {
	private static final int MAX_BYTES = Integer.MAX_VALUE - 100;
	private static final boolean CHECKS = true;

	byte[] data;

	public TroyBufferStandard(long capacity) {
		super(capacity);
		checkLong(capacity);
		data = new byte[(int) capacity];

	}

	
	
	public TroyBufferStandard(long limit, long capacity) {
		super(limit, capacity);
		checkLong(capacity);
		data = new byte[(int) capacity];
	}



	@Override
	public void expand(long newCapacity) {
		checkLong(newCapacity);
		data = Arrays.copyOf(data, (int) newCapacity);
	}

	private static final void checkLong(long value) {
		if (value > MAX_BYTES)
			throw new IllegalArgumentException("new capacity is too large for an array with 32 bit indexing!");
	}

	@Override
	public void set(long offset, long length, byte b) {
		if (CHECKS) {
			checkLong(offset);
			checkLong(length);
		}
		for (int i = 0; i < capacity; i++)
			data[i] = b;
	}

	@Override
	protected void writeByteImpl(long index, byte b) {
		if (CHECKS)
			checkLong(index);
	}

	@Override
	protected void writeShortImpl(long index, short s) {
		if (CHECKS)
			checkLong(index);
	}

	@Override
	protected void writeCharImpl(long index, char c) {
		if (CHECKS)
			checkLong(index);
	}

	@Override
	protected void writeIntImpl(long index, int i) {
		if (CHECKS)
			checkLong(index);
	}

	@Override
	protected void writeLongImpl(long index, long l) {
		if (CHECKS)
			checkLong(index);
	}

	@Override
	protected byte readByteImpl(long index) {
		checkMaxReadIndex(index + Byte.BYTES);

		return data[(int) index];
	}

	private final void checkMaxReadIndex(long index) {
		if (index > limit)
			throw new IndexOutOfBoundsException("max index " + index);
	}

	@Override
	protected short readShortImpl(long index) {
		checkMaxReadIndex(index + Short.BYTES);
		return 0;
	}

	@Override
	protected char readCharImpl(long index) {
		checkMaxReadIndex(index + Character.BYTES);
		return 0;
	}

	@Override
	protected int readIntImpl(long index) {
		checkMaxReadIndex(index + Integer.BYTES);
		final int intIndex = (int) index;
		byte[] data = this.data;
		if(flipRead) {
			return (int) data[intIndex + 3] << 24 | (int) data[intIndex + 2] << 16 | (int) data[intIndex + 1] << 8 | (int) data[intIndex + 0] << 0;
		} else {
			return (int) data[intIndex + 0] << 24 | (int) data[intIndex + 1] << 16 | (int) data[intIndex + 2] << 8 | (int) data[intIndex + 3] << 0;
		}
	}

	@Override
	protected long readLongImpl(long index) {
		checkMaxReadIndex(index + Long.BYTES);
		final int intIndex = (int) index;
		long l = (long) ((long) data[intIndex + 0] << 56 | (long) data[intIndex + 1] << 48 | (long) data[intIndex + 2] << 40 | (long) data[intIndex + 3] << 32 | (long) data[intIndex + 4] << 24
				| (long) data[intIndex + 5] << 16 | (long) data[intIndex + 6] << 8 | (long) data[intIndex + 7] << 0);
		return flipRead ? Long.reverseBytes(l) : l;
	}

	@Override
	public void writeToFile(File file) throws IOException {
		FileOutputStream stream = new FileOutputStream(file);
		stream.write(data, (int) 0, (int) limit);
		stream.close();
	}

	@Override
	public byte[] getBytes(long offset, int length) {
		checkLong(offset);
		checkLong(length);
		return Arrays.copyOfRange(data, (int) offset, (int) (offset + length));
	}

	@Override
	public void free() {
		data = null;
	}

	@Override
	public long address() {

		return 0;
	}

	@Override
	public String getElements() {

		return Arrays.toString(data);
	}

	@Override
	public void setFromArray(byte[] readBuffer) {
		ensureCapacity(readBuffer.length);
		System.arraycopy(readBuffer, 0, data, 0, readBuffer.length);
	}

	@Override
	public void copyFrom(TroyBuffer src, long srcOffset, long destOffset, long bytes) {
		if (CHECKS) {
			checkLong(srcOffset);
			checkLong(destOffset);
			checkLong(bytes);
		}
		if (src instanceof TroyBufferStandard) {
			System.arraycopy(((TroyBufferStandard) src).data, (int) srcOffset, this.data, (int) destOffset, (int) bytes);
		} else {
			super.copyFrom(src, srcOffset, destOffset, bytes);
		}
	}

	@Override
	protected void clearImpl() {
		for (int i = 0; i < capacity; i++)
			data[i] = 0;
	}



}
