package com.mxssh;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.mxdeploy.api.domain.middleware.IBMHTTPServer;
import com.mxdeploy.api.domain.middleware.IBMHTTPServer60;
import com.mxdeploy.api.domain.middleware.IBMHTTPServer61;
import com.mxdeploy.api.domain.middleware.IBMHTTPServer70;
import com.mxdeploy.api.domain.middleware.IHSConfig;
import com.mxdeploy.api.domain.middleware.IHSConfigLocation;
import com.mxdeploy.api.domain.middleware.VirtualHost;
import com.mxscript.MXBeanShellException;

public class IHSService {
	
	private SSHServiceNew ssh = null;
	private SSHHelper helper = null;
	private static LogService logService = LogService.getInstance();
	
	public IHSService() throws MXBeanShellException{
		this.ssh = new SSHServiceNew();
		this.helper = new SSHHelper(ssh);	
	}
	
	public IHSService(SSHServiceNew ssh) throws MXBeanShellException{
		this.ssh = ssh;
		this.helper = new SSHHelper(ssh);
	}
	
	public IBMHTTPServer createDomain() throws IOException, InterruptedException, SSHException, MXBeanShellException {
		IBMHTTPServer domain = null;
		
		//is IHS running?
		String ids[] = helper.getProcessIDs("\"httpd -d\"");

		if(ids == null){
			return null;
		}
		
		String processID = ids[0];
		if(processID.equals("root")) processID = ids[1];
		
		//get the process ID groups
		String userIDGroups = helper.getUserIDGroups(processID);
		
		//what is the ServerRoot?
		String serverRoot = getServerRoot();
		
		//what is the IHS version?
		String ihsVersion = getIHSVersion(serverRoot);
		if(ihsVersion == null || ihsVersion.trim().equals("")) return null;
		
		//create domain according to the version
		String ihsVersionParts[] = ihsVersion.split("\\.");
		if(ihsVersionParts != null){
			int ihsVersionLevel = new Integer(ihsVersionParts[0]);
			int ihsVersionMajor = new Integer(ihsVersionParts[1]);
			int ihsVersionMedium = 0;
			ihsVersionMedium = new Integer(ihsVersionParts[2]);
			int ihsVersionLow = 0;
			String ihsVersionFinal[] = ihsVersionParts[3].split("-");
			ihsVersionLow = new Integer(ihsVersionFinal[0]);
			
			if( ihsVersionLevel == 7 ){
				domain = new IBMHTTPServer70();
			}else if(ihsVersionLevel == 6){
				if(ihsVersionMajor == 0){
					domain = new IBMHTTPServer60();
				}else{
					domain = new IBMHTTPServer61();
				}
			}else if(ihsVersionLevel == 2){
				domain = new IBMHTTPServer();
			}else{
				domain = null;
				return null;
			}
			domain.setFixPackLevel(ihsVersionLow);
			domain.setServerRoot(serverRoot);
			domain.setVersion( ihsVersionLevel + "." + ihsVersionMajor + "." + ihsVersionMedium + "." + ihsVersionLow );
			domain.setProcessID(processID, userIDGroups);
			if(ihsVersionLevel > 2){
				String gskitVersion = getGSKitVersion();
				domain.setGskitVersion(gskitVersion);
			}

			//parseIHS config file
			IHSConfig config = parseIHSConfig(domain);	
			domain.setIHSConfig(config);
		}
		
		return domain;
	}

	/**
	 * get the IHS version of the was home informed as a parameter
	 * @param wasHome
	 * @return
	 * @throws InterruptedException 
	 * @throws MXBeanShellException 
	 */
	private String getIHSVersion(String serverRoot) throws IOException, InterruptedException, MXBeanShellException {
		String message = ssh.executeCommand("sudo " + serverRoot + "/bin/apachectl -version ");
		int indexSlash = message.indexOf("/");
		int indexSpace = message.indexOf(" ",indexSlash);
		String version = message.substring(indexSlash+1, indexSpace);
		logService.debug("IHS version: \n" + version);
		return version;
	}
	
	/**
	 * get SERVER ROOT directory from ps -ef command
	 * @return
	 * @throws InterruptedException 
	 * @throws MXBeanShellException 
	 */
	private String getServerRoot() throws IOException, InterruptedException, MXBeanShellException {
		String message = helper.getProcessLineDetails("httpd");
		String[] lines = message.split( "\r\n" );
		String line = lines[0];
		logService.debug(line);
		int minusD = line.indexOf("-d")+3;
		int spaceAfterMinusD = line.indexOf("-k")-1;
		//if(spaceAfterMinusD == -1)
		String serverRoot = line.substring(minusD, spaceAfterMinusD);
		return serverRoot;
	}
	
