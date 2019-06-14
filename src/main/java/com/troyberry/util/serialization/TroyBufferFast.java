package com.troyberry.util.serialization;

import java.io.*;
import java.nio.*;
import java.util.Arrays;

import com.troyberry.util.*;

public class TroyBufferFast implements TroyBuffer {

	byte[] buffer;
	private int positionRead, positionWrite, limit, capacity;
	protected boolean bigRead = true, bigWrite = true;

	public TroyBufferFast() {
		this(128);
	}

	public TroyBufferFast(long capacity) {
		this.buffer = new byte[(int) capacity];
		this.capacity = (int) capacity;
	}

	public TroyBufferFast(byte[] data) {
		this.buffer = Arrays.copyOf(data, data.length);
		this.capacity = buffer.length;
		this.limit = data.length;
	}

	public TroyBufferFast(int limit, int capacity) {
		this.buffer = new byte[capacity];
		this.limit = limit;
	}

	@Override
	public void clear() {
		for (int i = 0; i < limit; i++) {
			buffer[i] = 0;
		}
		positionRead = 0;
		positionWrite = 0;
		limit = 0;
	}

	@Override
	public void set(long offset, long length, byte b) {
		for (int i = (int) offset; i < length + offset; i++) {
			buffer[i] = b;
		}
	}

	private void resize(int minCapacity) {
		do
			minCapacity = minCapacity << 1;
		while (minCapacity < capacity);
		buffer = Arrays.copyOf(buffer, minCapacity);
		capacity = minCapacity;
	}

	@Override
	public final void writeByte(byte b) {
		if (positionWrite + Byte.BYTES > capacity) {
			resize(positionWrite + Byte.BYTES);
		}
		buffer[positionWrite++] = b;
	}

	@Override
	public final void writeByte(long index, byte value) {
		int intIndex = (int) index;
		if (intIndex + Byte.BYTES > capacity) {
			resize(intIndex + Byte.BYTES);
		}
		buffer[intIndex++] = value;
		if (intIndex > limit)
			limit = intIndex;
	}

	@Override
	public final void writeChar(char value) {
		if (positionWrite + Character.BYTES > capacity) {
			resize(positionWrite + Character.BYTES);
		}
		final byte[] buffer = this.buffer;
		if (bigWrite) {
			buffer[positionWrite++] = (byte) (value >>> 8);
			buffer[positionWrite++] = (byte) value;
		} else {
			buffer[positionWrite++] = (byte) value;
			buffer[positionWrite++] = (byte) (value >>> 8);
		}
	}

	@Override
	public final void writeChar(long index, char value) {
		int intIndex = (int) index;
		if (intIndex + Character.BYTES > capacity) {
			resize(intIndex + Character.BYTES);
		}
		final byte[] buffer = this.buffer;
		if (bigWrite) {
			buffer[intIndex++] = (byte) (value >>> 8);
			buffer[intIndex++] = (byte) value;
		} else {
			buffer[intIndex++] = (byte) value;
			buffer[intIndex++] = (byte) (value >>> 8);
		}
		if (intIndex > limit)
			limit = intIndex;

	}

	@Override
	public final void writeShort(short value) {
		if (positionWrite + Short.BYTES > capacity) {
			resize(positionWrite + Short.BYTES);
		}
		final byte[] buffer = this.buffer;
		if (bigWrite) {
			buffer[positionWrite++] = (byte) (value >>> 8);
			buffer[positionWrite++] = (byte) value;
		} else {
			buffer[positionWrite++] = (byte) value;
			buffer[positionWrite++] = (byte) (value >>> 8);
		}
	}

	@Override
	public final void writeShort(long index, short value) {
		int intIndex = (int) index;
		if (intIndex + Short.BYTES > capacity) {
			resize(intIndex + Short.BYTES);
		}
		final byte[] buffer = this.buffer;
		if (bigWrite) {
			buffer[intIndex++] = (byte) (value >>> 8);
			buffer[intIndex++] = (byte) value;
		} else {
			buffer[intIndex++] = (byte) value;
			buffer[intIndex++] = (byte) (value >>> 8);
		}
		if (intIndex > limit)
			limit = intIndex;
	}

	@Override
	public final void writeUnsignedShort(int value) {
		if (positionWrite + Short.BYTES > capacity) {
			resize(positionWrite + Short.BYTES);
		}
		final byte[] buffer = this.buffer;
		if (bigWrite) {
			buffer[positionWrite++] = (byte) ((value & 0xFF) >>> 8);
			buffer[positionWrite++] = (byte) (value & 0xFF);
		} else {
			buffer[positionWrite++] = (byte) (value & 0xFF);
			buffer[positionWrite++] = (byte) ((value & 0xFF) >>> 8);
		}
	}

