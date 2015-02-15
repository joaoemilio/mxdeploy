package com.mxdeploy.swt.explorer.popup.event;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.widgets.TreeItem;

import com.mxdeploy.api.domain.Database;
import com.mxdeploy.api.domain.Project;
import com.mxdeploy.api.domain.Server;
import com.mxdeploy.images.Constant;
import com.mxdeploy.plugin.event.EventHandler;
import com.mxdeploy.swt.MainShell;
import com.mxdeploy.swt.explorer.event.FocusProcedureServerEventHandler;
import com.mxdeploy.swt.explorer.helper.ControlPanelHelper;
import com.mxdeploy.swt.menu.event.WindownApplicationEventHandler;
import com.mxdeploy.swt.workbook.WorkbookTabItemComposite;
import com.mxterminal.swt.util.SWTUtils;
import com.mxterminal.swt.view.CTopTabFolder;
import com.mxterminal.swt.view.CentralTabItemFactory;
import com.wds.bean.WorkBook;

public class ConnectServerEventHandler implements EventHandler {
 
	public void execute() {

		Database.usePassSaved = false;
		ControlPanelHelper workingSetHelper = MainShell.getControlPanelHelper();

		TreeItem treeItemExplorer = workingSetHelper.getMyProjectTreeItemSelected();
		if (treeItemExplorer == null) {
			return;
		}
		CTabItem tabItem = null;

		if (treeItemExplorer.getData() instanceof Server) {
			if (treeItemExplorer.getData(Constant.KEY_TABITEM_SERVER) == null) {

				Server server = (Server) treeItemExplorer.getData();
				Project project = (Project) treeItemExplorer.getParentItem().getData();

				CTopTabFolder tabFolderServer = MainShell.getCTopTabFolder();
				tabItem = CentralTabItemFactory.createTabItemServer(project, server, tabFolderServer);

				treeItemExplorer.setData(Constant.KEY_TABITEM_SERVER, tabItem);
				tabItem.setData(Constant.KEY_TREEITEM_EXPLORER,	treeItemExplorer);
				
				MainShell.getToolBarViewHelper().configureToolBarView();
				
			} else {
				FocusProcedureServerEventHandler.execute();
			}
		} else if ( treeItemExplorer.getData() instanceof WorkBook ) {
			CTabFolder cTabFolder = MainShell.getCTopTabFolder();
			WorkbookTabItemComposite workbookTabItemComposite = new WorkbookTabItemComposite(cTabFolder,SWT.NONE);

			SWTUtils.createGridLayoutNoMargins(workbookTabItemComposite);
			SWTUtils.createGridDataMaximized(workbookTabItemComposite);		

			Project project = (Project)treeItemExplorer.getParentItem().getData();
			
			tabItem = new CTabItem(cTabFolder,SWT.CLOSE);
			tabItem.setText(project.getAlias()+" WorkBook"); 	
			tabItem.setData(treeItemExplorer.getData());

			tabItem.setControl(workbookTabItemComposite);
			tabItem.setImage(Constant.IMAGE_WORKBOOK);
			
			cTabFolder.setSelection(tabItem);
			
			(new WindownApplicationEventHandler()).openMaximized();
			WorkBook workbook = (WorkBook)treeItemExplorer.getData();
			workbookTabItemComposite.setProject(project);
			workbookTabItemComposite.loadForm();			
		}
	}

}
