package com.troyberry.util.serialization;

import static com.troyberry.util.serialization.NativeTroyBufferUtil.*;

import java.io.*;
import java.util.*;

import com.troyberry.internal.*;
import com.troyberry.util.*;

import sun.misc.Unsafe;

@SuppressWarnings("restriction")
public final class TroyBufferUnsafe extends TroyBuffer {

	protected static final Unsafe unsafe;
	protected static final boolean BIG_ENDIAN = MiscUtil.isBigEndian();
	private static final LinkedList<TroyBufferUnsafe> buffers = new LinkedList<TroyBufferUnsafe>();

	public static long getTotalNativeMemorySize() {
		long total = 0L;
		synchronized (buffers) {
			for (TroyBufferUnsafe buffer : buffers) {
				total += buffer.capacity;
			}
		}
		return total;
	}

	static {
		if (MiscUtil.isUnsafeSupported()) {
			unsafe = MiscUtil.getUnsafe();
			LibraryUtils.load();
			Runtime.getRuntime().addShutdownHook(new Thread(() -> {
				synchronized (buffers) {
					InternalLog.println("Preparing to free all native TroyBuffers (" + buffers.size() + " in all)");
					while (!buffers.isEmpty()) {
						buffers.getFirst().free();
					}
				}
			}));
		} else {
			unsafe = null;
		}
	}

	public static TroyBuffer createFromFile(File file) {
		long size = file.length();
		long address = unsafe.allocateMemory(size);
		if (address == 0)
			throw new OutOfMemoryError("Unable to allocate " + size + " bytes for file " + file + " out of off heap memory");
		address = ncreateFromFileSubset(address, file.getPath(), size);
		if (address == 0)
			throw new Error("Unable to create buffer!");
		TroyBufferUnsafe buffer = new TroyBufferUnsafe(address, size, size);
		synchronized (buffers) {
			buffers.add(buffer);
		}
		return buffer;
	}

	public static TroyBuffer createFromFile(File file, long length) {
		long fileSize = file.length();
		length = Math.max(fileSize, length);// Take the larger one in case length < file.length()
		long address = unsafe.allocateMemory(length);

		if (address == 0)
			throw new OutOfMemoryError("Unable to allocate " + length + " bytes for file " + file + " out of off heap memory");
		if (length > fileSize) {// The data after the file in memory will be uninitialized so fill it on with 0's;
			unsafe.setMemory(address + fileSize, length - fileSize, (byte) 0);
		}
		address = ncreateFromFileSubset(address, file.getPath(), length);
		if (address == 0)
			throw new Error("Unable to create buffer!");
		TroyBufferUnsafe buffer = new TroyBufferUnsafe(address, length, length);
		
		return buffer;
	}

	public static TroyBuffer createFromAddress(long address, long size) {
		return createFromAddress(address, size, size);
	}

	public static TroyBuffer createFromAddress(long address, long size, long capacity) {
		return new TroyBufferUnsafe(address, size, capacity);
	}

	public static TroyBuffer createFromArray(byte[] data) {
		TroyBufferUnsafe buffer = new TroyBufferUnsafe(data.length);
		ncpyBytesFrom(buffer.address, data, 0, data.length);
		buffer.size = data.length;
		return buffer;
	}

	public static TroyBuffer createFromArray(byte[] data, int offset, int bytes) {
		TroyBufferUnsafe buffer = new TroyBufferUnsafe(bytes);
		ncpyBytesFrom(buffer.address, data, offset, bytes);
		buffer.size = bytes;
		return buffer;
	}

	protected long address;

	TroyBufferUnsafe(long address, long size, long capacity) {
		super(size, capacity);
		this.address = address;
	}

	public TroyBufferUnsafe(long size) {
		super(size);
		this.address = unsafe.allocateMemory(size);// Allocate the requested size and store the pointer in address (like malloc in C)
		unsafe.setMemory(address, size, (byte) 0);// Set all allocated memory to 0 because it is uninitialized garbage
		synchronized (buffers) {
			buffers.add(this);
		}
	}

	public void clear() {
		this.size = 0;
		this.positionRead = 0;
		this.positionWrite = 0;
		unsafe.setMemory(address, capacity, (byte) 0);
	}

	/**
	 * Ensures that the writer
	 */
	public void ensureCapacity(long maxSize) {
		if (maxSize >= capacity) {
			long newCap = capacity;
			do
				newCap = newCap << 2;
			while (newCap < capacity);

			address = unsafe.reallocateMemory(address, newCap);// Re allocate the memory to a new size
			unsafe.setMemory(address + capacity, newCap - capacity, (byte) 0);// Set the new memory
			this.capacity = newCap;
		}
		this.size = size >= maxSize ? size : maxSize;// Inlined version of Math.max
	}

