package com.troy.troyberry.math;

import java.util.*;

public class Vector2d {

	public double x, y;

	public Vector2d() {
		this.x = 0.0;
		this.y = 0.0;
	}

	private static Random random = new Random();

	public Vector2d(double x, double y) {
		this.x = x;
		this.y = y;
	}

	public Vector2d(Vector2d vec) {
		this.x = vec.x;
		this.y = vec.y;
	}

	public Vector2d(Vector2f vec) {
		this.x = (double) vec.x;
		this.y = (double) vec.y;
	}

	public static double getAngleFromPoint(Vector2d firstPoint, Vector2d secondPoint) {

		if ((secondPoint.x > firstPoint.x)) {//above 0 to 180 degrees

			return (Math.atan2((secondPoint.x - firstPoint.x), (firstPoint.y - secondPoint.y)) * 180 / Math.PI);

		} else if ((secondPoint.x < firstPoint.x)) {//above 180 degrees to 360/0

			return 360 - (Math.atan2((firstPoint.x - secondPoint.x), (firstPoint.y - secondPoint.y)) * 180 / Math.PI);

		} //End if((secondPoint.x > firstPoint.x) && (secondPoint.y <= firstPoint.y))

		return Math.atan2(0, 0);

	}

	public double lengthSquared() {
		return this.x * this.x + this.y * this.y;
	}

	public double length() {
		return Math.sqrt(lengthSquared());
	}

	public Vector2d translate(Vector2d vec) {
		this.x += vec.x;
		this.y += vec.y;
		return this;
	}

	public Vector2d negate() {
		this.x = (-this.x);
		this.y = (-this.y);
		return this;
	}

	public static Vector2d negate(Vector2d dest) {
		return new Vector2d(-dest.x, -dest.y);
	}

	public Vector2d normalised() {
		double l = length();
		this.x = this.x / l;
		this.y = this.y / l;
		return this;
	}

	public static Vector2d normalise(Vector2d vec) {
		double l = vec.length();
		return new Vector2d(vec.x / l, vec.y / l);
	}

	public static double dot(Vector2d left, Vector2d right) {
		return left.x * right.x + left.y * right.y;
	}

	public double dot(Vector2d right) {
		return this.x * right.x + this.y * right.y;
	}

	public static Vector2d add(Vector2d left, Vector2d right) {
		return new Vector2d(left.x + right.x, left.y + right.y);
	}

	public Vector2d add(Vector2d velocity) {
		this.x += velocity.x;
		this.y += velocity.y;
		return this;
	}

	public static Vector2d subtract(Vector2d left, Vector2d right) {
		return new Vector2d(left.x - right.x, left.y - right.y);
	}

	public Vector2d subtract(Vector2d vec) {
		Vector2d dest = new Vector2d(this.x - vec.x, this.y - vec.y);
		return dest;
	}

	public static Vector2d lerp(Vector2d left, Vector2d right, double factor) {
		factor = Maths.clamp(0.0, 1.0, factor);
		return new Vector2d(Maths.lerp(left.x, right.x, factor), Maths.lerp(left.y, right.y, factor));
	}

	public Vector2d invertX() {
		this.x = (-this.x);
		return this;
	}

	public Vector2d invertY() {
		this.y = (-this.y);
		return this;
	}

	public Vector2d scale(double d) {
		this.x *= d;
		this.y *= d;

		return this;
	}

	public Vector2d divideScale(double scale) {
		this.x /= scale;
		this.y /= scale;
		return this;
	}

	public Vector2d rotate(double angleToRotate) {
		Vector2d vec = new Vector2d(this);
		double m = vec.length();

		double angle = Math.toDegrees(Math.acos(vec.x / m));

		if (vec.y < 0.0) {
			angle = 180 - angle + 180;
		}
		angle += angleToRotate;
		angle += 360;
		angle %= 360;

		vec.x = m * Math.cos(Math.toRadians(angle));
		vec.y = m * Math.sin(Math.toRadians(angle));
		return vec;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder(64);

		sb.append("Vector2f[");
		sb.append(this.x);
		sb.append(", ");
		sb.append(this.y);
		sb.append(']');
		return sb.toString();
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
		Vector2d other = (Vector2d) obj;
		if ((this.x == other.x) && (this.y == other.y)) {
			return true;
		}
		return false;
	}

	public static Vector2d randomVector(double d) {
		return new Vector2d(((1 - (random.nextDouble() * 2)) * d), ((1 - (random.nextDouble() * 2)) * d));
	}

	public Vector2d setSize(double size) {
		double l = length();
		this.x = this.x / l;
		this.y = this.y / l;
		this.x *= size;
		this.y *= size;
		return this;
	}

}
