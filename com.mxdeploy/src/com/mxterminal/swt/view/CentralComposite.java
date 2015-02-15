package com.mxterminal.swt.view;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;

import com.mxdeploy.api.domain.Project;
import com.mxdeploy.api.domain.Server;
import com.mxterminal.swt.util.SWTUtils;
import com.mxterminal.swt.view.helper.ServerHelper;


public class CentralComposite extends Composite {
 
	public CentralSashFormComposite serverSashFormComposite = null;
	protected Project project =null; 
	protected Server  server =null;
	private   ServerHelper serverHelper = null;
	//protected CTabFolder cTabFolder = null;
	protected Composite  terminalViewComposite = null;
	protected Composite  beanShellViewComposite = null;
	
	
	public CentralComposite(Composite parent, int style,Project project, Server server) {
		super(parent, style);
		this.project = project;
		this.server = server;
		initialize();
	}

	private void initialize() {
		serverHelper = new ServerHelper(this);
		
		GridLayout gridLayout = new GridLayout();
		gridLayout.marginHeight = 0;
		gridLayout.marginWidth = 0;
		gridLayout.verticalSpacing = 0;
		gridLayout.horizontalSpacing = 0;
		
		createCTabFolder();
		
		this.setLayout(gridLayout);
		setSize(new Point(300, 200)); 
	}
	
	private void createCTabFolder() {
		createTerminalViewComposite();
	}	

	private void createTerminalViewComposite() {
		terminalViewComposite = new Composite(this, SWT.NONE);
		SWTUtils.createGridDataMaximized(terminalViewComposite);
		SWTUtils.createGridLayoutNoMargins(terminalViewComposite);
		
		serverSashFormComposite = new CentralSashFormComposite(terminalViewComposite, SWT.NONE,project, server);
		SWTUtils.createGridDataMaximized(serverSashFormComposite);
		SWTUtils.createGridLayoutNoMargins(serverSashFormComposite);
	}
	
	public ServerHelper getServerHelper() {
		return serverHelper;
	}

	public CentralSashFormComposite getProcedureSashFormComposite(){
		return serverSashFormComposite;
	}
}
