package com.mxdeploy.plugin.graphic.swing.action;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.lang.reflect.Method;

import javax.swing.AbstractAction;
import javax.swing.JOptionPane;

import com.mxgraph.swing.mxGraphComponent;

/**
*
*/
@SuppressWarnings("serial")
public class PromptPropertyAction extends AbstractAction {
	/**
	 * 
	 */
	protected Object target;

	/**
	 * 
	 */
	protected String fieldname, message;

	/**
	 * 
	 */
	public PromptPropertyAction(Object target, String message) {
		this(target, message, message);
	}

	/**
	 * 
	 */
	public PromptPropertyAction(Object target, String message,
			String fieldname) {
		this.target = target;
		this.message = message;
		this.fieldname = fieldname;
	}

	/**
	 * 
	 */
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() instanceof Component) {
			try {
				Method getter = target.getClass().getMethod(
						"get" + fieldname);
				Object current = getter.invoke(target);

				// TODO: Support other atomic types
				if (current instanceof Integer) {
					Method setter = target.getClass().getMethod(
							"set" + fieldname, new Class[] { int.class });

					String value = (String) JOptionPane.showInputDialog(
							(Component) e.getSource(), "Value", message,
							JOptionPane.PLAIN_MESSAGE, null, null, current);

					if (value != null) {
						setter.invoke(target, Integer.parseInt(value));
					}
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}

		// Repaints the graph component
		if (e.getSource() instanceof mxGraphComponent) {
			mxGraphComponent graphComponent = (mxGraphComponent) e
					.getSource();
			graphComponent.repaint();
		}
	}
}

