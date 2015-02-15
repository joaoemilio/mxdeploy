package com.mxdeploy.plugin.graphic.swt;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MenuEvent;
import org.eclipse.swt.events.MenuListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

import com.mxdeploy.images.Constant;
import com.mxdeploy.plugin.graphic.swt.event.ActivityTableMenuEvent;

public class ActivityTableComposite extends Composite implements MenuListener {
	private Tree tree;
	private Menu popupMenu;
	
	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public ActivityTableComposite(Composite parent, int style) {
		super(parent, style);
		setLayout(new GridLayout(1, false));
		
		tree = new Tree(this, SWT.BORDER | SWT.FULL_SELECTION);
		tree.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		TreeItem item = new TreeItem(tree, SWT.NONE);
		item.setImage(new Image(Display.getCurrent(), Constant.class.getClass().getResourceAsStream("/activity/Event.png") ));
		item = new TreeItem(item, SWT.NONE);
		item.setImage(new Image(Display.getCurrent(), Constant.class.getClass().getResourceAsStream("/activity/arrow.jpg") ));
		item = new TreeItem(item, SWT.NONE);
		item.setImage(new Image(Display.getCurrent(), Constant.class.getClass().getResourceAsStream("/activity/conf.png") ));

		
		popupMenu = new Menu(tree);
		tree.setMenu(popupMenu); 
	    
		popupMenu.addMenuListener(this); 	

	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}
	
	public Tree getTree() {
		return tree;
	}

	public void setTree(Tree tree) {
		this.tree = tree;
	}

	@Override
	public void menuHidden(MenuEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void menuShown(MenuEvent e) {
		( new ActivityTableMenuEvent(e,popupMenu,this)).execute();
	}
}
