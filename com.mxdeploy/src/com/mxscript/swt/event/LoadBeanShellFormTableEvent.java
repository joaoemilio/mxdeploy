package com.mxscript.swt.event;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.TableItem;

import com.mxdeploy.api.domain.Database;
import com.mxdeploy.api.domain.Server;
import com.mxdeploy.images.Constant;
import com.mxdeploy.plugin.event.EventHandler;
import com.mxscript.swt.BeanShellFormComposite;

public class LoadBeanShellFormTableEvent implements EventHandler {

	private BeanShellFormComposite beanShellFormComposite;
	
	public LoadBeanShellFormTableEvent(BeanShellFormComposite beanShellFormComposite){
		this.beanShellFormComposite = beanShellFormComposite;
	}
	
	public void execute() {
		
		for(Server server : Database.getInstance().getServerList()){
			TableItem tableItem = new TableItem(beanShellFormComposite.getTable(),SWT.NONE);
			tableItem.setText(server.getHostname());
			tableItem.setImage(Constant.IMAGE_SERVER);
		}
	}
	
	

}
