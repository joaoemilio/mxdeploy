package com.mxterminal.swt.view;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;

import com.mxdeploy.api.domain.Project;
import com.mxdeploy.api.domain.Server;
import com.mxterminal.swt.view.helper.ServerSashFormHelper;

public class CentralSashFormComposite extends Composite {
 
	public SashForm sashForm = null;
	
	public ViewComposite viewComposite =  null;
	public TerminalComposite terminalComposite =  null;
	public Project project =null; 
	public Server server =null;
	
	public ServerSashFormHelper serverSashFormHelper = null;
	
	public CentralSashFormComposite(Composite parent, int style,Project project,Server server) {  
		super(parent, style);
		this.project = project;
		this.server = server;
		initialize();
	}

	private void initialize() {
		serverSashFormHelper = new ServerSashFormHelper(this);
		
		GridLayout gridLayout = new GridLayout();
		gridLayout.horizontalSpacing = 0;
		gridLayout.marginWidth = 0;
		gridLayout.verticalSpacing = 0;
		gridLayout.marginHeight = 0;
		createSashForm();
		this.setLayout(gridLayout);
		setSize(new Point(300, 200));
	}

	/**
	 * This method initializes sashForm	
	 *
	 */
	private void createSashForm() {
		GridData gridData = new GridData();
		gridData.grabExcessHorizontalSpace = true;
		gridData.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		gridData.verticalAlignment = org.eclipse.swt.layout.GridData.FILL;
		gridData.grabExcessVerticalSpace = true;
		sashForm = new SashForm(this, SWT.NONE);
		sashForm.setOrientation(SWT.VERTICAL);
		sashForm.setLayoutData(gridData);
		
		
		Display.getDefault().syncExec(new Runnable(){

			public void run(){
				viewComposite =  new ViewComposite(sashForm,SWT.NONE, project);
				terminalComposite =  new TerminalComposite(sashForm,SWT.NONE,project,server);
				
				viewComposite.setTerminalHelper(terminalComposite.getConsoleHelper());
			    sashForm.setWeights(new int [] {35,65});
			    
				//if( project==null){
				//viewComposite.getCTabFolder().restore();
				//}
			}
		});			
		
		
				
	}

	/**
	 * @return the serverSashFormHelper
	 */
	public ServerSashFormHelper getServerSashFormHelper() {
		return serverSashFormHelper;
	}

	/**
	 * @return the consoleComposite 
	 */
	public TerminalComposite getTerminalComposite() {
		return terminalComposite;
	}

	/**
	 * @return the procedureTreeComposite
	 */
	public ViewComposite getViewComposite() {
		return viewComposite;
	}
	
	
	
	
}
