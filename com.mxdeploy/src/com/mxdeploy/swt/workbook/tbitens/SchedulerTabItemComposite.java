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

import com.wds.bean.wman.Scheduler;

public class SchedulerTabItemComposite extends Composite {
	private Text nameText;
	private Text jndiNameText;
	private Text datasourceAliasText;
	private Text poolIntervalText;
	private Text workmanagerInfoJndiNameText;
	private Text datasourceJndiNameText;
	private Table table;
	private Button updateButton;
	private Button removeButton;
	private Button addButton;
	private Button cancelButton;
	
	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public SchedulerTabItemComposite(Composite parent, int style) {
		super(parent, SWT.NONE);
		GridLayout gridLayout = new GridLayout(1, false);
		gridLayout.verticalSpacing = 0;
		gridLayout.horizontalSpacing = 0;
		gridLayout.marginHeight = 0;
		setLayout(gridLayout);
		
		Composite composite = new Composite(this, SWT.NONE);
		composite.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		GridLayout gl_composite = new GridLayout(4, false);
		gl_composite.marginHeight = 10;
		gl_composite.marginWidth = 20;
		composite.setLayout(gl_composite);
		
		Label lblName = new Label(composite, SWT.NONE);
		lblName.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblName.setText("Name :");
		
		nameText = new Text(composite, SWT.BORDER);
		nameText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblJndiname = new Label(composite, SWT.NONE);
		lblJndiname.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblJndiname.setText("JndiName :");
		
		jndiNameText = new Text(composite, SWT.BORDER);
		jndiNameText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblMinThreads = new Label(composite, SWT.NONE);
		lblMinThreads.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblMinThreads.setText("DataSource Alias :");
		
		datasourceAliasText = new Text(composite, SWT.BORDER);
		datasourceAliasText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblMaxThreads = new Label(composite, SWT.NONE);
		lblMaxThreads.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblMaxThreads.setText("DataSource JndiName :");
		
		datasourceJndiNameText = new Text(composite, SWT.BORDER);
		datasourceJndiNameText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblNumAlarmThreads = new Label(composite, SWT.NONE);
		lblNumAlarmThreads.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblNumAlarmThreads.setText("PollInterval :");
		
		poolIntervalText = new Text(composite, SWT.BORDER);
		poolIntervalText.addVerifyListener(new VerifyListener() {
			public void verifyText(VerifyEvent e) {
				if(!verifyNumber(e))
					e.doit = false;					
			}
		});
		poolIntervalText.setText("30");
		poolIntervalText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);
		
		Label lblWorkReqQ = new Label(composite, SWT.NONE);
		lblWorkReqQ.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblWorkReqQ.setText("WorkManager Info JndiName : ");
		
		workmanagerInfoJndiNameText = new Text(composite, SWT.BORDER);
		workmanagerInfoJndiNameText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);
		
		Composite composite_3 = new Composite(this, SWT.BORDER);
		composite_3.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		composite_3.setLayout(new GridLayout(4, false));
		
		addButton = new Button(composite_3, SWT.NONE);
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
				Scheduler scheduler = new Scheduler();
				update(scheduler,tableItem);					
			}
		});
		addButton.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, true, false, 1, 1));
		addButton.setText("   Add   ");
		
		updateButton = new Button(composite_3, SWT.NONE);
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
				Scheduler scheduler = (Scheduler)tableItem.getData();
				update(scheduler,tableItem);				
			}
		});
		updateButton.setText("Update");
		updateButton.setEnabled(false);
		
		removeButton = new Button(composite_3, SWT.NONE);
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
		
		cancelButton = new Button(composite_3, SWT.NONE);
		cancelButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				resetComposite();
			}
		});
		cancelButton.setText("Cancel");
		
		Composite composite_2 = new Composite(this, SWT.NONE);
		GridLayout gl_composite_2 = new GridLayout(1, false);
		gl_composite_2.marginWidth = 0;
		composite_2.setLayout(gl_composite_2);
		composite_2.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		table = new Table(composite_2, SWT.BORDER | SWT.FULL_SELECTION);
		table.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if( table.getSelectionIndex() != -1 ){
					resetComposite();
					removeButton.setEnabled(true);
					updateButton.setEnabled(true);
					TableItem tableItem = table.getItem(table.getSelectionIndex());
					Scheduler scheduler = (Scheduler)tableItem.getData();
					nameText.setText(scheduler.getName());
					jndiNameText.setText(scheduler.getJndiName());
					datasourceAliasText.setText(scheduler.getDatasourceAlias());
					poolIntervalText.setText(scheduler.getPollInterval());
					workmanagerInfoJndiNameText.setText(scheduler.getWorkManagerInfoJNDIName());
					datasourceJndiNameText.setText(scheduler.getDatasourceJNDIName());
				}				
			}
		});
		table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		
		TableColumn tblclmnName = new TableColumn(table, SWT.NONE);
		tblclmnName.setWidth(256);
		tblclmnName.setText("Name");
		
		TableColumn tblclmnJndiName = new TableColumn(table, SWT.NONE);
		tblclmnJndiName.setWidth(100);
		tblclmnJndiName.setText("Jndi Name");

	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}

	public void update(Scheduler scheduler, TableItem tableItem){
		scheduler.setName(nameText.getText());
		scheduler.setJndiName(jndiNameText.getText());
		scheduler.setDatasourceAlias(datasourceAliasText.getText());
		scheduler.setPollInterval(poolIntervalText.getText());
		scheduler.setWorkManagerInfoJNDIName(workmanagerInfoJndiNameText.getText());
		scheduler.setDatasourceJNDIName(datasourceJndiNameText.getText());

		tableItem.setText(0, jndiNameText.getText());
		tableItem.setText(1, nameText.getText());
		tableItem.setData(scheduler);
		
		resetComposite();
	}
	
	private void resetComposite(){
		nameText.setText("");
		jndiNameText.setText("");
		datasourceAliasText.setText("");
		poolIntervalText.setText("30");
		workmanagerInfoJndiNameText.setText("");
		datasourceJndiNameText.setText("");
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
		
		if( datasourceAliasText.getText().length() == 0 ){
			sendMessage("Datasource Alias can't be null !");
			return false;
		}

		if( poolIntervalText.getText().length() == 0 ){
			sendMessage("Pool Interval can't be null !");
			return false;
		}

		if( workmanagerInfoJndiNameText.getText().length() == 0 ){
			sendMessage("Workmanager Info JndiName can't be null !");
			return false;
		}

		if( datasourceJndiNameText.getText().length() == 0 ){
			sendMessage("Datasource JndiName can't be null !");
			return false;
		}

		return true;
    }
    
	public Table getTable() {
		return table;
	}    

}
