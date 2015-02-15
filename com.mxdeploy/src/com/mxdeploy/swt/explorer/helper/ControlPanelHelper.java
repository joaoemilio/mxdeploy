package com.mxdeploy.swt.explorer.helper;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

import com.mxdeploy.api.domain.Project;
import com.mxdeploy.api.service.ProjectService;
import com.mxdeploy.images.Constant;
import com.mxdeploy.swt.MainShell;
import com.mxdeploy.swt.explorer.ControlPanelComposite;
import com.mxterminal.swt.view.helper.ServerHelper;
import com.mxterminal.swt.view.helper.TerminalHelper;

public class ControlPanelHelper {
	public ControlPanelComposite composite = null;
	
	public ControlPanelHelper(ControlPanelComposite composite){
		this.composite = composite;
	} 
	
	public Project getProjectSelectedInMyProjectTree(){
		if(composite.myProjectTree.getSelection().length<=0){ 
		   return null;
		} 
		TreeItem treeItem = getTreeItemProjectSelected(composite.myProjectTree.getSelection()[0]);
		if(treeItem == null){
			return null;
		} else {
	 	  return (Project)treeItem.getData();
		}
	}
	
	public TreeItem getTreeItemProjectSelected(TreeItem treeItem){
		MainShell.getControlPanelHelper().getMyProjectTreeItemSelected();
		if(treeItem.getData() instanceof Project){
			return treeItem;
		} else {
			return getTreeItemProjectSelected(treeItem.getParentItem());
		}
	}
				
	public void collapseAll(){
		TreeItem[] item = composite.myProjectTree.getItems();
		for(int i=0;i<item.length;i++){
			item[i].setExpanded(false);
		}
	}
	
	public TreeItem getMyProjectTreeItemSelected(){
		if(composite.myProjectTree.getSelectionCount()>0){
		    return composite.myProjectTree.getSelection()[0];
		}
		return null;
	}	
	
	public TreeItem getMyProjectProjectItemSelected(){
		if(composite.myProjectTree.getSelection().length<=0){ 
		   return null;
		} 
		TreeItem treeItem = getTreeItemProjectSelected(composite.myProjectTree.getSelection()[0]);
		if(treeItem == null){
			return null;
		} else {
	 	  return treeItem;
		}
	}
	
	public TerminalHelper getTerminalHelper(){
		CTabItem serverTabItem = (CTabItem)getMyProjectTreeItemSelected().getData(Constant.KEY_TABITEM_SERVER);
		if(serverTabItem == null){
			return null;
		}
		ServerHelper serverHelper = (ServerHelper)serverTabItem.getData(Constant.KEY_SERVER_HELPER);
		return serverHelper.getTerminalHelper();
	}
	
	public boolean connectionIsOpen(){
		if(getTerminalHelper()==null){
		   return false;
		}
		return getTerminalHelper().getSSHInteractiveClient().isAuthenticated();
	}
				
	public void closeProjectSelected(){ 
		Project project = getProjectSelectedInMyProjectTree();
		if(confirm("Do you want to close Project "+project.getAlias()+" ?")){
		   ProjectService service = new ProjectService();
		   project.setIsOpen(new Integer(0));
	  	   service.update(project);
           TreeItem treeItem = getMyProjectTreeItemSelected();
           treeItem.removeAll();
           treeItem.dispose();
		}
	}
	
	public void closeAllProjectSelected(){ 
		TreeItem[] itens = composite.myProjectTree.getItems();
    		if(confirm("Do you want to close all Projects ?")){
    		   ProjectService service = new ProjectService();
    		   Project project = null;
    		   for(int i=0;itens.length>i;i++){
    			   project = (Project)itens[i].getData();
    		       project.setIsOpen(new Integer(0));
    	  	       service.update(project);
    	  	       itens[i].removeAll();
    	  	       itens[i].dispose();
    		   }
    		}
	}		
		
	public boolean confirm(String msg){
    	MessageBox messageBox = new MessageBox(MainShell.sShell, SWT.ICON_QUESTION | SWT.OK | SWT.CANCEL);
        messageBox.setMessage(msg);
        messageBox.setText("Confirm");
        if (messageBox.open() == SWT.CANCEL)
        {
          return false;
        }
        return true;
    }	 
	    
    public Tree getMyProjectTree(){
    	return composite.myProjectTree;
    }

	/**
	 * @return the composite
	 */
	public ControlPanelComposite getComposite() {
		return composite;
	}

	public Project getProjectSelectedInAllProjectTree(){
		ControlPanelComposite composite = MainShell.getControlPanelHelper().getComposite();
		
		if(composite.allProjectTree.getSelection().length<=0){ 
		   return null;
		} 
		TreeItem treeItem = MainShell.getControlPanelHelper().getTreeItemProjectSelected(composite.allProjectTree.getSelection()[0]);
		if(treeItem == null){
			return null;
		} else {
	 	  return (Project)treeItem.getData();
		}
	}	

}