package com.mxssh;

import java.io.IOException;

import com.mxdeploy.api.domain.middleware.WebSphere60;
import com.mxdeploy.api.domain.middleware.WebSphere61;
import com.mxdeploy.api.domain.middleware.WebSphere70;
import com.mxdeploy.api.domain.middleware.WebSphereAppServer;
import com.mxscript.MXBeanShellException;

public class WebSphereService {
	
	private WebSphereAppServer domain;
	private SSHServiceNew ssh = null;
	private static LogService logService = LogService.getInstance();
	private SSHHelper helper = null;
	
	public WebSphereService() throws MXBeanShellException{
		this.ssh = new SSHServiceNew();
		this.helper = new SSHHelper(ssh);	
	}
	
	public WebSphereService(SSHServiceNew ssh) throws MXBeanShellException{
		this.ssh = ssh;
		this.helper = new SSHHelper(ssh);	
	}
	
	public WebSphereAppServer getDomain(){
		return domain;
	}
	
	public WebSphereAppServer createDomain() throws CannotDiscoverWebSphereException, IOException, InterruptedException, SSHException, MXBeanShellException {
		//is WebSphere running?
		String ids[] = helper.getProcessIDs("com.ibm.ws.bootstrap.WSLauncher");

		if(ids == null){
			return null;
		}
		
		String processID = ids[0];
		if(processID.equals("root")) {
			throw new CannotDiscoverWebSphereException("WebSphere is running as root");
		}
		
		//get the process ID groups
		String userIDGroups = helper.getUserIDGroups(processID);
		
		try{
			helper.sudoSUToUser(processID);
		}catch(CannotSudoException e){
			throw new CannotDiscoverWebSphereException(e.getMessage());
		}
		
		//what is the WAS_HOME?
		String wasHome = getWasHome();
		
		//get maint. packages
		String maintPackages = getMaintPackages(wasHome);
		
		//what is the WAS version?
		String wasVersion = getWasVersion(maintPackages);
		
		//logService.info(log)
		if(wasVersion == null || wasVersion.trim().equals("")) return null;
		
		//create domain according to the version
		String wasVersionParts[] = wasVersion.split("\\.");
		if(wasVersionParts != null){
			int wasVersionLevel = new Integer(wasVersionParts[0]);
			int wasVersionMajor = new Integer(wasVersionParts[1]);
			int wasVersionMedium = new Integer(wasVersionParts[2]);
			int wasVersionLow = new Integer(wasVersionParts[3]);
			if( wasVersionLevel == 7 ){
				domain = new WebSphere70();
			}else if(wasVersionLevel == 6){
				if(wasVersionMajor == 0){
					domain = new WebSphere60();
				}else{
					domain = new WebSphere61();
				}
			}else{
				domain = null;
				return null;
			}
			domain.setFixPackLevel(wasVersionLow);
			domain.setWasHome(wasHome);
			domain.setVersion(wasVersion);
			domain.setProcessID(processID, userIDGroups);
			domain.setMaintPack(maintPackages);
		}
		
		helper.exitUser();
		
		return domain;

	}

	/**
	 * get the was version of the was home informed as a parameter
	 * @param wasHome
	 * @return
	 */
	private String getWasVersion(String maintPackages){
		String message = maintPackages;
		logService.debug("\n\n\n\n");
		logService.debug(message);
		logService.debug("\n\n\n\n");
		int indexIP = message.indexOf("Installed Product");
		int indexV = message.indexOf("Version", indexIP);
		int indexDot = message.indexOf(".", indexV);
		int indexVEnd = message.indexOf("\n", indexDot);
		String version = message.substring(indexDot-1,indexVEnd-1);
		version = version.trim();
		version.replaceAll("\n", "");
		version.replaceAll("\t", "");
		return version;
	}
	
