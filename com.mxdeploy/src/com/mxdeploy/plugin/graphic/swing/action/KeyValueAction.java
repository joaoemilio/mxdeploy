package com.mxdeploy.plugin.graphic.swing.action;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import com.mxgraph.swing.util.mxGraphActions;
import com.mxgraph.view.mxGraph;

/**
*
*/
@SuppressWarnings("serial")
public class KeyValueAction extends AbstractAction {
	/**
	 * 
	 */
	protected String key, value;

	/**
	 * 
	 * @param key
	 */
	public KeyValueAction(String key) {
		this(key, null);
	}

	/**
	 * 
	 * @param key
	 */
	public KeyValueAction(String key, String value) {
		this.key = key;
		this.value = value;
	}

	/**
	 * 
	 */
	public void actionPerformed(ActionEvent e) {
		mxGraph graph = mxGraphActions.getGraph(e);

		if (graph != null && !graph.isSelectionEmpty()) {
			graph.setCellStyles(key, value);
		}
	}
}

