package com.troy.troyberry.logging;

import java.text.*;
import java.util.*;

public class Timer {

	private long startTime, endTime;

	public Timer() {
		this.startTime = System.nanoTime();
	}

	public String getTime() {
		stop();
		float difference = (this.endTime - this.startTime);
		String s;
		//for millaseconds
		if (difference > 1000000L && difference < 1000000000L) {
			s = ((difference / (float) 1000000) + " milla seconds");

		}
		//for seconds
		else if (difference > 1000000000L) {
			s = ((difference / (float) 1000000000) + " seconds");
		} else {
			s = (NumberFormat.getNumberInstance(Locale.US).format(difference) + " nano seconds");
			if (difference > 100000) {
				s = ("Or " + (difference / (float) 1000000) + " milla seconds");
			}
		}

		return s;
	}

	public Timer stop() {
		this.endTime = System.nanoTime();
		return this;
		//
	}

	public Timer reset() {
		this.startTime = System.nanoTime();
		this.endTime = 0L;
		return this;
	}

}
