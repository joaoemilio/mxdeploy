package com.mxdeploy.plugin.graphic.swing.action;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import com.mxgraph.swing.util.mxGraphActions;
import com.mxgraph.view.mxGraph;

/**
*
*/
@SuppressWarnings("serial")
public class ToggleAction extends AbstractAction {
	/**
	 * 
	 */
	protected String key;

	/**
	 * 
	 */
	protected boolean defaultValue;

	/**
	 * 
	 * @param key
	 */
	public ToggleAction(String key) {
		this(key, false);
	}

	/**
	 * 
	 * @param key
	 */
	public ToggleAction(String key, boolean defaultValue) {
		this.key = key;
		this.defaultValue = defaultValue;
	}

	/**
	 * 
	 */
	public void actionPerformed(ActionEvent e) {
		mxGraph graph = mxGraphActions.getGraph(e);

		if (graph != null) {
			graph.toggleCellStyles(key, defaultValue);
		}
	}
}

