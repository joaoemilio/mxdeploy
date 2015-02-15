package com.mxscript.swt.event;

import java.util.UUID;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MenuEvent;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.TreeItem;

import xml.properties;

import com.mxdeploy.api.domain.Property;
import com.mxdeploy.api.service.PropertyService;
import com.mxdeploy.images.Constant;
import com.mxdeploy.plugin.event.EventHandler;
import com.mxscript.swt.BeanShellFormComposite;
import com.mxscript.swt.property.PropertyDialog;

public class BeanShellFormMenuShowEvent implements EventHandler {

	private Menu popupMenu;
	private BeanShellFormComposite composite;
	private TreeItem treeItemSelected = null;
	
	public BeanShellFormMenuShowEvent(MenuEvent menuEvent, Menu popupMenu, BeanShellFormComposite composite){
		this.popupMenu = popupMenu;
		this.composite = composite;
	}
	
	public void execute() {
		
		MenuItem[] items = popupMenu.getItems();
        for (int i = 0; i < items.length; i++) {
           ((MenuItem) items[i]).dispose();
        }
        
        
        if( composite.getTreeProperty().getSelection()!= null && composite.getTreeProperty().getSelection()[0] != null ){
        	treeItemSelected = composite.getTreeProperty().getSelection()[0];
        	if( !treeItemSelected.getText().equals("Local") && !treeItemSelected.getText().equals("Global") ){
        		MenuItem addSubMenu = new MenuItem(popupMenu, SWT.PUSH);
        		addSubMenu.setText("Add Property"); 
        		addSubMenu.setImage(Constant.IMAGE_PROPERTY);
        		addSubMenu.addListener(SWT.Selection, new Listener() {
        	      	  public void handleEvent(Event event) {
        	      		openDialog(treeItemSelected, true);
        	      	  }
                });  
        		
        		MenuItem editSubMenu = new MenuItem(popupMenu, SWT.PUSH);
        		editSubMenu.setText("Edit Property"); 
        		editSubMenu.setImage(Constant.IMAGE_PROPERTY);
        		editSubMenu.addListener(SWT.Selection, new Listener() {
        	      	  public void handleEvent(Event event) {
        	      		 openDialog(treeItemSelected, false);
        	      	  }
                }); 
        		
        		MenuItem removeSubMenu = new MenuItem(popupMenu, SWT.PUSH);
        		removeSubMenu.setText("Remove Property"); 
        		removeSubMenu.setImage(Constant.IMAGE_PROPERTY);
        		removeSubMenu.addListener(SWT.Selection, new Listener() {
        	      	  public void handleEvent(Event event) {
        	      		    properties propXML = (properties)treeItemSelected.getParent().getData();
        	      		    Property property = (Property)treeItemSelected.getData();
        	      		    
	        	  			for( Property prop : propXML.getProperties() ){
	        					 if( prop.getId().equals(property.getId() ) ){
	        						 propXML.getProperties().remove(prop);
	        					 }
	        				}
	        	  			
	        	  			treeItemSelected.removeAll();
	        	  			treeItemSelected.dispose();
	        	  			
	        	  			PropertyService service = new PropertyService();
	        	  			service.writeXML(propXML);       	      		  
	        	      }
                });         		
        	} else {
        		MenuItem addSubMenu = new MenuItem(popupMenu, SWT.PUSH);
        		addSubMenu.setText("Add Property"); 
        		addSubMenu.setImage(Constant.IMAGE_PROPERTY);
        		addSubMenu.addListener(SWT.Selection, new Listener() {
        	      	  public void handleEvent(Event event) {
        	      		  openDialog(treeItemSelected, true);     	      		  
        	      	  }
                });
        	}
        } else {
        	return;
        }
	
	}
	
	public void openDialog(TreeItem editTreeItem, boolean isNew){
		Property property = null;
		properties propXML = (properties)editTreeItem.getParent().getData();
		
		PropertyDialog dialog = new PropertyDialog();
		dialog.setPropXML(propXML);
		if( isNew ){
			property = new Property();
			property.setId(UUID.randomUUID().toString());
			if( editTreeItem.getData()!= null && editTreeItem.getData() instanceof Property ) {
				TreeItem parent = editTreeItem.getParentItem();
				dialog.setType(parent.getText());				
			} else {
				dialog.setType(editTreeItem.getText());
			}
		} else {
			property = (Property)editTreeItem.getData();
			dialog.setName(property.getName());
			dialog.setType(property.getType());
			if( property.getValue()!=null ){
				dialog.setValue(property.getValue());
			}			
		}
		dialog.getComposite().getNameText().forceFocus();
		dialog.openShell();
		
		if ( dialog.getCanceled() ){
			return;
		}
		
		property.setName(dialog.getName());
		if( dialog.getValue() != null && dialog.getValue().trim().length()>0 ){
			property.setValue(dialog.getValue());
		}
		property.setType(dialog.getType());
		  
		if( isNew ){
			
			for( TreeItem treeItemFound : composite.getTreeProperty().getItems() ){
				if ( property.getType().equals(treeItemFound.getText() ) ){
					 
					 TreeItem subTreeItem = new TreeItem(treeItemFound, SWT.NONE);
					 subTreeItem.setText(0,property.getName());
					 if( property.getValue()!=null && !property.getValue().isEmpty()){
						 subTreeItem.setText(1,property.getValue());
					 }
					 subTreeItem.setData(property);
					 subTreeItem.getExpanded();
					 break;
				}
			}		
		} else {
			 editTreeItem.setText(0,property.getName());
			 if( property.getValue()!=null && !property.getValue().isEmpty()){
				 editTreeItem.setText(1,property.getValue());
			 }
			 editTreeItem.setData(property);
			 editTreeItem.getExpanded();
		}
		
		if( isNew ){
			propXML.getProperties().add(property);
		} else {
			for( Property prop : propXML.getProperties() ){
				 if( prop.getId().equals(property.getId() ) ){
					 prop.setName(property.getName());
					 prop.setValue(property.getValue());
					 prop.setType(property.getType());
				 }
			}
		}
		
		PropertyService service = new PropertyService();
		service.writeXML(propXML);
		
	}
	

}
