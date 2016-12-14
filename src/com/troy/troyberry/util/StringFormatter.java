package com.troy.troyberry.util;

public class StringFormatter {
	
	public static String byteToHexString(byte b){
		return String.format("%02X ", b).trim();
	}
	
	public static String byteToBinaryString(byte b){
		String result = "";
		for(int i = 7; i >= 0; i--){
			result += ((b >> i) & 0b00000001);
		}
		return result;
	}
	
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
	
	public static String clip(double value, int decimalPlaces){
		String x = "" + value;
		return x.substring(0, Math.min(x.lastIndexOf(".") + decimalPlaces + 1, x.length() - x.lastIndexOf(".")));
	}
	
	public static String clip(float value, int decimalPlaces){
		String x = "" + value;
		return x.substring(0, Math.min(x.lastIndexOf(".") + decimalPlaces + 1, x.length() - x.lastIndexOf(".")));
	}

	private StringFormatter() {
	}

}
