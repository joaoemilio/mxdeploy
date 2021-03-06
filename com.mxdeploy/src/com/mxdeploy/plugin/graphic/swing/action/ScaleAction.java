package com.mxdeploy.plugin.graphic.swing.action;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JOptionPane;

import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.util.mxResources;

/**
*
*/
@SuppressWarnings("serial")
public class ScaleAction extends AbstractAction {
	/**
	 * 
	 */
	protected double scale;

	/**
	 * 
	 */
	public ScaleAction(double scale) {
		this.scale = scale;
	}

	/**
	 * 
	 */
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() instanceof mxGraphComponent) {
			mxGraphComponent graphComponent = (mxGraphComponent) e
					.getSource();
			double scale = this.scale;

			if (scale == 0) {
				String value = (String) JOptionPane.showInputDialog(
						graphComponent, mxResources.get("value"),
						mxResources.get("scale") + " (%)",
						JOptionPane.PLAIN_MESSAGE, null, null, "");

				if (value != null) {
					scale = Double.parseDouble(value.replace("%", "")) / 100;
				}
			}

			if (scale > 0) {
				graphComponent.zoomTo(scale, graphComponent.isCenterZoom());
			}
		}
	}
}

