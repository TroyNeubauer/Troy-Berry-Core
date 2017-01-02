package com.troyberry.util.thread;

import java.util.concurrent.atomic.*;

public class Processor implements Runnable {
	
	protected AtomicBoolean workingOnTask = new AtomicBoolean(false);
	protected AtomicBoolean running;
	protected volatile Task currentTask;
	private volatile int id;
	
	public Processor(int id) {
		this.id = id;
		this.running = new AtomicBoolean(true);
	}
	
	public void setTask(Task runnable){
		synchronized (this) {
			currentTask = runnable;
			workingOnTask.set(true);
			this.notify();
		}
	}
	
	public void stop(){
		synchronized (this) {
			currentTask = null;
			running.set(false);
			this.notify();
		}
	}
		

	@Override
	public void run() {
		while(running.get()) {
			synchronized (this) {
				while(!workingOnTask.get() && running.get()){
					try {
						this.wait();
					} catch (InterruptedException e) {
						return;
					}
				}
				if(currentTask != null){
					//System.out.println("Running task id "+currentTask.getTaskId()+" on thread " + id);
					currentTask.run();
					//System.out.println("Worker " + id + " done with task id " + currentTask.getTaskId());
				}
				currentTask = null;
				workingOnTask.set(false);
				
			}
		}
	}

	public int getId() {
		return id;
	}
	
	

}
