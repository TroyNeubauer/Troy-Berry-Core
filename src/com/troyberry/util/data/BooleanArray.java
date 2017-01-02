package com.troyberry.util.data;

import com.troyberry.math.*;

/**
 * This class represents a boolean array. Instead of using a actual array, it uses an array of bytes and some clever bit shifting 
 * to store 8 booleans in one byte unlike a typical boolean which uses one byte to store one bit.
 * @author Troy Neubauer
 *
 */
public class BooleanArray {
	/**	The length in bits of this boolean array. 
	 * Long because the max length in bits the max value of an integer * 8 (because of 8 bits in byte) */
	private long length;
	/**
	 * The amount of bytes used to represent this array
	 */
	private int arrayLengthInBytes;
	/**
	 * The actual data for this array
	 */
	private byte[] data;
	/** The longest possible size for a this array type */
	public static final long MAX_SIZE = ((long)Integer.MAX_VALUE) * 8L;
	
	/**
	 * Useful constants for creating arrays of a given size. Note that if you create an array with one of these constants, 
	 * The amount of elements will be the same as the value of these constants, but the memory that this array actually 
	 * takes up will be 8X smaller. 
	 */
	public static final long ONE_KILOBYTE = Maths.pow(2, 10);
	public static final long ONE_MEGABYTE = ONE_KILOBYTE * 1024L;
	public static final long ONE_GIGABYTE = ONE_MEGABYTE * 1024L;
	
	/**
	 * Constructs a new boolean array with the specified size. All values will be false by default
	 * @param length The amount of boolean values that can be stored
	 */
	public BooleanArray(long length) {
		if(length > MAX_SIZE) throw new IllegalArgumentException(length + " is to lare for a boolean array! The largest vaule premitted is " + MAX_SIZE);
		if(length < 0) throw new IllegalArgumentException("A boolean array of a negavive vaule is not permitted " + length);
		this.length = length;
		// So 8 bits will yield 1 byte, but 9 bits will yield 2 bytes etc
		this.arrayLengthInBytes = (int)((length + Byte.SIZE - 1) / (long)Byte.SIZE);
		this.data = new byte[arrayLengthInBytes];
	}
	
	/**
	 * Resizes the array to a new capacity specified by newLength. All data within the range of bits will be copied If <code>bits</code> is less than the length of this array, 
	 * then the data after bits will be lost
	 * @param newLength The length of the resulting boolean array
	 */
	public void resize(long newLength){
		this.length = newLength;
		this.arrayLengthInBytes = (int)((newLength + Byte.SIZE - 1) / (long)Byte.SIZE);
		byte[] newData = new byte[arrayLengthInBytes];
		System.arraycopy(this.data, 0, newData, 0, this.data.length);
		this.data = newData;
	}
	
	/**
	 * Resets all values in this boolean array to be false
	 */
	public void clear(){
		this.data = new byte[arrayLengthInBytes];
	}
	
	/**
	 * Resets all values in this boolean array to be true
	 */
	public void setTrue(){
		byte allTrue = (byte) 0b11111111;
		for(int i = 0; i < data.length; i++){
			data[i] = allTrue;
		}
	}
	
	/**
	 * Returns the value at the index <code>byteIndex</code>
	 * @param index The index to search at
	 * @return The value at that index
	 */
	public boolean get(long index){
		checkRange(index);
		// Read the byte that index is contained in
		byte currentByte = data[(int) (index / Byte.SIZE)];
		// Sort out just the correct bit by shifting the bit to the ones place and discarding all other places
		return ((currentByte >> (7 - index % Byte.SIZE)) & 0b00000001) == 1;
	}
	
	/**
	 * Sets the value specified by <code>byteIndex</code> to the specfied value
	 * @param index The index to set
	 * @param value The new value at that index
	 */
	public void set(long index, boolean value) {// This code could probably be optimized
		checkRange(index);
		//Get the current byte that index is contained in
		byte current = data[(int) (index / Byte.SIZE)];
		//determine where the index is in the current byte
		byte subIndex = (byte)(7 - (int) index % Byte.SIZE);
		// Get the boolean value at that bit
		boolean currentValue = ((current >> subIndex) & 0b00000001) == 1;
		// We don't need to change anything, just return
		if (currentValue == value) return;
		// Move a 1 to the correct bit so that it can be added or subtracted
		byte valueToChange = (byte) (1 << subIndex);
		//Before it was false and we want to make it true
		if (value) {
			/** The new value at the correct byte is the old value plus the new value to set the correct bit
				IE original byte: 10010000 call: set index: 2 value: true
				valueToChange = 1 << 5 = 00100000
				new byte: 10010000 + 00100000 = 10110000
				10010000 -> 10110000
		 index: 01234567    01234567
				index 2 is now true
			 */
			data[(int) (index / Byte.SIZE)] = (byte) (current + valueToChange);
		}
		//Before it was true and we want to make it false
		else {
			
			data[(int) (index / Byte.SIZE)] = (byte) (current - valueToChange);
		}
	}
	
	/**
	 * Ensures that an index is in range of this array
	 * @param index The index to check
	 * @throws ArrayIndexOutOfBoundsException if the index is out of range
	 */
	public void checkRange(long index) {
		if(index < 0 || index >= length){
			throw new ArrayIndexOutOfBoundsException("Array index out of range: "+ index + "! Array size " + size());
		}
	}
	
	/**
	 * Returns the number of elements in this array.<br>
	 * Exactly the same as {@link #size()}
	 */
	public long length(){
		return length;
	}
	
	/**
	 * Returns the number of elements in this array.<br>
	 * Exactly the same as {@link #length()}
	 */
	public long size(){
		return length;
	}
	
	/**
	 * Returns the number of bytes used by this array. This will usually be <br><code>(size_in_elements + 7) / 8</code>
	 * @return The size in bytes
	 */
	public int bytes(){
		return data.length;
	}

}
