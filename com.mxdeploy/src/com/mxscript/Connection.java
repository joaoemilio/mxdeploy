package com.mxscript;

import com.mxssh.SSHServiceNew;

public class Connection {
	private ConnectionPool pool;
	private int index =0;
	private SSHServiceNew sshServiceNew = null;
	private boolean isAvailable = true;
	
	public synchronized boolean isAvailable() {
		return isAvailable;
	}

	public synchronized void setAvailable(boolean isAvailable) {
		this.isAvailable = isAvailable;
	} 

	public Connection(ConnectionPool pool,int index, SSHServiceNew sshServiceNew){
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
	protected SSHServiceNew getSshServiceNew() {
		if(sshServiceNew==null){
		   sshServiceNew = new SSHServiceNew();
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
