package com.troyberry.util.interpolation;

import java.util.*;

import com.troyberry.util.serialization.AbstractTroyBuffer;

public abstract class KeyFrameMaster<T> {

	protected TreeMap<Double, T> pairs;
	protected InterpolationType type;
	

	protected KeyFrameMaster(TreeMap<Double, T> pairs, InterpolationType type) {
		this.pairs = pairs;
		this.type = type;
	}

	public KeyFrameMaster(InterpolationType type) {
		this.type = type;
		pairs = new TreeMap<Double, T>(comperator);
	}
	
	public KeyFrameMaster() {
		this(InterpolationType.LINEAR);
	}

	public void addFrame(double position, T value) {
		pairs.put(Double.valueOf(position), value);
	}

	public abstract T getValue(double position);
	
	public abstract void write(AbstractTroyBuffer writer);
	
	public void setInterpolationTypeType(InterpolationType type) {
		this.type = type;
	}
	
	public InterpolationType getInterpolationType() {
		return type;
	}
	
	protected static final Comparator<? super Double> comperator = new Comparator<Double>() {
		@Override
		public int compare(Double o1, Double o2) {
			return o1.compareTo(o2);
		}
	};
}
