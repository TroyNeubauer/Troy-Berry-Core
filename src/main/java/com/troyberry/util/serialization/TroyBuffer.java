package com.troyberry.util.serialization;

import java.io.*;
import java.nio.*;

import com.troyberry.util.*;

import sun.misc.Unsafe;

@SuppressWarnings("restriction") // We use Unsafe and we want the compiler to shut up
public abstract class TroyBuffer {

	protected static final int DEFAULT_SIZE = 1024;

	private static final Unsafe unsafe = MiscUtil.getUnsafe();
	// format:off
	// For encoding strings
	static final byte EIGHT_BIT_CHARSET = (byte) 0b1_00_00000, SIXTEEN_BIT_CHARSET = (byte) 0b0_00_00000, CHARSET_MASK = (byte) 0b1_00_00000,

			FIVE_BIT_LENGTH = (byte) 0b0_00_00000, ONE_BYTE_LENGTH = (byte) 0b0_01_00000, TWO_BYTE_LENGTH = (byte) 0b0_10_00000,
			FOUR_BYTE_LENGTH = (byte) 0b0_11_00000,

			LENGTH_SET_MASK = (byte) 0b0_11_00000, LENGTH_MASK = (byte) 0b0_00_11111;

	// format:on
	public final static TroyBuffer create(long bytes) {
		if (MiscUtil.isUnsafeSupported())
			return new TroyBufferUnsafe(bytes);
		else
			return null;
	}

	public final static TroyBuffer create(byte[] data) {
		if (MiscUtil.isUnsafeSupported()) {
			return TroyBufferUnsafe.createFromArray(data);
		}
		return null;
	}

	public final static TroyBuffer create(byte[] data, int length) {
		TroyBuffer result = TroyBuffer.create(length);
		for (int i = 0; i < length; i++) {
			result.writeByte(data[i]);
		}
		result.readPosition(0);
		return result;
	}

	public final static TroyBuffer create(File file) {
		if (MiscUtil.isUnsafeSupported())
			return TroyBufferUnsafe.createFromFile(file);
		return null;
	}

	public final static TroyBuffer create(File file, long length) {
		if (MiscUtil.isUnsafeSupported())
			return TroyBufferUnsafe.createFromFile(file, length);
		return null;
	}

	public final static TroyBuffer create(TroyBuffer buffer) {
		return create(buffer, buffer.capacity);
	}

	public final static TroyBuffer create(TroyBuffer buffer, long capacity) {
		if (MiscUtil.isUnsafeSupported()) {
			if (buffer instanceof TroyBufferUnsafe) {
				TroyBufferUnsafe result = new TroyBufferUnsafe(capacity);
				NativeTroyBufferUtil.nmemcpy(result.address, ((TroyBufferUnsafe) buffer).address, buffer.limit);
				result.limit = buffer.limit;
				result.positionRead = buffer.positionRead;
				result.positionWrite = buffer.positionWrite;
				return result;
			}
		}
		return null;
	}

	/**
	 * Copies the data from the desired ByteBuffer to a new TroyBuffer that doesn't use sun.misc.Unsafe and native memory. <br>
	 * All data from the index zero to the limit of the specified ByteBuffer will be copied
	 * 
	 * @param buffer
	 *            The buffer to copy
	 * @return A new TroyBuffer representing a copy of the desired ByteBuffer
	 */
	public static TroyBuffer createSafe(ByteBuffer buffer) {
		TroyBuffer result = new TroyBufferSafe(buffer.limit(), buffer.capacity());
		if (buffer.hasArray()) {
			result.setFromArray(buffer.array());
		} else {
			for (int i = 0; i < buffer.limit(); i++) {
				result.writeByte(i, buffer.get(i));
			}
		}
		return result;
	}

