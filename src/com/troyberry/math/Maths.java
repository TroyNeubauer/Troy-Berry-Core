package com.troyberry.math;

import java.util.*;

public class Maths {

	private static Random random = new Random();
    public static final float PI = 3.14159265358979323846264f;
    
    public static final float sin(double angle){
    	return (float)StrictMath.sin(angle);
    }
    
    public static final float cos(double angle){
    	return (float)StrictMath.cos(angle);
    }
    
    public static final float tan(double angle){
    	return (float)StrictMath.tan(angle);
    }
	
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

	public static float lerp(float a, float b, float factor) {
		return a + factor * (b - a);
	}
	
	public static float lerpSafe(float a, float b, float factor) {
		factor = clamp(0.0f, 1.0f, factor);
		return a + factor * (b - a);
	}

	public static double lerp(double a, double b, double f) {
		return a + f * (b - a);
	}

	public static int floor(float f) {
		int xi = (int) f;
		return f < xi ? xi - 1 : xi;
	}

	public static int celi(float f) {
		return (int) (f + 1f);
	}
	
	public static int celi(double d) {
		return (int) (d + 1.0);
	}

	public static int round(float f) {
		if (f >= 0f) return (int) (f + 0.5f);
		else return (int) (f - 0.5f);
	}

	public static int round(double d) {
		if (d >= 0.0) return (int) (d + 0.5);
		else return (int) (d - 0.5);
	}

	public static int clamp(int min, int max, int value) {
		return Math.max(Math.min(value, max), min);
	}

	public static float clamp(float min, float max, float value) {
		return Math.max(Math.min(value, max), min);
	}

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

	public static double getDistanceBetweenPoints(Vector3f vector3f, Vector3d position) {
		return (double) Math.sqrt(Math.pow(vector3f.x - position.x, 2) + Math.pow(vector3f.y - position.y, 2));
	}
	
	public static double getDistanceBetweenPoints(Vector2d a, Vector2d b) {
		return getDistanceBetweenPoints(a.x, a.y, b.x, b.y);
	}
	
	public static float getDistanceBetweenPoints(Vector3f point1, Vector3f point2) {
		return (float) Math.sqrt(Math.pow(point1.x - point2.x, 2) + Math.pow(point1.y - point2.y, 2) + Math.pow(point1.z - point2.z, 2));
	}
	
	public static double getDistanceBetweenPoints(Vector3d left, Vector3d right) {
		return (double) Math.sqrt(Math.pow(left.x - right.x, 2) + Math.pow(left.y - right.y, 2) + Math.pow(left.z - right.z, 2));
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
