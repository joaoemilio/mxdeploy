package com.mxscript;

/* -*-mode:java; c-basic-offset:2; indent-tabs-mode:nil -*- */

import com.jcraft.jsch.*;
import java.io.*;
import java.util.*;

public class RemoteShell{
	static boolean debugMode=false;
	public static String commandPrompt="%%myprompt%%";
	public static void debug(String msg){
		if (debugMode){
			System.out.println("DEBUG: "+msg);
		}
	}
	public static void main(String[] arg){
		try{
			JSch jsch=new JSch();  

			String host=null;
			String commandFile=null;
			boolean verboseMode=false;
			boolean inputError=false;
			if(arg.length==2){
				host=arg[0];
				commandFile=arg[1];
			}else{
				if(arg.length==3){
					host=arg[1];
					commandFile=arg[2];
					if(arg[0].equals("-v") || arg[0].equals("-d")){
						if(arg[0].equals("-v")){ verboseMode=true; }
						if(arg[0].equals("-d")){ debugMode=true; }
					}else{
						inputError=true;
					}
				}else{
					inputError=true;
				}
			}
			if (inputError){
				System.out.println("You must enter the parameters: [-v|-d] hostname commands_file");
				System.out.println("-v is an optional parameter that triggers verbose mode showing all console output");
				System.out.println("-d is an optional parameter that triggers debug mode");
				System.out.println("Username and password will come from RemoteExec.properties");
				System.exit(1);
			}
			MyUserInfo ui=new MyUserInfo();
			String user=ui.getUsername();

			Session session=jsch.getSession(user, host, 22);
			session.setTimeout(120 * 1000);

			// username and password will be given via UserInfo interface.
			session.setUserInfo((UserInfo)ui);
			session.connect();

			ChannelShell channel= (ChannelShell)session.openChannel("shell");
			
			//Defaults are:
			//tcol=80;
			//trow=24;
		    //twp=640;
			//thp=480;
			int tcol=1023;
			int trow=24;
			int twp=640;
			int thp=480;
			channel.setPtySize(tcol, trow, twp, thp);

			PipedInputStream pins = new PipedInputStream();
			channel.setInputStream(pins);
			channel.setOutputStream(System.out);

			InputStream in=channel.getInputStream();

			PipedOutputStream pouts = new PipedOutputStream(pins);
			IOThread outThread = new IOThread(pouts,commandFile);

			channel.connect();

			outThread.start();
			boolean commandSent = false;
			boolean outputResult = false;
			boolean resultBack = false;
			int commandLength=0;
			byte[] tmp=new byte[1024];
			StringBuffer commandOutput= new StringBuffer();
			while(true){
				String tmpPrompt="";
				debug("In the BIG loop");
				while(in.available()>0){
					int i=in.read(tmp, 0, 1024);
					//System.out.println("In the loop - "+i );
					if(i<0){
						break;
					}
					String result = new String(tmp, 0, i);
					if(verboseMode){
						System.out.print(result);
					}
					commandOutput.append(result);
				}
				if(commandOutput.lastIndexOf("assword:") >= 0 && commandOutput.lastIndexOf("assword:") >= (commandOutput.length()-10)){ 
					System.out.println("\n\nERROR: Aborting due to missing sudo\n");
					channel.disconnect();
					session.disconnect();
					System.exit(1);
				}
				if(commandOutput.lastIndexOf(commandPrompt) == (commandOutput.length() - commandPrompt.length())){
					tmpPrompt=commandPrompt;
					resultBack = true;
					debug("GOT A ramdom prompt: \""+tmpPrompt+"\"");
				}
				if( commandOutput.lastIndexOf("> ") == (commandOutput.length() - 2) ){
					tmpPrompt="> ";
					resultBack = true;
					debug("GOT A Prompt is \""+tmpPrompt+"\"");
				}
				if( commandOutput.lastIndexOf("$ ") == (commandOutput.length() - 2) ){
					tmpPrompt="$ ";
					resultBack = true;
					debug("GOT A Prompt is \""+tmpPrompt+"\"");
				}
				if( commandOutput.lastIndexOf("# ") == (commandOutput.length() - 2) ){
					tmpPrompt="# ";
					resultBack = true;
					debug("GOT A Prompt is \""+tmpPrompt+"\"");
				}
				if( commandOutput.lastIndexOf("% ") == (commandOutput.length() - 2) ){
					tmpPrompt="% ";
					resultBack = true;
					debug("GOT A Prompt is \""+tmpPrompt+"\"");
				}
				if(channel.isClosed()){
					int exitStatus = channel.getExitStatus();
					if(exitStatus!=0){
						System.out.println("********** Error ********* exit-status: "+exitStatus);
					}
					break;
				}
				if(resultBack == false){// && commandSent==true){
					debug("Waiting for prompt");
					try{Thread.sleep(100);}catch(Exception ee){}
					continue;
				}
				
				//System.out.println("Finished input!! command length="+commandLength);
				//System.out.println("commandLength is "+commandLength+" / Prompt is \""+tmpPrompt+"\" / CommandOutput is #####"+commandOutput+"#####");
				if(!verboseMode){
					if(commandSent == true  && resultBack == true){
						if(outputResult){
							if(commandOutput.length()>0){
									int promptIndex=0;
									promptIndex=commandOutput.lastIndexOf(tmpPrompt);
									debug("commandOutput: "+commandOutput+" :endcommandOutput");
									if (promptIndex > 0){
											//System.out.println("Comparison: "+commandOutput.length()+" > "+commandLength+" :"+ (commandOutput.length() > commandLength));
											
											//System.out.println("Commandout: len:"+commandOutput.length()+" substr start:"+(commandLength+1)+" substr end("+tmpPrompt+"):"+promptIndex);
											//System.out.println("commandOutput: #####"+commandOutput+"##### :endcommandOutput"); 
											String commandOut="";
											if ( (commandLength+1) > 0 && (promptIndex-1) >0 ) {
												debug("Start: "+(commandLength+1)+" LEN: "+(promptIndex-1)+" commandOutputLen: "+commandOutput.length());
												commandOut=commandOutput.substring((commandLength+1),(promptIndex-1));
											}
											if(! commandOut.equals("\n")){
												System.out.print(commandOut);
											}
									}
							}
						}
					}
				}
				commandOutput.delete(0,commandOutput.length());

				try{Thread.sleep(100);}catch(Exception ee){}
				if(resultBack == true || commandSent==false){
					if (outThread.commandIndex > 0){
						outputResult = true;
					}
					commandSent = true;
					debug("Sending next command");
					commandLength=outThread.sendNextCommand();
					resultBack = false;
				}
			}
			System.out.println("");
			channel.disconnect();
			session.disconnect();
		}
		catch(Exception e){
			System.out.println(e);
		}
	}

