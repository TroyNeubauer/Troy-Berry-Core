package com.troyberry.math;

import java.util.Random;

import com.troyberry.util.interpolation.Range;

/**
 * A static class that has various helpful math functions not in the {@link Math} class
 * 
 * @author Troy Neubauer
 * 
 */
public class Maths {

	private static Random random = new Random();
	/** PI as a float */
	public static final float PI = 3.14159265358979323846264f;

	/** 2 * PI */
	public static final float PI2 = PI * 2.0f;
	public static final double FLOAT_ROUNDING_ERROR = 0.00001f;

	public static final double ROOT_2 = StrictMath.sqrt(2.0);

	public static final float ROOT_2F = (float) ROOT_2;

	/**
	 * Returns the trigonometric sine of an angle as a float. This method uses {@link Math#sin(double)}
	 * 
	 * @param a
	 *            An angle, in radians.
	 * @return The sine of the argument.
	 */
	public static final float sinFloat(double a) {
		return (float) StrictMath.sin(a);
	}

	/**
	 * Returns the trigonometric sine of an angle as a float. This method uses {@link Math#sin(double)}
	 * 
	 * @param a
	 *            An angle, in degrees.
	 * @return The sine of the argument.
	 */
	public static final float sinFloatDegrees(double a) {
		return (float) StrictMath.sin(Math.toRadians(a));
	}

	/**
	 * Returns the trigonometric cosine of an angle as a float. This method uses {@link Math#cos(double)}
	 * 
	 * @param a
	 *            An angle, in radians.
	 * @return The cosine of the argument.
	 */
	public static final float cosFloat(double a) {
		return (float) StrictMath.cos(a);
	}

	/**
	 * Returns the trigonometric cosine of an angle as a float. This method uses {@link Math#cos(double)}
	 * 
	 * @param a
	 *            An angle, in degrees.
	 * @return The sine of the argument.
	 */
	public static final float cosFloatDegrees(double a) {
		return (float) StrictMath.cos(Math.toRadians(a));
	}

	/**
	 * Returns the trigonometric tangent of an angle as a float. This method uses {@link Math#tan(double)}
	 * 
	 * @param a
	 *            An angle, in radians.
	 * @return The tangent of the argument.
	 */
	public static final float tanFloat(double a) {
		return (float) StrictMath.tan(a);
	}

	/**
	 * Returns the trigonometric tangent of an angle as a float. This method uses {@link Math#tan(double)}
	 * 
	 * @param a
	 *            An angle, in degrees.
	 * @return The sine of the argument.
	 */
	public static final float tanFloatDegrees(double a) {
		return (float) StrictMath.cos(Math.toRadians(a));
	}

	/**
	 * Returns the float representation of base raised to the power of exp
	 * 
	 * @param base
	 *            The base
	 * @param exp
	 *            The exponent
	 * @return <code>(float)base^exp</code>
	 */
	public static float pow(float base, float exp) {
		return (float) StrictMath.pow(base, exp);
	}

	/**
	 * Raises the base to the power of the exponent and returns the result
	 * 
	 * @param base
	 *            The base
	 * @param exp
	 *            The power to raise the base to
	 * @return The result of base^exp
	 */
	public static long pow(long base, long exp) {
		if (exp == 0)
			return 1;
		if (exp == 1)
			return base;

		if (exp % 2 == 0) {
			long half = pow(base, exp / 2);
			return half * half;
		} else {
			long half = pow(base, (exp - 1) / 2);
			return base * half * half;
		}
	}

	/**
	 * Raises the base to the power of the exponent and returns the result
	 * 
	 * @param base
	 *            The base
	 * @param exp
	 *            The power to raise the base to
	 * @return The result of base^exp
	 */
	public static int pow(int base, int exp) {
		if (exp == 0)
			return 1;
		if (exp == 1)
			return base;

		if (exp % 2 == 0) {
			int half = pow(base, exp / 2);
			return half * half;
		} else {
			int half = pow(base, (exp - 1) / 2);
			return base * half * half;
		}
	}

	public static float map(float value, float sourceMin, float sourceMax, float destMin, float destMax) {
		float n = normalize(sourceMin, sourceMax, value);
		return lerp(destMin, destMax, n);
	}

	public static double map(double value, double sourceMin, double sourceMax, double destMin, double destMax) {
		double n = normalize(sourceMin, sourceMax, value);
		return lerp(destMin, destMax, n);
	}

	public static int normalize(int min, int max, int value) {
		return (value - min) / (max - min);
	}

	public static float normalize(float min, float max, float value) {
		return (value - min) / (max - min);
	}

	public static double normalize(double min, double max, double value) {
		return (value - min) / (max - min);
	}

	public static double gamma(double min, double max, double value, double slope) {
		return Math.pow((double) normalize(min, max, value), slope) * max + min;
	}

	/**
	 * Linearly interpolates between to factors a, and b with the factor f.<br>
	 * <b>Special cases:</b> The calculation will continue even if f is > 1.0f or < 0.0f, for a safe version of this calculation see
	 * {@link Maths#lerpSafe(float, float, float)}
	 * 
	 * @param a
	 *            The first number
	 * @param b
	 *            The second number
	 * @param f
	 *            The factor to interpolate between a and b
	 * @return The interpolated output
	 */
	public static float lerp(float a, float b, float f) {
		return a + f * (b - a);
	}

