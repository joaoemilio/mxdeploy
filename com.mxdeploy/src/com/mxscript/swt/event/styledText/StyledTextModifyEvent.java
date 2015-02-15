package com.mxscript.swt.event.styledText;

import org.eclipse.swt.events.ModifyEvent;

import com.mxdeploy.plugin.event.EventHandler;
import com.mxscript.swt.BeanShellScriptComposite;

public class StyledTextModifyEvent implements EventHandler{

	private BeanShellScriptComposite composite;
	private ModifyEvent event;
	
	public StyledTextModifyEvent(BeanShellScriptComposite composite, ModifyEvent event){
		this.composite = composite;
		this.event = event; 
	}
	
	public void execute() {
		if( composite.getStyledText1().getLineCount() < composite.getStyledText().getLineCount() ){
			String lines = composite.getStyledText1().getText(); 
			int count = composite.getStyledText1().getLineCount()+1;
			for(int i=count; i <= composite.getStyledText().getLineCount(); i++ ){
				if(count<10){
					lines = lines+"\r\n0"+String.valueOf(count);
					//composite.getStyledText1().append("\r\n0"+String.valueOf(count));
				} else {
					lines = lines+"\r\n"+String.valueOf(count);
					//composite.getStyledText1().append("\r\n"+String.valueOf(count));
				}
			}
			composite.getStyledText1().setText(lines);
		}
		composite.getSaveToolItem().setEnabled(true);		
	}

}
