package com.mxdeploy.swt.explorer;

import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.MenuEvent;
import org.eclipse.swt.events.MenuListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

import com.mxdeploy.api.domain.Database;
import com.mxdeploy.api.domain.Project;
import com.mxdeploy.api.domain.Server;
import com.mxdeploy.api.service.ProjectService;
import com.mxdeploy.images.Constant;
import com.mxdeploy.plugin.event.EventHandler;
import com.mxdeploy.plugin.explorer.ChildItemMenuExplorerPlugin;
import com.mxdeploy.plugin.explorer.TopItemMenuExplorerPlugin;
import com.mxdeploy.plugin.menu.MenuBT;
import com.mxdeploy.plugin.menu.SubMenu;
import com.mxdeploy.swt.MainShell;
import com.mxdeploy.swt.explorer.event.AddInMyProjectTreeItemEventHandler;
import com.mxdeploy.swt.explorer.event.LoadAllProjectTreeEventHandler;
import com.mxdeploy.swt.explorer.event.SearchKeyPressEventHandler;
import com.mxdeploy.swt.explorer.event.SearchProjectEventHandler;
import com.mxdeploy.swt.explorer.helper.ControlPanelHelper;
import com.mxdeploy.swt.explorer.popup.PopupFactory;
import com.mxdeploy.swt.explorer.popup.event.ConnectServerEventHandler;


public class ControlPanelComposite extends Composite implements MenuListener {

	public CTabFolder cTabFolder = null;
	public CTabFolder cTabFolderBotton = null;
	public CTabItem tabItemExplorer = null;
	public CTabItem tabItemBeanShellExplorer = null;

	public CTabItem tabItemMyProject = null;
	public CTabItem tabItemAllProject = null;

	public Composite composite = null;

	public Composite myProjectComposite = null;
	public Composite allProjectComposite = null;
	public ToolBar toolBar = null;
	public Tree myProjectTree = null;
	public Tree allProjectTree = null;

	public ToolItem filterToolItem = null;
	public ToolItem collapseallToolItem = null;

	public ToolItem toolItem = null;
	public Menu popupMenu = null;

	public SashForm sashForm = null;

	public ControlPanelHelper helper = null;

	public boolean dbconnected = true;
	public Composite compositeSearch = null;
	public Label searchLabel = null;
	public Text searchText = null;
	
	public ControlPanelComposite(Composite parent, int style) {
		super(parent, style);
		this.sashForm = (SashForm) parent;
		initialize();
	}

	public ControlPanelHelper getHelper() {
		return helper;
	}

	private void initialize() {
		helper = new ControlPanelHelper(this);

		GridLayout gridLayout = new GridLayout();
		gridLayout.horizontalSpacing = 0;
		gridLayout.marginWidth = 0;
		gridLayout.verticalSpacing = 0;
		gridLayout.numColumns = 1;
		gridLayout.marginHeight = 0;

		createCTabFolder();
		this.setLayout(gridLayout);
		this.setVisible(true);
		setSize(new Point(300, 497));
	}

	/**
	 * This method initializes cTabFolder
	 * 
	 */
	private void createCTabFolder() {
		GridData gridData = new GridData();
		gridData.grabExcessHorizontalSpace = true;
		gridData.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		gridData.verticalAlignment = org.eclipse.swt.layout.GridData.FILL;
		gridData.verticalSpan = 0;
		gridData.grabExcessVerticalSpace = true;

		GridData gridData3 = new GridData();
		gridData3.grabExcessVerticalSpace = true;
		gridData3.horizontalAlignment = GridData.FILL;
		gridData3.verticalAlignment = GridData.FILL;
		gridData3.grabExcessHorizontalSpace = true;

		cTabFolder = new CTabFolder(this, SWT.NONE);
		cTabFolder.setMRUVisible(true);
		cTabFolder.setMinimizeVisible(true);
		cTabFolder.setMinimized(false);
		cTabFolder.setLayoutData(gridData);
		cTabFolder.setUnselectedImageVisible(false);
		cTabFolder.setUnselectedCloseVisible(false);
		cTabFolder.setSimple(false);

		createComposite();

		cTabFolder.addCTabFolder2Listener(new org.eclipse.swt.custom.CTabFolder2Adapter() {
					public void minimize(
							org.eclipse.swt.custom.CTabFolderEvent e) {
						MainShell.getCTopTabFolder().maximize();
					}
				});
		tabItemExplorer = new CTabItem(cTabFolder, SWT.NONE);
		tabItemExplorer.setText("Control Panel");
		tabItemExplorer.setImage(Constant.IMAGE_MODULE_ITEM);
		tabItemExplorer.setControl(composite);

		cTabFolder.setSelection(tabItemExplorer);
		
	}
	

