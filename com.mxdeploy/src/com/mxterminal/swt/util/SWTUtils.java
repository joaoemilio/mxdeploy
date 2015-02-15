package com.mxterminal.swt.util;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class SWTUtils {

	private static String aux;
	
	public static GridLayout createGridLayoutNoMargins(Composite composite) {
		GridLayout gridLayout = new GridLayout();
		gridLayout.marginWidth = 0;
		gridLayout.horizontalSpacing = 0;
		gridLayout.verticalSpacing = 0;
		gridLayout.marginHeight = 0;
		gridLayout.numColumns=1;
		composite.setLayout(gridLayout);
		return gridLayout;
	}
	
	public static GridLayout createGridLayoutLeftTopMargins(Composite composite) {
		GridLayout gridLayout = new GridLayout();
		gridLayout.marginWidth = 0;
		gridLayout.horizontalSpacing = 0;
		gridLayout.verticalSpacing = 0;
		gridLayout.marginHeight = 0;
		gridLayout.numColumns=1;
		composite.setLayout(gridLayout);
		return gridLayout;
	}
	
	public static GridData createGridDataMaximized(Composite composite) {
		GridData gridData = new GridData();
		gridData.grabExcessHorizontalSpace = true;
		gridData.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		gridData.verticalAlignment = org.eclipse.swt.layout.GridData.FILL;
		gridData.verticalSpan = 1;
		gridData.grabExcessVerticalSpace = true;		
		composite.setLayoutData(gridData);
		return gridData;
	}
	
	public static void showMessage(Shell shell, String message){
	    MessageBox msgBox = new MessageBox(shell, SWT.OK);
	    msgBox.setMessage(message);
	    msgBox.open();	
	}

	public static void setTextContent(final Text widget, final String string) {
		Display.getDefault().syncExec(new Runnable(){
			public void run(){ 
				widget.setText(string);
			}
		});
	}

	public static void setLabelText(final Label widget, final String string) {
		Display.getDefault().syncExec(new Runnable(){
			public void run(){ 
				widget.setText(string);
			}
		});
	}
	
	public static String returnTextContent(final Text text) {
		Display.getDefault().syncExec(new Runnable(){
			public void run(){ 
				aux = text.getText();
			}
		});
		return aux;
	}
		
}
