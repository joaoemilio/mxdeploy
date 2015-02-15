package com.mxdeploy.swt.explorer.popup;

import java.io.File;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.TreeItem;

import com.mxdeploy.api.domain.Database;
import com.mxdeploy.api.domain.Project;
import com.mxdeploy.images.Constant;
import com.mxdeploy.plugin.explorer.ChildItemMenuExplorerPlugin;
import com.mxdeploy.plugin.menu.MenuBT;
import com.mxdeploy.plugin.menu.SubMenu;
import com.mxdeploy.swt.MainShell;
import com.mxdeploy.swt.explorer.popup.event.AddServerInProjectEventHandler;
import com.mxdeploy.swt.explorer.popup.event.CloseAllProjectEventHandler;
import com.mxdeploy.swt.explorer.popup.event.CloseProjectEventHandler;
import com.mxdeploy.swt.explorer.popup.event.ConnectServerEventHandler;
import com.mxdeploy.swt.explorer.popup.event.EditProjectEventHandler;
import com.mxdeploy.swt.explorer.popup.event.EditServerEventHandler;
import com.mxdeploy.swt.explorer.popup.event.NewProjectEventHandler;
import com.mxdeploy.swt.explorer.popup.event.NewServerEventHandler;
import com.mxdeploy.swt.explorer.popup.event.NewWorkbookEventHandler;
import com.mxdeploy.swt.explorer.popup.event.RemoveProjectEventHandler;
import com.mxdeploy.swt.explorer.popup.event.RemoveServerEventHandler;
import com.mxdeploy.swt.explorer.popup.event.RemoveWorkbookEventHandler;

public class PopupFactory {  
	
	private static MenuBT menuBt = null;
	private static boolean newProjectCreated = false;
	
	public static MenuBT createPopup(){
		menuBt = new MenuBT();
		
		SubMenu newProjectSubMenu = new SubMenu();
		newProjectSubMenu.setText("New Project");
		newProjectSubMenu.setImage(Constant.IMAGE_APPLICATION);
		newProjectSubMenu.setEventHandler(new NewProjectEventHandler());
		
		menuBt.addSubMenu(newProjectSubMenu);	
		
		newProjectCreated = true;
		
		return menuBt;
	}
	
	public static MenuBT createPopupProject(){
		menuBt = createPopup();
		
		SubMenu editProjectSubMenu = new SubMenu();
		editProjectSubMenu.setText("Edit Project");
		editProjectSubMenu.setImage(Constant.IMAGE_APPLICATION);
		editProjectSubMenu.setEventHandler(new EditProjectEventHandler());
		editProjectSubMenu.setSWT_Style(SWT.NONE);
		
		menuBt.addSubMenu(editProjectSubMenu);	
		
		SubMenu removerProjectSubMenu = new SubMenu();
		removerProjectSubMenu.setText("Remove Project");
		removerProjectSubMenu.setImage(Constant.IMAGE_REMOVE_COMMAND);
		removerProjectSubMenu.setEventHandler(new RemoveProjectEventHandler());
		removerProjectSubMenu.setSWT_Style(SWT.NONE);

        if(MainShell.getCTopTabFolder().getItemCount()>0){
           removerProjectSubMenu.setEnabled(false);
        }		
		
		menuBt.addSubMenu(removerProjectSubMenu);	
		
		SubMenu addServerInProjectSubMenu = new SubMenu();
		addServerInProjectSubMenu.setText("Add Server in Project");
		addServerInProjectSubMenu.setImage(Constant.IMAGE_ADD_SERVER_IN_PROJECT);
		addServerInProjectSubMenu.setEventHandler(new AddServerInProjectEventHandler());
		addServerInProjectSubMenu.setCreateSeparatorBefore(true);
		addServerInProjectSubMenu.setSWT_Style(SWT.NONE);
		
		menuBt.addSubMenu(addServerInProjectSubMenu);		
		
		SubMenu newServerSubMenu = new SubMenu();
		newServerSubMenu.setText("New Server");
		newServerSubMenu.setImage(Constant.IMAGE_NEW_SERVER);
		newServerSubMenu.setEventHandler(new NewServerEventHandler());
		newServerSubMenu.setSWT_Style(SWT.NONE);
		
		menuBt.addSubMenu(newServerSubMenu);	
		
//		SubMenu newLinkSubMenu = new SubMenu();
//		newLinkSubMenu.setText("New Link");
//		newLinkSubMenu.setImage(Constant.IMAGE_BROWSER);
//		newLinkSubMenu.setEventHandler(new NewLinkEventHandler());
//		newLinkSubMenu.setCreateSeparatorBefore(true);
//		newLinkSubMenu.setSWT_Style(SWT.NONE);
//		
//		menuBt.addSubMenu(newLinkSubMenu);
		
		ChildItemMenuExplorerPlugin.addSubItemMenuExplorer(menuBt);
		
		SubMenu closeProjectSubMenu = new SubMenu();
		closeProjectSubMenu.setText("Close Project");
		closeProjectSubMenu.setEventHandler(new CloseProjectEventHandler());
		closeProjectSubMenu.setCreateSeparatorBefore(true);
		closeProjectSubMenu.setSWT_Style(SWT.NONE);
		
		menuBt.addSubMenu(closeProjectSubMenu);		
		
		SubMenu closeAllProjectSubMenu = new SubMenu();
		closeAllProjectSubMenu.setText("Close All Projects");
		closeAllProjectSubMenu.setEventHandler(new CloseAllProjectEventHandler());
		closeAllProjectSubMenu.setSWT_Style(SWT.NONE);
		
		menuBt.addSubMenu(closeAllProjectSubMenu);		

		SubMenu workbookSubMenu = new SubMenu();
		workbookSubMenu.setImage(Constant.IMAGE_WEBSPHERE);
		workbookSubMenu.setCreateSeparatorBefore(true);
		workbookSubMenu.setSWT_Style(SWT.NONE);
		
		TreeItem treeItem = MainShell.getControlPanelHelper().getTreeItemProjectSelected(MainShell.getControlPanelHelper().getMyProjectTreeItemSelected());
		if( treeItem!=null && treeItem.getData()!=null && treeItem.getData() instanceof Project ){
			Project project = (Project)treeItem.getData();
			String PROJECT_PATH = Database.getProjectPath()+"/"+project.getAlias();
			
			File f = new File(PROJECT_PATH+"/"+Database.WORKBOOK_FILE_NAME);
			if( !f.exists() ){
				workbookSubMenu.setText("New Workbook");
				workbookSubMenu.setEventHandler(new NewWorkbookEventHandler());
			} else {
				workbookSubMenu.setText("Remove Workbook");
				workbookSubMenu.setEventHandler(new RemoveWorkbookEventHandler());
			}
		}
		
		
		menuBt.addSubMenu(workbookSubMenu);
		
		
		return menuBt;
	        
	}

