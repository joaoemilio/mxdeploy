package com.mxdeploy.swt.dialogs.authentication;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.wb.swt.SWTResourceManager;

import com.mxdeploy.swt.MainHelper;

public class HostnameComposite extends Composite {

	private Combo combo;
	
	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public HostnameComposite(final HostnameDialog dialog, Composite parent, int style) {
		super(parent, style);
		
		GridLayout gridLayout = new GridLayout(1, false);
		gridLayout.verticalSpacing = 0;
		gridLayout.marginWidth = 0;
		gridLayout.marginHeight = 0;
		gridLayout.horizontalSpacing = 0;
		setLayout(gridLayout);
		
		combo = new Combo(this, SWT.NONE);
		combo.setFont(SWTResourceManager.getFont("Tahoma", 14, SWT.NORMAL));
		combo.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		combo.addKeyListener( new KeyListener() {
            public void keyPressed( KeyEvent e ) {
            }

            public void keyReleased( KeyEvent e ) {
            	if( e.keyCode == SWT.CR ){ 
            		dialog.setHostname(combo.getText());
    				getShell().close();	
    				return;
            	}
            	if( e.keyCode == SWT.ARROW_LEFT || e.keyCode == SWT.ARROW_RIGHT || 
                	    e.keyCode == SWT.ARROW_UP || e.keyCode == SWT.ARROW_DOWN || 
                	    e.keyCode == SWT.HOME || e.keyCode == SWT.END ||
                	    e.keyCode == SWT.DEL || e.keyCode == SWT.BS  ||
                	    e.keyCode == SWT.SHIFT || e.keyCode == SWT.CTRL ||
                	    e.keyCode == SWT.ALT ){
                		return;
                	}
                	String[] servidores = ((Combo)e.widget).getItems(); 
                	String text = ((Combo)e.widget).getText();
                	for (int i = 0; i < servidores.length; i++) {
                		if(servidores[i].startsWith(text) ){
                			combo.setText(servidores[i]); //setSelection(selection)select(i);
                			combo.setSelection(new Point(text.length(),servidores[i].length()));
                			break;
                		}
                	}            	
            }
        });
		
        final String[] items = MainHelper.loadServerComboItem();
        if( items !=null ){
			Display.getDefault().asyncExec(new Runnable(){
				public void run(){ 	
			        combo.setItems(items);
				}
			});        
       }
		

	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}

}
