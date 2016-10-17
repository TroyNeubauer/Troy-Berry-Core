package com.troy.troyberry.math;

import java.util.*;

public class Maths {

	private static Random random = new Random();

	public static int millasecondsToUpdates(int ms, int ups) {
		return Math.round(((float) ms / 1000f) * (float) ups);
	}

	public static float lerp(float a, float b, float f) {
		return a + f * (b - a);
	}

	public static double lerp(double a, double b, double f) {
		return a + f * (b - a);
	}

	public static int floor(float f) {
		return (int) f;
	}

	public static int celi(float f) {
		return (int) (f + 1f);
	}

	public static int round(float f) {
		if (f > 0f) return (int) (f + 0.5f);
		else return (int) (f - 0.5f);
	}

	public static int round(double d) {
		return (int) Math.round(d);
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

	public static double clamp(double min, double max, double value) {
		return Math.max(Math.min(value, max), min);
	}

	public static float randRange(float min, float max) {
		return (min + (float) (Math.random()) * (max - min));
	}

	/**@return a random double between the ranges inclusive 
	 * */
	public static double randRange(double min, double max) {
		return (min + random.nextDouble() * (max - min));
	}

	/**@return a random int between the ranges inclusive 
	 * */
	public static int randRange(int min, int max) {
		return round(min + random.nextDouble() * (max - min));
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
		return value >= Math.min(min, max) && value < Math.max(min, max);
	}

	public static boolean inRange(int value, int min, int max) {
		return value >= Math.min(min, max) && value <= Math.max(min, max);
	}

	public static boolean rangeIntersect(float min0, float max0, float min1, float max1) {
		return Math.max(min0, max0) >= Math.min(min1, max1) && Math.min(min0, max0) <= Math.max(min1, max1);
	}

	public static boolean intersect(int min, int max, int point) {
		return point >= min && point <= max;
	}

	public static float average(float... input) {
		float count = 0;
		float total = 0f;
		for (int i = 0; i < input.length; i++) {
			count++;
			total += ((float) input[i]);
		}
		return total / (float) count;
	}

	public static float average(int... input) {
		float count = 0;
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

	public static double getDistanceBetweenPoints(Vector2d a, Vector2d b) {
		return getDistanceBetweenPoints(a.x, a.y, b.x, b.y);
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
}
