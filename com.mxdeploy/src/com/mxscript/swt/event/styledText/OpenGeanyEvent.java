package com.mxscript.swt.event.styledText;

import java.io.IOException;
import java.io.InputStream;

import org.apache.log4j.Logger;

import xml.module;

import com.mxdeploy.api.domain.Database;
import com.mxdeploy.plugin.event.EventHandler;
import com.mxdeploy.swt.MainShell;

public class OpenGeanyEvent implements EventHandler{

	static Logger logger = Logger.getLogger(OpenGeanyEvent.class);
	
	@Override
	public void execute() {
		String filePath = null;
		if ( MainShell.getCTopTabFolder().getSelection().getData()!=null && MainShell.getCTopTabFolder().getSelection().getData() instanceof module){
			module __module__ = (module)MainShell.getCTopTabFolder().getSelection().getData();
			filePath = __module__.getFullpath();
		} else {
			String scriptName = MainShell.getCTopTabFolder().getSelection().getText();
			filePath = Database.WORKSPACE_PATH+"/"+Database.WORKSPACE_NAME+"/beanshell/bsh/"+scriptName;
		}
		
		String scriptGeany = "start_geany.sh";
		if ( !System.getProperty("os.name").contains("Linux")){
			scriptGeany = "start_geany.bat";
		}		
		String WORKSPACE = System.getenv("MXD_WORKSPACE");
		 
		String geanyPath = WORKSPACE+"/"+Database.WORKSPACE_NAME+"/"+scriptGeany;
		
		String cmd = geanyPath+" "+filePath;
		logger.debug("[Geany starts] - cmd = "+cmd);
		Process process;
		try {
			process = Runtime.getRuntime().exec(cmd);
			
			InputStream in = process.getInputStream();
			StringBuilder strb = new StringBuilder();
			int ch;
			while((ch = in.read()) != -1) {
				String valor = String.valueOf((char)ch);
				strb.append( valor );
			}					
		} catch (IOException e) {
			e.printStackTrace();
		} 
		
	}

}
