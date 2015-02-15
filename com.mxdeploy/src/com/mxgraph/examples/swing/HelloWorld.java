package com.mxgraph.examples.swing;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.Map;

import javax.swing.JFrame;

import com.mxgraph.model.mxCell;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.util.mxCellRenderer;
import com.mxgraph.util.mxConstants;
import com.mxgraph.util.mxRectangle;
import com.mxgraph.view.mxGraph;



public class HelloWorld extends JFrame
{

	/**
	 * 
	 */
	private static final long serialVersionUID = -2707712944901661771L;

	public HelloWorld()
	{
		super("Hello, World!");

		mxGraph graph = new mxGraph();
		Object parent = graph.getDefaultParent();

		Map<String, Object> style = graph.getStylesheet().getDefaultVertexStyle();
		//style.put(mxConstants.STYLE_GRADIENTCOLOR, "#FFFFFF");
		//style.put(mxConstants.STYLE_ROUNDED, true);
		style.put(mxConstants.STYLE_SHADOW, true);
		style.put(mxConstants.STYLE_IMAGE,"/com/mxgraph/examples/swing/images/fork.png");
		style.put(mxConstants.STYLE_IMAGE_HEIGHT,"15");
		style.put(mxConstants.STYLE_IMAGE_WIDTH,"15");
		//style.put(mxConstants.STYLE_SHAPE,mxConstants.SHAPE_DOUBLE_ELLIPSE);
		style.put(mxConstants.STYLE_SHAPE,mxConstants.SHAPE_RHOMBUS);
		//style.put(mxConstants.STYLE_SHAPE,mxConstants.SHAPE_IMAGE);
		
		
		graph.getModel().beginUpdate();
		try
		{
			mxCell v1 = (mxCell)graph.insertVertex(parent, null, "", 40, 40, 40,40);
			
			Object v2 = graph.insertVertex(parent, null, "World!", 40, 120,80, 30,"shape=label;rounded=true;gradientcolor=#FFFFFF;shadow=true;image=/com/mxgraph/examples/swing/images/gear.png"); 
			graph.insertEdge(parent, null, "Edge", v1, v2);
		}
		finally
		{
			graph.getModel().endUpdate();
		}


		// Creates an image than can be saved using ImageIO
		BufferedImage image = mxCellRenderer.createBufferedImage(graph, null,
				1, Color.WHITE, true, null);
		
		mxGraphComponent graphComponent = new mxGraphComponent(graph);
		getContentPane().add(graphComponent);
	}
	
	

	public static void main(String[] args)
	{
		HelloWorld frame = new HelloWorld();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(400, 320);
		frame.setVisible(true);
	}

}
