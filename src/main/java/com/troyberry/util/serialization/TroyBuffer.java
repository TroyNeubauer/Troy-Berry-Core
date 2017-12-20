package com.troyberry.util.serialization;

import java.io.*;
import java.nio.*;

/**
 * Represents a linear container for binary data. All types of primitives plus strings can be read and written to a buffer. A buffer's main
 * components are its capacity, limit, readPosition and writePosition <blockquote>
 *
 * <p>
 * A buffer's <i>capacity</i> is the number of elements it contains. The capacity of a buffer is never negative, and may change over time
 * depending on the implementation
 * </p>
 *
 * <p>
 * A buffer's <i>limit</i> is the index of the first element that should not be read or written. A buffer's limit is never negative and is never
 * greater than its capacity.
 * </p>
 *
 * <p>
 * A buffer's <i>readPosition</i> is the index of the next element to be read. A buffer's readPosition is never negative and is never greater
 * than its limit.
 * </p>
 * *
 * <p>
 * A buffer's <i>writePosition</i> is the index of the next element to be written. A buffer's writePosition is never negative and is never
 * greater than its limit.
 * </p>
 *
 * </blockquote>
 * 
 * @author Troy Neubauer
 *
 */
public interface TroyBuffer {

	// format:off
	// For encoding strings
	static final byte EIGHT_BIT_CHARSET = (byte) 0b1_00_00000, SIXTEEN_BIT_CHARSET = (byte) 0b0_00_00000, CHARSET_MASK = (byte) 0b1_00_00000,

			FIVE_BIT_LENGTH =  (byte) 0b0_00_00000, ONE_BYTE_LENGTH = (byte) 0b0_01_00000, TWO_BYTE_LENGTH = (byte) 0b0_10_00000,
			FOUR_BYTE_LENGTH = (byte) 0b0_11_00000,

			LENGTH_SET_MASK = (byte) 0b0_11_00000, LENGTH_MASK = (byte) 0b0_00_11111;

	// format:on
	
	static final int DEFAULT_SIZE = 1024;

	/**
	 * Clears this buffer's content and assigns {@code positionWrite} {@code positionRead} and {@code limit} to zero
	 */
	public void clear();

	/**
	 * Sets all bytes in the range [offset, offset + length) to the desired value
	 * 
	 * @param offset
	 *            The offset to start writing at
	 * @param length
	 *            THe number of bytes to set to the chosen value
	 * @param value
	 *            The value to assign
	 */
	public void set(long offset, long length, byte value);

	/**
	 * Writes one byte then increments the writer pointer
	 * 
	 * @param value
	 *            The value to write
	 */
	public void writeByte(byte value);

	/**
	 * Writes one byte at the desired index leaving the write pointer unchanged
	 * 
	 * @param index
	 *            the index to write to
	 * @param value
	 *            The value to write
	 */
	public void writeByte(long index, byte value);

	/**
	 * Writes two bytes consisting of the char's value, then increments the writer pointer by two
	 * 
	 * @param value
	 *            The value to write
	 */
	public void writeChar(char value);

	/**
	 * Writes two bytes consisting of the char's value, leaving the write pointer unchanged
	 * 
	 * @param index
	 *            the index to write to
	 * @param value
	 *            The value to write
	 */
	public void writeChar(long index, char value);

	/**
	 * Writes two bytes consisting of the short's value, then increments the writer pointer by two
	 * 
	 * @param value
	 *            The value to write
	 */
	public void writeShort(short value);

	/**
	 * Writes two bytes consisting of the short's value, leaving the write pointer unchanged
	 * 
	 * @param index
	 *            the index to write to
	 * @param value
	 *            The value to write
	 */
	public void writeShort(long index, short value);
	
	/**
	 * Writes two bytes consisting of the two low bits of the int's unsigned value, then increments the writer pointer by two
	 * 
	 * @param value
	 *            The value to write
	 */
	public void writeUnsignedShort(int value);

	/**
	 * Writes two bytes consisting of the two low bits of the int's unsigned value, leaving the write pointer unchanged
	 * 
	 * @param index
	 *            the index to write to
	 * @param value
	 *            The value to write
	 */
	public void writeUnsignedShort(long index, int value);

	/**
	 * Writes four bytes consisting of the int's value, then increments the writer pointer by four
	 * 
	 * @param value
	 *            The value to write
	 */
	public void writeInt(int value);

