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

import com.wds.bean.wman.WorkManager;

public class WorkManagerTabItemComposite extends Composite {
	private Text nameText;
	private Text jndiNameText;
	private Text minThreadsText;
	private Text numAlarmThreadsText;
	private Text workReqQSizeText;
	private Text maxThreadsText;
	private Text threadsPriorityText;
	private Text workTimeoutText;
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
	public WorkManagerTabItemComposite(Composite parent, int style) {
		super(parent, SWT.BORDER);
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
		lblMinThreads.setText("Min Threads :");
		
		minThreadsText = new Text(composite, SWT.BORDER);
		minThreadsText.addVerifyListener(new VerifyListener() {
			public void verifyText(VerifyEvent arg0) {
			}
		});
		minThreadsText.setText("1");
		minThreadsText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		minThreadsText.addVerifyListener(new VerifyListener() {
			public void verifyText(VerifyEvent e) {
				if(!verifyNumber(e))
					e.doit = false;					
			}
		});
		
		Label lblMaxThreads = new Label(composite, SWT.NONE);
		lblMaxThreads.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblMaxThreads.setText("Max Threads :");
		
		maxThreadsText = new Text(composite, SWT.BORDER);
		maxThreadsText.setText("5");
		maxThreadsText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		maxThreadsText.addVerifyListener(new VerifyListener() {
			public void verifyText(VerifyEvent e) {
				if(!verifyNumber(e))
					e.doit = false;					
			}
		});
		
		Label lblNumAlarmThreads = new Label(composite, SWT.NONE);
		lblNumAlarmThreads.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblNumAlarmThreads.setText("Num Alarm Threads :");
		
		numAlarmThreadsText = new Text(composite, SWT.BORDER);
		numAlarmThreadsText.setText("5");
		numAlarmThreadsText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		numAlarmThreadsText.addVerifyListener(new VerifyListener() {
			public void verifyText(VerifyEvent e) {
				if(!verifyNumber(e))
					e.doit = false;					
			}
		});
		
		Label lblThreadPriority = new Label(composite, SWT.NONE);
		lblThreadPriority.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblThreadPriority.setText("Thread Priority :");
		
		threadsPriorityText = new Text(composite, SWT.BORDER);
		threadsPriorityText.setText("10");
		threadsPriorityText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		threadsPriorityText.addVerifyListener(new VerifyListener() {
			public void verifyText(VerifyEvent e) {
				if(!verifyNumber(e))
					e.doit = false;					
			}
		});
		
		Label lblWorkReqQ = new Label(composite, SWT.NONE);
		lblWorkReqQ.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblWorkReqQ.setText("Work Req Q Size :");
		
		workReqQSizeText = new Text(composite, SWT.BORDER);
		workReqQSizeText.setText("0");
		workReqQSizeText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		workReqQSizeText.addVerifyListener(new VerifyListener() {
			public void verifyText(VerifyEvent e) {
				if(!verifyNumber(e))
					e.doit = false;					
			}
		});
		
		Label lblWorkTimeout = new Label(composite, SWT.NONE);
		lblWorkTimeout.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblWorkTimeout.setText("Work Timeout :");
		
		workTimeoutText = new Text(composite, SWT.BORDER);
		workTimeoutText.setText("0");
		workTimeoutText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		new Label(composite, SWT.NONE);
		
		Button isGrowableCheckBox = new Button(composite, SWT.CHECK);
		isGrowableCheckBox.setSelection(true);
		isGrowableCheckBox.setText("Is Growable");
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);
		
		Composite composite_2 = new Composite(this, SWT.NONE);
		GridLayout gl_composite_2 = new GridLayout(1, false);
		gl_composite_2.marginWidth = 0;
		composite_2.setLayout(gl_composite_2);
		composite_2.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		Composite composite_3 = new Composite(composite_2, SWT.BORDER);
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
				WorkManager workManager = new WorkManager();
				update(workManager,tableItem);				
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
				WorkManager workManager = (WorkManager)tableItem.getData();
				update(workManager,tableItem);				
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
		
		table = new Table(composite_2, SWT.BORDER | SWT.FULL_SELECTION);
		table.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if( table.getSelectionIndex() != -1 ){
					removeButton.setEnabled(true);
					updateButton.setEnabled(true);
					TableItem tableItem = table.getItem(table.getSelectionIndex());
					WorkManager workmanager = (WorkManager)tableItem.getData();
					nameText.setText(workmanager.getName());
					jndiNameText.setText(workmanager.getJndiName());
					minThreadsText.setText(workmanager.getMinThreads());
					numAlarmThreadsText.setText(workmanager.getNumAlarmThreads());
					workReqQSizeText.setText(workmanager.getWorkReqQSize());
					maxThreadsText.setText(workmanager.getMaxThreads());
					threadsPriorityText.setText(workmanager.getThreadPriority());
					workTimeoutText.setText(workmanager.getWorkTimeout()); 
				}
			}
		});
		table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		
		TableColumn tblclmnName = new TableColumn(table, SWT.NONE);
		tblclmnName.setWidth(333);
		tblclmnName.setText("Name");
		
		TableColumn tblclmnJndiName = new TableColumn(table, SWT.NONE);
		tblclmnJndiName.setWidth(100);
		tblclmnJndiName.setText("Jndi Name");

	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}


	public Table getTable() {
		return table;
	}

	public void update(WorkManager workManager, TableItem tableItem){
		workManager.setName(nameText.getText());
		workManager.setJndiName(jndiNameText.getText());
		workManager.setMinThreads(minThreadsText.getText());
		workManager.setNumAlarmThreads(numAlarmThreadsText.getText());
		workManager.setWorkReqQSize(workReqQSizeText.getText());
		workManager.setMaxThreads(maxThreadsText.getText());
		workManager.setThreadPriority(threadsPriorityText.getText());
		workManager.setWorkTimeout(workTimeoutText.getText());

		tableItem.setText(0, jndiNameText.getText());
		tableItem.setText(1, nameText.getText());
		tableItem.setData(workManager);
		
		resetComposite();
	}
	
	private void resetComposite(){
		nameText.setText("");
		jndiNameText.setText("");
		minThreadsText.setText("1");
		numAlarmThreadsText.setText("5");
		workReqQSizeText.setText("0");
		maxThreadsText.setText("5");
		threadsPriorityText.setText("10");
		workTimeoutText.setText("0");
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
		if( workTimeoutText.getText().length() == 0 ){
			sendMessage("Work Timeout can't be null !");
			return false;
		}
		
		if( threadsPriorityText.getText().length() == 0 ){
			sendMessage("Threads Priority can't be null !");
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
		
		if( minThreadsText.getText().length() == 0 ){
			sendMessage("Min Threads can't be null !");
			return false;
		}

		if( numAlarmThreadsText.getText().length() == 0 ){
			sendMessage("NumAlarm Threads can't be null !");
			return false;
		}

		if( workReqQSizeText.getText().length() == 0 ){
			sendMessage("Work Req Q Size can't be null !");
			return false;
		}

		if( maxThreadsText.getText().length() == 0 ){
			sendMessage("Max Threads can't be null !");
			return false;
		}

		return true;
    }

}
