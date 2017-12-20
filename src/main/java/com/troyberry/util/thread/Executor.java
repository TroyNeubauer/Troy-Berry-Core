package com.troyberry.util.thread;

import java.util.*;

public class Executor {

	private volatile LinkedList<Task> awaitingTasks;
	private ThreadGroup group;
	private Thread[] threads;
	private Processor[] processors;

	public Executor(int threadCount, String name) {
		this.group = new ThreadGroup(name);
		this.threads = new Thread[threadCount];
		this.processors = new Processor[threadCount];
		this.awaitingTasks = new LinkedList<Task>();
		for (int i = 0; i < threadCount; i++) {
			processors[i] = new Processor(i);
			threads[i] = new Thread(group, processors[i], name + " #" + i);
			threads[i].start();
		}
		group.setMaxPriority(Thread.MAX_PRIORITY);
	}

	public void submitTask(Task task) {
		for (Processor p : processors) {
			if (p.workingOnTask.get() == false) {
				p.setTask(task);
				return;
			}
		}
		awaitingTasks.addLast(task);
	}

	public void shutdown() {
		for (Processor p : processors) {
			p.stop();
		}
		for (Thread t : threads) {
			t.interrupt();
		}
		for (Thread t : threads) {
			try {
				t.join();
			} catch (InterruptedException ignore) {
			}
		}
		group.destroy();
	}

	public static enum ExecutorState {
		ACCEPTING_NEW_TASKS, NOT_ACCEPTING_NEW_TASKS, WILL_TERMINATE_WAITING_ON_LAST_PROCESS_TO_FINISH, TERMINATED;
	}

	/**
	 * This method will wait and only return once all tasks are complete. This method will automatically call update so that 
	 * queued tasks will be assigned and completed automatically.<br>
	 * If you want to see weather or not tasks are finished and have the method return asynchronously, {@link Executor#allTasksFinished()}
	 * does exactly that
	 */
	public void waitForTasksAllTasksToComplete() {

		while (true) {
			update();
			boolean clean = true;
			for (Processor p : processors) {
				if (p.workingOnTask.get()) {
					try {
						Thread.sleep(10);
					} catch (InterruptedException e) {
					}
					clean = false;
					break;
				}
			}
			if (!clean) continue;

			if (awaitingTasks.isEmpty()) return;

			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
			}

		}
	}

	/**
	 * Ensures that all processors are working on something and assigns them a new task if there is queue task ready 
	 * and there aren't working
	 */
	public void update() {
		// We have tasks that need to be done
		if (awaitingTasks.size() > 0) {
			for (Processor p : processors) {
				if (!p.workingOnTask.get()) {
					if (awaitingTasks.size() > 0) p.setTask(awaitingTasks.removeFirst());
				}
			}
		}
	}

	/**
	 * Returns true if all tasks are complete
	 * This method returns instantly, it does not wait for tasks to end. <br>
	 * If you need this method to wait until all tasks are complete {@link Executor#waitForTasksAllTasksToComplete()} does exactly that
	 * @return Weather or not all tasks are complete
	 */
	public boolean allTasksFinished() {
		if (awaitingTasks.size() > 0) return false;
		for (Processor p : processors) {
			if (p.workingOnTask.get()) return false;
		}
		return true;
	}

}
