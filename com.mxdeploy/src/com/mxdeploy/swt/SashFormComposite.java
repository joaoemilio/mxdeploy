package com.mxdeploy.swt;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.widgets.Composite;

import com.mxdeploy.swt.explorer.ControlPanelComposite;
import com.mxterminal.swt.util.SWTUtils;
import com.mxterminal.swt.view.CTopTabFolder;

public class SashFormComposite extends Composite {
	//static Logger logger = Logger.getLogger(SashFormComposite.class);
	public SashForm sashForm = null;
	private ControlPanelComposite workingSetComposite = null;
	private CTopTabFolder topTabFolder = null;
	
	public SashFormComposite(Composite parent, int style) {
		super(parent, style);
		initialize();
	}
 
	public CTopTabFolder getCTopTabFolder(){
		return topTabFolder;
	}
		
	public ControlPanelComposite getWorkingSetComposite(){
		return workingSetComposite;
	}

	private void initialize() {
		createSashForm();
	}

	/**
	 * This method initializes sashForm	 
	 *
	 */
	private void createSashForm() {
		
		sashForm = new SashForm(this, SWT.NONE);
		SWTUtils.createGridLayoutNoMargins(sashForm);
		SWTUtils.createGridDataMaximized(sashForm);
		workingSetComposite = new ControlPanelComposite(sashForm, SWT.NONE);
		topTabFolder =  new CTopTabFolder(sashForm,SWT.CLOSE ); 
		sashForm.setWeights(new int [] {25,75});	
	}
}  //  @jve:decl-index=0:visual-constraint="10,10"
