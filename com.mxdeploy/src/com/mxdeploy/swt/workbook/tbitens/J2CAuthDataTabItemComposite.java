package com.mxdeploy.swt.workbook.tbitens;

import java.util.ArrayList;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;

import com.mxdeploy.swt.workbook.WorkbookTabItemComposite;
import com.wds.bean.security.JAASAuthData;

public class J2CAuthDataTabItemComposite extends Composite {
	private Text aliasText;
	private Text userIDText;
	private Text passwordText;
	private Table table;
	private Button addButton;
	private Button removeButton;
	private Button updateButton;
	private Button cancelButton;
	
	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public J2CAuthDataTabItemComposite(Composite parent, int style) {
		super(parent, SWT.BORDER);
		setLayout(new GridLayout(1, false));
		
		Composite composite = new Composite(this, SWT.NONE);
		composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
		GridLayout gl_composite = new GridLayout(4, false);
		gl_composite.marginHeight = 10;
		gl_composite.marginWidth = 20;
		composite.setLayout(gl_composite);
		
		Label lblAlias = new Label(composite, SWT.NONE);
		lblAlias.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblAlias.setText("Alias :");
		
		aliasText = new Text(composite, SWT.BORDER);
		aliasText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);
		
		Label lblUserId = new Label(composite, SWT.NONE);
		lblUserId.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblUserId.setText("User ID :");
		
		userIDText = new Text(composite, SWT.BORDER);
		userIDText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblPassword = new Label(composite, SWT.NONE);
		lblPassword.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblPassword.setText("Password :");
		
		passwordText = new Text(composite, SWT.BORDER | SWT.PASSWORD);
		passwordText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Composite composite_1 = new Composite(this, SWT.BORDER);
		composite_1.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		composite_1.setLayout(new GridLayout(4, false));
		
		addButton = new Button(composite_1, SWT.NONE);
		addButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				JAASAuthData jaasAuthData = new JAASAuthData();
				if( aliasText.getText().length() == 0 ){
					sendMessage("Alias can't be null !");
					return;
				} else {
					jaasAuthData.setAlias(aliasText.getText());
				}
				
				if( aliasExist(aliasText.getText(), -1) ){
					sendMessage("Alias already exist !");
					return;	
				}				
				
				if( userIDText.getText().length() == 0 ){
					sendMessage("User can't be null !");
					return;
				} else {
					jaasAuthData.setUserId(userIDText.getText());
				}
				
