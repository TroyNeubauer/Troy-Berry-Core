package com.troyberry.util.data;

public class UpdateTimeStamp {
	
	private long time;
	private double targetUPS;
	private UpdateTimeStampManager manager;
	
	public UpdateTimeStamp(UpdateTimeStampManager manager, long time, double targetUPS) {
		this.time = time;
		this.targetUPS = targetUPS;
		this.manager = manager;
		manager.add(this);
	}
	
	public long getTime() {
		return time;
	}

	public double getTargetUPS() {
		return targetUPS;
	}

	@Override
	public String toString() {
		return "UpdateTimeStamp [time=" + time + ", targetUPS=" + targetUPS + "]";
	}
	
	

}
