package com.mxscript.swt;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.custom.TableEditor;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.MenuEvent;
import org.eclipse.swt.events.MenuListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;
import org.eclipse.swt.widgets.TreeItem;

import xml.properties;

import com.mxdeploy.api.domain.Database;
import com.mxdeploy.images.Constant;
import com.mxdeploy.swt.MainHelper;
import com.mxdeploy.swt.MainShell;
import com.mxscript.Task;
import com.mxscript.swt.event.AddServerToTableEvent;
import com.mxscript.swt.event.BeanShellFormMenuShowEvent;
import com.mxscript.swt.event.DeleteServerFromTableEvent;
import com.mxscript.swt.event.ImportServerEvent;
import com.mxscript.swt.event.LoadPropertiesEvent;
import com.mxterminal.swt.BusyIndicator;
import com.mxterminal.swt.ToolbarLayout;
import com.mxterminal.swt.util.SWTUtils;

public class BeanShellFormComposite extends Composite implements MenuListener {

	private SashForm sashForm = null;
	private Composite tableComposite = null;
	private Composite templateComposite = null;
	private Table table = null;
	private CTabFolder cTabFolder = null;
	private StyledText styledText = null;
	protected Menu popupMenu = null;
    private boolean lockConsole = false;
    private boolean stopBeanShell = false;
    private Composite toolbarComposite;
    protected Combo   serverCombo;
    private ToolBar  toolbar = null;
    private String    hostname = null;
    private BusyIndicator busy;
    private ToolItem addServerToolItem;
    private ToolItem deleteServerToolItem;
    private ToolItem importServerToolItem;
    
    private CTabItem tabItemProperty;
    private Tree treeProperty;
    
    private TreeItem treeItemLocal;
    private TreeItem treeItemGlobal;
    
    private ToolBarViewComposite toolBarConsoleComposite = null;
    
	private properties propertiesXML = new properties();
	
	public BeanShellFormComposite(Composite parent, int style) {
		super(parent, style);
		initialize();
	}

	private void initialize() {
		GridLayout gridLayout = new GridLayout();
		gridLayout.horizontalSpacing = 0;
		gridLayout.marginWidth = 0;
		gridLayout.verticalSpacing = 0;
		gridLayout.marginHeight = 0;
		createSashForm();
		this.setLayout(gridLayout);
		setSize(new Point(300, 200));
		
		popupMenu = new Menu(treeProperty);
		treeProperty.setMenu(popupMenu); 
	    
	    popupMenu.addMenuListener(this); 	    

	}

	/**
	 * This method initializes sashForm	
	 *
	 */
	private void createSashForm() {
		GridData gridData = new GridData();
		gridData.grabExcessHorizontalSpace = true;
		gridData.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		gridData.verticalAlignment = org.eclipse.swt.layout.GridData.FILL;
		gridData.grabExcessVerticalSpace = true;
		sashForm = new SashForm(this, SWT.NONE);
		sashForm.setOrientation(SWT.VERTICAL);
		sashForm.setLayoutData(gridData);
		createTableComposite();
		createTemplateComposite();
	}

