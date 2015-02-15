package com.mxdeploy.swt.workbook.tbitens;

import java.util.List;

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

import com.mxdeploy.swt.workbook.WorkbookTabItemComposite;
import com.wds.bean.mq.MQQueueConnectionFactory;

public class MQQueueCFTabItemComposite extends Composite {
	private Text portText;
	private Text hostText;
	private Text queueManagerText;
	private Text nameText;
	private Text jndiNameText;
	private Table table;
	private Text channelText;
	private Text connectionTimeoutCPText;
	private Text ageTimeoutCPText;
	private Text reapTimeTextCPText;
	private Text maxConnectionCPText;
	private Text minConnectionCPText;
	private Text unusedTimeoutCPText;
	private Text connectionTimeoutSPText;
	private Text ageTimeoutSPText;
	private Text reapTimeSPText;
	private Text maxConnectionSPText;
	private Text minConnectionSPText;
	private Text unusedTimeoutSPText;
	private Combo aliasCombo;
	private Button addButton;
	private Button removeButton;
	private Button cancelButton;
	private Button updateButton;
	private Combo scopeCombo;
	private Combo transportTypeText;
	
	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public MQQueueCFTabItemComposite(Composite parent, int style) {
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
		
		Composite composite_4 = new Composite(this, SWT.NONE);
		GridLayout gl_composite_4 = new GridLayout(1, false);
		gl_composite_4.marginWidth = 0;
		composite_4.setLayout(gl_composite_4);
		composite_4.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		TabFolder tabFolder = new TabFolder(composite_4, SWT.NONE);
		tabFolder.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		TabItem tbtmDatabase = new TabItem(tabFolder, SWT.NONE);
		tbtmDatabase.setText("DataBase");
		
		Composite composite = new Composite(tabFolder, SWT.NONE);
		tbtmDatabase.setControl(composite);
		GridLayout gl_composite = new GridLayout(4, false);
		gl_composite.marginWidth = 20;
		gl_composite.marginHeight = 10;
		composite.setLayout(gl_composite);
		
		Label label_1 = new Label(composite, SWT.NONE);
		label_1.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		label_1.setText("Alias :");
		
		aliasCombo = new Combo(composite, SWT.READ_ONLY);
		aliasCombo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblQueueManager = new Label(composite, SWT.NONE);
		lblQueueManager.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblQueueManager.setText("Queue Manager :");
		
		queueManagerText = new Text(composite, SWT.BORDER);
		queueManagerText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblHost = new Label(composite, SWT.NONE);
		lblHost.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblHost.setText("Host :");
		
		hostText = new Text(composite, SWT.BORDER);
		hostText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblPort = new Label(composite, SWT.NONE);
		lblPort.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblPort.setText("Port :");
		
		portText = new Text(composite, SWT.BORDER);
		portText.setText("0");
		portText.addVerifyListener(new VerifyListener() {
			public void verifyText(VerifyEvent e) {
				if(!verifyNumber(e))
					e.doit = false;
			}
		});
		portText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblTransportType = new Label(composite, SWT.NONE);
		lblTransportType.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblTransportType.setText("Transport Type :");
		
		transportTypeText = new Combo(composite, SWT.READ_ONLY);
		transportTypeText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		transportTypeText.add("BINDINGS_THEN_CLIENT");
		//transportTypeText.add("DIRECTHTTP");
		transportTypeText.add("BINDINGS");
		//transportTypeText.add("DIRECT");
		transportTypeText.add("CLIENT");
		transportTypeText.select(0);
		
		Label lblChannel = new Label(composite, SWT.NONE);
		lblChannel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblChannel.setText("Channel :");
		
		channelText = new Text(composite, SWT.BORDER);
		channelText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		TabItem tbtmConnectionPool_1 = new TabItem(tabFolder, SWT.NONE);
		tbtmConnectionPool_1.setText("Connection Pool");
		
		Composite composite_8 = new Composite(tabFolder, SWT.NONE);
		tbtmConnectionPool_1.setControl(composite_8);
		composite_8.setLayout(new GridLayout(6, false));
		
		Label lblConnectionTimeout = new Label(composite_8, SWT.NONE);
		lblConnectionTimeout.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblConnectionTimeout.setText("Connection Timeout :");
		
		connectionTimeoutCPText = new Text(composite_8, SWT.BORDER);
		connectionTimeoutCPText.setText("10");
		connectionTimeoutCPText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		connectionTimeoutCPText.addVerifyListener(new VerifyListener() {
			public void verifyText(VerifyEvent e) {
				if(!verifyNumber(e))
					e.doit = false;
			}
		});		
		Label lblAgeTimeout = new Label(composite_8, SWT.NONE);
		lblAgeTimeout.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblAgeTimeout.setText("Age Timeout :");
		
		ageTimeoutCPText = new Text(composite_8, SWT.BORDER);
		ageTimeoutCPText.setText("3600");
		ageTimeoutCPText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		ageTimeoutCPText.addVerifyListener(new VerifyListener() {
			public void verifyText(VerifyEvent e) {
				if(!verifyNumber(e))
					e.doit = false;
			}
		});			
		Label lblReapTime = new Label(composite_8, SWT.NONE);
		lblReapTime.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblReapTime.setText("Reap Time :");
		
		reapTimeTextCPText = new Text(composite_8, SWT.BORDER);
		reapTimeTextCPText.setText("120");
		reapTimeTextCPText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		reapTimeTextCPText.addVerifyListener(new VerifyListener() {
			public void verifyText(VerifyEvent e) {
				if(!verifyNumber(e))
					e.doit = false;
			}
		});			
		Label lblMaxConnection = new Label(composite_8, SWT.NONE);
		lblMaxConnection.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblMaxConnection.setText("Max Connection :");
		
		maxConnectionCPText = new Text(composite_8, SWT.BORDER);
		maxConnectionCPText.setText("10");
		maxConnectionCPText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		maxConnectionCPText.addVerifyListener(new VerifyListener() {
			public void verifyText(VerifyEvent e) {
				if(!verifyNumber(e))
					e.doit = false;
			}
		});			
		Label lblMinConnection = new Label(composite_8, SWT.NONE);
		lblMinConnection.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblMinConnection.setText("Min Connection :");
		
		minConnectionCPText = new Text(composite_8, SWT.BORDER);
		minConnectionCPText.setText("1");
		minConnectionCPText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		minConnectionCPText.addVerifyListener(new VerifyListener() {
			public void verifyText(VerifyEvent e) {
				if(!verifyNumber(e))
					e.doit = false;
			}
		});			
		Label lblUnusedTimeout = new Label(composite_8, SWT.NONE);
		lblUnusedTimeout.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblUnusedTimeout.setText("Unused Timeout :");
		
		unusedTimeoutCPText = new Text(composite_8, SWT.BORDER);
		unusedTimeoutCPText.setText("300");
		unusedTimeoutCPText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		unusedTimeoutCPText.addVerifyListener(new VerifyListener() {
			public void verifyText(VerifyEvent e) {
				if(!verifyNumber(e))
					e.doit = false;
			}
		});		
		TabItem tbtmSessionPool = new TabItem(tabFolder, SWT.NONE);
		tbtmSessionPool.setText("Session Pool");
		
		Composite composite_2 = new Composite(tabFolder, SWT.NONE);
		tbtmSessionPool.setControl(composite_2);
		composite_2.setLayout(new GridLayout(6, false));
		
		Label label = new Label(composite_2, SWT.NONE);
		label.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		label.setText("Connection Timeout :");
		
		connectionTimeoutSPText = new Text(composite_2, SWT.BORDER);
		connectionTimeoutSPText.setText("10");
		connectionTimeoutSPText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		connectionTimeoutSPText.addVerifyListener(new VerifyListener() {
			public void verifyText(VerifyEvent e) {
				if(!verifyNumber(e))
					e.doit = false;
			}
		});			
		Label label_2 = new Label(composite_2, SWT.NONE);
		label_2.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		label_2.setText("Age Timeout :");
		
		ageTimeoutSPText = new Text(composite_2, SWT.BORDER);
		ageTimeoutSPText.setText("3600");
		ageTimeoutSPText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		ageTimeoutSPText.addVerifyListener(new VerifyListener() {
			public void verifyText(VerifyEvent e) {
				if(!verifyNumber(e))
					e.doit = false;
			}
		});		
		Label label_3 = new Label(composite_2, SWT.NONE);
		label_3.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		label_3.setText("Reap Time :");
		
		reapTimeSPText = new Text(composite_2, SWT.BORDER);
		reapTimeSPText.setText("120");
		reapTimeSPText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		reapTimeSPText.addVerifyListener(new VerifyListener() {
			public void verifyText(VerifyEvent e) {
				if(!verifyNumber(e))
					e.doit = false;
			}
		});		
		Label label_4 = new Label(composite_2, SWT.NONE);
		label_4.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		label_4.setText("Max Connection :");
		
		maxConnectionSPText = new Text(composite_2, SWT.BORDER);
		maxConnectionSPText.setText("10");
		maxConnectionSPText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		maxConnectionSPText.addVerifyListener(new VerifyListener() {
			public void verifyText(VerifyEvent e) {
				if(!verifyNumber(e))
					e.doit = false;
			}
		});			
		Label label_5 = new Label(composite_2, SWT.NONE);
		label_5.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		label_5.setText("Min Connection :");
		
		minConnectionSPText = new Text(composite_2, SWT.BORDER);
		minConnectionSPText.setText("1");
		minConnectionSPText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		minConnectionSPText.addVerifyListener(new VerifyListener() {
			public void verifyText(VerifyEvent e) {
				if(!verifyNumber(e))
					e.doit = false;
			}
		});			
		Label label_6 = new Label(composite_2, SWT.NONE);
		label_6.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		label_6.setText("Unused Timeout :");
		
		unusedTimeoutSPText = new Text(composite_2, SWT.BORDER);
		unusedTimeoutSPText.setText("300");
		unusedTimeoutSPText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		unusedTimeoutSPText.addVerifyListener(new VerifyListener() {
			public void verifyText(VerifyEvent e) {
				if(!verifyNumber(e))
					e.doit = false;
			}
		});		
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
				
				if( jndiNameExist(jndiNameText.getText(), -1) ){
					sendMessage("JndiName already exist !");
					return;	
				}

				MQQueueConnectionFactory mqQueueCF = new MQQueueConnectionFactory();
				TableItem tableItem= new TableItem(table, SWT.NONE);
				updateMQQueueCF(mqQueueCF,tableItem);				
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
				MQQueueConnectionFactory mqQueueCF = (MQQueueConnectionFactory)tableItem.getData();
				
				updateMQQueueCF(mqQueueCF,tableItem);					
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
					MQQueueConnectionFactory mqQueueCF = (MQQueueConnectionFactory)tableItem.getData();
					scopeCombo.setText(mqQueueCF.getScope());
					portText.setText(mqQueueCF.getPort());
					hostText.setText(mqQueueCF.getHost());
					queueManagerText.setText(mqQueueCF.getQueueManager());
					nameText.setText(mqQueueCF.getName());
					jndiNameText.setText(mqQueueCF.getJndiName());
					channelText.setText(mqQueueCF.getChannel());
					connectionTimeoutCPText.setText(mqQueueCF.getMQConnectionPool().getConnectionTimeout());
					ageTimeoutCPText.setText(mqQueueCF.getMQConnectionPool().getAgedTimeout());
					reapTimeTextCPText.setText(mqQueueCF.getMQConnectionPool().getReapTime());
					maxConnectionCPText.setText(mqQueueCF.getMQConnectionPool().getMaxConnections());
					minConnectionCPText.setText(mqQueueCF.getMQConnectionPool().getMinConnections());
					unusedTimeoutCPText.setText(mqQueueCF.getMQConnectionPool().getUnusedTimeout());
					connectionTimeoutSPText.setText(mqQueueCF.getMQSessionPool().getConnectionTimeout());
					ageTimeoutSPText.setText(mqQueueCF.getMQSessionPool().getAgedTimeout());
					reapTimeSPText.setText(mqQueueCF.getMQSessionPool().getReapTime());
					maxConnectionSPText.setText(mqQueueCF.getMQSessionPool().getMaxConnections());
					minConnectionSPText.setText(mqQueueCF.getMQSessionPool().getMinConnections());
					unusedTimeoutSPText.setText(mqQueueCF.getMQSessionPool().getUnusedTimeout());
					transportTypeText.setText(mqQueueCF.getTransportType());
					aliasCombo.setText(mqQueueCF.getAuthDataAlias());
				}
			}
		});
		table.setLinesVisible(true);
		table.setHeaderVisible(true);
		table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		TableColumn tableColumn = new TableColumn(table, SWT.NONE);
		tableColumn.setWidth(235);
		tableColumn.setText("Jndi Name");
		
		TableColumn tblclmnName = new TableColumn(table, SWT.NONE);
		tblclmnName.setWidth(389);
		tblclmnName.setText("Name");
		
		TableColumn tblclmnScope = new TableColumn(table, SWT.NONE);
		tblclmnScope.setWidth(100);
		tblclmnScope.setText("Scope");
	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}


	public void loadAliasCombo(List<String> list){
		if( list.size() > 0 ){
			for( String alias : WorkbookTabItemComposite.aliasList){
				aliasCombo.removeAll();
				aliasCombo.add(alias);
			}
			aliasCombo.select(0);
		}
	}	
	
	private void updateMQQueueCF(MQQueueConnectionFactory mqQueueCF, TableItem tableItem){
		mqQueueCF.setName(nameText.getText());
		mqQueueCF.setJndiName(jndiNameText.getText());
		mqQueueCF.setScope(scopeCombo.getText());
		mqQueueCF.setPort(portText.getText());
		mqQueueCF.setHost(hostText.getText());
		mqQueueCF.setQueueManager(queueManagerText.getText());
		mqQueueCF.setChannel(channelText.getText());
		mqQueueCF.getMQConnectionPool().setConnectionTimeout(connectionTimeoutCPText.getText());
		mqQueueCF.getMQConnectionPool().setAgedTimeout(ageTimeoutCPText.getText());
		mqQueueCF.getMQConnectionPool().setReapTime(reapTimeTextCPText.getText());
		mqQueueCF.getMQConnectionPool().setMaxConnections(maxConnectionCPText.getText());
		mqQueueCF.getMQConnectionPool().setMinConnections(minConnectionCPText.getText());
		mqQueueCF.getMQConnectionPool().setUnusedTimeout(unusedTimeoutCPText.getText());
		mqQueueCF.getMQSessionPool().setConnectionTimeout(connectionTimeoutSPText.getText());
		mqQueueCF.getMQSessionPool().setAgedTimeout(ageTimeoutSPText.getText());
		mqQueueCF.getMQSessionPool().setReapTime(reapTimeSPText.getText());
		mqQueueCF.getMQSessionPool().setMaxConnections(maxConnectionSPText.getText());
		mqQueueCF.getMQSessionPool().setMinConnections(minConnectionSPText.getText());
		mqQueueCF.getMQSessionPool().setUnusedTimeout(unusedTimeoutSPText.getText());
		mqQueueCF.setTransportType(transportTypeText.getText());
		mqQueueCF.setXaRecoveryAuthAlias(aliasCombo.getText());
		mqQueueCF.setAuthDataAlias(aliasCombo.getText());
		mqQueueCF.setMappingAuthDataAlias(aliasCombo.getText());
		tableItem.setText(0, jndiNameText.getText());
		tableItem.setText(1, nameText.getText());
		tableItem.setText(2, scopeCombo.getText());
		tableItem.setData(mqQueueCF);
		
		resetComposite();		
	}
	
	private void resetComposite(){
		scopeCombo.setText("SERVER");
		portText.setText("0");
		hostText.setText("");
		queueManagerText.setText("");
		nameText.setText("");
		jndiNameText.setText("");
		channelText.setText("");
		connectionTimeoutCPText.setText("10");
		ageTimeoutCPText.setText("3600");
		reapTimeTextCPText.setText("120");
		maxConnectionCPText.setText("10");
		minConnectionCPText.setText("1");
		unusedTimeoutCPText.setText("300");
		connectionTimeoutSPText.setText("10");
		ageTimeoutSPText.setText("3600");
		reapTimeSPText.setText("120");
		maxConnectionSPText.setText("10");
		minConnectionSPText.setText("1");
		unusedTimeoutSPText.setText("300");
		transportTypeText.setText("BINDINGS_THEN_CLIENT");
		
		
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
    	
		if( transportTypeText.getText().length() == 0 ){
			sendMessage("Transport Type can't be null !");
			return false;
		}

    	if( portText.getText().length() == 0 ){
			sendMessage("Port can't be null !");
			return false;
		}

		if( hostText.getText().length() == 0 ){
			sendMessage("Host can't be null !");
			return false;
		}
		
		if( queueManagerText.getText().length() == 0 ){
			sendMessage("Queue Manager can't be null !");
			return false;
		}
		if( nameText.getText().length() == 0 ){
			sendMessage("Name can't be null !");
			return false;
		}
		if( jndiNameText.getText().length() == 0 ){
			sendMessage("Jndi Name can't be null !");
			return false;
		}
		if( channelText.getText().length() == 0 ){
			sendMessage("Channel can't be null !");
			return false;
		}
		
		if( connectionTimeoutCPText.getText().length() == 0 ){
			sendMessage("Connection Pool - Connection Timeout can't be null !");
			return false;
		}
		
		if( unusedTimeoutCPText.getText().length() == 0 ){
			sendMessage("Connection Pool - Unused Timeout can't be null !");
			return false;
		}

		if( minConnectionCPText.getText().length() == 0 ){
			sendMessage("Connection Pool - Min Connection can't be null !");
			return false;
		}

		if( maxConnectionCPText.getText().length() == 0 ){
			sendMessage("Connection Pool Max Connection can't be null !");
			return false;
		}	
		
		if( ageTimeoutCPText.getText().length() == 0 ){
			sendMessage("Connection Pool Age Timeout can't be null !");
			return false;
		}		
		
		if( reapTimeTextCPText.getText().length() == 0 ){
			sendMessage("Connection Pool Reap Time can't be null !");
			return false;
		}
		
		if( connectionTimeoutSPText.getText().length() == 0 ){
			sendMessage("Connection Pool - Connection Timeout can't be null !");
			return false;
		}
		
		if( unusedTimeoutSPText.getText().length() == 0 ){
			sendMessage("Session Pool - Unused Timeout can't be null !");
			return false;
		}

		if( minConnectionSPText.getText().length() == 0 ){
			sendMessage("Session Pool - Min Connection can't be null !");
			return false;
		}

		if( maxConnectionSPText.getText().length() == 0 ){
			sendMessage("Session Pool Max Connection can't be null !");
			return false;
		}	
		
		if( ageTimeoutSPText.getText().length() == 0 ){
			sendMessage("Session Pool Age Timeout can't be null !");
			return false;
		}		
		
		if( reapTimeSPText.getText().length() == 0 ){
			sendMessage("Session Pool Reap Time can't be null !");
			return false;
		}			
		return true;
    }
    
    public Table getTable(){
    	return table;
    }    
	
}
