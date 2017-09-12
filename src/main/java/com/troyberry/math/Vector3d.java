package com.troyberry.math;

import java.nio.*;
import java.util.*;

import com.troyberry.util.interpolation.*;

public final class Vector3d implements Interpolatable<Vector3d> {

	public static final int ELEMENTS = 3;
	public static final int BYTES = ELEMENTS * Double.BYTES, BITS = ELEMENTS * Double.SIZE;
	public static final Vector3d UP = new Vector3d(0, +1, 0), DOWN = new Vector3d(0, -1, 0), POS_X = new Vector3d(+1, 0, 0), NEG_X = new Vector3d(-1, 0, 0),
			POS_Y = UP, NEG_Y = DOWN, POS_Z = new Vector3d(0, 0, +1), NEG_Z = new Vector3d(0, 0, -1), ZERO = new Vector3d();

	public double x, y, z;
	private static Random random = new Random();

	public Vector3d(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public Vector3d(double x, double y, double z, double length) {
		this.x = x;
		this.y = y;
		this.z = z;
		double len = length();
		this.x /= len;
		this.y /= len;
		this.z /= len;
		this.x *= length;
		this.y *= length;
		this.z *= length;
	}

	public Vector3d() {
		this(0, 0, 0);
	}

	public Vector3d(Vector3d vec) {
		this(vec.x, vec.y, vec.z);
	}

	public Vector3d(Vector3f vec) {
		this(vec.x, vec.y, vec.z);
	}

	public Vector3d(Vector2f vec) {
		this(vec.x, vec.y, 0);
	}

	public Vector3d(Vector4f vec) {
		this.x = vec.x;
		this.y = vec.y;
		this.z = vec.z;
	}

	public final double lengthSquared() {
		return x * x + y * y + z * z;
	}

	public final double length() {
		return (double) Math.sqrt(lengthSquared());
	}

	public Vector3d add(Vector3d other) {
		this.x += other.x;
		this.y += other.y;
		this.z += other.z;
		return this;
	}

	public static Vector3d add(Vector3d first, Vector3d second) {
		return new Vector3d(first.x + second.x, first.y + second.y, first.z + second.z);
	}

	public Vector3d rotate(Vector3d axis, double radins) {
		this.set(rotate(this, axis, radins));
		return this;
	}

	public static Vector3d rotate(Vector3d baseVector, Vector3d axis, double radins) {
		Vector3d result = new Vector3d(baseVector);
		Matrix4d matrix = new Matrix4d();
		Matrix4d.rotate(radins, axis.x, axis.y, axis.z, matrix, matrix);
		result.multiply(matrix);
		return result;
	}

	public Vector3d negate() {
		this.x = -x;
		this.y = -y;
		this.z = -z;
		return this;
	}

	public static Vector3d negate(Vector3d vec) {
		return new Vector3d(-vec.x, -vec.y, -vec.z);
	}

	public Vector3d zero() {
		this.x = 0f;
		this.y = 0f;
		this.z = 0f;
		return this;
	}

	public Vector3d normalise() {
		double l = length();
		if (l != 0) {
			this.x /= l;
			this.y /= l;
			this.z /= l;
		}
		return this;
	}

	public Vector3d normaliseWithMultiplier(double multiplier) {
		double l = length();
		if (l != 0) {
			this.x *= multiplier;
			this.y *= multiplier;
			this.z *= multiplier;
			this.x /= l;
			this.y /= l;
			this.z /= l;
		}
		return this;
	}

	public static Vector3d normalise(Vector3d passVec) {
		Vector3d vec = new Vector3d(passVec);
		double l = vec.length();
		vec.x = vec.x / l;
		vec.y = vec.y / l;
		vec.z = vec.z / l;
		return vec;
	}

	/**
	 * Returns weather or not all of the components of this vector are rational
	 * @return Weather or not all of the components of this vector are rational
	 */
	public boolean isRational() {
		return (Double.isFinite(x) && Double.isFinite(y) && Double.isFinite(z) && !(Double.isNaN(x) || Double.isNaN(y) || Double.isNaN(z)));
	}

	public static Vector3d absoluteValue(Vector3d first) {
		return new Vector3d(Math.abs(first.x), Math.abs(first.y), Math.abs(first.z));
	}

	public Vector3d absoluteValue() {
		this.x = Math.abs(this.x);
		this.y = Math.abs(this.y);
		this.z = Math.abs(this.z);
		return this;
	}

	public Vector3d subtract(Vector3d other) {
		this.x -= other.x;
		this.y -= other.y;
		this.z -= other.z;
		return this;
	}

	public static Vector3d subtract(Vector3d first, Vector3d second) {
		return new Vector3d(first.x - second.x, first.y - second.y, first.z - second.z);
	}

	public Vector3d scale(double scale) {
		this.x *= scale;
		this.y *= scale;
		this.z *= scale;
		return this;
	}

	public Vector3d divideScale(double scale) {
		this.x /= scale;
		this.y /= scale;
		this.z /= scale;
		return this;
	}

	public Vector2d toVec2() {
		return new Vector2d(x, y);
	}

	@Override
	public String toString() {
		return "Vector3d[" + x + ", " + y + ", " + z + "]";
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) { return true; }
		if (obj == null) { return false; }
		if (this.getClass() != obj.getClass()) { return false; }
		Vector3d other = (Vector3d) obj;
		return (this.x == other.x) && (this.y == other.y) && (this.z == other.z);
	}

