package com.troyberry.logging;

import java.text.*;
import java.util.*;

/**
 * This is a simple class that acts as a shortened version of 
 * System.out.println() and System.err.println()
 *  
 */

public class Log {

	/** Prints an standard message
	 * @param message this parameter message will be printed to the System.out 
	 */

	public static void info(String message) {
		String timeStamp = new SimpleDateFormat("HH:mm:ss").format(new Date());
		long millis = System.currentTimeMillis() % 1000;
		System.out.println(timeStamp + "." + millis + ": [Info] " + message);
	}
	
	/** Prints an standard message
	 * @param message this parameter message will be printed to the System.out 
	 */
	
	public static void info(Object message) {
		info(message.toString());
	}


	/** Prints an error message
	 * @param message this parameter message will be printed to the System.err 
	 */
	
	public static void error(String message) {
		String timeStamp = new SimpleDateFormat("HH:mm:ss").format(new Date());
		long millis = System.currentTimeMillis() % 1000;
		System.err.println(timeStamp + "." + millis + ": [ERROR] " + message);
	}
	
	/** Prints an error message
	 * @param message this parameter message will be printed to the System.err 
	 */
	
	public static void error(Object message) {
		error(message.toString());
	}
	
	/** Prints an error message
	 * @param message this parameter message will be printed to the System.err 
	 */
	
	public static void fatal(String message) {
		String timeStamp = new SimpleDateFormat("HH:mm:ss").format(new Date());
		long millis = System.currentTimeMillis() % 1000;
		System.err.println(timeStamp + "." + millis + ": [FATAL ERROR] " + message);
	}
	
	/** Prints an error message
	 * @param message this parameter message will be printed to the System.err 
	 */
	
	public static void fatal(Object message) {
		fatal(message.toString());
	}
	
	/** Prints a warning message
	 * @param message this parameter message will be printed to the System.err 
	 */

	public static void warning(String message) {
		String timeStamp = new SimpleDateFormat("HH:mm:ss").format(new Date());
		long millis = System.currentTimeMillis() % 1000;
		System.out.println(timeStamp + "." + millis + ": [WARNING] " + message);
	}
	
	/** Prints a warning message
	 * @param message this parameter message will be printed to the System.err 
	 */
	
	public static void warning(Object message) {
		warning(message.toString());
	}
	
	/** To make this a entirely static class **/
	private Log() {

	}

}
