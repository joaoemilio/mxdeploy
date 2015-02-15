package com.mxscript.swt.event.styledText;

import java.io.FileWriter;
import java.io.IOException;

import xml.module;

import com.mxdeploy.api.domain.Database;
import com.mxdeploy.plugin.event.EventHandler;
import com.mxdeploy.swt.MainShell;
import com.mxscript.swt.BeanShellScriptComposite;

public class SaveBeanShellScriptEvent implements EventHandler{

	private BeanShellScriptComposite composite;
	
	public SaveBeanShellScriptEvent(BeanShellScriptComposite composite){
		this.composite = composite;
	}
	
	public void execute() {
		
		String filePath = null;
		if ( MainShell.getCTopTabFolder().getSelection().getData()!=null && MainShell.getCTopTabFolder().getSelection().getData() instanceof module){
			module __module__ = (module)MainShell.getCTopTabFolder().getSelection().getData();
			filePath = __module__.getFullpath();
		} else {
			String scriptName = MainShell.getCTopTabFolder().getSelection().getText();
			filePath = Database.WORKSPACE_PATH+"/"+Database.WORKSPACE_NAME+"/beanshell/bsh/"+scriptName;
		}
		
//		String scriptName = MainShell.getCTopTabFolder().getSelection().getText();
//		String filePath = Database.WORKSPACE_PATH+"/account/"+Database.ACCOUNT+"/beanshell/bsh/"+scriptName;		
		
		FileWriter write;
		try {
			write = new FileWriter(filePath);
			write.write( composite.getStyledText().getText() );
			write.close();
			composite.getSaveToolItem().setEnabled(false);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
