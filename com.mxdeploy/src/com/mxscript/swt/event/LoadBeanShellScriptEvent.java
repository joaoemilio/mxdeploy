package com.mxscript.swt.event;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import com.mxdeploy.api.domain.Database;
import com.mxdeploy.plugin.event.EventHandler;
import com.mxscript.swt.BeanShellScriptComposite;

public class LoadBeanShellScriptEvent implements EventHandler {

	private BeanShellScriptComposite composite;
	
	public LoadBeanShellScriptEvent(BeanShellScriptComposite composite){
		this.composite = composite;
	}
	
	public void execute() {
		String filePath = Database.getBeanShellScriptPath();
		
		FileReader input = null;
		String contents = "";
		String lines = "";
		try {
			input = new FileReader(filePath);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} 
		 
		BufferedReader bufRead = new BufferedReader(input);
		String content;
		try {
			int count = 1;
			while ( ( content = bufRead.readLine())!=null ) {
				if(contents.trim().length()==0){
					contents = content;
					lines = "0"+String.valueOf(count);
					//composite.getStyledText().append("\r\n"+ content);
				} else {
					contents = contents +"\r\n"+ content;
					//composite.getStyledText().append("\r\n"+ content);
					if(count<10){
						lines = lines+"\r\n0"+String.valueOf(count);
						//composite.getStyledText1().append("\r\n0"+String.valueOf(count));
					} else {
						lines = lines+"\r\n"+String.valueOf(count);
						//composite.getStyledText1().append("\r\n"+String.valueOf(count));
					}
					
				}
				count++;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		try { 
			bufRead.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		composite.getStyledText().setText(contents);
		composite.getStyledText1().setText(lines);
		composite.getSaveToolItem().setEnabled(false);
	}
	

}
