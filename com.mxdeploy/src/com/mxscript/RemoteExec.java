package com.mxscript;

/* -*-mode:java; c-basic-offset:2; indent-tabs-mode:nil -*- */
import com.jcraft.jsch.*;
import java.io.*;
import java.util.ResourceBundle;

public class RemoteExec{
  public static void main(String[] arg){
    try{
      JSch jsch=new JSch();  

      String host=null;
      if(arg.length==2){
        host=arg[0];
      }
      else{
        System.out.println("You must enter the parameters: hostname command");
        System.out.println("Username and password will come from RemoteExec.properties");
	System.exit(1);
      }
      MyUserInfo ui=new MyUserInfo();
      String user=ui.getUsername();
      String command=arg[1];
 
      Session session=jsch.getSession(user, host, 22);
      session.setTimeout(120 * 1000);
 
      // username and password will be given via UserInfo interface.
      session.setUserInfo((UserInfo)ui);
      session.connect();

      Channel channel=session.openChannel("exec");
      ((ChannelExec)channel).setCommand(command);

      // X Forwarding
      // channel.setXForwarding(true);

      //channel.setInputStream(System.in);
      channel.setInputStream(null);

      channel.setOutputStream(System.out);

      //FileOutputStream fos=new FileOutputStream("/tmp/stderr");
      //((ChannelExec)channel).setErrStream(fos);
      ((ChannelExec)channel).setErrStream(System.err);

      InputStream in=channel.getInputStream();

      channel.connect();

      byte[] tmp=new byte[1024];
      while(true){
        while(in.available()>0){
          int i=in.read(tmp, 0, 1024);
          if(i<0)break;
          System.out.print(new String(tmp, 0, i));
        }
        if(channel.isClosed()){
          int exitStatus = channel.getExitStatus();
          if(exitStatus!=0){
              System.out.println("********** Error ********* exit-status: "+exitStatus);
          }
          break;
        }
        try{Thread.sleep(1000);}catch(Exception ee){}
      }
      channel.disconnect();
      session.disconnect();
    }
    catch(Exception e){
      System.out.println(e);
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
    public boolean promptPassword(String message){
        return true;
    }
    public void showMessage(String message){
    }
  }
}

