package com.troyberry.math;

import java.util.*;

/**
 * A static class that has various helpful math functions not in the {@link Math} class
 * @author Troy Neubauer
 *	
 */
public class Maths {

	private static Random random = new Random();
	/** PI as a float to the 23rd decimal */
    public static final float PI = 3.14159265358979323846264f;
    /** PI as a float to the 5th decimal to save time in calculations that don't need to be super accurate <br>
     * For the a more accurate PI as a float {@link Maths#PI}*/
    public static final float SHORT_PI = 3.14159f;
    
    /**
     * Returns the trigonometric sine of an angle as a float. This method uses {@link Math#sin(double)}
     * @param a An angle, in radians.
     * @return The sine of the argument.
     */
    public static final float sinFloat(double a){
    	return (float)StrictMath.sin(a);
    }
    
    /**
     * Returns the trigonometric cosine of an angle as a float. This method uses {@link Math#cos(double)}
     * @param a An angle, in radians.
     * @return The cosine of the argument.
     */
    public static final float cosFloat(double a){
    	return (float)StrictMath.cos(a);
    }
    
    /**
     * Returns the trigonometric tangent of an angle as a float. This method uses {@link Math#tan(double)}
     * @param a An angle, in radians.
     * @return The tangent of the argument.
     */
    public static final float tanFloat(double a){
    	return (float)StrictMath.tan(a);
    }
	
    /**
     * Raises the base to the power of the exponent and returns the result
     * @param base The base
     * @param exp The power to raise the base to
     * @return The result of base^exp
     */
	public static long pow(long base, long exp){        
	    if (exp == 0) return 1;
	    if (exp == 1) return base;

	    if (exp % 2 == 0) {
	        long half = pow(base, exp/2);
	        return half * half;
	    } else {
	        long half = pow(base, (exp -1)/2);
	        return base * half * half;
	    }       
	}
	
    /**
     * Raises the base to the power of the exponent and returns the result
     * @param base The base
     * @param exp The power to raise the base to
     * @return The result of base^exp
     */
	public static int pow(int base, int exp){        
	    if (exp == 0) return 1;
	    if (exp == 1) return base;

	    if (exp % 2 == 0) {
	        int half = pow(base, exp/2);
	        return half * half;
	    } else {
	        int half = pow(base, (exp -1)/2);
	        return base * half * half;
	    }       
	}

	/**
	 * Linearly interpolates between to factors a, and b with the factor f.<br>
	 * <b>Special cases:</b>
	 * The calculation will continue even if f is > 1.0f or < 0.0f, for a safe version of this calculation 
	 * see {@link Maths#lerpSafe(float, float, float)} 
	 * @param a The first number
	 * @param b The second number
	 * @param f The factor to interpolate between a and b
	 * @return The interpolated output
	 */
	public static float lerp(float a, float b, float f) {
		return a + f * (b - a);
	}
	
	/**
	 * Linearly interpolates between two factors a, and b with the factor f "safely".<br>
	 * The factor f is clamped between 0 and 1 so any values outside that range will be 0 or 1 {@link Maths#clamp(float, float, float)}
	 * @param a The first number
	 * @param b The second number
	 * @param f The factor to interpolate between a and b
	 * @return The interpolated output
	 */
	public static float lerpSafe(float a, float b, float f) {
		f = clamp(0.0f, 1.0f, f);
		return a + f * (b - a);
	}

	/**
	 * Linearly interpolates between two factors a, and b with the factor f.<br>
	 * <b>Special cases:</b>
	 * The calculation will continue even if f is > 1.0 or < 0.0, for a safe version of this calculation 
	 * see {@link Maths#lerpSafe(double, double, double)} 
	 * @param a The first number
	 * @param b The second number
	 * @param f The factor to interpolate between a and b
	 * @return The interpolated output
	 */
	public static double lerp(double a, double b, double f) {
		return a + f * (b - a);
	}
	
	/**
	 * Linearly interpolates between two factors a, and b with the factor f "safely".<br>
	 * The factor f is clamped between 0 and 1 so any values outside that range will be 0 or 1 {@link Maths#clamp(double, double, double)}
	 * @param a The first number
	 * @param b The second number
	 * @param f The factor to interpolate between a and b
	 * @return The interpolated output
	 */
	public static double lerpSafe(double a, double b, double f) {
		f = clamp(0.0, 1.0, f);
		return a + f * (b - a);
	}
	
	/**
	 * Returns the greatest integer less than or equal to the real number f.<br>
	 * IE: floor(0.9f) = 0, floor(5.0f) = 5, floor(-5.5f) = -6
	 * @param f The float to floor
	 * @return The floored result
	 */
	public static int floor(float f) {
		int xi = (int) f;
		return f < xi ? xi - 1 : xi;
	}

