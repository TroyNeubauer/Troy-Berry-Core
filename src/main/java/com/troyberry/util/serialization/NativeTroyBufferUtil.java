package com.troyberry.util.serialization;

/**
 * Contains all the native methods used in direct TroyBuffer's {@link TroyBufferUnsafe}
 * @author Troy Neubauer
 *
 */
public class NativeTroyBufferUtil {

	public static native void freePointers(long[] pointers, int count);

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
	public static native boolean nwriteToFile(String fileName, long address, long length);

	/**
	 * Allocates enough memory to store length of the specified file, then loads length of the file into the memory returning the resulting pointer
	 * @param fileName The file to load
	 * @param length The number of bytes to read in the file
	 * @return The pointer to the memory filled with the file or NULL if an error occurred
	 */
	public static native long ncreateFromFileSubset(long address, String fileName, long length);

	/**
	 * Allocates enough memory to store length of the specified file, then loads length of the file into the memory returning the resulting pointer
	 * @param fileName The file to load
	 * @param offset The number of bytes into the file to start reading
	 * @param length The number of bytes to read in the file
	 * @return The pointer to the memory filled with the file
	 */
	public static native long ncreateFromFileChunk(long address, String fileName, long offset, long length);
	

}
