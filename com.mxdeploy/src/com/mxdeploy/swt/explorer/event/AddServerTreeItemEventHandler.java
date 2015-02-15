package com.mxdeploy.swt.explorer.event;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.TreeItem;

import com.mxdeploy.api.domain.Server;
import com.mxdeploy.images.Constant;

public class AddServerTreeItemEventHandler {

	public static void execute(TreeItem parent, Server server){ 
		parent.setExpanded(true);
		TreeItem treeItem = new TreeItem(parent, SWT.NULL);
		treeItem.setImage(Constant.IMAGE_SERVER_STOPPED);
		treeItem.setText(server.getName()); 
		treeItem.setData(server);
		treeItem.setExpanded(true);		
		treeItem.setData(Constant.KEY_ITEM_CONNECTED,false);
	}	
}
