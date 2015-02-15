package com.mxterminal.swt.view;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.MenuEvent;
import org.eclipse.swt.events.MenuListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;

import com.mxdeploy.images.Constant;
import com.mxterminal.swt.util.SWTUtils;
import com.mxterminal.swt.view.helper.CommandHelper;

public class CommandComposite extends Composite implements MenuListener {

	protected CTabFolder cTabFolder = null;
	protected Composite sendCommandComposite = null;
	private   Text textArea = null; 
	protected CommandHelper helper = null;  //  @jve:decl-index=0:
	protected Composite templateComposite = null;
	protected SashForm sashForm = null;
	protected Composite tableComposite = null;
	protected Composite contentComposite = null;
	private   Table table = null;
	public Text contentTemplateTextArea = null;
	protected Menu popupMenu = null;

	public CommandComposite(Composite parent, int style) {
		super(parent, style);
		initialize();
	}

	private void initialize() { 
		helper = new CommandHelper(this);
		SWTUtils.createGridDataMaximized(this);
		SWTUtils.createGridLayoutNoMargins(this);		
		//createCTabFolder();
		createSendCommandComposite();
		setSize(new Point(300, 200));
	}

	/**
	 * This method initializes cTabFolder	
	 *
	 */
	private void createCTabFolder() {
		GridData gridData11 = new GridData();
		gridData11.grabExcessHorizontalSpace = true;
		gridData11.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		gridData11.verticalAlignment = org.eclipse.swt.layout.GridData.FILL;
		gridData11.grabExcessVerticalSpace = true;
		GridData gridData = new GridData();
		gridData.grabExcessHorizontalSpace = true;
		gridData.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		gridData.verticalAlignment = org.eclipse.swt.layout.GridData.FILL;
		gridData.grabExcessVerticalSpace = true;
		cTabFolder = new CTabFolder(this, SWT.BOTTOM | SWT.FLAT | SWT.NONE);
		cTabFolder.setUnselectedCloseVisible(false);
		cTabFolder.setUnselectedImageVisible(false);
		cTabFolder.setSimple(false); 
		
		cTabFolder.setLayoutData(gridData);
		createSendCommandComposite();
		createTemplateComposite();
		cTabFolder.setLayoutData(gridData11);
		
		CTabItem cTabItem = new CTabItem(cTabFolder, SWT.NONE);
		cTabItem.setText("Commands");
		cTabItem.setControl(sendCommandComposite);
		cTabItem.setData("sendCommandComposite");
		
		CTabItem cTabItem2 = new CTabItem(cTabFolder, SWT.NONE);
		cTabItem2.setText("How to ...");
		cTabItem2.setControl(templateComposite);
		cTabItem2.setData("templateComposite");

		cTabFolder.setSelection(cTabItem);		
		createListeners();
	}

	private void createListeners(){	
		cTabFolder.addSelectionListener(new SelectionAdapter() {
		      public void widgetSelected(SelectionEvent event) {
		    	  CTabFolder tabFolder = (CTabFolder)event.widget;
	    		  if (tabFolder.getSelection().getData() instanceof String){
	    			  String txt = (String)tabFolder.getSelection().getData();
	    			  if(txt.equals("templateComposite")){
	    				 helper.reloadTemplate();
	    			  }
	    		  }
	    	}
		 });
	}
	
	/**
	 * This method initializes sendCommandComposite	
	 *
	 */
	private void createSendCommandComposite() {
		GridData gridData1 = new GridData();
		gridData1.grabExcessHorizontalSpace = true;
		gridData1.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		gridData1.verticalAlignment = org.eclipse.swt.layout.GridData.FILL;
		gridData1.horizontalSpan = 0;
		gridData1.grabExcessVerticalSpace = true;
//		GridLayout gridLayout = new GridLayout();
//		gridLayout.numColumns = 2; 
//		gridLayout.marginHeight = 0;
//		gridLayout.marginWidth = 0;
//		gridLayout.verticalSpacing = 0;
//		gridLayout.horizontalSpacing = 0;
		sendCommandComposite = new Composite(this, SWT.NONE);
		SWTUtils.createGridDataMaximized(sendCommandComposite);
		SWTUtils.createGridLayoutNoMargins(sendCommandComposite);
//		sendCommandComposite.setLayout(gridLayout);
		sendCommandComposite.setData("sendCommandComposite");
		textArea = new Text(sendCommandComposite, SWT.MULTI | SWT.WRAP | SWT.V_SCROLL );
		textArea.setBackground(new Color(Display.getCurrent(), 255, 255, 255));
		textArea.setLayoutData(gridData1);  
		textArea.setFont(new Font(Display.getDefault(), "Courier New", 10, SWT.NORMAL));
		
		textArea.addKeyListener(new KeyAdapter() {   
			public void keyReleased(org.eclipse.swt.events.KeyEvent e) {    
				helper.keyPressed(e);
			}
		    public void keyPressed(KeyEvent e) {
		    	//helper.keyPressed(e);
		    }
		}); 
		
		textArea.addModifyListener(new org.eclipse.swt.events.ModifyListener() {
			public void modifyText(org.eclipse.swt.events.ModifyEvent e) {
				helper.setStatusModifyText(true);
			}
		});
		
		textArea.addMouseListener(new org.eclipse.swt.events.MouseAdapter() {   
			public void mouseUp(org.eclipse.swt.events.MouseEvent e) {    
				helper.mouseUp(e);
			}
		});
		textArea.addVerifyListener(new org.eclipse.swt.events.VerifyListener() {
			public void verifyText(org.eclipse.swt.events.VerifyEvent e) {
				helper.verifyText(e);
			}
		});
		

		
	}

