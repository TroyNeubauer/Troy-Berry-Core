package com.troyberry.util;

/**
 * A simple static class for handling thread functions (sleep, join etc) ignoring any InterruptedExceptions
 */
public class ThreadUtils {
	
	/**
	 * Sleeps the current thread for MS milliseconds and ignores any InterruptedExceptions
	 * @param ms The time in milliseconds to sleep
	 */
	public static void sleep(long ms){
		try {
			Thread.sleep(ms);
		} catch (InterruptedException e) {
		}
	}

	
	/**
	 * Waits for the desired thread to stop and ignores any InterruptedExceptions
	 * @param thread The thread to wait for
	 * @see Thread#join()
	 */
	public static void join(Thread thread) {
		try {
			thread.join();
		} catch (InterruptedException e) {
		}
	}
	
	/**
	 * Waits for the desired thread to stop and ignores any InterruptedExceptions
	 * @param thread The thread to wait for
	 * @param milliseconds The number of milliseconds to wait for before returning
	 * @see Thread#join(long)
	 */
	public static void join(Thread thread, long milliseconds) {
		try {
			thread.join(milliseconds);
		} catch (InterruptedException e) {
		}
	}

	private ThreadUtils() {
	}
}