	@Override
	public final void writeUnsignedShort(long index, int value) {
		int intIndex = (int) index;
		if (intIndex + Short.BYTES > capacity) {
			resize(intIndex + Short.BYTES);
		}
		final byte[] buffer = this.buffer;
		if (bigWrite) {
			buffer[intIndex++] = (byte) ((value & 0xFF) >>> 8);
			buffer[intIndex++] = (byte) (value & 0xFF);
		} else {
			buffer[intIndex++] = (byte) (value & 0xFF);
			buffer[intIndex++] = (byte) ((value & 0xFF) >>> 8);
		}
		if (intIndex > limit)
			limit = intIndex;
	}

	@Override
	public final void writeInt(int value) {
		if (positionWrite + Integer.BYTES > capacity) {
			resize(positionWrite + Integer.BYTES);
		}
		final byte[] buffer = this.buffer;
		if (bigWrite) {
			buffer[positionWrite++] = (byte) (value >>> 24);
			buffer[positionWrite++] = (byte) (value >>> 16);
			buffer[positionWrite++] = (byte) (value >>> 8);
			buffer[positionWrite++] = (byte) value;
		} else {
			buffer[positionWrite++] = (byte) value;
			buffer[positionWrite++] = (byte) (value >>> 8);
			buffer[positionWrite++] = (byte) (value >>> 16);
			buffer[positionWrite++] = (byte) (value >>> 24);
		}
	}

	@Override
	public final void writeInt(long index, int value) {
		int intIndex = (int) index;
		if (intIndex + Integer.BYTES > capacity) {
			resize(intIndex + Integer.BYTES);
		}
		final byte[] buffer = this.buffer;
		if (bigWrite) {
			buffer[intIndex++] = (byte) (value >>> 24);
			buffer[intIndex++] = (byte) (value >>> 16);
			buffer[intIndex++] = (byte) (value >>> 8);
			buffer[intIndex++] = (byte) value;
		} else {
			buffer[intIndex++] = (byte) value;
			buffer[intIndex++] = (byte) (value >>> 8);
			buffer[intIndex++] = (byte) (value >>> 16);
			buffer[intIndex++] = (byte) (value >>> 24);
		}
		if (intIndex > limit)
			limit = intIndex;
	}

	@Override
	public final void writeLong(long value) {
		if (positionWrite + Long.BYTES > capacity) {
			resize(positionWrite + Long.BYTES);
		}
		final byte[] buffer = this.buffer;
		if (bigWrite) {
			buffer[positionWrite++] = (byte) (value >>> 56);
			buffer[positionWrite++] = (byte) (value >>> 48);
			buffer[positionWrite++] = (byte) (value >>> 40);
			buffer[positionWrite++] = (byte) (value >>> 32);
			buffer[positionWrite++] = (byte) (value >>> 24);
			buffer[positionWrite++] = (byte) (value >>> 16);
			buffer[positionWrite++] = (byte) (value >>> 8);
			buffer[positionWrite++] = (byte) value;
		} else {
			buffer[positionWrite++] = (byte) value;
			buffer[positionWrite++] = (byte) (value >>> 8);
			buffer[positionWrite++] = (byte) (value >>> 16);
			buffer[positionWrite++] = (byte) (value >>> 24);
			buffer[positionWrite++] = (byte) (value >>> 32);
			buffer[positionWrite++] = (byte) (value >>> 40);
			buffer[positionWrite++] = (byte) (value >>> 48);
			buffer[positionWrite++] = (byte) (value >>> 56);
		}
	}

	@Override
	public final void writeLong(long index, long value) {
		int intIndex = (int) index;
		if (intIndex + Long.BYTES > capacity) {
			resize(intIndex + Long.BYTES);
		}
		final byte[] buffer = this.buffer;
		if (bigWrite) {
			buffer[intIndex++] = (byte) (value >>> 56);
			buffer[intIndex++] = (byte) (value >>> 48);
			buffer[intIndex++] = (byte) (value >>> 40);
			buffer[intIndex++] = (byte) (value >>> 32);
			buffer[intIndex++] = (byte) (value >>> 24);
			buffer[intIndex++] = (byte) (value >>> 16);
			buffer[intIndex++] = (byte) (value >>> 8);
			buffer[intIndex++] = (byte) value;
		} else {
			buffer[intIndex++] = (byte) value;
			buffer[intIndex++] = (byte) (value >>> 8);
			buffer[intIndex++] = (byte) (value >>> 16);
			buffer[intIndex++] = (byte) (value >>> 24);
			buffer[intIndex++] = (byte) (value >>> 32);
			buffer[intIndex++] = (byte) (value >>> 40);
			buffer[intIndex++] = (byte) (value >>> 48);
			buffer[intIndex++] = (byte) (value >>> 56);
		}
		if (intIndex > limit)
			limit = intIndex;
	}

