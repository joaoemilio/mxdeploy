package com.mxscript.swt.event;

import com.mxdeploy.plugin.event.EventHandler;
import com.mxscript.Task;
import com.mxscript.swt.BeanShellFormComposite;

public class StopSelectedTaskEvent implements EventHandler {

	private BeanShellFormComposite composite;
	
	public StopSelectedTaskEvent(BeanShellFormComposite composite){
		this.composite = composite;
	}
	
	@Override
	public void execute() {
		int selected = composite.getTable().getSelectionIndex();
		
		if ( composite.getTable().getSelectionIndex() == -1 ){
			 return;
		}
		
		Task task = (Task)composite.getTable().getItem(selected).getData();
		task.setColumnStatus("STOPPED");
		task.interrupt();
		task.disconnect();
	}

}