	public void set(long offset, long length, byte b) {
		ensureCapacity(offset + length);
		unsafe.setMemory(address + offset, length, b);
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
		unsafe.putByte(address + positionWrite++, flags);// Write flags
		switch (length) {
		case FIVE_BIT_LENGTH:
			// Do nothing because the length is written in the flags
			break;
		case ONE_BYTE_LENGTH:
			unsafe.putByte(address + positionWrite++, PrimitiveUtils.signedShortToUnsignedByte((short) chars.length));
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

		if ((charset & CHARSET_MASK) == EIGHT_BIT_CHARSET) {// If we are using UTF 8
			ensureCapacity(positionWrite + chars.length * Byte.BYTES);
			for (int i = 0; i < chars.length; i++) {
				unsafe.putByte(address + positionWrite++, (byte) (chars[i] & 0xFF));
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
		ensureCapacity(
				positionWrite + 1 + (length == FOUR_BYTE_LENGTH ? 4 : (length == TWO_BYTE_LENGTH ? 2 : (length == ONE_BYTE_LENGTH ? 1 : 0))));
		unsafe.putByte(address + positionWrite++, flags);// Write flags
		switch (length) {
		case FIVE_BIT_LENGTH:
			// Do nothing because the length is written in the flags
			break;
		case ONE_BYTE_LENGTH:
			unsafe.putByte(address + index++, PrimitiveUtils.signedShortToUnsignedByte((short) chars.length));
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
				unsafe.putByte(address + index++, (byte) (chars[i] & 0xFF));
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

	public String readString() {
		byte flags = unsafe.getByte(address + positionRead++);
		boolean readWithUTF8 = (flags & CHARSET_MASK) == EIGHT_BIT_CHARSET;
		int length;
		byte sizeOfLength = (byte) (flags & LENGTH_SET_MASK);
		if (sizeOfLength == FIVE_BIT_LENGTH)
			length = flags & LENGTH_MASK;
		else if (sizeOfLength == ONE_BYTE_LENGTH)
			length = unsafe.getByte(address + positionRead++);
		else if (sizeOfLength == TWO_BYTE_LENGTH)
			length = (int) readChar();
		else if (sizeOfLength == FOUR_BYTE_LENGTH)
			length = readInt();
		else
			length = 0;

		char[] chars = new char[length];
		if (readWithUTF8) {
			for (int i = 0; i < length; i++) {
				chars[i] = (char) (unsafe.getByte(address + positionRead++) & 0xFF);
			}
		} else {
			for (int i = 0; i < length; i++) {
				chars[i] = readChar();
			}
		}
		return new String(chars);
	}

	@Override
	public void writeToFile(File file) throws IOException {
		String path = file.getAbsolutePath();
		if (!nwriteToFile(path, address, size))
			throw new IOException("Unable to write to file: " + path);
	}

	public void free() {
		if (address != 0) {
			unsafe.freeMemory(address);
			this.address = 0;
			synchronized (buffers) {
				buffers.remove(this);
			}
		}
	}

	public byte[] getBytes(long offset, int length) {
		byte[] bytes = null;
		if (size < (long) length)
			length = (int) size;
		bytes = new byte[length];
		for (int i = 0; i < length; i++)
			bytes[i] = unsafe.getByte(address + i + offset);

		return bytes;
	}

	public long address() {
		return address;
	}

	@Override
	public void copyFrom(TroyBuffer src, long srcOffset, long destOffset, long bytes) {
		if (srcOffset + bytes > src.size)
			bytes = src.size - srcOffset;
		ensureCapacity(destOffset + bytes);
		nmemcpy(this.address + destOffset, src.address() + srcOffset, bytes);
	}

	@Override
	public String toString() {
		return "TroyBufferUnsafe [address=" + address + ", positionRead=" + positionRead + ", positionWrite=" + positionWrite + ", size=" + size
				+ ", capacity=" + capacity + "]";
	}

	@Override
	public String getElements() {
		int elements = (int) Math.min((long) Integer.MAX_VALUE, size);
		StringBuilder sb = new StringBuilder(elements * 6 + 2);// 6 chars per byte + 2 for []
		sb.append('[');
		for (int i = 0; i < elements; i++) {
			sb.append('0').append('x').append(StringFormatter.toHexString(readByte(i)));
			if (i != elements - 1)
				sb.append(',').append(' ');

		}
		sb.append(']');
		return sb.toString();
	}

	@Override
	protected void writeByteImpl(long index, byte b) {
		unsafe.putByte(address + index, b);
	}

	@Override
	protected void writeShortImpl(long index, short s) {
		unsafe.putShort(address + index, flipWrite ? Short.reverseBytes(s) : s);
	}

	@Override
	protected void writeCharImpl(long index, char c) {
		unsafe.putChar(address + index, flipWrite ? Character.reverseBytes(c) : c);
	}

	@Override
	protected void writeIntImpl(long index, int i) {
		unsafe.putInt(address + index, flipWrite ? Integer.reverseBytes(i) : i);
	}

	@Override
	protected void writeLongImpl(long index, long l) {
		unsafe.putLong(address + index, flipWrite ? Long.reverseBytes(l) : l);
	}

	@Override
	protected byte readByteImpl(long index) {
		return unsafe.getByte(address + index);
	}

	@Override
	protected short readShortImpl(long index) {
		return flipRead ? Short.reverseBytes(unsafe.getShort(address + index)) : unsafe.getShort(address + index);
	}

	@Override
	protected char readCharImpl(long index) {
		return flipRead ? Character.reverseBytes(unsafe.getChar(address + index)) : unsafe.getChar(address + index);
	}

	@Override
	protected int readIntImpl(long index) {
		return flipRead ? Integer.reverseBytes(unsafe.getInt(address + index)) : unsafe.getInt(address + index);
	}

	@Override
	protected long readLongImpl(long index) {
		return flipRead ? Long.reverseBytes(unsafe.getLong(address + index)) : unsafe.getLong(address + index);
	}

	@Override
	public void writeBytesImpl(long index, byte[] bytes, int offset, int elements) {
		ncpyBytesFrom(address + index, bytes, offset, elements);
	}

	@Override
	protected void writeShortsImpl(long index, short[] shorts, int offset, int elements) {
		ncpyShortsFrom(address + index, shorts, offset, elements, flipWrite);
	}

	@Override
	protected void writeCharsImpl(long index, char[] chars, int offset, int elements) {
		ncpyCharsFrom(address + index, chars, offset, elements, flipWrite);
	}

	@Override
	protected void writeIntsImpl(long index, int[] ints, int offset, int elements) {
		ncpyIntsFrom(address + index, ints, offset, elements, flipWrite);
	}

	@Override
	protected void writeLongsImpl(long index, long[] longs, int offset, int elements) {
		ncpyLongsFrom(address + index, longs, offset, elements, flipWrite);
	}

	@Override
	protected void writeFloatsImpl(long index, float[] floats, int offset, int elements) {
		ncpyFloatsFrom(address + index, floats, offset, elements, flipWrite);
	}

	@Override
	protected void writeDoublesImpl(long index, double[] doubles, int offset, int elements) {
		ncpyDoublesFrom(address + index, doubles, offset, elements, flipWrite);
	}

	@Override
	protected void readBytesImpl(long index, byte[] bytes, int offset, int elements) {
		ncpyBytesTo(bytes, address + index, offset, elements);
	}

	@Override
	protected void readShortsImpl(long index, short[] shorts, int offset, int elements) {
		ncpyShortsTo(shorts, address + index, offset, elements, flipRead);
	}

	@Override
	protected void readCharsImpl(long index, char[] chars, int offset, int elements) {
		ncpyCharsTo(chars, address + index, offset, elements, flipRead);
	}

	@Override
	protected void readIntsImpl(long index, int[] ints, int offset, int elements) {
		ncpyIntsTo(ints, address + index, offset, elements, flipRead);
	}

	@Override
	protected void readLongsImpl(long index, long[] longs, int offset, int elements) {
		ncpyLongsTo(longs, address + index, offset, elements, flipRead);
	}

	@Override
	protected void readFloatsImpl(long index, float[] floats, int offset, int elements) {
		ncpyFloatsTo(floats, address + index, offset, elements, flipRead);
	}

	@Override
	protected void readDoublesImpl(long index, double[] doubles, int offset, int elements) {
		ncpyDoublesTo(doubles, address + index, offset, elements, flipRead);
	}

	@Override
	public void setFromArray(byte[] readBuffer) {
		this.clear();
		ensureCapacity(readBuffer.length);
		ncpyBytesFrom(address, readBuffer, 0, readBuffer.length);
	}
}
