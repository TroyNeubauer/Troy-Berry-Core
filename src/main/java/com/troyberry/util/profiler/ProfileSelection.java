package com.troyberry.util.profiler;

import com.troyberry.logging.*;

/**
 * @author Troy Neubauer
 *
 */
public class ProfileSelection extends ProfileData {
	
	private final long startTime;
	private long endTime = -1;

	public ProfileSelection(long startTime, String name) {
		super(name);
		this.startTime = startTime;
	}

	public ProfileSelection(String name, long startTime, long endTime) {
		super(name);
		this.startTime = startTime;
		this.endTime = endTime;
	}

	void setEndTime(long entTime) {
		this.endTime = entTime;
	}

	public long getStartTime() {
		return startTime;
	}

	public long getEndTime() {
		return endTime;
	}

	public String getName() {
		return name;
	}

	@Override
	public String toString() {
		return "ProfileData [name=" + name + (endTime != -1 ? " Took " + Timer.getString((endTime - startTime)) : " Currently running.") + "]";
	}

	public long getLength() {
		if (endTime == -1) throw new IllegalStateException("Profile has not been finished! Cannot compute time taken!");
		return endTime - startTime;
	}

	@Override
	public byte getIdentifier() {
		return SELECTION_ID;
	}

}
