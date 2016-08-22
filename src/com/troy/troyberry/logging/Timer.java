package com.troy.troyberry.logging;

import java.text.*;
import java.util.*;

public class Timer {
	
	private long startTime;
	
	public Timer() {
		this.startTime = System.nanoTime();
	}
	
	public Timer stop(){
		float difference = (System.nanoTime() - this.startTime);
		
		//for millaseconds
		if(difference > 1000000L && difference < 1000000000L){
			System.out.println(  (difference / (float)1000000) + " milla seconds");
			
		}
		//for seconds
		else if(difference > 1000000000L){
			System.out.println(  (difference / (float)1000000000) + " seconds");
		}else{
			System.out.println( NumberFormat.getNumberInstance(Locale.US)
					.format(difference) + " nano seconds");
			if(difference > 100000){
				System.out.println( "Or " + (difference / (float)1000000) + " milla seconds");
			}
		}
		this.startTime = System.nanoTime();
		return this;
		//
	}
	
	public Timer reset(){
		this.startTime = System.nanoTime();
		return this;
	}

}
