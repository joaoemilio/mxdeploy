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
public class WarningAction extends AbstractAction {
	/**
	 * 
	 */
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() instanceof mxGraphComponent) {
			mxGraphComponent graphComponent = (mxGraphComponent) e
					.getSource();
			Object[] cells = graphComponent.getGraph().getSelectionCells();

			if (cells != null && cells.length > 0) {
				String warning = JOptionPane.showInputDialog(mxResources
						.get("enterWarningMessage"));

				for (int i = 0; i < cells.length; i++) {
					graphComponent.setCellWarning(cells[i], warning);
				}
			} else {
				JOptionPane.showMessageDialog(graphComponent,
						mxResources.get("noCellSelected"));
			}
		}
	}
}

