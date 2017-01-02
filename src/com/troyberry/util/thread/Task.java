package com.troyberry.util.thread;

public abstract class Task implements Runnable {
	
	private int taskId;
	public abstract void onRun();
	private volatile long timeTaken = -1;
	
	public void run(){
		long start = System.nanoTime();
		onRun();
		timeTaken = System.nanoTime() - start;
	}

	public Task(int taskId) {
		this.taskId = taskId;
	}
	
	public int getTaskId() {
		return taskId;
	}
	
	public void setTaskId(int taskId) {
		this.taskId = taskId;
	}
	
	public long getTimeTaken() {
		if(timeTaken == -1)throw new IllegalStateException("The task hasent been run yet...");
		return timeTaken;
	}

}
