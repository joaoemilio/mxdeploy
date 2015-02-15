package com.mxdeploy.plugin.graphic.swing.action;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JOptionPane;

import com.mxdeploy.plugin.graphic.swing.GraphicBasicPanel;
import com.mxgraph.model.mxCell;
import com.mxgraph.util.mxResources;
import com.mxgraph.view.mxGraph;

/**
*
*/
@SuppressWarnings("serial")
public class NewAction extends AbstractAction {
	/**
	 * 
	 */
	public void actionPerformed(ActionEvent e) {
		GraphicBasicPanel editor = GraphicAction.getEditor(e);

		if (editor != null) {
			if (!editor.isModified()
					|| JOptionPane.showConfirmDialog(editor,
							mxResources.get("loseChanges")) == JOptionPane.YES_OPTION) {
				mxGraph graph = editor.getGraphComponent().getGraph();

				// Check modified flag and display save dialog
				mxCell root = new mxCell();
				root.insert(new mxCell());
				graph.getModel().setRoot(root);

				editor.setModified(false);
				editor.setCurrentFile(null);
				editor.getGraphComponent().zoomAndCenter();
			}
		}
	}
}

