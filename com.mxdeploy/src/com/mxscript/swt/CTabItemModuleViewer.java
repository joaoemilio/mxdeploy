package com.mxscript.swt;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.events.MenuEvent;
import org.eclipse.swt.events.MenuListener;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;

import xml.module;

import com.mxdeploy.images.Constant;
import com.mxscript.menu.OpenModuleEventHandler;
import com.mxscript.swt.event.LoadComboBoxModuleEvent;
import com.mxscript.swt.event.LoadTreeModuleEvent;

public class CTabItemModuleViewer extends CTabItem implements MenuListener, SelectionListener, MouseListener {

	private Tree treeModule = null;
	private CCombo comboModule;
	private Text text;
	
	private Menu popupMenu = null;
	
	public CTabItemModuleViewer(CTabFolder parent, int style) {
		super(parent, style);
		initialize();
	}
	
	public void initialize(){
		setText("Module Viewer");
		setImage(Constant.IMAGE_MODULE_VIEWER);
		
		Composite moduleComposite = new Composite(getParent(), SWT.BORDER);
		setControl(moduleComposite);
		GridLayout gl_moduleComposite = new GridLayout(1, false);
		gl_moduleComposite.verticalSpacing = 0;
		gl_moduleComposite.marginWidth = 0;
		gl_moduleComposite.marginHeight = 0;
		moduleComposite.setLayout(gl_moduleComposite);
		
		Composite comboComposite = new Composite(moduleComposite, SWT.NONE);
		comboComposite.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		GridLayout gl_comboComposite = new GridLayout(1, false);
		gl_comboComposite.marginBottom = 5;
		gl_comboComposite.marginLeft = 5;
		gl_comboComposite.marginRight = 5;
		gl_comboComposite.marginTop = 5;
		gl_comboComposite.marginWidth = 0;
		gl_comboComposite.verticalSpacing = 0;
		gl_comboComposite.marginHeight = 0;
		comboComposite.setLayout(gl_comboComposite);
		
		comboModule = new CCombo(comboComposite, SWT.BORDER | SWT.FLAT);
		
		final CTabItemModuleViewer thisViewer = (CTabItemModuleViewer)this;
		
		comboModule.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				LoadTreeModuleEvent event = new LoadTreeModuleEvent( thisViewer  ); 
				event.execute();
			}
		});
		comboModule.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		
		Composite treeComposite = new Composite(moduleComposite, SWT.BORDER);
		treeComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		GridLayout gl_treeComposite = new GridLayout(1, false);
		gl_treeComposite.verticalSpacing = 0;
		gl_treeComposite.marginWidth = 0;
		gl_treeComposite.marginHeight = 0;
		treeComposite.setLayout(gl_treeComposite);
		
		treeModule = new Tree(treeComposite, SWT.BORDER);
		
		popupMenu = new Menu(treeModule);
		treeModule.setMenu(popupMenu); 
	    
	    popupMenu.addMenuListener(this); 	    
	      
	    treeModule.addMouseListener(this);
		
		treeModule.addMouseListener(new MouseAdapter() {
			public void mouseDoubleClick(MouseEvent e) {
				if( treeModule.getSelectionCount()>0){
				    module __module__ = (module)treeModule.getSelection()[0].getData();
				    (new OpenModuleEventHandler(__module__)).execute();
				}
			}
		});
		
		
		treeModule.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		Composite searchModuleComposite = new Composite(moduleComposite, SWT.NONE);
		searchModuleComposite.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		searchModuleComposite.setLayout(new GridLayout(2, false));
		
		Label lblSearch = new Label(searchModuleComposite, SWT.NONE);
		lblSearch.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblSearch.setBounds(0, 0, 65, 16);
		lblSearch.setText("Search :");
		
		text = new Text(searchModuleComposite, SWT.BORDER);
		text.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		LoadComboBoxModuleEvent event = new LoadComboBoxModuleEvent(this);
		event.execute();
	}

	public CCombo getComboModule() {
		return comboModule;
	}

	public Tree getTreeModule() {
		return treeModule;
	}

	public void setTreeModule(Tree treeModule) {
		this.treeModule = treeModule;
	}

	@Override
	public void mouseDoubleClick(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseDown(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseUp(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void widgetDefaultSelected(SelectionEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void widgetSelected(SelectionEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void menuHidden(MenuEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void menuShown(MenuEvent arg0) {
		// TODO Auto-generated method stub
		
	}	
}
