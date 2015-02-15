package com.mxterminal.swt.view.helper;

import java.awt.Frame;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.ScrollBar;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

import com.mxdeploy.api.domain.Project;
import com.mxdeploy.api.domain.Server;
import com.mxsecurity.util.RandomSeed;
import com.mxsecurity.util.SecureRandomAndPad;
import com.mxterminal.console.ConsoleLineReader;
import com.mxterminal.console.ConsoleWin;
import com.mxterminal.ssh.InteractiveClientSSH;
import com.mxterminal.ssh2.TerminalAdapterImplSSH2;
import com.mxterminal.ssh2.TransportSSH2;
import com.mxterminal.swt.TerminalFace;
import com.mxterminal.swt.view.CentralSashFormComposite;
import com.mxterminal.swt.view.TerminalComposite;

public class TerminalHelper {
	//static Logger logger = Logger.getLogger(ConsoleHelper.class);
	
	private TerminalComposite composite = null;
//	private ProductInfoCollectorService productInfoCollectorService = null;
    private String username = null;
    private String password = null;
    private Server server = null;
    private Project project = null;
//    protected boolean isRunning = false;
    
	private Frame frame;
	private ConsoleWin terminal;
	private TransportSSH2 transport;
	//private SimpleClientSSH2 client;
	private ConsoleLineReader lineReader;
	private TerminalAdapterImplSSH2 termAdapter;
	private RandomSeed seed;
	private int port;
	private SecureRandomAndPad secureRandom = null;
	//private RemoteShellScript remoteShellScript = null;
	protected javax.swing.JScrollPane scrollpane;
    protected ScrollBar vBar = null;
	/**
	 * Essa vari�vel � setada no Factory do Server
	 */    
    private ServerHelper serverHelper = null;
	
	public TerminalHelper(TerminalComposite composite){
		this.composite = composite;
		initialize();
	}
	
	public void initialize(){ }
	
    public TreeItem getItemProcedimentoSelecionado(){
    	TreeItem item = null; 
		Tree tree = composite.getProcedureSashFormComposite().getServerSashFormHelper().getViewComposite().getHelper().getTreeProcedure();
		if(( tree.getSelection()!=null)&&(tree.getSelection().length>0) ){
		  item = tree.getSelection()[0];
		}
        return item;
    }	
    
    public ProcedureViewHelper getProcedureTreeHelper(){
    	return composite.getProcedureSashFormComposite().getServerSashFormHelper().getViewComposite().getHelper();
    }
    
	/**
	 * Essa vari�vel � setada no Factory do Server
	 */
	public ServerHelper getServerHelper() {
		return serverHelper;
	}

	/**
	 * Essa vari�vel � setada no Factory do Server
	 */
	public void setServerHelper(ServerHelper serverHelper) {
		this.serverHelper = serverHelper;
	}    

	/**
	 * @return the server
	 */
	public Server getServer() {
		return server;
	}

	/**
	 * @param server the server to set
	 */
	public void setServer(Server server) {
		this.server = server;
	}

	/**
	 * @return the frame  
	 */
	public Frame getFrame() {
		return frame;
	}

	
	private void updateSWTScrollbar() {
		if (scrollpane.getViewport() == null || scrollpane.getViewport().getView() == null)
			return;

		java.awt.Rectangle rect = scrollpane.getViewport().getView().getBounds();

		Rectangle client = composite.termianlFace.getClientArea();
		if (vBar != null) {
			vBar.setMinimum(0);
			vBar.setMaximum(rect.height);
			vBar.setThumb(Math.min(rect.height, client.height));
			vBar.setIncrement(vBar.getThumb() / 4);
			vBar.setPageIncrement(vBar.getThumb());
			vBar.setSelection(scrollpane.getVerticalScrollBar().getValue());
		}
	}
	
	private class ScrollAdjustmentListener implements java.awt.event.AdjustmentListener {
		public void adjustmentValueChanged(java.awt.event.AdjustmentEvent e) {
			//MainShell.sShell.getDisplay().asyncExec(new Runnable() { 20100222 Testando travamento ao abrir terminal
			Display.getDefault().asyncExec(new Runnable() {
				public void run() {
					updateSWTScrollbar();
				}
			});
		}
	}

	private class VBarListener implements Listener {
		public void handleEvent(Event e) {
			final int vSelection = vBar.getSelection();

			javax.swing.SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					scrollpane.getVerticalScrollBar().setValue(vSelection);
				}
			});

		}
	}
	
	private class MouseWheelListener implements java.awt.event.MouseWheelListener {
		public void mouseWheelMoved(final java.awt.event.MouseWheelEvent e) {
			if (e.getScrollType() != java.awt.event.MouseWheelEvent.WHEEL_UNIT_SCROLL)
				return;
			//MainShell.sShell.getDisplay().asyncExec(new Runnable() { 20100222 Testando travamento ao abrir terminal
			Display.getDefault().asyncExec(new Runnable() {
				public void run() {
					if (vBar != null) {
						int move = e.getWheelRotation() * vBar.getIncrement();
						vBar.setSelection(vBar.getSelection() + move);
						vBar.notifyListeners(SWT.Selection, new Event());
					}
				}
			});
		}
	}
	

	public InteractiveClientSSH getSSHInteractiveClient(){
		return composite.termianlFace.getTerminalComposite().getSSHInteractiveClient();
	}
	
	public ToolBarViewHelper getToolBarConsoleHelper(){
		CentralSashFormComposite procedureSashFormComposite = (CentralSashFormComposite)composite.getParent().getParent();
		return procedureSashFormComposite.getServerSashFormHelper().getViewComposite().getHelper().getToolBarViewHelper();
	}

	/**
	 * @return the composite
	 */
	public TerminalComposite getComposite() {
		return composite;
	}
	
	/**
	 * @return the termianlFace
	 */
	public TerminalFace getTermianlFace() {
		return composite.termianlFace;
	}

	/**
	 * @return the project
	 */
	public Project getProject() {
		return project;
	}

	/**
	 * @param project the project to set
	 */
	public void setProject(Project project) {
		this.project = project;
	}	
	
}
