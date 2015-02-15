package com.mxdeploy.swt.workbook.tbitens;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;

import com.wds.bean.mq.MQQueue;

public class MQQueueTabItemComposite extends Composite {
	private Text queueManagerPortText;
	private Text queueManagerHostText;
	private Text serverConnectionChannelNameText;
	private Text nameText;
	private Text jndiNameText;
	private Table table;
	private Text baseQueueManagerNameText;
	private Text baseQueueNameText;
	private Button removeButton;
	private Button addButton;
	private Combo scopeCombo;
	private Button updateButton;
	
	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public MQQueueTabItemComposite(Composite parent, int style) {
		super(parent, style);
		GridLayout gridLayout = new GridLayout(1, false);
		gridLayout.verticalSpacing = 0;
		gridLayout.marginWidth = 0;
		gridLayout.marginHeight = 0;
		setLayout(gridLayout);
		
		Composite composite_1 = new Composite(this, SWT.NONE);
		composite_1.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		GridLayout gl_composite_1 = new GridLayout(4, false);
		gl_composite_1.marginHeight = 10;
		gl_composite_1.marginWidth = 20;
		composite_1.setLayout(gl_composite_1);
		
		Label lblScope = new Label(composite_1, SWT.NONE);
		lblScope.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblScope.setText("Scope :");
		
		scopeCombo = new Combo(composite_1, SWT.READ_ONLY);
		scopeCombo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		new Label(composite_1, SWT.NONE);
		new Label(composite_1, SWT.NONE);
		
		scopeCombo.add("SERVER");
		scopeCombo.add("CLUSTER");
		scopeCombo.select(0);		
		
		Label lblDatasourceName = new Label(composite_1, SWT.NONE);
		lblDatasourceName.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblDatasourceName.setText("Name :");
		
		nameText = new Text(composite_1, SWT.BORDER);
		nameText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblJndiname = new Label(composite_1, SWT.NONE);
		lblJndiname.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblJndiname.setText("JndiName :");
		
		jndiNameText = new Text(composite_1, SWT.BORDER);
		jndiNameText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Composite composite_6 = new Composite(this, SWT.NONE);
		composite_6.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		composite_6.setLayout(new GridLayout(1, false));
		
		Composite composite_2 = new Composite(composite_6, SWT.NONE);
		composite_2.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		GridLayout gl_composite_2 = new GridLayout(1, false);
		gl_composite_2.verticalSpacing = 0;
		gl_composite_2.marginWidth = 0;
		gl_composite_2.marginHeight = 0;
		gl_composite_2.horizontalSpacing = 0;
		composite_2.setLayout(gl_composite_2);
		
		TabFolder tabFolder = new TabFolder(composite_2, SWT.NONE);
		tabFolder.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		TabItem tbtmBaseConfiguration = new TabItem(tabFolder, SWT.NONE);
		tbtmBaseConfiguration.setText("Basic Configuration");
		
		Composite composite = new Composite(tabFolder, SWT.NONE);
		tbtmBaseConfiguration.setControl(composite);
		GridLayout gl_composite = new GridLayout(4, false);
		gl_composite.marginWidth = 20;
		gl_composite.marginHeight = 10;
		composite.setLayout(gl_composite);
		
		Label lblBaseQueueManager = new Label(composite, SWT.NONE);
		lblBaseQueueManager.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblBaseQueueManager.setText("Base Queue Manager Name :");
		
		baseQueueManagerNameText = new Text(composite, SWT.BORDER);
		baseQueueManagerNameText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblBaseQueueName = new Label(composite, SWT.NONE);
		lblBaseQueueName.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblBaseQueueName.setText("Base Queue Name :");
		
		baseQueueNameText = new Text(composite, SWT.BORDER);
		baseQueueNameText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblQueueManagerHost = new Label(composite, SWT.NONE);
		lblQueueManagerHost.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblQueueManagerHost.setText("Queue Manager Host :");
		
		queueManagerHostText = new Text(composite, SWT.BORDER);
		queueManagerHostText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblQueueManagerPort = new Label(composite, SWT.NONE);
		lblQueueManagerPort.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblQueueManagerPort.setText("Queue Manager Port :");
		
		queueManagerPortText = new Text(composite, SWT.BORDER);
		queueManagerPortText.setText("0");
		queueManagerPortText.addVerifyListener(new VerifyListener() {
			public void verifyText(VerifyEvent e) {
				if( !verifyNumber(e)){
					e.doit = false;
				}
			}
		});
		queueManagerPortText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblServerConnectionChannel = new Label(composite, SWT.NONE);
		lblServerConnectionChannel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblServerConnectionChannel.setText("Server Connection Channel Name :");
		
		serverConnectionChannelNameText = new Text(composite, SWT.BORDER);
		serverConnectionChannelNameText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);
		
		Composite composite_5 = new Composite(composite_6, SWT.BORDER);
		composite_5.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		composite_5.setLayout(new GridLayout(4, false));
		