	/**
	 * Copies the data from the desired ByteBuffer to a new TroyBuffer. <br>
	 * All data from the index zero to the limit of the specified ByteBuffer will be copied
	 * 
	 * @implSpec the data will be copied using the most efficient method available, for example if the ByteBuffer is direct and the system
	 *           supports unsafe, the memory will be copied directly.
	 * @param buffer
	 *            The buffer to copy
	 * @return A new TroyBuffer representing a copy of the desired ByteBuffer
	 */
	public static TroyBuffer create(ByteBuffer buffer) {
		TroyBuffer result;
		if (MiscUtil.isUnsafeSupported()) {
			if (buffer.isDirect()) {
				result = new TroyBufferUnsafe(buffer.limit());
				NativeTroyBufferUtil.nmemcpy(result.address(), MiscUtil.address(buffer), buffer.limit());
				result.capacity = buffer.limit();
				result.limit = buffer.limit();
			} else {
				result = new TroyBufferUnsafe(0);
				if (buffer.hasArray()) {
					result.setFromArray(buffer.array());
				} else {
					for (int i = 0; i < buffer.limit(); i++) {
						result.writeByte(i, buffer.get(i));
					}
				}
			}

		} else {
			result = new TroyBufferSafe(buffer.limit(), buffer.capacity());
			if (buffer.hasArray()) {
				result.setFromArray(buffer.array());
			} else {
				for (int i = 0; i < buffer.limit(); i++) {
					result.writeByte(i, buffer.get(i));
				}
			}
		}
		return result;
	}

	public final static TroyBuffer create() {
		return create(DEFAULT_SIZE);
	}

	protected volatile long positionRead, positionWrite, limit, capacity;
	protected boolean flipRead = ByteOrder.nativeOrder() != ByteOrder.BIG_ENDIAN, flipWrite = ByteOrder.nativeOrder() != ByteOrder.BIG_ENDIAN;

	TroyBuffer(long size) {
		this.positionRead = 0;
		this.positionWrite = 0;
		this.limit = 0;
		this.capacity = size;
	}

	TroyBuffer(long size, long capacity) {
		this.positionRead = 0;
		this.positionWrite = 0;
		this.limit = size;
		this.capacity = capacity;
	}

	public abstract void clear();

	public abstract void ensureCapacity(long maxSize);

	public abstract void set(long offset, long length, byte b);

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
		ensureCapacity(
				positionWrite + 1 + (length == FOUR_BYTE_LENGTH ? 4 : (length == TWO_BYTE_LENGTH ? 2 : (length == ONE_BYTE_LENGTH ? 1 : 0))));
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

	/**
	 * Reads the next byte then increments the pointer.
	 * 
	 * @return The read byte
	 */
	public byte readByte() {
		return readByteImpl(positionRead++);
	}

	/**
	 * Reads a byte at the specified index leaving the read pointer unchanged
	 * 
	 * @param index
	 *            The index to read from
	 * @return The byte at that index
	 */
	public byte readByte(long index) {
		return readByteImpl(index);
	}

	/**
	 * Reads the next two bytes at this reader's current position, composing them into a short value, and then increments the pointer by two.
	 * 
	 * @return The read short
	 * @throws BufferUnderflowException
	 *             If there are fewer than two bytes remaining in this reader
	 */
	public short readShort() {
		short s = readShortImpl(positionRead);
		positionRead += Short.BYTES;
		return s;
	}

	/**
	 * Reads a short at the specified index leaving the read pointer unchanged
	 * 
	 * @param index
	 *            The index to read from
	 * @return The byte at that index
	 */
	public short readShort(long index) {
		return readShortImpl(index);
	}

	/**
	 * Reads the next two bytes at this reader's current position, composing them into a short value, and then increments the pointer by two.
	 * 
	 * @return The read short
	 * @throws BufferUnderflowException
	 *             If there are fewer than two bytes remaining in this reader
	 */
	public int readUnsignedShort() {
		return PrimitiveUtils.unsignedShortToInt(readShort());
	}

	/**
	 * Reads a short at the specified index leaving the read pointer unchanged
	 * 
	 * @param index
	 *            The index to read from
	 * @return The byte at that index
	 */
	public int readUnsignedShort(long index) {
		return PrimitiveUtils.unsignedShortToInt(readShort(index));
	}

	/**
	 * Reads the next two bytes at this reader's current position, composing them into a short value, and then increments the pointer by two.
	 * 
	 * @return The read short
	 * @throws BufferUnderflowException
	 *             If there are fewer than two bytes remaining in this reader
	 */
	public char readChar() {
		char s = readCharImpl(positionRead);
		positionRead += Character.BYTES;
		return s;
	}

	/**
	 * Reads a short at the specified index leaving the read pointer unchanged
	 * 
	 * @param index
	 *            The index to read from
	 * @return The byte at that index
	 */
	public char readChar(long index) {
		return readCharImpl(index);
	}

