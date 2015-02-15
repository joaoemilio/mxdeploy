package com.mxscript.menu;

import org.eclipse.swt.SWT;

import com.mxdeploy.images.Constant;
import com.mxdeploy.plugin.menu.MenuAddon;
import com.mxdeploy.plugin.menu.SubMenu;
import com.mxscript.menu.event.OpenNewBeanShellEventHandler;

public class CreateBeanShellExplorerAddonAdapter implements MenuAddon {

	public SubMenu addOn() {
	
		SubMenu createBeanShellSubMenu = new SubMenu();
		createBeanShellSubMenu.setText("New BeanShell");
		createBeanShellSubMenu.setImage(Constant.IMAGE_JAVABEAN);
		createBeanShellSubMenu.setEventHandler(new OpenNewBeanShellEventHandler());
		createBeanShellSubMenu.setCreateSeparatorBefore(false);
		createBeanShellSubMenu.setSWT_Style(SWT.NONE);
		
		return createBeanShellSubMenu;
	}

}
