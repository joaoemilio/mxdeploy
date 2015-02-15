package com.mxscript.swt.property.event;

import org.eclipse.swt.widgets.Control;

import xml.properties;

import com.mxdeploy.api.domain.Property;
import com.mxscript.swt.property.PropertyComposite;

public class PropertyEvent {

	private PropertyComposite composite;
	
	public void validate(org.eclipse.swt.events.TypedEvent e){
		composite = (PropertyComposite)((Control)e.widget).getParent().getParent().getParent();
		composite.getWarnigLabel().setText("");
		if( composite.getNameText().getText().trim().length()==0 ){
			composite.getWarnigLabel().setText("   * Name is empty !");
			return;
		}
		
		properties propXML = composite.getDialog().getPropXML();
		if( propXML.getProperties()!=null && !propXML.getProperties().isEmpty() ){
			for( Property prop : propXML.getProperties() ){
				 if( prop.getName().equals(composite.getNameText().getText()) ){
					 composite.getWarnigLabel().setText("   * Name alreay exists !");
					 return;
				 }
			}
				
		}
		
	}
	

}
