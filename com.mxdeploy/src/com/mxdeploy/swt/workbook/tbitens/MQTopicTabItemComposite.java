package com.mxdeploy.swt.workbook.tbitens;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;

import com.wds.bean.mq.MQTopic;

public class MQTopicTabItemComposite extends Composite {
	private Text nameText;
	private Text jndiNameText;
	private Table table;
	private Text baseTopicNameText;
	private Text wmqTopicNameText;
	private Combo scopeCombo;
	private Button removeButton;
	private Button addButton;
	private Button updateButton;
	private Button cancelButton;
	
	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public MQTopicTabItemComposite(Composite parent, int style) {
		super(parent, style);
		GridLayout gridLayout = new GridLayout(1, false);
		gridLayout.verticalSpacing = 0;
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
		
		Label lblBaseQueueManager = new Label(composite_1, SWT.NONE);
		lblBaseQueueManager.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblBaseQueueManager.setText("Base Topic Name :");
		
		baseTopicNameText = new Text(composite_1, SWT.BORDER);
		baseTopicNameText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblBaseQueueName = new Label(composite_1, SWT.NONE);
		lblBaseQueueName.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblBaseQueueName.setText("WMQ Topic Name :");
		
		wmqTopicNameText = new Text(composite_1, SWT.BORDER);
		wmqTopicNameText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Composite composite_5 = new Composite(this, SWT.BORDER);
		composite_5.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		composite_5.setLayout(new GridLayout(4, false));
		
		addButton = new Button(composite_5, SWT.NONE);
		addButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if( !validate() ){
					return;
				}
				if( jndiNameExist(jndiNameText.getText(), table.getSelectionIndex()) ){
					sendMessage("JndiName already exist !");
					return;	
				}
				MQTopic mqTopic = new MQTopic();
				TableItem tableItem= new TableItem(table, SWT.NONE);				
				updateMQTopic(mqTopic,tableItem);					
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
				MQTopic mqTopic = (MQTopic)tableItem.getData();
				updateMQTopic(mqTopic,tableItem);				
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
		
		cancelButton = new Button(composite_5, SWT.NONE);
		cancelButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				resetComposite();
			}
		});
		cancelButton.setText("Cancel");
		
		Composite composite_6 = new Composite(this, SWT.NONE);
		composite_6.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		GridLayout gl_composite_6 = new GridLayout(1, false);
		gl_composite_6.marginWidth = 0;
		composite_6.setLayout(gl_composite_6);
		
		table = new Table(composite_6, SWT.BORDER | SWT.FULL_SELECTION);
		table.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if( table.getSelectionIndex() != -1 ){
					resetComposite();
					removeButton.setEnabled(true);
					updateButton.setEnabled(true);
					TableItem tableItem = table.getItem(table.getSelectionIndex());
					MQTopic mqTopic = (MQTopic)tableItem.getData();
					scopeCombo.setText(mqTopic.getScope());
					jndiNameText.setText(mqTopic.getJndiName());
					nameText.setText(mqTopic.getName());
					wmqTopicNameText.setText(mqTopic.getWmqTopicName());
					baseTopicNameText.setText(mqTopic.getBaseTopicName());
				}
			}
		});
		table.setLinesVisible(true);
		table.setHeaderVisible(true);
		table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		TableColumn tableColumn = new TableColumn(table, SWT.NONE);
		tableColumn.setWidth(312);
		tableColumn.setText("Jndi Name");
		
		TableColumn tblclmnName = new TableColumn(table, SWT.NONE);
		tblclmnName.setWidth(388);
		tblclmnName.setText("Name");
		
		TableColumn tblclmnScope = new TableColumn(table, SWT.NONE);
		tblclmnScope.setWidth(100);
		tblclmnScope.setText("Scope");

	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}

	public void updateMQTopic(MQTopic mqTopic, TableItem tableItem){
		mqTopic.setName(nameText.getText());
		mqTopic.setJndiName(jndiNameText.getText());
		mqTopic.setScope(scopeCombo.getText());
		mqTopic.setBaseTopicName(baseTopicNameText.getText());
		mqTopic.setWmqTopicName(wmqTopicNameText.getText());

		tableItem.setText(0, jndiNameText.getText());
		tableItem.setText(1, nameText.getText());
		tableItem.setText(2, scopeCombo.getText());
		tableItem.setData(mqTopic);
		
		resetComposite();
	}
	
	private void resetComposite(){
		scopeCombo.setText("SERVER");
		nameText.setText("");
		jndiNameText.setText("");
		baseTopicNameText.setText("");
		wmqTopicNameText.setText("");		
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
		if( nameText.getText().length() == 0 ){
			sendMessage("Name can't be null !");
			return false;
		}
		
		if( jndiNameText.getText().length() == 0 ){
			sendMessage("JndiName can't be null !");
			return false;
		}	
		
		if( baseTopicNameText.getText().length() == 0 ){
			sendMessage("Base Topic Name can't be null !");
			return false;
		}

		if( wmqTopicNameText.getText().length() == 0 ){
			sendMessage("WMQ Topic Name can't be null !");
			return false;
		}

		if( scopeCombo.getText().length() == 0 ){
			sendMessage("Scope Name can't be null !");
			return false;
		}

		return true;
    }
    public Table getTable(){
    	return table;
    } 
}
