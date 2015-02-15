package com.mxdeploy.plugin.graphic.swing.action;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JOptionPane;

import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.util.mxResources;
import com.mxgraph.view.mxGraph;

/**
*
*/
@SuppressWarnings("serial")
public class StyleAction extends AbstractAction {
	/**
	 * 
	 */
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() instanceof mxGraphComponent) {
			mxGraphComponent graphComponent = (mxGraphComponent) e
					.getSource();
			mxGraph graph = graphComponent.getGraph();
			String initial = graph.getModel().getStyle(
					graph.getSelectionCell());
			String value = (String) JOptionPane.showInputDialog(
					graphComponent, mxResources.get("style"),
					mxResources.get("style"), JOptionPane.PLAIN_MESSAGE,
					null, null, initial);

			if (value != null) {
				graph.setCellStyle(value);
			}
		}
	}
}
