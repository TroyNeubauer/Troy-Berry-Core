package com.troy.troyberry.math;

public class Vector2i {

	public int x, y;

	public Vector2i(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public static Vector2i add(Vector2i vec1, Vector2i vec2) {
		return new Vector2i(vec1.x + vec2.x, vec1.y + vec2.y);
	}

	public Vector2i add(Vector2i vec) {
		this.x += vec.x;
		this.y += vec.y;
		return this;
	}
	
	public static Vector2i subtract(Vector2i vec1, Vector2i vec2) {
		return new Vector2i(vec1.x - vec2.x, vec1.y - vec2.y);
	}

	public Vector2i subtract(Vector2i vec) {
		this.x -= vec.x;
		this.y -= vec.y;
		return this;
	}
	
	public static Vector2i mutiply(Vector2i vec1, Vector2i vec2) {
		return new Vector2i(vec1.x * vec2.x, vec1.y * vec2.y);
	}

	public Vector2i mutiply(Vector2i vec) {
		this.x *= vec.x;
		this.y *= vec.y;
		return this;
	}
	
	public static Vector2i divide(Vector2i vec1, Vector2i vec2) {
		return new Vector2i(vec1.x / vec2.x, vec1.y / vec2.y);
	}

	public Vector2i divide(Vector2i vec) {
		this.x /= vec.x;
		this.y /= vec.y;
		return this;
	}
	
	public Vector2i add(int a){
		this.x += a;
		this.y += a;
		return this;
	}
	
	public Vector2i subtract(int a){
		this.x -= a;
		this.y -= a;
		return this;
	}
	
	public Vector2i mutiply(int a){
		this.x *= a;
		this.y *= a;
		return this;
	}
	
	public Vector2i divide(int a){
		this.x -= a;
		this.y -= a;
		return this;
	}

	public boolean equals(Object object){
		if(!(object instanceof Vector2i)) return false;
		Vector2i vec = (Vector2i) object;
		if(vec.x == this.x && vec.y == this.y){
			return true;
		}
		return false;
		
	}

	public String toString() {
		return "[" + x + ", " + y + "]\n";
	}

}
