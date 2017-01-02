package com.troyberry.util;

public class ThreadUtils {
	
	/**
	 * Sleeps the current for MS and ignores any InterruptedExceptions
	 * @param ms The time in milliseconds to sleep
	 */
	public static void sleep(long ms){
		try {
			Thread.sleep(ms);
		} catch (InterruptedException e) {
		}
	}

	private ThreadUtils() {
	}
	
	/**
	 * Waits for the desired thread to stop and ignores any InterruptedExceptions
	 * @param thread The thread to wait for
	 */
	public static void join(Thread thread) {
		try {
			thread.join();
		} catch (InterruptedException e) {
		}
	}

}