	private void createComposite() {
		GridLayout gridLayout1 = new GridLayout();
		gridLayout1.horizontalSpacing = 0;
		gridLayout1.marginWidth = 0;
		gridLayout1.verticalSpacing = 0;
		gridLayout1.numColumns = 1;
		gridLayout1.marginHeight = 0;

		GridData gridData2 = new GridData();
		gridData2.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		gridData2.grabExcessHorizontalSpace = true;
		gridData2.grabExcessVerticalSpace = true;
		gridData2.verticalAlignment = org.eclipse.swt.layout.GridData.FILL;

		composite = new Composite(cTabFolder, SWT.NONE);
		composite.setData("projectExplorer");
		composite.setVisible(true);
		composite.setLayout(gridLayout1);

		cTabFolderBotton = new CTabFolder(composite, SWT.BOTTOM);
		cTabFolderBotton.setMinimized(false);
		cTabFolderBotton.setMinimizeVisible(false);
		cTabFolderBotton.setMRUVisible(false);
		cTabFolderBotton.setLayoutData(gridData2);
		cTabFolderBotton.setUnselectedImageVisible(false);
		cTabFolderBotton.setUnselectedCloseVisible(false);
		cTabFolderBotton.setSimple(false);
		cTabFolderBotton.setSize(200, 200);

		cTabFolderBotton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				CTabFolder tabFolder = (CTabFolder) event.widget;
				if (tabFolder.getSelection().getText().equals("All")) {
					LoadAllProjectTreeEventHandler.execute();
					searchText.setText("");
					searchText.setFocus();
				}
			}
		});

		tabItemMyProject = new CTabItem(cTabFolderBotton, SWT.NONE);
		tabItemMyProject.setText("Workspace");

		myProjectComposite = new Composite(cTabFolderBotton, SWT.NONE);
		tabItemMyProject.setControl(myProjectComposite);

		GridLayout gridLayout2 = new GridLayout();
		gridLayout2.horizontalSpacing = 0;
		gridLayout2.marginWidth = 0;
		gridLayout2.verticalSpacing = 0;
		gridLayout2.numColumns = 1;
		gridLayout2.marginHeight = 0;

		GridData gridData3 = new GridData();
		gridData3.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		gridData3.grabExcessHorizontalSpace = true;
		gridData3.grabExcessVerticalSpace = true;
		gridData3.verticalAlignment = org.eclipse.swt.layout.GridData.FILL;

		myProjectComposite.setLayout(gridLayout2);
		myProjectComposite.setLayoutData(gridData3);

		cTabFolderBotton.setSelection(tabItemMyProject);

		GridData gridData4 = new GridData();
		gridData4.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		gridData4.grabExcessHorizontalSpace = true;
		gridData4.grabExcessVerticalSpace = true;
		gridData4.verticalAlignment = org.eclipse.swt.layout.GridData.FILL;

		myProjectTree = new Tree(myProjectComposite, SWT.MULTI);
		myProjectTree.setHeaderVisible(false);
		myProjectTree.setLinesVisible(false);
		myProjectTree.setLayoutData(gridData4);

		myProjectTree
				.addMouseListener(new org.eclipse.swt.events.MouseAdapter() {
					public void mouseDoubleClick(MouseEvent e) {
						Database.usePassSaved = true;
						try {
							(new ConnectServerEventHandler()).execute();
						} catch (Exception ex) {
							MainShell.sendMessage(ex.getMessage(), SWT.ERROR);
						}
					}

					public void mouseDown(MouseEvent event) {
						if (event.button == 3) {
							myProjectTree.setMenu(getMenuProjectTree());
						}
					}

				});
		myProjectTree.addKeyListener(new org.eclipse.swt.events.KeyAdapter() {
			public void keyPressed(org.eclipse.swt.events.KeyEvent e) {
				if (e.keyCode == SWT.CR) {
					Tree tree = (Tree) e.widget;
					if (tree.getSelectionCount() > 0) {
						if (tree.getSelection()[0].getData() != null
								&& tree.getSelection()[0].getData() instanceof Server) {
							(new ConnectServerEventHandler()).execute();
						}
					}
				}
			}
		});

		Menu menu = new Menu(myProjectTree);

		MenuItem editProjectSubMenu = new MenuItem(menu, SWT.PUSH);
		editProjectSubMenu.setText("Edit Project");
		editProjectSubMenu.setImage(Constant.IMAGE_APPLICATION);

		myProjectTree.setMenu(getMenuProjectTree());

		tabItemAllProject = new CTabItem(cTabFolderBotton, SWT.NONE);
		tabItemAllProject.setText("All Projects");

		GridLayout gridLayout5 = new GridLayout();
		gridLayout5.horizontalSpacing = 0;
		gridLayout5.marginWidth = 0;
		gridLayout5.verticalSpacing = 0;
		gridLayout5.numColumns = 1;
		gridLayout5.marginHeight = 0;

		GridData gridData5 = new GridData();
		gridData5.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		gridData5.grabExcessHorizontalSpace = true;
		gridData5.grabExcessVerticalSpace = true;
		gridData5.verticalAlignment = org.eclipse.swt.layout.GridData.FILL;

		allProjectComposite = new Composite(cTabFolderBotton, SWT.NONE);
		allProjectComposite.setLayout(gridLayout5);
		allProjectComposite.setLayoutData(gridData5);

		tabItemAllProject.setControl(allProjectComposite);

		GridData gridData6 = new GridData();
		gridData6.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		gridData6.grabExcessHorizontalSpace = true;
		gridData6.grabExcessVerticalSpace = true;
		gridData6.verticalAlignment = org.eclipse.swt.layout.GridData.FILL;

		allProjectTree = new Tree(allProjectComposite, SWT.SINGLE);
		allProjectTree.setHeaderVisible(false);
		allProjectTree.setLinesVisible(false);
		allProjectTree.setLayoutData(gridData6);
		createCompositeSearch();

		allProjectTree
				.addMouseListener(new org.eclipse.swt.events.MouseAdapter() {
					public void mouseDoubleClick(MouseEvent e) {
						Project project = helper.getProjectSelectedInAllProjectTree();
				    	ProjectService service = new ProjectService();

				    	project = service.obtemProject(project.getId());
				    	project.setIsOpen(new Integer(1));
				    	
						service.update(project);
						AddInMyProjectTreeItemEventHandler.execute(project);
						
						cTabFolderBotton.setSelection(tabItemMyProject);
					}
				});
	}

	public Menu getMenuProjectTree() {
		Menu menu = new Menu(myProjectTree);

		MenuBT menuBt = null;

		if (myProjectTree.getItemCount() == 0 && MainShell.getSashFormComposite() != null) {
			menuBt = PopupFactory.createPopup();
		} else if (myProjectTree.getSelection().length > 0) {

			TreeItem item = myProjectTree.getSelection()[0];

			if (item.getData() instanceof Project) {
				menuBt = PopupFactory.createPopupProject();
			} else if (item.getData() instanceof List) {
				menuBt = PopupFactory.createPopupList();
				TopItemMenuExplorerPlugin.addTopItemMenuExplorer(menuBt);
			} else if (item.getData() instanceof Server) {
				menuBt = PopupFactory.createPopupServer();
			} else {
				menuBt = PopupFactory.createPopup();
				ChildItemMenuExplorerPlugin.addSubItemMenuExplorer(menuBt);
			}

		}

		if (menuBt != null) {
			List<SubMenu> subMenuList = (List<SubMenu>) menuBt.getSubMenuList();

			for (SubMenu subMenu : subMenuList) {
				if (subMenu.isCreateSeparatorBefore()) {
					@SuppressWarnings("unused")
					MenuItem sepMItem = new MenuItem(menu, SWT.SEPARATOR);
				}
				MenuItem menuItem = new MenuItem(menu, SWT.NONE);
				menuItem.setText(subMenu.getText());
				menuItem.setImage(subMenu.getImage());
				menuItem.setData("EventHandler", subMenu.getEventHandler());
				menuItem.setEnabled(subMenu.isEnabled());

				if (subMenu.getEventHandler() != null) {
					menuItem.addListener(SWT.Selection, new Listener() {
						public void handleEvent(Event event) {
							EventHandler eventHandler = (EventHandler) event.widget
									.getData("EventHandler");
							eventHandler.execute();
						}
					});
				}
			}
		}
		return menu;
	}

	public void menuHidden(MenuEvent e) {
	}

	public void menuShown(MenuEvent e) {
		MenuItem[] items = popupMenu.getItems();
		for (int i = 0; i < items.length; i++) {
			((MenuItem) items[i]).dispose();
		}

		MenuBT menuBt = null;

		if (myProjectTree.getSelection().length > 0) {

			TreeItem item = myProjectTree.getSelection()[0];

			if (item.getData() instanceof Project) {
				menuBt = PopupFactory.createPopupProject();
			} else if (item.getData() instanceof List) {
				menuBt = PopupFactory.createPopupList();
				TopItemMenuExplorerPlugin.addTopItemMenuExplorer(menuBt);
			} else if (item.getData() instanceof Server) {
				menuBt = PopupFactory.createPopupServer();
			} else {
				menuBt = PopupFactory.createPopup();
				ChildItemMenuExplorerPlugin.addSubItemMenuExplorer(menuBt);
			}

		}

		if (menuBt == null) {
			menuBt = PopupFactory.createPopup();
		}

		for (SubMenu subMenu : menuBt.getSubMenuList()) {
			if (subMenu.isCreateSeparatorBefore()) {
				@SuppressWarnings("unused")
				MenuItem sepMItem = new MenuItem(popupMenu, SWT.SEPARATOR);
			}
			MenuItem menuItem = new MenuItem(popupMenu, SWT.NONE);
			menuItem.setText(subMenu.getText());
			menuItem.setImage(subMenu.getImage());
			menuItem.setData("EventHandler", subMenu.getEventHandler());
			menuItem.setEnabled(subMenu.isEnabled());

			if (subMenu.getEventHandler() != null) {
				menuItem.addListener(SWT.Selection, new Listener() {
					public void handleEvent(Event event) {
						EventHandler eventHandler = (EventHandler) event.widget
								.getData("EventHandler");
						eventHandler.execute();
					}
				});
			}
		}
	}

	public void setFocusTabFolder() {
		cTabFolder.setFocus();
	}

	/**
	 * This method initializes compositeSearch
	 * 
	 */
	private void createCompositeSearch() {
		GridData gridData4 = new GridData();
		gridData4.grabExcessHorizontalSpace = true;
		gridData4.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		GridLayout gridLayout2 = new GridLayout();
		gridLayout2.numColumns = 2;
		GridData gridData1 = new GridData();
		gridData1.grabExcessHorizontalSpace = true;
		gridData1.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		compositeSearch = new Composite(allProjectComposite, SWT.NONE);
		compositeSearch.setLayoutData(gridData1);
		compositeSearch.setLayout(gridLayout2);
		searchLabel = new Label(compositeSearch, SWT.NONE);
		searchLabel.setText("Search");
		searchText = new Text(compositeSearch, SWT.BORDER);
		searchText.setLayoutData(gridData4);
		searchText
				.addModifyListener(new org.eclipse.swt.events.ModifyListener() {
					public void modifyText(org.eclipse.swt.events.ModifyEvent e) {
						SearchProjectEventHandler.execute(e);
					}
				});
		searchText.addKeyListener(new org.eclipse.swt.events.KeyAdapter() {
			public void keyPressed(org.eclipse.swt.events.KeyEvent e) {
				SearchKeyPressEventHandler.execute(e);
			}
		});
	}


	
	
}