	public static MenuBT createPopupList(){
		createPopup();
				
		return menuBt;
	}
		
	public static MenuBT createPopupServer(){
		createPopupProject();
		
		SubMenu connectSubMenu = new SubMenu();
		connectSubMenu.setText("Connect");
		connectSubMenu.setImage(Constant.IMAGE_CONNECT);
		connectSubMenu.setEventHandler(new ConnectServerEventHandler());
		connectSubMenu.setSWT_Style(SWT.NONE);
		connectSubMenu.setCreateSeparatorBefore(true);
		
		menuBt.addSubMenu(connectSubMenu);
				
		SubMenu editServerSubMenu = new SubMenu();
		editServerSubMenu.setText("Edit Server");
		editServerSubMenu.setImage(Constant.IMAGE_SERVER);
		editServerSubMenu.setEventHandler(new EditServerEventHandler());
		editServerSubMenu.setSWT_Style(SWT.NONE);
		editServerSubMenu.setCreateSeparatorBefore(true);
		
		menuBt.addSubMenu(editServerSubMenu);
		
		SubMenu removeServerFromProjectSubMenu = new SubMenu();
		removeServerFromProjectSubMenu.setText("Remove Server from Project");
		removeServerFromProjectSubMenu.setImage(Constant.IMAGE_REMOVE_COMMAND);
		removeServerFromProjectSubMenu.setEventHandler(new RemoveServerEventHandler());
		removeServerFromProjectSubMenu.setSWT_Style(SWT.NONE);
		
		menuBt.addSubMenu(removeServerFromProjectSubMenu);	
		
		return menuBt;
	}
	
	public static MenuBT createPopupBeanShellProject(){
		menuBt = new MenuBT();
		
		SubMenu createBeanShellProjectSubMenu = new SubMenu();
		createBeanShellProjectSubMenu.setText("Create Project");
		createBeanShellProjectSubMenu.setImage(Constant.IMAGE_APPLICATION);
		createBeanShellProjectSubMenu.setEventHandler(new RemoveServerEventHandler());
		createBeanShellProjectSubMenu.setSWT_Style(SWT.NONE);
		
		return menuBt;
	}
	
	public static MenuBT createPopupBeanShell(){
		menuBt = createPopupBeanShellProject();
		
		SubMenu createBeanShellSubMenu = new SubMenu();
		createBeanShellSubMenu.setText("Create BeanShell");
		createBeanShellSubMenu.setImage(Constant.IMAGE_JAVABEAN);
		createBeanShellSubMenu.setEventHandler(new RemoveServerEventHandler());
		createBeanShellSubMenu.setCreateSeparatorBefore(true);
		createBeanShellSubMenu.setSWT_Style(SWT.NONE);
		
		menuBt.addSubMenu(createBeanShellSubMenu);		
		
		SubMenu editBeanShellSubMenu = new SubMenu();
		editBeanShellSubMenu.setText("Edit BeanShell");
		editBeanShellSubMenu.setImage(Constant.IMAGE_JAVABEAN);
		editBeanShellSubMenu.setEventHandler(new RemoveServerEventHandler());
		editBeanShellSubMenu.setSWT_Style(SWT.NONE);
		
		menuBt.addSubMenu(editBeanShellSubMenu);		
		
		SubMenu removeBeanShellSubMenu = new SubMenu();
		removeBeanShellSubMenu.setText("Remove BeanShell");
		removeBeanShellSubMenu.setImage(Constant.IMAGE_REMOVE_COMMAND);
		removeBeanShellSubMenu.setEventHandler(new RemoveServerEventHandler());
		removeBeanShellSubMenu.setSWT_Style(SWT.NONE);
		
		menuBt.addSubMenu(removeBeanShellSubMenu);	
		
		return menuBt;
	}
	
	/**
	 * @return the newProjectCreated
	 */
	public static boolean isNewProjectCreated() {
		return newProjectCreated;
	}

	/**
	 * @param newProjectCreated the newProjectCreated to set
	 */
	public static void setNewProjectCreated(boolean newProjectCreated) {
		PopupFactory.newProjectCreated = newProjectCreated;
	}	
}
