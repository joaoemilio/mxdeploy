package com.mxdeploy.plugin.graphic.swing.action;

import java.awt.event.ActionEvent;
import java.awt.print.PageFormat;
import java.awt.print.PrinterJob;

import javax.swing.AbstractAction;

import com.mxgraph.swing.mxGraphComponent;

/**
*
*/
@SuppressWarnings("serial")
public class PageSetupAction extends AbstractAction {
	/**
	 * 
	 */
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() instanceof mxGraphComponent) {
			mxGraphComponent graphComponent = (mxGraphComponent) e
					.getSource();
			PrinterJob pj = PrinterJob.getPrinterJob();
			PageFormat format = pj.pageDialog(graphComponent
					.getPageFormat());

			if (format != null) {
				graphComponent.setPageFormat(format);
				graphComponent.zoomAndCenter();
			}
		}
	}
}
