package com.mxdeploy.swt.workbook.tbitens;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
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

import com.wds.bean.listener.ListenerPort;

public class ListenerPortTabItemComposite extends Composite {
	private Text nameText;
	private Text connectionFactoryJNDINameText;
	private Text destinationJNDINameText;
	private Text maxRetriesText;
	private Text maxSessionsText;
	private Text maxMessagesText;	
	private Table table;
	private Button addButton;
	private Button removeButton;
	private Button updateButton;
	private Button cancelButton;
	private Composite composite_1;

	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public ListenerPortTabItemComposite(Composite parent, int style) {
		super(parent, style);
		GridLayout gridLayout = new GridLayout(1, false);
		setLayout(gridLayout);
		
		Composite composite = new Composite(this, SWT.BORDER);
		composite.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		GridLayout gl_composite = new GridLayout(2, false);
		gl_composite.marginWidth = 20;
		gl_composite.marginHeight = 10;
		composite.setLayout(gl_composite);
		
		Label lblName = new Label(composite, SWT.NONE);
		lblName.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblName.setText("Name :");
		
		nameText = new Text(composite, SWT.BORDER);
		GridData gd_nameText = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
		gd_nameText.widthHint = 196;
		nameText.setLayoutData(gd_nameText);
		
		Label lblConnectionFactoryJndi = new Label(composite, SWT.NONE);
		lblConnectionFactoryJndi.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblConnectionFactoryJndi.setText("Connection Factory JNDI Name :");
		
		connectionFactoryJNDINameText = new Text(composite, SWT.BORDER);
		connectionFactoryJNDINameText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblDestinationJndiName = new Label(composite, SWT.NONE);
		lblDestinationJndiName.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblDestinationJndiName.setText("Destination JNDI Name :");
		
		destinationJNDINameText = new Text(composite, SWT.BORDER);
		destinationJNDINameText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		composite_1 = new Composite(this, SWT.BORDER);
		composite_1.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		GridLayout gl_composite_1 = new GridLayout(6, false);
		gl_composite_1.marginWidth = 20;
		gl_composite_1.marginHeight = 10;
		composite_1.setLayout(gl_composite_1);
		
		Label lblMaxRetries = new Label(composite_1, SWT.NONE);
		lblMaxRetries.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblMaxRetries.setText("Max Retries :");
		
		maxRetriesText = new Text(composite_1, SWT.BORDER);
		maxRetriesText.setText("5");
		maxRetriesText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		maxRetriesText.addVerifyListener(new VerifyListener() {
			public void verifyText(VerifyEvent e) {
				if(!verifyNumber(e))
					e.doit = false;					
			}
		});
		
		Label lblMaxSessions = new Label(composite_1, SWT.NONE);
		lblMaxSessions.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblMaxSessions.setText("Max Sessions :");
		
		maxSessionsText = new Text(composite_1, SWT.BORDER);
		maxSessionsText.setText("1");
		maxSessionsText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		maxSessionsText.addVerifyListener(new VerifyListener() {
			public void verifyText(VerifyEvent e) {
				if(!verifyNumber(e))
					e.doit = false;					
			}
		});
		
		Label lblMaxMessages = new Label(composite_1, SWT.NONE);
		lblMaxMessages.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblMaxMessages.setText("Max Messages :");
		
		maxMessagesText = new Text(composite_1, SWT.BORDER);
		maxMessagesText.setText("5");
		maxMessagesText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		maxMessagesText.addVerifyListener(new VerifyListener() {
			public void verifyText(VerifyEvent e) {
				if(!verifyNumber(e))
					e.doit = false;					
			}
		});
		
		Composite composite_2 = new Composite(this, SWT.BORDER);
		composite_2.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		composite_2.setLayout(new GridLayout(4, false));
		
		addButton = new Button(composite_2, SWT.NONE);
		addButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				
				if( !validate() ){
					return;
				}
				
				if( nameExist(nameText.getText(), table.getSelectionIndex()) ){
					sendMessage("JndiName already exist !");
					return;	
				}
				TableItem tableItem= new TableItem(table, SWT.NONE);
				ListenerPort listenerPort = new ListenerPort();
				
				update(listenerPort,tableItem);				
			}
		});
		addButton.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, true, false, 1, 1));
		addButton.setText("   Add   ");
		
		updateButton = new Button(composite_2, SWT.NONE);
		updateButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				
				if( !validate() ){
					return;
				}
				
				if( nameExist(nameText.getText(), table.getSelectionIndex()) ){
					sendMessage("Name already exist !");
					return;	
				}
				TableItem tableItem = table.getItem(table.getSelectionIndex());
				ListenerPort listenerPort = (ListenerPort)tableItem.getData();
				
				update(listenerPort,tableItem);					
			}
		});
		updateButton.setText("Update");
		updateButton.setEnabled(false);
		
		removeButton = new Button(composite_2, SWT.NONE);
		removeButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if( table.getSelectionIndex() != -1 ){
					table.remove(table.getSelectionIndex());
					resetComposite();
				}					
			}
		});
		removeButton.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		removeButton.setText("Remove");
		removeButton.setEnabled(false);
		
		cancelButton = new Button(composite_2, SWT.NONE);
		cancelButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				resetComposite();
			}
		});
		cancelButton.setText("Cancel");
		
		table = new Table(this, SWT.BORDER | SWT.FULL_SELECTION);
		table.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if( table.getSelectionIndex() != -1 ){
					resetComposite();
					removeButton.setEnabled(true);
					updateButton.setEnabled(true);
					TableItem tableItem = table.getItem(table.getSelectionIndex());
					ListenerPort listenerPort = (ListenerPort)tableItem.getData();
					nameText.setText(listenerPort.getName());
					connectionFactoryJNDINameText.setText(listenerPort.getConnectionFactoryJNDIName());
					destinationJNDINameText.setText(listenerPort.getDestinationJNDIName());
					maxRetriesText.setText(listenerPort.getMaxRetries());
					maxSessionsText.setText(listenerPort.getMaxSessions());
					maxMessagesText.setText(listenerPort.getMaxMessages());
				}
			}
		});
		table.setLinesVisible(true);
		table.setHeaderVisible(true);
		table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		TableColumn tableColumn = new TableColumn(table, SWT.NONE);
		tableColumn.setWidth(333);
		tableColumn.setText("Name");

	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}
	
	public Table getTable() {
		return table;
	}

	public void update(ListenerPort listenerPort, TableItem tableItem){
		listenerPort.setName(nameText.getText());
		listenerPort.setConnectionFactoryJNDIName(connectionFactoryJNDINameText.getText());
		listenerPort.setDestinationJNDIName(destinationJNDINameText.getText());
		listenerPort.setMaxRetries(maxRetriesText.getText());
		listenerPort.setMaxSessions(maxSessionsText.getText());
		listenerPort.setMaxMessages(maxMessagesText.getText());

		tableItem.setText(0, nameText.getText());
		tableItem.setData(listenerPort);
		
		resetComposite();
	}
	
	private void resetComposite(){
		nameText.setText("");
		connectionFactoryJNDINameText.setText("");
		destinationJNDINameText.setText("");
		maxRetriesText.setText("5");
		maxSessionsText.setText("1");
		maxMessagesText.setText("5");
		updateButton.setEnabled(false);
		removeButton.setEnabled(false);			
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
    
    public boolean verifyNumber(VerifyEvent e){
		boolean isInteger = true;
		if( e.keyCode == 8 || e.keyCode == 127 ){
			return isInteger;
		} else {
			try{
				Integer.parseInt(e.text);
			} catch(NumberFormatException ex ){
				isInteger = false;
			}
		}
		return isInteger;
    }	
    
    public boolean nameExist(String jndiName, int index){
		for( int i=0; i < table.getItemCount(); i++){
			if( i == index){
				continue;
			}
			TableItem tableItem = table.getItem(i);
			if( tableItem.getText().trim().equals(jndiName.trim())){
				return true;
			}
		}    	
		return false;
    } 

    public boolean validate(){
		if( nameText.getText().length() == 0 ){
			sendMessage("Name can't be null !");
			return false;
		}
		
		if( connectionFactoryJNDINameText.getText().length() == 0 ){
			sendMessage("Connection Factory JNDI Name can't be null !");
			return false;
		}
		
		if( destinationJNDINameText.getText().length() == 0 ){
			sendMessage("Destination JNDI Name can't be null !");
			return false;
		}
		
		if( maxRetriesText.getText().length() == 0 ){
			sendMessage("Max Retries can't be null !");
			return false;
		}

		if( maxSessionsText.getText().length() == 0 ){
			sendMessage("Max Sessions can't be null !");
			return false;
		}

		if( maxMessagesText.getText().length() == 0 ){
			sendMessage("Max Messages can't be null !");
			return false;
		}

		return true;
    }	

}
