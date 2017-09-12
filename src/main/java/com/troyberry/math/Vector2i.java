package com.troyberry.math;

import java.nio.*;
import java.util.*;

import com.troyberry.util.interpolation.*;

public class Vector2i implements Interpolatable<Vector2i> {

	public static final int ELEMENTS = 2;
	public static final int BYTES = ELEMENTS * Integer.BYTES, BITS = ELEMENTS * Integer.SIZE;

	private static Random random = new Random();
	public int x, y;

	public Vector2i(Vector3f vec) {
		this.x = (int) vec.x;
		this.y = (int) vec.y;
	}

	public Vector2i(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public Vector2i(Vector2i position) {
		this.x = position.x;
		this.y = position.y;
	}

	public Vector2i() {
		this(0f, 0f);
	}

	public Vector2i(float x, float y) {
		this((int) x, (int) y);
	}

	public Vector2i set(Vector2i vec) {
		this.x = vec.x;
		this.y = vec.y;
		return this;
	}

	public static Vector2i lerp(Vector2i left, Vector2i right, int factor) {
		factor = Maths.clamp(0, 1, factor);
		return new Vector2i(Maths.lerp(left.x, right.x, factor), Maths.lerp(left.y, right.y, factor));
	}

	public static double getAngleFromPoint(Vector2i firstPoint, Vector2i secondPoint) {

		if ((secondPoint.x > firstPoint.x)) {//above 0 to 180 degrees

			return (Math.atan2((secondPoint.x - firstPoint.x), (firstPoint.y - secondPoint.y)) * 180 / Math.PI);

		} else if ((secondPoint.x < firstPoint.x)) {//above 180 degrees to 360/0

			return 360 - (Math.atan2((firstPoint.x - secondPoint.x), (firstPoint.y - secondPoint.y)) * 180 / Math.PI);

		} //End if((secondPoint.x > firstPoint.x) && (secondPoint.y <= firstPoint.y))

		return Math.atan2(0, 0);

	}

	public int lengthSquared() {
		return this.x * this.x + this.y * this.y;
	}

	public final int length() {
		return (int) Math.sqrt(lengthSquared());
	}

	public Vector2i translate(Vector2i vec) {
		this.x += vec.x;
		this.y += vec.y;
		return this;
	}

	public Vector2i negate() {
		this.x = (-this.x);
		this.y = (-this.y);
		return this;
	}

	public static Vector2i negate(Vector2i dest) {
		return new Vector2i(-dest.x, -dest.y);
	}

	public Vector2i normalised() {
		int l = length();
		if (l == 0.0) return this;
		this.x = this.x / l;
		this.y = this.y / l;
		return this;
	}

	public static Vector2i normalise(Vector2i vec) {
		int l = vec.length();
		return new Vector2i(vec.x / l, vec.y / l);
	}

	public static int dot(final Vector2i left, final Vector2i right) {
		return left.x * right.x + left.y * right.y;
	}

	public int dot(final Vector2i right) {
		return this.x * right.x + this.y * right.y;
	}

	public static Vector2i add(final Vector2i left, final Vector2i right) {
		return new Vector2i(left.x + right.x, left.y + right.y);
	}

	public Vector2i add(final Vector2i vec) {
		this.x += vec.x;
		this.y += vec.y;
		return this;
	}

	public static Vector2i subtract(final Vector2i left, final Vector2i right) {
		return new Vector2i(left.x - right.x, left.y - right.y);
	}

	public Vector2i subtract(final Vector2i vec) {
		Vector2i dest = new Vector2i(this.x - vec.x, this.y - vec.y);
		return dest;
	}

	public Vector2i invertX() {
		this.x = (-this.x);
		return this;
	}

	public Vector2i invertY() {
		this.y = (-this.y);
		return this;
	}

	public Vector2i scale(final int scale) {
		this.x *= scale;
		this.y *= scale;

		return this;
	}

	public Vector2i divideScale(final int scale) {
		this.x /= scale;
		this.y /= scale;
		return this;
	}

	public Vector2i rotate(int angleToRotate) {
		Vector2i vec = new Vector2i(this);
		int m = vec.length();

		int angle = (int) Math.toDegrees(Math.acos(vec.x / m));

		if (Maths.isNegative(vec.y)) {
			angle = 180 - angle + 180;
		}
		angle += angleToRotate;
		angle += 360;
		angle %= 360;

		vec.x = ((int) (m * Math.cos(Math.toRadians(angle))));
		vec.y = ((int) (m * Math.sin(Math.toRadians(angle))));
		return vec;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder(64);

		sb.append("Vector2i[");
		sb.append(this.x);
		sb.append(", ");
		sb.append(this.y);
		sb.append(']');
		return sb.toString();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) { return true; }
		if (obj == null) { return false; }
		if (this.getClass() != obj.getClass()) { return false; }
		Vector2i other = (Vector2i) obj;
		if ((this.x == other.x) && (this.y == other.y)) { return true; }
		return false;
	}

	public static Vector2i randomVector(int scale) {
		return new Vector2i(((1 - (random.nextInt() * 2)) * scale), ((1 - (random.nextInt() * 2)) * scale));
	}

	public Vector2i setLength(int length) {
		this.normalised();
		this.scale(length);
		return this;
	}

	public Vector2i zero() {
		this.x = 0;
		this.y = 0;
		return this;
	}

	public Vector2i set(int x, int y) {
		this.x = x;
		this.y = y;
		return this;
	}

	public Vector2i translate(int x, int y) {
		this.x += x;
		this.y += y;
		return this;
	}

	public static float angle(Vector2i a, Vector2i b) {
		double dls = dot(a, b) / (a.length() * b.length());
		if (dls < -1.0) {
			dls = -1.0;
		} else if (dls > 1.0F) {
			dls = 1.0;
		}
		return (float) Math.acos(dls);
	}

	public static Vector2i add(Vector2i left, Vector2i right, Vector2i dest) {
		if (dest == null) { return new Vector2i(left.x + right.x, left.y + right.y); }
		dest.set(left.x + right.x, left.y + right.y);
		return dest;
	}

	public static Vector2i sub(Vector2i left, Vector2i right, Vector2i dest) {
		if (dest == null) { return new Vector2i(left.x - right.x, left.y - right.y); }
		dest.set(left.x - right.x, left.y - right.y);
		return dest;
	}

	public Vector2i store(IntBuffer buf) {
		buf.put(this.x);
		buf.put(this.y);
		return this;
	}

	public Vector2i load(IntBuffer buf) {
		this.x = buf.get();
		this.y = buf.get();
		return this;
	}

	public static Vector2i scale(Vector2i size, int amount) {
		Vector2i vec = new Vector2i(size);
		vec.x *= amount;
		vec.y *= amount;
		return vec;
	}

	@Override
	public Vector2i interpolate(Vector2i a, Vector2i b, double f, InterpolationType type) {
		return new Vector2i((int)type.interpolate(a.x, b.x, f), (int)type.interpolate(a.y, b.y, f));
	}

}
