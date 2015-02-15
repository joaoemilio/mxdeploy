package com.mxdeploy.swt;

import org.eclipse.swt.widgets.Composite;

public class ToolBarTopComposite extends Composite {

	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public ToolBarTopComposite(Composite parent, int style) {
		super(parent, style);

	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}

}