	@Override
	public final void writeFloat(float value) {
		writeInt(Float.floatToRawIntBits(value));
	}

	@Override
	public final void writeFloat(long index, float value) {
		writeInt(index, Float.floatToRawIntBits(value));
	}

	@Override
	public final void writeDouble(double value) {
		writeLong(Double.doubleToRawLongBits(value));
	}

	@Override
	public final void writeDouble(long index, double value) {
		writeLong(index, Double.doubleToRawLongBits(value));
	}

	@Override
	public final void writeString(String value) {
		char[] chars = value.toCharArray();
		if (Byte.BYTES + Integer.BYTES + chars.length * Character.BYTES > capacity) {
			resize(Byte.BYTES + Integer.BYTES + chars.length * Character.BYTES + positionWrite);
		}
		byte charset = EIGHT_BIT_CHARSET;
		byte length;
		for (int i = 0; i < chars.length; i++) {
			if (chars[i] > 0xFF) {
				charset = SIXTEEN_BIT_CHARSET;
				break;
			}
		}
		if (chars.length <= 0b00011111)
			length = FIVE_BIT_LENGTH;
		else if (chars.length <= 0xFF)
			length = ONE_BYTE_LENGTH;
		else if (chars.length <= 0xFFFF)
			length = TWO_BYTE_LENGTH;
		else
			length = FOUR_BYTE_LENGTH;

		byte flags = (byte) (charset | length);

		if (length == FIVE_BIT_LENGTH)
			flags |= chars.length;
		if (positionWrite + Byte.BYTES > capacity) {
			resize(positionWrite + Byte.BYTES);
		}
		writeByte(flags);// Write flags
		switch (length) {
		case FIVE_BIT_LENGTH:
			// Do nothing because the length is written in the flags
			break;
		case ONE_BYTE_LENGTH:
			writeByte(PrimitiveUtils.signedShortToUnsignedByte((short) chars.length));
			break;
		case TWO_BYTE_LENGTH:
			writeShort(PrimitiveUtils.signedIntToUnsignedShort(chars.length));
			break;
		case FOUR_BYTE_LENGTH:
			writeInt(chars.length);
			break;
		default:
			assert false;
			break;
		}

		if ((charset & CHARSET_MASK) == EIGHT_BIT_CHARSET) {// If we are using 8 bit
			for (int i = 0; i < chars.length; i++) {
				writeByte((byte) (chars[i] & 0xFF));
			}
		} else {
			for (int i = 0; i < chars.length; i++) {
				writeChar(chars[i]);
			}
		}
	}

	public final void writeString(long index, String str) {
		int intIndex = (int) index;
		char[] chars = str.toCharArray();
		if (Byte.BYTES + Integer.BYTES + chars.length * Character.BYTES > capacity) {
			resize(Byte.BYTES + Integer.BYTES + chars.length * Character.BYTES + positionWrite);
		}
		byte charset = EIGHT_BIT_CHARSET;
		byte length;
		for (int i = 0; i < chars.length; i++) {
			if (chars[i] > 0xFF) {
				charset = SIXTEEN_BIT_CHARSET;
				break;
			}
		}
		if (chars.length <= 0b00011111)
			length = FIVE_BIT_LENGTH;
		else if (chars.length <= 0xFF)
			length = ONE_BYTE_LENGTH;
		else if (chars.length <= 0xFFFF)
			length = TWO_BYTE_LENGTH;
		else
			length = FOUR_BYTE_LENGTH;

		byte flags = (byte) (charset | length);

		if (length == FIVE_BIT_LENGTH)
			flags |= chars.length;
		writeByte(intIndex++, flags);// Write flags
		switch (length) {
		case FIVE_BIT_LENGTH:
			// Do nothing because the length is written in the flags
			break;
		case ONE_BYTE_LENGTH:
			writeByte(intIndex++, PrimitiveUtils.signedShortToUnsignedByte((short) chars.length));
			break;
		case TWO_BYTE_LENGTH:
			writeShort(intIndex, PrimitiveUtils.signedIntToUnsignedShort(chars.length));
			intIndex += Short.BYTES;
			break;
		case FOUR_BYTE_LENGTH:
			writeInt(intIndex, chars.length);
			intIndex += Integer.BYTES;
			break;
		default:
			assert false;
			break;
		}

		if ((charset & CHARSET_MASK) == EIGHT_BIT_CHARSET) {// If we are using UTF 8
			for (int i = 0; i < chars.length; i++) {
				writeByte(intIndex++, (byte) (chars[i] & 0xFF));
			}
		} else {
			for (int i = 0; i < chars.length; i++) {
				writeChar(intIndex, chars[i]);
				intIndex += Character.BYTES;
			}
		}
		limit = Math.max(limit, intIndex);
	}

