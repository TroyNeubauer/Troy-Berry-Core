package com.troyberry.logging;

import java.text.*;
import java.util.*;

/**
 * A class that very easily allows the timing the any action using nanosecond precision.<br>
 * To use this class to time something, create a new timer object, this will start counting automatically.
 * Then do the task you wish to time, promptly after finishing the task, call Timer.stop() to stop the timer. 
 * To get the time taken call Timer.getRawTime() to get the amount of nanoseconds taken. <br>
 * 
 * To get the time taken in a time unit other than nanoseconds, Call Timer.getTime() witch will return a String something like 
 * "15,604 nanoseconds" or "14.243 milliseconds", in whichever the most reasonable time unit is. Something like 
 * "12050801 nanoseconds" will never be returned because 12.050801 milliseconds is a "most reasonable" time unit
 * <br><br>
 * Due to thread scheduling and differences in JVM's, this class may not always deliver 100% accuracy. This class does its best to 
 * deliver accurate results, although the actual time will usually be slightly faster than the time reported by 0 - 1000 nanoseconds.
 * @author Troy Neubauer
 *
 */
public class Timer {

	private long startTime, endTime;
	private boolean running;
	
	/**
	 * Creates a new timer object and starts timing
	 * be sure to call Timer.stop() before getting the time taken!
	 * @see Timer#stop()
	 * @see Timer#getTime()
	 * @see Timer#getRawTime()
	 */
	public Timer() {
		running = true;
		this.startTime = System.nanoTime();// Doing this last to retain accuracy
	}
	/**
	 * Returns the amount of time taken formatted into the most desirable time unit. something like "15,604 nanoseconds" or 
	 * "14.243 milliseconds". Something like "12050801 nanoseconds" will never be returned. <br>
	 * 
	 * @return The string containing the time
	 * @throws IllegalStateException if the timer is running. be sure to call Timer.stop() before calling this method.
	 * @see Timer#stop()
	 * @see Timer#reset()
	 */
	public String getTime() {
		if(running) throw new IllegalStateException("The timer has not been stopped! Cannot get time taken! Call Timer.stop()");
		return getString(this.endTime - this.startTime);
	}
	
	/**
	 * Returns a string representation of the time passed in (in nanoseconds). the time will be formatted into the most 
	 * desirable time unit. something like "15,604 nanoseconds" or "14.243 milliseconds"
	 * @param time The time in nanoseconds to format
	 * @return The formatted String
	 */
	public static String getString(long time) {
		String s;
		// for milliseconds
		if (time > 1000000L && time < 1000000000L) {
			s = ((time / 1000000f) + " milla seconds");

		}
		// for seconds
		else if (time > 1000000000L) {
			s = ((time / 1000000000f) + " seconds");
		} else {
			s = (NumberFormat.getNumberInstance(Locale.US).format(time) + " nano seconds");
			if (time > 100000) {
				s += (" Or " + (time / 1000000f) + " milla seconds");
			}
		}

		return s;
	}
	
	/**
	 * Returns the time taken in nanoseconds.
	 * @throws IllegalStateException if the timer is running. be sure to call Timer.stop() before calling this method.
	 * @return The time taken in nanoseconds from object creation or since the last timer.reset() was called
	 * @see Timer#stop()
	 * @see Timer#reset()
	 */
	public long getRawTime() {
		if(running) throw new IllegalStateException("The timer has not been stopped! Cannot get time taken! Call Timer.stop()");
		return this.endTime - this.startTime;
	}
	
	/**
	 * Stops the timer and records the time taken. This must be called before and sort of "number crunching" methods can be 
	 * used such as getRawTime() and getTime().
	 * @return A this timer object so this call can be stacked IE timer.stop().getRawTime();
	 * @see Timer#getTime()
	 * @see Timer#getRawTime()
	 */
	public Timer stop() {
		if(running) this.endTime = System.nanoTime();
		// Because checking the condition takes some time, a bit of accuracy is lost here
		running = false;
		return this;
	}
	
	/**
	 * Resets the timer so that it can be used again. Timer.stop() must be called before and sort of "number crunching" methods 
	 * can be used such as getRawTime() and getTime().
	 * @return A this timer object so this call can be stacked.
	 * @see Timer#stop()
	 */
	public Timer reset() {
		this.endTime = 0L;
		running = true;
		this.startTime = System.nanoTime();// Doing this last to retain accuracy
		return this;
	}
}
