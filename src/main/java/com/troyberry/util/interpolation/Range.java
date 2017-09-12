package com.troyberry.util.interpolation;

public class Range implements Interpolatable<Range> {
	
	private final double min, max;

	public Range(double min, double max) {
		this.min = min;
		this.max = max;
	}

	public double getMin() {
		return min;
	}

	public double getMax() {
		return max;
	}

	@Override
	public Range interpolate(Range a, Range b, double f, InterpolationType type) {
		return new Range(type.interpolate(a.min, b.min, f), type.interpolate(a.max, b.max, f));
	}
	
	
	

}