	/**
	 * Linearly interpolates between two factors a, and b with the factor f "safely".<br>
	 * The factor f is clamped between 0 and 1 so any values outside that range will be 0 or 1 {@link Maths#clamp(float, float, float)}
	 * 
	 * @param a
	 *            The first number
	 * @param b
	 *            The second number
	 * @param f
	 *            The factor to interpolate between a and b
	 * @return The interpolated output
	 */
	public static float lerpSafe(float a, float b, float f) {
		f = clamp(0.0f, 1.0f, f);
		return a + f * (b - a);
	}

	/**
	 * Linearly interpolates between two factors a, and b with the factor f.<br>
	 * <b>Special cases:</b> The calculation will continue even if f is > 1.0 or < 0.0, for a safe version of this calculation see
	 * {@link Maths#lerpSafe(double, double, double)}
	 * 
	 * @param a
	 *            The first number
	 * @param b
	 *            The second number
	 * @param f
	 *            The factor to interpolate between a and b
	 * @return The interpolated output
	 */
	public static double lerp(double a, double b, double f) {
		return a + f * (b - a);
	}

	/**
	 * Linearly interpolates between two factors a, and b with the factor f "safely".<br>
	 * The factor f is clamped between 0 and 1 so any values outside that range will be 0 or 1 {@link Maths#clamp(double, double, double)}
	 * 
	 * @param a
	 *            The first number
	 * @param b
	 *            The second number
	 * @param f
	 *            The factor to interpolate between a and b
	 * @return The interpolated output
	 */
	public static double lerpSafe(double a, double b, double f) {
		f = clamp(0.0, 1.0, f);
		return a + f * (b - a);
	}

	/**
	 * Interpolates using a cosine wave between the values a and b using the factor f<br>
	 * The calculation will continue even if f is > 1.0 or < 0.0, for a safe version of this calculation use
	 * 
	 * @param a
	 *            The first number
	 * @param b
	 *            The second number
	 * @param f
	 *            The factor to interpolate between a and b
	 * @return The interpolated output
	 */
	public static double cerp(double a, double b, double f) {
		return Maths.lerp(a, b, (1.0 - Math.cos(f * Math.PI)) * 0.5);
	}

	/**
	 * Interpolates using a cosine wave between the values a and b using the factor f<br>
	 * The factor f will be clamped between 0.0 and 1.0 before the calculation is carried out, therefore the result will always lie somewhere between
	 * and a b inclusive.
	 * 
	 * @param a
	 *            The first number
	 * @param b
	 *            The second number
	 * @param f
	 *            The factor to interpolate between a and b (automaticalld clamped between 0.0 and 1.0)
	 * @return The interpolated output
	 */
	public static double cerpSafe(double a, double b, double f) {
		f = Maths.clamp(a, b, f);
		return Maths.lerp(a, b, (1.0 - Math.cos(f * Math.PI)) * 0.5);
	}

	/**
	 * Interpolates using a cosine wave between the values a and b using the factor f<br>
	 * The calculation will continue even if f is > 1.0 or < 0.0, for a safe version of this calculation use
	 * 
	 * @param a
	 *            The first number
	 * @param b
	 *            The second number
	 * @param f
	 *            The factor to interpolate between a and b
	 * @return The interpolated output
	 */
	public static float cerp(float a, float b, float f) {
		return Maths.lerp(a, b, (float) (1.0 - Math.cos(f * Math.PI)) * 0.5f);
	}

	/**
	 * Interpolates using a cosine wave between the values a and b using the factor f<br>
	 * The factor f will be clamped between 0.0 and 1.0 before the calculation is carried out, therefore the result will always lie somewhere between
	 * and a b inclusive.
	 * 
	 * @param a
	 *            The first number
	 * @param b
	 *            The second number
	 * @param f
	 *            The factor to interpolate between a and b (automaticalld clamped between 0.0 and 1.0)
	 * @return The interpolated output
	 */
	public static float cerpSafe(float a, float b, float f) {
		f = Maths.clamp(a, b, f);
		return Maths.lerp(a, b, (float) (1.0 - Math.cos(f * Math.PI)) * 0.5f);
	}

	/**
	 * Returns the greatest integer less than or equal to the real number f.<br>
	 * IE: floor(0.9f) = 0, floor(5.0f) = 5, floor(-5.5f) = -6
	 * 
	 * @param f
	 *            The float to floor
	 * @return The floored result
	 */
	public static int floor(float f) {
		int xi = (int) f;
		return f < xi ? xi - 1 : xi;
	}

	/**
	 * Returns the greatest integer less or equal to than the real number d.<br>
	 * IE: floor(0.9) = 0, floor(5.0) = 5, floor(-5.5) = -6
	 * 
	 * @param d
	 *            The double to floor
	 * @return The floored result
	 */
	public static int floor(double d) {
		int xi = (int) d;
		return d < xi ? xi - 1 : xi;
	}