	private IHSConfig parseIHSConfig(IBMHTTPServer domain){
		IHSConfig config = new IHSConfig();
		
		String destinationDir = "c:/temp/" + ssh.getSshSession().getHost() + "/";
		
		File dir = new File(destinationDir);
		dir.mkdir();
		
		try {
			org.apache.commons.io.FileUtils.cleanDirectory(dir);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String destinationFile = "httpd.conf";
		
		try{
			SCPService scp = new SCPService(ssh);
			scp.downloadFile(domain.getServerRoot() + "/conf/", "httpd.conf", destinationDir, destinationFile);
		}catch(Exception e){
			return null;
		}

		File httpdConfFile = new File(destinationDir + destinationFile);
		List<String> lines = null;
		if(httpdConfFile.exists()){
			lines = FileUtilDummy.getInstance().getContents(httpdConfFile);
		}
		
		List<IHSConfigLocation> list = parseIHSConfigLocation(lines);
		config.setIHSConfigLocations(list);

		List<IHSConfigLocation> list2 = parseIHSConfigLocationMatch(lines);
		config.setIHSConfigLocationsMatch(list2);
		
		List<VirtualHost> listVH = parseIHSConfigVirtualHost(lines);
		config.setVirtualHosts(listVH);
		
		return config;		
	}
	
	private List<IHSConfigLocation> parseIHSConfigLocation(List<String> lines){
		List<IHSConfigLocation> list = new ArrayList<IHSConfigLocation>();
		IHSConfigLocation configLocation = null;
		String[] aux = null;
		for(int i=0; i < lines.size(); i++){
			String line = lines.get(i);
			int j = i;
			if(line != null && !line.trim().equals("")){
				if(line.substring(0,1).equals("#")) continue;
			}
			if(line.contains("<Location")){
				configLocation = new IHSConfigLocation();
				for(; j < lines.size(); j++){
					line = lines.get(j);
					logService.info(line);
					if(line.contains("SetHandler")){
						aux = line.split(" "); 
						configLocation.setSetHandler(aux[1]);
					}
					
					if(line.contains("Order")){
						aux = line.split(" "); 
						configLocation.setOrder(aux[1]);
					}
					
					if(line.contains("Deny")){
						aux = line.split(" from "); 
						configLocation.setOrder(aux[1]);
					}
					/*
					if(line.contains("Allow")){
						aux = line.split(" from "); 
						configLocation.setOrder(aux[1]);
					}
					*/
					
					if(line.contains("</")) break;
				}
			}
			list.add(configLocation);
			i = j;
		}
		return list;	
	}
	
	private List<IHSConfigLocation> parseIHSConfigLocationMatch(List<String> lines){
		List<IHSConfigLocation> list = new ArrayList<IHSConfigLocation>();
		IHSConfigLocation configLocation = null;
		String[] aux = null;
		for(int i=0; i < lines.size(); i++){
			String line = lines.get(i);
			int j = i;
			if(line != null && !line.trim().equals("")){
				if(line.substring(0,1).equals("#")) continue;
			}
			if(line.contains("<LocationMatch")){
				configLocation = new IHSConfigLocation();
				for(; j < lines.size(); j++){
					line = lines.get(j);
					logService.info(line);
					if(line.contains("</")) break;
				}
			}
			list.add(configLocation);
			i = j;
		}
		return list;	
	}
	
	private List<VirtualHost> parseIHSConfigVirtualHost(List<String> lines){
		List<VirtualHost> list = new ArrayList<VirtualHost>();
		String[] aux = null;
		for(int i=0; i < lines.size(); i++){
			String line = lines.get(i);
			int j = i;
			if(line != null && !line.trim().equals("")){
				if(line.substring(0,1).equals("#")) continue;
			}
			if(line.contains("<VirtualHost")){
				VirtualHost vh = new VirtualHost();
				for(; j < lines.size(); j++){
					line = lines.get(j);
					logService.info(line);
					if(line.contains("</")) break;
				}
				list.add(vh);
			}
			i = j;
		}
		return list;	
	}
	
	private String getGSKitVersion() throws IOException, InterruptedException, MXBeanShellException {
		String gskit = "";
		String message = ssh.executeCommand("whereis gsk7ver");
		logService.debug(message);
		String[] lines = message.split( "\r\n" );
		for(String line: lines){
			if(line.contains("gsk7ver")){
				int indSlash = message.indexOf("/");
				String gskitBin = message.substring(indSlash);
				message = ssh.executeCommand("sudo " + gskitBin );
				if(message.contains("not allowed")) {
					return "0.0.0.0.0";
				}
				logService.debug(message);

				int indexIP = message.indexOf("KJNI");
				int indexV = message.indexOf("ProductVersion", indexIP);
				int indexDot = message.indexOf(".", indexV);
				int indexVEnd = message.indexOf("\n", indexDot);
				gskit = message.substring(indexDot-1,indexVEnd-1);
				gskit = gskit.trim();
				gskit.replaceAll("\n", "");
				gskit.replaceAll("\t", "");
			}
		}
		return gskit;
		
	}
	
	public String assessEntityXssFinding(IBMHTTPServer domain){
		String assessment = "FAIL";

		String versionParts[] = domain.getVersion().split("\\.");
		
		if(versionParts[0].equals("6")){
			if(versionParts[1].equals("1")){
				if(domain.getFixPackLevel() >= 27){
					assessment = "PASS";
				}
			}else if(versionParts[1].equals("0")){
				if(domain.getFixPackLevel() >= 37){
					assessment = "PASS";
				}
			}else{
				assessment = "PASS";
			}
		}
		
		return assessment;
	}
	
	public String assessServerStatusFinding(IBMHTTPServer domain){
		String assessment = "FAIL";

		if(!domain.isServerStatusEnabled()){
			assessment = "PASS";
		}
		
		return assessment;
	}
	
}
