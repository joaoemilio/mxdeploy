package com.mxdeploy.swt.command;

import java.util.List;

import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TableItem;

import com.mxdeploy.swt.MainShell;
import com.mxterminal.swt.view.helper.ServerHelper;

public class CommandItemHelper {

	private CommandItemComposite composite = null;
	
	private ServerHelper serverHelper = null;
	
	private CommandByServerHelper commandByServerHelper = null;
	
	public CommandItemHelper(CommandItemComposite composite) {
	   this.composite = composite;
	   serverHelper = MainShell.getCTopTabFolder().getServerHelper();
	}
	
	protected void setCommandByServerHelper(CommandByServerHelper commandByServerHelper){
		this.commandByServerHelper = commandByServerHelper;
	}
	
	protected void close(){
		Shell shell = (Shell)composite.getParent();
		shell.close();
		shell.dispose();
	}
	
	protected void addCommandItem(){
		String text = composite.commandSSHStyleText.getText();
		commandByServerHelper.removeAllCommandItemInTable();
		
		String[] lines = text.split("\n");
		
		for(String line: lines) {
			line = line.replace("\r", "");
			commandByServerHelper.addCommandItemInTable(line);
		}
		
		/*
		byte[] b = text.getBytes();
		boolean char10 = false;
		boolean char13 = false;
		int count = 0;
		byte[] bline = new byte[b.length]; 
		for(int i=0;b.length>i;i++){
			if(b[i]==13){
			   char10 = true;
			} else if(char10 && b[i]==10){
			   char13 = true;	
			} else {
			   char10 = false;
			}
			
			if(char10 && char13){
				count--;
				String line = new String(bline,0,count);		
				if(line.trim().length()>1){
				   commandByServerHelper.addCommandItemInTable(line);
				}
				char10 = false;
				char13 = false;
				count = 0;
				bline = new byte[b.length];
			} else {
				bline[count] = b[i];
				count++;				
			}
		}
		
		if(count>0){
		   String line = new String(bline,0,count);				
			if(line.trim().length()>1){
			   commandByServerHelper.addCommandItemInTable(line);
			}
		}
		*/
	}
	
	protected void fillStyleText(){
		List<TableItem> list = commandByServerHelper.getTableItems();
		if(list!=null && !list.isEmpty()){
			for(TableItem tableItem : list){
				composite.commandSSHStyleText.append(tableItem.getText()+"\r\n");
			}
		}
	}
	
//	public void checkPermission(){
//		User user = MainShell.getMainHelper().getUser(); 
//		if( (user==null)||(user.getIdUserGroup().equals(User.GROUP_USER))||(user.getIdUserGroup().equals(User.GROUP_EDITOR)) ){
//			composite.saveButton.setEnabled(false);
//		} else {
//			composite.saveButton.setEnabled(true);
//		}
//	}	

	
	
}