	/**
	 * Returns the greatest integer less or equal to than the real number d.<br>
	 * IE: floor(0.9) = 0, floor(5.0) = 5, floor(-5.5) = -6
	 * @param d The double to floor
	 * @return The floored result
	 */
	public static int floor(double d) {
		int xi = (int) d;
		return d < xi ? xi - 1 : xi;
	}

	/**
	 * Returns the lowest integer greater than or equal to the real number f.<br>
	 * IE: ceil(0.9f) = 1, ceil(5.0f) = 5, ceil(-5.5f) = -5
	 * @param f The float to floor
	 * @return The floored result
	 */
	public static int ceil(float f) {
		int xi = (int) f;
		return f > xi ? xi + 1 : xi;
	}
	
	/**
	 * Returns the lowest integer greater than or equal to the real number d.<br>
	 * IE: ceil(0.9) = 1, ceil(5.0) = 5, ceil(-5.5) = -5
	 * @param f The float to floor
	 * @return The floored result
	 */
	public static int ceil(double d) {
		int xi = (int) d;
		return d > xi ? xi + 1 : xi;
	}

	/**
	 * Rounds the float f to the nearest integer.<br>
	 * This method abides by Math rules therefore 0.5f will be rounded up.
	 * IE: round(5.5f) = 6, round(0.3f) = 0, round(-0.7f) = -1, round(-0.3f) = 0
	 * @param f The float to round
	 * @return The rounded integer
	 */
	public static int round(float f) {
		if (f >= 0f) return (int) (f + 0.5f);
		else return (int) (f - 0.5f);
	}

	/**
	 * Rounds the double d to the nearest integer.<br>
	 * This method abides by Math rules therefore 0.5 will be rounded up.
	 * IE: round(5.5) = 6, round(0.3) = 0, round(-0.7) = -1, round(-0.3) = 0
	 * @param d The double to round
	 * @return The rounded integer
	 */
	public static int round(double d) {
		if (d >= 0.0) return (int) (d + 0.5);
		else return (int) (d - 0.5);
	}

	/**
	 * Clamps a value between a minimum and a maxiumn.<br>
	 * Any values lower than the min will cause min to be returned. Any values greater than the max will cause max to be returned. 
	 * Any values in between will return themselves.<br><br>
	 * clamp(min, max, value)<br>
	 * IE: clamp(0, 10, 5) = 5, clamp(0, 10, -5) = 0, clamp(0, 10, 50) = 10, clamp(0, 10, 0) = 0, clamp(0, 10, 9999) = 10<br><br>
	 * Because pointers don't exist in Java, in order to clamp the integer i in between 0 and 10, the following code must be used:<code><br>
	 * int i = -1000;<br>
	 * i = Maths.clamp(0, 10, i);<br>
	 * </code><br>
	 * After this code segment, i will have the value of 0
	 * @param min The minimum value
	 * @param max The maxiumn value
	 * @param value The value to check
	 * @return The clamped value
	 */
	public static int clamp(int min, int max, int value) {
		return Math.max(Math.min(value, max), min);
	}

	/**
	 * Clamps a value between a minimum and a maxiumn.<br>
	 * Any values lower than the min will cause min to be returned. Any values greater than the max will cause max to be returned. 
	 * Any values in between will return themselves.<br><br>
	 * clamp(min, max, value)<br>
	 * IE: clamp(0.0f, 10.0f, 5.5f) = 5.5f, clamp(0.0f, 10.0f, -5.0f) = 0.0f, clamp(0.0f, 10.0f, 50.0f) = 10.0f, 
	 * clamp(0.0f, 10.0f, -0.1f) = 0.0f, clamp(0.0f, 10.0f, 9999.5f) = 10.0f <br><br>
	 * Because pointers don't exist in Java, in order to clamp the float i in between 0.0f and 10.0f, the following code must be used:<code><br>
	 * float i = -1000.0f;<br>
	 * i = Maths.clamp(0.0f, 10.0f, i);<br>
	 * </code><br>
	 * After this code segment, i will have the value of 0
	 * @param min The minimum value
	 * @param max The maxiumn value
	 * @param value The value to check
	 * @return The clamped value
	 */
	public static float clamp(float min, float max, float value) {
		return Math.max(Math.min(value, max), min);
	}

