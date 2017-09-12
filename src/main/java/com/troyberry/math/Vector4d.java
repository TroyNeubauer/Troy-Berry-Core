package com.troyberry.math;

import java.nio.*;

public class Vector4d {
	public static final int ELEMENTS = 4;
	public static final int BYTES = ELEMENTS * Double.BYTES, BITS = ELEMENTS * Double.SIZE;

	public double x, y, z, w;

	public Vector4d() {
		set(0, 0, 0, 0);
	}

	public Vector4d(double x, double y, double z, double w) {
		set(x, y, z, w);
	}
	
	public Vector4d(Vector3f vec, double w) {
		set(vec.x, vec.y, vec.z, w);
	}
	
	public Vector4d(Vector3d vec, double w) {
		set(vec.x, vec.y, vec.z, w);
	}

	public void set(double x, double y) {
		this.x = x;
		this.y = y;
	}

	public void set(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public void set(double x, double y, double z, double w) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.w = w;
	}

	public double lengthSquared() {
		return this.x * this.x + this.y * this.y + this.z * this.z + this.w * this.w;
	}

	public Vector4d translate(double x, double y, double z, double w) {
		this.x += x;
		this.y += y;
		this.z += z;
		this.w += w;
		return this;
	}
	
    /**
     * Multiplies the vector by the given matrix.
     * @param matrix The matrix
     * @return This vector for chaining
     */
	
    public Vector4d mul(Matrix4f matrix) {
       this.set(x*matrix.m00+y*matrix.m01+z*matrix.m02+matrix.m03,
            x*matrix.m10+y*matrix.m11+z*matrix.m12+matrix.m13,
            x*matrix.m20+y*matrix.m21+z*matrix.m22+matrix.m23,
            x*matrix.m30+y*matrix.m31+z*matrix.m32+matrix.m33);
       return this;
    }

	public static Vector4d add(Vector4d left, Vector4d right, Vector4d dest) {
		if (dest == null) {
			return new Vector4d(left.x + right.x, left.y + right.y, left.z + right.z, left.w + right.w);
		}
		dest.set(left.x + right.x, left.y + right.y, left.z + right.z, left.w + right.w);
		return dest;
	}

	public static Vector4d sub(Vector4d left, Vector4d right, Vector4d dest) {
		if (dest == null) {
			return new Vector4d(left.x - right.x, left.y - right.y, left.z - right.z, left.w - right.w);
		}
		dest.set(left.x - right.x, left.y - right.y, left.z - right.z, left.w - right.w);
		return dest;
	}

	public Vector4d negate() {
		this.x = (-this.x);
		this.y = (-this.y);
		this.z = (-this.z);
		this.w = (-this.w);
		return this;
	}

	public Vector4d negate(Vector4d dest) {
		if (dest == null) {
			dest = new Vector4d();
		}
		dest.x = (-this.x);
		dest.y = (-this.y);
		dest.z = (-this.z);
		dest.w = (-this.w);
		return dest;
	}

	public double length() {
		return (double) Math.sqrt(lengthSquared());
	}

	public Vector4d normalise(Vector4d dest) {
		double l = length();
		if (dest == null) {
			dest = new Vector4d(this.x / l, this.y / l, this.z / l, this.w / l);
		} else {
			dest.set(this.x / l, this.y / l, this.z / l, this.w / l);
		}
		return dest;
	}

	public static double dot(Vector4d left, Vector4d right) {
		return left.x * right.x + left.y * right.y + left.z * right.z + left.w * right.w;
	}

	public static double angle(Vector4d a, Vector4d b) {
		double dls = dot(a, b) / (a.length() * b.length());
		if (dls < -1.0F) {
			dls = -1.0F;
		} else if (dls > 1.0F) {
			dls = 1.0F;
		}
		return (double) Math.acos(dls);
	}

	public Vector4d load(FloatBuffer buf) {
		this.x = buf.get();
		this.y = buf.get();
		this.z = buf.get();
		this.w = buf.get();
		return this;
	}

	public Vector4d scale(double scale) {
		this.x *= scale;
		this.y *= scale;
		this.z *= scale;
		this.w *= scale;
		return this;
	}

	public Vector4d store(DoubleBuffer buf) {
		buf.put(this.x);
		buf.put(this.y);
		buf.put(this.z);
		buf.put(this.w);

		return this;
	}

	public String toString() {
		return "Vector4d: " + this.x + " " + this.y + " " + this.z + " " + this.w;
	}

	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		Vector4d other = (Vector4d) obj;
		if ((this.x == other.x) && (this.y == other.y) && (this.z == other.z) && (this.w == other.w)) {
			return true;
		}
		return false;
	}
	
}