	@Override
	public byte readByte() {
		if (positionRead + Byte.BYTES > limit)
			throw new TroyBufferOutOfRangeException("Unable to read index " + positionRead);
		return buffer[positionRead++];
	}

	@Override
	public byte readByte(long index) {
		if (positionRead + Byte.BYTES > limit)
			throw new TroyBufferOutOfRangeException("Unable to read index " + positionRead);
		return buffer[(int) index];
	}

	@Override
	public short readShort() {
		if (positionRead + Short.BYTES > limit)
			throw new TroyBufferOutOfRangeException("Unable to read index " + positionRead);
		byte[] buffer = this.buffer;
		if (bigRead) {
			return (short) (buffer[positionRead++] << 8 | buffer[positionRead++]);
		} else {
			return (short) (buffer[positionRead++] | buffer[positionRead++] << 8);
		}
	}

	@Override
	public short readShort(long index) {
		final int intIndex = (int) index;
		if (intIndex + Short.BYTES > limit)
			throw new TroyBufferOutOfRangeException("Unable to read index " + positionRead);
		byte[] buffer = this.buffer;
		if (bigRead) {
			return (short) (buffer[intIndex] << 8 | buffer[intIndex + 1]);
		} else {
			return (short) (buffer[intIndex] | buffer[intIndex + 1] << 8);
		}
	}

	@Override
	public int readUnsignedShort() {
		if (positionRead + Short.BYTES > limit)
			throw new TroyBufferOutOfRangeException("Unable to read index " + positionRead);
		byte[] buffer = this.buffer;
		if (bigRead) {
			return buffer[positionRead++] << 8 | buffer[positionRead++];
		} else {
			return buffer[positionRead++] | buffer[positionRead++] << 8;
		}
	}

	@Override
	public int readUnsignedShort(long index) {
		final int intIndex = (int) index;
		if (intIndex + Short.BYTES > limit)
			throw new TroyBufferOutOfRangeException("Unable to read index " + positionRead);
		byte[] buffer = this.buffer;
		if (bigRead) {
			return buffer[intIndex] << 8 | buffer[intIndex + 1];
		} else {
			return buffer[intIndex] | buffer[intIndex + 1] << 8;
		}
	}

	@Override
	public char readChar() {
		if (positionRead + Character.BYTES > limit)
			throw new TroyBufferOutOfRangeException("Unable to read index " + positionRead);
		byte[] buffer = this.buffer;
		if (bigRead) {
			return (char) (buffer[positionRead++] << 8 | buffer[positionRead++]);
		} else {
			return (char) (buffer[positionRead++] | buffer[positionRead++] << 8);
		}
	}

	@Override
	public char readChar(long index) {
		final int intIndex = (int) index;
		if (intIndex + Character.BYTES > limit)
			throw new TroyBufferOutOfRangeException("Unable to read index " + positionRead);
		byte[] buffer = this.buffer;
		if (bigRead) {
			return (char) (buffer[intIndex] << 8 | buffer[intIndex + 1]);
		} else {
			return (char) (buffer[intIndex] | buffer[intIndex + 1] << 8);
		}
	}

	@Override
	public int readInt() {
		if (positionRead + Integer.BYTES > limit)
			throw new TroyBufferOutOfRangeException("Unable to read index " + positionRead);
		byte[] buffer = this.buffer;
		if (bigRead) {
			return (int) (buffer[positionRead++] << 24 | buffer[positionRead++] << 16 | buffer[positionRead++] << 8 | buffer[positionRead++]);
		} else {
			return (int) (buffer[positionRead++] | buffer[positionRead++] << 8);
		}
	}

	@Override
	public int readInt(long index) {

		return 0;
	}

	@Override
	public long readLong() {

		return 0;
	}

	@Override
	public long readLong(long index) {

		return 0;
	}

	@Override
	public float readFloat() {

		return 0;
	}

	@Override
	public float readFloat(long index) {

		return 0;
	}

	@Override
	public double readDouble() {

		return 0;
	}

