package com.mxscript;

import com.mxssh.SSHClient;

public class Connection {
	private ConnectionPool pool;
	private int index =0;
	private SSHClient sshServiceNew = null;
	private boolean isAvailable = true;
	
	public synchronized boolean isAvailable() {
		return isAvailable;
	}

	public synchronized void setAvailable(boolean isAvailable) {
		this.isAvailable = isAvailable;
	} 

	public Connection(ConnectionPool pool,int index, SSHClient sshServiceNew){
		this.pool = pool;
		this.index = index;
		this.sshServiceNew = sshServiceNew;
	}
	
	public Connection(ConnectionPool pool,int index){
		this.pool = pool;
		this.index = index;
		this.sshServiceNew = null;
	}	
	
	public int getIndex() {
		return index;
	}
	public void setIndex(int index) {
		this.index = index;
	}
	public ConnectionPool getPool() {
		return pool;
	}
	public void setPool(ConnectionPool pool) {
		this.pool = pool;
	}
	protected SSHClient getSshServiceNew() {
		if(sshServiceNew==null){
		   sshServiceNew = new SSHClient();
		}
		return sshServiceNew;
	}
	
//	public void setSshServiceNew(SSHServiceNew sshServiceNew) {
//		this.sshServiceNew = sshServiceNew;
//	}
	
	protected synchronized void release(){
		setAvailable(true);
	}
	
	

}
