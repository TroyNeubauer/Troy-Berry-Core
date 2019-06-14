package com.troyberry.math;

import java.nio.*;
import java.util.*;

import com.troyberry.util.interpolation.*;

public class Vector3f implements Interpolatable<Vector3f> {

	public static final int ELEMENTS = 3;
	public static final int BYTES = ELEMENTS * Float.BYTES, BITS = ELEMENTS * Float.SIZE;
	public static final Vector3f UP = new Vector3f(0, +1, 0), DOWN = new Vector3f(0, -1, 0), POS_X = new Vector3f(+1, 0, 0), NEG_X = new Vector3f(-1, 0, 0),
			POS_Y = UP, NEG_Y = DOWN, POS_Z = new Vector3f(0, 0, +1), NEG_Z = new Vector3f(0, 0, -1), ZERO = new Vector3f();

	public float x, y, z;
	private static Random random = new Random();

	public Vector3f(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public Vector3f() {
		this(0, 0, 0);
	}

	public Vector3f(Vector3f vec) {
		this(vec.x, vec.y, vec.z);
	}

	public Vector3f(Vector2f vec) {
		this(vec.x, vec.y, 0);
	}

	public Vector3f(Vector4f vec) {
		this.x = vec.x;
		this.y = vec.y;
		this.z = vec.z;
	}

	public Vector3f(double x, double y, double z) {
		this((float) x, (float) y, (float) z);
	}

	public final float lengthSquared() {
		return x * x + y * y + z * z;
	}

	public final float length() {
		return (float) Math.sqrt(lengthSquared());
	}

	public Vector3f add(Vector3f other) {
		this.x += other.x;
		this.y += other.y;
		this.z += other.z;
		return this;
	}

	public static Vector3f add(Vector3f first, Vector3f second) {
		return new Vector3f(first.x + second.x, first.y + second.y, first.z + second.z);
	}

	public Vector3f rotate(Vector3f axis, float radins) {
		this.set(rotate(this, axis, radins));
		return this;
	}

	public static Vector3f rotate(Vector3f baseVector, Vector3f axis, float radins) {
		Vector3f result = new Vector3f(baseVector);
		Matrix4f matrix = new Matrix4f();
		matrix.rotate(radins, axis);
		result.multiply(matrix);
		return result;
	}

	public Vector3f negate() {
		this.x = -x;
		this.y = -y;
		this.z = -z;
		return this;
	}

	public static Vector3f negate(Vector3f vec) {
		return new Vector3f(-vec.x, -vec.y, -vec.z);
	}

	public Vector3f zero() {
		this.x = 0f;
		this.y = 0f;
		this.z = 0f;
		return this;
	}

	public Vector3f normalise() {
		float l = length();
		if (l != 0) {
			this.x /= l;
			this.y /= l;
			this.z /= l;
		}
		return this;
	}

	public Vector3f normaliseWithMultiplier(float multiplier) {
		float l = length();
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

	public static Vector3f normalise(Vector3f passVec) {
		Vector3f vec = new Vector3f(passVec);
		float l = vec.length();
		vec.x = vec.x / l;
		vec.y = vec.y / l;
		vec.z = vec.z / l;
		return vec;
	}

	public static Vector3f absoluteValue(Vector3f first) {
		return new Vector3f(Math.abs(first.x), Math.abs(first.y), Math.abs(first.z));
	}

	public Vector3f absoluteValue() {
		this.x = Math.abs(this.x);
		this.y = Math.abs(this.y);
		this.z = Math.abs(this.z);
		return this;
	}

	public Vector3f subtract(Vector3f other) {
		this.x -= other.x;
		this.y -= other.y;
		this.z -= other.z;
		return this;
	}

	public static Vector3f subtract(Vector3f first, Vector3f second) {
		return new Vector3f(first.x - second.x, first.y - second.y, first.z - second.z);
	}

	public Vector3f scale(float scale) {
		this.x *= scale;
		this.y *= scale;
		this.z *= scale;
		return this;
	}

	public Vector3f divideScale(float scale) {
		this.x /= scale;
		this.y /= scale;
		this.z /= scale;
		return this;
	}

	public Vector2f toVec2() {
		return new Vector2f(x, y);
	}

	@Override
	public String toString() {
		return "Vector3f[" + x + ", " + y + ", " + z + "]";
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) { return true; }
		if (obj == null) { return false; }
		if (this.getClass() != obj.getClass()) { return false; }
		Vector3f other = (Vector3f) obj;
		return (this.x == other.x) && (this.y == other.y) && (this.z == other.z);
	}

	public static Vector3f lerp(Vector3f left, Vector3f right, float factor) {
		return new Vector3f(Maths.lerp(left.x, right.x, factor), Maths.lerp(left.y, right.y, factor), Maths.lerp(left.z, right.z, factor));
	}

	public static Vector3f randomVector(float scale) {
		Vector3f vec = new Vector3f(1 - (random.nextFloat() * 2), 1 - (random.nextFloat() * 2), 1 - (random.nextFloat() * 2)).scale(Maths.randRange(0, scale));
		return vec;
	}

	public Vector3f setLength(float length) {
		if (this.length() == 0.0f) return this;
		this.normalise();
		this.scale(length);
		return this;
	}

	public void set(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public void set(double x, double y, double z) {
		this.x = (float) x;
		this.y = (float) y;
		this.z = (float) z;
	}

	public void modulus(float x, float y, float z) {
		this.x %= x;
		this.y %= y;
		this.z %= z;
	}

	public Vector3f set(Vector3f vec) {
		this.x = vec.x;
		this.y = vec.y;
		this.z = vec.z;
		return this;
	}

	public Vector3f translate(float x, float y, float z) {
		this.x += x;
		this.y += y;
		this.z += z;
		return this;
	}

	public static Vector3f cross(Vector3f left, Vector3f right) {
		Vector3f dest = new Vector3f();
		dest.set(left.y * right.z - left.z * right.y, right.x * left.z - right.z * left.x, left.x * right.y - left.y * right.x);
		return dest;
	}

	public static float dot(Vector3f left, Vector3f right) {
		return left.x * right.x + left.y * right.y + left.z * right.z;
	}

	public static float angle(Vector3f a, Vector3f b) {
		float dls = dot(a, b) / (a.length() * b.length());
		if (dls < -1.0F) {
			dls = -1.0F;
		} else if (dls > 1.0F) {
			dls = 1.0F;
		}
		return (float) Math.acos(dls);
	}

	public static Vector3f fromEulerAngles(float pitch, float yaw, float roll) {
		pitch = -pitch;
		yaw = -yaw;
		Vector3f vec = new Vector3f();
		vec.x = +(Maths.sinFloat(yaw));
		vec.y = -(Maths.sinFloat(pitch) * Maths.cosFloat(yaw));
		vec.z = -(Maths.cosFloat(pitch) * Maths.cosFloat(yaw));
		vec.normalise();
		return vec;
	}

	public Vector3f load(FloatBuffer buf) {
		this.x = buf.get();
		this.y = buf.get();
		this.z = buf.get();
		return this;
	}

	public Vector3f store(FloatBuffer buf) {
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
	public Vector3f multiply(Matrix4f matrix) {
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

	public static Vector3f scale(Vector3f vec, float value) {
		return new Vector3f(vec.x * value, vec.y * value, vec.z * value);
	}

	public static Vector3f randomVector(float xMin, float xMax, float yMin, float yMax, float zMin, float zMax) {
		return new Vector3f(Maths.randRange(xMin, xMax), Maths.randRange(yMin, yMax), Maths.randRange(zMin, zMax));
	}

	@Override
	public Vector3f interpolate(Vector3f a, Vector3f b, double f, InterpolationType type) {
		return new Vector3f((float) type.interpolate(a.x, b.x, f), (float) type.interpolate(a.y, b.y, f), (float) type.interpolate(a.z, b.z, f));
	}

	public float modulus() {
		return modulus(this);
	}

	private float modulus(Vector3f vec) {
		return Maths.sqrt(Vector3f.dot(vec, vec));
	}

	public static Vector3f arbitraryOrthogonal(Vector3f vec) {
		if (vec.equals(Vector3f.ZERO)) return new Vector3f(0, 0, 0);
		Vector3f normalized = Vector3f.normalise(vec);
		Vector3f otherVector;

		if (Math.abs(Vector3f.dot(normalized, Vector3f.POS_X)) < 0.5f) otherVector = Vector3f.POS_X;
		else if (Math.abs(Vector3f.dot(normalized, Vector3f.NEG_X)) < 0.5f) otherVector = Vector3f.NEG_X;
		else otherVector = POS_Y;

		return Vector3f.cross(normalized, otherVector);
	}

	public static Vector3f addAndSetLength(Vector3f vec1, Vector3f vec2, float length) {
		float x = vec1.x + vec2.x;
		float y = vec1.y + vec2.y;
		float z = vec1.z + vec2.z;
		float len = (float) StrictMath.sqrt(x * x + y * y + z * z);
		x /= len;
		y /= len;
		z /= len;
		x *= length;
		y *= length;
		z *= length;
		return new Vector3f(x, y, z);
	}

	public void addScaled(Vector3f vec, float addLength) {
		float length = vec.length();
		float x = vec.x;
		float y = vec.y;
		float z = vec.z;
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

	public static Vector3f addLength(Vector3f vec, float value) {
		float len = vec.length();
		float x = vec.x / len;
		float y = vec.y / len;
		float z = vec.z / len;
		float newLength = len + value;
		x *= newLength;
		y *= newLength;
		z *= newLength;
		
		return new Vector3f(x, y, z);
	}

	public Vector3d toDouble() {
		return new Vector3d(x, y, z);
	}
}