	/**
	 * Clamps a value between a minimum and a maxiumn.<br>
	 * Any values lower than the min will cause min to be returned. Any values greater than the max will cause max to be returned. 
	 * Any values in between will return themselves.<br><br>
	 * clamp(min, max, value)<br>
	 * IE: clamp(0.0, 10.0, 5.5) = 5.5, clamp(0.0, 10.0, -5.0) = 0.0, clamp(0.0, 10.0, 50.0) = 10.0, clamp(0.0, 10.0, -0.1) = 0.0, 
	 * clamp(0.0, 10.0, 9999.5) = 10.0 <br><br>
	 * Because pointers don't exist in Java, in order to clamp the integer i in between 0 and 10, the following code must be used:<code><br>
	 * int i = -1000;<br>
	 * i = Maths.clamp(0, 10, i);<br>
	 * </code><br>
	 * After this code segment, i will have the value of 0
	 * @param min The minimum value
	 * @param max The maxiumn value
	 * @param value The value to check
	 * @return The clamped value
	 */
	public static double clamp(double min, double max, double value) {
		return Math.max(Math.min(value, max), min);
	}

	public static float randRange(float min, float max) {
		return (min + (float) (Math.random()) * (max - min));
	}

	/**@return a random double between the ranges inclusive 
	 * */
	public static double randRange(double min, double max) {
		return (min + random.nextDouble() * (max - min + 1));
	}

	/**@return a random int between the ranges inclusive of min and exclusive of max
	 * */
	public static int randRange(int min, int max) {
		return round(min + random.nextDouble() * (max - min));
	}

	public static double calculateVolumeOfASphere(double radius) {
		return ((4d / 3d) * Math.PI * Math.pow(radius, 3));
	}

	public static double solveForRaduisGivenASphere(double volume) {
		return Math.cbrt(volume / (4d / 3d) * Math.PI);
	}