		addButton = new Button(composite_5, SWT.NONE);
		addButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if( !validate() ){
					return;
				}
				if( jndiNameExist(jndiNameText.getText(), -1) ){
					sendMessage("JndiName already exist !");
					return;	
				}
				MQQueue mqQueue = new MQQueue();
				TableItem tableItem= new TableItem(table, SWT.NONE);
				updateMQQueue(mqQueue,tableItem);
			}
		});
		addButton.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, true, false, 1, 1));
		addButton.setText("   Add   ");
		
		updateButton = new Button(composite_5, SWT.NONE);
		updateButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if( !validate() ){
					return;
				}
				if( jndiNameExist(jndiNameText.getText(), table.getSelectionIndex()) ){
					sendMessage("JndiName already exist !");
					return;	
				}				
				TableItem tableItem = table.getItem(table.getSelectionIndex());
				MQQueue mqQueue = (MQQueue)tableItem.getData();
				updateMQQueue(mqQueue,tableItem);
			}
		});
		updateButton.setText("Update");
		
		removeButton = new Button(composite_5, SWT.NONE);
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
		
		Button cancelButton = new Button(composite_5, SWT.NONE);
		cancelButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				resetComposite();
			}
		});
		cancelButton.setText("Cancel");
		
		table = new Table(composite_6, SWT.BORDER | SWT.FULL_SELECTION);
		table.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if( table.getSelectionIndex() != -1 ){
					resetComposite();
					removeButton.setEnabled(true);
					updateButton.setEnabled(true);
					TableItem tableItem = table.getItem(table.getSelectionIndex());
					MQQueue mqQueue = (MQQueue)tableItem.getData();
					
					nameText.setText(mqQueue.getName());
					jndiNameText.setText(mqQueue.getJndiName());
					scopeCombo.setText(mqQueue.getScope());
					baseQueueManagerNameText.setText(mqQueue.getBaseQueueManagerName());
					baseQueueNameText.setText(mqQueue.getBaseQueueName());
					serverConnectionChannelNameText.setText(mqQueue.getServerConnectionChannelName());
					queueManagerHostText.setText(mqQueue.getQueueManagerHost());
					queueManagerPortText.setText(mqQueue.getQueueManagerPort());
				}				
			}
		});
		table.setLinesVisible(true);
		table.setHeaderVisible(true);
		table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		TableColumn tableColumn = new TableColumn(table, SWT.NONE);
		tableColumn.setWidth(220);
		tableColumn.setText("Jndi Name");
		
		TableColumn tblclmnQueueName = new TableColumn(table, SWT.NONE);
		tblclmnQueueName.setWidth(336);
		tblclmnQueueName.setText("Queue Name");
		
		TableColumn tblclmnScope = new TableColumn(table, SWT.NONE);
		tblclmnScope.setWidth(100);
		tblclmnScope.setText("Scope");

	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}

	
	public void updateMQQueue(MQQueue mqQueue, TableItem tableItem){
		mqQueue.setName(nameText.getText());
		mqQueue.setJndiName(jndiNameText.getText());
		mqQueue.setScope(scopeCombo.getText());
		mqQueue.setBaseQueueManagerName(baseQueueManagerNameText.getText());
		mqQueue.setBaseQueueName(baseQueueNameText.getText());
		mqQueue.setServerConnectionChannelName(serverConnectionChannelNameText.getText());
		mqQueue.setQueueManagerHost(queueManagerHostText.getText());
		mqQueue.setQueueManagerPort(queueManagerPortText.getText());
		
		tableItem.setText(0, jndiNameText.getText());
		tableItem.setText(1, nameText.getText());
		tableItem.setText(2, scopeCombo.getText());
		tableItem.setData(mqQueue);
		
		resetComposite();
	}
	
	public void addMQQueue(){
		MQQueue mqQueue = new MQQueue();
		validate();
		
		if( jndiNameExist(jndiNameText.getText(), -1) ){
			sendMessage("JndiName already exist !");
			return;	
		}				
		
		TableItem tableItem= new TableItem(table, SWT.NONE);
		tableItem.setText(0, jndiNameText.getText());
		tableItem.setText(1, nameText.getText());
		tableItem.setText(1, scopeCombo.getText());
		tableItem.setData(mqQueue);
		
		resetComposite();
	}	
	
	private void resetComposite(){
		scopeCombo.setText("SERVER");
		queueManagerPortText.setText("0");
		queueManagerHostText.setText("");
		serverConnectionChannelNameText.setText("");
		nameText.setText("");
		jndiNameText.setText("");
		baseQueueManagerNameText.setText("");
		baseQueueNameText.setText("");
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
    
    public boolean jndiNameExist(String jndiName, int index){
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
		if( queueManagerPortText.getText().length() == 0 ){
			sendMessage("Queue Manager Port can't be null !");
			return false;
		}

		if( queueManagerHostText.getText().length() == 0 ){
			sendMessage("Queue Manager Host can't be null !");
			return false;
		}

		if( serverConnectionChannelNameText.getText().length() == 0 ){
			sendMessage("Server Connection Channel Name can't be null !");
			return false;
		}

		if( nameText.getText().length() == 0 ){
			sendMessage("Name can't be null !");
			return false;
		}

		if( jndiNameText.getText().length() == 0 ){
			sendMessage("JndiName can't be null !");
			return false;
		}	
		
		if( baseQueueManagerNameText.getText().length() == 0 ){
			sendMessage("Base Queue Manager Name can't be null !");
			return false;
		}		
		
		if( baseQueueNameText.getText().length() == 0 ){
			sendMessage("Base Queue Name can't be null !");
			return false;
		}			
		return true;
    }
    
    public Table getTable(){
    	return table;
    }

}

