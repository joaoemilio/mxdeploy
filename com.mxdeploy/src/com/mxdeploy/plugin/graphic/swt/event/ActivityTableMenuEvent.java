package com.mxdeploy.plugin.graphic.swt.event;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MenuEvent;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.TreeItem;

import com.mxdeploy.images.Constant;
import com.mxdeploy.plugin.event.EventHandler;
import com.mxdeploy.plugin.graphic.swt.ActivityTableComposite;

public class ActivityTableMenuEvent implements EventHandler {

	private Menu popupMenu;
	private ActivityTableComposite composite;
	private TreeItem treeItemSelected;
	
	public ActivityTableMenuEvent(MenuEvent menuEvent, Menu popupMenu, ActivityTableComposite composite){
		this.popupMenu = popupMenu;
		this.composite = composite;
	}	
	
	public void execute() {
		
		MenuItem[] items = popupMenu.getItems();
        for (int i = 0; i < items.length; i++) {
           ((MenuItem) items[i]).dispose();
        }
        
        
        if( composite.getTree().getSelection()!= null && composite.getTree().getSelection().length>0 ){
        	treeItemSelected = composite.getTree().getSelection()[0];
    		MenuItem addSubMenu = new MenuItem(popupMenu, SWT.PUSH);
    		addSubMenu.setText("Add Action"); 
    		addSubMenu.setImage(Constant.IMAGE_PROPERTY);
    		addSubMenu.addListener(SWT.Selection, new Listener() {
    	      	  public void handleEvent(Event event) {
    	      		//openDialog(tableItemSelected, true);
    	      	  }
            });  
	
        } else {
    		MenuItem addSubMenu = new MenuItem(popupMenu, SWT.PUSH);
    		addSubMenu.setText("Add Bengin"); 
    		addSubMenu.setImage(Constant.IMAGE_PROPERTY);
    		addSubMenu.addListener(SWT.Selection, new Listener() {
    	      	  public void handleEvent(Event event) {
    	      		//openDialog(tableItemSelected, true);
    	      	  }	

            });  
        	
        }
        
	}
	
}
