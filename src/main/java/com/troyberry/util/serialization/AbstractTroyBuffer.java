package com.troyberry.util.serialization;

import java.nio.ByteOrder;

import com.troyberry.util.*;

import sun.misc.Unsafe;

@SuppressWarnings("restriction") // We use Unsafe and we want the compiler to shut up
public abstract class AbstractTroyBuffer implements TroyBuffer {

	// Unsafe is used to allocate instances of classes
	private static final Unsafe unsafe = MiscUtil.getUnsafe();

	protected long positionRead, positionWrite, limit, capacity;
	protected boolean flipRead = ByteOrder.nativeOrder() != ByteOrder.BIG_ENDIAN, flipWrite = ByteOrder.nativeOrder() != ByteOrder.BIG_ENDIAN;

	AbstractTroyBuffer(long capacity) {
		this(0, capacity);
	}

	AbstractTroyBuffer(long limit, long capacity) {
		this.positionRead = 0;
		this.positionWrite = 0;
		this.limit = limit;
		this.capacity = capacity;
	}

	public void clear() {
		this.positionRead = 0;
		this.positionWrite = 0;
		this.limit = 0;
		clearImpl();
	}

	protected abstract void clearImpl();

	public void ensureCapacity(long maxSize) {
		if (maxSize > capacity) {
			long newCap = (capacity < 1) ? 1 : capacity;
			do
				newCap = newCap << 2;
			while (newCap < capacity);

			expand(newCap);
			this.capacity = newCap;
		}
		this.limit = limit >= maxSize ? limit : maxSize;// Inlined version of Math.max
	}

	/**
	 * Resizes the implementation for data storage to be at least {@code newSize} bytes long
	 * 
	 * @param newSize
	 *            The new size that the buffer must be after this call returns
	 */
	protected abstract void expand(long newSize);

	protected abstract void writeByteImpl(long index, byte b);

	protected abstract void writeShortImpl(long index, short s);

	protected abstract void writeCharImpl(long index, char c);

	protected abstract void writeIntImpl(long index, int i);

	protected abstract void writeLongImpl(long index, long l);

	protected final void writeFloatImpl(long index, float f) {
		ensureCapacity(index + Float.BYTES);
		writeIntImpl(index, Float.floatToRawIntBits(f));
	}

	protected final void writeDoubleImpl(long index, double d) {
		ensureCapacity(index + Double.BYTES);
		writeLongImpl(index, Double.doubleToRawLongBits(d));
	}

	public final void writeByte(byte b) {
		ensureCapacity(positionWrite + Byte.BYTES);
		writeByteImpl(positionWrite++, b);
	}

	public final void writeByte(long index, byte b) {
		ensureCapacity(index + Byte.BYTES);
		writeByteImpl(index, b);
	}

	public final void writeUnsignedByte(short b) {
		writeByte(PrimitiveUtils.signedShortToUnsignedByte(b));
	}

	public final void writeUnsignedByte(long index, short b) {
		writeByte(index, PrimitiveUtils.signedShortToUnsignedByte(b));
	}

	public final void writeChar(char c) {
		ensureCapacity(positionWrite + Character.BYTES);
		writeCharImpl(positionWrite, c);
		positionWrite += Character.BYTES;
	}

	public final void writeChar(long index, char c) {
		ensureCapacity(index + Character.BYTES);
		writeCharImpl(index, c);
	}

	public final void writeShort(short s) {
		ensureCapacity(positionWrite + Short.BYTES);
		writeShortImpl(positionWrite, s);
		positionWrite += Short.BYTES;
	}

	public final void writeShort(long index, short s) {
		ensureCapacity(index + Short.BYTES);
		writeShortImpl(index, s);
	}

	public final void writeUnsignedShort(int s) {
		writeShort(PrimitiveUtils.signedIntToUnsignedShort(s));
	}

	public final void writeUnsignedShort(long index, int s) {
		writeShort(index, PrimitiveUtils.signedIntToUnsignedShort(s));
	}

	public final void writeInt(int i) {
		ensureCapacity(positionWrite + Integer.BYTES);
		writeIntImpl(positionWrite, i);
		positionWrite += Integer.BYTES;
	}

