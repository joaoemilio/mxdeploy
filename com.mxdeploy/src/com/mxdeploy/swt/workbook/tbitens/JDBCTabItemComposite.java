package com.mxdeploy.swt.workbook.tbitens;

import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
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

import com.mxdeploy.api.domain.Database;
import com.mxdeploy.api.service.WorkbookService;
import com.mxdeploy.swt.workbook.WorkbookTabItemComposite;
import com.wds.Util;
import com.wds.bean.WorkBook;
import com.wds.bean.jdbc.DB2UniversalJDBCDriver;
import com.wds.bean.jdbc.JDBCProvider;
import com.wds.bean.jdbc.OracleJDBCDriver;
import com.wds.bean.jdbc.SQLServerJDBCDriver;
import com.wds.bean.jdbc.SQLServerJTDSDriver;

public class JDBCTabItemComposite extends Composite {
	private Text databaseNameText;
	private Text serverNameText;
	private Text portNumberText;
	private Text datasourceNameText;
	private Text jndiNameText;
	private Text maxConnectionText;
	private Text minConnectionText;
	private Text ageTimeoutText;
	private Text connectionTimeoutText;
	private Text reapTimeText;
	private Text unusedTimeoutText;
	private Table table;
	private Button addButton;
	private Button removeButton;
	private Combo jdbcProviderCombo;
	private Combo scopeCombo;
	private Combo aliasCombo;
	private Text URLText;
	private Button updateButton;
	
	
	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public JDBCTabItemComposite(Composite parent, int style) {
		super(parent, SWT.NONE);
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
		scopeCombo.add("SERVER");
		scopeCombo.add("CLUSTER");
		scopeCombo.select(0);
		
		Label lblJdbcProvider = new Label(composite_1, SWT.NONE);
		lblJdbcProvider.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblJdbcProvider.setBounds(0, 0, 65, 16);
		lblJdbcProvider.setText("JDBC Provider :");
		
		jdbcProviderCombo = new Combo(composite_1, SWT.READ_ONLY);
		jdbcProviderCombo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		jdbcProviderCombo.add("DB2 Universal JDBC Driver Provider");
		jdbcProviderCombo.add("DB2 Universal JDBC Driver Provider (XA)");
		jdbcProviderCombo.add("Oracle JDBC Driver");
		jdbcProviderCombo.add("Oracle JDBC Driver (XA)");
		jdbcProviderCombo.add("Microsoft SQL Server JDBC Driver");
		jdbcProviderCombo.add("Microsoft SQL Server JDBC Driver (XA)");
		jdbcProviderCombo.add("JTDS JDBC Driver for SQL");		
		
		jdbcProviderCombo.select(0);
		
		Label lblDatasourceName = new Label(composite_1, SWT.NONE);
		lblDatasourceName.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblDatasourceName.setText("DataSource Name :");
		
		datasourceNameText = new Text(composite_1, SWT.BORDER);
		datasourceNameText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
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
		tabFolder.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
		
		TabItem tbtmGeneral = new TabItem(tabFolder, SWT.NONE);
		tbtmGeneral.setText("DataBase");
		
		Composite composite = new Composite(tabFolder, SWT.NONE);
		tbtmGeneral.setControl(composite);
		GridLayout gl_composite = new GridLayout(6, false);
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
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);
		
		Label label_2 = new Label(composite, SWT.NONE);
		label_2.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		label_2.setText("Server Name :");
		
		serverNameText = new Text(composite, SWT.BORDER);
		serverNameText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label label = new Label(composite, SWT.NONE);
		label.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		label.setText("Database Name :");
		
		databaseNameText = new Text(composite, SWT.BORDER);
		databaseNameText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label label_3 = new Label(composite, SWT.NONE);
		label_3.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		label_3.setText("Port Number :");
		
		portNumberText = new Text(composite, SWT.BORDER);
		portNumberText.setText("0");
		portNumberText.addVerifyListener(new VerifyListener() {
			public void verifyText(VerifyEvent e) {
				if(!verifyNumber(e))
					e.doit = false;
			}
		});
		portNumberText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblUrl = new Label(composite, SWT.NONE);
		lblUrl.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblUrl.setText("URL :");
		