	@Override
	public double readDouble(long index) {

		return 0;
	}

	public String readString() {
		byte flags = readByte();
		System.out.println("flage " + StringFormatter.toHexString(flags));
		boolean readWithUTF8 = (flags & CHARSET_MASK) == EIGHT_BIT_CHARSET;
		int length;
		byte sizeOfLength = (byte) (flags & LENGTH_SET_MASK);
		if (sizeOfLength == FIVE_BIT_LENGTH) {
			length = flags & LENGTH_MASK;
			System.out.println("five bit length");
		} else if (sizeOfLength == ONE_BYTE_LENGTH) {
			length = readByte();
			System.out.println("onebyte length");
		} else if (sizeOfLength == TWO_BYTE_LENGTH) {
			length = (int) readShort();
			System.out.println("two byte length");
		} else if (sizeOfLength == FOUR_BYTE_LENGTH) {
			length = readInt();
			System.out.println("four b length");
		} else {
			length = 0;
			System.out.println("un length");
		}
		System.out.println("length " + length);
		char[] chars = new char[length];
		if (readWithUTF8) {
			for (int i = 0; i < length; i++) {
				chars[i] = (char) (readByte() & 0xFF);
			}
		} else {
			for (int i = 0; i < length; i++) {
				chars[i] = readChar();
			}
		}
		return new String(chars);
	}

	@Override
	public final void readPosition(long positionRead) {
		this.positionRead = (int) positionRead;
	}

	@Override
	public final void writePosition(long positionWrite) {
		this.positionWrite = (int) positionWrite;
	}

	@Override
	public long readPosition() {

		return positionRead;
	}

	@Override
	public long writePosition() {

		return positionWrite;
	}

	@Override
	public long limit() {
		fixLimit();
		return limit;
	}

	@Override
	public void limit(long newLimit) {
		this.limit = (int) newLimit;
	}

	private void fixLimit() {
		limit = Math.max(limit, positionWrite);
	}

	@Override
	public long capacity() {
		return capacity;
	}

	@Override
	public final void writeToFile(File file) throws IOException {
		fixLimit();
		FileOutputStream stream = new FileOutputStream(file);
		stream.write(buffer, 0, limit);
		stream.close();
	}

	@Override
	public byte[] getBytes() {
		fixLimit();
		return getBytes(0, limit);
	}

	@Override
	public byte[] getBytes(long offset, int length) {
		fixLimit();
		if (offset + length > limit)
			throw new IndexOutOfBoundsException("length is greater than the limit! Length " + length + " limit " + limit);
		return Arrays.copyOfRange(buffer, (int) offset, (int) (offset + length));
	}

	@Override
	public void free() {
		buffer = null;
	}

	@Override
	public void skipReader(long bytes) {
		positionRead += bytes;
	}

	@Override
	public void skipWriter(long bytes) {
		positionWrite += bytes;
	}

	@Override
	public long remainingRead() {
		fixLimit();
		return limit - positionRead;
	}

	@Override
	public boolean canRead() {
		fixLimit();
		return positionRead < limit;
	}

	@Override
	public void copyFrom(TroyBuffer src, long srcOffset, long destOffset, long bytes) {
		if (src instanceof TroyBufferFast) {
			System.arraycopy(((TroyBufferFast) src).buffer, (int) srcOffset, this.buffer, (int) destOffset, (int) bytes);
		} else {
			int destOffsetInt = (int) destOffset;
			for (int i = (int) 0; i < bytes; i++) {
				buffer[i + destOffsetInt] = src.readByte(i + srcOffset);
			}
		}
	}

	public void setReadOrder(ByteOrder readOrder) {
		this.bigRead = (readOrder == ByteOrder.BIG_ENDIAN);
	}

	public ByteOrder getReadOrder() {
		return bigRead ? ByteOrder.BIG_ENDIAN : ByteOrder.LITTLE_ENDIAN;
	}

	public void setWriteOrder(ByteOrder writeOrder) {
		this.bigWrite = (writeOrder == ByteOrder.BIG_ENDIAN);
	}

	public ByteOrder getWriteOrder() {
		return bigWrite ? ByteOrder.BIG_ENDIAN : ByteOrder.LITTLE_ENDIAN;
	}

	@Override
	public long address() {

		return 0;
	}

	@Override
	public String getElements() {

		return StringFormatter.toHexString(buffer);
	}

	@Override
	public void setFromArray(byte[] array) {
		this.buffer = array.clone();
		this.positionRead = 0;
		this.positionWrite = array.length;
		this.capacity = array.length;
		this.limit = array.length;
	}

}
