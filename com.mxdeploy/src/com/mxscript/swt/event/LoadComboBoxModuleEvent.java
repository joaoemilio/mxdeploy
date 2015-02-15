package com.mxscript.swt.event;

import org.apache.log4j.Logger;

import xml.modulegroup;
import xml.modulegroups;

import com.mxdeploy.api.domain.Database;
import com.mxdeploy.plugin.event.EventHandler;
import com.mxscript.swt.CTabItemModuleViewer;

public class LoadComboBoxModuleEvent implements EventHandler {

	static Logger logger = Logger.getLogger(LoadModuleViewerEvent.class);
	private CTabItemModuleViewer cTabItemModuleViewer = null;
	
	public LoadComboBoxModuleEvent(CTabItemModuleViewer cTabItemModuleViewer){
		this.cTabItemModuleViewer = cTabItemModuleViewer;
	}
	
	public void execute() {
		(new LoadModuleViewerEvent()).execute();
		cTabItemModuleViewer.getComboModule().removeAll();
		
		modulegroups _modulegroups_ = Database.getInstance().get_modulegroups();
		if ( _modulegroups_!=null &&_modulegroups_.getList().size()>0){
			for ( modulegroup _modulegroup_ : _modulegroups_.getList() ){
				cTabItemModuleViewer.getComboModule().add(_modulegroup_.getName());
				cTabItemModuleViewer.getComboModule().setData(_modulegroup_.getName(), _modulegroup_);
			}
			cTabItemModuleViewer.getComboModule().select(0); 
			
			LoadTreeModuleEvent event = new LoadTreeModuleEvent(cTabItemModuleViewer);
			event.execute();
		}
	}


}
