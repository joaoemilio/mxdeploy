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
import com.wds.bean.mq.MQTopicConnectionFactory;

public class MQTopicCFTabItemComposite extends Composite {
	private Text portText;
	private Text hostText;
	private Text queueManagerText;
	private Text nameText;
	private Text jndiNameText;
	private Text channelText;
	private Text connectionTimeoutCPText;
	private Text ageTimeoutCPText;
	private Text reapTimeCPText;
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
	private Combo transportTypeCombo;
	private Combo scopeCombo;	
	private Table table;
	private Button removeButton;
	private Button addButton;	
	private Button updateButton;
	private Button cancelButton;
	
	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public MQTopicCFTabItemComposite(Composite parent, int style) {
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
		
		TabItem tbtmConfig = new TabItem(tabFolder, SWT.NONE);
		tbtmConfig.setText("Queue Manager");
		
		Composite composite = new Composite(tabFolder, SWT.NONE);
		tbtmConfig.setControl(composite);
		GridLayout gl_composite = new GridLayout(4, false);
		gl_composite.marginWidth = 20;
		gl_composite.marginHeight = 10;
		composite.setLayout(gl_composite);
		
		Label label_1 = new Label(composite, SWT.NONE);
		label_1.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		label_1.setText("Alias :");
		
		aliasCombo = new Combo(composite, SWT.READ_ONLY);
		aliasCombo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);
		
		Label lblHost = new Label(composite, SWT.NONE);
		lblHost.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblHost.setText("Host :");
		
		hostText = new Text(composite, SWT.BORDER);
		hostText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblPort = new Label(composite, SWT.NONE);
		lblPort.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblPort.setText("Port :");
		
		portText = new Text(composite, SWT.BORDER);
		portText.addVerifyListener(new VerifyListener() {
			public void verifyText(VerifyEvent e) {
				if(!verifyNumber(e))
					e.doit = false;					
			}
		});

		portText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblQueueManager = new Label(composite, SWT.NONE);
		lblQueueManager.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblQueueManager.setText("Queue Manager");
		
		queueManagerText = new Text(composite, SWT.BORDER);
		queueManagerText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblChannel = new Label(composite, SWT.NONE);
		lblChannel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblChannel.setText("Channel :");
		
		channelText = new Text(composite, SWT.BORDER);
		channelText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblTransportType = new Label(composite, SWT.NONE);
		lblTransportType.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblTransportType.setText("Transport Type :");
		
