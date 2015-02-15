package com.mxscript;

import java.io.IOException;

import org.apache.log4j.Logger;
import org.eclipse.swt.widgets.Display;

import com.mxscript.swt.BeanShellFormComposite;

public class ThreadPoolTask {
	private Thread threads[];
	private Task[] tasks;
	public  boolean isStoppingThreads = false;
	private boolean wasTested = false;
	
	private BeanShellFormComposite composite;
	private int threadNumber;
	private int threadIndex=0;
	
	private Logger logger = Logger.getLogger(ThreadPoolTask.class);
	
	public ThreadPoolTask(BeanShellFormComposite composite, int threadNumber){
		logger.debug("constructor ThreadPoolTask " + threadNumber);
		this.composite = composite;
		this.threadNumber = threadNumber;
		threads = new Thread[threadNumber];
		tasks = new Task[threadNumber];
		isStoppingThreads = false;
	}
	
	public synchronized boolean addOperation(Task task) {

		int nextThread;
		while ((nextThread = getNextThread()) == -1) {
			try {
				if(!isStoppingThreads){
				   wait();
				}
				
				if(isStoppingThreads){
				   break;
				}
			} catch (InterruptedException ex) {
				ex.printStackTrace();
			}
		}
		
		if(!isStoppingThreads){
		   tasks[nextThread] = task;
		   
		   logger.debug("run thread for: " + task.getServerName() );
		   start(task, nextThread);
		}
		
		return !isStoppingThreads;

	}

	public int getNextThread() {
		synchronized (threads) {
			for (int i = 0; i < threads.length; i++) {
				if( composite.isStopBeanShell() || ( tasks[i]!=null && isStoppingThreads ) ){
					break;
				}
				if ( ( (!wasTested && i==0) ||  wasTested )&&  threads[i] == null  ) {
					return i;
				} 
			} 
		}
		return -1;

	}
	
	public synchronized void release() {
		
		notifyAll();
	}
	
	public synchronized void close() {
		isStoppingThreads = true;
		
		Thread threadStop = new Thread() {
			public void run() {		
				
				for(threadIndex=0; threadIndex < threads.length;threadIndex++){
					if( tasks[threadIndex]==null ){
					   continue;	
					}
					
					tasks[threadIndex].setStopping(true);
					
					tasks[threadIndex].warn("Stopping Task executing on server - "+tasks[threadIndex].getServerName());
					
					Display.getDefault().syncExec(new Runnable(){
						public void run(){ 
							tasks[threadIndex].setColumnStatus("Stopping");	
						}
					});
					
					if(tasks[threadIndex].getSSHClient()!=null && tasks[threadIndex].getSSHClient().isConnected()){
						try {
							if(tasks[threadIndex].isConnected()){
								// comentei essa linha pois o stop espera o retorno dos comandos rodando parar para depois 
							   //tasks[threadIndex].stopCommand();
							}
							
							if(threads[threadIndex]!=null){
								if(tasks[threadIndex].getConnection()!=null){
								   tasks[threadIndex].getConnection().getPool().disconnectAll();
								} else if(tasks[threadIndex].getSSHClient()!=null){
							      tasks[threadIndex].getSSHClient().disconnect();
							   }
							   
							   tasks[threadIndex].setColumnStatus("Stopped");
							   threads[threadIndex].interrupt();
							   threads[threadIndex]=null;
							   tasks[threadIndex]=null;
							}	 						
						} catch (Exception e) { 
							e.printStackTrace();
						}
					}
			    }
				notifyAll();
			}
			
		};

		threadStop.start();		
	}	
	
	public synchronized void start(final Task task, final int i) {
		threads[i] = new Thread() {
			public void run() {

				try {
					task.run();
					if( !task.isStopping() ){
					   wasTested=true;
					} else {
					   isStoppingThreads=true;
					}
				} catch (Exception ex) {
					task.warn("Task was interrupted");
				}

				synchronized (threads) {
					threads[i] = null;
				}

				release();
			}
			
		};

		threads[i].start();
	}
	
	public Thread[] getThreads() {
		return threads;
	}

	public Task[] getObjectEventArr() {
		return tasks;
	}
	
}
