package com.mxscript;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

public class TaskManager extends Thread {
	
	protected List<Task> tasks = new ArrayList<Task>();
	protected List<Task> runningTasks = new ArrayList<Task>();
	protected boolean stop = false; //this variable is set when the stopTasks method is invoked
	protected int maxConcurrentTasks = 0;
	protected boolean completed = false;
	
	public TaskManager(int maxConcurrentTasks) {
		this.maxConcurrentTasks = maxConcurrentTasks;
	}
	
	public void addTask(Task task) {
		tasks.add(task);
	}
	
	public boolean isCompleted() {
		return this.completed;
	}
	
	/**
	 * Implementation of Thread.run interface
	 */
	public void run() {
		Logger.getLogger("TaskManager.class").debug("starting tasks");
		try{
			startTasks();
		}catch(MXBeanShellException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Manages start of all tasks respecting a max number of concurrent tasks to start a new parallel task
	 * @param maxConcurrentTasks
	 */
	public void startTasks() throws MXBeanShellException {
		int totalTasks = tasks.size();
		int intRunningTasks = 0;
		int nextTask = 0;
		boolean tasksCompleted = false;
		
		//run first task as a test before moving on with concurrent threads
		Task task = tasks.get(0);
		if ( task.isFirst() ){
			startTask(0); //execute first task
			while( true ) {
				
				if( this.stop) {
					task.getEvent().getLogger().error("User requested to abort");
					throw new MXBeanShellException("User requested to abort");					
				}
				
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) { }
				
				if ( task.hasError() ){
					break;
				}
				
				if ( task.isComplete() ){
					break;
				}
				
			}
			
			if(task.hasError()) {
				task.getEvent().getLogger().error("First task has not completed successfully. Aborting");
				throw new MXBeanShellException("First task has not completed successfully. Aborting");
			} else {
				if(this.stop) {
					task.getEvent().getLogger().error("User requested to abort");
					throw new MXBeanShellException("User requested to abort");
				}			
			}
			
			nextTask++;
		}
		
		try{
			Logger.getLogger("TaskManager.class").debug("totalTasks: " + totalTasks);
			while(!tasksCompleted) {
				if(this.stop) {
				   break;
				}				
				//gets the number of runningTasks
				intRunningTasks = getRunningTasksCount();
				//start tasks until it reaches the max number of concurrent tasks chosen
				if( (intRunningTasks < maxConcurrentTasks) && (nextTask < totalTasks) ) {
					Logger.getLogger("TaskManager.class").debug("runningTasks: " + intRunningTasks);
					Logger.getLogger("TaskManager.class").debug("maxConcurrentTask: " + maxConcurrentTasks);
					Logger.getLogger("TaskManager.class").debug("nextTask: " + nextTask);
					
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}					
					
					if(this.stop) {
					   break;
					}
					
					startTask(nextTask); //
					nextTask ++;
				}
				
				//if the user requests threads to stop then leave the loop
				if(this.stop) {
					break;
				}
				
				tasksCompleted = isTaskListCompleted();
			}
		} finally {
			this.completed = true;
		}
	}
	
	/**
	 * Start a task by its index
	 * @param taskIndex
	 */
	private Task startTask(int taskIndex) {
		Task task = tasks.get(taskIndex);
		task.setRunning(true);
		task.start();
		return task;
	}

	/**
	 * Stop all tasks running by interrupting all running threads
	 *
	 */
	public void stopTasks() {
		this.stop = true;
		for(Task task: tasks) {
			try{
				if(task.isRunning() || !task.isComplete()){
					task.stopTask();
					task.interrupt();
				}
			}catch(Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Gets the current number of running tasks
	 */
	public int getRunningTasksCount() {
		int runningTasksCount = 0;
		for(Task task: tasks) {
			try{
				if(task.isRunning()) {
					runningTasksCount ++;
				}
			}catch(Exception e) {
				e.printStackTrace();
			}
		}
		return runningTasksCount;
	}

	/**
	 * Gets the current number of running tasks
	 */
	public int updateCompletedTasks() {
		List<Task> removeList = new ArrayList<Task>();
		int numTasksCompleted = 0;
		for(Task task: tasks) {
			try{
				if(task.isComplete()) {
					task.log("task for host: " + task.getServerName() + " has been completed.");
					numTasksCompleted ++;
				}
			}catch(Exception e) {
				e.printStackTrace();
			}
		}
		
		for(Task task: removeList) {
			runningTasks.remove(task);
		}
		
		return numTasksCompleted;
	}
	
	/**
	 * Verify if all tasks are completed
	 */
	public boolean isTaskListCompleted() {
		boolean completed = true;
		
		for(Task task: tasks) {
			if(!task.isComplete()) {
				completed = false;
			}
		}
		
		return completed;
	}

	/**
	 * Disconnect all ssh clients connected
	 */
	public void disconnectAll() {
		for(Task task: tasks) {
			if(task != null && task.isConnected()) {
				task.disconnect();
			}
		}
	}
	
	/**
	 * Get average time spent on each task
	 * @return
	 */
	public long getAverageTime() {
		long avgTime = 0;
		int totalTasks = tasks.size();
		for(Task task: tasks) {
			avgTime = avgTime + task.getTotalTime();
		}		
		avgTime = avgTime / totalTasks;
		return avgTime;
	}
	
	public boolean isStopped() {
		return this.stop;
	}
}
