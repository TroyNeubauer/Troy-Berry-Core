package com.troy.troyberry.util;

import java.util.*;
import com.troy.troyberry.math.*;

public class ArrayUtil {

	public static float[] toFloatArray(List<Float> positions) {
		float[] finalArray = new float[positions.size()];
		Iterator<Float> i = positions.iterator();
		int count = 0;
		while (i.hasNext()) {
			finalArray[count] = (float) i.next();
			count++;
		}
		return finalArray;
	}

	public static int[] toIntArray(List<Integer> positions) {
		int[] finalArray = new int[positions.size()];
		Iterator<Integer> i = positions.iterator();
		int count = 0;
		while (i.hasNext()) {
			finalArray[count] = (int) i.next();
			count++;
		}
		return finalArray;
	}

	public static byte[] trimZeros(byte[] data) {
		if ((data == null) || isEmpty(data)) {
			return new byte[] {}; 
		}
		int beginIndex = 0, endIndex = data.length - 1;
		for (int i = 0; i < data.length; i++) {
			if (data[i] == 0x0) {
				beginIndex = i + 1;
			} else {
				break;
			}

		}
		for (int i = data.length - 1; i >= 0; i--) {
			if (data[i] == 0x0) {
				endIndex = i - 1;
			} else {
				break;
			}

		}

		byte[] result = new byte[endIndex - beginIndex + 1];
		int index = 0;
		for (int i = beginIndex; i <= endIndex; i++) {
			result[index] = data[i];
			index++;
		}
		return result;

	}

	public static byte[] toByteArray(List<Byte> data) {
		byte[] finalArray = new byte[data.size()];
		Iterator<Byte> i = data.iterator();
		int count = 0;
		while (i.hasNext()) {
			finalArray[count] = (byte) i.next();
			count++;
		}
		return finalArray;
	}

	public static float[] outOfVector3f(List<Vector3f> list) {
		float[] data = new float[list.size() * 3];
		for (int i = 0; i < list.size(); i++) {
			Vector3f vec = list.get(i);
			data[i * 3 + 0] = vec.x;
			data[i * 3 + 1] = vec.y;
			data[i * 3 + 2] = vec.z;
		}
		return data;
	}

	public static boolean isEmpty(byte[] data) {
		if (data == null)
			return true;
		for (int i = 0; i < data.length; i++) {
			if (data[i] != 0x0)
				return false;
		}
		return true;
	}

}