	/**
	 * Returns the greatest integer less or equal to than the real number d.<br>
	 * IE: floor(0.9) = 0, floor(5.0) = 5, floor(-5.5) = -6
	 * 
	 * @param d
	 *            The double to floor
	 * @return The floored result
	 */
	public static long floorLong(double d) {
		long xi = (long) d;
		return d < xi ? xi - 1 : xi;
	}

	/**
	 * Returns the lowest integer greater than or equal to the real number f.<br>
	 * IE: ceil(0.9f) = 1, ceil(5.0f) = 5, ceil(-5.5f) = -5
	 * 
	 * @param f
	 *            The float to floor
	 * @return The floored result
	 */
	public static int ceil(float f) {
		int xi = (int) f;
		return f > xi ? xi + 1 : xi;
	}

	/**
	 * Returns the lowest integer greater than or equal to the real number d.<br>
	 * IE: ceil(0.9) = 1, ceil(5.0) = 5, ceil(-5.5) = -5
	 * 
	 * @param f
	 *            The float to floor
	 * @return The floored result
	 */
	public static int ceil(double d) {
		int xi = (int) d;
		return d > xi ? xi + 1 : xi;
	}

	/**
	 * Rounds the float f to the nearest integer.<br>
	 * This method abides by Math rules therefore 0.5f will be rounded up. IE: round(5.5f) = 6, round(0.3f) = 0, round(-0.7f) = -1, round(-0.3f) = 0
	 * 
	 * @param f
	 *            The float to round
	 * @return The rounded integer
	 */
	public static int round(float f) {
		if (f >= 0f)
			return (int) (f + 0.5f);
		else
			return (int) (f - 0.5f);
	}

	/**
	 * Rounds the double d to the nearest integer.<br>
	 * This method abides by Math rules therefore 0.5 will be rounded up. IE: round(5.5) = 6, round(0.3) = 0, round(-0.7) = -1, round(-0.3) = 0
	 * 
	 * @param d
	 *            The double to round
	 * @return The rounded integer
	 */
	public static int round(double d) {
		if (d >= 0.0)
			return (int) (d + 0.5);
		else
			return (int) (d - 0.5);
	}

	/**
	 * Clamps a value between a minimum and a maxiumn.<br>
	 * Any values lower than the min will cause min to be returned. Any values greater than the max will cause max to be returned. Any values in
	 * between will return themselves.<br>
	 * <br>
	 * clamp(min, max, value)<br>
	 * IE: clamp(0, 10, 5) = 5, clamp(0, 10, -5) = 0, clamp(0, 10, 50) = 10, clamp(0, 10, 0) = 0, clamp(0, 10, 9999) = 10<br>
	 * <br>
	 * Because pointers don't exist in Java, in order to clamp the integer i in between 0 and 10, the following code must be used:<code><br>
	 * int i = -1000;<br>
	 * i = Maths.clamp(0, 10, i);<br>
	 * </code><br>
	 * After this code segment, i will have the value of 0
	 * 
	 * @param min
	 *            The minimum value
	 * @param max
	 *            The maximum value
	 * @param value
	 *            The value to check
	 * @return The clamped value
	 */
	public static int clamp(int min, int max, int value) {
		return Math.max(Math.min(value, max), min);
	}

	/**
	 * Clamps a value between a minimum and a maxiumn.<br>
	 * Any values lower than the min will cause min to be returned. Any values greater than the max will cause max to be returned. Any values in
	 * between will return themselves.<br>
	 * <br>
	 * clamp(min, max, value)<br>
	 * IE: clamp(0.0f, 10.0f, 5.5f) = 5.5f, clamp(0.0f, 10.0f, -5.0f) = 0.0f, clamp(0.0f, 10.0f, 50.0f) = 10.0f, clamp(0.0f, 10.0f, -0.1f) = 0.0f,
	 * clamp(0.0f, 10.0f, 9999.5f) = 10.0f <br>
	 * <br>
	 * Because pointers don't exist in Java, in order to clamp the float i in between 0.0f and 10.0f, the following code must be used:<code><br>
	 * float i = -1000.0f;<br>
	 * i = Maths.clamp(0.0f, 10.0f, i);<br>
	 * </code><br>
	 * After this code segment, i will have the value of 0
	 * 
	 * @param min
	 *            The minimum value
	 * @param max
	 *            The maxiumn value
	 * @param value
	 *            The value to check
	 * @return The clamped value
	 */
	public static float clamp(float min, float max, float value) {
		return Math.max(Math.min(value, max), min);
	}

