package com.troyberry.util.interpolation;

import com.troyberry.math.*;

public enum InterpolationType {
	LINEAR((a, b, f) -> Maths.lerp(a, b, f), 0), COSINE((a, b, f) -> Maths.cerp(a, b, f), LINEAR.id + 1);

	public double interpolate(double a, double b, double f) {
		return this.action.doAction(a, b, f);
	}

	private final DoAction action;
	private final int id;

	InterpolationType(DoAction action, int id) {
		this.action = action;
		this.id = id;
	}
	
	public static InterpolationType getType(int id) {
		for(InterpolationType type : values()) {
			if(type.id == id)  return type;
		}
		return LINEAR;
		
	}

	static interface DoAction {
		public double doAction(double a, double b, double f);
	}

	public int getId() {
		return id;
	}
	
}
