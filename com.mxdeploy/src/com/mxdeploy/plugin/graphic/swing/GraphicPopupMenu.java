package com.mxdeploy.plugin.graphic.swing;

import javax.swing.BorderFactory;
import javax.swing.JPopupMenu;
import javax.swing.TransferHandler;

import com.mxdeploy.plugin.graphic.swing.action.HistoryAction;
import com.mxdeploy.plugin.graphic.swing.action.NewAction;
import com.mxdeploy.plugin.graphic.swing.action.SaveAction;
import com.mxdeploy.plugin.graphic.swing.action.VertexAction;
import com.mxgraph.swing.util.mxGraphActions;
import com.mxgraph.util.mxResources;

public class GraphicPopupMenu extends JPopupMenu {

	private static final long serialVersionUID = -3132749140550242191L;
	
	public static String IMAGE_EVENT = "/com/mxgraph/examples/swing/images/action.gif";
	public static String IMAGE_SAVE = "/com/mxgraph/examples/swing/images/save.gif";	
	public static String IMAGE_CUT = "/com/mxgraph/examples/swing/images/cut.gif";
	public static String IMAGE_UNDO = "/com/mxgraph/examples/swing/images/undo.gif"; 
	public static String IMAGE_COPY = "/com/mxgraph/examples/swing/images/copy.gif";
	public static String IMAGE_PASTE = "/com/mxgraph/examples/swing/images/paste.gif";
	
	public static String IMAGE_DELETE = "/com/mxgraph/examples/swing/images/delete.gif";

	public GraphicPopupMenu(GraphicBasicPanel editor)	{
		
		setLightWeightPopupEnabled(false);
		setOpaque(true);
		setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

		boolean selected = !editor.getGraphComponent().getGraph().isSelectionEmpty();

		add( editor.bind(mxResources.get("new"), new VertexAction(), IMAGE_EVENT ));
		
		addSeparator();

		add( editor.bind(mxResources.get("save"), new SaveAction(), IMAGE_SAVE ));
		
		addSeparator();
		
		add( editor.bind(mxResources.get("undo"), new HistoryAction(true), IMAGE_UNDO ));

		addSeparator();

		add( editor.bind( mxResources.get("cut"), TransferHandler.getCutAction(), IMAGE_CUT) ).setEnabled(selected);
		
		add( editor.bind( mxResources.get("copy"), TransferHandler.getCopyAction(),	IMAGE_COPY)).setEnabled(selected);
		
		add(editor.bind(mxResources.get("paste"), TransferHandler.getPasteAction(),	IMAGE_PASTE ));

		addSeparator();

		add( editor.bind(mxResources.get("delete"), mxGraphActions.getDeleteAction(), IMAGE_DELETE)).setEnabled(selected);

		addSeparator();

		addSeparator();

		add( editor.bind(mxResources.get("edit"), mxGraphActions.getEditAction())).setEnabled(selected);

		addSeparator();

		add(editor.bind(mxResources.get("selectAll"), mxGraphActions.getSelectAllAction()));
	}
	
//	   public void paintComponent(final Graphics g) {
//	        g.setColor(Color.DARK_GRAY);
//	        g.fillRect(0,0,getWidth(), getHeight());
//	    }	
	   
  

}
