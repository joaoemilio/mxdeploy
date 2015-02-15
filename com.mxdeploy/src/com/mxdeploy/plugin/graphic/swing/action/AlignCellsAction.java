package com.mxdeploy.plugin.graphic.swing.action;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import com.mxgraph.swing.util.mxGraphActions;
import com.mxgraph.view.mxGraph;

/**
*
*/
@SuppressWarnings("serial")
public class AlignCellsAction extends AbstractAction {

	protected String align;

	/**
	 * 
	 * @param key
	 */
	public AlignCellsAction(String align) {
		this.align = align;
	}

	/**
	 * 
	 */
	public void actionPerformed(ActionEvent e) {
		mxGraph graph = mxGraphActions.getGraph(e);

		if (graph != null && !graph.isSelectionEmpty()) {
			graph.alignCells(align);
		}
	}
}