	/**
	 * Reads the next four bytes at this reader's current position, composing them into a int value, and then increments the pointer by four.
	 * 
	 * @return The read int
	 * @throws BufferUnderflowException
	 *             If there are fewer than four bytes remaining in this reader
	 */
	public int readInt() {
		int value = readIntImpl(positionRead);
		positionRead += Integer.BYTES;
		return value;
	}

	/**
	 * Reads a int at the specified index leaving the read pointer unchanged
	 * 
	 * @param index
	 *            The index to read from
	 * @return The byte at that index
	 */
	public int readInt(long index) {
		return readIntImpl(index);
	}

	/**
	 * Reads the next eight bytes at this reader's current position, composing them into a long value, and then increments the pointer by eight.
	 * 
	 * @return The read long
	 * @throws BufferUnderflowException
	 *             If there are fewer than eight bytes remaining in this reader
	 */
	public long readLong() {
		long value = readLongImpl(positionRead);
		positionRead += Long.BYTES;
		return value;
	}

	/**
	 * Reads a long at the specified index leaving the read pointer unchanged
	 * 
	 * @param index
	 *            The index to read from
	 * @return The byte at that index
	 */
	public long readLong(long index) {
		return readLongImpl(index);
	}

	/**
	 * Reads the next four bytes at this reader's current position, composing them into a float value, and then increments the pointer by four.
	 * 
	 * @return The read short
	 * @throws BufferUnderflowException
	 *             If there are fewer than four bytes remaining in this reader
	 */
	public float readFloat() {
		float value = readFloatImpl(positionRead);
		positionRead += Float.BYTES;
		return value;
	}

	/**
	 * Reads a float at the specified index leaving the read pointer unchanged
	 * 
	 * @param index
	 *            The index to read from
	 * @return The byte at that index
	 */
	public float readFloat(long index) {
		return readFloatImpl(index);
	}

	/**
	 * Reads the next eight bytes at this reader's current position, composing them into a double value, and then increments the pointer by
	 * eight.
	 * 
	 * @return The read short
	 * @throws BufferUnderflowException
	 *             If there are fewer than eight bytes remaining in this reader
	 */
	public double readDouble() {
		double value = readDoubleImpl(positionRead);
		positionRead += Double.BYTES;
		return value;
	}

	/**
	 * Reads a double at the specified index leaving the read pointer unchanged
	 * 
	 * @param index
	 *            The index to read from
	 * @return The byte at that index
	 */
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

	/**
	 * Writes the current contents of this writer to the file specified.<br>
	 * If the file exists, it <b>will be deleted and re written with the new data in this writer </b>After this method is called, the file will
	 * have the exact same contents as this writer.
	 * 
	 * @param file
	 *            The file to write to
	 * @throws IOException
	 *             If an I/O error occurs while writing to the file.
	 */
	public abstract void writeToFile(File file) throws IOException;

	public byte[] getBytes() {
		return getBytes(0, Integer.MAX_VALUE);
	}

	public abstract byte[] getBytes(long offset, int length);

	public abstract void free();

	/**
	 * Skips the reader forward the desired amount of bytes
	 * 
	 * @param bytes
	 *            the number of bytes to skip
	 */
	public void skipReader(long bytes) {
		positionRead += bytes;
	}

	/**
	 * Skips the writer forward the desired amount of bytes
	 * 
	 * @param bytes
	 *            the number of bytes to skip
	 */
	public void skipWriter(long bytes) {
		positionWrite += bytes;
	}

	public long remaining() {
		return this.limit - this.positionRead;
	}

	/**
	 * Copies data from one buffer to another
	 * 
	 * @param src
	 *            The source TroyBuffer to copy from
	 * @param srcOffset
	 *            The offset to start into the source buffer
	 * @param destOffset
	 *            The offset to start into this buffer
	 * @param bytes
	 *            The number of bytes to copy
	 */
	public abstract void copyFrom(TroyBuffer src, long srcOffset, long destOffset, long bytes);

	@Override
	public String toString() {
		return "TroyBuffer [positionRead=" + positionRead + ", positionWrite=" + positionWrite + ", size=" + limit + ", capacity=" + capacity
				+ "]";
	}

	public void setReadOrder(ByteOrder readOrder) {
		this.flipRead = readOrder != ByteOrder.nativeOrder();
	}

