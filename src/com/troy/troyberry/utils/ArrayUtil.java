package com.troy.troyberry.utils;

import java.util.*;

public class ArrayUtil {

	public static float[] toFloatArray(List<Float> positions) {
		float[] finalArray = new float[positions.size()];
		Iterator<Float> i = positions.iterator();
		int count = 0;
		while(i.hasNext()){
			finalArray[count] = (float)i.next(); 
			count++;
		}
		return finalArray;
	}
	
	public static int[] toIntArray(List<Integer> positions) {
		int[] finalArray = new int[positions.size()];
		Iterator<Integer> i = positions.iterator();
		int count = 0;
		while(i.hasNext()){
			finalArray[count] = (int)i.next(); 
			count++;
		}
		return finalArray;
	}
	
	public static byte[] toByteArray(List<Byte> positions) {
		byte[] finalArray = new byte[positions.size()];
		Iterator<Byte> i = positions.iterator();
		int count = 0;
		while(i.hasNext()){
			finalArray[count] = (byte)i.next(); 
			count++;
		}
		return finalArray;
	}
	
	

}
