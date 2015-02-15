package com.mxscript.menu.event;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;

import com.mxdeploy.api.domain.Database;
import com.mxdeploy.images.Constant;
import com.mxdeploy.plugin.event.EventHandler;
import com.mxdeploy.plugin.menu.SubMenu;

public class LoadBSHFilesEventHandler  implements EventHandler {
	
	SubMenu parent;
	
	public LoadBSHFilesEventHandler(SubMenu parent){
		this.parent = parent;
	}

	public void execute() {
		 
		for(String fileName : loadFileName()){
			SubMenu beanSubMenu = new SubMenu();
			beanSubMenu.setText(fileName);		
			beanSubMenu.setEventHandler(new OpenDiscoverEventHandler(fileName));
			beanSubMenu.setSWT_Style(SWT.PUSH); 
			beanSubMenu.setImage(Constant.IMAGE_JAVABEAN);	 	
			
			parent.addSubMenu(beanSubMenu);
		}		

	}
	
	public List<String> loadFileName(){
		
		List<String> serverNameList = new ArrayList<String>();
		
		File file = new File(Database.WORKSPACE_PATH+"/"+Database.WORKSPACE_NAME+"/beanshell/bsh");
		File[] files = file.listFiles();
		for(int i=0; i < files.length; i++){
			if(!files[i].isDirectory()){
				if(files[i].getName().endsWith(".bsh"))
					serverNameList.add(files[i].getName());
			}
		}
		
		return serverNameList;
	}	

}