	public ByteOrder getReadOrder() {
		return flipRead ? (ByteOrder.nativeOrder() == ByteOrder.BIG_ENDIAN ? ByteOrder.LITTLE_ENDIAN : ByteOrder.BIG_ENDIAN)
				: ByteOrder.nativeOrder();
	}

	public void setWriteOrder(ByteOrder writeOrder) {
		this.flipWrite = writeOrder != ByteOrder.nativeOrder();
	}

	public ByteOrder getWriteOrder() {
		return flipWrite ? (ByteOrder.nativeOrder() == ByteOrder.BIG_ENDIAN ? ByteOrder.LITTLE_ENDIAN : ByteOrder.BIG_ENDIAN)
				: ByteOrder.nativeOrder();
	}

	public abstract long address();

	public abstract String getElements();

	public abstract void setFromArray(byte[] readBuffer);

	public static <T> T createInstance(Class<T> clazz) {
		if (MiscUtil.isUnsafeSupported()) {
			try {
				return (T) unsafe.allocateInstance(clazz);
			} catch (InstantiationException e) {
				return null;// If we cannot instantiate a new object using unsafe, we have no hope...

			}
		} else {// No unsafe... So use reflection
			try {
				return clazz.newInstance();
			} catch (Exception e) {

			}
			return null;
		}
	}

	/**
	 * Writes a region of the specified file into this buffer at the specified index
	 * 
	 * @param index
	 *            The index inside this buffer to begin writing to
	 * @param file
	 *            The file to write into this buffer
	 * @param offset
	 *            The offset within the file to start reading
	 * @param length
	 *            The length of the file to read
	 */
	protected abstract void writeFileImpl(long index, File file, long offset, long length) throws IOException;

	/**
	 * Writes a region of the specified file into this buffer at the specified index
	 * 
	 * @param index
	 *            The index inside this buffer to begin writing to
	 * @param file
	 *            The file to write into this buffer
	 * @param offset
	 *            The offset within the file to start reading
	 * @param length
	 *            The length of the file to read
	 */
	public void writeFile(long index, File file, long offset, long length) throws IOException {
		if (!file.exists())
			throw new FileNotFoundException("Unable to find file " + file);
		ensureCapacity(index + Long.BYTES + length);
		writeLong(length);
		writeFileImpl(index, file, offset, length);
	}

	/**
	 * Writes the contents of the specified file into this buffer at the specified index
	 * 
	 * @param index
	 *            The index inside this buffer to begin writing to
	 * @param file
	 *            The file to write into this buffer
	 */
	public void writeFile(long index, File file) throws IOException {
		writeFile(index, file, 0, file.length());
	}

	/**
	 * Writes the contents of the specified file into this buffer at the current write offset, advancing the write pointer
	 * 
	 * @param file
	 *            The file to write
	 */
	public void writeFile(File file, long offset, long length) throws IOException {
		writeFile(positionWrite, file, offset, length);
		positionWrite += length;
	}

	/**
	 * Writes the contents of the specified file into this buffer at the specified index
	 * 
	 * @param file
	 *            The file to write
	 */
	public void writeFile(File file) throws IOException {
		long length = file.length();
		writeFile(positionWrite, file, 0, length);
		positionWrite += length;
	}

	/**
	 * Reads a section of this buffer starting at index and ending at index + length, and writes it to the desired file
	 * 
	 * @param file
	 *            The file to write the data to
	 * @param index
	 *            The index to start reading the file at
	 */
	protected abstract void readFileImpl(File file, long index, long length) throws IOException;

	/**
	 * Reads a file stored in this buffer starting at index @code{index} and writes the data to the file specified
	 * 
	 * @param file
	 *            The file to write the data to
	 * @param index
	 *            The index to start reading the file at
	 */
	public long readFile(File file, long index) throws IOException {
		long lengthInBuffer = this.readLong(index);
		readFileImpl(file, index + Long.BYTES, lengthInBuffer);
		return lengthInBuffer;
	}

	/**
	 * Reads a file stored in this buffer starting the current write pointer and writes the data to the file specified
	 * 
	 * @param file
	 *            The file to write the data to
	 */
	public long readFile(File file) throws IOException {
		long bytesRead = readFile(file, positionRead);
		positionRead += bytesRead + Long.BYTES;
		return bytesRead;
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
		TroyBuffer other = (TroyBuffer) obj;
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
