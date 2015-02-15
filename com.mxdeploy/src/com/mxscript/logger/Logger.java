package com.mxscript.logger;

import org.eclipse.swt.widgets.Display;

import com.mxscript.swt.BeanShellFormComposite;

public class Logger {
	
	private BeanShellFormComposite composite;
	private String bshName;
	
	public Logger(BeanShellFormComposite composite, String bshName){
		this.composite = composite;
		this.bshName = bshName;
	}
	
	public Logger(BeanShellFormComposite composite, String bshName, String serverName ){
		this.composite = composite;
		this.bshName = bshName;
	}	

	public void error(final String valeu){
		Display.getDefault().syncExec(new Runnable(){
			public void run(){ 
				String log = " ERROR - "+valeu;
				//String txt = composite.getStyledText().getText()+"\n "+log;	
				//composite.getStyledText().setText(txt);
				composite.getStyledText().append("\n "+log);
			}
		});		
	}
	
	public void warn(final String valeu){
		Display.getDefault().syncExec(new Runnable(){
			public void run(){ 
				String logLevel = composite.getToolBarConsoleComposite().getLogLevelCombo().getText();
				
				if( logLevel.equals("DEBUG") || logLevel.equals("INFO") || logLevel.equals("WARN") ){
					String log = " WARN - "+valeu;
//					String txt = composite.getStyledText().getText()+"\n "+log;	
//					composite.getStyledText().setText(txt);
					composite.getStyledText().append("\n "+log);
				}
			}
		});		
	}
	
	public void info(final String valeu){
		Display.getDefault().syncExec(new Runnable(){
			public void run(){ 
				String logLevel = composite.getToolBarConsoleComposite().getLogLevelCombo().getText();
				
				if( logLevel.equals("DEBUG") || logLevel.equals("INFO") ){
					String log = " INFO - "+valeu;
//					String txt = composite.getStyledText().getText()+"\n "+log;	
//					composite.getStyledText().setText(txt);
					composite.getStyledText().append("\n "+log);
				}
			}
		});		
	}	
	
	public void debug(final String valeu){
		Display.getDefault().syncExec(new Runnable(){
			public void run(){ 
				String logLevel = composite.getToolBarConsoleComposite().getLogLevelCombo().getText();
				if(logLevel.equals("DEBUG")){
					String log = " DEBUG - "+valeu;
//					String txt = composite.getStyledText().getText()+"\n "+log;	
//					composite.getStyledText().setText(txt);
					composite.getStyledText().append("\n "+log);
				}
			}
		});		 
	}
	
	public void log(final String valeu){
		Display.getDefault().syncExec(new Runnable(){
			public void run(){ 
				String log = " LOG - "+valeu;
//				String txt = composite.getStyledText().getText()+"\n "+log;	
//				composite.getStyledText().setText(txt);
				composite.getStyledText().append("\n "+log);				
			}
		});		
	}
		
	
}
