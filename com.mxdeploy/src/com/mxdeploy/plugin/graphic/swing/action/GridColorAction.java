package com.mxdeploy.plugin.graphic.swing.action;

import java.awt.Color;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JColorChooser;

import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.util.mxResources;

/**
*
*/
@SuppressWarnings("serial")
public class GridColorAction extends AbstractAction {
	/**
	 * 
	 */
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() instanceof mxGraphComponent) {
			mxGraphComponent graphComponent = (mxGraphComponent) e
					.getSource();
			Color newColor = JColorChooser.showDialog(graphComponent,
					mxResources.get("gridColor"),
					graphComponent.getGridColor());

			if (newColor != null) {
				graphComponent.setGridColor(newColor);
				graphComponent.repaint();
			}
		}
	}
}
