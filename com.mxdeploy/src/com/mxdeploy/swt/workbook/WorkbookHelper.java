package com.mxdeploy.swt.workbook;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.TreeItem;

import com.mxdeploy.api.domain.Database;
import com.mxdeploy.api.domain.Project;
import com.mxdeploy.api.service.WorkbookService;
import com.mxdeploy.images.Constant;
import com.mxdeploy.swt.MainShell;
import com.mxdeploy.swt.explorer.helper.ControlPanelHelper;
import com.wds.Util;
import com.wds.bean.Cell;
import com.wds.bean.Cluster;
import com.wds.bean.Node;
import com.wds.bean.WorkBook;
import com.wds.bean.security.SecurityDomain;
import com.wds.bean.server.Server;

public class WorkbookHelper {
	//static Logger logger = Logger.getLogger(ServerEditHelper.class);
	
	private WorkbookComposite composite = null;
	private Project project;
		
	public WorkbookHelper(WorkbookComposite composite) {
		this.composite = composite;
	}

	protected void close(){
		Shell shell = (Shell)composite.getParent();
		shell.close();
		shell.dispose();
	}
	
	protected boolean save(){
		
		if( composite.nameText.getText().length() == 0 ){
			composite.getErrorLabel().setText("        Name is mandatory !");
			composite.nameText.setFocus();
			return false;			
		}
		
		if( composite.nodeTable.getItemCount() == 0 ){
			composite.getErrorLabel().setText("        Node is mandatory !");
			composite.nodeNameText.setFocus();
			return false;			
		}
		
		TreeItem treeItemProject = MainShell.getControlPanelHelper().getTreeItemProjectSelected(MainShell.getControlPanelHelper().getMyProjectTreeItemSelected());
		
		WorkBook workbook = new WorkBook();
		
		Cell cell = new Cell();
		workbook.setCell(cell);

		cell.setName("@cellName");
		
		Cluster cluster = new Cluster();
        cell.setCluster(cluster);
        
        cluster.setName("@clusterName");
        cluster.setAppName("@appName");

        SecurityDomain securityDomain = new SecurityDomain();
        securityDomain.setName("@securityDomainName");
        cluster.setSecurityDomain(securityDomain);
        
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
		
		String PROJECT_PATH = Database.getProjectPath()+"/"+project.getAlias();
		
		File f = new File(PROJECT_PATH+"/"+Database.WORKBOOK_FILE_NAME);
		if( !f.exists() ){
			TreeItem workbookTreeItemXML = new TreeItem(treeItemProject,SWT.NONE);
			workbookTreeItemXML.setText("WorkBook");
			workbookTreeItemXML.setImage(Constant.IMAGE_WORKBOOK );
			workbookTreeItemXML.setData(workbook);	
		}
        
        Util.writeXML(workbook, PROJECT_PATH, Database.WORKBOOK_FILE_NAME, "CI Package" );
        
        
        List<String> nodeList = new ArrayList<String>();
        for(int i=0; i < composite.nodeTable.getItemCount(); i++ ){
        	TableItem tableItem = composite.nodeTable.getItem(i);
        	String nodeName = tableItem.getText();
        	nodeList.add(nodeName);
        }
        
        String webservers = "intranet";
        if( composite.webServerCombo.getText().equals("Internet") ){
        	webservers = "internet";
        } else if( composite.webServerCombo.getText().equals("Internet and WebServices") ){
        	webservers = "internet;webservices";
        } else if( composite.webServerCombo.getText().equals("Intranet and WebServices") ){
        	webservers = "intranet;webservices";
        }
        service.exportToken(workbook,project, composite.nameText.getText(),composite.verticalCombo.getText(),webservers,  nodeList);
        
		//service.writeXML(workbook, project.getAlias());
		
		//service.writeToken(workbookProject, project.getAlias());
		return true;
	}
	
	/**
	 * @return the workingSetHelper
	 */
	public ControlPanelHelper getWorkingSetHelper() {
		return MainShell.getControlPanelHelper();
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

}
