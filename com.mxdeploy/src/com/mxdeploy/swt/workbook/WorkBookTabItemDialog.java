package com.mxdeploy.swt.workbook;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

public class WorkBookTabItemDialog extends Dialog {

	protected Object result;
	protected Shell shell;

	/**
	 * Create the dialog.
	 * @param parent
	 * @param style
	 */
	public WorkBookTabItemDialog(Shell parent, int style) {
		super(parent, style);
		setText("SWT Dialog");
	}

	/**
	 * Open the dialog.
	 * @return the result
	 */
	public Object open() {
		createContents();
		shell.open();
		shell.layout();
		Display display = getParent().getDisplay();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		return result;
	}

	/**
	 * Create contents of the dialog.
	 */
	private void createContents() {
		shell = new Shell(getParent(), SWT.SHELL_TRIM);
		shell.setSize(866, 589);
		shell.setText(getText());
		shell.setLayout(new GridLayout(1, false));
		
		WorkbookTabItemComposite workbookTabItemComposite = new WorkbookTabItemComposite(shell, SWT.NONE);
		workbookTabItemComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		GridLayout gl_workbookTabItemComposite = new GridLayout(1, false);
		gl_workbookTabItemComposite.verticalSpacing = 0;
		gl_workbookTabItemComposite.marginWidth = 0;
		gl_workbookTabItemComposite.marginHeight = 0;
		gl_workbookTabItemComposite.horizontalSpacing = 0;
		workbookTabItemComposite.setLayout(gl_workbookTabItemComposite);

	}
	
	
	public static void main(String args[]){
		Shell shell = new Shell();
		shell.setSize(866, 589);
		shell.setLayout(new GridLayout(1, false));		
		WorkBookTabItemDialog workBookTabItemDialog = new WorkBookTabItemDialog(shell, SWT.NONE);
		workBookTabItemDialog.createContents();
		workBookTabItemDialog.open();
	}


	
}
