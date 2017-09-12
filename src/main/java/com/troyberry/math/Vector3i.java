package com.troyberry.math;

import com.troyberry.util.interpolation.*;

public class Vector3i implements Interpolatable<Vector3i> {

	public int x, y, z;

	public Vector3i(int x, int y, int z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	@Override
	public Vector3i interpolate(Vector3i a, Vector3i b, double f, InterpolationType type) {
		return new Vector3i((int)type.interpolate(a.x, b.x, f), (int)type.interpolate(a.y, b.y, f), (int)type.interpolate(a.z, b.z, f));
	}

	@Override
	public String toString() {
		return "Vector3i [x=" + x + ", y=" + y + ", z=" + z + "]";
	}
	
	

}
