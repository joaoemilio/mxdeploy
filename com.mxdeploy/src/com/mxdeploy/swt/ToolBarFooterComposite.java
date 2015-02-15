package com.mxdeploy.swt;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;

import com.mxdeploy.api.util.Util;
import com.mxdeploy.images.Constant;
import com.mxterminal.swt.ToolbarLayout;

public class ToolBarFooterComposite extends Composite {

	private Label label = null;
	private CLabel obsCLabel = null;
    private ToolBar toolBar;
    private ToolItem gatewayItem;
    private ProgressBar progressBar;
    
	public ToolBarFooterComposite(Composite parent, int style) {
		super(parent, style);
		initialize();
	}

	public ProgressBar getProgressBar() {
		return progressBar;
	}

	public ToolItem getGatewayItem() {
		return gatewayItem;
	}

	public ToolBar getToolBar() {
		return toolBar;
	}

	private void initialize() { 
		GridData gridData1 = new GridData();
		gridData1.horizontalAlignment = org.eclipse.swt.layout.GridData.END;
		
		progressBar = new ProgressBar(this, SWT.BORDER | SWT.SMOOTH);
		progressBar.setVisible(false);
		
		GridData gridData = new GridData();
		gridData.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		gridData.verticalAlignment = org.eclipse.swt.layout.GridData.FILL;
		gridData.grabExcessHorizontalSpace = true;
		obsCLabel = new CLabel(this, SWT.SHADOW_NONE);
		obsCLabel.setText("");
		obsCLabel.setLayoutData(gridData);
		GridLayout gridLayout = new GridLayout();
		gridLayout.verticalSpacing = 0;
		gridLayout.marginHeight = 0;
		gridLayout.numColumns = 4; 
		
		Composite toolbarComposite = new Composite(this, SWT.NONE);
        
		toolbarComposite.setLayout( new ToolbarLayout() );
        
		toolbarComposite.setLayoutData(new GridData( GridData.VERTICAL_ALIGN_BEGINNING | 
        		                                     GridData.FILL_HORIZONTAL));		
		toolBar = new ToolBar(toolbarComposite, SWT.FLAT);
		//toolBar.setVisible(true);
		//toolBar.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, false, false, 1, 1));
		
		gatewayItem = new ToolItem(toolBar, SWT.NONE);
		gatewayItem.setToolTipText("gateway");
		gatewayItem.setImage(Constant.IMAGE_GATEWAY_RED);

		
		label = new Label(this, SWT.SHADOW_OUT | SWT.RIGHT);
		label.setText("Version "+Util.getCurrVersion());
		label.setLayoutData(gridData1);
		label.setEnabled(false);
		this.setLayout(gridLayout);
		setSize(new Point(529, 26));
	}

	public CLabel getObsCLabel() {
		return obsCLabel;
	}

	public void setObsCLabel(CLabel obsCLabel) {
		this.obsCLabel = obsCLabel;
	}
	
	

}  //  @jve:decl-index=0:visual-constraint="10,10"
