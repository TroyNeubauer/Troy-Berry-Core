package com.troyberry.logging;

import com.troyberry.color.*;

/**
 * This class represents a way to store the amount of time from the last frame or update, so that animations can always move at a 
 * constant rate. Just calculate the time since the last update or frame, set the value in here, then when animations are happening,
 * Scale their progress by this this value
 * @author Troy Neubauer
 *
 */
public class Time {

	public static float delta = 0.0f;
	
	/**
	 * Returns the time since the frame or update. (Assuming {@link Time#setDelta } has already set the delta)
	 * @return The delta
	 */
	public static float getDelta() {
		return delta;
	}
	
	public static void setDelta(float delta) {
		Time.delta = delta;	
	}
}
