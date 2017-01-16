package com.troyberry.util;

public class StringFormatter {
	
	public static String byteToHexString(byte b){
		return String.format("%02X ", b).trim();
	}
	
	/**
	 * Returns a String the represents a byte in binary form<br>
	 * The String will always be 8 characters long<br>
	 * IE: <code>
	 * byte: 16 -> 00010000, byte: 100 -> 01100100,<br> byte: 0 -> 00000000
	 * </code>
	 * @param b The byte to be molded after
	 * @return The formatted String
	 */
	public static String byteToBinaryString(byte b){
		String result = "";
		for(int i = 7; i >= 0; i--){
			result += ((b >> i) & 0b00000001);
		}
		return result;
	}
	
	/**
	 * Returns a String the represents a long in binary form<br>
	 * The String will always be 64 characters long<br>
	 * IE: <code>
	 * long: 16 -> 0000...preceding 0's 010000, long: 100 -> 01100100,<br> long: 0 -> 00000000
	 * </code>
	 * @param l The long to be molded after
	 * @return The formatted String
	 */
	public static String longToBinaryString(long l){
		String result = "";
		for(int i = Long.SIZE - 1; i >= 0; i--){
			result += ((l >> i) & 0b0000000000000000000000000000000000000000000000000000000000000001);
		}
		return result;
	}
	
	/**
	 * Ensures that a particular String is always a certain length by adding whitespace at the end or trimming 
	 * to a shorter length<br>
	 * Because of Javadoc formatting issues, assume that '-' means space<br>
	 * IE: <code> String: "Hi,-i'm-here", Length: 20 ->  "Hi,-i'm-here--------"<br>
	 * String: "This-is-very-interesting,-I-am-a-particularly-long-String,-what-a-waste-of-memory...",<br> 
	 * Length: 10 -> "This is ve"
	 * </code>
	 * @param string The string to clip or extend
	 * @param characters The amount of characters to clip of extend to
	 * @return The formatted String
	 */
	public static String cutToSize(String string, int characters){
		if(string.length() < characters){
			String temp = string;
			for(int i = 0; i < characters - string.length(); i++){
				temp = temp + " ";
			}
			return temp;
		}else{
			return string.substring(0, characters);
		}
	}
	
	/**
	 * Returns a String representing the double where all decimals after decimalPlaces are clipped.</br>
	 * Example: <code></br>
	 * System.out.println(clip(123.456789091234, 3));
	 * </code></br>
	 * Yields the output "123.456"
	 * @param value The double value to be clipped
	 * @param decimalPlaces The amount of decimal places to keep
	 * @return The formatted String
	 */
	public static String clip(double value, int decimalPlaces){
		String x = "" + value;
		return x.substring(0, Math.min(x.lastIndexOf(".") + decimalPlaces + 1, x.length() - x.lastIndexOf(".")));
	}
	
	/**
	 * Returns a String representing the float where all decimals after decimalPlaces are clipped.</br>
	 * Example: <code></br>
	 * System.out.println(clip(123.456789091234f, 3));
	 * </code></br>
	 * Yields the output "123.456"
	 * @param value The float value to be clipped
	 * @param decimalPlaces The amount of decimal places to keep
	 * @return The formatted String
	 */
	public static String clip(float value, int decimalPlaces){
		String x = "" + value;
		return x.substring(0, Math.min(x.lastIndexOf(".") + decimalPlaces + 1, x.length() - x.lastIndexOf(".")));
	}
	
	/**
	 * Returns a string representing the amount of bytes in "Standard data form"</br> 
	 * Examples:<code> 33 -> "33B", 15360 -> "15K", 583008256 -> "556MB", 3221225472 -> "3TB" etc.</code></br>
	 * This method assumes 1024 bits to one kilobyte 1024^2 to one megabyte
	 * @param bytes The value in bytes to be formatted
	 * @return The formatted string
	 */
	public static String formatBytes(long bytes) {
		int unit = 1024;
		if (bytes < unit) return bytes + " B";
		int exp = (int) (Math.log(bytes) / Math.log(unit));
		String pre = ("KMGTPE").charAt(exp-1) + "";
		return String.format("%.1f %sB", bytes / Math.pow(unit, exp), pre);
		
	}
	
	/**
	 * Returns a string representing the amount of bytes in "Standard data form" with decimals</br> 
	 * Examples:<code> 33 -> "33B", 15360 -> "15.43K", 583008256 -> "556.5MB", 3221225472 -> "3.1TB" etc.</code></br>
	 * This method assumes 1024 bits to one kilobyte 1024^2 to one megabyte
	 * @param bytes The value in bytes to be formatted
	 * @return The formatted string
	 */
	public static String formatBytesExact(long bytes) {
		int unit = 1024;
		if (bytes < unit) return bytes + " B";
		int exp = (int) (Math.log(bytes) / Math.log(unit));
		String pre = ("KMGTPE").charAt(exp-1) + "";
		return String.format("%.1f %sB", (double)bytes / Math.pow(unit, exp), pre);
		
	}
	
	// No instances of this class allowed
	private StringFormatter() {
	}

	/**
	 * Ensures that the string is all lower case except for the first letter 
	 * @param string The string to capitalize
	 * @return The formatted string
	 */
	public static String capitalizeFirstLetter(String string) {
		String result = string.toLowerCase();
		if(result.length() > 0) {
			String end = result.substring(1);
			result = string.substring(0, 1).toUpperCase() + end;
		}
		return result;
	}

}
