package com.troyberry.util;

import com.troyberry.math.*;

/**
 * A class that has some useful utilities for manipulating numbers
 * @author Troy Neubauer
 *
 */
public class NumberUtil {
	
	public static double truncate(double number, int decimalPlaces) {
		long numberToMultiply = Maths.pow(10, decimalPlaces);
		number *= numberToMultiply;
		number = (int)number;
		number /= numberToMultiply;
		return number;
	}
	
	public static float truncate(float number, int decimalPlaces) {
		long numberToMultiply = Maths.pow(10, decimalPlaces);
		number *= numberToMultiply;
		number = (int)number;
		number /= numberToMultiply;
		return number;
	}
	
	public static double roundOff(double number, int decimalPlaces) {
		long numberToMultiply = Maths.pow(10, decimalPlaces);
		number *= numberToMultiply;
		number = Maths.round(number);
		number /= numberToMultiply;
		return number;
	}
	
	public static float roundOff(float number, int decimalPlaces) {
		long numberToMultiply = Maths.pow(10, decimalPlaces);
		number *= numberToMultiply;
		number = Maths.round(number);
		number /= numberToMultiply;
		return number;
	}

}
