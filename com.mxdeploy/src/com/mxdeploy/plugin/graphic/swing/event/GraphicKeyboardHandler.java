package com.mxdeploy.plugin.graphic.swing.event;

import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.KeyStroke;

import com.mxdeploy.plugin.graphic.swing.action.HistoryAction;
import com.mxdeploy.plugin.graphic.swing.action.NewAction;
import com.mxdeploy.plugin.graphic.swing.action.SaveAction;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.swing.handler.mxKeyboardHandler;
import com.mxgraph.swing.util.mxGraphActions;

/**
 * @author Administrator
 * 
 */
public class GraphicKeyboardHandler extends mxKeyboardHandler {

	/**
	 * 
	 * @param graphComponent
	 */
	public GraphicKeyboardHandler(mxGraphComponent graphComponent) {
		super(graphComponent);
	}

	/**
	 * Return JTree's input map.
	 */
	protected InputMap getInputMap(int condition) {
		InputMap map = super.getInputMap(condition);

		if (condition == JComponent.WHEN_FOCUSED && map != null) {
			map.put(KeyStroke.getKeyStroke("control S"), "save");
			map.put(KeyStroke.getKeyStroke("control shift S"), "saveAs");
			map.put(KeyStroke.getKeyStroke("control N"), "new");
			map.put(KeyStroke.getKeyStroke("control O"), "open");

			map.put(KeyStroke.getKeyStroke("control Z"), "undo");
			map.put(KeyStroke.getKeyStroke("control Y"), "redo");
			map.put(KeyStroke.getKeyStroke("control shift V"), "selectVertices");
			map.put(KeyStroke.getKeyStroke("control shift E"), "selectEdges");
		}

		return map;
	}

	/**
	 * Return the mapping between JTree's input map and JGraph's actions.
	 */
	protected ActionMap createActionMap() {
		ActionMap map = super.createActionMap();

		map.put("new", new NewAction());
		map.put("save", new SaveAction());
		//map.put("saveAs", new EditorActions.SaveAction(true));
		//map.put("open", new OpenAction());
		map.put("undo", new HistoryAction(true));
		map.put("redo", new HistoryAction(false));
		map.put("selectVertices", mxGraphActions.getSelectVerticesAction());
		map.put("selectEdges", mxGraphActions.getSelectEdgesAction());

		return map;
	}

}
