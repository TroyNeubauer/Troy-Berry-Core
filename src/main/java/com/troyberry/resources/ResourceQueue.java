package com.troyberry.resources;

import java.util.LinkedList;

public class ResourceQueue {

	private LinkedList<ResourceRequest> requestQueue = new LinkedList<ResourceRequest>();
	
	public synchronized void addRequest(ResourceRequest request){
		requestQueue.addLast(request);
	}
	
	public synchronized ResourceRequest acceptNextRequest(){
		return requestQueue.removeFirst();
	}
	
	public synchronized boolean hasRequests(){
		return !requestQueue.isEmpty();
	}
	
}
