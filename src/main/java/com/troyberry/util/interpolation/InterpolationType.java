package com.troyberry.util.interpolation;

import com.troyberry.math.*;

public enum InterpolationType {
	LINEAR(new DoAction(){

        @Override
        public double doAction(double a, double b, double f) {
            return Maths.lerp(a, b, f);
        }
    }), COSINE(new DoAction(){

        @Override
        public double doAction(double a, double b, double f) {
            return Maths.cerp(a, b, f);
        }
    });

	public double interpolate(double a, double b, double f) {
		return this.action.doAction(a, b, f);
	}

	private final DoAction action;
	private final int id;

	InterpolationType(DoAction action) {
		this.action = action;
		this.id = this.ordinal();
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
