package com.mxdeploy.swt.explorer.event;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.TreeItem;

import com.mxdeploy.api.domain.Project;
import com.mxdeploy.images.Constant;
import com.mxdeploy.swt.MainShell;
import com.mxdeploy.swt.explorer.ControlPanelComposite;

public class AddInAllProjectTreeItemEventHandler {

	public static TreeItem execute(Project project){ 
		ControlPanelComposite composite = MainShell.getControlPanelHelper().getComposite();
		
		TreeItem treeItem = new TreeItem(composite.allProjectTree, SWT.NULL);
		treeItem.setImage(Constant.IMAGE_APPLICATION);
		treeItem.setText(project.getAlias()); 
		treeItem.setData(project);
		treeItem.setExpanded(true);
		return treeItem;
	}

}
