package com.troy.troyberry.utils;

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

	public static byte[] toByteArray(List<Byte> positions) {
		byte[] finalArray = new byte[positions.size()];
		Iterator<Byte> i = positions.iterator();
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

}