	/**
	 * Clamps a value between a minimum and a maxiumn.<br>
	 * Any values lower than the min will cause min to be returned. Any values greater than the max will cause max to be returned. Any values in
	 * between will return themselves.<br>
	 * <br>
	 * clamp(min, max, value)<br>
	 * IE: clamp(0.0, 10.0, 5.5) = 5.5, clamp(0.0, 10.0, -5.0) = 0.0, clamp(0.0, 10.0, 50.0) = 10.0, clamp(0.0, 10.0, -0.1) = 0.0, clamp(0.0, 10.0,
	 * 9999.5) = 10.0 <br>
	 * <br>
	 * Because pointers don't exist in Java, in order to clamp the integer i in between 0 and 10, the following code must be used:<code><br>
	 * int i = -1000;<br>
	 * i = Maths.clamp(0, 10, i);<br>
	 * </code><br>
	 * After this code segment, i will have the value of 0
	 * 
	 * @param min
	 *            The minimum value
	 * @param max
	 *            The maxiumn value
	 * @param value
	 *            The value to check
	 * @return The clamped value
	 */
	public static double clamp(double min, double max, double value) {
		return Math.max(Math.min(value, max), min);
	}

	public static float randRange(float min, float max) {
		return (min + (float) Math.random() * (max - min));
	}

	/**
	 * @return a random double between the ranges inclusive
	 */
	public static double randRange(double min, double max) {
		return (min + random.nextDouble() * (max - min + 1));
	}

	/**
	 * @return a random int between the ranges inclusive of min and exclusive of max
	 */
	public static int randRange(int min, int max) {
		return floor(min + random.nextDouble() * (max - min));
	}

	public static float randRange(float min, float max, Random random) {
		return (min + (float) (Math.random()) * (max - min));
	}

	/**
	 * @return a random double between the ranges inclusive
	 */
	public static double randRange(double min, double max, Random random) {
		return (min + random.nextDouble() * (max - min + 1));
	}

	/**
	 * @return a random int between the ranges inclusive of min and exclusive of max
	 */
	public static int randRange(int min, int max, Random random) {
		return floor(min + random.nextDouble() * (max - min));
	}

	/**
	 * @return a random long between the ranges inclusive of min and exclusive of max
	 */
	public static long randRange(long min, long max) {
		return floorLong(min + random.nextDouble() * (max - min));
	}

	public static double randRange(Range value) {
		return randRange(value.getMax(), value.getMax());
	}

	public static float randRangeFloat(Range value) {
		return (float) randRange(value.getMax(), value.getMax());
	}

	/**
	 * Returns a float usually in the range [-1.0f, 1.0f]<br>
	 * Equivalent to calling <code>{@link Random#nextGaussian()} / 5.0</code>
	 * 
	 * @return The gaussian float
	 */
	public static float guassianFloat() {
		return (float) (random.nextGaussian() / 5.0);
	}

	/**
	 * Returns a double usually in the range [-1.0, 1.0]<br>
	 * Equivalent to calling <code>{@link Random#nextGaussian()} / 5.0</code>
	 * 
	 * @return The gaussian double
	 */
	public static double guassianDouble() {
		return random.nextGaussian() / 5.0;
	}

	/**
	 * Returns a float usually in the range [-1.0f, 1.0f] using the specified random number generator<br>
	 * Equivalent to calling <code>{@link Random#nextGaussian()} / 5.0</code>
	 * 
	 * @param random
	 *            The random number generator to use
	 * @return The gaussian float
	 */
	public static float guassianFloat(Random random) {
		return (float) (random.nextGaussian() / 5.0);
	}

	/**
	 * Returns a double usually in the range [-1.0, 1.0] using the specified random number generator<br>
	 * Equivalent to calling <code>{@link Random#nextGaussian()} / 5.0</code>
	 * 
	 * @param random
	 *            The random number generator to use
	 * @return The gaussian double
	 */
	public static double guassianDouble(Random random) {
		return random.nextGaussian() / 5.0;
	}

	/**
	 * Calculates the euclidean distance from a point to a line segment.
	 *
	 * @param vec
	 *            the point
	 * @param a
	 *            start of line segment
	 * @param b
	 *            end of line segment
	 * @return distance from v to line segment [a,b]
	 *
	 */
	public static float distanceToSegment(final Vector3f a, final Vector3f b, final Vector3f vec) {
		final Vector3f ab = b.subtract(a);
		final Vector3f av = vec.subtract(a);

		if (Vector3f.dot(av, ab) <= 0.0) // Point is lagging behind start of the segment, so perpendicular distance is not viable.
			return av.modulus(); // Use distance to start of segment instead.

		final Vector3f bv = vec.subtract(b);

		if (Vector3f.dot(bv, ab) >= 0.0) // Point is advanced past the end of the segment, so perpendicular distance is not viable.
			return bv.modulus(); // Use distance to end of the segment instead.

		return Vector3f.cross(ab, av).modulus() / ab.modulus(); // Perpendicular distance of point to segment.
	}

	/**
	 * Calculates the euclidean distance from a point to a line segment.
	 *
	 * @param vec
	 *            the point
	 * @param a
	 *            start of line segment
	 * @param b
	 *            end of line segment
	 * @return distance from v to line segment [a,b]
	 *
	 */
	public static double distanceToSegment(final Vector3d a, final Vector3d b, final Vector3d vec) {
		final Vector3d ab = b.subtract(a);
		final Vector3d av = vec.subtract(a);

		if (Vector3d.dot(av, ab) <= 0.0) // Point is lagging behind start of the segment, so perpendicular distance is not viable.
			return av.modulus(); // Use distance to start of segment instead.

		final Vector3d bv = vec.subtract(b);

		if (Vector3d.dot(bv, ab) >= 0.0) // Point is advanced past the end of the segment, so perpendicular distance is not viable.
			return bv.modulus(); // Use distance to end of the segment instead.

		return Vector3d.cross(ab, av).modulus() / ab.modulus(); // Perpendicular distance of point to segment.
	}

