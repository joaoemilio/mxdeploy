package com.mxdeploy.plugin.graphic.swing.action;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.swing.handler.mxConnectionHandler;

/**
*
*/
@SuppressWarnings("serial")
public class ToggleConnectModeAction extends AbstractAction {
	/**
	 * 
	 */
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() instanceof mxGraphComponent) {
			mxGraphComponent graphComponent = (mxGraphComponent) e
					.getSource();
			mxConnectionHandler handler = graphComponent
					.getConnectionHandler();
			handler.setHandleEnabled(!handler.isHandleEnabled());
		}
	}
}
