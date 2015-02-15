package com.mxdeploy.plugin.graphic.swing.action;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JCheckBoxMenuItem;

import com.mxdeploy.plugin.graphic.swing.GraphicBasicPanel;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.swing.handler.mxConnectionHandler;

/**
*
*/
@SuppressWarnings("serial")
public class ToggleCreateTargetItem extends JCheckBoxMenuItem {
	/**
	 * 
	 */
	public ToggleCreateTargetItem(final GraphicBasicPanel editor, String name) {
		super(name);
		setSelected(true);

		addActionListener(new ActionListener() {
			/**
			 * 
			 */
			public void actionPerformed(ActionEvent e) {
				mxGraphComponent graphComponent = editor
						.getGraphComponent();

				if (graphComponent != null) {
					mxConnectionHandler handler = graphComponent
							.getConnectionHandler();
					handler.setCreateTarget(!handler.isCreateTarget());
					setSelected(handler.isCreateTarget());
				}
			}
		});
	}
}
