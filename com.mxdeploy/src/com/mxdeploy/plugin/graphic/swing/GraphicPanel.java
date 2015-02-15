package com.mxdeploy.plugin.graphic.swing;

import java.awt.Color;
import java.io.File;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.UIManager;

import org.w3c.dom.Document;

import com.mxgraph.io.mxCodec;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.swing.util.mxSwingConstants;
import com.mxgraph.util.mxConstants;
import com.mxgraph.util.mxUtils;
import com.mxgraph.util.mxXmlUtils;
import com.mxgraph.view.mxGraph;



public class GraphicPanel extends GraphicBasicPanel {

	/** 
	 * 
	 */
	private static final long serialVersionUID = -2707712944901661771L;

	public GraphicPanel(){
		this("GraphicPanel", new GraphicComponent( new GraphicCustom() ) );	 	
	}
	
	public GraphicPanel(String appTitle, mxGraphComponent component) {
		super(appTitle, component);
		final mxGraph graph = graphComponent.getGraph();
		
		Document document;
		try {
			document = mxXmlUtils.parseXml(mxUtils.readFile("/home/fbsantos/workspace/com.wds.plugin/Install_WAS_v855.mxe"));
			mxCodec codec = new mxCodec(document);
			codec.decode(document.getDocumentElement(),	graph.getModel());
			setCurrentFile(new File("/home/fbsantos/workspace/com.wds.plugin/Install_WAS_v855.mxe"));

			setModified(false);
			getUndoManager().clear();
			getGraphComponent().zoomAndCenter();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
//		OpenAction action = new OpenAction();
//		try {
//			action.openXmlPng(GraphicPanel.this, file);
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
//		Map<String, Object> style = graph.getStylesheet().getDefaultVertexStyle();
//		style.put(mxConstants.STYLE_IMAGE_HEIGHT,"15");
//		style.put(mxConstants.STYLE_IMAGE_WIDTH,"15");
//		style.put(mxConstants.STYLE_IMAGE_VERTICAL_ALIGN,mxConstants.ALIGN_TOP);
//		style.put(mxConstants.STYLE_VERTICAL_ALIGN,mxConstants.ALIGN_BOTTOM);		

		// Creates the shapes palette
		//GraphicPalette shapesPalette = insertPalette(mxResources.get("shapes"));

		// Sets the edge template to be used for creating new edges if an edge
		// is clicked in the shape palette
//		shapesPalette.addListener(mxEvent.SELECT, new mxIEventListener() {
//			public void invoke(Object sender, mxEventObject evt) {
//				Object tmp = evt.getProperty("transferable");
//
//				if (tmp instanceof mxGraphTransferable) 	{
//					mxGraphTransferable t = (mxGraphTransferable) tmp;
//					Object cell = t.getCells()[0];
//
//					if (graph.getModel().isEdge(cell))	{
//						((GraphicCustom)graph).setEdgeTemplate(cell);
//					}
//				}
//			}
//
//		});

		// Adds some template cells for dropping into the graph
//		ImageIcon roundedImageIcon = new ImageIcon(	GraphicPanel.class.getResource("/com/mxgraph/examples/swing/images/rounded.png"));
//		ImageIcon ellipseImageIcon = new ImageIcon(	GraphicPanel.class.getResource("/com/mxgraph/examples/swing/images/ellipse.png"));
//		ImageIcon rhombusImageIcon = new ImageIcon(	GraphicPanel.class.getResource("/com/mxgraph/examples/swing/images/ellipse.png"));
//		ImageIcon endImageIcon = new ImageIcon(	GraphicPanel.class.getResource("/com/mxgraph/examples/swing/images/terminate.png"));
//		
//		String styleStart = "shape=ellipse;fillColor=greenyellow;gradientColor=green;shadow=false";
//		String styleAction = "shape=label;rounded=true;shadow=true;gradientColor=#FFFFFF;shadow=true;image=/com/mxgraph/examples/swing/images/gear.png";
//		String styleRhombus = "shape=rhombus;shadow=true;fillColor=white;gradientColor=white";
//		String styleEnd = "roundImage;image=/com/mxgraph/examples/swing/images/terminate.png";
		 
//		shapesPalette.addTemplate("Start",ellipseImageIcon,styleStart, 30,30, "");
//		shapesPalette.addTemplate("Action",roundedImageIcon,styleAction,120, 40, "Label");
//		shapesPalette.addTemplate("Rhombus",rhombusImageIcon,styleRhombus, 50, 50, "");
//		
//		shapesPalette.addTemplate("End",endImageIcon,styleEnd,30, 30, "Terminate");
		
//		VertexAction vertex= new VertexAction(graphComponent);
//		mxCell v1 = vertex.insertStartEvent();
//		
//		mxCell v2 = vertex.insertAction(v1, "Stop JVM");
//		
//		mxCell V4_END = vertex.insertEndEvent(v2, "" );
		
	}
	
//	public GraphicPanel(String appTitle, mxGraphComponent component, boolean is) {
//		super(appTitle, component);
//		final mxGraph graph = graphComponent.getGraph();
//
//		Map<String, Object> style = graph.getStylesheet().getDefaultVertexStyle();
//		style.put(mxConstants.STYLE_IMAGE_HEIGHT,"15");
//		style.put(mxConstants.STYLE_IMAGE_WIDTH,"15");
//		style.put(mxConstants.STYLE_IMAGE_VERTICAL_ALIGN,mxConstants.ALIGN_TOP);
//		style.put(mxConstants.STYLE_VERTICAL_ALIGN,mxConstants.ALIGN_BOTTOM);
//		
//		VertexAction vertex= new VertexAction(graphComponent);
//		
//		graph.getModel().beginUpdate();
//		try	{
//			
//			mxCell v1 = vertex.insertStartEvent();
//			
//			mxCell v2 = vertex.insertAction(v1, "Stop JVM");
//			//mxCell v2 = vertex.insertAction(v1, "Stop JVM", 40, 80);
//			
//			mxCell v3 = vertex.insertRhombus( v2 );
//			
//			mxCell v3_Y = vertex.insertAction(v3, "Stop Node");
//
//			mxCell v3_N = vertex.insertAction(v3, "Kill Process", true);
//			
//			mxCell v3_N_1 = vertex.insertRhombus( v3_N );
//			
//			mxCell v4 = vertex.insertRhombus( v3_Y );
//			
//			mxCell V4_END = vertex.insertEndEvent(v4, "No" );	
//			
//						
//			
//		} finally {
//			graph.getModel().endUpdate();
//		}
//
//
//		// Creates an image than can be saved using ImageIO
//		BufferedImage image = mxCellRenderer.createBufferedImage(graph, null,
//				1, Color.WHITE, true, null);
//		
//		mxGraphComponent graphComponent = new mxGraphComponent(graph);
//		
//		add(graphComponent);
//	}
	
	public JFrame createFrame(JMenuBar menuBar) {
		JFrame frame = new JFrame();
		frame.getContentPane().add(this);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setJMenuBar(menuBar);
		frame.setSize(870, 640);
		frame.setVisible(true);

		// Updates the frame title
		//updateTitle();

		return frame;
	}	

	public static void main(String[] args)	{
		
		try
		{
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
//			UIManager.getLookAndFeelDefaults().put("PopupMenu.background", Color.GRAY);
//			UIManager.getLookAndFeelDefaults().put("Panel.background", Color.RED);
//	        UIManager.getLookAndFeelDefaults().put("List.background", Color.BLUE);
		}
		catch (Exception e1)
		{
			e1.printStackTrace();
		}

		mxSwingConstants.SHADOW_COLOR = Color.LIGHT_GRAY;
		mxConstants.W3C_SHADOWCOLOR = "#D3D3D3";
		
		GraphicPanel graphicPanel = new GraphicPanel();
		graphicPanel.createFrame(null);
		
	}

}

