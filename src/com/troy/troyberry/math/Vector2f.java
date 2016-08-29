package com.troy.troyberry.math;

import java.util.*;

public final class Vector2f {
	
	public float x, y;
	public Vector2f() {
	}
	
	private static Random random = new Random();


	public Vector2f(float x, float y) {
		this.x = x;
		this.y = y;
	}

	public Vector2f(Vector2f position) {
		this.x = position.x;
		this.y = position.y;
	}
	
	public static double getAngleFromPoint(Vector2f firstPoint, Vector2f secondPoint) {

	    if((secondPoint.x > firstPoint.x)) {//above 0 to 180 degrees

	        return (Math.atan2((secondPoint.x - firstPoint.x), (firstPoint.y - secondPoint.y)) * 180 / Math.PI);

	    }
	    else if((secondPoint.x < firstPoint.x)) {//above 180 degrees to 360/0

	        return 360 - (Math.atan2((firstPoint.x - secondPoint.x), (firstPoint.y - secondPoint.y)) * 180 / Math.PI);

	    }//End if((secondPoint.x > firstPoint.x) && (secondPoint.y <= firstPoint.y))

	    return Math.atan2(0 ,0);

	}

	public float lengthSquared() {
		return this.x * this.x + this.y * this.y;
	}
	
	public final float length() {
		return (float) Math.sqrt(lengthSquared());
	}
	
	public Vector2f translate(Vector2f vec) {
		this.x += vec.x;
		this.y += vec.y;
		return this;
	}

	public Vector2f negate() {
		this.x = (-this.x);
		this.y = (-this.y);
		return this;
	}

	public static Vector2f negate(Vector2f dest) {
		return new Vector2f(-dest.x, -dest.y);
	}

	public Vector2f normalised() {
		float l = length();
		this.x =  this.x / l;
		this.y = this.y / l;
		return this;
	}
	
	public static Vector2f normalise(Vector2f vec) {
		float l = vec.length();
		return new Vector2f(vec.x / l, vec.y / l);
	}

	public static float dot(final Vector2f left, final Vector2f right) {
		return left.x * right.x + left.y * right.y;
	}
	
	public float dot(final Vector2f right) {
		return this.x * right.x + this.y * right.y;
	}

	public static Vector2f add(final Vector2f left, final Vector2f right) {
		return new Vector2f(left.x + right.x, left.y + right.y);
	}
	
	public Vector2f add(final Vector2f vec) {
		this.x += vec.x;
		this.y += vec.y;
		return this;
	}

	public static Vector2f subtract(final Vector2f left, final Vector2f right) {
		return new Vector2f(left.x - right.x, left.y - right.y);
	}
	
	public Vector2f subtract(final Vector2f vec) {
		Vector2f dest = new Vector2f(this.x - vec.x, this.y - vec.y);
		return dest;
	}
	
	public Vector2f invertX(){
		this.x = (-this.x);
		return this;
	}
	
	public Vector2f invertY(){
		this.y = (-this.y);
		return this;
	}


	public Vector2f scale(final float scale) {
		this.x *= scale;
		this.y *= scale;

		return this;
	}
	
	public Vector2f divideScale(final float scale) {
		this.x /= scale;
		this.y /= scale;
		return this;
	}
	
	public Vector2f rotate(float angleToRotate){
		Vector2f vec = new Vector2f(this);
		float m = vec.length();
		
		float angle = (float) Math.toDegrees(Math.acos(vec.x / m));
		
		if(Maths.isNegative(vec.y)){
			angle = 180 - angle + 180;
		}
		angle += angleToRotate;
		angle += 360;
		angle %= 360;
		
		vec.x = ((float) (m * Math.cos(Math.toRadians(angle))));
		vec.y = ((float) (m * Math.sin(Math.toRadians(angle))));
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
		Vector2f other = (Vector2f) obj;
		if ((this.x == other.x) && (this.y == other.y)) {
			return true;
		}
		return false;
	}

	public static Vector2f randomVector(float scale) {
		return new Vector2f(((1 - (random.nextFloat() * 2)) * scale), ((1 - (random.nextFloat() * 2)) * scale));
	}

}
