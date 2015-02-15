package com.mxterminal.swt.view;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.TreeItem;

import com.mxdeploy.api.domain.Project;
import com.mxdeploy.api.domain.Server;
import com.mxdeploy.images.Constant;
import com.mxdeploy.swt.MainShell;
import com.mxdeploy.swt.explorer.helper.ControlPanelHelper;
import com.mxterminal.swt.TerminalFace;
import com.mxterminal.swt.util.SWTUtils;
import com.mxterminal.swt.view.helper.TerminalHelper;

public class TerminalComposite extends Composite {
	//static Logger logger = Logger.getLogger(ConsoleComposite.class);  //  @jve:decl-index=0:
	
	public CTabItem consoleTabItem = null;
	public TerminalHelper helper = null;
	public Project project =null; 
	public Server server =null;	
	public TerminalFace termianlFace = null;
	
	public TerminalComposite(Composite parent, int style, Project project, Server server) {
		super(parent, style);
		this.project = project;
		this.server = server;			
		helper =  new TerminalHelper(this);
		initialize();
	} 

	public ControlPanelHelper getWorkingSetHelper(){
		return MainShell.getControlPanelHelper();
	}		
	
	public CentralSashFormComposite getProcedureSashFormComposite(){
		return (CentralSashFormComposite)this.getParent().getParent();
	}

	private void initialize() {
		createTabITerminal();
        this.setSize(new Point(346, 234));
        GridLayout gridLayout = new GridLayout();
        gridLayout.marginHeight = 0;
        gridLayout.verticalSpacing = 0;
        gridLayout.marginWidth = 0;
        this.setLayout(gridLayout);
	}

	public void createTabITerminal(){
		if (server!=null){
			termianlFace = new TerminalFace(this,SWT.NONE,server.getHostname());
			//termianlFace.connectToolItem.setEnabled(false);
			//helper.isRunning=true;
			if( project != null ){
				final TreeItem item = getWorkingSetHelper().getMyProjectTreeItemSelected();
				//MainShell.display.asyncExec(new Runnable() { Teste Terminal hung...
				Display.getDefault().syncExec(new Runnable(){				
					public void run() {
						  item.setImage(Constant.IMAGE_SERVER_CONNECTED);
						  item.setData(Constant.KEY_ITEM_CONNECTED,true);
					}
				});		
			}
		} else {
			termianlFace = new TerminalFace(this,SWT.NONE,null);
			//termianlFace.getCombo().setFocus();
		}
		SWTUtils.createGridLayoutNoMargins(termianlFace);
		SWTUtils.createGridDataMaximized(termianlFace);
	}
	

	
	/**
	 * @return the consoleTabItem
	 */
	protected CTabItem getConsoleTabItem() {
		return consoleTabItem;
	}

	/**
	 * @return the consoleHelper
	 */
	public TerminalHelper getConsoleHelper() {
		return helper;
	}

	/**
	 * @return the termianlFace
	 */
	public TerminalFace getTermianlFace() {
		return termianlFace;
	}




	
	
	
}  //  @jve:decl-index=0:visual-constraint="10,10"
