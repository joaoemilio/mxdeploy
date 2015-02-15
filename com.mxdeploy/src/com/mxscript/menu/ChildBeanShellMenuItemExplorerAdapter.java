package com.mxscript.menu;

import org.eclipse.swt.SWT;

import com.mxdeploy.images.Constant;
import com.mxdeploy.plugin.menu.ChildrenMenuExplorer;
import com.mxdeploy.plugin.menu.MenuBT;
import com.mxdeploy.plugin.menu.SubMenu;
import com.mxscript.menu.event.OpenNewBeanShellEventHandler;

public class ChildBeanShellMenuItemExplorerAdapter implements ChildrenMenuExplorer {

	public MenuBT addChildMenu() {
		MenuBT menu = new MenuBT();
		
		SubMenu openBeanShellSubMenu = new SubMenu();
		openBeanShellSubMenu.setText("Open BeanShell");
		openBeanShellSubMenu.setImage(Constant.IMAGE_JAVABEAN);
		openBeanShellSubMenu.setEventHandler(new OpenNewBeanShellEventHandler());
		openBeanShellSubMenu.setCreateSeparatorBefore(false);
		openBeanShellSubMenu.setSWT_Style(SWT.NONE);
		
		menu.addSubMenu(openBeanShellSubMenu);
		
		SubMenu removeBeanShellSubMenu = new SubMenu();
		removeBeanShellSubMenu.setText("Remove BeanShell");
		removeBeanShellSubMenu.setImage(Constant.IMAGE_JAVABEAN);
		removeBeanShellSubMenu.setEventHandler(new OpenNewBeanShellEventHandler());
		removeBeanShellSubMenu.setCreateSeparatorBefore(true);
		removeBeanShellSubMenu.setSWT_Style(SWT.NONE);	
		
		menu.addSubMenu(removeBeanShellSubMenu);
		
		return menu;
	}


}