	public static double calculateVolumeOfASphere(double radius) {
		return ((4d / 3d) * Math.PI * Math.pow(radius, 3));
	}

	public static double solveForRaduisGivenASphere(double volume) {
		return Math.cbrt(volume / (4d / 3d) * Math.PI);
	}

	public static float length(float x, float y, float z) {
		return (float) Math.sqrt(x * x + y * y + z * z);
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

	public static float getDistanceBetweenPoints(float x, float y, float z, float x2, float y2, float z2) {
		return (float) Math.sqrt(Math.pow(x - x2, 2.0) + Math.pow(y - y2, 2.0) + Math.pow(z - z2, 2.0));
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
		return Math.abs(Math.abs(point1.x) - Math.abs(point2.x)) + Math.abs(Math.abs(point1.y) - Math.abs(point2.y)) + +Math.abs(Math.abs(point1.z) - Math.abs(point2.z));
	}

	public static float degreesToRadians(float degrees) {
		return (float) Math.toRadians(degrees);
	}

	public static float radiansToDegrees(float degrees) {
		return (float) Math.toDegrees(degrees);
	}

	/**
	 * Evaluates a mathematical expression using the order od operations<br>
	 * This method supports:<br>
	 * <ul>
	 *
	 * <li>addition (+), subtraction (-), multiplication (*), and division (/) of terms. (10 + 4), (7 * 2)</li>
	 * <li>unary plus (+), and unary minus (-). (-10), (+10)</li>
	 * <li>unary plus (+), and unary minus (-). (-10), (+10)</li>
	 * <li>all 6 basic trig functions sine "sin", cosine "cos", tangent "tan", arc sine "asin", arc cosine "acos", arc tangent "atan"</li>
	 * <li>Square root "sqrt" and exponents "^"</li>
	 * <li>Uses of the constants pi "pi", and e "e" (upper or lowercase)</li>
	 * </ul>
	 * Examples:<br>
	 * "2 + 2" returns 4.0<br>
	 * "sin90" (in degree mode) returns 1.0<br>
	 * "sin1.5708" (in radians mode) returns 1.0<br>
	 * "PI" returns 3.141592653589793<br>
	 * "(4/3) * PI * 10^3" (the volume of a radius 10 sphere) returns 4188.79<br>
	 * "75 + (3-2)^6 * sin90" returns 76.0<br>
	 * 
	 * @param expression
	 *            The expression to parse
	 * @return The result of the expression with
	 * @throws NumberFormatException
	 *             If the
	 */
	public static double evaluate(String expression, final boolean degrees) throws NumberFormatException {
		String pi = Double.toString(Math.PI);
		String constantE = Double.toString(Math.E);

		expression = expression.replaceAll("pi", pi);
		expression = expression.replaceAll("PI", pi);
		expression = expression.replaceAll("e", constantE);
		final String e = expression.replaceAll("E", constantE);
		return new Object() {
			int pos = -1, ch;

			void nextChar() {
				ch = (++pos < e.length()) ? e.charAt(pos) : -1;
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
				if (pos < e.length()) {
					throw new NumberFormatException("Unexpected: " + (char) ch);
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
					if (eat('+'))
						x += parseTerm(); // addition
					else if (eat('-'))
						x -= parseTerm(); // subtraction
					else
						return x;
				}
			}

			double parseTerm() {
				double x = parseFactor();
				for (;;) {
					if (eat('*'))
						x *= parseFactor(); // multiplication
					else if (eat('/'))
						x /= parseFactor(); // division
					else
						return x;
				}
			}

			double parseFactor() {
				if (eat('+'))
					return parseFactor(); // unary plus
				if (eat('-'))
					return -parseFactor(); // unary minus
				double x;
				int startPos = this.pos;
				if (eat('(')) { // parentheses
					x = parseExpression();
					eat(')');
				} else if ((ch >= '0' && ch <= '9') || ch == '.') { // numbers
					while ((ch >= '0' && ch <= '9') || ch == '.')
						nextChar();
					x = Double.parseDouble(e.substring(startPos, this.pos));
				} else if (ch >= 'a' && ch <= 'z') { // functions
					while (ch >= 'a' && ch <= 'z')
						nextChar();
					String func = e.substring(startPos, this.pos);
					x = parseFactor();
					if (func.equalsIgnoreCase("sqrt"))
						x = Math.sqrt(x);
					else if (func.equalsIgnoreCase("sin"))
						x = Math.sin(degrees ? Math.toRadians(x) : x);
					else if (func.equalsIgnoreCase("cos"))
						x = Math.cos(degrees ? Math.toRadians(x) : x);
					else if (func.equalsIgnoreCase("tan"))
						x = Math.tan(degrees ? Math.toRadians(x) : x);
					else if (func.equalsIgnoreCase("asin"))
						x = Math.asin(degrees ? Math.toRadians(x) : x);
					else if (func.equalsIgnoreCase("acos"))
						x = Math.acos(degrees ? Math.toRadians(x) : x);
					else if (func.equalsIgnoreCase("atan"))
						x = Math.atan(degrees ? Math.toRadians(x) : x);
					else
						throw new NumberFormatException("Unknown function: " + func);
				} else {
					throw new NumberFormatException("Unexpected: " + (char) ch);
				}

				if (eat('^'))
					x = Math.pow(x, parseFactor()); // exponentiation

				return x;
			}
		}.parse();
	}

	/**
	 * Evaluates a mathematical expression with the default trig mode of radians<br>
	 * To parse with degrees, use <code>Maths.evaulate(expression, true);</code> Examples:<br>
	 * "75 + (3-2)^6 * sin90" returns 76
	 * 
	 * @param expression
	 * @return
	 * @throws NumberFormatException
	 *             If the
	 */
	public static double evaluate(final String expression) throws NumberFormatException {
		return evaluate(expression, false);
	}

	/**
	 * Calculates the spherical distance between 2 points of latitude and longitude using the Haversine formula
	 * 
	 * @param latitude1
	 *            Latitude of point 1
	 * @param longitude1
	 *            Longitude of point 1
	 * @param latitude2
	 *            Latitude of point 2
	 * @param longitude2
	 *            Longitude of point 2
	 * @param radius
	 *            The radius of the sphere that points are on
	 * @return The spherical distance between points one and two
	 */

	public static double sphereDistanceFast(double latitude1, double longitude1, double latitude2, double longitude2, double radius) {
		double lat1 = Math.toRadians(latitude1);
		double lon1 = Math.toRadians(longitude1);
		double lat2 = Math.toRadians(latitude2);
		double lon2 = Math.toRadians(longitude2);

		double d_lat = Math.abs(lat1 - lat2);
		double d_lon = Math.abs(lon1 - lon2);

		double a = Math.pow(Math.sin(d_lat / 2), 2) + Math.cos(lat1) * Math.cos(lat2) * Math.pow(Math.sin(d_lon / 2), 2);

		// double d_sigma = 2 * atan2(sqrt(a), sqrt(1 - a));
		double d_sigma = 2 * Math.asin(Math.sqrt(a));

		return radius * d_sigma;
	}

	/**
	 * Calculates the spherical distance between 2 points of latitude and longitude using the Vincenty formula
	 * 
	 * @param latitude1
	 *            Latitude of point 1
	 * @param longitude1
	 *            Longitude of point 1
	 * @param latitude2
	 *            Latitude of point 2
	 * @param longitude2
	 *            Longitude of point 2
	 * @param radius
	 *            The radius of the sphere that points are on
	 * @return The spherical distance between points one and two
	 */
	public static double sphereDistanceSlow(double latitude1, double longitude1, double latitude2, double longitude2, double radius) {
		double lat1 = Math.toRadians(latitude1);
		double lon1 = Math.toRadians(longitude1);
		double lat2 = Math.toRadians(latitude2);
		double lon2 = Math.toRadians(longitude2);

		double d_lon = Math.abs(lon1 - lon2);

		// Numerator
		double a = Math.pow(Math.cos(lat2) * Math.sin(d_lon), 2);

		double b = Math.cos(lat1) * Math.sin(lat2);
		double c = Math.sin(lat1) * Math.cos(lat2) * Math.cos(d_lon);
		double d = Math.pow(b - c, 2);

		double e = Math.sqrt(a + d);

		// Denominator
		double f = Math.sin(lat1) * Math.sin(lat2);
		double g = Math.cos(lat1) * Math.cos(lat2) * Math.cos(d_lon);

		double h = f + g;

		double d_sigma = Math.atan2(e, h);

		return radius * d_sigma;
	}

	/**
	 * This method converts a float to a double preventing sign flipping or other other weird issues if the double is out of the representable range
	 * for a float.<br>
	 * If the double is greater then a float's max value, {@link Float.MAX_VALUE} will be returned<br>
	 * Conversely, if the double is less than a float's min value, {@link Float.MIN_VALUE}<br>
	 * Equivalent to calling<br>
	 * <code>
	 * <t>float f = (float) Maths.clamp({@link Float#MIN_VALUE}, {@link Float#MAX_VALUE}, d)
	 * </code>
	 * 
	 * @param d
	 *            The value to convert
	 * @return The converted float ({@link Float#MIN_VALUE} <= float <= {@link Float#MAX_VALUE})
	 */
	public static float toFloat(double d) {
		return (float) clamp(Float.MIN_VALUE, Float.MAX_VALUE, d);
	}

	public static int toInt(long value) {
		return (int) clamp(Integer.MIN_VALUE, Integer.MAX_VALUE, value);
	}

	public static float sqrt(float vaule) {
		return (float) Math.sqrt(vaule);
	}

	public static int logBase2Int(int bits) // returns 0 for bits=0
	{
		int log = 0;
		if ((bits & 0xffff0000) != 0) {
			bits >>>= 16;
			log = 16;
		}
		if (bits >= 256) {
			bits >>>= 8;
			log += 8;
		}
		if (bits >= 16) {
			bits >>>= 4;
			log += 4;
		}
		if (bits >= 4) {
			bits >>>= 2;
			log += 2;
		}
		return log + (bits >>> 1);
	}

	public static long logBase2Int(long bits) // returns 0 for bits=0
	{
		long log = 0;
		if ((bits & 0xffff0000) != 0) {
			bits >>>= 16;
			log = 16;
		}
		if (bits >= 256) {
			bits >>>= 8;
			log += 8;
		}
		if (bits >= 16) {
			bits >>>= 4;
			log += 4;
		}
		if (bits >= 4) {
			bits >>>= 2;
			log += 2;
		}
		return log + (bits >>> 1);
	}

	public static Vector3d calculateNormal(Vector3d p1, Vector3d p2, Vector3d p3) {
		return Vector3d.cross(Vector3d.subtract(p2, p1), Vector3d.subtract(p3, p1));

	}

	public static Vector3f calculateNormalFloat(Vector3d p1, Vector3d p2, Vector3d p3) {
		return Vector3d.crossFloat(Vector3d.subtract(p2, p1), Vector3d.subtract(p3, p1));
	}

	public static double minTriangleDistance(Vector3d p1, Vector3d p2, Vector3d p3, Vector3d point) {
		Vector3d B = p1;
		Vector3d E0 = Vector3d.subtract(p2, B);
		Vector3d E1 = Vector3d.subtract(p3, B);
		Vector3d D = Vector3d.subtract(B, point);
		double a = Vector3d.dot(E0, E0);
		double b = Vector3d.dot(E0, E1);
		double c = Vector3d.dot(E1, E1);
		double d = Vector3d.dot(E0, D);
		double e = Vector3d.dot(E1, D);
		double f = Vector3d.dot(D, D);

		double det = a * c - b * b;
		double s = b * e - c * d;
		double t = b * d - a * e;

		double sqrdistance = 0.0;

		if (s + t <= det) {
			if (s < 0.0) {
				if (t < 0.0) {
					// region4
					if (d < 0) {
						t = 0.0;
						if (-d >= a) {
							s = 1.0;
							sqrdistance = a + 2.0 * d + f;
						} else {
							s = -d / a;
							sqrdistance = d * s + f;
						}
					} else {
						s = 0.0;
						if (e >= 0.0) {
							t = 0.0;
							sqrdistance = f;
						} else {
							if (-e >= c) {
								t = 1.0;
								sqrdistance = c + 2.0 * e + f;
							} else {
								t = -e / c;
								sqrdistance = e * t + f;
							}
						}

						// of region 4
					}
				} else {
					// region 3
					s = 0;
					if (e >= 0) {
						t = 0;
						sqrdistance = f;
					} else {
						if (-e >= c) {
							t = 1;
							sqrdistance = c + 2.0 * e + f;
						} else {
							t = -e / c;
							sqrdistance = e * t + f;
							// of region 3
						}
					}
				}
			} else {
				if (t < 0) {
					// region 5;
					t = 0;
					if (d >= 0) {
						s = 0;
						sqrdistance = f;
					} else {
						if (-d >= a) {
							s = 1;
							sqrdistance = a + 2.0 * d + f; // GF 20101013 fixed typo d*s ->2*d
						} else {
							s = -d / a;
							sqrdistance = d * s + f;
						}
					}
				} else {
					// region 0
					double invDet = 1.0 / det;
					s = s * invDet;
					t = t * invDet;
					sqrdistance = s * (a * s + b * t + 2.0 * d) + t * (b * s + c * t + 2.0 * e) + f;
				}
			}

		} else {
			if (s < 0.0) {
				// region 2
				double tmp0 = b + d;
				double tmp1 = c + e;
				if (tmp1 > tmp0) {// # minimum on edge s+t=1
					double numer = tmp1 - tmp0;
					double denom = a - 2.0 * b + c;
					if (numer >= denom) {
						s = 1.0;
						t = 0.0;
						sqrdistance = a + 2.0 * d + f; // GF 20101014 fixed typo 2*b -> 2*d;
					} else {
						s = numer / denom;
						t = 1 - s;
						sqrdistance = s * (a * s + b * t + 2 * d) + t * (b * s + c * t + 2 * e) + f;
					}
				} else { // minimum on edge s=0
					s = 0.0;
					if (tmp1 <= 0.0) {
						t = 1;
						sqrdistance = c + 2.0 * e + f;
					} else {
						if (e >= 0.0) {
							t = 0.0;
							sqrdistance = f;
						} else {
							t = -e / c;
							sqrdistance = e * t + f;
							// of region 2
						}
					}
				}
			} else {
				if (t < 0.0) {
					// region6
					double tmp0 = b + e;
					double tmp1 = a + d;
					if (tmp1 > tmp0) {
						double numer = tmp1 - tmp0;
						double denom = a - 2.0 * b + c;
						if (numer >= denom) {
							t = 1.0;
							s = 0;
							sqrdistance = c + 2.0 * e + f;
						} else {
							t = numer / denom;
							s = 1 - t;
							sqrdistance = s * (a * s + b * t + 2.0 * d) + t * (b * s + c * t + 2.0 * e) + f;
						}
					} else {
						t = 0.0;
						if (tmp1 <= 0.0) {
							s = 1;
							sqrdistance = a + 2.0 * d + f;
						} else {
							if (d >= 0.0) {
								s = 0.0;
								sqrdistance = f;
							} else {
								s = -d / a;
								sqrdistance = d * s + f;
							}
						}
					}
				} else {
					// region 1
					double numer = c + e - b - d;
					if (numer <= 0) {
						s = 0.0;
						t = 1.0;
						sqrdistance = c + 2.0 * e + f;

					} else {
						double denom = a - 2.0 * b + c;
						if (numer >= denom) {
							s = 1.0;
							t = 0.0;
							sqrdistance = a + 2.0 * d + f;
						} else {
							s = numer / denom;
							t = 1 - s;
							sqrdistance = s * (a * s + b * t + 2.0 * d) + t * (b * s + c * t + 2.0 * e) + f;
						}
					}
				}
			}
		}

		// account for numerical round-off error
		if (sqrdistance < 0)
			sqrdistance = 0;

		return Math.sqrt(sqrdistance);

	}

	public static boolean rayTriangleIntersect(Vector3d o, Vector3d d, Vector3d v1, Vector3d v2, Vector3d v3) {
		double epsilon = 0.000001;

		Vector3d e1 = Vector3d.subtract(v2, v1);
		Vector3d e2 = Vector3d.subtract(v3, v1);
		Vector3d q = Vector3d.cross(d, e2);
		double a = Vector3d.dot(e1, q); // Determinant of the matrix M

		if (a > -epsilon && a < epsilon) {
			return false;
		} // The vector is parallel to the plane (the intersection is at infinity)

		double f = 1 / a;
		Vector3d s = Vector3d.subtract(o, v1);
		double u = f * Vector3d.dot(s, q);

		if (u < 0.0)
			return false;// The intersection is outside of the triangle

		Vector3d r = Vector3d.cross(s, e1);
		double v = f * Vector3d.dot(d, r);

		if (v < 0.0 || u + v > 1.0)
			return false;
		d = Vector3d.normalise(d);
		v1 = Vector3d.normalise(v1);
		v2 = Vector3d.normalise(v2);
		v3 = Vector3d.normalise(v3);
		double maxDotP = Vector3d.dot(d, v1);
		maxDotP = Math.max(maxDotP, Vector3d.dot(d, v2));
		maxDotP = Math.max(maxDotP, Vector3d.dot(d, v3));

		return maxDotP > 0.0;
	}

	public static boolean rayTriangleIntersect(Vector3f o, Vector3f d, Vector3f v1, Vector3f v2, Vector3f v3) {
		float epsilon = 0.00001f;

		Vector3f e1 = Vector3f.subtract(v2, v1);
		Vector3f e2 = Vector3f.subtract(v3, v1);
		Vector3f q = Vector3f.cross(d, e2);
		float a = Vector3f.dot(e1, q); // Determinant of the matrix M

		if (a > -epsilon && a < epsilon) {
			return false;
		} // The vector is parallel to the plane (the intersection is at infinity)

		float f = 1 / a;
		Vector3f s = Vector3f.subtract(o, v1);
		float u = f * Vector3f.dot(s, q);

		if (u < 0.0)
			return false;// The intersection is outside of the triangle

		Vector3f r = Vector3f.cross(s, e1);
		float v = f * Vector3f.dot(d, r);

		if (v < 0.0 || u + v > 1.0)
			return false;

		return true;
	}

	public static double getLatitude(double r, Vector3d vec) {
		return getLatitude(r, vec.x, vec.y, vec.z);
	}

	public static double getLatitude(double r, double x, double y, double z) {
		return Math.toDegrees(Math.asin(z / r));
	}

	public static double getLongitude(double r, Vector3d vec) {
		return getLongitude(r, vec.x, vec.y, vec.z);
	}

	public static double getLongitude(double r, double x, double y, double z) {
		if (x > 0) {
			return Math.toDegrees(Math.atan(y / x));
		} else if (y > 0) {
			return Math.toDegrees(Math.atan(y / x)) + 180;
		} else {
			return Math.toDegrees(Math.atan(y / x)) - 180;
		}
	}

	// y = r sin(long) sin(lat)
	// z = r cos(lat)

	public static double getX(double radius, double latitude, double longitude) {
		return radius * Math.cos(Math.toRadians(longitude)) * Math.sin(Math.toRadians(latitude));
	}

	public static double getY(double radius, double latitude, double longitude) {
		return radius * Math.sin(Math.toRadians(longitude)) * Math.sin(Math.toRadians(latitude));
	}

	public static double getZ(double radius, double latitude, double longitude) {
		return radius * Math.cos(Math.toRadians(latitude));
	}
}
