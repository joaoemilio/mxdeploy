package com.mxdeploy.plugin.graphic.swing.action;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import org.w3c.dom.Document;

import com.mxgraph.io.mxCodec;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.util.mxUtils;
import com.mxgraph.view.mxGraph;

@SuppressWarnings("serial")
public class StylesheetAction extends AbstractAction {
	/**
	 * 
	 */
	protected String stylesheet;

	/**
	 * 
	 */
	public StylesheetAction(String stylesheet) {
		this.stylesheet = stylesheet;
	}

	/**
	 * 
	 */
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() instanceof mxGraphComponent) {
			mxGraphComponent graphComponent = (mxGraphComponent) e
					.getSource();
			mxGraph graph = graphComponent.getGraph();
			mxCodec codec = new mxCodec();
			Document doc = mxUtils.loadDocument(GraphicAction.class.getResource(stylesheet).toString());

			if (doc != null) {
				codec.decode(doc.getDocumentElement(), graph.getStylesheet() );
				graph.refresh();
			}
		}
	}
}