	public static class IOThread extends Thread {
		OutputStream out_ = null;
		public IOThread (OutputStream out, String commandFile) { out_ = out; commandFile_ = commandFile; }
		public String [] commandArray = null;
		public int commandIndex = 0;
		int commandLen = 0;
		String commandFile_ = null;
		public void run() {
			try{
				String [] fileLines = readLines(commandFile_);
				String newPrompt="export PS1=\""+commandPrompt+"\"";
				int lineCount = fileLines.length;
				commandArray = new String [(lineCount+1)];
				commandArray[0]=newPrompt;
				for (int i=0; i< lineCount; i++){ commandArray[i+1]=fileLines[i]; }
				commandLen=commandArray.length;
				//for (int i=0; i< commandLen; i++){ System.out.println("Command: "+commandArray[i]); }
			}catch (IOException e){
				System.out.println(e);
			}
			//try{Thread.sleep(8000);}catch(Exception ee){}
		}
		
		public synchronized void writeCommand(String command){
			PrintWriter pWriter = new PrintWriter(out_, true); 
			//System.out.println("Sending command:"+command.trim()+":endCommand:"+command.length());
			pWriter.println(command);
		}
		
		// return value will be command length to be removed from the output
		public synchronized int sendNextCommand(){
			if(commandIndex < commandLen){
				debug("Command to be sent:"+commandArray[commandIndex].trim());
				String commandStr=commandArray[commandIndex].trim();
				int cmdStrLen=commandStr.length();
				writeCommand(commandStr);
				commandIndex++;
				return cmdStrLen;
			}else{
				String commandStr="exit";
				int cmdStrLen=commandStr.length();
				writeCommand(commandStr);
				return cmdStrLen;
			}
		}


		public String[] readLines(String filename) throws IOException {
			FileReader fileReader = new FileReader(filename);
			BufferedReader bufferedReader = new BufferedReader(fileReader);
			//List<String> lines = new ArrayList<String>();
			List lines = new ArrayList();
			String line = null;
			while ((line = bufferedReader.readLine()) != null) {
				lines.add(line);
			}
			bufferedReader.close();
			//System.out.println("done reading file");
			return (String[])lines.toArray(new String[lines.size()]);
		}

	}


	public static class MyUserInfo implements UserInfo{
		String passwd;
		String username;

		public void loadProps(String filename){ 
			ResourceBundle props = ResourceBundle.getBundle(filename);
			username = props.getString("USER");
			passwd = props.getString("PASS");
			//System.out.println("Loading Properties file:");
			//System.out.println("USER:" + username );
			//System.out.println("PASS:" + passwd);

		}
		public MyUserInfo(){
			loadProps("RemoteExec");
		}
		public boolean promptYesNo(String str){
			return true;
		}

		public void setPassword(String passwd){
			this.passwd=passwd;
		}
		public String getPassword(){ return passwd; }
		public String getUsername(){return username;}
		public String getPassphrase(){ return null; }
		public boolean promptPassphrase(String message){ return true; }
		public boolean promptPassword(String message){ return true; }
		
		public void showMessage(String message){ }
	}
}


