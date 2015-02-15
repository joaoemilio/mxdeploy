package com.mxdeploy.plugin.graphic.swing.action;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JOptionPane;

import com.mxdeploy.plugin.graphic.swing.GraphicBasicPanel;
import com.mxgraph.model.mxCell;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.util.mxResources;
import com.mxgraph.view.mxGraph;

public class VertexAction extends AbstractAction {
	
	private static final long serialVersionUID = 1L;
	private mxGraph graph;
	private Object parent;
	
	public VertexAction(){
	}
	
	public VertexAction(mxGraphComponent graphComponent){
		this.graph = graphComponent.getGraph();
		this.parent = graph.getDefaultParent();	
	}
	
	public mxCell insertStartEvent(){
		return (mxCell)graph.insertVertex(parent, null, "", 80, 10, 30,30,"shape=ellipse;fillColor=greenyellow;gradientColor=green;shadow=false");
	}
	
	public mxCell insertLabel(){
		return insertLabel(null, "", false );
	}
	
	public mxCell insertLabel(mxCell v2, String title ){
		return insertLabel(v2, title, false ); 
	}
	
	public mxCell insertLabel(mxCell v2, String title, boolean isRhombusCondition ){
		
		if( v2 == null ){
			return (mxCell)graph.insertVertex(parent, null, "New Event", 10, 10, 160, 100,"label;whiteSpace=wrap;rounded=true;shadow=true;gradientColor=#FFFFFF;shadow=true;image=/com/mxgraph/examples/swing/images/gear.png");
		}
		
		double v2X = v2.getGeometry().getX();
		double v2Y = v2.getGeometry().getY();
		
		mxCell v1 = null;
		if( v2.getStyle().contains("shape=rhombus") && isRhombusCondition ){
			v2X += 90;
			v2Y += 5; 
			v1 = (mxCell)graph.insertVertex(parent, null, title, v2X, v2Y, 100, 160,"shape=label;whiteSpace=wrap;rounded=true;shadow=true;gradientColor=#FFFFFF;shadow=true;image=/com/mxgraph/examples/swing/images/gear.png");	
		} else if ( v2.getStyle().contains("shape=ellipse;fillColor=greenyellow") ){
			v2X = 40;
			v2Y += 70;
			v1 = (mxCell)graph.insertVertex(parent, null, title, v2X, v2Y, 100, 160,"shape=label;whiteSpace=wrap;rounded=true;shadow=true;gradientColor=#FFFFFF;shadow=true;image=/com/mxgraph/examples/swing/images/gear.png");			
		} else {
			v2X = 40;
			v2Y += 90;
			v1 = (mxCell)graph.insertVertex(parent, null, title, v2X, v2Y, 100, 160,"shape=label;whiteSpace=wrap;rounded=true;shadow=true;gradientColor=#FFFFFF;shadow=true;image=/com/mxgraph/examples/swing/images/gear.png");	
		}
		
		graph.insertEdge(parent, null, "", v2, v1);
		
		return v1;
	}
	
	public mxCell insertRhombus(mxCell v2){
		
		double v2X = v2.getGeometry().getX()+30;
		double v2Y = v2.getGeometry().getY()+80;
		
//		if( v2.getGeometry().getY() > 100 ){
//			v2X += 30;
//			v2Y += 80; 
//		} else {
//			v2X += 30;
//			v2Y += 80;
//		}
		
		mxCell v1 =  (mxCell)graph.insertVertex(parent, null, "", v2X, v2Y, 30,30,"shape=rhombus;shadow=true;fillColor=white;gradientColor=white");	
		
		graph.insertEdge(parent, null, "", v2, v1);
		
		return v1;
	}
	
	public mxCell insertEndEvent(mxCell v2, String edgeTitle ){
		mxCell v1 =  null;
		
		double v2X = v2.getGeometry().getX();
		double v2Y = v2.getGeometry().getY();
		
		if( v2.getStyle().contains("shape=rhombus") ) {
			v2X += 10;
			v2Y += 90;
			v1 = (mxCell)graph.insertVertex(parent, null, "", v2X, v2Y, 30,30,"shape=image;image=/com/mxgraph/examples/swing/images/terminate.png;whiteSpace=wrap");
		} else {
			v2X += 10;
			v2Y += 80;
			v1 = (mxCell)graph.insertVertex(parent, null, "", v2X, v2Y, 30,30,"shape=image;image=/com/mxgraph/examples/swing/images/terminate.png;whiteSpace=wrap");
		}
		
		if( edgeTitle == null){
			edgeTitle = "";
		}
		
		
		graph.insertEdge(parent, null, edgeTitle, v2, v1);
		
		
		return v1;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		GraphicBasicPanel editor = GraphicAction.getEditor(e);
		graph = editor.getGraphComponent().getGraph();		
		insertLabel();
	}	

}
