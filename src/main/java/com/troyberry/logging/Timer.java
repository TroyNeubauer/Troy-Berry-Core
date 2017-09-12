package com.troyberry.logging;

import java.text.NumberFormat;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

/**
 * A class that very easily allows the timing the any action using nanosecond precision.<br>
 * To use this class to time something, create a new timer object, this will start counting automatically. Then do the task you wish to time,
 * promptly after finishing the task, call Timer.stop() to stop the timer. To get the time taken call Timer.getRawTime() to get the amount of
 * nanoseconds taken. <br>
 * 
 * To get the time taken in a time unit other than nanoseconds, Call Timer.getTime() witch will return a String something like "15,604
 * nanoseconds" or "14.243 milliseconds", in whichever the most reasonable time unit is. Something like "12050801 nanoseconds" will never be
 * returned because 12.050801 milliseconds is a "most reasonable" time unit <br>
 * <br>
 * Due to thread scheduling and differences in JVM's, this class may not always deliver 100% accuracy. This class does its best to deliver
 * accurate results, although the actual time will usually be slightly faster than the time reported by 0 - 1000 nanoseconds.
 * 
 * @author Troy Neubauer
 *
 */
public class Timer {

	/**
	 * Time units expressed in nanosecond precision
	 */
	public static final double NANOS = 1.0;
	public static final double MILLIS = 1000.0 * 1000.0;
	public static final double SECONDS = MILLIS * 1000.0;
	public static final double MINUTES = SECONDS * 60.0;
	public static final double HOURS = MINUTES * 60.0;
	public static final double DAYS = HOURS * 24.0;
	public static final double WEEKS = DAYS * 7.0;
	public static final double MONHS = DAYS * 30.0;
	public static final double YEARS = DAYS * 365.2422;

	private long startTime, endTime;
	private boolean running;

	/**
	 * Creates a new timer object and starts timing be sure to call Timer.stop() before getting the time taken!
	 * 
	 * @see Timer#stop()
	 * @see Timer#getTime()
	 * @see Timer#getRawTime()
	 */
	public Timer() {
		running = true;
		this.startTime = System.nanoTime();// Doing this last to retain accuracy
	}

	/**
	 * Returns the amount of time taken formatted into the most desirable time unit. something like "15,604 nanoseconds" or "14.243
	 * milliseconds". Something like "12050801 nanoseconds" will never be returned. <br>
	 * 
	 * @return The string containing the time
	 * @throws IllegalStateException
	 *             if the timer is running. be sure to call Timer.stop() before calling this method.
	 * @see Timer#stop()
	 * @see Timer#reset()
	 */
	public String getTime() {
		if (running)
			throw new IllegalStateException("The timer has not been stopped! Cannot get time taken! Call Timer.stop()");
		return getString(this.endTime - this.startTime);
	}

	/**
	 * Returns a string representation of the time passed in (in nanoseconds). the time will be formatted into the most desirable time unit.
	 * something like "15,604 nanoseconds" or "14.243 milliseconds"
	 * 
	 * @param time
	 *            The time in nanoseconds to format
	 * @return The formatted String
	 */
	public static String getString(long time) {
		time = Math.abs(time);
		if (time < MILLIS * 0.5)
			return NumberFormat.getNumberInstance(Locale.US).format(time / NANOS) + " " + TimeUnit.NANOSECONDS.name().toLowerCase();
		if (time < SECONDS)
			return NumberFormat.getNumberInstance(Locale.US).format(time / MILLIS) + " " + TimeUnit.MILLISECONDS.name().toLowerCase();
		if (time < MINUTES)
			return NumberFormat.getNumberInstance(Locale.US).format(time / SECONDS) + " " + TimeUnit.SECONDS.name().toLowerCase();
		if (time < HOURS) {
			long minuites = (long) (time / MINUTES);
			long seconds = (long) ((time - ((long) (time / MINUTES) * MINUTES)) / SECONDS);
			if (seconds < 1) {
				return minuites + " " + TimeUnit.MINUTES.name().toLowerCase();
			}
			return minuites + " " + TimeUnit.MINUTES.name().toLowerCase() + " and " + NumberFormat.getNumberInstance(Locale.US).format(seconds)
					+ " " + TimeUnit.SECONDS.name().toLowerCase();
		}

		if (time < DAYS) {
			long hours = (long) (time / HOURS);
			long minutes = (long) ((time - ((long) (time / HOURS) * HOURS)) / MINUTES);
			if (minutes < 1) {
				return hours + " " + TimeUnit.HOURS.name().toLowerCase();
			}
			return hours + " " + TimeUnit.HOURS.name().toLowerCase() + " and " + NumberFormat.getNumberInstance(Locale.US).format(minutes)
					+ " " + TimeUnit.MINUTES.name().toLowerCase();
		}
		if (time < YEARS) {
			long days = (long) (time / DAYS);
			long hours = (long) ((time - ((long) (time / DAYS) * DAYS)) / HOURS);
			if (hours < 1) {
				return days + " " + TimeUnit.DAYS.name().toLowerCase();
			}
			return days + " " + TimeUnit.DAYS.name().toLowerCase() + " and " + NumberFormat.getNumberInstance(Locale.US).format(hours)
					+ " " + TimeUnit.HOURS.name().toLowerCase();
		}

		{
			long years = (long) (time / YEARS);
			long days = (long) ((time - ((long) (time / YEARS) * YEARS)) / DAYS);
			if (days < 1) {
				return NumberFormat.getNumberInstance(Locale.US).format(years) + com.troyberry.util.StringFormatter.plural(" year", years);
			}
			return NumberFormat.getNumberInstance(Locale.US).format(years) + com.troyberry.util.StringFormatter.plural(" year", years) + " and " + 
					+ days + com.troyberry.util.StringFormatter.plural(" day", days);
		}
	}

	/**
	 * Returns the time taken in nanoseconds.
	 * 
	 * @throws IllegalStateException
	 *             if the timer is running. be sure to call Timer.stop() before calling this method.
	 * @return The time taken in nanoseconds from object creation or since the last timer.reset() was called
	 * @see Timer#stop()
	 * @see Timer#reset()
	 */
	public long getRawTime() {
		if (running)
			throw new IllegalStateException("The timer has not been stopped! Cannot get time taken! Call Timer.stop()");
		return this.endTime - this.startTime;
	}

	/**
	 * Stops the timer and records the time taken. This must be called before and sort of "number crunching" methods can be used such as
	 * getRawTime() and getTime().
	 * 
	 * @return A this timer object so this call can be stacked IE timer.stop().getRawTime();
	 * @see Timer#getTime()
	 * @see Timer#getRawTime()
	 */
	public Timer stop() {
		if (running)
			this.endTime = System.nanoTime();
		// Because checking the condition takes some time, a bit of accuracy is lost here
		running = false;
		return this;
	}

	/**
	 * Resets the timer so that it can be used again. Timer.stop() must be called before and sort of "number crunching" methods can be used such
	 * as getRawTime() and getTime().
	 * 
	 * @see Timer#stop()
	 */
	public void reset() {
		this.endTime = 0L;
		running = true;
		this.startTime = System.nanoTime();// Doing this last to retain accuracy
	}

	public void printTime() {
		System.out.println(getTime());
	}
}
