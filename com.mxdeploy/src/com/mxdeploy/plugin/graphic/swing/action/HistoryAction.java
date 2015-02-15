package com.mxdeploy.plugin.graphic.swing.action;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import com.mxdeploy.plugin.graphic.swing.GraphicBasicPanel;

public class HistoryAction extends AbstractAction {

	private static final long serialVersionUID = 1L;
	
	protected boolean undo;

	public HistoryAction(boolean undo) {
		this.undo = undo;
	}

	public void actionPerformed(ActionEvent e) {
		GraphicBasicPanel editor = GraphicAction.getEditor(e);

		if (editor != null) {
			if (undo) {
				editor.getUndoManager().undo();
			} else {
				editor.getUndoManager().redo();
			}
		}
	}
	
}