	public static double getDistanceBetweenPoints(double x1, double y1, double x2, double y2) {
		return (double) Math.sqrt(Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2));
	}
	
	public static double getDistanceBetweenPoints(double x, double y, double z, double x2, double y2, double z2) {
		return Math.sqrt(Math.pow(x - x2, 2) + Math.pow(y - y2, 2) + Math.pow(z - z2, 2));
	}

	public static double getDistanceBetweenPoints(Vector3f point1, Vector3d point2) {
		return (double) Math.sqrt(Math.pow(point1.x - point2.x, 2) + Math.pow(point1.y - point2.y, 2));
	}
	
	public static double getDistanceBetweenPoints(Vector2d point1, Vector2d point2) {
		return getDistanceBetweenPoints(point1.x, point1.y, point2.x, point2.y);
	}
	
	public static float getDistanceBetweenPoints(Vector3f point1, Vector3f point2) {
		return (float) Math.sqrt(Math.pow(point1.x - point2.x, 2) + Math.pow(point1.y - point2.y, 2) + Math.pow(point1.z - point2.z, 2));
	}
	
	public static double getDistanceBetweenPoints(Vector3d point1, Vector3d point2) {
		return (double) Math.sqrt(Math.pow(point1.x - point2.x, 2) + Math.pow(point1.y - point2.y, 2) + Math.pow(point1.z - point2.z, 2));
	}
	
	public static double getDistanceBetweenPoints(float x, float y, float z,
			float x2, float y2, float z2) {
		return Math.sqrt(Math.pow(x - x2, 2.0) + Math.pow(y - y2, 2.0) + Math.pow(z - z2, 2.0));
	}

	public static double approximateDistanceBetweenPoints(Vector2f point1, Vector2f point2) {
		return Math.abs(Math.abs(point1.x) - Math.abs(point2.x)) + Math.abs(Math.abs(point1.y) - Math.abs(point2.y));
	}

	public static double approximateDistanceBetweenPoints(double x1, double y1, double x2, double y2) {
		return Math.abs(x1 - x2) + Math.abs(y1 - y2);
	}

	public static boolean inRange(float value, float min, float max) {
		return value >= Math.min(min, max) && value <= Math.max(min, max);
	}

	public static boolean inRange(int value, int min, int max) {
		return value >= Math.min(min, max) && value <= Math.max(min, max);
	}

	public static boolean rangeIntersect(float min0, float max0, float min1, float max1) {
		return Math.max(min0, max0) >= Math.min(min1, max1) && Math.min(min0, max0) <= Math.max(min1, max1);
	}

	public static float average(float... input) {
		float count = 0;
		float total = 0f;
		for (int i = 0; i < input.length; i++) {
			count++;
			total += (input[i]);
		}
		return total / count;
	}
	
	public static double average(double... input) {
		double count = 0.0;
		double total = 0.0;
		for (int i = 0; i < input.length; i++) {
			count++;
			total += (input[i]);
		}
		return total / count;
	}

	public static float average(int... input) {
		int count = 0;
		float total = 0f;
		for (int i = 0; i < input.length; i++) {
			count++;
			total += ((float) input[i]);
		}
		return ((float) total) / ((float) count);
	}

	public static boolean isNegative(float value) {
		return value < 0.0f;
	}

	public static boolean isPosative(float value) {
		return value > 0.0f;
	}

	public static void setSeed(long seed) {
		random.setSeed(seed);
	}

	public static float barryCentric(Vector3f p1, Vector3f p2, Vector3f p3, Vector2f pos) {
		float det = (p2.z - p3.z) * (p1.x - p3.x) + (p3.x - p2.x) * (p1.z - p3.z);
		float l1 = ((p2.z - p3.z) * (pos.x - p3.x) + (p3.x - p2.x) * (pos.y - p3.z)) / det;
		float l2 = ((p3.z - p1.z) * (pos.x - p3.x) + (p1.x - p3.x) * (pos.y - p3.z)) / det;
		float l3 = 1.0f - l1 - l2;
		return l1 * p1.y + l2 * p2.y + l3 * p3.y;
	}

	public static double gaussian(double lowest, double highest, double center) {
		double difference = Math.abs(Math.max(highest, highest) - Math.min(highest, lowest)) / 2.0;
		double number = random.nextGaussian();
		number /= 5;
		number *= difference;
		number += center;
		number = Maths.clamp(lowest, highest, number);
		return number;
	}
	
	public static double approximateDistanceBetweenPoints(Vector3f point1, Vector3f point2) {
		return Math.abs(Math.abs(point1.x) - Math.abs(point2.x)) + Math.abs(Math.abs(point1.y) - Math.abs(point2.y)) + + Math.abs(Math.abs(point1.z) - Math.abs(point2.z));
	}
	
	public static float degreesToRadians(float degrees) {
		return (float) Math.toRadians(degrees);
	}
	
	public static double evaluate(final String str) {
		return new Object() {
			int pos = -1, ch;

			void nextChar() {
				ch = (++pos < str.length()) ? str.charAt(pos) : -1;
			}

			boolean eat(int charToEat) {
				while (ch == ' ')
					nextChar();
				if (ch == charToEat) {
					nextChar();
					return true;
				}
				return false;
			}

			double parse() {
				nextChar();
				double x = parseExpression();
				if (pos < str.length()) {
					try {
						throw new RuntimeException("Unexpected: " + (char) ch);
					} catch (Exception e) {
						e.printStackTrace();
						return 0.0;
					}
				}
				return x;
			}

			// Grammar:
			// expression = term | expression `+` term | expression `-` term
			// term = factor | term `*` factor | term `/` factor
			// factor = `+` factor | `-` factor | `(` expression `)`
			// | number | functionName factor | factor `^` factor

			double parseExpression() {
				double x = parseTerm();
				for (;;) {
					if (eat('+')) x += parseTerm(); // addition
					else if (eat('-')) x -= parseTerm(); // subtraction
					else return x;
				}
			}

			double parseTerm() {
				double x = parseFactor();
				for (;;) {
					if (eat('*')) x *= parseFactor(); // multiplication
					else if (eat('/')) x /= parseFactor(); // division
					else return x;
				}
			}

			double parseFactor() {
				if (eat('+')) return parseFactor(); // unary plus
				if (eat('-')) return -parseFactor(); // unary minus

				double x;
				int startPos = this.pos;
				if (eat('(')) { // parentheses
					x = parseExpression();
					eat(')');
				} else if ((ch >= '0' && ch <= '9') || ch == '.') { // numbers
					while ((ch >= '0' && ch <= '9') || ch == '.')
						nextChar();
					x = Double.parseDouble(str.substring(startPos, this.pos));
				} else if (ch >= 'a' && ch <= 'z') { // functions
					while (ch >= 'a' && ch <= 'z')
						nextChar();
					String func = str.substring(startPos, this.pos);
					x = parseFactor();
					if (func.equals("sqrt")) x = Math.sqrt(x);
					else if (func.equals("sin")) x = Math.sin(Math.toRadians(x));
					else if (func.equals("cos")) x = Math.cos(Math.toRadians(x));
					else if (func.equals("tan")) x = Math.tan(Math.toRadians(x));
					else throw new RuntimeException("Unknown function: " + func);
				} else {
					throw new RuntimeException("Unexpected: " + (char) ch);
				}

				if (eat('^')) x = Math.pow(x, parseFactor()); // exponentiation

				return x;
			}
		}.parse();
	}

	public static float toFloat(double d) {
		return (float) clamp(Float.MIN_VALUE, Float.MAX_VALUE, d);
	}
}