	public final void writeInt(long index, int i) {
		ensureCapacity(index + Integer.BYTES);
		writeIntImpl(index, i);
	}

	public final void writeLong(long l) {
		ensureCapacity(positionWrite + Long.BYTES);
		writeLongImpl(positionWrite, l);
		positionWrite += Long.BYTES;
	}

	public final void writeLong(long index, long l) {
		ensureCapacity(index + Long.BYTES);
		writeLongImpl(index, l);
	}

	public final void writeFloat(float f) {
		writeInt(Float.floatToRawIntBits(f));
	}

	public final void writeFloat(long index, float f) {
		writeInt(index, Float.floatToRawIntBits(f));
	}

	public final void writeDouble(double d) {
		writeLong(Double.doubleToRawLongBits(d));
	}

	public final void writeDouble(long index, double d) {
		writeLong(index, Double.doubleToRawLongBits(d));
	}

	public void writeString(String str) {
		char[] chars = str.toCharArray();
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
		ensureCapacity(positionWrite + 1 + (length == FOUR_BYTE_LENGTH ? 4 : (length == TWO_BYTE_LENGTH ? 2 : (length == ONE_BYTE_LENGTH ? 1 : 0))));
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
			ensureCapacity(positionWrite + chars.length * Byte.BYTES);
			for (int i = 0; i < chars.length; i++) {
				writeByte((byte) (chars[i] & 0xFF));
			}
		} else {
			ensureCapacity(positionWrite + chars.length * Character.BYTES);
			for (int i = 0; i < chars.length; i++) {
				writeChar(chars[i]);
			}
		}
	}

	public void writeString(long index, String str) {
		char[] chars = str.toCharArray();
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
		ensureCapacity(index + 1 + (length == FOUR_BYTE_LENGTH ? 4 : (length == TWO_BYTE_LENGTH ? 2 : (length == ONE_BYTE_LENGTH ? 1 : 0))));
		writeByte(index++, flags);// Write flags
		switch (length) {
		case FIVE_BIT_LENGTH:
			// Do nothing because the length is written in the flags
			break;
		case ONE_BYTE_LENGTH:
			writeByte(index++, PrimitiveUtils.signedShortToUnsignedByte((short) chars.length));
			break;
		case TWO_BYTE_LENGTH:
			writeShort(index, PrimitiveUtils.signedIntToUnsignedShort(chars.length));
			index += Short.BYTES;
			break;
		case FOUR_BYTE_LENGTH:
			writeInt(index, chars.length);
			index += Integer.BYTES;
			break;
		default:
			assert false;
			break;
		}

		if ((charset & CHARSET_MASK) == EIGHT_BIT_CHARSET) {// If we are using UTF 8
			ensureCapacity(index + chars.length * Byte.BYTES);
			for (int i = 0; i < chars.length; i++) {
				writeByte(index++, (byte) (chars[i] & 0xFF));
			}
		} else {
			ensureCapacity(index + chars.length * Character.BYTES);
			for (int i = 0; i < chars.length; i++) {
				writeChar(index, chars[i]);
				index += Character.BYTES;
			}
		}
	}

	// ########## Read Operations ##########

	protected void writeBytesImpl(long index, byte[] bytes, int offset, int elements) {
		for (int i = 0; i < elements; i++) {
			writeByteImpl(i + index, bytes[i + offset]);
		}
	}

	protected void writeShortsImpl(long index, short[] shorts, int offset, int elements) {
		for (int i = 0; i < elements; i++) {
			writeShortImpl(i + index, shorts[i + offset]);
		}
	}

	protected void writeCharsImpl(long index, char[] chars, int offset, int elements) {
		for (int i = 0; i < elements; i++) {
			writeCharImpl(i + index, chars[i + offset]);
		}
	}

	protected void writeIntsImpl(long index, int[] ints, int offset, int elements) {
		for (int i = 0; i < elements; i++) {
			writeIntImpl(i + index, ints[i + offset]);
		}
	}

	protected void writeLongsImpl(long index, long[] longs, int offset, int elements) {
		for (int i = 0; i < elements; i++) {
			writeLongImpl(i + index, longs[i + offset]);
		}
	}

	protected void writeFloatsImpl(long index, float[] floats, int offset, int elements) {
		for (int i = 0; i < elements; i++) {
			writeFloatImpl(i + index, floats[i + offset]);
		}
	}

	protected void writeDoublesImpl(long index, double[] doubles, int offset, int elements) {
		for (int i = 0; i < elements; i++) {
			writeDoubleImpl(i + index, doubles[i + offset]);
		}
	}

	protected void writeBytes(long index, byte[] bytes, int offset, int elements) {
		ensureCapacity(index + elements);
		writeBytesImpl(index, bytes, offset, elements);
	}

	public void writeBytesSigned(long index, byte[] bytes, int offset, int elements) {
		ensureCapacity(index + elements + Integer.BYTES);
		writeInt(index, elements);
		index += Integer.BYTES;
		writeBytesImpl(index, bytes, offset, elements);
	}

	public void writeBytesRaw(long index, int offset, int elements, byte[] bytes) {
		writeBytes(index, bytes, offset, elements);
	}

	public void writeBytesRaw(long index, byte[] bytes) {
		writeBytes(index, bytes, 0, bytes.length);
	}

	public void writeBytesRaw(int offset, int elements, byte[] bytes) {
		writeBytes(positionWrite, bytes, offset, elements);
		positionWrite += elements;
	}

	public void writeBytesRaw(byte[] bytes) {
		writeBytes(positionWrite, bytes, 0, bytes.length);
		positionWrite += bytes.length;
	}

	public void writeBytesSigned(long index, byte[] bytes) {
		writeBytesSigned(index, bytes, 0, bytes.length);
	}

	public void writeBytesSigned(int offset, int elements, byte[] bytes) {
		writeBytesSigned(positionWrite, bytes, offset, elements);
		positionWrite += elements;
	}

	public void writeBytesSigned(byte[] bytes) {
		writeBytesSigned(positionWrite, bytes, 0, bytes.length);
		positionWrite += bytes.length + Integer.BYTES;
	}

	protected void writeShorts(long index, short[] shorts, int offset, int elements) {
		ensureCapacity(index + elements * Short.BYTES);
		writeShortsImpl(index, shorts, offset, elements);
	}

	public void writeShortsSigned(long index, short[] shorts, int offset, int elements) {
		ensureCapacity(index + elements * Short.BYTES + Integer.BYTES);
		writeInt(index, elements);
		index += Integer.BYTES;
		writeShortsImpl(index, shorts, offset, elements);
	}

	public void writeShortsRaw(long index, int offset, int elements, short[] shorts) {
		writeShorts(index, shorts, offset, elements);
	}

	public void writeShortsRaw(long index, short[] shorts) {
		writeShorts(index, shorts, 0, shorts.length);
	}

	public void writeShortsRaw(int offset, int elements, short[] shorts) {
		writeShorts(positionWrite, shorts, offset, elements);
		positionWrite += elements * Short.BYTES;
	}

	public void writeShortsRaw(short[] shorts) {
		writeShorts(positionWrite, shorts, 0, shorts.length);
		positionWrite += shorts.length * Short.BYTES;
	}

	public void writeShortsSigned(long index, short[] bytes) {
		writeShortsSigned(index, bytes, 0, bytes.length);
	}

	public void writeShortsSigned(int offset, int elements, short[] shorts) {
		writeShortsSigned(positionWrite, shorts, offset, elements);
		positionWrite += elements * Short.BYTES;
	}

	public void writeShortsSigned(short[] shorts) {
		writeShortsSigned(positionWrite, shorts, 0, shorts.length);
		positionWrite += shorts.length * Short.BYTES + Integer.BYTES;
	}

	protected void writeChars(long index, char[] chars, int offset, int elements) {
		ensureCapacity(index + elements * Character.BYTES);
		writeCharsImpl(index, chars, offset, elements);
	}

	public void writeCharsSigned(long index, char[] chars, int offset, int elements) {
		ensureCapacity(index + elements * Character.BYTES + Integer.BYTES);
		writeInt(index, elements);
		index += Integer.BYTES;
		writeCharsImpl(index, chars, offset, elements);
	}

	public void writeCharsRaw(long index, int offset, int elements, char[] chars) {
		writeChars(index, chars, offset, elements);
	}

	public void writeCharsRaw(long index, char[] chars) {
		writeChars(index, chars, 0, chars.length);
	}

	public void writeCharsRaw(int offset, int elements, char[] chars) {
		writeChars(positionWrite, chars, offset, elements);
		positionWrite += elements * Character.BYTES;
	}

	public void writeCharsRaw(char[] chars) {
		writeChars(positionWrite, chars, 0, chars.length);
		positionWrite += chars.length * Character.BYTES;
	}

	public void writeCharsSigned(long index, char[] chars) {
		writeCharsSigned(index, chars, 0, chars.length);
	}

	public void writeCharsSigned(int offset, int elements, char[] chars) {
		writeCharsSigned(positionWrite, chars, offset, elements);
		positionWrite += elements * Character.BYTES;
	}

	public void writeCharsSigned(char[] chars) {
		writeCharsSigned(positionWrite, chars, 0, chars.length);
		positionWrite += chars.length * Character.BYTES + Integer.BYTES;
	}

	protected void writeInts(long index, int[] ints, int offset, int elements) {
		ensureCapacity(index + elements * Integer.BYTES);
		writeIntsImpl(index, ints, offset, elements);
	}

	public void writeIntsSigned(long index, int[] ints, int offset, int elements) {
		ensureCapacity(index + elements * Integer.BYTES + Integer.BYTES);
		writeInt(index, elements);
		index += Integer.BYTES;
		writeIntsImpl(index, ints, offset, elements);
	}

	public void writeIntsRaw(long index, int offset, int elements, int[] ints) {
		writeInts(index, ints, offset, elements);
	}

	public void writeIntsRaw(long index, int[] ints) {
		writeInts(index, ints, 0, ints.length);
	}

	public void writeIntsRaw(int offset, int elements, int[] ints) {
		writeInts(positionWrite, ints, offset, elements);
		positionWrite += elements * Integer.BYTES;
	}

	public void writeIntsRaw(int[] ints) {
		writeInts(positionWrite, ints, 0, ints.length);
		positionWrite += ints.length * Integer.BYTES;
	}

	public void writeIntsSigned(long index, int[] ints) {
		writeIntsSigned(index, ints, 0, ints.length);
	}

	public void writeIntsSigned(int offset, int elements, int[] ints) {
		writeIntsSigned(positionWrite, ints, offset, elements);
		positionWrite += elements * Integer.BYTES;
	}

	public void writeIntsSigned(int[] ints) {
		writeIntsSigned(positionWrite, ints, 0, ints.length);
		positionWrite += ints.length * Integer.BYTES + Integer.BYTES;
	}

	protected void writeLongs(long index, long[] longs, int offset, int elements) {
		ensureCapacity(index + elements * Long.BYTES);
		writeLongsImpl(index, longs, offset, elements);
	}

	public void writeLongsSigned(long index, long[] longs, int offset, int elements) {
		ensureCapacity(index + elements * Long.BYTES + Integer.BYTES);
		writeInt(index, elements);
		index += Integer.BYTES;
		writeLongsImpl(index, longs, offset, elements);
	}

	public void writeLongsRaw(long index, int offset, int elements, long[] longs) {
		writeLongs(index, longs, offset, elements);
	}

	public void writeLongsRaw(long index, long[] longs) {
		writeLongs(index, longs, 0, longs.length);
	}

	public void writeLongsRaw(int offset, int elements, long[] longs) {
		writeLongs(positionWrite, longs, offset, elements);
		positionWrite += elements * Long.BYTES;
	}

	public void writeLongsRaw(long[] longs) {
		writeLongs(positionWrite, longs, 0, longs.length);
		positionWrite += longs.length * Long.BYTES;
	}

	public void writeLongsSigned(long index, long[] longs) {
		writeLongsSigned(index, longs, 0, longs.length);
	}

	public void writeLongsSigned(int offset, int elements, long[] longs) {
		writeLongsSigned(positionWrite, longs, offset, elements);
		positionWrite += elements * Long.BYTES;
	}

	public void writeLongsSigned(long[] longs) {
		writeLongsSigned(positionWrite, longs, 0, longs.length);
		positionWrite += longs.length * Long.BYTES + Integer.BYTES;
	}

	//
	//
	//
	//
	// READ OPERATIONS
	//
	//
	//
	//

	protected abstract byte readByteImpl(long index);

	protected abstract short readShortImpl(long index);

	protected abstract char readCharImpl(long index);

	protected abstract int readIntImpl(long index);

	protected abstract long readLongImpl(long index);

	protected float readFloatImpl(long index) {
		return Float.intBitsToFloat(readIntImpl(index));
	}

	protected double readDoubleImpl(long index) {
		return Double.longBitsToDouble(readLongImpl(index));
	}

	public byte readByte() {
		return readByteImpl(positionRead++);
	}

	public byte readByte(long index) {
		return readByteImpl(index);
	}

	public short readShort() {
		short s = readShortImpl(positionRead);
		positionRead += Short.BYTES;
		return s;
	}

	public short readShort(long index) {
		return readShortImpl(index);
	}

	@Override
	public int readUnsignedShort() {
		return PrimitiveUtils.unsignedShortToInt(readShort());
	}

	@Override
	public int readUnsignedShort(long index) {
		return PrimitiveUtils.unsignedShortToInt(readShort(index));
	}

	public char readChar() {
		char s = readCharImpl(positionRead);
		positionRead += Character.BYTES;
		return s;
	}

	public char readChar(long index) {
		return readCharImpl(index);
	}

	public int readInt() {
		int value = readIntImpl(positionRead);
		positionRead += Integer.BYTES;
		return value;
	}

	public int readInt(long index) {
		return readIntImpl(index);
	}

	public long readLong() {
		long value = readLongImpl(positionRead);
		positionRead += Long.BYTES;
		return value;
	}

	public long readLong(long index) {
		return readLongImpl(index);
	}

	public float readFloat() {
		float value = readFloatImpl(positionRead);
		positionRead += Float.BYTES;
		return value;
	}

	public float readFloat(long index) {
		return readFloatImpl(index);
	}

	public double readDouble() {
		double value = readDoubleImpl(positionRead);
		positionRead += Double.BYTES;
		return value;
	}

	public double readDouble(long index) {
		return readDoubleImpl(index);
	}

	public String readString() {
		byte flags = readByte();
		boolean readWithUTF8 = (flags & CHARSET_MASK) == EIGHT_BIT_CHARSET;
		int length;
		byte sizeOfLength = (byte) (flags & LENGTH_SET_MASK);
		if (sizeOfLength == FIVE_BIT_LENGTH)
			length = flags & LENGTH_MASK;
		else if (sizeOfLength == ONE_BYTE_LENGTH)
			length = readByte();
		else if (sizeOfLength == TWO_BYTE_LENGTH)
			length = (int) readChar();
		else if (sizeOfLength == FOUR_BYTE_LENGTH)
			length = readInt();
		else
			length = 0;

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

	/**
	 * Reads elements number of bytes bytes from the buffer starting at {@link index}, to the bytes array starting at index {@link offset}
	 * 
	 * @param index
	 *            The index to start reading at
	 * @param bytes
	 *            A byte array to copy the data from the buffer into
	 * @param offset
	 *            The offset to start writing into the byte array
	 * @param elements
	 *            The number of elements to copy into {@link bytes}
	 */
	protected void readBytesImpl(long index, byte[] bytes, int offset, int elements) {
		for (int i = 0; i < elements; i++) {
			bytes[i + offset] = readByteImpl(i + index);
		}
	}

	protected void readShortsImpl(long index, short[] shorts, int offset, int elements) {
		for (int i = 0; i < elements; i++) {
			shorts[i + offset] = readShortImpl(i + index);
		}
	}

	protected void readCharsImpl(long index, char[] chars, int offset, int elements) {
		for (int i = 0; i < elements; i++) {
			chars[i + offset] = readCharImpl(i + index);
		}
	}

	protected void readIntsImpl(long index, int[] ints, int offset, int elements) {
		for (int i = 0; i < elements; i++) {
			ints[i + offset] = readIntImpl(i + index);
		}
	}

	protected void readLongsImpl(long index, long[] longs, int offset, int elements) {
		for (int i = 0; i < elements; i++) {
			longs[i + offset] = readLongImpl(i + index);
		}
	}

	protected void readFloatsImpl(long index, float[] floats, int offset, int elements) {
		for (int i = 0; i < elements; i++) {
			floats[i + offset] = readFloatImpl(i + index);
		}
	}

	protected void readDoublesImpl(long index, double[] doubles, int offset, int elements) {
		for (int i = 0; i < elements; i++) {
			doubles[i + offset] = readDoubleImpl(i + index);
		}
	}

	public void readBytes(long index, byte[] bytes, int offset, int length) {
		if (index + length > capacity)
			throw new IndexOutOfBoundsException("Index: " + index + " length: " + length + " > buffer size: " + capacity);
		readBytesImpl(index, bytes, offset, length);
	}

	public byte[] readBytesRaw(long index, int length) {
		byte[] result = new byte[length];
		readBytes(index, result, 0, length);
		return result;
	}

	public byte[] readBytesRaw(int length) {
		byte[] result = new byte[length];
		readBytes(positionRead, result, 0, length);
		positionRead += length;
		return result;
	}

	public byte[] readBytesSigned(long index) {
		int length = readInt(index);
		index += Integer.BYTES;
		return readBytesRaw(index, length);
	}

	public byte[] readBytesSigned() {
		int length = readInt();
		byte[] bytes = readBytesRaw(positionRead, length);
		positionRead += length;
		return bytes;
	}

	public void readPosition(long positionRead) {
		this.positionRead = positionRead;
	}

	public void writePosition(long positionWrite) {
		this.positionWrite = positionWrite;
	}

	public long readPosition() {
		return positionRead;
	}

	public long writePosition() {
		return positionWrite;
	}

	public long limit() {
		return limit;
	}

	public void limit(long newLimit) {
		ensureCapacity(newLimit);
	}

	public long capacity() {
		return capacity;
	}

	public byte[] getBytes() {
		return getBytes(0, Integer.MAX_VALUE);
	}

	public void skipReader(long bytes) {
		positionRead += bytes;
	}

	public void skipWriter(long bytes) {
		positionWrite += bytes;
	}

	public long remainingRead() {
		return this.limit - this.positionRead;
	}

	@Override
	public boolean canRead() {
		return positionRead < limit;
	}
	
	@Override
	public String toString() {
		return "TroyBuffer [positionRead=" + positionRead + ", positionWrite=" + positionWrite + ", limit=" + limit + ", capacity=" + capacity + "]";
	}

	public void setReadOrder(ByteOrder readOrder) {
		this.flipRead = readOrder != ByteOrder.nativeOrder();
	}

	public ByteOrder getReadOrder() {
		return flipRead ? (ByteOrder.nativeOrder() == ByteOrder.BIG_ENDIAN ? ByteOrder.LITTLE_ENDIAN : ByteOrder.BIG_ENDIAN) : ByteOrder.nativeOrder();
	}

	public void setWriteOrder(ByteOrder writeOrder) {
		this.flipWrite = writeOrder != ByteOrder.nativeOrder();
	}

	public ByteOrder getWriteOrder() {
		return flipWrite ? (ByteOrder.nativeOrder() == ByteOrder.BIG_ENDIAN ? ByteOrder.LITTLE_ENDIAN : ByteOrder.BIG_ENDIAN) : ByteOrder.nativeOrder();
	}

	@Override
	public void copyFrom(TroyBuffer src, long srcOffset, long destOffset, long bytes) {
		if (bytes < 0 || srcOffset + bytes > src.limit() || destOffset + bytes > this.limit())
			for (int i = 0; i < bytes; i++) {
				this.writeByte(srcOffset + i, src.readByte(destOffset + i));
			}
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (capacity ^ (capacity >>> 32));
		result = prime * result + (flipRead ? 1231 : 1237);
		result = prime * result + (flipWrite ? 1231 : 1237);
		result = prime * result + (int) (limit ^ (limit >>> 32));
		result = prime * result + (int) (positionRead ^ (positionRead >>> 32));
		result = prime * result + (int) (positionWrite ^ (positionWrite >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AbstractTroyBuffer other = (AbstractTroyBuffer) obj;
		if (capacity != other.capacity)
			return false;
		if (flipRead != other.flipRead)
			return false;
		if (flipWrite != other.flipWrite)
			return false;
		if (limit != other.limit)
			return false;
		if (positionRead != other.positionRead)
			return false;
		if (positionWrite != other.positionWrite)
			return false;
		return true;
	}

}
