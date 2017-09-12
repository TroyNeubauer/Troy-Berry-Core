package com.troyberry.resources;

public class ResourceProcessor extends Thread {

	private static final ResourceProcessor PROCESSOR = new ResourceProcessor();

	private ResourceQueue requestQueue = new ResourceQueue();
	private boolean running = true;

	public static void sendRequest(ResourceRequest request) {
		PROCESSOR.addRequestToQueue(request);
	}

	public static void cleanUp() {
		PROCESSOR.kill();
	}

	public synchronized void run() {
		while (running || requestQueue.hasRequests()) {
			if (requestQueue.hasRequests()) {
				requestQueue.acceptNextRequest().doResourceRequest();
			} else {
				try {
					wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}

	private void kill() {
		running = false;
		indicateNewRequests();
	}

	private synchronized void indicateNewRequests() {
		notifyAll();
	}

	private ResourceProcessor() {
		super("ResourceProcessor Thread");
		this.start();
	}

	private void addRequestToQueue(ResourceRequest request) {
		boolean isPaused = !requestQueue.hasRequests();
		requestQueue.addRequest(request);
		if (isPaused) {
			indicateNewRequests();
		}
	}

}
