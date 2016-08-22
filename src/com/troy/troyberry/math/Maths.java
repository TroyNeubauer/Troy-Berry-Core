package com.troy.troyberry.math;

public class Maths {
	
	public static int floor(float f) {
		return (int) f;
	}
	
	public static int celi(float f) {
		return (int) (f + 1f);
	}
	
	public static int round(float f) {
		return (int) (f + 0.5f);
	}

	public static Vector2f getRowAndColom(final int width, final int value) {
		return new Vector2f(value % width, value / width);
	}

	public static int clamp(final int min, final int max, int value) {
		return Math.max(Math.min(value, max), min);
	}

	public static float clamp(float min, float max, float value) {
		return Math.max(Math.min(value, max), min);
	}

	public static float randRange(float min, float max) {
		return (min + (float) (Math.random()) * (max - min));
	}

	public static int randomInt(int min, int max) {
		return (int) Math.floor(min + Math.random() * (max - min + 1));
	}

	public static double calculateVolumeOfASphere(double radius) {
		return ((4d / 3d) * Math.PI * Math.pow(radius, 3));
	}

	public static double SolveForRaduisGivenASphere(double volume) {
		return Math.cbrt(volume / (4d / 3d) * Math.PI);
	}

	public static double getDistanceBetweenPoints(double x1, double y1, double x2, double y2) {
		return (double) Math.sqrt(Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2));
	}

	public static double getDistanceBetweenPoints(Vector2f point1, Vector2f point2) {
		return (double) Math.sqrt(Math.pow(point1.x - point2.x, 2) + Math.pow(point1.y - point2.y, 2));
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

	public static boolean rangeIntersect(float min0, float max0, float min1, float max1) {
		return Math.max(min0, max0) >= Math.min(min1, max1) && Math.min(min0, max0) <= Math.max(min1, max1);
	}

	public static float average(float... input) {
		float count = 0;
		float total = 0f;
		for (int i = 0; i < input.length; i++) {
			count++;
			total += ((float)input[i]);
		}
		return total / (float) count;
	}
	
	public static float average(int... input) {
		float count = 0;
		float total = 0f;
		for (int i = 0; i < input.length; i++) {
			count++;
			total += ((float)input[i]);
		}
		return ((float)total) / ((float) count);
	}

	public static boolean isNegative(float value) {
		return value < 0.0f;
	}

	public static boolean isPosative(float value) {
		return value > 0.0f;
	}
}