		URLText = new Text(composite, SWT.BORDER);
		URLText.setEnabled(false);
		URLText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 3, 1));
		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);
		
		TabItem tbtmConnectionPool = new TabItem(tabFolder, SWT.NONE);
		tbtmConnectionPool.setText("Connection Pool");
		
		Composite composite_3 = new Composite(tabFolder, SWT.NONE);
		tbtmConnectionPool.setControl(composite_3);
		GridLayout gl_composite_3 = new GridLayout(6, false);
		gl_composite_3.marginWidth = 10;
		gl_composite_3.marginHeight = 10;
		composite_3.setLayout(gl_composite_3);
		
		Label lblMaxConnection = new Label(composite_3, SWT.NONE);
		lblMaxConnection.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblMaxConnection.setText("Max Connection :");
		
		maxConnectionText = new Text(composite_3, SWT.BORDER);
		maxConnectionText.setText("10");
		maxConnectionText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		maxConnectionText.addVerifyListener(new VerifyListener() {
			public void verifyText(VerifyEvent e) {
				if(!verifyNumber(e))
					e.doit = false;
			}
		});
		
		Label lblMinConnection = new Label(composite_3, SWT.NONE);
		lblMinConnection.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblMinConnection.setText("Min Connection :");
		
		minConnectionText = new Text(composite_3, SWT.BORDER);
		minConnectionText.setText("1");
		minConnectionText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		minConnectionText.addVerifyListener(new VerifyListener() {
			public void verifyText(VerifyEvent e) {
				if(!verifyNumber(e))
					e.doit = false;
			}
		});
		
		Label lblAge = new Label(composite_3, SWT.NONE);
		lblAge.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblAge.setText("Age Timeout :");
		
		ageTimeoutText = new Text(composite_3, SWT.BORDER);
		ageTimeoutText.setText("0");
		ageTimeoutText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		ageTimeoutText.addVerifyListener(new VerifyListener() {
			public void verifyText(VerifyEvent e) {
				if(!verifyNumber(e))
					e.doit = false;
			}
		});
		
		Label lblConnectionTimeout = new Label(composite_3, SWT.NONE);
		lblConnectionTimeout.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblConnectionTimeout.setText("Connection Timeout :");
		
		connectionTimeoutText = new Text(composite_3, SWT.BORDER);
		connectionTimeoutText.setText("10");
		connectionTimeoutText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		connectionTimeoutText.addVerifyListener(new VerifyListener() {
			public void verifyText(VerifyEvent e) {
				if(!verifyNumber(e))
					e.doit = false;
			}
		});
		
		Label lblReapTime = new Label(composite_3, SWT.NONE);
		lblReapTime.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblReapTime.setText("Reap Time :");
		
		reapTimeText = new Text(composite_3, SWT.BORDER);
		reapTimeText.setText("120");
		reapTimeText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		reapTimeText.addVerifyListener(new VerifyListener() {
			public void verifyText(VerifyEvent e) {
				if(!verifyNumber(e))
					e.doit = false;
			}
		});
		
		Label lblUnusedTimeout = new Label(composite_3, SWT.NONE);
		lblUnusedTimeout.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblUnusedTimeout.setText("Unused Timeout :");
		
		unusedTimeoutText = new Text(composite_3, SWT.BORDER);
		unusedTimeoutText.setText("300");
		unusedTimeoutText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		unusedTimeoutText.addVerifyListener(new VerifyListener() {
			public void verifyText(VerifyEvent e) {
				if(!verifyNumber(e))
					e.doit = false;
			}
		});
		
		Composite composite_5 = new Composite(this, SWT.BORDER);
		composite_5.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		GridLayout gl_composite_5 = new GridLayout(4, false);
		composite_5.setLayout(gl_composite_5);
		
		addButton = new Button(composite_5, SWT.NONE);
		addButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				addJDBCProvider();
			}
		});
		addButton.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, true, false, 1, 1));
		addButton.setText("   Add   ");
		
		updateButton = new Button(composite_5, SWT.NONE);
		updateButton.setEnabled(false);
		updateButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				updateJDBCProvider();
			}
		});
		updateButton.setText("Update");
		
		removeButton = new Button(composite_5, SWT.NONE);
		removeButton.setEnabled(false);
		removeButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if( table.getSelectionIndex() != -1 ){
					resetComposite();					
					table.remove(table.getSelectionIndex());
				}						
			}
		});
		removeButton.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		removeButton.setText("Remove");
		
		Button btnCancel = new Button(composite_5, SWT.NONE);
		btnCancel.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				resetComposite();				
			}
		});
		btnCancel.setText("Cancel");
		
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
					JDBCProvider jdbcProvider = (JDBCProvider)tableItem.getData();

			    	if( jdbcProvider.getImplementationClassName().contains("DB2ConnectionPoolDataSource") ){
			    		jdbcProviderCombo.setText("DB2 Universal JDBC Driver Provider");
			    	} else if( jdbcProvider.getImplementationClassName().contains("DB2XADataSource") ){
			    		jdbcProviderCombo.setText("DB2 Universal JDBC Driver Provider (XA)");
			    	} else if( jdbcProvider.getImplementationClassName().contains("OracleConnectionPoolDataSource") ){
			    		jdbcProviderCombo.setText("Oracle JDBC Driver");
			    	} else if( jdbcProvider.getImplementationClassName().contains("OracleXADataSource") ){
			    		jdbcProviderCombo.setText("Oracle JDBC Driver (XA)");
			    	} else if( jdbcProvider.getImplementationClassName().contains("JtdsDataSource") ){
			    		jdbcProviderCombo.setText("JTDS JDBC Driver for SQL");
			    	} else if( jdbcProvider.getImplementationClassName().contains("SQLServerConnectionPoolDataSource") ){
			    		jdbcProviderCombo.setText("Microsoft SQL Server JDBC Driver");
			    	} else if ( jdbcProvider.getImplementationClassName().contains("SQLServerXADataSource") ){
			    		jdbcProviderCombo.setText("Microsoft SQL Server JDBC Driver (XA)");
			    	}
			    	
					if( jdbcProvider.getDb2UniversalJDBCDrivers().size() > 0 ){
						
						scopeCombo.setText(jdbcProvider.getScope());
						
						for(DB2UniversalJDBCDriver db2UniversalJDBCDriver : jdbcProvider.getDb2UniversalJDBCDrivers()){
							datasourceNameText.setText(db2UniversalJDBCDriver.getName());
							jndiNameText.setText(db2UniversalJDBCDriver.getJndiName());
							aliasCombo.setText(db2UniversalJDBCDriver.getAuthDataAlias());
							serverNameText.setText(db2UniversalJDBCDriver.getJdbcResourcePropertySet().getServerName());
							databaseNameText.setText(db2UniversalJDBCDriver.getJdbcResourcePropertySet().getDatabaseName());
							portNumberText.setText(db2UniversalJDBCDriver.getJdbcResourcePropertySet().getPortNumber());
							maxConnectionText.setText(db2UniversalJDBCDriver.getConnectionPool().getMaxConnections());
							connectionTimeoutText.setText(db2UniversalJDBCDriver.getConnectionPool().getConnectionTimeout());
							minConnectionText.setText(db2UniversalJDBCDriver.getConnectionPool().getMinConnections());
							reapTimeText.setText(db2UniversalJDBCDriver.getConnectionPool().getReapTime());
							ageTimeoutText.setText(db2UniversalJDBCDriver.getConnectionPool().getAgedTimeout());
							unusedTimeoutText.setText(db2UniversalJDBCDriver.getConnectionPool().getUnusedTimeout());
							aliasCombo.setText(db2UniversalJDBCDriver.getAuthDataAlias());
							break;
						}
					}
					if( jdbcProvider.getOracleJDBCDrivers().size() > 0 ){
						for(OracleJDBCDriver oracleJDBCDriver : jdbcProvider.getOracleJDBCDrivers()){
							datasourceNameText.setText(oracleJDBCDriver.getName());
							jndiNameText.setText(oracleJDBCDriver.getJndiName());
							aliasCombo.setText(oracleJDBCDriver.getAuthDataAlias());
							URLText.setText(oracleJDBCDriver.getJdbcResourcePropertySet().getURL());
							maxConnectionText.setText(oracleJDBCDriver.getConnectionPool().getMaxConnections());
							connectionTimeoutText.setText(oracleJDBCDriver.getConnectionPool().getConnectionTimeout());
							minConnectionText.setText(oracleJDBCDriver.getConnectionPool().getMinConnections());
							reapTimeText.setText(oracleJDBCDriver.getConnectionPool().getReapTime());
							ageTimeoutText.setText(oracleJDBCDriver.getConnectionPool().getAgedTimeout());
							unusedTimeoutText.setText(oracleJDBCDriver.getConnectionPool().getUnusedTimeout());
							aliasCombo.setText(oracleJDBCDriver.getAuthDataAlias());
							break;
						}
					}
					if( jdbcProvider.getSQLServerJDBCDrivers().size() > 0 ){
						for(SQLServerJDBCDriver sqlServerJDBCDriver : jdbcProvider.getSQLServerJDBCDrivers()){
							datasourceNameText.setText(sqlServerJDBCDriver.getName());
							jndiNameText.setText(sqlServerJDBCDriver.getJndiName());
							aliasCombo.setText(sqlServerJDBCDriver.getAuthDataAlias());
							serverNameText.setText(sqlServerJDBCDriver.getJdbcResourcePropertySet().getServerName());
							databaseNameText.setText(sqlServerJDBCDriver.getJdbcResourcePropertySet().getDatabaseName());
							portNumberText.setText(sqlServerJDBCDriver.getJdbcResourcePropertySet().getPortNumber());
							maxConnectionText.setText(sqlServerJDBCDriver.getConnectionPool().getMaxConnections());
							connectionTimeoutText.setText(sqlServerJDBCDriver.getConnectionPool().getConnectionTimeout());
							minConnectionText.setText(sqlServerJDBCDriver.getConnectionPool().getMinConnections());
							reapTimeText.setText(sqlServerJDBCDriver.getConnectionPool().getReapTime());
							ageTimeoutText.setText(sqlServerJDBCDriver.getConnectionPool().getAgedTimeout());
							unusedTimeoutText.setText(sqlServerJDBCDriver.getConnectionPool().getUnusedTimeout());
							aliasCombo.setText(sqlServerJDBCDriver.getAuthDataAlias());
							break;
						}
					}
					if( jdbcProvider.getSQLServerJTDSDrivers().size() > 0 ){
						for(SQLServerJTDSDriver sqlServerJTDSDriver : jdbcProvider.getSQLServerJTDSDrivers()){
							datasourceNameText.setText(sqlServerJTDSDriver.getName());
							jndiNameText.setText(sqlServerJTDSDriver.getJndiName());
							aliasCombo.setText(sqlServerJTDSDriver.getAuthDataAlias());
							serverNameText.setText(sqlServerJTDSDriver.getJdbcResourcePropertySet().getServerName());
							databaseNameText.setText(sqlServerJTDSDriver.getJdbcResourcePropertySet().getDatabaseName());
							portNumberText.setText(sqlServerJTDSDriver.getJdbcResourcePropertySet().getPortNumber());
							maxConnectionText.setText(sqlServerJTDSDriver.getConnectionPool().getMaxConnections());
							connectionTimeoutText.setText(sqlServerJTDSDriver.getConnectionPool().getConnectionTimeout());
							minConnectionText.setText(sqlServerJTDSDriver.getConnectionPool().getMinConnections());
							reapTimeText.setText(sqlServerJTDSDriver.getConnectionPool().getReapTime());
							ageTimeoutText.setText(sqlServerJTDSDriver.getConnectionPool().getAgedTimeout());
							unusedTimeoutText.setText(sqlServerJTDSDriver.getConnectionPool().getUnusedTimeout());
							aliasCombo.setText(sqlServerJTDSDriver.getAuthDataAlias());
							break;
						}
					}					
				}				
			}
		});
		table.setLinesVisible(true);
		table.setHeaderVisible(true);
		table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		TableColumn tableColumn = new TableColumn(table, SWT.NONE);
		tableColumn.setWidth(160);
		tableColumn.setText("Jndi Name");
		
		TableColumn tableColumn_1 = new TableColumn(table, SWT.NONE);
		tableColumn_1.setWidth(237);
		tableColumn_1.setText("DataSource Name");
		
		TableColumn tableColumn_2 = new TableColumn(table, SWT.NONE);
		tableColumn_2.setWidth(298);
		tableColumn_2.setText("JDBC Provider Type");
		
		TableColumn tblclmnScope = new TableColumn(table, SWT.NONE);
		tblclmnScope.setWidth(100);
		tblclmnScope.setText("Scope");

		jdbcProviderCombo.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent arg0) {
				if( jdbcProviderCombo.getText().contains("Oracle") ){
					databaseNameText.setEnabled(false);
					portNumberText.setEnabled(false);
					serverNameText.setEnabled(false);
					URLText.setEnabled(true);
				} else {
					databaseNameText.setEnabled(true);
					portNumberText.setEnabled(true);
					serverNameText.setEnabled(true);
					URLText.setEnabled(false);
				}
			}
		});
		
	}

	public void addJDBCProvider(){
		if( !validate() ){
			return;
		}
		
		if( jndiNameExist(jndiNameText.getText(), -1) ){
			sendMessage("JndiName already exist !");
			return;	
		}
		
		WorkbookService service = new WorkbookService();
		WorkBook workbookTemplate = service.loadWorkBook(Database.HOME+"/"+Database.WORKBOOK_TEMPLATE_FILE_NAME);
		JDBCProvider jdbcProvider = null;
		if( jdbcProviderCombo.getText().contains("DB2 Universal JDBC Driver Provider") ){
			jdbcProvider = Util.getCloneJDBCProvider(workbookTemplate, "DB2 Universal JDBC Driver Provider");
		} else if ( jdbcProviderCombo.getText().equals("DB2 Universal JDBC Driver Provider (XA)") ){
			jdbcProvider = Util.getCloneJDBCProvider(workbookTemplate, "DB2 Universal JDBC Driver Provider (XA)");
		} else if ( jdbcProviderCombo.getText().equals("Oracle JDBC Driver") ){
			jdbcProvider = Util.getCloneJDBCProvider(workbookTemplate, "Oracle JDBC Driver");
		} else if ( jdbcProviderCombo.getText().equals("Oracle JDBC Driver (XA)") ){
			jdbcProvider = Util.getCloneJDBCProvider(workbookTemplate, "Oracle JDBC Driver (XA)");
		} else if ( jdbcProviderCombo.getText().equals("JTDS JDBC Driver for SQL") ){
			jdbcProvider = Util.getCloneJDBCProvider(workbookTemplate, "JTDS JDBC Driver for SQL");
		} else if ( jdbcProviderCombo.getText().contains("Microsoft SQL Server JDBC Driver") ){
			jdbcProvider = Util.getCloneJDBCProvider(workbookTemplate, "Microsoft SQL Server JDBC Driver");
		} else if ( jdbcProviderCombo.getText().contains("Microsoft SQL Server JDBC Driver (XA)") ){
			jdbcProvider = Util.getCloneJDBCProvider(workbookTemplate, "Microsoft SQL Server JDBC Driver (XA)");
		}		
		
		updateJDBCProvider(jdbcProvider);
		
		TableItem  tableItem= new TableItem(table, SWT.NONE);
		tableItem.setText(0,jndiNameText.getText() );
		tableItem.setText(1,datasourceNameText.getText() );
		tableItem.setText(2,jdbcProviderCombo.getText() );
		tableItem.setText(3,scopeCombo.getText() );
		tableItem.setData(jdbcProvider);
		resetComposite();	 

	}
	
	public void updateJDBCProvider(){
		if( !validate() ){
			return;
		}
		
		if( jndiNameExist(jndiNameText.getText(), table.getSelectionIndex()) ){
			sendMessage("JndiName already exist !");
			return;	
		}

		TableItem tableItem = table.getItem(table.getSelectionIndex());
		JDBCProvider jdbcProvider = (JDBCProvider)tableItem.getData();		
		
		updateJDBCProvider(jdbcProvider);
		
		tableItem.setText(0,jndiNameText.getText() );
		tableItem.setText(1,datasourceNameText.getText() );
		tableItem.setText(2,jdbcProviderCombo.getText() );
		tableItem.setText(3,scopeCombo.getText() );
		tableItem.setData(jdbcProvider);
		resetComposite();
	}
	
	public boolean validate(){
		if( jndiNameText.getText().length() == 0 ){
			sendMessage("JndiName can't be null !");
			return false;
		}
		if( aliasCombo.getItemCount() == 0 ){
			sendMessage("Alias can't be null !");
			return false;
		}	
		if ( jdbcProviderCombo.getText().contains("Oracle") ){
			if( URLText.getText().length() == 0 ){
				sendMessage("URL can't be null !");
				return false;
			}
		} else {
			if( serverNameText.getText().length() == 0 ){
				sendMessage("Server Name can't be null !");
				return false;
			}
			if( databaseNameText.getText().length() == 0 ){
				sendMessage("Database Name can't be null !");
				return false;
			}		
			if( portNumberText.getText().length() == 0 ){
				sendMessage("Port Number can't be null !");
				return false;
			}
		}
		if( maxConnectionText.getText().length() == 0 ){
			sendMessage("Max Connection can't be null !");
			return false;
		}		
		if( connectionTimeoutText.getText().length() == 0 ){
			sendMessage("Connection Timeout can't be null !");
			return false;
		}		
		if( minConnectionText.getText().length() == 0 ){
			sendMessage("Min Connection can't be null !");
			return false;
		}		
		if( reapTimeText.getText().length() == 0 ){
			sendMessage("Reap Time can't be null !");
			return false;
		}		
		if( ageTimeoutText.getText().length() == 0 ){
			sendMessage("Age Timeout can't be null !");
			return false;
		}		
		if( unusedTimeoutText.getText().length() == 0 ){
			sendMessage("Unused Timeout can't be null !");
			return false;
		}		
		
		return true;
	}
	
	public JDBCProvider updateJDBCProvider(JDBCProvider jdbcProvider){
		if( jdbcProviderCombo.getText().contains("DB2 Universal JDBC Driver Provider") ){
			jdbcProvider.setScope(scopeCombo.getText());
			for( DB2UniversalJDBCDriver db2UniversalJDBCDriver : jdbcProvider.getDb2UniversalJDBCDrivers()){
				 db2UniversalJDBCDriver.setName(datasourceNameText.getText());
				 db2UniversalJDBCDriver.setJndiName(jndiNameText.getText());
				 db2UniversalJDBCDriver.setAuthDataAlias(aliasCombo.getText());
				 db2UniversalJDBCDriver.setXaRecoveryAuthAlias(aliasCombo.getText());
				 db2UniversalJDBCDriver.setMappingAuthDataAlias(aliasCombo.getText());
				 db2UniversalJDBCDriver.getJdbcResourcePropertySet().setServerName(serverNameText.getText());
				 db2UniversalJDBCDriver.getJdbcResourcePropertySet().setDatabaseName(databaseNameText.getText());
				 db2UniversalJDBCDriver.getJdbcResourcePropertySet().setPortNumber(portNumberText.getText());
				 db2UniversalJDBCDriver.getConnectionPool().setMaxConnections(maxConnectionText.getText());
				 db2UniversalJDBCDriver.getConnectionPool().setConnectionTimeout(connectionTimeoutText.getText());
				 db2UniversalJDBCDriver.getConnectionPool().setMinConnections(minConnectionText.getText());
				 db2UniversalJDBCDriver.getConnectionPool().setReapTime(reapTimeText.getText());
				 db2UniversalJDBCDriver.getConnectionPool().setAgedTimeout(ageTimeoutText.getText());
				 db2UniversalJDBCDriver.getConnectionPool().setUnusedTimeout(unusedTimeoutText.getText());
			}
		} else if ( jdbcProviderCombo.getText().equals("DB2 Universal JDBC Driver Provider (XA)") ){
			jdbcProvider.setScope(scopeCombo.getText());
			for( DB2UniversalJDBCDriver db2UniversalJDBCDriver : jdbcProvider.getDb2UniversalJDBCDrivers()){
				 db2UniversalJDBCDriver.setName(datasourceNameText.getText());
				 db2UniversalJDBCDriver.setJndiName(jndiNameText.getText());
				 db2UniversalJDBCDriver.setAuthDataAlias(aliasCombo.getText());
				 db2UniversalJDBCDriver.setMappingAuthDataAlias(aliasCombo.getText());
				 db2UniversalJDBCDriver.setXaRecoveryAuthAlias(aliasCombo.getText());
				 db2UniversalJDBCDriver.getJdbcResourcePropertySet().setServerName(serverNameText.getText());
				 db2UniversalJDBCDriver.getJdbcResourcePropertySet().setDatabaseName(databaseNameText.getText());
				 db2UniversalJDBCDriver.getJdbcResourcePropertySet().setPortNumber(portNumberText.getText());
				 db2UniversalJDBCDriver.getConnectionPool().setMaxConnections(maxConnectionText.getText());
				 db2UniversalJDBCDriver.getConnectionPool().setConnectionTimeout(connectionTimeoutText.getText());
				 db2UniversalJDBCDriver.getConnectionPool().setMinConnections(minConnectionText.getText());
				 db2UniversalJDBCDriver.getConnectionPool().setReapTime(reapTimeText.getText());
				 db2UniversalJDBCDriver.getConnectionPool().setAgedTimeout(ageTimeoutText.getText());
				 db2UniversalJDBCDriver.getConnectionPool().setUnusedTimeout(unusedTimeoutText.getText());
			}
		} else if ( jdbcProviderCombo.getText().equals("Oracle JDBC Driver") ){
			jdbcProvider.setScope(scopeCombo.getText());
			for( OracleJDBCDriver oracleJDBCDriver : jdbcProvider.getOracleJDBCDrivers()){
				 oracleJDBCDriver.setName(datasourceNameText.getText());
				 oracleJDBCDriver.setJndiName(jndiNameText.getText());
				 oracleJDBCDriver.setAuthDataAlias(aliasCombo.getText());
				 oracleJDBCDriver.setXaRecoveryAuthAlias(aliasCombo.getText());
				 oracleJDBCDriver.setMappingAuthDataAlias(aliasCombo.getText());
				 oracleJDBCDriver.getJdbcResourcePropertySet().setURL(URLText.getText());
				 oracleJDBCDriver.getConnectionPool().setMaxConnections(maxConnectionText.getText());
				 oracleJDBCDriver.getConnectionPool().setConnectionTimeout(connectionTimeoutText.getText());
				 oracleJDBCDriver.getConnectionPool().setMinConnections(minConnectionText.getText());
				 oracleJDBCDriver.getConnectionPool().setReapTime(reapTimeText.getText());
				 oracleJDBCDriver.getConnectionPool().setAgedTimeout(ageTimeoutText.getText());
				 oracleJDBCDriver.getConnectionPool().setUnusedTimeout(unusedTimeoutText.getText());
			}
		} else if ( jdbcProviderCombo.getText().equals("Oracle JDBC Driver (XA)") ){
			jdbcProvider.setScope(scopeCombo.getText());
			for( OracleJDBCDriver oracleJDBCDriver : jdbcProvider.getOracleJDBCDrivers()){
				 oracleJDBCDriver.setName(datasourceNameText.getText());
				 oracleJDBCDriver.setJndiName(jndiNameText.getText());
				 oracleJDBCDriver.setAuthDataAlias(aliasCombo.getText());
				 oracleJDBCDriver.setMappingAuthDataAlias(aliasCombo.getText());
				 oracleJDBCDriver.setXaRecoveryAuthAlias(aliasCombo.getText());
				 oracleJDBCDriver.getJdbcResourcePropertySet().setURL(URLText.getText());
				 oracleJDBCDriver.getConnectionPool().setMaxConnections(maxConnectionText.getText());
				 oracleJDBCDriver.getConnectionPool().setConnectionTimeout(connectionTimeoutText.getText());
				 oracleJDBCDriver.getConnectionPool().setMinConnections(minConnectionText.getText());
				 oracleJDBCDriver.getConnectionPool().setReapTime(reapTimeText.getText());
				 oracleJDBCDriver.getConnectionPool().setAgedTimeout(ageTimeoutText.getText());
				 oracleJDBCDriver.getConnectionPool().setUnusedTimeout(unusedTimeoutText.getText());
			}			
		} else if ( jdbcProviderCombo.getText().equals("JTDS JDBC Driver for SQL") ){
			jdbcProvider.setScope(scopeCombo.getText());
			for( SQLServerJTDSDriver sqlServerJTDSDriver : jdbcProvider.getSQLServerJTDSDrivers()){
				 sqlServerJTDSDriver.setName(datasourceNameText.getText());
				 sqlServerJTDSDriver.setJndiName(jndiNameText.getText());
				 sqlServerJTDSDriver.setAuthDataAlias(aliasCombo.getText());
				 sqlServerJTDSDriver.setMappingAuthDataAlias(aliasCombo.getText());
				 sqlServerJTDSDriver.setXaRecoveryAuthAlias(aliasCombo.getText());
				 sqlServerJTDSDriver.getJdbcResourcePropertySet().setServerName(serverNameText.getText());
				 sqlServerJTDSDriver.getJdbcResourcePropertySet().setDatabaseName(databaseNameText.getText());
				 sqlServerJTDSDriver.getJdbcResourcePropertySet().setPortNumber(portNumberText.getText());
				 sqlServerJTDSDriver.getConnectionPool().setMaxConnections(maxConnectionText.getText());
				 sqlServerJTDSDriver.getConnectionPool().setConnectionTimeout(connectionTimeoutText.getText());
				 sqlServerJTDSDriver.getConnectionPool().setMinConnections(minConnectionText.getText());
				 sqlServerJTDSDriver.getConnectionPool().setReapTime(reapTimeText.getText());
				 sqlServerJTDSDriver.getConnectionPool().setAgedTimeout(ageTimeoutText.getText());
				 sqlServerJTDSDriver.getConnectionPool().setUnusedTimeout(unusedTimeoutText.getText());
			}
		} else if ( jdbcProviderCombo.getText().contains("Microsoft SQL Server JDBC Driver") ){
			jdbcProvider.setScope(scopeCombo.getText());
			for( SQLServerJDBCDriver sqlServerJDBCDriver : jdbcProvider.getSQLServerJDBCDrivers()){
				 sqlServerJDBCDriver.setName(datasourceNameText.getText());
				 sqlServerJDBCDriver.setJndiName(jndiNameText.getText());
				 sqlServerJDBCDriver.setAuthDataAlias(aliasCombo.getText());
				 sqlServerJDBCDriver.setMappingAuthDataAlias(aliasCombo.getText());
				 sqlServerJDBCDriver.setXaRecoveryAuthAlias(aliasCombo.getText());
				 sqlServerJDBCDriver.getJdbcResourcePropertySet().setServerName(serverNameText.getText());
				 sqlServerJDBCDriver.getJdbcResourcePropertySet().setDatabaseName(databaseNameText.getText());
				 sqlServerJDBCDriver.getJdbcResourcePropertySet().setPortNumber(portNumberText.getText());
				 sqlServerJDBCDriver.getConnectionPool().setMaxConnections(maxConnectionText.getText());
				 sqlServerJDBCDriver.getConnectionPool().setConnectionTimeout(connectionTimeoutText.getText());
				 sqlServerJDBCDriver.getConnectionPool().setMinConnections(minConnectionText.getText());
				 sqlServerJDBCDriver.getConnectionPool().setReapTime(reapTimeText.getText());
				 sqlServerJDBCDriver.getConnectionPool().setAgedTimeout(ageTimeoutText.getText());
				 sqlServerJDBCDriver.getConnectionPool().setUnusedTimeout(unusedTimeoutText.getText());
			}
		} else if ( jdbcProviderCombo.getText().contains("Microsoft SQL Server JDBC Driver (XA)") ){
			jdbcProvider.setScope(scopeCombo.getText());
			for( SQLServerJDBCDriver sqlServerJDBCDriver : jdbcProvider.getSQLServerJDBCDrivers()){
				 sqlServerJDBCDriver.setName(datasourceNameText.getText());
				 sqlServerJDBCDriver.setJndiName(jndiNameText.getText());
				 sqlServerJDBCDriver.setAuthDataAlias(aliasCombo.getText());
				 sqlServerJDBCDriver.setMappingAuthDataAlias(aliasCombo.getText());
				 sqlServerJDBCDriver.setXaRecoveryAuthAlias(aliasCombo.getText());
				 sqlServerJDBCDriver.getJdbcResourcePropertySet().setServerName(serverNameText.getText());
				 sqlServerJDBCDriver.getJdbcResourcePropertySet().setDatabaseName(databaseNameText.getText());
				 sqlServerJDBCDriver.getJdbcResourcePropertySet().setPortNumber(portNumberText.getText());
				 sqlServerJDBCDriver.getConnectionPool().setMaxConnections(maxConnectionText.getText());
				 sqlServerJDBCDriver.getConnectionPool().setConnectionTimeout(connectionTimeoutText.getText());
				 sqlServerJDBCDriver.getConnectionPool().setMinConnections(minConnectionText.getText());
				 sqlServerJDBCDriver.getConnectionPool().setReapTime(reapTimeText.getText());
				 sqlServerJDBCDriver.getConnectionPool().setAgedTimeout(ageTimeoutText.getText());
				 sqlServerJDBCDriver.getConnectionPool().setUnusedTimeout(unusedTimeoutText.getText());
			}			
		}		
		return jdbcProvider;
	}
	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}

	
	public void loadAliasCombo(List<String> list){
		for( String alias : list){
			aliasCombo.removeAll();
			aliasCombo.add(alias);
		}
		aliasCombo.select(0);
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
    
    public void resetComposite(){
    	scopeCombo.setText("SERVER");
		addButton.setEnabled(true);
		updateButton.setEnabled(false);
		removeButton.setEnabled(false);
		datasourceNameText.setText("");
		jndiNameText.setText("");

		if( jdbcProviderCombo.getText().contains("Oracle")){
			URLText.setText("");
		} else {
			serverNameText.setText("");
			databaseNameText.setText("");
			portNumberText.setText("0");					
		}
		maxConnectionText.setText("10");
		connectionTimeoutText.setText("10");
		minConnectionText.setText("1");
		reapTimeText.setText("120");
		ageTimeoutText.setText("0");
		unusedTimeoutText.setText("300");    	
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
    
    public Table getTable(){
    	return this.table;
    }
}
