package com.troyberry.util;

import com.troyberry.math.*;

public class PrimitiveUtils {
	public static final byte UNSIGNED_BYTE_MAX_VALUE = (byte) (Maths.pow(2L, 8L) - 1L);
	public static final short UNSIGNED_SHORT_MAX_VALUE = (short) (Maths.pow(2L, 16L) - 1L);
	public static final int UNSIGNED_INT_MAX_VALUE = (byte) (Maths.pow(2L, 32L) - 1L);

	public static boolean unsignedBytesGeratorThan(byte unsigned1, byte unsigned2) {
		return (((short) unsigned1) & 0xFF) > (((short) unsigned2) & 0xFF);
	}
	
	public static boolean unsignedBytesLessThan(byte unsigned1, byte unsigned2) {
		return (((short) unsigned1) & 0xFF) < (((short) unsigned2) & 0xFF);
	}
	
	public static boolean unsignedBytesGeratorThanOrEqualTo(byte unsigned1, byte unsigned2) {
		return (((short) unsigned1) & 0xFF) >= (((short) unsigned2) & 0xFF);
	}
	
	public static boolean unsignedBytesLessThanOrEqualTo(byte unsigned1, byte unsigned2) {
		return (((short) unsigned1) & 0xFF) <= (((short) unsigned2) & 0xFF);
	}
	
	public static boolean unsignedShortsGeratorThan(short unsigned1, short unsigned2) {
		return (((int) unsigned1) & 0xFF) > (((int) unsigned2) & 0xFF);
	}
	
	public static boolean unsignedShortsLessThan(short unsigned1, short unsigned2) {
		return (((int) unsigned1) & 0xFF) < (((int) unsigned2) & 0xFF);
	}
	
	public static boolean unsignedShortsGeratorThanOrEqualTo(short unsigned1, short unsigned2) {
		return (((int) unsigned1) & 0xFF) >= (((int) unsigned2) & 0xFF);
	}
	
	public static boolean unsignedShortsLessThanOrEqualTo(short unsigned1, short unsigned2) {
		return (((int) unsigned1) & 0xFF) <= (((int) unsigned2) & 0xFF);
	}

	public static byte signedByteToUnsignedByte(byte b) {
		return (byte) (b & 0b01111111);// Get rid of sign byte
	}

	public static byte signedShortToUnsignedByte(short s) {
		return (byte) ((byte) s & 0x000000FF);// Get rid of sign byte
	}

	public static String unsignedByteToString(byte b) {
		return "" + ((short) (b & 0xFF));
	}
	
	public static short unsignedByteToShort(byte b) {
		return (short) (b & 0xFF);
	}

	public static short signedShortToUnsignedShort(short b) {
		return (short) (b & 0b0111111111111111);// Get rid of sign byte
	}

	public static short signedIntToUnsignedShort(int s) {
		return (short) (s & 0x0000FFFF);// Get rid of sign byte
	}

	public static String unsignedShortToString(short s) {
		return "" + ((int) (s & 0xFFFF));
	}
	
	public static int unsignedShortToInt(short s) {
		return (int) (s & 0xFFFF);
	}

	public static short signedIntToUnsignedInt(int b) {
		return (short) (b & 0b01111111111111111111111111111111);// Get rid of sign byte
	}

	public static int signedLongToUnsignedInt(long s) {
		return (short) (s & 0x0000FFFF);// Get rid of sign byte
	}

	public static String unsignedIntToString(int b) {
		return "" + (b & 0xFFFFFFFFL);
	}

	public static int sizeof(boolean b) {
		return 1;// Usually one. But sometimes compacted to 1 bit by the JVM
	}

	//
	//
	//
	// ############### Sizes ###############
	//
	//
	//
	public static int sizeof(byte b) {
		return Byte.BYTES;
	}

	public static int sizeof(short s) {
		return Short.BYTES;
	}

	public static int sizeof(char s) {
		return Character.BYTES;
	}

	public static int sizeof(int s) {
		return Integer.BYTES;
	}

	public static int sizeof(long s) {
		return Long.BYTES;
	}

	public static int sizeof(float s) {
		return Float.BYTES;
	}

	public static int sizeof(double s) {
		return Double.BYTES;
	}

	public static <T> int sizeof(Class<T> clazz) {
		if (clazz.isArray()) return sizeof(clazz.getComponentType());

		if (clazz.equals(Boolean.TYPE)) return 1;// Usually one. But sometimes compacted to 1 bit by the JVM
		if (clazz.equals(Byte.TYPE)) return Byte.BYTES;
		if (clazz.equals(Short.TYPE)) return Short.BYTES;
		if (clazz.equals(Integer.TYPE)) return Integer.BYTES;
		if (clazz.equals(Long.TYPE)) return Long.BYTES;
		if (clazz.equals(Float.TYPE)) return Float.BYTES;
		if (clazz.equals(Double.TYPE)) return Double.BYTES;

		if (clazz.equals(Boolean.class)) return 1;// Usually one. But sometimes compacted to 1 bit by the JVM
		if (clazz.equals(Byte.class)) return Byte.BYTES;
		if (clazz.equals(Short.class)) return Short.BYTES;
		if (clazz.equals(Integer.class)) return Integer.BYTES;
		if (clazz.equals(Long.class)) return Long.BYTES;
		if (clazz.equals(Float.class)) return Float.BYTES;
		if (clazz.equals(Double.class)) return Double.BYTES;

		throw new IllegalArgumentException("The class " + clazz.getName() + " is not primative, so it doesnt have a concrete size");
	}

}