	public static Vector3d lerp(Vector3d left, Vector3d right, double factor) {
		return new Vector3d(Maths.lerp(left.x, right.x, factor), Maths.lerp(left.y, right.y, factor), Maths.lerp(left.z, right.z, factor));
	}

	public static Vector3d randomVector(double scale) {
		Vector3d vec = new Vector3d(1 - (random.nextDouble() * 2), 1 - (random.nextDouble() * 2), 1 - (random.nextDouble() * 2))
				.scale(Maths.randRange(0, scale));
		return vec;
	}

	public Vector3d setLength(double length) {
		if (this.length() == 0.0f) return this;
		this.normalise();
		this.scale(length);
		return this;
	}

	public void set(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public void modulus(double x, double y, double z) {
		this.x %= x;
		this.y %= y;
		this.z %= z;
	}

	public Vector3d set(Vector3d vec) {
		this.x = vec.x;
		this.y = vec.y;
		this.z = vec.z;
		return this;
	}
	
	public Vector3d set(Vector3f vec) {
		this.x = vec.x;
		this.y = vec.y;
		this.z = vec.z;
		return this;
	}

	public Vector3d translate(double x, double y, double z) {
		this.x += x;
		this.y += y;
		this.z += z;
		return this;
	}

	public static Vector3d cross(Vector3d left, Vector3d right) {
		Vector3d dest = new Vector3d();
		dest.set(left.y * right.z - left.z * right.y, right.x * left.z - right.z * left.x, left.x * right.y - left.y * right.x);
		return dest;
	}

	public static double dot(Vector3d left, Vector3d right) {
		return left.x * right.x + left.y * right.y + left.z * right.z;
	}

	public static double angle(Vector3d a, Vector3d b) {
		double dls = dot(a, b) / (a.length() * b.length());
		if (dls < -1.0F) {
			dls = -1.0F;
		} else if (dls > 1.0F) {
			dls = 1.0F;
		}
		return (double) Math.acos(dls);
	}

	public static Vector3d fromEulerAngles(double pitch, double yaw, double roll) {
		pitch = -pitch;
		yaw = -yaw;
		Vector3d vec = new Vector3d();
		vec.x = +(Math.sin(yaw));
		vec.y = -(Math.sin(pitch) * Math.cos(yaw));
		vec.z = -(Math.cos(pitch) * Math.cos(yaw));
		vec.normalise();
		return vec;
	}

	public Vector3d load(DoubleBuffer buf) {
		this.x = buf.get();
		this.y = buf.get();
		this.z = buf.get();
		return this;
	}

	public Vector3d store(DoubleBuffer buf) {
		buf.put(this.x);
		buf.put(this.y);
		buf.put(this.z);

		return this;
	}

	/**
	 * Multiplies the vector by the given matrix.
	 * @param matrix The matrix
	 * @return This vector for chaining
	 */
	public Vector3d multiply(Matrix4f matrix) {
		this.set(x * matrix.m00 + y * matrix.m01 + z * matrix.m02 + matrix.m03, x * matrix.m10 + y * matrix.m11 + z * matrix.m12 + matrix.m13,
				x * matrix.m20 + y * matrix.m21 + z * matrix.m22 + matrix.m23);
		return this;
	}

	/**
	 * Multiplies the vector by the given matrix.
	 * @param matrix The matrix
	 * @return This vector for chaining
	 */
	public Vector3d multiply(Matrix4d matrix) {
		this.set(x * matrix.m00 + y * matrix.m01 + z * matrix.m02 + matrix.m03, x * matrix.m10 + y * matrix.m11 + z * matrix.m12 + matrix.m13,
				x * matrix.m20 + y * matrix.m21 + z * matrix.m22 + matrix.m23);
		return this;
	}

	public String clip(int decimalPoints) {
		decimalPoints += 1;
		String x = this.x + "", y = this.y + "", z = this.z + "";

		return "(" + x.substring(0, x.lastIndexOf(".") + decimalPoints) + ", " + y.substring(0, y.lastIndexOf(".") + decimalPoints) + ", "
				+ z.substring(0, z.lastIndexOf(".") + decimalPoints) + ")";
	}

	public static Vector3d scale(Vector3d vec, double value) {
		return new Vector3d(vec.x * value, vec.y * value, vec.z * value);
	}

	public static Vector3d randomVector(double xMin, double xMax, double yMin, double yMax, double zMin, double zMax) {
		return new Vector3d(Maths.randRange(xMin, xMax), Maths.randRange(yMin, yMax), Maths.randRange(zMin, zMax));
	}

	@Override
	public Vector3d interpolate(Vector3d a, Vector3d b, double f, InterpolationType type) {
		return new Vector3d((double) type.interpolate(a.x, b.x, f), (double) type.interpolate(a.y, b.y, f), (double) type.interpolate(a.z, b.z, f));
	}

	public double modulus() {
		return modulus(this);
	}

	private double modulus(Vector3d vec) {
		return Math.sqrt(Vector3d.dot(vec, vec));
	}

	public static Vector3d arbitraryOrthogonal(Vector3d vec) {
		if (vec.equals(Vector3d.ZERO)) return new Vector3d(0, 0, 0);
		Vector3d normalized = Vector3d.normalise(vec);
		Vector3d otherVector;
		if (Math.abs(Vector3d.dot(normalized, Vector3d.POS_X)) < 0.5f) otherVector = Vector3d.POS_X;
		else if (Math.abs(Vector3d.dot(normalized, Vector3d.NEG_X)) < 0.5f) otherVector = Vector3d.NEG_X;
		else otherVector = POS_Y;

		return Vector3d.cross(normalized, otherVector);
	}

	public static Vector3d addAndSetLength(Vector3d vec1, Vector3d vec2, double length) {
		double x = vec1.x + vec2.x;
		double y = vec1.y + vec2.y;
		double z = vec1.z + vec2.z;
		double len = StrictMath.sqrt(x * x + y * y + z * z);
		x /= len;
		y /= len;
		z /= len;
		x *= length;
		y *= length;
		z *= length;
		return new Vector3d(x, y, z);
	}

	public static Vector3d addLength(Vector3d vec, double value) {
		double len = vec.length();
		double x = vec.x / len;
		double y = vec.y / len;
		double z = vec.z / len;
		double newLength = len + value;
		x *= newLength;
		y *= newLength;
		z *= newLength;

		return new Vector3d(x, y, z);
	}

	public static Vector3d sumAndNormalize(Vector3d... vectors) {
		double x = vectors[0].x;
		double y = vectors[0].y;
		double z = vectors[0].z;
		for (int i = 1; i < vectors.length; i++) {
			x += vectors[i].x;
			y += vectors[i].y;
			z += vectors[i].z;
		}
		return new Vector3d(x, y, z, 1.0);
	}

	public static Vector3f crossFloat(Vector3d left, Vector3d right) {
		Vector3f dest = new Vector3f();
		dest.set(left.y * right.z - left.z * right.y, right.x * left.z - right.z * left.x, left.x * right.y - left.y * right.x);
		return dest;
	}

	public void addScaled(Vector3d vec, double addLength) {
		double length = vec.length();
		double x = vec.x;
		double y = vec.y;
		double z = vec.z;
		x /= length;
		y /= length;
		z /= length;

		x *= addLength;
		y *= addLength;
		z *= addLength;
		this.x += x;
		this.y += y;
		this.z += z;
	}

	public Vector3f toFloat() {
		return new Vector3f(x, y, z);
	}

	public static Vector3d setLength(Vector3d vec, double length) {
		return new Vector3d(vec.x, vec.y, vec.z, length);
	}
}
