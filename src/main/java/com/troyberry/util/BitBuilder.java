package com.troyberry.util;

public class BitBuilder {

	private long currentByte;

	public BitBuilder() {
		currentByte = 0x0000000000000000;
	}

	/**
	 * Returns the value at the index <code>byteIndex</code>
	 * @param bit The index to search at
	 * @return The value at that index
	 */
	public boolean get(int bit) {
		checkRange(bit);
		// Read the byte that index is contained in
		// Sort out just the correct bit by shifting the bit to the ones place and discarding all other places
		return ((currentByte >> (7 - bit % Byte.SIZE)) & 0b00000001) == 1;
	}

	public BitBuilder set(int min, int max, boolean value) {
		for (int i = min; i < max; i++) {
			set(i, value);
		}
		return this;
	}

	public void clear() {
		currentByte = 0L;
	}

	/**
	 * Sets the value specified by <code>bitIndex</code> to the specfied value
	 * @param bit The index to set
	 * @param value The new value at that index
	 */
	public void set(int bit, boolean value) {
		checkRange(bit);
		//Get the current byte that index is contained in
		boolean currentValueBit = ((currentByte >> bit) & 0b1) == 1;
		// We don't need to change anything, just return
		if (currentValueBit == value) return;
		// Move a 1 to the correct bit so that it can be added or subtracted
		long valueToChange = (1L << (long) bit);
		//Before it was false and we want to make it true
		if (value) {
			/** The new value at the correct byte is the old value plus the new value to set the correct bit
				IE original byte: 10010000 call: set index: 5 value: true
				valueToChange = 1 << 5 = 00100000
				new byte: 10010000 + 00100000 = 10110000
				10010000 -> 10110000
				index 2 is now true
			 */
			currentByte += valueToChange;
		}
		//Before it was true and we want to make it false
		else {
			currentByte -= valueToChange;
		}
	}

	@Override
	public String toString() {
		return "Bit Builder [" + StringFormatter.toBinaryString(currentByte) + "]";
	}

	public int first32() {
		return (int) currentByte;
	}

	public int second32() {
		return (int) (currentByte >> 32);
	}

	public long all() {
		return currentByte;
	}

	public short first16() {
		return (short) currentByte;
	}

	public short second16() {
		return (short) (currentByte >> 16);
	}

	public short third16() {
		return (short) (currentByte >> 32);
	}

	public short last16() {
		return (short) (currentByte >> 48);
	}

	public byte first8() {
		return (byte) currentByte;
	}

	public byte second8() {
		return (byte) (currentByte >> 8);
	}

	public byte third8() {
		return (byte) (currentByte >> 16);
	}

	public byte fourth8() {
		return (byte) (currentByte >> 24);
	}

	public byte fifth8() {
		return (byte) (currentByte >> 32);
	}

	public byte sixth8() {
		return (byte) (currentByte >> 40);
	}

	public byte seventh8() {
		return (byte) (currentByte >> 48);
	}

	public byte last8() {
		return (byte) (currentByte >> 56);
	}

	private void checkRange(int bit) {
		if (bit < 0 || bit >= Long.SIZE) throw new IndexOutOfBoundsException("bit out of range" + bit);
	}

}