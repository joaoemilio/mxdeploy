package com.mxterminal.swt.view.helper;

import org.eclipse.swt.custom.SashForm;

import com.mxterminal.swt.view.ViewComposite;
import com.mxterminal.swt.view.CentralSashFormComposite;
import com.mxterminal.swt.view.TerminalComposite;

public class ServerSashFormHelper {

	private CentralSashFormComposite centralSashFormComposite = null;
	
	public ServerSashFormHelper(CentralSashFormComposite serverSashFormComposite){
		this.centralSashFormComposite = serverSashFormComposite;
	}
	
	/**
	 * @return the consoleComposite
	 */
	public TerminalComposite getConsoleComposite() {
		return centralSashFormComposite.terminalComposite;
	}

	/**
	 * @return the procedimentoTreeComposite
	 */
	public ViewComposite getViewComposite() {
		return centralSashFormComposite.getViewComposite();
	}
	
	/**
	 * @return the sashForm
	 */
	public SashForm getSashForm() {
		return centralSashFormComposite.sashForm;
	}
	
}