		transportTypeCombo = new Combo(composite, SWT.READ_ONLY);
		transportTypeCombo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		transportTypeCombo.add("BINDINGS_THEN_CLIENT");
		//transportTypeCombo.add("DIRECTHTTP");
		transportTypeCombo.add("BINDINGS");
		//transportTypeCombo.add("DIRECT");
		transportTypeCombo.add("CLIENT");
		transportTypeCombo.select(0);		
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);
		
		TabItem tbtmConnectionPool_1 = new TabItem(tabFolder, SWT.NONE);
		tbtmConnectionPool_1.setText("Connection Pool");
		
		Composite composite_8 = new Composite(tabFolder, SWT.NONE);
		tbtmConnectionPool_1.setControl(composite_8);
		GridLayout gl_composite_8 = new GridLayout(6, false);
		gl_composite_8.marginWidth = 20;
		gl_composite_8.marginHeight = 10;
		composite_8.setLayout(gl_composite_8);
		
		Label lblConnectionTimeout = new Label(composite_8, SWT.NONE);
		lblConnectionTimeout.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblConnectionTimeout.setText("Connection Timeout :");
		
		connectionTimeoutCPText = new Text(composite_8, SWT.BORDER);
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
		
		reapTimeCPText = new Text(composite_8, SWT.BORDER);
		reapTimeCPText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		reapTimeCPText.addVerifyListener(new VerifyListener() {
			public void verifyText(VerifyEvent e) {
				if(!verifyNumber(e))
					e.doit = false;					
			}
		});
		
		Label lblMaxConnection = new Label(composite_8, SWT.NONE);
		lblMaxConnection.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblMaxConnection.setText("Max Connection :");
		
		maxConnectionCPText = new Text(composite_8, SWT.BORDER);
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
		GridLayout gl_composite_2 = new GridLayout(6, false);
		gl_composite_2.marginWidth = 20;
		gl_composite_2.marginHeight = 10;
		composite_2.setLayout(gl_composite_2);
		
		Label label = new Label(composite_2, SWT.NONE);
		label.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		label.setText("Connection Timeout :");
		
		connectionTimeoutSPText = new Text(composite_2, SWT.BORDER);
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
				if( jndiNameExist(jndiNameText.getText(), table.getSelectionIndex()) ){
					sendMessage("JndiName already exist !");
					return;	
				}
				TableItem tableItem= new TableItem(table, SWT.NONE);
				MQTopicConnectionFactory mqTopicCF = new MQTopicConnectionFactory();
				updateMQTopicCF(mqTopicCF,tableItem);					
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
				MQTopicConnectionFactory mqTopicCF = (MQTopicConnectionFactory)tableItem.getData();
				updateMQTopicCF(mqTopicCF,tableItem);				
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
					MQTopicConnectionFactory mqTopicCF = (MQTopicConnectionFactory)tableItem.getData();
					scopeCombo.setText(mqTopicCF.getScope());
					portText.setText(mqTopicCF.getPort());
					hostText.setText(mqTopicCF.getHost());
					queueManagerText.setText(mqTopicCF.getQueueManager());
					nameText.setText(mqTopicCF.getName());
					jndiNameText.setText(mqTopicCF.getJndiName());
					channelText.setText(mqTopicCF.getChannel());
					connectionTimeoutCPText.setText(mqTopicCF.getMQConnectionPool().getConnectionTimeout());
					ageTimeoutCPText.setText(mqTopicCF.getMQConnectionPool().getAgedTimeout());
					reapTimeCPText.setText(mqTopicCF.getMQConnectionPool().getReapTime());
					maxConnectionCPText.setText(mqTopicCF.getMQConnectionPool().getMaxConnections());
					minConnectionCPText.setText(mqTopicCF.getMQConnectionPool().getMinConnections());
					unusedTimeoutCPText.setText(mqTopicCF.getMQConnectionPool().getUnusedTimeout());
					connectionTimeoutSPText.setText(mqTopicCF.getMQSessionPool().getConnectionTimeout());
					ageTimeoutSPText.setText(mqTopicCF.getMQSessionPool().getAgedTimeout());
					reapTimeSPText.setText(mqTopicCF.getMQSessionPool().getReapTime());
					maxConnectionSPText.setText(mqTopicCF.getMQSessionPool().getMaxConnections());
					minConnectionSPText.setText(mqTopicCF.getMQSessionPool().getMinConnections());
					unusedTimeoutSPText.setText(mqTopicCF.getMQSessionPool().getUnusedTimeout());
					transportTypeCombo.setText(mqTopicCF.getTransportType());
					aliasCombo.setText(mqTopicCF.getAuthDataAlias());
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
		
		resetComposite();

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
	
	private void updateMQTopicCF(MQTopicConnectionFactory mqTopicCF, TableItem tableItem){
		mqTopicCF.setName(nameText.getText());
		mqTopicCF.setJndiName(jndiNameText.getText());
		mqTopicCF.setScope(scopeCombo.getText());
		mqTopicCF.setPort(portText.getText());
		mqTopicCF.setHost(hostText.getText());
		mqTopicCF.setQueueManager(queueManagerText.getText());
		mqTopicCF.setChannel(channelText.getText());
		mqTopicCF.getMQConnectionPool().setConnectionTimeout(connectionTimeoutCPText.getText());
		mqTopicCF.getMQConnectionPool().setAgedTimeout(ageTimeoutCPText.getText());
		mqTopicCF.getMQConnectionPool().setReapTime(reapTimeCPText.getText());
		mqTopicCF.getMQConnectionPool().setMaxConnections(maxConnectionCPText.getText());
		mqTopicCF.getMQConnectionPool().setMinConnections(minConnectionCPText.getText());
		mqTopicCF.getMQConnectionPool().setUnusedTimeout(unusedTimeoutCPText.getText());
		mqTopicCF.getMQSessionPool().setConnectionTimeout(connectionTimeoutSPText.getText());
		mqTopicCF.getMQSessionPool().setAgedTimeout(ageTimeoutSPText.getText());
		mqTopicCF.getMQSessionPool().setReapTime(reapTimeSPText.getText());
		mqTopicCF.getMQSessionPool().setMaxConnections(maxConnectionSPText.getText());
		mqTopicCF.getMQSessionPool().setMinConnections(minConnectionSPText.getText());
		mqTopicCF.getMQSessionPool().setUnusedTimeout(unusedTimeoutSPText.getText());
		mqTopicCF.setTransportType(transportTypeCombo.getText());
		mqTopicCF.setMappingAuthDataAlias(aliasCombo.getText());
		mqTopicCF.setAuthDataAlias(aliasCombo.getText());
		mqTopicCF.setXaRecoveryAuthAlias(aliasCombo.getText());
		
		tableItem.setText(0, jndiNameText.getText());
		tableItem.setText(1, nameText.getText());
		tableItem.setText(2, scopeCombo.getText());
		tableItem.setData(mqTopicCF);
		
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
		reapTimeCPText.setText("120");
		maxConnectionCPText.setText("10");
		minConnectionCPText.setText("1");
		unusedTimeoutCPText.setText("300");
		connectionTimeoutSPText.setText("10");
		ageTimeoutSPText.setText("3600");
		reapTimeSPText.setText("120");
		maxConnectionSPText.setText("10");
		minConnectionSPText.setText("1");
		unusedTimeoutSPText.setText("300");
		transportTypeCombo.setText("BINDINGS_THEN_CLIENT");
		
		
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
    	
		if( aliasCombo.getText().length() == 0 ){
			sendMessage("Alias can't be null !");
			return false;
		}
		
		if( transportTypeCombo.getText().length() == 0 ){
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
		
		if( reapTimeCPText.getText().length() == 0 ){
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
