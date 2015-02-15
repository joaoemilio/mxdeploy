package com.mxscript.swt.event;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.TreeItem;

import xml.properties;

import com.mxdeploy.api.domain.Property;
import com.mxdeploy.api.service.PropertyService;
import com.mxdeploy.plugin.event.EventHandler;

public class LoadPropertiesEvent  implements EventHandler {

    private TreeItem treeItemLocal;
    private TreeItem treeItemGlobal;
	
	public LoadPropertiesEvent(TreeItem treeItemLocal, TreeItem treeItemGlobal){
		this.treeItemLocal = treeItemLocal;
		this.treeItemGlobal = treeItemGlobal;
	}
	
	@Override
	public void execute() {
		
		PropertyService service = new PropertyService();
		properties _properties_ = service.loadPropertyXML();
		
		if( _properties_!=null && !_properties_.getProperties().isEmpty() ){
			for( Property property : _properties_.getProperties() ){
				 if( property.getType().equals("Global")){
					 TreeItem treeItem = new TreeItem(treeItemGlobal, SWT.NONE);
					 treeItem.setText(0,property.getName());
					 if( property.getValue()!=null ){
						 treeItem.setText(1,property.getValue());
					 }
					 treeItem.setData(property);
					 treeItem.setExpanded(true);
				 } else {
					 TreeItem treeItem = new TreeItem(treeItemLocal, SWT.NONE);
					 treeItem.setText(0,property.getName());
					 if( property.getValue()!=null ){
						 treeItem.setText(1,property.getValue());
					 }
					 treeItem.setData(property);
					 treeItem.setExpanded(true);					 
				 }
			}
		}
		
		treeItemGlobal.setExpanded(true);
		treeItemLocal.setExpanded(true);
		
	} 

}