				if( passwordText.getText().length() == 0 ){
					sendMessage("Password can't be null !");
					return;
				} else {
					jaasAuthData.setPassword(passwordText.getText());
				}				
				TableItem tableItem= new TableItem(table, SWT.NONE);
				tableItem.setText(0, aliasText.getText());
				tableItem.setText(1, userIDText.getText());
				tableItem.setData(jaasAuthData);
				aliasText.setText("");
				userIDText.setText("");
				passwordText.setText("");
				updateButton.setEnabled(false);
				removeButton.setEnabled(false);	
				loadAliasList(); 
			}
		});
		addButton.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, true, false, 1, 1));
		addButton.setText("   Add   ");
		
		updateButton = new Button(composite_1, SWT.NONE);
		updateButton.setEnabled(false);
		updateButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if( aliasText.getText().length() == 0 ){
					sendMessage("Alias can't be null !");
					return;
				}
				
				if( userIDText.getText().length() == 0 ){
					sendMessage("User can't be null !");
					return;
				}
				
				if( passwordText.getText().length() == 0 ){
					sendMessage("Password can't be null !");
					return;
				}
				
				if( aliasExist(aliasText.getText(), table.getSelectionIndex()) ){
					sendMessage("Alias already exist !");
					return;	
				}
				
				TableItem tableItem = table.getItem(table.getSelectionIndex());
				JAASAuthData jaasAuthData = (JAASAuthData)tableItem.getData();
				jaasAuthData.setAlias(aliasText.getText());
				jaasAuthData.setUserId(userIDText.getText());
				jaasAuthData.setPassword(passwordText.getText());
				
				tableItem.setText(0, aliasText.getText());
				tableItem.setText(1, userIDText.getText());
				tableItem.setData(jaasAuthData);
				aliasText.setText("");
				userIDText.setText("");
				passwordText.setText("");
				updateButton.setEnabled(false);
				removeButton.setEnabled(false);		
				loadAliasList();
			}
		});
		updateButton.setText("Update");
		
		removeButton = new Button(composite_1, SWT.NONE);
		removeButton.setEnabled(false);
		removeButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if( table.getSelectionIndex() != -1 ){
					removeButton.setEnabled(false);
					updateButton.setEnabled(false);
					aliasText.setText("");
					userIDText.setText("");
					passwordText.setText("");
					table.remove(table.getSelectionIndex());
					loadAliasList();
				}				
			}
		});
		removeButton.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		removeButton.setText("Remove");
		
		cancelButton = new Button(composite_1, SWT.NONE);
		cancelButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				addButton.setEnabled(true);
				updateButton.setEnabled(false);
				removeButton.setEnabled(false);
				aliasText.setText("");
				userIDText.setText("");
				passwordText.setText("");		
			}
		});
		cancelButton.setText("Cancel");
		
		Composite composite_2 = new Composite(this, SWT.NONE);
		composite_2.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		GridLayout gl_composite_2 = new GridLayout(1, false);
		gl_composite_2.marginWidth = 0;
		gl_composite_2.marginHeight = 0;
		composite_2.setLayout(gl_composite_2);
		
		table = new Table(composite_2, SWT.BORDER | SWT.FULL_SELECTION);
		table.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if( table.getSelectionIndex() != -1 ){
					aliasText.setText("");
					userIDText.setText("");
					passwordText.setText("");
					removeButton.setEnabled(true);
					updateButton.setEnabled(true);
					TableItem tableItem = table.getItem(table.getSelectionIndex());
					JAASAuthData jaasAuthData = (JAASAuthData)tableItem.getData();
					aliasText.setText(jaasAuthData.getAlias());
					userIDText.setText(jaasAuthData.getUserId());
					passwordText.setText(jaasAuthData.getPassword());
				}
			}
		});
		table.setLinesVisible(true);
		table.setHeaderVisible(true);
		table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		TableColumn tableColumn = new TableColumn(table, SWT.NONE);
		tableColumn.setWidth(226);
		tableColumn.setText("Alias");
		
		TableColumn tableColumn_1 = new TableColumn(table, SWT.NONE);
		tableColumn_1.setWidth(319);
		tableColumn_1.setText("User ID");

	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}

	public Text getAliasText() {
		return aliasText;
	}

	public void setAliasText(Text aliasText) {
		this.aliasText = aliasText;
	}

	public Text getUserIDText() {
		return userIDText;
	}

	public void setUserIDText(Text userIDText) {
		this.userIDText = userIDText;
	}

	public Text getPasswordText() {
		return passwordText;
	}

	public void setPasswordText(Text passwordText) {
		this.passwordText = passwordText;
	}

	public Table getTable() {
		return table;
	}

	public void setTable(Table table) {
		this.table = table;
	}

	public Button getAddButton() {
		return addButton;
	}

	public void setAddButton(Button addButton) {
		this.addButton = addButton;
	}

	public Button getRemoveButton() {
		return removeButton;
	}

	public void setRemoveButton(Button removeButton) {
		this.removeButton = removeButton;
	}
	
    public void sendMessage(final String msg){
		Display.getDefault().asyncExec(new Runnable() {
		    public void run() {
		    	MessageBox msgBox = new MessageBox(getShell(), SWT.OK );
		    	msgBox.setMessage(msg);
		    	msgBox.open();
		    }
		});
    }	
    
    public boolean aliasExist(String nodeName, int index){
		for( int i=0; i < table.getItemCount(); i++){
			if( i == index){
				continue;
			}
			TableItem tableItem = table.getItem(i);
			if( tableItem.getText().trim().equals(nodeName.trim())){
				return true;
			}
		}    	
		return false;
    }    
    
    public void loadAliasList(){
    	WorkbookTabItemComposite.aliasList = new ArrayList<String>(); 
    	if( table.getItemCount() != -1 ){
    		for(int index=0; index < table.getItemCount(); index++){
    			TableItem tableItem = table.getItem(index);
    			WorkbookTabItemComposite.aliasList.add(tableItem.getText());
    		}
    	}
    }
	
}
