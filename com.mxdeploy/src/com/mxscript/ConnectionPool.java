package com.mxscript;

import com.mxssh.SSHServiceNew;

public class ConnectionPool {
	private Thread threads[];
	private Connection[] connections;
	private int poolSize=0;
	private String hostname;
	private String username;
	private String password;
	
	public ConnectionPool(String hostname, String username, String password, int poolSize){
		this.poolSize = poolSize;
		this.connections = new Connection[poolSize];
		this.hostname = hostname;
		this.username = username;
		this.password = password;
		
		for(int i=0; i < poolSize; i++){
			Connection connection = new Connection(this,i);
			connections[i] = connection;
		}
	}
	 
	private int getNextConnection(){
		synchronized (connections) {
			for (int i = 0; i < connections.length; i++) {
				if ( connections[i].isAvailable() ) {
					 connections[i].setAvailable(false);
					 return i;
				}
			} 
		}
		return -1;
	}
	
	protected synchronized void releaseConnection(int index){
		connections[index] = null;
		notifyAll();
	}
	
	public synchronized Connection getConnection(){
		int nextConnection;
		while((nextConnection = getNextConnection()) == -1){
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		return connections[nextConnection];
	}
	
	public synchronized void disconnectAll(){
		for(int i=0; i < poolSize; i++){
			if( connections[i].getSshServiceNew() !=null && connections[i].getSshServiceNew().isConnected() ){
				connections[i].getSshServiceNew().disconnect();
			}
		}		
	}
	
	
	public Connection[] getConnections() {
		return connections;
	}

	public void setConnections(Connection[] connections) {
		this.connections = connections;
	}

	public String getHostname() {
		return hostname;
	}

	public void setHostname(String hostname) {
		this.hostname = hostname;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public int getPoolSize() {
		return poolSize;
	}

	public void setPoolSize(int poolSize) {
		this.poolSize = poolSize;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public synchronized void createPool(final String hostname, final String username, final String password){
		threads = new Thread[poolSize];
		
		for(int i=0;poolSize > i;i++){
			threads[i] = new Thread() {
				public void run() {
	
					try {
						SSHServiceNew sshService = new SSHServiceNew(); 
						sshService.connect(hostname, username, password);
						//add(sshService);
					} catch (Exception ex) {
						ex.printStackTrace();
					}
	
				}
				
			};
			threads[i].start();
		}
		
		boolean isAlive = false;
		while(true){
			for(int j=0;poolSize > j;j++){
				if(threads[j].isAlive()){
					isAlive = true;
					break;
				}
			}
			
			if(!isAlive){
				break;
			}
			
			isAlive=false;
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
	}
	
	


	
	
	
}
