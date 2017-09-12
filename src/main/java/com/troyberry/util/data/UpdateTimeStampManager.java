package com.troyberry.util.data;

import java.util.*;

public class UpdateTimeStampManager {
	
	private LinkedList<UpdateTimeStamp> stamps;
	
	public UpdateTimeStampManager() {
		this.stamps = new LinkedList<UpdateTimeStamp>();
	}
	
	protected void add(UpdateTimeStamp stamp){
		stamps.addLast(stamp);
	}

}
