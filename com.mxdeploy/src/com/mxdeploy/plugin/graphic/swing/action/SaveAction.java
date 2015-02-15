package com.mxdeploy.plugin.graphic.swing.action;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.UUID;

import javax.swing.AbstractAction;
import javax.swing.JOptionPane;

import com.mxdeploy.plugin.graphic.swing.GraphicBasicPanel;
import com.mxgraph.io.mxCodec;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.util.mxCellRenderer;
import com.mxgraph.util.mxResources;
import com.mxgraph.util.mxUtils;
import com.mxgraph.util.mxXmlUtils;
import com.mxgraph.util.png.mxPngEncodeParam;
import com.mxgraph.util.png.mxPngImageEncoder;
import com.mxgraph.view.mxGraph;

@SuppressWarnings("serial")
public class SaveAction extends AbstractAction {
	/**
	 * 
	 */
	protected boolean showDialog;

	/**
	 * 
	 */
	protected String lastDir = null;

	/**
	 * Saves XML+PNG format.
	 */
	protected void saveXmlPng(GraphicBasicPanel editor, String filename, Color bg) throws IOException {
		mxGraphComponent graphComponent = editor.getGraphComponent();
		mxGraph graph = graphComponent.getGraph();

		// Creates the image for the PNG file
		BufferedImage image = mxCellRenderer.createBufferedImage(graph, null,
				1, bg, graphComponent.isAntiAlias(), null,
				graphComponent.getCanvas());

		// Creates the URL-encoded XML data
		mxCodec codec = new mxCodec();
		String xml = URLEncoder.encode(
				mxXmlUtils.getXml(codec.encode(graph.getModel())), "UTF-8");
		mxPngEncodeParam param = mxPngEncodeParam.getDefaultEncodeParam(image);
		param.setCompressedText(new String[] { "mxGraphModel", xml });

		// Saves as a PNG file
		FileOutputStream outputStream = new FileOutputStream(new File(filename));
		try {
			mxPngImageEncoder encoder = new mxPngImageEncoder(outputStream,	param);

			if (image != null) {
				encoder.encode(image);
				editor.setModified(false);
				editor.setCurrentFile(new File(filename));
			} else {
				JOptionPane.showMessageDialog( graphComponent, mxResources.get("noImageData") );
			}
		} finally {
			outputStream.close();
		}
	}

	/**
	 * 
	 */
	public void actionPerformed(ActionEvent e) {
		GraphicBasicPanel editor = GraphicAction.getEditor(e);

		if (editor != null) {
			mxGraphComponent graphComponent = editor.getGraphComponent();
			mxGraph graph = graphComponent.getGraph();
			
			String filename = "/home/fbsantos/workspace/com.wds.plugin/"+UUID.randomUUID().toString()+".xme";

			try {
				mxCodec codec = new mxCodec();
				String xml = mxXmlUtils.getXml(codec.encode(graph.getModel()));

				mxUtils.writeFile(xml, filename);

				editor.setModified(false);
				editor.setCurrentFile(new File(filename));
			} catch (Throwable ex) {
				ex.printStackTrace();
				JOptionPane.showMessageDialog(graphComponent, ex.toString(), mxResources.get("error"), JOptionPane.ERROR_MESSAGE);
			}
		}
	}
}
