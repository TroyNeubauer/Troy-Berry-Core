package com.troyberry.util;

public class MiscUtil {
	
	/**
	 * Generates a long from a string by adding the ASCII codes together. The long is more of less random but the same string 
	 * will always return the same result.</br>
	 * This is useful for using a string to seed a random number generator. In that case, the user will be able to enter 
	 * something like "Troy is cool" and the output of random numbers would always be the same as long as "Troy is cool" was 
	 * the seed. However, as soon as the user changes what they entered to lets say "Troy is awesome", the seed would be different.
	 * @param s The string to convert
	 * @return A long representing the string that can be used scenarios
	 */
	public static long getlong(String s) {
		long number = 0;
		for (int i = 0; i < s.length(); i++) {
			char c = s.charAt(i);
			number += ((int) c) * ((long) Math.pow(10, i));
		}
		return number;
	}

	private MiscUtil() {
		
	}
}
