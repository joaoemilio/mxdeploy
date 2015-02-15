package com.mxdeploy.plugin.graphic.swing.action;

import java.awt.Component;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JOptionPane;

import com.mxgraph.swing.util.mxGraphActions;
import com.mxgraph.util.mxConstants;
import com.mxgraph.util.mxResources;
import com.mxgraph.view.mxGraph;

/**
*
*/
@SuppressWarnings("serial")
public class PromptValueAction extends AbstractAction {
	/**
	 * 
	 */
	protected String key, message;

	/**
	 * 
	 * @param key
	 */
	public PromptValueAction(String key, String message) {
		this.key = key;
		this.message = message;
	}

	/**
	 * 
	 */
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() instanceof Component) {
			mxGraph graph = mxGraphActions.getGraph(e);

			if (graph != null && !graph.isSelectionEmpty()) {
				String value = (String) JOptionPane.showInputDialog(
						(Component) e.getSource(),
						mxResources.get("value"), message,
						JOptionPane.PLAIN_MESSAGE, null, null, "");

				if (value != null) {
					if (value.equals(mxConstants.NONE)) {
						value = null;
					}

					graph.setCellStyles(key, value);
				}
			}
		}
	}
}

