package com.mxdeploy.plugin.graphic.swing.action;

import java.awt.Component;
import java.awt.event.ActionEvent;

import com.mxdeploy.plugin.graphic.swing.GraphicBasicPanel;

/**
 *
 */
public class GraphicAction {

	public static GraphicBasicPanel getEditor(ActionEvent e) {
		if (e.getSource() instanceof Component) {
			Component component = (Component) e.getSource();

			while (component != null && !(component instanceof GraphicBasicPanel)) {
				component = component.getParent();
			}

			return (GraphicBasicPanel) component;
		}

		return null;
	}
	
}
