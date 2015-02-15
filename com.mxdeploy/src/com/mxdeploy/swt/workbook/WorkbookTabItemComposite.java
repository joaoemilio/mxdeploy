package com.mxdeploy.swt.workbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.StringTokenizer;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
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
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.wb.swt.SWTResourceManager;

import com.mxdeploy.api.domain.Database;
import com.mxdeploy.api.domain.Project;
import com.mxdeploy.api.service.WorkbookService;
import com.mxdeploy.swt.workbook.tbitens.J2CAuthDataTabItemComposite;
import com.mxdeploy.swt.workbook.tbitens.JDBCTabItemComposite;
import com.mxdeploy.swt.workbook.tbitens.ListenerPortTabItemComposite;
import com.mxdeploy.swt.workbook.tbitens.MQQueueCFTabItemComposite;
import com.mxdeploy.swt.workbook.tbitens.MQQueueTabItemComposite;
import com.mxdeploy.swt.workbook.tbitens.MQTopicCFTabItemComposite;
import com.mxdeploy.swt.workbook.tbitens.MQTopicTabItemComposite;
import com.mxdeploy.swt.workbook.tbitens.NodeTabItemComposite;
import com.mxdeploy.swt.workbook.tbitens.SchedulerTabItemComposite;
import com.mxdeploy.swt.workbook.tbitens.ServerTabItemComposite;
import com.mxdeploy.swt.workbook.tbitens.WorkManagerTabItemComposite;
import com.wds.Util;
import com.wds.bean.Cell;
import com.wds.bean.Cluster;
import com.wds.bean.Node;
import com.wds.bean.WorkBook;
import com.wds.bean.jdbc.DB2UniversalJDBCDriver;
import com.wds.bean.jdbc.JDBCProvider;
import com.wds.bean.jdbc.OracleJDBCDriver;
import com.wds.bean.jdbc.SQLServerJDBCDriver;
import com.wds.bean.jdbc.SQLServerJTDSDriver;
import com.wds.bean.listener.ListenerPort;
import com.wds.bean.listener.MessageListenerService;
import com.wds.bean.mq.MQQueue;
import com.wds.bean.mq.MQQueueConnectionFactory;
import com.wds.bean.mq.MQTopic;
import com.wds.bean.mq.MQTopicConnectionFactory;
import com.wds.bean.security.JAASAuthData;
import com.wds.bean.security.SecurityDomain;
import com.wds.bean.server.ApplicationServer;
import com.wds.bean.server.Cookie;
import com.wds.bean.server.JavaVirtualMachine;
import com.wds.bean.server.Server;
import com.wds.bean.server.TuningParams;
import com.wds.bean.wman.Scheduler;
import com.wds.bean.wman.WorkManager;

public class WorkbookTabItemComposite extends Composite {

	private Composite topComposite = null;

	private Label imageTop = null;

	private Composite centerComposite = null;

	private Composite botomComposite = null;

	private Label topLabel = null;

	private Label label1 = null;

	private Button OKButton = null;

	private TabFolder tabFolder;
	
	private ServerTabItemComposite serverTabItemComposite;
	private NodeTabItemComposite nodeTabItemComposite;
	private J2CAuthDataTabItemComposite j2cAuthDataTabItemComposite;
	private JDBCTabItemComposite jdbcTabItemComposite;
	private MQQueueTabItemComposite mqQueueTabItemComposite;
	private MQQueueCFTabItemComposite mqQueueCFTabItemComposite;
	private MQTopicTabItemComposite mqTopicTabItemComposite;
	private MQTopicCFTabItemComposite mqTopicCFTabItemComposite;
	private WorkManagerTabItemComposite workManagerTabItemComposite;
	private SchedulerTabItemComposite schedulerTabItemComposite;
	private ListenerPortTabItemComposite listenerPortTabItemComposite;
	
	private Label errorLabel = null;
	
	private String retorno = null;

	protected WorkbookTabItemHelper helper = null;
	protected Text instanceNameText;
	private Combo webserversCombo;
	private Combo verticalCombo;
	
	public static List<String> aliasList = new ArrayList<String>();
	
	private Project project = null;
	
	
	public WorkbookTabItemComposite(Composite parent, int style) {
		super(parent, SWT.NONE);
		initialize();
	}
	
	private void initialize() { 
		helper = new WorkbookTabItemHelper(this);
		
		GridLayout gridLayout1 = new GridLayout();
		gridLayout1.horizontalSpacing = 0;
		gridLayout1.marginWidth = 0;
		gridLayout1.verticalSpacing = 0;
		gridLayout1.marginHeight = 0;
		createTopComposite();
		this.setLayout(gridLayout1);
		createCenterComposite();
		createBotomComposite();
		this.setSize(new Point(788, 551));
	}

