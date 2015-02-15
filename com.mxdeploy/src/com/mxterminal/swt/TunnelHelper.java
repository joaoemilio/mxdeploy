package com.mxterminal.swt;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Shell;

import com.mxdeploy.swt.MainShell;
import com.mxterminal.ssh.InteractiveClientSSH;

public class TunnelHelper {
	
	protected TunnelComposite composite = null;
	protected InteractiveClientSSH client = null;
	protected String hostname = null;
	public boolean BindWasRemoved = false;
	
	public TunnelHelper(TunnelComposite composite){
		this.composite = composite;
	}
	
	protected void close(){
		Shell shell = (Shell)composite.getParent();
		shell.close();
		shell.dispose();
	}
	
	public void setSSHInteractiveClient(InteractiveClientSSH client){
		this.client = client;
	}

	public InteractiveClientSSH getSSHInteractiveClient(){
		return client;
	}
	
	public void add(){
		   if(composite.bindPortText.getText().trim().length()==0){
			   MainShell.sendMessage("Source Port is empty !",SWT.ICON_ERROR);
			   composite.bindPortText.setFocus();
			   return;
		   }
		
		   if(composite.destinationText.getText().trim().length()==0){
			   MainShell.sendMessage("Destination is empty !",SWT.ICON_ERROR);
			   composite.destinationText.setFocus();
			   return;
		   }
		   String nextBind = "L:127.0.0.1:"+composite.bindPortText.getText()+" --> "+composite.destinationText.getText()+":"+composite.destPortText.getText();
		   String[] listBind = composite.list.getItems();
		   for(int i=0;listBind.length>i;i++){
			   if(listBind[i].equals(nextBind)){
				  MainShell.sendMessage("Duplicated Bind !",SWT.ICON_ERROR);
				  return;
			   }
		   }
		   composite.bindPortText.setText("");
		   composite.destinationText.setText("");
		   composite.destPortText.setText("");

		   composite.list.add(nextBind);
		   composite.removeBotton.setEnabled(true);
	}
	
	public void loadBindHash(String hostname ){
		this.hostname = hostname;
		List<String> list = TerminalFace.bindHash.get(hostname);
		if(list !=null && !list.isEmpty()){
			for(String bind : list){
				composite.list.add(bind);
			}
			composite.list.select(0);
			composite.addButton.setEnabled(false);
			composite.removeBotton.setEnabled(true);
			composite.okButton.setEnabled(false);
		}
	}
	
	public void save(){
	   String[] listBind = composite.list.getItems();
	   List<String> list = new ArrayList<String>();
	   int localPort = 0;
	   String destination = null;
	   int desPort = 0;
	   boolean save = true;
	   for(int i=0;listBind.length>i;i++){
		   list.add(listBind[i]);
		   String sub = listBind[i].substring(12);
		   int index = sub.indexOf(" ");
		   String var = sub.substring(0,index);
		   localPort = (new Integer(var)).intValue();
		   String alldestination = sub.substring(index+5);
		   index = alldestination.indexOf(":");
		   destination = alldestination.substring(0,index);
		   var = alldestination.substring(index+1,alldestination.length());
		   desPort = (new Integer(var)).intValue();
		   if (getSSHInteractiveClient().isConnected() 
				    && getSSHInteractiveClient().isAuthenticated()){ 
			   try {
				getSSHInteractiveClient().getConnection().newLocalForward("127.0.0.1", localPort, destination, desPort);
				
			   } catch (IOException e) {
				   MainShell.sendMessage("Address already in use !",SWT.ICON_ERROR);
				   save=false;
				   e.printStackTrace();
			   }
		   }
	   }
	   if(save){
	      TerminalFace.bindHash.remove(hostname);
	      TerminalFace.bindHash.put(hostname,list);
	   }
	   close();
	}
	
	public void deleteLocalForward(String bind){
	   int localPort = 0;
	   String sub = bind.substring(12);
	   int index = sub.indexOf(" ");
	   String var = sub.substring(0,index);
	   localPort = (new Integer(var)).intValue();
	   if (getSSHInteractiveClient().isConnected() && getSSHInteractiveClient().isAuthenticated()){ 
		   getSSHInteractiveClient().getConnection().deleteLocalForward("127.0.0.1", localPort);
	   }
	   List<String> list = TerminalFace.bindHash.get(hostname);
	   int count = 0;
	   if(list!=null && !list.isEmpty()){
		   for(String subList : list){
			   sub = subList.substring(12); 
			   index = sub.indexOf(" ");
			   String var2 = sub.substring(0,index);
			   if(var.equals(var2)){
				  break;
			   }
			   count++;
		   }
		   list.remove(count);
	   }
	   TerminalFace.bindHash.put(hostname,list);
	   composite.okButton.setEnabled(true);
	   composite.cancelButton.setEnabled(false);
	}	
	
	public void remove(String bind){
		   String[] listBind = composite.list.getItems();
		   List<String> list = new ArrayList<String>();
		   int localPort = 0;
		   String destination = null;
		   int desPort = 0;
		   boolean save = true;
		   for(int i=0;listBind.length>i;i++){
			   list.add(listBind[i]);
			   String sub = listBind[i].substring(12);
			   int index = sub.indexOf(" ");
			   String var = sub.substring(0,index);
			   localPort = (new Integer(var)).intValue();
			   String alldestination = sub.substring(index+5);
			   index = alldestination.indexOf(":");
			   destination = alldestination.substring(0,index);
			   var = alldestination.substring(index+1,alldestination.length());
			   desPort = (new Integer(var)).intValue();
			   if (getSSHInteractiveClient().isConnected() 
					    && getSSHInteractiveClient().isAuthenticated()){ 
				   try {
					getSSHInteractiveClient().getConnection().newLocalForward("127.0.0.1", localPort, destination, desPort);
				   } catch (IOException e) {
					   MainShell.sendMessage("Address already in use !",SWT.ICON_ERROR);
					   save=false;
					   e.printStackTrace();
				   }
			   }
		   }
		   if(save){
		      TerminalFace.bindHash.remove(hostname);
		      TerminalFace.bindHash.put(hostname,list);
		   }
		   close();
	}		
}
