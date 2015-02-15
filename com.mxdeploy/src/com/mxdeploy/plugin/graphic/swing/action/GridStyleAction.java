package com.mxdeploy.plugin.graphic.swing.action;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import com.mxgraph.swing.mxGraphComponent;

@SuppressWarnings("serial")
public class GridStyleAction extends AbstractAction {
	/**
	 * 
	 */
	protected int style;

	/**
	 * 
	 */
	public GridStyleAction(int style) {
		this.style = style;
	}

	/**
	 * 
	 */
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() instanceof mxGraphComponent) {
			mxGraphComponent graphComponent = (mxGraphComponent)e.getSource();
			graphComponent.setGridStyle(style);
			graphComponent.repaint();
		}
	}
}
