package com.mxdeploy.swt.preferences.event;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

import com.mxdeploy.plugin.event.EventHandler;
import com.mxdeploy.swt.preferences.PreferenceComposite;
import com.mxdeploy.swt.preferences.TunnelComposite;
import com.mxterminal.swt.util.SWTUtils;

public class SelectEventHandler implements EventHandler {

	private SelectionEvent event;
	private PreferenceComposite preferenceComposite;
	
	public SelectEventHandler(SelectionEvent e){
		event = e;
	}
	
	@Override
	public void execute() {
		if( event.widget instanceof Tree ){
			
			Tree tree = (Tree)event.widget;
			preferenceComposite = (PreferenceComposite)tree.getParent().getParent().getParent();
			if( tree.getSelectionCount() > 0 ){
				TreeItem treeItem = tree.getSelection()[0];
				if( treeItem.getText().equals("Tunnel") ) {
					
					TunnelComposite tunnelComposite = new TunnelComposite(preferenceComposite.getTabFolder(),SWT.NONE);
					SWTUtils.createGridDataMaximized(tunnelComposite);
					SWTUtils.createGridLayoutNoMargins(tunnelComposite);
					CTabItem tabItem = new CTabItem(preferenceComposite.getTabFolder(), SWT.NONE);
					tabItem.setControl(tunnelComposite);
					
				}
			}
		}
		
	}

}
