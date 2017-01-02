package com.troyberry.math;

import java.nio.*;
import java.util.*;

public class Vector3d {
	public static final int ELEMENTS = 3;
	public static final int BYTES = ELEMENTS * Float.BYTES, BITS = ELEMENTS * Float.SIZE; 

	public double x, y, z;
	private static Random random = new Random();
	public Vector3d(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public Vector3d() {
		this(0, 0, 0);
	}

	public Vector3d(Vector3d vec) {
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

	public Vector3d(Vector3f vec) {
		this((double)vec.x, (double)vec.y, (double)vec.z);
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

	public Vector3d normalised() {
		double l = length();
		if (this.x != 0.0f) this.x /= l;
		if (this.y != 0.0f) this.y /= l;
		if (this.z != 0.0f) this.z /= l;
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
	public boolean isRational(){
		return(Double.isFinite(x) && Double.isFinite(y) && Double.isFinite(z) && !(Double.isNaN(x) || Double.isNaN(y) || Double.isNaN(z)));
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
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (this.getClass() != obj.getClass()) {
			return false;
		}
		Vector3d other = (Vector3d) obj;
		if ((this.x == other.x) && (this.y == other.y) && (this.z == other.z)) {
			return true;
		}
		return false;
	}
	
	public static Vector3d lerp(Vector3d left, Vector3d right, double factor) {
		factor = Maths.clamp(0.0f, 1.0f, factor);
		return new Vector3d(Maths.lerp(left.x, right.x, factor), Maths.lerp(left.y, right.y, factor), Maths.lerp(left.z, right.z, factor));
	}

	public static Vector3d randomVector(double scale) {
		Vector3d vec = new Vector3d(1 - (random.nextFloat() * 2), 1 - (random.nextFloat() * 2), 1 - (random.nextFloat() * 2)).scale(scale);
		return vec;
	}

	public Vector3d setLength(double length) {
		this.normalised();
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
    public Vector3d mul(Matrix4f matrix)
    {
       this.set(x*matrix.m00+y*matrix.m01+z*matrix.m02+matrix.m03,
            x*matrix.m10+y*matrix.m11+z*matrix.m12+matrix.m13,
            x*matrix.m20+y*matrix.m21+z*matrix.m22+matrix.m23);
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

	public Vector3f toFloat() {
		return new Vector3f(Maths.toFloat(x), Maths.toFloat(y), Maths.toFloat(z));
	}
}
