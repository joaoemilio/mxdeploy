package com.mxdeploy.swt.explorer.event;

import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.widgets.TreeItem;

import com.mxdeploy.api.domain.Server;
import com.mxdeploy.images.Constant;
import com.mxdeploy.swt.MainShell;

public class FocusProcedureServerEventHandler {

	public static void execute(){
		TreeItem treeItemExplorer = MainShell.getControlPanelHelper().getMyProjectTreeItemSelected();
		CTabItem tabItem = null;
		
		if (treeItemExplorer.getData() instanceof Server) {
			if (treeItemExplorer.getData(Constant.KEY_TABITEM_SERVER) != null) {
				tabItem = (CTabItem)treeItemExplorer.getData(Constant.KEY_TABITEM_SERVER);
				CTabFolder folder = (CTabFolder) tabItem.getParent();
				folder.setSelection(tabItem);
			}
		}
	}
}
