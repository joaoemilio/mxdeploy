package com.mxscript.swt.event.styledText;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.PopupList;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Display;

import com.mxdeploy.plugin.event.EventHandler;
import com.mxdeploy.swt.MainShell;
import com.mxscript.Task;
import com.mxscript.swt.BeanShellScriptComposite;

public class StyledTextKeyPressedEvent implements EventHandler {

	private BeanShellScriptComposite composite;
	private KeyEvent event;
	
	public StyledTextKeyPressedEvent(BeanShellScriptComposite composite, KeyEvent event){
		this.composite = composite;
		this.event = event;
	}
	
	public void execute() {
		if( composite.getStyledText().getTopPixel() != composite.getStyledText1().getTopPixel()){
			composite.getStyledText1().setTopPixel(composite.getStyledText().getTopPixel());
		}
		
		if(event.stateMask == SWT.CTRL && event.keyCode != SWT.CTRL){
			if(event.keyCode==115){ 
			   (new SaveBeanShellScriptEvent(composite)).execute(); 
//			} else if(event.keyCode==32){
//		        PopupList list = new PopupList(MainShell.sShell,SWT.BORDER);
//		        
//		        String[] methods = new String[Task.class.getDeclaredMethods().length];
//		        for(int i=0; i < Task.class.getMethods().length;i++){
//		        	int annTypes = Task.class.getMethods()[i].getParameterTypes().length;
//		        	
//		        	methods[i] = Task.class.getMethods()[i].getName()+"(";
//		        	if(annTypes>0){
//			        	String[] paramTypes = new String[annTypes];
//			        	for(int j=0; j < annTypes; j++){
//			        		paramTypes[j] = Task.class.getMethods()[i].getParameterTypes()[j].getSimpleName()+", ";
//			        		methods[i] = methods[i]+paramTypes[j];
//			        	}
//			        	methods[i] = methods[i]+")";
//		        	} else {
//		        		methods[i] = Task.class.getMethods()[i].getName()+")";
//		        	}
//		        	
//		        	methods[i] = methods[i]+" - "+Task.class.getMethods()[i].getReturnType().getName();
//		        }
//		        list.setItems(methods);
//		        list.setFont(new Font(Display.getDefault(), "Courier New", 10, SWT.NORMAL));
//		        
//		        Point p = composite.getStyledText().getCaret().getLocation();
//		        p = MainShell.display.map(composite.getStyledText(), null, p.x, p.y);
//		        list.open(new Rectangle(p.x, p.y-488 , 400, 500));  
			}
		}
		
	}

}

