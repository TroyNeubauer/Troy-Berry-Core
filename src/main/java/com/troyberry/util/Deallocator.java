package com.troyberry.util;

public abstract class Deallocator implements Runnable {
	
	/**
	 * Called when the object that is linked to this Deallocator is no longer accessible allowing all native or other resources to be freed
	 * @see Runnable#run()
	 */
	public abstract void run();

}
