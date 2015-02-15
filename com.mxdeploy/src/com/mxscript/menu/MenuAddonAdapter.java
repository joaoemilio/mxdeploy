package com.mxscript.menu;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;

import com.mxdeploy.api.domain.Database;
import com.mxdeploy.images.Constant;
import com.mxdeploy.plugin.menu.MenuAddon;
import com.mxdeploy.plugin.menu.SubMenu;
import com.mxscript.menu.event.LoadBSHFilesEventHandler;
import com.mxscript.menu.event.OpenDiscoverEventHandler;

public class MenuAddonAdapter implements MenuAddon {

	public SubMenu addOn() {
		SubMenu subMenu = new SubMenu();
		subMenu.setText("BeanShell");
		subMenu.setSWT_Style(SWT.CASCADE); 
		//subMenu.setEventHandler(new LoadBSHFilesEventHandler(subMenu)); 
		
//		SubMenu newScriptSubMenu = new SubMenu();
//		newScriptSubMenu.setText("New Script");
//		newScriptSubMenu.setSWT_Style(SWT.PUSH);
//		newScriptSubMenu.setEventHandler(new NewScriptEventHandler());
//		subMenu.addSubMenu(newScriptSubMenu);
//		
//		SubMenu scriptSubMenu = new SubMenu();
//		scriptSubMenu.setText("Scripts");
//		scriptSubMenu.setSWT_Style(SWT.CASCADE); 
//		scriptSubMenu.setCreateSeparatorBefore(true);
//		subMenu.addSubMenu(scriptSubMenu); 
		List<String> fileList = loadFileName();
		if(fileList != null) {
			for(String fileName : loadFileName()){
				SubMenu beanSubMenu = new SubMenu();
				beanSubMenu.setText(fileName);		
				beanSubMenu.setEventHandler(new OpenDiscoverEventHandler(fileName));
				beanSubMenu.setSWT_Style(SWT.PUSH); 
				beanSubMenu.setImage(Constant.IMAGE_JAVABEAN);	 	
			
				subMenu.addSubMenu(beanSubMenu);
			}
		
		}
		   
		return subMenu;
	}
	
	public List<String> loadFileName(){
		
		List<String> serverNameList = new ArrayList<String>();
		
		File file = new File(Database.WORKSPACE_PATH+"/"+Database.WORKSPACE_NAME+"/beanshell/bsh");
		File[] files = file.listFiles();
		if(files == null) {
			return null;
		}
		for(int i=0; i < files.length; i++){
			if(!files[i].isDirectory()){
				if(files[i].getName().endsWith(".bsh"))
					serverNameList.add(files[i].getName());
			}
		}
		
		return serverNameList;
	}	

}