	/**
	 * This method initializes tableComposite	
	 *
	 */
	private void createTableComposite() {
		GridData gridData1 = new GridData();
		gridData1.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		gridData1.grabExcessVerticalSpace = true;
		gridData1.grabExcessHorizontalSpace = true;
		gridData1.verticalAlignment = org.eclipse.swt.layout.GridData.FILL;
		GridLayout gridLayout1 = new GridLayout();
		gridLayout1.horizontalSpacing = 0;
		gridLayout1.marginWidth = 0;
		gridLayout1.verticalSpacing = 0;
		gridLayout1.marginHeight = 0; 
		
		tableComposite = new Composite(sashForm, SWT.BORDER);
		tableComposite.setLayout(gridLayout1);
		
		createToolbarComposite();
		
		table = new Table(tableComposite, SWT.FULL_SELECTION);
		table.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if ( getTable().getSelection() != null && getTable().getSelection().length > 0 ){
					Object obj = getTable().getSelection()[0].getData();
					if ( obj != null && ( obj instanceof Task) ){
						Task task = (Task)obj;
						if ( task.isAlive() ){
							getToolBarConsoleComposite().getStopSelectedToolItem().setEnabled(true);
						} else {
							getToolBarConsoleComposite().getStopSelectedToolItem().setEnabled(false);
						}
					}
				} else {
					getToolBarConsoleComposite().getStopSelectedToolItem().setEnabled(false);
				}
				
			}
		});
		table.setLinesVisible(true);
		table.setLayoutData(gridData1);
		table.setHeaderVisible(true);
		table.setSize(300, 15);
		TableColumn tcolum = new TableColumn(table,SWT.NONE);
		tcolum.setText("Server");
		tcolum.setWidth(200);
		
		tcolum = new TableColumn(table,SWT.NONE);
		tcolum.setText("Status");
		tcolum.setWidth(100);		
		
		final TableEditor editor = new TableEditor(table);
		editor.horizontalAlignment = SWT.LEFT;
	    editor.grabHorizontal = true;
	    editor.setColumn(1);
	    
		table.addKeyListener(new KeyAdapter() {
	        public void keyPressed(KeyEvent event) {
	          // Make sure one and only one item is selected when F2 is pressed
	          if (event.keyCode == SWT.F2 && table.getSelectionCount() == 1) {
	            // Determine the item to edit
	            final TableItem item = table.getSelection()[0];

	            // Create a text field to do the editing
	            final Text text = new Text(table, SWT.NONE);
	            text.setText(item.getText(0));
	            text.selectAll();
	            text.setFocus();

	            // If the text field loses focus, set its text into the tree
	            // and end the editing session
	            text.addFocusListener(new FocusAdapter() {
	              public void focusLost(FocusEvent event) {
	                //item.setText(text.getText());
	                text.dispose();
	              }
	            });

	            // If they hit Enter, set the text into the tree and end the editing
	            // session. If they hit Escape, ignore the text and end the editing
	            // session
	            text.addKeyListener(new KeyAdapter() {
	              public void keyPressed(KeyEvent event) {
	                switch (event.keyCode) {
	                case SWT.CR:
	                  // Enter hit--set the text into the tree and drop through
	                  //item.setText(text.getText());
	                case SWT.ESC:
	                  // End editing session
	                  text.dispose();
	                  break;
	                }
	              }
	            });
 
	            // Set the text field into the editor
	            editor.setEditor(text, item,0);
	          }
	        }
	      });
	
	}

	/**
	 * This method initializes templateComposite	
	 *
	 */
	private void createTemplateComposite() {
		GridLayout gridLayout2 = new GridLayout();
		gridLayout2.horizontalSpacing = 0;
		gridLayout2.marginWidth = 0;
		gridLayout2.verticalSpacing = 0;
		gridLayout2.marginHeight = 0;
		templateComposite = new Composite(sashForm, SWT.NONE);
		createCTabFolder();
		templateComposite.setLayout(gridLayout2);
		createTabItem();
	}
	
	private void createTabItem(){
		CTabItem templateTabItem = new CTabItem(cTabFolder,SWT.NONE);
		templateTabItem.setText("Console  ");
		templateTabItem.setImage(Constant.IMAGE_CONSOLE);
		
		styledText = new StyledText(cTabFolder, SWT.MULTI | SWT.WRAP | SWT.V_SCROLL | SWT.BORDER);
		styledText.setFont(new Font(Display.getDefault(), "Arial", 10, SWT.NORMAL));
		styledText.addModifyListener(new org.eclipse.swt.events.ModifyListener() {
			public void modifyText(org.eclipse.swt.events.ModifyEvent e) {
				if(!lockConsole){
				   styledText.setTopIndex(styledText.getLineCount()-1);
				}
			}
		});
		styledText.setEditable(false);
		
		SWTUtils.createGridLayoutNoMargins(styledText);
		SWTUtils.createGridDataMaximized(styledText);		
				
	    templateTabItem.setControl(styledText);	
	    cTabFolder.setSelection(templateTabItem);	
	    
	    tabItemProperty = new CTabItem(cTabFolder,SWT.NONE);	
		tabItemProperty.setText("Properties  ");
	    tabItemProperty.setImage(Constant.IMAGE_PROPERTY);
	    
	    treeProperty = new Tree(cTabFolder, SWT.FULL_SELECTION | SWT.SINGLE);
	    treeProperty.setData(propertiesXML);
	    
	    treeProperty.setLinesVisible(true);
	    treeProperty.setHeaderVisible(true);
		TreeColumn treeColumn = new TreeColumn(treeProperty, SWT.NONE);
		treeColumn.setWidth(300);
		treeColumn.setText("Name");
		TreeColumn treeColumn1 = new TreeColumn(treeProperty, SWT.NONE);
		treeColumn1.setWidth(300);
		treeColumn1.setText("Value");
		
		treeItemGlobal = new TreeItem(treeProperty, SWT.NONE);
	    treeItemGlobal.setText(0,"Global" );
	    treeItemGlobal.setImage(Constant.IMAGE_PROPERTY);
	    
	    treeItemLocal = new TreeItem(treeProperty, SWT.NONE);
	    treeItemLocal.setText(0,"Local" );
	    treeItemLocal.setImage(Constant.IMAGE_PROPERTY);
	    
	    tabItemProperty.setControl(treeProperty);
	    
	}

	/**
	 * This method initializes cTabFolder	
	 *
	 */
	private void createCTabFolder() {
		GridData gridData2 = new GridData();
		gridData2.grabExcessHorizontalSpace = true;
		gridData2.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		gridData2.verticalAlignment = org.eclipse.swt.layout.GridData.FILL;
		gridData2.grabExcessVerticalSpace = true;
		cTabFolder = new CTabFolder(templateComposite, SWT.NONE);
		cTabFolder.setLayoutData(gridData2);
		cTabFolder.setMinimized(false);
		cTabFolder.setMinimizeVisible(false);
		cTabFolder.setMRUVisible(false);
		cTabFolder.setLayoutData(gridData2);
		cTabFolder.setUnselectedImageVisible(true);
		cTabFolder.setUnselectedCloseVisible(true);
		cTabFolder.setSimple(false);	
		
	    toolBarConsoleComposite = new ToolBarViewComposite(cTabFolder, SWT.NONE);
	    		
		cTabFolder.setTopRight(toolBarConsoleComposite, SWT.FILL);
		if( !System.getProperty("os.name").equals("Linux") ){ 
			cTabFolder.setTabHeight(26);
		}		
		Display display = MainShell.sShell.getDisplay();
		cTabFolder.setSelectionBackground(new Color[] {
				display.getSystemColor(SWT.COLOR_LIST_BACKGROUND),
				display.getSystemColor(SWT.COLOR_LIST_BACKGROUND),
				display.getSystemColor(SWT.COLOR_WIDGET_BACKGROUND)
			}, new int[] {	10,	90	}, true);			
	}
	
	/**
	 * @return the cTabFolder
	 */
	public CTabFolder getCTabFolder() {
		return cTabFolder;
	}

	/**
	 * @return the sashForm
	 */
	public SashForm getSashForm() {
		return sashForm;
	}

	/**
	 * @return the styledText
	 */
	public StyledText getStyledText() {
		return styledText;
	}

	/**
	 * @return the table
	 */
	public Table getTable() {
		return table;
	}

	/**
	 * @return the tableComposite
	 */
	public Composite getTableComposite() {
		return tableComposite;
	}

	/**
	 * @return the templateComposite
	 */
	public Composite getTemplateComposite() {
		return templateComposite;
	}

	/**
	 * @return the toolBarConsoleComposite
	 */
	public ToolBarViewComposite getToolBarConsoleComposite() {
		return toolBarConsoleComposite;
	}

	public void menuHidden(MenuEvent e) {
		// TODO Auto-generated method stub
		
	}

	public void menuShown(MenuEvent e) {
		stopBeanShell = false;
		( new BeanShellFormMenuShowEvent(e,popupMenu,this)).execute();
	}

	/**
	 * @return the lockConsole
	 */
	public boolean isLockConsole() {
		return lockConsole;
	}

	/**
	 * @param lockConsole the lockConsole to set
	 */
	public void setLockConsole(boolean lockConsole) {
		this.lockConsole = lockConsole;
	}

	/**
	 * @return the stopBeanShell
	 */
	public boolean isStopBeanShell() {
		return stopBeanShell;
	}

	/**
	 * @param stopBeanShell the stopBeanShell to set
	 */
	public void setStopBeanShell(boolean stopBeanShell) {
		this.stopBeanShell = stopBeanShell;
	}

	private void createToolbarComposite() {
		GridLayout gridLayout1 = new GridLayout();
		gridLayout1.horizontalSpacing = 0;
		gridLayout1.marginWidth       = 0;
		gridLayout1.verticalSpacing   = 0;
		gridLayout1.marginHeight      = 0;
		gridLayout1.numColumns        = 1;
		
		GridData gridData = new GridData();
		gridData.grabExcessHorizontalSpace = true; 
		gridData.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		
		toolbarComposite = new Composite(tableComposite, SWT.NONE);
        
		toolbarComposite.setLayout( new ToolbarLayout() );
        
		toolbarComposite.setLayoutData(new GridData( GridData.VERTICAL_ALIGN_BEGINNING | 
        		                                     GridData.FILL_HORIZONTAL));
		
		createHostNameText(toolbarComposite);
		createToolbar(toolbarComposite);
		
        if(hostname!=null && hostname.trim().length()>0){
        	serverCombo.add(hostname);
        	serverCombo.select(serverCombo.getItemCount()-1);
        	serverCombo.setEnabled(false);
        }
		
        busy = new BusyIndicator( toolbarComposite, SWT.NONE );
        busy.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_END));
        //busy.createBusyThread();
	}

    private void createHostNameText( Composite parent ) {

        Label serverNameLabel = new Label( parent, SWT.NONE );
        serverNameLabel.setText( " Add Server :" );

        this.serverCombo = new Combo(parent, SWT.DROP_DOWN | SWT.BORDER );
        
        serverCombo.addKeyListener( new KeyListener() {
           
            public void keyPressed( KeyEvent e ) {
            }

            public void keyReleased( KeyEvent e ) {
            	if( e.keyCode == SWT.CR ){ 
    				BeanShellFormComposite composite = (BeanShellFormComposite)sashForm.getParent();
    				(new AddServerToTableEvent(composite)).execute();             		
             	   return;
            	}
            	if( e.keyCode == SWT.ARROW_LEFT || e.keyCode == SWT.ARROW_RIGHT || 
            	    e.keyCode == SWT.ARROW_UP || e.keyCode == SWT.ARROW_DOWN || 
            	    e.keyCode == SWT.HOME || e.keyCode == SWT.END ||
            	    e.keyCode == SWT.DEL || e.keyCode == SWT.BS  ||
            	    e.keyCode == SWT.SHIFT || e.keyCode == SWT.CTRL ||
            	    e.keyCode == SWT.ALT ){
            		return;
            	}
	        	String[] servidores = ((Combo)e.widget).getItems(); 
	        	String text = ((Combo)e.widget).getText();
	        	for (int i = 0; i < servidores.length; i++) {
	        		if(servidores[i].startsWith(text) ){
	        			serverCombo.setText(servidores[i]); //setSelection(selection)select(i);
	        			serverCombo.setSelection(new Point(text.length(),servidores[i].length()));
	        			break;
	        		}
	        	}            	
            }
        });
        
        final String[] items = MainHelper.serverComboItems;
        if( items !=null ){
			Display.getDefault().asyncExec(new Runnable(){
				public void run(){ 	
					serverCombo.setItems(items);
				}
			});  
        }
        
        serverCombo.setFocus();
        
     

    }
	
    private ToolBar createToolbar( Composite parent ){
        toolbar = new ToolBar( parent, SWT.FLAT );
         
		addServerToolItem = new ToolItem(toolbar, SWT.PUSH);
		addServerToolItem.setImage(Constant.IMAGE_NEW_SERVER);
		addServerToolItem.setHotImage(Constant.IMAGE_NEW_SERVER);
		addServerToolItem.setToolTipText("Add Server to Table");
		addServerToolItem.setSelection(false);
		addServerToolItem.setEnabled(true);
		addServerToolItem.addSelectionListener(new org.eclipse.swt.events.SelectionListener() {
			public void widgetSelected(SelectionEvent e) {
				BeanShellFormComposite composite = (BeanShellFormComposite)sashForm.getParent();
				(new AddServerToTableEvent(composite)).execute(); 
			}
			public void widgetDefaultSelected(org.eclipse.swt.events.SelectionEvent e) {
			}
		});		

		deleteServerToolItem = new ToolItem(toolbar, SWT.PUSH);
		deleteServerToolItem.setImage(Constant.IMAGE_REMOVE_COMMAND);
		deleteServerToolItem.setHotImage(Constant.IMAGE_REMOVE_COMMAND);
		deleteServerToolItem.setToolTipText("Remove Server from Table");
		deleteServerToolItem.setSelection(false); 
		deleteServerToolItem.setEnabled(false);
		deleteServerToolItem.addSelectionListener(new org.eclipse.swt.events.SelectionListener() {
			public void widgetSelected(SelectionEvent e) {
				BeanShellFormComposite composite = (BeanShellFormComposite)sashForm.getParent();
				(new DeleteServerFromTableEvent(composite)).execute();
			}
			public void widgetDefaultSelected(org.eclipse.swt.events.SelectionEvent e) {
			}
		});		
		
		@SuppressWarnings("unused")
		ToolItem toolItem02 = new ToolItem(toolbar, SWT.SEPARATOR);
		
		importServerToolItem = new ToolItem(toolbar, SWT.PUSH);
		importServerToolItem.setImage(Constant.IMAGE_IMPORT_SERVER);
		importServerToolItem.setHotImage(Constant.IMAGE_IMPORT_SERVER);
		importServerToolItem.setToolTipText("Import Server to Table");
		importServerToolItem.setSelection(false);
		importServerToolItem.setEnabled(true);
		importServerToolItem.addSelectionListener(new org.eclipse.swt.events.SelectionListener() {
			public void widgetSelected(SelectionEvent e) {
				BeanShellFormComposite composite = (BeanShellFormComposite)sashForm.getParent();
				(new ImportServerEvent(composite)).execute();
			}
			public void widgetDefaultSelected(org.eclipse.swt.events.SelectionEvent e) {
			}
		});				

        
        return toolbar;
    }	
	
    public List<String> getHostNames() {
    	List<String>        list = null;
    	File        tmpFile;
		String      fileName     = null;
		String sshHomeDir = Database.getServerPath(); //System.getProperty("user.dir");
	  
		fileName = sshHomeDir; //+ "/hostkeys";
		tmpFile = new File(fileName);

		if(!tmpFile.exists()) {
			try {
				tmpFile.mkdir();
			} catch (Throwable t) {
				//logger.error(t.getMessage());
			}
		}

		if(!tmpFile.exists() || !tmpFile.isDirectory()) {
		  //logger.warn("No hostkeys directory");
          return null;
		}
		File[] files = tmpFile.listFiles();
		list = new ArrayList<String>();
    	for (int i = 0; i < files.length; i++) {
    		if(files[i].getName().endsWith(".xml")){
	    		//int lengh = new Long(files[i].getName().length()-4).intValue();
	    		//String hostname = files[i].getName().substring(7,lengh);
    			String name = files[i].getName().replace(".xml", "");
	    		list.add(name);
    		}
    	}    
    	return list;
    }

	/**
	 * @return the busy
	 */
	public BusyIndicator getBusy() {
		return busy;
	}

	/**
	 * @return the addServerToolItem
	 */
	public ToolItem getAddServerToolItem() {
		return addServerToolItem;
	}

	/**
	 * @return the deleteServerToolItem
	 */
	public ToolItem getDeleteServerToolItem() {
		return deleteServerToolItem;
	}

	/**
	 * @return the importServerToolItem
	 */
	public ToolItem getImportServerToolItem() {
		return importServerToolItem;
	}

	/**
	 * @return the serverCombo
	 */
	public Combo getServerCombo() {
		return serverCombo;
	}

	public CTabItem getTabItemProperty() {
		return tabItemProperty;
	}

	public Tree getTreeProperty() {
		return treeProperty;
	}

	public void loadProperties(){
		LoadPropertiesEvent loadEvent = new LoadPropertiesEvent(treeItemLocal, treeItemGlobal);
		loadEvent.execute();
	}
	
	
    
}
