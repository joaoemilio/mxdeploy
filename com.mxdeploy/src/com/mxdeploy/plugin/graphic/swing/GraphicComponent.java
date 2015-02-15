package com.mxdeploy.plugin.graphic.swing;

import java.awt.Color;
import java.awt.Point;

import org.w3c.dom.Document;

import com.mxgraph.io.mxCodec;
import com.mxgraph.model.mxICell;
import com.mxgraph.model.mxIGraphModel;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.util.mxUtils;
import com.mxgraph.view.mxGraph;

public class GraphicComponent extends mxGraphComponent {

	private static final long serialVersionUID = -6833603133512882012L;

	/**
	 * 
	 * @param graph
	 */
	public GraphicComponent(mxGraph graph) {
		super(graph);

		// Sets switches typically used in an editor
		//setPageVisible(true);
		//setGridVisible(true);
		setToolTips(true);
		getConnectionHandler().setCreateTarget(true);

		// Loads the defalt stylesheet from an external file
		String styleXMLPath = "/com/mxgraph/examples/swing/resources/default-style.xml";
		mxCodec codec = new mxCodec();
		Document doc = mxUtils.loadDocument(GraphicPanel.class.getResource(styleXMLPath).toString());
		codec.decode(doc.getDocumentElement(), graph.getStylesheet());

		// Sets the background to white
		getViewport().setOpaque(true);
		getViewport().setBackground(Color.WHITE);
	}

	/**
	 * Overrides drop behaviour to set the cell style if the target is not a
	 * valid drop target and the cells are of the same type (eg. both vertices
	 * or both edges).
	 */
	public Object[] importCells(Object[] cells, double dx, double dy,
			Object target, Point location) {
		if (target == null && cells.length == 1 && location != null) {
			target = getCellAt(location.x, location.y);

			if (target instanceof mxICell && cells[0] instanceof mxICell) {
				mxICell targetCell = (mxICell) target;
				mxICell dropCell = (mxICell) cells[0];

				if (targetCell.isVertex() == dropCell.isVertex()
						|| targetCell.isEdge() == dropCell.isEdge()) {
					mxIGraphModel model = graph.getModel();
					model.setStyle(target, model.getStyle(cells[0]));
					graph.setSelectionCell(target);

					return null;
				}
			}
		}

		return super.importCells(cells, dx, dy, target, location);
	}

}