	/**
	 * Writes four bytes consisting of the int's value, leaving the write pointer unchanged
	 * 
	 * @param index
	 *            the index to write to
	 * @param value
	 *            The value to write
	 */
	public void writeInt(long index, int value);

	/**
	 * Writes eight bytes consisting of the long's value, then increments the writer pointer by eight
	 * 
	 * @param value
	 *            The value to write
	 */
	public void writeLong(long value);

	/**
	 * Writes eight bytes consisting of the long's value, leaving the write pointer unchanged
	 * 
	 * @param index
	 *            the index to write to
	 * @param value
	 *            The value to write
	 */
	public void writeLong(long index, long value);

	/**
	 * Writes four bytes consisting of the float's value, then increments the writer pointer by four
	 * 
	 * @param value
	 *            The value to write
	 */
	public void writeFloat(float value);

	/**
	 * Writes four bytes consisting of the float's value, leaving the write pointer unchanged
	 * 
	 * @param index
	 *            the index to write to
	 * @param value
	 *            The value to write
	 */
	public void writeFloat(long index, float value);

	/**
	 * Writes eight bytes consisting of the double's value, then increments the writer pointer by eight
	 * 
	 * @param value
	 *            The value to write
	 */
	public void writeDouble(double value);

	/**
	 * Writes eight bytes consisting of the double's value, leaving the write pointer unchanged
	 * 
	 * @param index
	 *            the index to write to
	 * @param value
	 *            The value to write
	 */
	public void writeDouble(long index, double value);

	/**
	 * Writes a string to this buffer incrementing the write pointer by the number of bytes written. The string may be written in any charset or
	 * format so long as a matching call to {@link TroyBuffer#readString()} returns a string with the same content as the parameter
	 * 
	 * @param value
	 *            The string to write
	 */
	public void writeString(String value);

	/**
	 * Writes a string to this buffer at the desired index. The string may be written in any charset or format so long as a matching call to
	 * {@link TroyBuffer#readString()} returns a string with the same content as the parameter
	 * 
	 * @param value
	 *            The string to write
	 */
	public void writeString(long index, String value);

	/**
	 * Reads the next byte then increments the pointer.
	 * 
	 * @return The read byte
	 */
	public byte readByte();

	/**
	 * Reads a byte at the specified index leaving the read pointer unchanged
	 * 
	 * @param index
	 *            The index to read from
	 * @return The byte at that index
	 */
	public byte readByte(long index);

	/**
	 * Reads the next two bytes at this reader's current position, composing them into a short value, and then increments the pointer by two.
	 * 
	 * @return The read short
	 * @throws BufferUnderflowException
	 *             If there are fewer than two bytes remaining in this reader
	 */
	public short readShort();

	/**
	 * Reads a short at the specified index leaving the read pointer unchanged
	 * 
	 * @param index
	 *            The index to read from
	 * @return The byte at that index
	 */
	public short readShort(long index);
	
	/**
	 * Reads the next two bytes at this reader's current position, composing them into an unsigned int value, and then increments the pointer by two.
	 * 
	 * @return The read unsigned short
	 * @throws BufferUnderflowException
	 *             If there are fewer than two bytes remaining in this reader
	 */
	public int readUnsignedShort();

	/**
	 * Reads an unsigned short (two bytes) at the specified index leaving the read pointer unchanged
	 * 
	 * @param index
	 *            The index to read from
	 * @return The unsigned short at that index
	 */
	public int readUnsignedShort(long index);

	/**
	 * Reads the next two bytes at this reader's current position, composing them into a short value, and then increments the pointer by two.
	 * 
	 * @return The read short
	 * @throws BufferUnderflowException
	 *             If there are fewer than two bytes remaining in this reader
	 */
	public char readChar();

	/**
	 * Reads a short at the specified index leaving the read pointer unchanged
	 * 
	 * @param index
	 *            The index to read from
	 * @return The byte at that index
	 */
	public char readChar(long index);

	/**
	 * Reads the next four bytes at this reader's current position, composing them into a int value, and then increments the pointer by four.
	 * 
	 * @return The read int
	 * @throws BufferUnderflowException
	 *             If there are fewer than four bytes remaining in this reader
	 */
	public int readInt();

	/**
	 * Reads a int at the specified index leaving the read pointer unchanged
	 * 
	 * @param index
	 *            The index to read from
	 * @return The byte at that index
	 */
	public int readInt(long index);

	/**
	 * Reads the next eight bytes at this reader's current position, composing them into a long value, and then increments the pointer by eight.
	 * 
	 * @return The read long
	 * @throws BufferUnderflowException
	 *             If there are fewer than eight bytes remaining in this reader
	 */
	public long readLong();

