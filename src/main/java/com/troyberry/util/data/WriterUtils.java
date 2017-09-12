package com.troyberry.util.data;

public class WriterUtils {

	public static byte booleanToByte(boolean value) {
		return value ? (byte) 1 : (byte) 0;
	}

	public static boolean byteToBoolean(byte value) {
		return value != 0;
	}

}
