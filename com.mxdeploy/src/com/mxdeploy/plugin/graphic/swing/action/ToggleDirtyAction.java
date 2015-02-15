package com.mxdeploy.plugin.graphic.swing.action;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import com.mxgraph.swing.mxGraphComponent;

/**
*
*/
@SuppressWarnings("serial")
public class ToggleDirtyAction extends AbstractAction {
	/**
	 * 
	 */
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() instanceof mxGraphComponent) {
			mxGraphComponent graphComponent = (mxGraphComponent) e
					.getSource();
			graphComponent.showDirtyRectangle = !graphComponent.showDirtyRectangle;
		}
	}

}