	/**
	 * get the was version of the was home informed as a parameter
	 * @param wasHome
	 * @return
	 * @throws InterruptedException 
	 * @throws MXBeanShellException 
	 */
	private String getMaintPackages(String wasHome) throws IOException, InterruptedException, MXBeanShellException {
		String message = ssh.executeCommand(wasHome + "/bin/versionInfo.sh");
		logService.debug(wasHome + "/bin/versionInfo.sh");
		return message;
	}
	
	/**
	 * get WAS HOME directory from ps -ef command
	 * @return
	 * @throws InterruptedException 
	 * @throws MXBeanShellException 
	 */
	private String getWasHome() throws IOException, InterruptedException, MXBeanShellException {
		String message = helper.getProcessLineDetails("com.ibm.ws.bootstrap.WSLauncher");
		logService.debug("\n\n\n\n");
		logService.debug("was home");
		logService.debug(message);
		logService.debug("\n\n\n\n");
		String[] lines = message.split( "-D" );
		String wasRoot = "";
		for(String line: lines){
			if(line.contains("server.root")){
				logService.debug(line);
				int indSlash = line.indexOf("/");
				int indSpace = line.indexOf(" ");
				wasRoot = line.substring(indSlash, indSpace);
				logService.debug("WAS Root\n" + wasRoot);
			}
		}
		return wasRoot;
	}
	
	public String assessPatchAdvisory(WebSphereAppServer domain, Integer number){
		String assessment = "FAIL";
		
		if(domain == null) return "PASS - No WAS running"; 
		
		if(number == 100310950){
			assessment = assessPatchAdvisory100310950(domain);
		}else if(number == 100310822){
			assessment = assessPatchAdvisory100310822(domain);
		}else if(number == 100310594){
			assessment = assessPatchAdvisory100310594(domain);
		}
		
		return assessment;
	}

	public String assessPatchAdvisory100310950(WebSphereAppServer domain){
		String assessment = "FAIL";

		String versionParts[] = domain.getVersion().split("\\.");
		
		if(versionParts[0].equals("7")){
			if(domain.getFixPackLevel() >= 5){
				assessment = "PASS";
			}
		}else if(versionParts[0].equals("6")){
			if(versionParts[1].equals("1")){
				if(domain.getFixPackLevel() >= 25){
					assessment = "PASS";
				}
			}else if(versionParts[1].equals("0")){
				if(domain.getFixPackLevel() >= 35){
					assessment = "PASS";
				}
			}else{
				assessment = "PASS";
			}
		}
		
		if(assessment.equals("FAIL")){
			if(domain.isMaintPackInstalled("PK80596")){
				assessment = "PASS - PK80596 installed";
			}
		}
		return assessment;
	}

	public String assessPatchAdvisory100310822(WebSphereAppServer domain){
		String assessment = "FAIL";

		String versionParts[] = domain.getVersion().split("\\.");
		
		if(versionParts[0].equals("7")){
			if(domain.getFixPackLevel() >= 5){
				assessment = "PASS";
			}
		}else if(versionParts[0].equals("6")){
			if(versionParts[1].equals("1")){
				if(domain.getFixPackLevel() >= 25){
					assessment = "PASS";
				}
			}else{
				assessment = "PASS";
			}
		}
		
		if(assessment.equals("FAIL")){
			if(domain.isMaintPackInstalled("PK78134")){
				assessment = "PASS - PK78134 installed";
			}
		}
		
		return assessment;
	}

	public String assessPatchAdvisory100310594(WebSphereAppServer domain){
		String assessment = "FAIL";

		String versionParts[] = domain.getVersion().split("\\.");
		
		if(versionParts[0].equals("7")){
			if(domain.getFixPackLevel() >= 5){
				assessment = "PASS";
			}
		}else if(versionParts[0].equals("6")){
			if(versionParts[1].equals("1")){
				if(domain.getFixPackLevel() >= 25){
					assessment = "PASS";
				}
			}else{
				assessment = "PASS";
			}
		}
		
		if(assessment.equals("FAIL")){
			if(domain.isMaintPackInstalled("PK72138")){
				assessment = "PASS - PK72138 installed";
			}
		}
		
		return assessment;
	}

}
