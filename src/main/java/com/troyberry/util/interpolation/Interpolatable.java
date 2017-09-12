package com.troyberry.util.interpolation;

public interface Interpolatable<T> {
	public T interpolate(T a, T b, double f, InterpolationType type);
}
