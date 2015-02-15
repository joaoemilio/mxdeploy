package com.mxscript.swt.event;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.TableItem;

import com.mxdeploy.api.domain.Database;
import com.mxdeploy.api.domain.Server;
import com.mxdeploy.images.Constant;
import com.mxdeploy.plugin.event.EventHandler;
import com.mxscript.swt.BeanShellFormComposite;


public class AddServerToTableEvent implements EventHandler {

	BeanShellFormComposite beanShellFormComposite;
	
	public AddServerToTableEvent(BeanShellFormComposite beanShellFormComposite){
		this.beanShellFormComposite = beanShellFormComposite;
	}
	
	public void execute() {
		if(beanShellFormComposite.getServerCombo().getText().trim().length()==0){
			return;
		}
		
		if(beanShellFormComposite.getServerCombo().getText().trim().equals("*")){
			(new LoadBeanShellFormTableEvent(beanShellFormComposite)).execute();
		} else if(beanShellFormComposite.getServerCombo().getText().contains("*")){
			String prefix = beanShellFormComposite.getServerCombo().getText().replace("*", "");
			for(Server server : Database.getInstance().getServerList()){
				if(server.getHostname().contains(prefix)){
					TableItem tableItem = new TableItem(beanShellFormComposite.getTable(),SWT.NONE);
					tableItem.setText(server.getHostname());
					tableItem.setImage(Constant.IMAGE_SERVER);
				}
			}
		} else {
		  TableItem item = new TableItem(beanShellFormComposite.getTable(),SWT.NONE);
		  item.setText(beanShellFormComposite.getServerCombo().getText());
		}
		beanShellFormComposite.getToolBarConsoleComposite().getRunToolItem().setEnabled(true);
		beanShellFormComposite.getDeleteServerToolItem().setEnabled(true);
	}


	

}