	/**
	 * This method initializes topComposite
	 * 
	 */ 
	private void createTopComposite() {
		GridData gridData11 = new GridData(); 
		gridData11.horizontalAlignment = org.eclipse.swt.layout.GridData.BEGINNING;
		gridData11.verticalAlignment = org.eclipse.swt.layout.GridData.BEGINNING;
		GridData gridData1 = new GridData();
		gridData1.horizontalAlignment = org.eclipse.swt.layout.GridData.END;
		gridData1.grabExcessVerticalSpace = true;
		gridData1.verticalAlignment = org.eclipse.swt.layout.GridData.FILL;
		gridData1.horizontalIndent = 0;
		gridData1.verticalSpan = 4;
		gridData1.grabExcessHorizontalSpace = true;
		GridData gridData = new GridData();
		gridData.heightHint = 72;
		gridData.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		gridData.grabExcessHorizontalSpace = true;
		GridLayout gridLayout = new GridLayout();
		gridLayout.horizontalSpacing = 0;
		gridLayout.marginWidth = 0;
		gridLayout.verticalSpacing = 0;
		gridLayout.numColumns = 2;
		gridLayout.marginHeight = 0;
		topComposite = new Composite(this, SWT.BORDER);
		topComposite.setBackground(new Color(Display.getCurrent(), 255, 255,255));
		topComposite.setLayout(gridLayout);
		topComposite.setLayoutData(gridData);
		@SuppressWarnings("unused")
		Label filler2 = new Label(topComposite, SWT.NONE);
		imageTop = new Label(topComposite, SWT.NONE);
		imageTop.setText("");
		imageTop.setLayoutData(gridData1);
		imageTop.setImage(new Image(Display.getCurrent(), getClass().getResourceAsStream("/MXTerminalMedio.jpg")));
		topLabel = new Label(topComposite, SWT.NONE);
		topLabel.setText("  Workbook Form"); 
		topLabel.setFont(new Font(Display.getDefault(), "Tahoma", 12, SWT.NORMAL));
		topLabel.setLayoutData(gridData11);
		topLabel.setBackground(new Color(Display.getCurrent(), 255, 255, 255));
		label1 = new Label(topComposite, SWT.NONE);
		label1.setText("        Fill up all fields and Save");
		label1.setBackground(new Color(Display.getCurrent(), 255, 255, 255));

		errorLabel = new Label(topComposite, SWT.NONE);
		errorLabel.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_LIST_SELECTION_TEXT));
		errorLabel.setText("                                                                                    ");
		GridData gd_errorLabel = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_errorLabel.verticalIndent = 5;
		errorLabel.setLayoutData(gd_errorLabel);
		errorLabel.setForeground(new Color(Display.getCurrent(), 255, 0, 0));
		new Label(topComposite, SWT.NONE);
		new Label(topComposite, SWT.NONE);
	}

	/**
	 * This method initializes centerComposite
	 * 
	 */
	private void createCenterComposite() {
		GridData gridData3 = new GridData();
		gridData3.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		gridData3.grabExcessHorizontalSpace = true;
		gridData3.verticalAlignment = org.eclipse.swt.layout.GridData.END;
		GridLayout gridLayout3 = new GridLayout();
		gridLayout3.horizontalSpacing = 0;
		gridLayout3.marginWidth = 2;
		gridLayout3.verticalSpacing = 0;
		gridLayout3.marginHeight = 2;
		botomComposite = new Composite(this, SWT.BORDER);
		botomComposite.setLayout(gridLayout3);
		botomComposite.setLayoutData(gridData3);
		
		ToolBar toolBar = new ToolBar(botomComposite, SWT.FLAT | SWT.RIGHT);
		
		ToolItem tltmSave = new ToolItem(toolBar, SWT.NONE);
		tltmSave.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				save();
			}
		});
		tltmSave.setImage(SWTResourceManager.getImage(WorkbookTabItemComposite.class, "/enabled/save.gif"));
		tltmSave.setText("Save");
		GridData gridData2 = new GridData();
		gridData2.grabExcessVerticalSpace = true;
		gridData2.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		gridData2.grabExcessHorizontalSpace = true;
		gridData2.verticalAlignment = SWT.FILL;
		GridLayout gridLayout2 = new GridLayout();
		gridLayout2.horizontalSpacing = 1;
		gridLayout2.marginWidth = 1;
		gridLayout2.verticalSpacing = 1;
		gridLayout2.marginHeight = 1;
		centerComposite = new Composite(this, SWT.NONE);
		centerComposite.setLayout(gridLayout2);
		createComposite1();
		centerComposite.setLayoutData(gridData2);
		{
			Composite composite_1 = new Composite(centerComposite, SWT.NONE);
			GridLayout gl_composite_1 = new GridLayout(6, false);
			gl_composite_1.marginHeight = 10;
			gl_composite_1.marginWidth = 20;
			composite_1.setLayout(gl_composite_1);
			composite_1.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
			{
				Label lblNode = new Label(composite_1, SWT.NONE);
				lblNode.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
				lblNode.setBounds(0, 0, 65, 16);
				lblNode.setText("Name :");
			}
			{
				instanceNameText = new Text(composite_1, SWT.BORDER);
				instanceNameText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
			}
			
			Label lblWebServers = new Label(composite_1, SWT.NONE);
			lblWebServers.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
			lblWebServers.setText("Web Servers :");
			
			webserversCombo = new Combo(composite_1, SWT.READ_ONLY);
			webserversCombo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
			webserversCombo.add("Intranet");
			webserversCombo.add("Internet");
			webserversCombo.add("WebServices");
			webserversCombo.add("Intranet and WebServices");
			webserversCombo.add("Internet and WebServices");
			webserversCombo.select(0);
			{
				Label lblVertical = new Label(composite_1, SWT.NONE);
				lblVertical.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
				lblVertical.setText("Vertical :");
			}
			{
				verticalCombo = new Combo(composite_1, SWT.READ_ONLY);
				verticalCombo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
				verticalCombo.add("0");
				verticalCombo.add("2");
				verticalCombo.add("3");
				verticalCombo.add("4");
				verticalCombo.add("5");
				verticalCombo.add("6");
				verticalCombo.add("7");
				verticalCombo.add("8");
				verticalCombo.select(0);
			}
		}
		
		tabFolder = new TabFolder(centerComposite, SWT.NONE);
		tabFolder.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				if( tabFolder.getSelectionIndex() !=  -1 ){
					TabItem tabItem = tabFolder.getItem(tabFolder.getSelectionIndex());
					if( tabItem.getText().equals("JDBC") ){
						jdbcTabItemComposite.loadAliasCombo(WorkbookTabItemComposite.aliasList);
					} else 	if( tabItem.getText().equals("MQQueue CF") ){
						mqQueueCFTabItemComposite.loadAliasCombo(WorkbookTabItemComposite.aliasList);
					} else 	if( tabItem.getText().equals("MQTopic CF") ){
						mqTopicCFTabItemComposite.loadAliasCombo(WorkbookTabItemComposite.aliasList);
					}
					
				}
			}
		});
		tabFolder.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		TabItem tbtmJavaVirtualMachine = new TabItem(tabFolder, SWT.NONE);
		tbtmJavaVirtualMachine.setText("Server");
		
		serverTabItemComposite = new ServerTabItemComposite(tabFolder, SWT.BORDER);
		tbtmJavaVirtualMachine.setControl(serverTabItemComposite);
		
		TabItem tbtmNodes = new TabItem(tabFolder, SWT.NONE);
		tbtmNodes.setText("Nodes");
		
		nodeTabItemComposite = new NodeTabItemComposite(tabFolder, SWT.BORDER);
		tbtmNodes.setControl(nodeTabItemComposite);
		
		TabItem tbtmJcAuthdata = new TabItem(tabFolder, SWT.NONE);
		tbtmJcAuthdata.setText("J2C AuthData");
		
		j2cAuthDataTabItemComposite = new J2CAuthDataTabItemComposite(tabFolder, SWT.NONE);
		tbtmJcAuthdata.setControl(j2cAuthDataTabItemComposite);
		
		TabItem tbtmJdbc = new TabItem(tabFolder, SWT.NONE);
		tbtmJdbc.setText("JDBC");
		
		jdbcTabItemComposite = new JDBCTabItemComposite(tabFolder, SWT.BORDER);
		tbtmJdbc.setControl(jdbcTabItemComposite);
		
		TabItem tbtmListenerPort = new TabItem(tabFolder, SWT.NONE);
		tbtmListenerPort.setText("Listener Port");
		
		listenerPortTabItemComposite = new ListenerPortTabItemComposite(tabFolder, SWT.NONE);
		tbtmListenerPort.setControl(listenerPortTabItemComposite);
		
		TabItem tbtmMqqueue = new TabItem(tabFolder, SWT.NONE);
		tbtmMqqueue.setText("MQQueue");
		
		mqQueueTabItemComposite = new MQQueueTabItemComposite(tabFolder, SWT.NONE);
		tbtmMqqueue.setControl(mqQueueTabItemComposite);
		
		TabItem tbtmMqqueueConnectionFactory = new TabItem(tabFolder, SWT.NONE);
		tbtmMqqueueConnectionFactory.setText("MQQueue CF");
		
		mqQueueCFTabItemComposite = new MQQueueCFTabItemComposite(tabFolder, SWT.NONE);
		tbtmMqqueueConnectionFactory.setControl(mqQueueCFTabItemComposite);
		
		TabItem tbtmMqtopic = new TabItem(tabFolder, SWT.NONE);
		tbtmMqtopic.setText("MQTopic");
		
		mqTopicTabItemComposite = new MQTopicTabItemComposite(tabFolder, SWT.NONE);
		tbtmMqtopic.setControl(mqTopicTabItemComposite);
		
		TabItem tbtmMqtopicConnectionFactory = new TabItem(tabFolder, SWT.NONE);
		tbtmMqtopicConnectionFactory.setText("MQTopic CF");
		
		mqTopicCFTabItemComposite = new MQTopicCFTabItemComposite(tabFolder, SWT.NONE);
		tbtmMqtopicConnectionFactory.setControl(mqTopicCFTabItemComposite);
		
		TabItem tbtmWorkmanager = new TabItem(tabFolder, SWT.NONE);
		tbtmWorkmanager.setText("WorkManager");
		
		workManagerTabItemComposite = new WorkManagerTabItemComposite(tabFolder, SWT.NONE);
		tbtmWorkmanager.setControl(workManagerTabItemComposite);
		
		TabItem tbtmScheduler = new TabItem(tabFolder, SWT.NONE);
		tbtmScheduler.setText("Scheduler");
		
		schedulerTabItemComposite = new SchedulerTabItemComposite(tabFolder, SWT.NONE);
		tbtmScheduler.setControl(schedulerTabItemComposite);
	}

	/**
	 * This method initializes botomComposite
	 * 
	 */
	private void createBotomComposite() {
		createComposite();
	}

	/**
	 * This method initializes composite
	 * 
	 */
	private void createComposite() {
	}

	/**
	 * This method initializes composite1
	 * 
	 */
	private void createComposite1() {

	}

	/**
	 * @return the oKButton
	 */
	public Button getOKButton() {
		return OKButton;
	}

	/**
	 * @return the errorLabel
	 */
	public Label getErrorLabel() {
		return errorLabel;
	}

	/**
	 * @return the retorno
	 */
	public String getRetorno() {
		return retorno;
	}

	/**
	 * @param retorno the retorno to set
	 */
	public void setRetorno(String retorno) {
		this.retorno = retorno;
	}

	private void save(){
		if( instanceNameText.getText().length() == 0 ){
			sendMessage("Name can't be null ");
			return;
		}
		
		if( nodeTabItemComposite.getTable().getItemCount() == 0 ){
			sendMessage("Node can't be null ");
			return;			
		}
		
		String PROJECT_PATH = Database.getProjectPath()+"/"+project.getAlias()+"/";
		WorkBook newWorkbook = createWorkBook();
        Util.writeXML(newWorkbook, PROJECT_PATH, Database.WORKBOOK_FILE_NAME, "CI Package" );
        
        List<String> nodeList = new ArrayList<String>();
        for(int i=0; i < nodeTabItemComposite.getTable().getItemCount(); i++ ){
        	TableItem tableItem = nodeTabItemComposite.getTable().getItem(i);
        	String nodeName = tableItem.getText();
        	nodeList.add(nodeName);
        }
        
        String webservers = "intranet";
        if( webserversCombo.getText().equals("internet") ){
        	webservers = "internet";
        } else if( webserversCombo.getText().equals("internet and webservices") ){
        	webservers = "internet;webservices";
        } else if( webserversCombo.getText().equals("intranet and webservices") ){
        	webservers = "intranet;webservices";
        }
        
        WorkbookService service = new WorkbookService();
        service.exportToken(newWorkbook, project, instanceNameText.getText(),verticalCombo.getText(),webservers,nodeList);
	}
	
	private WorkBook createWorkBook(){
		WorkBook newWorkbook = new WorkBook();
		
		Cell cell = new Cell();
		newWorkbook.setCell(cell);

		cell.setName("@cellName");
		
		Cluster cluster = new Cluster();
        cell.setCluster(cluster);
        
        cluster.setName("@clusterName");
        cluster.setAppName("@appName");
        
        Node node = new Node();
        cluster.addNode(node);

        node.setName("@nodeName");
        
        WorkbookService service = new WorkbookService();
        WorkBook workbookTemplate = service.loadWorkBook(Database.HOME+"/"+Database.WORKBOOK_TEMPLATE_FILE_NAME);
        
        Server server = Util.getCloneServer(workbookTemplate);
        server.resertSchedulers();
        server.resetJDBCProviders();
        server.resetJMSProvider();
        server.resetWorkManagers();
        server.getApplicationServer().getEJBContainer().getMessageListenerService().resetListenerPort();
        
        node.addServer(server);
        
        JavaVirtualMachine jvm = server.getJavaProcessDef().getJavaVirtualMachine();
        
        jvm.setInitialHeapSize( serverTabItemComposite.getInitialHeapSizeText().getText() );
        jvm.setMaximumHeapSize( serverTabItemComposite.getMaxHeapSizeText().getText() );
        jvm.setGenericJvmArguments(serverTabItemComposite.getGenericJVMArgumentsText().getText());
        
        ApplicationServer appServer = server.getApplicationServer();
        appServer.setApplicationClassLoaderPolicy(serverTabItemComposite.getAppClassLoaderPolicyCombo().getText());
        appServer.setApplicationClassLoadingMode(serverTabItemComposite.getAppClassLoaderModeCombo().getText());

        TuningParams tuningParams = appServer.getWebContainer().getSessionManager().getTuningParams();
        tuningParams.setInvalidationTimeout(serverTabItemComposite.getSessionTimeoutText().getText());
        
        if( serverTabItemComposite.getCookieNameText().getText().length() > 0 ){
	        Cookie cookie = appServer.getWebContainer().getSessionManager().getCookie();
	        cookie.setName(serverTabItemComposite.getCookieNameText().getText());
        }
        
        SecurityDomain securityDomain = new SecurityDomain();
        securityDomain.setName("@securityDomainName");
        cluster.setSecurityDomain(securityDomain);
            
        Table j2cAuthDataTable = j2cAuthDataTabItemComposite.getTable();
        for( int i=0; i < j2cAuthDataTable.getItemCount(); i++){
        	 TableItem tableItem = j2cAuthDataTable.getItem(i);  
        	 JAASAuthData jaasAuthData = (JAASAuthData)tableItem.getData();
        	 securityDomain.addJAASAuthData(jaasAuthData);
        }
        
        Table jdbcAuthDataTable = jdbcTabItemComposite.getTable();
        for( int i=0; i < jdbcAuthDataTable.getItemCount(); i++){
	       	 TableItem tableItem = jdbcAuthDataTable.getItem(i);  
	       	 JDBCProvider jdbcProvider = (JDBCProvider)tableItem.getData();
	       	 if( jdbcProvider.getScope().equals("SERVER") ){
	       		 server.addJDBCProvider(jdbcProvider);
	       	 } else if( jdbcProvider.getScope().equals("CLUSTER") ){
	       		cluster.addJDBCProvider(jdbcProvider);
	       	 }
        } 
        
        Table listenerPortTable = listenerPortTabItemComposite.getTable();
        for( int i=0; i < listenerPortTable.getItemCount(); i++){
	       	 TableItem tableItem = listenerPortTable.getItem(i);  
	       	 ListenerPort listenerPort = (ListenerPort)tableItem.getData();
      		 server.getApplicationServer().getEJBContainer().getMessageListenerService().addListenerPort(listenerPort);
        }
        
        Table mqQueueTable = mqQueueTabItemComposite.getTable();
        for( int i=0; i < mqQueueTable.getItemCount(); i++){
	       	 TableItem tableItem = mqQueueTable.getItem(i);  
	       	 MQQueue mqQueue = (MQQueue)tableItem.getData();
	       	 if( mqQueue.getScope().equals("SERVER") ){
	       		 server.getJMSProvider().addMQQueue(mqQueue);
	       	 } else if( mqQueue.getScope().equals("CLUSTER") ){
	       		cluster.getJMSProvider().addMQQueue(mqQueue);
	       	 }
        } 
        
        Table mqQueueCFTable = mqQueueCFTabItemComposite.getTable();
        for( int i=0; i < mqQueueCFTable.getItemCount(); i++){
	       	 TableItem tableItem = mqQueueCFTable.getItem(i);  
	       	 MQQueueConnectionFactory mqQueueCF = (MQQueueConnectionFactory)tableItem.getData();
	       	 if( mqQueueCF.getScope().equals("SERVER") ){
	       		 server.getJMSProvider().addMQQueueConnectionFactory(mqQueueCF);
	       	 } else if( mqQueueCF.getScope().equals("CLUSTER") ){
	       		cluster.getJMSProvider().addMQQueueConnectionFactory(mqQueueCF);
	       	 }
        }   
        
        Table mqTopicTable = mqTopicTabItemComposite.getTable();
        for( int i=0; i < mqTopicTable.getItemCount(); i++){
	       	 TableItem tableItem = mqTopicTable.getItem(i);  
	       	 MQTopic mqTopic = (MQTopic)tableItem.getData();
	       	 if( mqTopic.getScope().equals("SERVER") ){
	       		 server.getJMSProvider().addMQTopic(mqTopic);
	       	 } else if( mqTopic.getScope().equals("CLUSTER") ){
	       		cluster.getJMSProvider().addMQTopic(mqTopic);
	       	 }
        } 
        
        Table mqTopicCFTable = mqTopicCFTabItemComposite.getTable();
        for( int i=0; i < mqTopicCFTable.getItemCount(); i++){
	       	 TableItem tableItem = mqTopicCFTable.getItem(i);  
	       	 MQTopicConnectionFactory mqTopicCF = (MQTopicConnectionFactory)tableItem.getData();
	       	 if( mqTopicCF.getScope().equals("SERVER") ){
	       		 server.getJMSProvider().addMQTopicConnectionFactory(mqTopicCF);
	       	 } else if( mqTopicCF.getScope().equals("CLUSTER") ){
	       		cluster.getJMSProvider().addMQTopicConnectionFactory(mqTopicCF);
	       	 }
        }
        
        Table workManagerTable = workManagerTabItemComposite.getTable();
        for( int i=0; i < workManagerTable.getItemCount(); i++){
	       	 TableItem tableItem = workManagerTable.getItem(i);  
	       	 WorkManager workManager = (WorkManager)tableItem.getData();
      		 server.addWorkManager(workManager);
        }  
        
        Table schedulerTable = schedulerTabItemComposite.getTable();
        for( int i=0; i < schedulerTable.getItemCount(); i++){
	       	 TableItem tableItem = schedulerTable.getItem(i);  
	       	 Scheduler scheduler = (Scheduler)tableItem.getData();
      		 server.addScheduler(scheduler);
        }        
        
        return newWorkbook;
        
	}
	
	public void loadForm(){
		String PROJECT_PATH = Database.getProjectPath()+"/"+project.getAlias();		
		WorkbookService service = new WorkbookService();
		WorkBook workbook = service.loadWorkBook(PROJECT_PATH+"/"+Database.WORKBOOK_FILE_NAME);
		
		String appname = "";
		String nodes = "";
		String webservers = "";
		String vertical = "0";
		String PROP_PATH = Database.getProjectPath()+"/"+project.getAlias()+"/token.properties";
		File f = new File(PROP_PATH);
		if( f.exists() ){
			Properties prop = new Properties();
			FileInputStream inStream;
			try {
				inStream = new FileInputStream(f);
				prop.load(inStream);
				appname = (String)prop.get("appname");
				nodes = (String)prop.get("nodes");
				webservers = (String)prop.get("webservers");
				vertical = (String)prop.get("vertical");
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		if( appname.length() > 0 ){
			instanceNameText.setText(appname);
		}
		
		if( nodes.length() > 0 ){
			StringTokenizer nodeToken = new StringTokenizer(nodes,";");
			while(nodeToken.hasMoreTokens()){
				TableItem tableItem = new TableItem(nodeTabItemComposite.getTable(),SWT.NONE);
				tableItem.setText(nodeToken.nextToken());
			}
		}
		
		if( webservers.length() > 0 ){
			if( webservers.equals("intranet") ){
				webserversCombo.setText("Intranet");
			} else if( webservers.equals("internet") ){
				webserversCombo.setText("Internet");
			} else if( webservers.equals("webservices") ){
				webserversCombo.setText("WebServices");
			} else if( webservers.equals("intranet;webservices") ){
				webserversCombo.setText("Intranet and WebServices");
			} else if( webservers.equals("internet;webservices") ){
				webserversCombo.setText("Intranet and WebServices");
			}
		}
		
		if( vertical.length() > 0 ){
			verticalCombo.setText(vertical);
		}
		
		Cluster cluster = workbook.getCell().getCluster();
		Node node = cluster.getNodes().get(0);
		Server server = node.getServers().get(0);
        
        JavaVirtualMachine jvm = server.getJavaProcessDef().getJavaVirtualMachine();
        
        serverTabItemComposite.getInitialHeapSizeText().setText( jvm.getInitialHeapSize() );
        serverTabItemComposite.getMaxHeapSizeText().setText(jvm.getMaximumHeapSize()); 
        serverTabItemComposite.getGenericJVMArgumentsText().setText(jvm.getGenericJvmArguments());
        
        ApplicationServer appServer = server.getApplicationServer();
        serverTabItemComposite.getAppClassLoaderPolicyCombo().setText(appServer.getApplicationClassLoaderPolicy());
        serverTabItemComposite.getAppClassLoaderModeCombo().setText(appServer.getApplicationClassLoadingMode());

        TuningParams tuningParams = appServer.getWebContainer().getSessionManager().getTuningParams();
        serverTabItemComposite.getSessionTimeoutText().setText(tuningParams.getInvalidationTimeout());
        
        if( serverTabItemComposite.getCookieNameText().getText().length() > 0 ){
	        Cookie cookie = appServer.getWebContainer().getSessionManager().getCookie();
	        serverTabItemComposite.getCookieNameText().setText(cookie.getName());
        }
        
        SecurityDomain securityDomain = cluster.getSecurityDomain();

        for(JAASAuthData jaasAuthData : securityDomain.getJAASAuthDatas()){
        	TableItem tableItem = new TableItem(j2cAuthDataTabItemComposite.getTable(), SWT.NONE); 
			tableItem.setText(0, jaasAuthData.getAlias());
			tableItem.setText(1, jaasAuthData.getUserId());
			tableItem.setData(jaasAuthData);
        }
        
        for( JDBCProvider jdbcProvider : cluster.getJDBCProviders() ){
        	loadJDBC(jdbcProvider);
        } 

        for( JDBCProvider jdbcProvider : server.getJDBCProviders() ){
        	loadJDBC(jdbcProvider);
        } 
        
        MessageListenerService mls = server.getApplicationServer().getEJBContainer().getMessageListenerService();
        for(ListenerPort listenerPort : mls.getListenerPorts() ){
        	TableItem tableItem = new TableItem(listenerPortTabItemComposite.getTable(), SWT.NONE); 
			tableItem.setText(listenerPort.getName());
			tableItem.setData(listenerPort);
        }
        
        for(MQQueue mqQueue : cluster.getJMSProvider().getMQQueues()){
        	TableItem tableItem = new TableItem(mqQueueTabItemComposite.getTable(), SWT.NONE); 
    		tableItem.setText(0, mqQueue.getJndiName());
    		tableItem.setText(1, mqQueue.getName());
    		tableItem.setText(2, mqQueue.getScope());
    		tableItem.setData(mqQueue);        	
        }
        
        for(MQQueue mqQueue : server.getJMSProvider().getMQQueues()){
        	TableItem tableItem = new TableItem(mqQueueTabItemComposite.getTable(), SWT.NONE); 
    		tableItem.setText(0, mqQueue.getJndiName());
    		tableItem.setText(1, mqQueue.getName());
    		tableItem.setText(2, mqQueue.getScope());
    		tableItem.setData(mqQueue);        	
        }        
        
        for(MQQueueConnectionFactory mqQueueCF : cluster.getJMSProvider().getMQQueueConnectionFactories()){
        	TableItem tableItem = new TableItem(mqQueueCFTabItemComposite.getTable(), SWT.NONE); 
    		tableItem.setText(0, mqQueueCF.getJndiName());
    		tableItem.setText(1, mqQueueCF.getName());
    		tableItem.setText(2, mqQueueCF.getScope());
    		tableItem.setData(mqQueueCF);        	
        }
        
        for(MQQueueConnectionFactory mqQueueCF : server.getJMSProvider().getMQQueueConnectionFactories()){
        	TableItem tableItem = new TableItem(mqQueueCFTabItemComposite.getTable(), SWT.NONE); 
    		tableItem.setText(0, mqQueueCF.getJndiName());
    		tableItem.setText(1, mqQueueCF.getName());
    		tableItem.setText(2, mqQueueCF.getScope());
    		tableItem.setData(mqQueueCF);        	
        }
        
        for(MQTopicConnectionFactory mqTopicCF : cluster.getJMSProvider().getMQTopicConnectionFactories()){
        	TableItem tableItem = new TableItem(mqTopicCFTabItemComposite.getTable(), SWT.NONE); 
    		tableItem.setText(0, mqTopicCF.getJndiName());
    		tableItem.setText(1, mqTopicCF.getName());
    		tableItem.setText(2, mqTopicCF.getScope());
    		tableItem.setData(mqTopicCF);        	
        }
        
        for(MQTopicConnectionFactory mqTopicCF : server.getJMSProvider().getMQTopicConnectionFactories()){
        	TableItem tableItem = new TableItem(mqTopicCFTabItemComposite.getTable(), SWT.NONE); 
    		tableItem.setText(0, mqTopicCF.getJndiName());
    		tableItem.setText(1, mqTopicCF.getName());
    		tableItem.setText(2, mqTopicCF.getScope());
    		tableItem.setData(mqTopicCF);        	
        }
        
        for(MQTopic mqTopic : cluster.getJMSProvider().getMQTopics()){
        	TableItem tableItem = new TableItem(mqTopicTabItemComposite.getTable(), SWT.NONE); 
    		tableItem.setText(0, mqTopic.getJndiName());
    		tableItem.setText(1, mqTopic.getName());
    		tableItem.setText(2, mqTopic.getScope());
    		tableItem.setData(mqTopic);        	
        }
        
        for(MQTopic mqTopic : server.getJMSProvider().getMQTopics()){
        	TableItem tableItem = new TableItem(mqTopicTabItemComposite.getTable(), SWT.NONE); 
    		tableItem.setText(0, mqTopic.getJndiName());
    		tableItem.setText(1, mqTopic.getName());
    		tableItem.setText(2, mqTopic.getScope());
    		tableItem.setData(mqTopic);        	
        }        
        
        for(WorkManager workManager : server.getWorkManagers() ){
        	TableItem tableItem = new TableItem(workManagerTabItemComposite.getTable(), SWT.NONE); 
    		tableItem.setText(0, workManager.getJndiName());
    		tableItem.setText(1, workManager.getName());
    		tableItem.setData(workManager);        	
        }
        
        for(Scheduler scheduler : server.getSchedulers() ){
        	TableItem tableItem = new TableItem(workManagerTabItemComposite.getTable(), SWT.NONE); 
    		tableItem.setText(0, scheduler.getJndiName());
    		tableItem.setText(1, scheduler.getName());
    		tableItem.setData(scheduler);        	
        } 
        
	}
	
	public void loadJDBC(JDBCProvider jdbcProvider){
    	if( jdbcProvider.getImplementationClassName().contains("DB2ConnectionPoolDataSource")
    		|| jdbcProvider.getImplementationClassName().contains("DB2XADataSource") ){
			for( DB2UniversalJDBCDriver db2UniversalJDBCDriver : jdbcProvider.getDb2UniversalJDBCDrivers()){
		       	 TableItem tableItem = new TableItem(jdbcTabItemComposite.getTable(), SWT.NONE); 
				 tableItem.setText(0,db2UniversalJDBCDriver.getJndiName() );
				 tableItem.setText(1,db2UniversalJDBCDriver.getName() );
				 if( jdbcProvider.getImplementationClassName().contains("DB2XADataSource") ){
					 tableItem.setText(2,"DB2 Universal JDBC Driver Provider (XA)" );
				 } else {
					 tableItem.setText(2,"DB2 Universal JDBC Driver Provider" );
				 }
				 tableItem.setText(3,jdbcProvider.getScope() );
				 tableItem.setData(jdbcProvider);	       	 
			}
		} else if( jdbcProvider.getImplementationClassName().contains("OracleConnectionPoolDataSource") 
				|| jdbcProvider.getImplementationClassName().contains("OracleXADataSource") ){
			for( OracleJDBCDriver oracleJDBCDriver : jdbcProvider.getOracleJDBCDrivers()){
		       	 TableItem tableItem = new TableItem(jdbcTabItemComposite.getTable(), SWT.NONE); 
				 tableItem.setText(0,oracleJDBCDriver.getJndiName() );
				 tableItem.setText(1,oracleJDBCDriver.getName() );
				 if( jdbcProvider.getImplementationClassName().contains("OracleXADataSource") ){
					 tableItem.setText(2,"Oracle JDBC Driver (XA)" );
				 } else {
					 tableItem.setText(2,"Oracle JDBC Driver" );
				 }
				 tableItem.setText(3,jdbcProvider.getScope() );
				 tableItem.setData(jdbcProvider);       
			}
		} else if( jdbcProvider.getImplementationClassName().contains("JtdsDataSource") ){
			for( SQLServerJTDSDriver sqlServerJTDSDriver : jdbcProvider.getSQLServerJTDSDrivers()){
		       	 TableItem tableItem = new TableItem(jdbcTabItemComposite.getTable(), SWT.NONE); 
				 tableItem.setText(0,sqlServerJTDSDriver.getJndiName() );
				 tableItem.setText(1,sqlServerJTDSDriver.getName() );
				 tableItem.setText(2,"JTDS JDBC Driver for SQL" );
				 tableItem.setText(3,jdbcProvider.getScope() );
				 tableItem.setData(jdbcProvider);       
			}
		} else if( jdbcProvider.getImplementationClassName().contains("SQLServerConnectionPoolDataSource") 
			    || jdbcProvider.getImplementationClassName().contains("SQLServerXADataSource") ){
			for( SQLServerJDBCDriver sqlServerJDBCDriver : jdbcProvider.getSQLServerJDBCDrivers()){
		       	 TableItem tableItem = new TableItem(jdbcTabItemComposite.getTable(), SWT.NONE); 
				 tableItem.setText(0,sqlServerJDBCDriver.getJndiName() );
				 tableItem.setText(1,sqlServerJDBCDriver.getName() );
				 if( jdbcProvider.getImplementationClassName().contains("SQLServerXADataSource") ){
					 tableItem.setText(2,"Microsoft SQL Server JDBC Driver (XA)" );
				 } else {
					 tableItem.setText(2,"Microsoft SQL Server JDBC Driver" );
				 }
				 tableItem.setText(3,jdbcProvider.getScope() );
				 tableItem.setData(jdbcProvider);       
			}
		}
		
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
    
	/**
	 * @return the project
	 */
	public Project getProject() {
		return project;
	}

	/**
	 * @param project the project to set
	 */
	public void setProject(Project project) {
		this.project = project;
	}	
	
} // @jve:decl-index=0:visual-constraint="10,10"
