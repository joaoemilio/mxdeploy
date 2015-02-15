package com.mxdeploy.launcher.account.event;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.eclipse.swt.widgets.Button;

import com.mxdeploy.AccountConfig;
import com.mxdeploy.api.domain.Database;
import com.mxdeploy.launcher.account.AccountComposite;
import com.mxdeploy.swt.MainShell;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

public class AccountEvent {
	
	public static List<String> getAccounts(){
		List<String> accounts = new ArrayList<String>();
		 
		File file = new File(Database.WORKSPACE_PATH);
		File[] files = file.listFiles();
		
		if(!file.exists()){
		  System.out.println("Workspace does not exist !");
		  MainShell.sendMessage("Workspace does not exist. " + file.getAbsolutePath() );
		  System.exit(5000);
		}
		
		for(int i=0; i < files.length; i++){
			if(files[i].isDirectory() && !files[i].getName().equals(".svn")){
				accounts.add(files[i].getName());
			}
		}
		
		accounts.add("create new workspace");

		return accounts;
	}	
	
	public static void execute(AccountComposite composite){
		
		//AccountComposite composite = (AccountComposite)((Button)e.widget).getParent().getParent().getParent();
		
		String newAccount = composite.getCCombo().getText().trim();
		File file = new File(Database.WORKSPACE_PATH + "/"+newAccount);
		if(file.exists()){
			composite.setRetorno(composite.getCCombo().getText());
			composite.getShell().close();

			return;
		}
		
		file.mkdir();
		
		file = new File(Database.WORKSPACE_PATH + "/"+newAccount+"/projects");
		file.mkdir();

		file = new File(Database.WORKSPACE_PATH + "/"+newAccount+"/beanshell");
		file.mkdir();

		file = new File(Database.WORKSPACE_PATH + "/"+newAccount+"/beanshell/bsh");
		file.mkdir();
		
		file = new File(Database.WORKSPACE_PATH + "/"+newAccount+"/servers");
		file.mkdir();		

		file = new File(Database.WORKSPACE_PATH + "/"+newAccount+"/servers/properties");
		file.mkdir();		
		
		file = new File(Database.WORKSPACE_PATH + "/"+newAccount+"/scripts");
		file.mkdir();		
		
		file = new File(Database.WORKSPACE_PATH + "/"+newAccount+"/scripts/lib");
		file.mkdir();	
		
		AccountConfig ac = new AccountConfig();
		ac.setName(newAccount);
		ac.setDescription(newAccount);
		XStream xstream = new XStream(new DomDriver());
		String xml = xstream.toXML(ac);
		try {
			FileUtils.writeStringToFile(new File(Database.WORKSPACE_PATH + "/"+newAccount+"/config.xml"), xml);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
//		String scriptStartGateway = "start_gateway.sh";
//		String scriptStartProxy = "start_proxy.sh";
//		String scriptStopGateway = "stop_gateway.sh";
//		if ( !System.getProperty("os.name").contains("Linux")){
//			 scriptStartGateway = "start_gateway.bat";
//			 scriptStartProxy = "start_proxy.bat";
//			 scriptStopGateway = "stop_gateway.bat";
//		}
		
		File module_template_dir = new File(Database.LIBRARY_PATH+"/templates/modules");
		File account_dir = new File(Database.WORKSPACE_PATH + "/"+newAccount);
		
//		File scriptStartGatewayFile = new File(Database.LIBRARY_PATH+"/templates/"+scriptStartGateway);
//		File scriptStartProxyFile = new File(Database.LIBRARY_PATH+"/templates/"+scriptStartProxy);
//		File scriptStopGatewayFile = new File(Database.LIBRARY_PATH+"/templates/"+scriptStopGateway);
		
		try {
			FileUtils.copyDirectoryToDirectory(module_template_dir, account_dir);
			
//			FileUtils.copyFileToDirectory(scriptStartGatewayFile, account_dir);
//			FileUtils.copyFileToDirectory(scriptStartProxyFile, account_dir);
//			FileUtils.copyFileToDirectory(scriptStopGatewayFile, account_dir);
		} catch (IOException e2) {
			e2.printStackTrace();
		}
		
		composite.setRetorno(newAccount);
		
		composite.getShell().close();
	}
	
}
