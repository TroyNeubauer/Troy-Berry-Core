package com.troyberry.math;


public class SmoothDouble {
	
	private final double agility;
	
	private double target;
	private double actual;
	
	public SmoothDouble(double initialValue, double agility){
		this.target = initialValue;
		this.actual = initialValue;
		this.agility = agility;
	}
	
	public void update(double delta){
		double offset = target - actual;
		double change = offset * delta * agility;
		actual += change;
	}
	
	public void increaseTarget(double dT){
		this.target += dT;
	}
	
	public void setTarget(double target){
		this.target = target;
	}
	
	public void instantIncrease(double increase){
		this.actual += increase;
	}
	
	public double get(){
		return actual;
	}
	
	public double getTarget(){
		return target;
	}

	public void clamp(double min, double max) {
		actual = Maths.clamp(min, max, actual);
		target = Maths.clamp(min, max, target);
	}

}

