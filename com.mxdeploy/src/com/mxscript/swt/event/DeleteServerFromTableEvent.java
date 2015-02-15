package com.mxscript.swt.event;

import com.mxdeploy.plugin.event.EventHandler;
import com.mxscript.swt.BeanShellFormComposite;

public class DeleteServerFromTableEvent implements EventHandler {
	
	private BeanShellFormComposite beanShellFormComposite;
	
	public DeleteServerFromTableEvent(BeanShellFormComposite beanShellFormComposite){
		this.beanShellFormComposite = beanShellFormComposite;
	}

	public void execute() {
		
		for(int i=0; i<	beanShellFormComposite.getTable().getSelection().length ; i++){
			
			beanShellFormComposite.getTable().remove(beanShellFormComposite.getTable().getSelectionIndices()[i]);
			                           
		}
		
		
	}

}
