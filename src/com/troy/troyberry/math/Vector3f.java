package com.troy.troyberry.math;

import java.nio.*;
import java.util.*;

public final class Vector3f {

	public static final int ELEMENTS = 3;
	public static final int BYTES = ELEMENTS * Float.BYTES, BITS = ELEMENTS * Float.SIZE;

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

	public Vector3f normalised() {
		float l = length();
		if (this.x != 0.0f) this.x /= l;
		if (this.y != 0.0f) this.y /= l;
		if (this.z != 0.0f) this.z /= l;
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
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (this.getClass() != obj.getClass()) {
			return false;
		}
		Vector3f other = (Vector3f) obj;
		if ((this.x == other.x) && (this.y == other.y) && (this.z == other.z)) {
			return true;
		}
		return false;
	}

	public static Vector3f randomVector(float scale) {
		Vector3f vec = new Vector3f(1 - (random.nextFloat() * 2), 1 - (random.nextFloat() * 2), 1 - (random.nextFloat() * 2)).scale(scale);
		return vec;
	}

	public Vector3f setLength(float length) {
		this.normalised();
		this.scale(length);
		return this;
	}

	public void set(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public void modulus(float x, float y, float z) {
		this.x %= x;
		this.y %= y;
		this.z %= z;
	}

	public void set(Vector3f vec) {
		this.x = vec.x;
		this.y = vec.y;
		this.z = vec.z;
	}

	public Vector3f translate(float x, float y, float z) {
		this.x += x;
		this.y += y;
		this.z += z;
		return this;
	}

	public static Vector3f add(Vector3f left, Vector3f right, Vector3f dest) {
		if (dest == null) {
			return new Vector3f(left.x + right.x, left.y + right.y, left.z + right.z);
		}
		dest.set(left.x + right.x, left.y + right.y, left.z + right.z);
		return dest;
	}

	public static Vector3f sub(Vector3f left, Vector3f right, Vector3f dest) {
		if (dest == null) {
			return new Vector3f(left.x - right.x, left.y - right.y, left.z - right.z);
		}
		dest.set(left.x - right.x, left.y - right.y, left.z - right.z);
		return dest;
	}

	public static Vector3f cross(Vector3f left, Vector3f right, Vector3f dest) {
		if (dest == null) {
			dest = new Vector3f();
		}
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

	public String clip(int decimalPoints) {
		decimalPoints += 1;
		String x = this.x + "", y = this.y + "", z = this.z + "";

		return "(" + x.substring(0, x.lastIndexOf(".") + decimalPoints) + ", " + y.substring(0, y.lastIndexOf(".") + decimalPoints) + ", "
			+ z.substring(0, z.lastIndexOf(".") + decimalPoints) + ")";
	}
}