	/**
	 * This method initializes templateComposite	
	 *
	 */
	private void createTemplateComposite() {
		templateComposite = new Composite(cTabFolder, SWT.NONE);
		createSashForm();
		SWTUtils.createGridLayoutNoMargins(templateComposite);
	}

	/**
	 * This method initializes sashForm	
	 *
	 */
	private void createSashForm() {
		sashForm = new SashForm(templateComposite, SWT.NONE);
		SWTUtils.createGridDataMaximized(sashForm);
		createTableComposite();
		createContentComposite();
		sashForm.setWeights(new int [] {35,65});
	}

	/**
	 * This method initializes tableComposite	
	 *
	 */
	private void createTableComposite() {
		GridData gridData3 = new GridData();
		gridData3.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		gridData3.grabExcessVerticalSpace = true;
		gridData3.grabExcessHorizontalSpace = true;
		gridData3.verticalAlignment = org.eclipse.swt.layout.GridData.FILL;
		GridLayout gridLayout3 = new GridLayout();
		gridLayout3.horizontalSpacing = 0;
		gridLayout3.marginWidth = 0;
		gridLayout3.verticalSpacing = 0;
		gridLayout3.makeColumnsEqualWidth = true;
		gridLayout3.marginHeight = 0;
		tableComposite = new Composite(getSashForm(), SWT.NONE);
		tableComposite.setData("tableComposite");
		tableComposite.setLayout(gridLayout3);
		table = new Table(tableComposite, SWT.FULL_SELECTION);
		table.setLinesVisible(true);
		table.setLayoutData(gridData3);
		table.setHeaderVisible(true);
		table.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
				helper.loadTextSelectItem((TableItem)e.item);
			}
		});
		TableColumn tableColumn = new TableColumn(getTable(), SWT.NONE);
		tableColumn.setWidth(220);
		tableColumn.setText("Name");
		
		popupMenu = new Menu(table);
		table.setMenu(popupMenu); 
	    
	    popupMenu.addMenuListener(this);    
		
	}

	/**
	 * This method initializes contentComposite	
	 *
	 */
	private void createContentComposite() {
		GridLayout gridLayout4 = new GridLayout();
		gridLayout4.horizontalSpacing = 0;
		gridLayout4.marginWidth = 0;
		gridLayout4.verticalSpacing = 0;
		gridLayout4.marginHeight = 0;
		GridData gridData4 = new GridData();
		gridData4.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		gridData4.grabExcessHorizontalSpace = true;
		gridData4.grabExcessVerticalSpace = true;
		gridData4.verticalAlignment = org.eclipse.swt.layout.GridData.FILL;
		contentComposite = new Composite(sashForm, SWT.NONE);
		contentComposite.setLayout(gridLayout4);
		contentComposite.setData("contentComposite");
		contentTemplateTextArea = new Text(contentComposite, SWT.MULTI | SWT.WRAP | SWT.V_SCROLL | SWT.READ_ONLY);
		contentTemplateTextArea.setEditable(true);
		contentTemplateTextArea.setEnabled(true);
		contentTemplateTextArea.setFont(new Font(Display.getDefault(), "Courier New", 10, SWT.NORMAL));
		contentTemplateTextArea.setLayoutData(gridData4);
		contentTemplateTextArea.addModifyListener(new org.eclipse.swt.events.ModifyListener() {
			public void modifyText(org.eclipse.swt.events.ModifyEvent e) {
				helper.setStatusModifyText(true);
			}
		});
		contentTemplateTextArea.addKeyListener(new KeyAdapter() {
		    public void keyPressed(KeyEvent e) {
		    	helper.keyPressed(e);
		    }
		});		
	}

	/**
	 * @return the helper
	 */
	public CommandHelper getHelper() {
		return helper;
	}

	/**
	 * @return the textArea
	 */
	public Text getTextArea() {
		return textArea;
	}

	/**
	 * @return the contentTemplateTextArea
	 */
	public Text getContentTemplateTextArea() {
		return contentTemplateTextArea;
	}

	/**
	 * @return the sashForm
	 */
	public SashForm getSashForm() {
		return sashForm;
	}

	/**
	 * @return the table
	 */
	public Table getTable() {
		return table;
	}

	public void menuHidden(MenuEvent e) {
		// TODO Auto-generated method stub
		
	}

	public void menuShown(MenuEvent e) {
		MenuItem[] items = popupMenu.getItems();
        for (int i = 0; i < items.length; i++) {
           ((MenuItem) items[i]).dispose();
        }

        MenuItem refreshMItem = new MenuItem(popupMenu, SWT.NONE);
        refreshMItem.setText("New Template");
        refreshMItem.setImage(Constant.IMAGE_SERVER_COMMAND);
        
        refreshMItem.addListener(SWT.Selection, new Listener() {
    	  public void handleEvent(Event event) {
    		  helper.newTemplate(); 
    	  }
        });
        
        if(table.getSelectionCount()>0){
            MenuItem removeMItem = new MenuItem(popupMenu, SWT.NONE);
            removeMItem.setText("Remove Template");
            removeMItem.setImage(Constant.IMAGE_REMOVE_COMMAND);
            
            removeMItem.addListener(SWT.Selection, new Listener() {
        	  public void handleEvent(Event event) {
        		  helper.removeTemplate(); 
        	  }
            });
        }
	}

	/**
	 * @param table the table to set
	 */
	public void setTable(Table table) {
		this.table = table;
	}

	/**
	 * @param textArea the textArea to set
	 */
	public void setTextArea(Text textArea) {
		this.textArea = textArea;
	}
	


}
