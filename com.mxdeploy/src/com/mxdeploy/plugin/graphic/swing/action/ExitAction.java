package com.mxdeploy.plugin.graphic.swing.action;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import com.mxdeploy.plugin.graphic.swing.GraphicBasicPanel;

public class ExitAction extends AbstractAction {

	private static final long serialVersionUID = 1L;

	public void actionPerformed(ActionEvent e) {
		GraphicBasicPanel editor = GraphicAction.getEditor(e);

		if (editor != null) {
			editor.exit();
		}
	}
}