	/**
	 * Reads a long at the specified index leaving the read pointer unchanged
	 * 
	 * @param index
	 *            The index to read from
	 * @return The byte at that index
	 */
	public long readLong(long index);

	/**
	 * Reads the next four bytes at this reader's current position, composing them into a float value, and then increments the pointer by four.
	 * 
	 * @return The read short
	 * @throws BufferUnderflowException
	 *             If there are fewer than four bytes remaining in this reader
	 */
	public float readFloat();

	/**
	 * Reads a float at the specified index leaving the read pointer unchanged
	 * 
	 * @param index
	 *            The index to read from
	 * @return The byte at that index
	 */
	public float readFloat(long index);

	/**
	 * Reads the next eight bytes at this reader's current position, composing them into a double value, and then increments the pointer by eight.
	 * 
	 * @return The read short
	 * @throws BufferUnderflowException
	 *             If there are fewer than eight bytes remaining in this reader
	 */
	public double readDouble();

	/**
	 * Reads a double at the specified index leaving the read pointer unchanged
	 * 
	 * @param index
	 *            The index to read from
	 * @return The byte at that index
	 */
	public double readDouble(long index);

	/**
	 * Reads a string from the reader's current position. The string may be written in any charset or format so long as
	 * {@link TroyBuffer#writeString(String)} or {@link TroyBuffer#writeString(long, String)} was used to write the string into the buffer
	 * 
	 * @return The string stored at the buffer's current position
	 */
	public String readString();

	public void readPosition(long positionRead);

	public void writePosition(long positionWrite);

	public long readPosition();

	public long writePosition();

	public long limit();

	public void limit(long newLimit);

	public long capacity();

	/**
	 * Writes the current contents of this writer to the file specified.<br>
	 * If the file exists, it <b>will be deleted and re written with the new data in this writer </b>After this method is called, the file will have
	 * the exact same contents as this writer.
	 * 
	 * @param file
	 *            The file to write to
	 * @throws IOException
	 *             If an I/O error occurs while writing to the file.
	 */
	public void writeToFile(File file) throws IOException;

	public byte[] getBytes();

	public byte[] getBytes(long offset, int length);

	/**
	 * Frees any native resources that this buffer uses. After calling this method, native buffer types will be unusable, so only call this method
	 * when you are prepared to loose all access to the buffer.
	 */
	public void free();

	/**
	 * Skips the reader forward the desired amount of bytes
	 * 
	 * @param bytes
	 *            the number of bytes to skip
	 */
	public void skipReader(long bytes);

	/**
	 * Skips the writer forward the desired amount of bytes
	 * 
	 * @param bytes
	 *            the number of bytes to skip
	 */
	public void skipWriter(long bytes);

	/**
	 * Returns the number of bytes between the read pointer and this buffer's limit
	 * 
	 * @return The number of bytes between the read pointer and this buffer's limit
	 */
	public long remainingRead();
	
	/**
	 * Returns {@code true} if there are more bytes between the readers position and this buffer's limit and {@code false} otherwise
	 * @return If more data can be read
	 */
	public boolean canRead();

	/**
	 * Copies data from another buffer to this one
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
	public void copyFrom(TroyBuffer src, long srcOffset, long destOffset, long bytes);

	/**
	 * Sets the byte order for reading data
	 * 
	 * @param readOrder
	 *            The new byte order to use when reading values
	 */
	public void setReadOrder(ByteOrder readOrder);

	/**
	 * Returns the byte order that is being used to read values
	 * 
	 * @return The currently active byte order for reading
	 */
	public ByteOrder getReadOrder();

	/**
	 * Sets the byte order for writing data
	 * 
	 * @param writeOrder
	 *            The new byte order to use when writing values
	 */
	public void setWriteOrder(ByteOrder writeOrder);

	/**
	 * Returns the byte order that is being used to write values
	 * 
	 * @return The currently active byte order for writing
	 */
	public ByteOrder getWriteOrder();

	/**
	 * Returns a native platform specific memory address where the data for the buffer starts
	 * 
	 * @return A memory address
	 */
	public long address();

	/**
	 * Returns a {@link String} representation of the values in this buffer
	 */
	public String getElements();

	/**
	 * Sets the content of this buffer to match the desired byte array
	 * 
	 * @param array
	 *            The array to copy from
	 */
	public void setFromArray(byte[] array);

}
