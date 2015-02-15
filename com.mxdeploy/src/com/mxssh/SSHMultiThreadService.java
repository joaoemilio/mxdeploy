package com.mxssh;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SSHMultiThreadService {
	
	private List<Command> commandList = new ArrayList<Command>();
	public final static int TIMEOUT = -1;
	public final static int COMPLETED = 0;
	public final static int COMPLETED_WITH_ERROR = 1;
	public final static int ERROR = -2;
	public final static int DEFAULT_TIMEOUT = 300000; //5 minutes
	
	public void addToQueue(SSHConsoleService ssh, String cmd) {
		addToQueue(ssh, cmd, DEFAULT_TIMEOUT);
	}
	
	public void addToQueue(SSHConsoleService ssh, String cmd, int timeout) {
		Command command = new Command();
		command.setCommand(cmd);
		command.setSSHConsoleService(ssh);
		command.setTimeout(timeout);
		commandList.add(command);
	}
	
	public void clearQueue() {
		commandList.clear();
	}
	
	public int executeCommands(List<String> outputList) {
		
		List<SSHCommandThread> threadList = new ArrayList<SSHCommandThread>();
		for(Command command: commandList) {
			SSHCommandThread t = new SSHCommandThread(command);
			threadList.add(t);
		}
		
		for(SSHCommandThread t: threadList) {
			t.start();
		}
		
		// wait till commands finish or time out is reached
		boolean running = true;
		boolean error = false;
		boolean allerror = true;
		while(running) {
			
			running = false;
			for(SSHCommandThread t: threadList) {
				System.out.println(t.getHostname() + " - " + t.getCommand() + " - isRunning: " + t.isRunning() );
				if(t.isRunning()) {
					running = true;
				}
				
				System.out.println(t.getHostname() + " - " + t.getCommand() + " - hasError: " + t.isRunning() );
				if(t.hasError()) {
					error = true;
				} else {
					allerror = false;
				}
				
			}
			
			try{
				Thread.sleep(300);
			}catch(InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		if(outputList != null) {
			for(SSHCommandThread t: threadList) {
				StringBuffer sb = new StringBuffer();
				sb.append("*****************************************************\n");
				sb.append("HOSTNAME: " + t.getHostname() + "\n");
				sb.append("COMMAND: " + t.getCommand() + "\n");
				sb.append("-----------------------------------------------------\n");
				sb.append("OUTPUT " + "\n");
				sb.append("-----------------------------------------------------\n");
				if(!t.hasError()) {
					sb.append(t.getOutput());
					System.out.println(t.getOutput());
				}else{
					System.err.println("Error executing command: " + t.getCommand() + " on host: " + t.getHostname() + " \n"
							+ "output: " + t.getOutput());
					sb.append("ATTENTION!!! ERROR EXECUTING COMMAND \n" + t.getOutput() );
				}
				sb.append("-----------------------------------------------------\n");
				outputList.add(sb.toString());
			}
		}
		
		clearQueue();
		
		if(error) {
			return COMPLETED_WITH_ERROR;
		} else if(allerror) {
			return ERROR;
		} else {
			return COMPLETED;
		}
	}

}

class SSHCommandThread extends Thread {
	
	private boolean running = true;
	private SSHConsoleService ssh;
	private String cmd;
	private String output;
	private boolean error = false;
	
	public boolean isRunning() {
		return this.running;
	}
	
	public boolean hasError() {
		return this.error;
	}
	
	public SSHCommandThread(Command command) {
		this.ssh = command.getSSHConsoleService();
		this.cmd = command.getCommand();
	}
	
	public void run() {
		String msg = null;
		this.running = true;
		try {
			System.out.println("running: " + this.running);
			System.out.println("SSHCommandThread - execute cmd: " + cmd);
			msg = ssh.execute(this.cmd);
			this.output = msg;
			System.out.println("\n\n\n\n\n" + this.ssh.getSshSession().getHost() + " cmd: " + this.cmd  + "\n" + this.output + "\n\n\n\n");
		} catch (IOException e) {
			this.error = true;
			e.printStackTrace();
		} catch (InterruptedException e) {
			this.error = true;
			e.printStackTrace();
		}finally{
			this.running = false;
		}
	}
	
	private void setOutput(String output) {
		this.output = output;
	}
	
	public String getOutput() {
		return this.output;
	}
	
	public String getCommand() {
		return this.cmd;
	}
	
	public String getHostname() {
		return this.ssh.getSshSession().getHost();
	}
	
}

class Command {
	
	private SSHConsoleService ssh;
	private String command;
	private int timeout;
	
	public void setSSHConsoleService(SSHConsoleService ssh) {
		this.ssh = ssh;
	}
	
	public void setCommand(String command) {
		this.command = command;
	}
	
	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}
	
	public SSHConsoleService getSSHConsoleService() {
		return this.ssh;
	}
	
	public String getCommand() {
		return this.command;
	}
	
	public int getTimeout() {
		return this.timeout;
	}
}

