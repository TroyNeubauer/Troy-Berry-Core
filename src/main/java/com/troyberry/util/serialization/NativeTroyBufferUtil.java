package com.troyberry.util.serialization;

/**
 * Contains all the native methods used in direct TroyBuffer's {@link TroyBufferUnsafe}
 * @author Troy Neubauer
 *
 */
public class NativeTroyBufferUtil {

	/**
	 * Copies n bytes from the source address to the destination address<br>
	 * Both {@code dest} and {@code src} pointers are <b>NOT</b> checked for validity! If they are null or point to 
	 * memory that cannot be read, the Java Virtual Machine will terminate with a memory access violation!!!
	 * @param dest A pointer to copy n bytes to
	 * @param src A pointer to copy n bytes from
	 * @param bytes The number of bytes to copy
	 */
	public static native void nmemcpy(long dest, long src, long bytes);

	// From array to native memory
	public static native void ncpyBytesFrom(long dest, byte[] src, int offset, int elements);

	public static native void ncpyShortsFrom(long dest, short[] src, int offset, int elements, boolean swapEndianness);

	public static native void ncpyCharsFrom(long dest, char[] src, int offset, int elements, boolean swapEndianness);

	public static native void ncpyIntsFrom(long dest, int[] src, int offset, int elements, boolean swapEndianness);

	public static native void ncpyLongsFrom(long dest, long[] src, int offset, int elements, boolean swapEndianness);

	public static native void ncpyFloatsFrom(long dest, float[] src, int offset, int elements, boolean swapEndianness);

	public static native void ncpyDoublesFrom(long dest, double[] src, int offset, int elements, boolean swapEndianness);

	// From native memory to array
	public static native void ncpyBytesTo(byte[] dest, long src, int offset, int bytes);

	public static native void ncpyShortsTo(short[] dest, long src, int offset, int bytes, boolean swapEndianness);

	public static native void ncpyCharsTo(char[] dest, long src, int offset, int bytes, boolean swapEndianness);

	public static native void ncpyIntsTo(int[] dest, long src, int offset, int bytes, boolean swapEndianness);

	public static native void ncpyLongsTo(long[] dest, long src, int offset, int bytes, boolean swapEndianness);

	public static native void ncpyFloatsTo(float[] dest, long src, int offset, int bytes, boolean swapEndianness);

	public static native void ncpyDoublesTo(double[] dest, long src, int offset, int bytes, boolean swapEndianness);

	// Methods for dealing with files
	/**
	 * Copies a section of memory (address to address + length exclusive) to the file specified by {@code file}
	 * @param file The file to write to
	 * @param address A pointer to the start of the block of memory to copy
	 * @param length The number of bytes to copy starting at {@code address}
	 * @return false if writing to the file failed due to some I/O error, true if successful
	 */
	public static native boolean nwriteToFile(String file, long address, long length);

	/**
	 * Copies the specified length of the desired file into the memory starting at address {@code address}
	 * @param address The address to copy the file to
	 * @param file The file to load
	 * @param length The number of bytes to read in the file
	 * @return false if the operation failed due to some I/O error, true if successful
	 */
	public static native boolean ncopyFileSubset(long address, String file, long length);

	/**
	 * Copies the a chunk of the desired file to the address specified.
	 * @param address The address to copy the file to
	 * @param file The file to load
	 * @param offset The number of bytes into the file to start reading
	 * @param length The number of bytes to read in the file
	 * @return false if the operation failed due to some I/O error, true if successful
	 */
	public static native boolean ncopyFileChunk(long address, String file, long offset, long length);
	

}
