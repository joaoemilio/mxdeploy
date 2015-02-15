package com.mxssh.service.was;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import org.apache.log4j.Logger;

import com.mxdeploy.api.domain.Profile;
import com.mxscript.MXBeanShellException;
import com.mxssh.SSHServiceNew;
import com.mxssh.service.MiddlewareService;

public abstract class WebSphereAppServerService extends MiddlewareService {

//	protected Logger logger = Logger.getLogger("WebSphereAppServerService");
//	protected SSHServiceNew sshClient = null;
//	
//	public WebSphereAppServerService(SSHServiceNew sshClient) {
//		this.sshClient = sshClient;
//	}
//
//	public boolean chkPath(String path, char t) throws IOException, InterruptedException, MXTerminalBeanShellException {
//		logger.debug("chkPath");
//		String command = null;
//		//
//		// 'd' for Directory.
//		// 'f' for File.
//		//
//		switch (t){
//			case 'd':
//				command = "[ -d " + path + " ]&& echo yes";
//				logger.debug("COMMAND - " + command);
//
//				break;
//			case 'f':
//				command = "[ -r " + path + " ]&& echo yes";
//				logger.debug("COMMAND - " + command);
//				break;
//		}
//
//		String result = sshClient.executeCommand(command);
//		logger.debug("RETURN - " + result);
//
//		if(result.contains("yes") ) {
//			return true;
//		} else {
//			return false;
//		}
//	}
//
//	public boolean verifyProc(String proc) throws IOException, InterruptedException{
//		logger.debug("verifyProc");
//		String retorno = null;
//		if (proc.equals("was")){
//			retorno = sshClient.executeCommand("ps -ef | grep -v grep | grep java | grep -i websphere > /dev/null && echo yes");
//		} else if (proc.equals("ihs")){
//			retorno = sshClient.executeCommand("ps -ef | grep -v grep | grep httpd > /dev/null && echo yes");
//		}
//
//		if (retorno.contains("yes")){
//			return true;
//		} else {
//			return false;
//		}
//	}
//
//	public String getWhoami() throws IOException, InterruptedException{
//		logger.debug("getWhoami");
//		String retorno = sshClient.executeCommand("whoami");
//		if(retorno.contains("whoami")){
//		   retorno = retorno.replace("whoami", "");
//		}
//		if(retorno.indexOf("\n")>0){
//		   retorno = retorno.substring( retorno.indexOf("\n") );
//		}
//		logger.debug("RETURN - "+retorno.trim());
//		return retorno.trim();
//	}
//
//	public String getIP(String hostname) throws IOException, InterruptedException{
//		logger.debug("getIP");
//		String ip = sshClient.executeCommand("ping -c1 "+hostname+" | head -1 | awk '{split($3,a,\"(\"); split(a[2],b,\")\"); print b[1]; }'");
//		logger.info("IP = "+ip);
//		return ip;
//	}
//
//	public String getOwner(String user) throws IOException, InterruptedException{
//		logger.debug("getOwner");
//		sshClient.executeCommand("iest_owner=`ps -ef | grep Dwas.install.root | grep -v grep | awk '{ print $1 }'`");
//		String owner = sshClient.executeCommand("echo $iest_owner | awk '{split($0,a,\" \"); for(word in a) if(index(a[word],\""+user+"\")!=0){ print a[word]; break; } }'");
//		logger.info("OWNER = "+owner);
//		return owner;
//	}
//
//	public List<String> getWasHomeList(List<String> homePathList) throws IOException, InterruptedException{
//	   logger.debug("getWasHomeList");
//	   List<String> homeList = new ArrayList<String>();
//
//	   for(String homePath: homePathList){
//			List<String> list = getWasHomes( homePath );
//			if(list!=null && list.size() > 0){
//				for(String home : list ){
//					homeList.add(homePath+"/"+home);
//					logger.info("ADD WAS_HOME = "+home);
//				}
//			}
//	   }
//
//	   return homeList;
//
//	}
//
//	public List<String> getWasHomes(String home) throws IOException, InterruptedException{
//		logger.debug("getWasHomes - "+home);
//		String command = "ls -l "+home+" | awk '{ if ( $1 ~ /(d)/ ) { print $9; } }' | xargs";
//		logger.debug("COMMAND - "+command);
//		String retorno = sshClient.executeCommand("ls -l "+home+" | awk '{ if ( $1 ~ /(d)/ ) { print $9; } }' | xargs");
//		logger.debug("RETURN - "+retorno);
//		if( retorno.contains("not found") ) {
//			return null;
//		}
//
//		List<String> listHomes = new ArrayList<String>();
//		StringTokenizer token = new StringTokenizer(retorno," ");
//		while(token.hasMoreTokens()){
//			String value = token.nextToken();
//			if(!value.trim().equals("Deployment") && !value.trim().contains("PortalServer") && !value.trim().contains("pdate") && !value.trim().contains("Plugin") && !value.contains("lost+found") ){
//				command = "cd "+home+"/"+value+"/bin";
//				logger.debug("COMMAND - "+command);
//				String pathHome = sshClient.executeCommand(command);
//				logger.debug("RETURN - "+pathHome);
//
//				if( !pathHome.contains("not found") ) {
//					logger.debug("ADD LIST_HOMES - "+pathHome);
//					listHomes.add( value.trim() );
//				}
//			}
//		}
//		return listHomes;
//	}
//
//	public List<String> getWasHomes() throws IOException, InterruptedException{
//		logger.debug("getWasHomes ");
//
//		// Return all running WAS_HOMES
//		String retorno = sshClient.executeCommand("ps -ef | grep -v grep | grep Dwas.install.root | awk '{ split($0, a, \"-Dwas.install.root=\"); split(a[2], b, \" \"); print b[1] }'|sed 's/\");//'|sort |uniq |xargs");
//		//String retorno = sshClient.executeCommand("echo $iest_home | awk '{split($0,a,\" \"); for(word in a) if(index(a[word],\"AppServer\")!=0){ print a[word]; break; } }'");
//		logger.debug("RETURN - "+retorno);
//
//		// was is not running.
//		if (retorno.trim().length()==0){
//			 retorno = "/opt/ibm/dmgr/websphere /opt/ibm/lc/dmgr/websphere /opt/ibm/lc/websphere /opt/IBM/WebSphere/AppServer /opt/IBM/WebSphereN1/AppServer /opt/IBM/WebSphere/ProcServer /opt/WebSphere/AppServer /usr1/IBM/WebSphere61/AppServer /usr1/IBM/WebSphere62/ProcServer /usr/IBM/WebSphere/AppServer /usr/IBM/WebSphere/ProcServer /usr/WebSphere/AppServer /usr/WebSphere/DeploymentManager /usr/WebSphereV61/AppServer";
//		}
//
//		StringTokenizer token = new StringTokenizer(retorno," ");
//		List<String> listHomes = new ArrayList<String>();
//
//		while(token.hasMoreTokens()){
//			String value = token.nextToken();
//		//	if(!value.trim().equals("Deployment") && !value.trim().contains("PortalServer") && !value.trim().contains("pdate") && !value.trim().contains("Plugin") && !value.contains("lost+found") ){
//			if(!value.trim().contains("pdate") && !value.trim().contains("Plugin") && !value.contains("lost+found") ){
//				boolean command = chkPath(value, 'd');
//				if(command){
//					logger.debug("ADD LIST_HOMES - " + value.trim());
//					listHomes.add( value.trim());
//				}
//			}
//		}
//
//		if(listHomes.isEmpty()){
//			listHomes.add("notfound");
//		}
//		return listHomes;
//	}
//
//	public List<String> getIhsHomes() throws IOException, InterruptedException{
//		logger.debug("getIhsHomes ");
//
//		// Return all running IHS_HOMES
//		String retorno = sshClient.executeCommand("ps -ef |grep -vE \"grep|//|vnc\" |grep httpd | awk '{ split($0, a, \"-d\"); split(a[2], b, \" \"); print b[1] }' |sort |uniq|xargs");
//		//String retorno = sshClient.executeCommand("echo $iest_home | awk '{split($0,a,\" \"); for(word in a) if(index(a[word],\"AppServer\")!=0){ print a[word]; break; } }'");
//		logger.debug("RETURN - "+retorno);
//
//		List<String> pathList = new ArrayList<String>();
//		// IHS is not running.
//		if(retorno.trim().length() == 0){
//			retorno = "/usr/IBMIHS /usr/IBMIHSN1 /usr/IBMIHSV6 /usr/IBMIHS2 /usr/IBM/HTTPServer /usr2/IBMIHS /opt/IBMIHS /opt/IBMIHSN1 /opt/IBMIHSV6 /opt/IBMIHS2 /opt/IBM/HTTPServer";
//		}
//
//		StringTokenizer token = new StringTokenizer(retorno," ");
//		List<String> listHomes = new ArrayList<String>();
//
//		while(token.hasMoreTokens()){
//			String value = token.nextToken();
//			boolean command = chkPath(value, 'd');
//			if(command){
//				logger.debug("ADD LIST_HOMES - " + value.trim());
//				listHomes.add( value.trim());
//			}
//		}
//
//		if(listHomes.isEmpty()){
//			listHomes.add("notfound");
//		}
//		return listHomes;
//	}
//
//	//---
//	// Each HTTPD HOME has one httpd conf file. Return this.
//	//
//	public String getIhsConf(String path) throws IOException, InterruptedException{
//
//		String ihsConf = null;
//
//		if(verifyProc("ihs")){
//			if (chkPath(path + "/conf/httpd.conf", 'f')){
//				logger.debug("HTTPD_CONF = " + path + "/conf/httpd.conf");
//				ihsConf = path + "/conf/httpd.conf";
//			} else {
//				ihsConf = sshClient.executeCommand( "ps -ef | grep -vE \"grep|//|vnc\" |grep " + path + " | awk '{ split($0, a, \"-f\"); split(a[2], b, \" \"); print b[1] }' |sort |uniq|xargs");
//				logger.debug("RETURN - " + ihsConf);
//			}
//		} else {
//			if (chkPath(path + "/conf/httpd.conf", 'f')){
//				logger.debug("HTTPD_CONF = " + path + "/conf/httpd.conf");
//				ihsConf = path + "/conf/httpd.conf";
//			} else {
//				ihsConf = "notfound";
//			}
//		}
//
//		String msg = ihsConf.replace("\r\n","");
//		if(msg.trim().length()==0){
//			ihsConf = "notfound";
//		}
//		logger.debug("HTTPD_CONF = " + ihsConf);
//
//		// If file doesn't exist, there is something wrong.
//		if(!chkPath(ihsConf, 'f')){
//			ihsConf = "notfound";
//		}
//
//		// Return httpd conf file path.
//		return ihsConf;
//	}
//
//	 //---
//	 // Return the IHS version
//	 //
//	public String getIhsVersion(String path) throws IOException, InterruptedException{
//		String ihsVersion = null;
//
//		if(chkPath(path + "/version.signature", 'f')){
//			ihsVersion = sshClient.executeCommand("cat " + path + "/version.signature |awk '{ split($0,a,\" \"); print a[NF]}'");
//		} else {
//			ihsVersion = "1";
//		}
//		// Give me the IHS_HOME and I'll return the version of it.
//		return ihsVersion;
//	}
//
// 	public String verifySizeDir(String value) throws IOException, InterruptedException{
//		logger.debug("verifySizeDir ");
//		boolean command = chkPath(value, 'd');
//
//		String retorno = null;
//
//		if(command) {
//			retorno = sshClient.executeCommand("du -sk " + value + " 2> /dev/null |awk '{print $1}'");
//		} else {
//			retorno = "notfound";
//		}
//		return retorno;
//	}
//
//	public String getWasHome() throws IOException, InterruptedException{
//		logger.debug("getWasHome");
//		sshClient.executeCommand("iest_home=`ps -ef | grep Dwas.install.root | awk '{ split($0, a, \"-Dwas.install.root=\"); split(a[2], b, \" \"); print b[1] }'|sort |uniq|xargs`");
//		String retorno = sshClient.executeCommand("echo $iest_home | awk '{split($0,a,\" \"); for(word in a) if(index(a[word],\"AppServer\")!=0){ print a[word]; break; } }'");
//
//		if(retorno!=null && retorno.trim().length()<=2){
//			logger.debug("Checking whether is ProcServer");
//			retorno = sshClient.executeCommand("echo $iest_home | awk '{split($0,a,\" \"); for(word in a) if(index(a[word],\"ProcServer\")!=0){ print a[word]; break; } }'");
//		}
//
//		if(retorno!=null && retorno.trim().length()<=2){
//			logger.debug("Checking whether is DeploymentManager");
//			retorno = sshClient.executeCommand("echo $iest_home | awk '{split($0,a,\" \"); for(word in a) if(index(a[word],\"DeploymentManager\")!=0){ print a[word]; break; } }'");
//		}
//
//		logger.info("WAS_HOME = "+retorno);
//		return retorno;
//	}
//
//	public void runSetupCmdLine(String wasHome) throws IOException, InterruptedException{
//		logger.debug("runSetupCmdLine");
//		String retorno = sshClient.executeCommand("cd "+wasHome+"/bin");
//
//		retorno = sshClient.executeCommand(". ./setupCmdLine.sh");
//		if(retorno.length()>5){
//			sshClient.executeCommand("./setupCmdLine.sh");
//		}
//		try {
//			  Thread.sleep(300);
//			} catch (InterruptedException e) {
//			  e.printStackTrace();
//			}
//	}
//
//	public String getPlatform() throws IOException, InterruptedException{
//		logger.debug("getPlatform");
//		String platform = sshClient.executeCommand("/bin/uname -s");
//		logger.info("PLATFORM = "+platform);
//		return platform;
//	}
//
//	public String getUname() throws IOException, InterruptedException{
//		logger.debug("getUname");
//		String uname = sshClient.executeCommand("/bin/uname -n");
//		logger.info("UNAME = "+uname);
//		return uname;
//	}
//
//	public String getPartialWasVersion(String home) throws IOException, InterruptedException{
//		logger.debug("getWasVersion");
//		String retorno = sshClient.executeCommand( "ls -ltr "+home+"/profiles | xargs");
//
//		if ( retorno.trim().contains("not found") ){
//			logger.info("WAS 5");
//			return "5";
//		} else {
//			logger.info("WAS 6");
//			return "6";
//		}
//	}
//
//	public String getCell() throws IOException, InterruptedException{
//		logger.debug("getCell");
//		String retorno = sshClient.executeCommand("echo $WAS_CELL");
//		if(retorno.trim().length()==0){
//			retorno = sshClient.executeCommand("ps -ef | grep -v grep | grep Dwas.install.root | awk '{ split($0,a,\" \"); print a[NF-2]}' | sort |uniq |xargs");
//		}
//
//		if(retorno.trim().length()==0){
//			retorno = "not found";
//		}
//
//		logger.info("CELL : "+retorno);
//		return retorno;
//	}
//
//	public String getNode() throws IOException, InterruptedException{
//		logger.debug("getNode");
//		String node = sshClient.executeCommand("echo $WAS_NODE");
//		if(node.trim().length()==0){
//			node = sshClient.executeCommand("ps -ef | grep -v grep | grep Dwas.install.root | awk '{ split($0,a,\" \"); print a[NF-1]}' | sort |uniq |xargs");
//		}
//		logger.info("NODE : "+node);
//		return node;
//	}
//
//    public List<String> getAppServers() throws IOException, InterruptedException{
//		logger.debug("getAppServer");
//		List<String> appServerList = new ArrayList();
//		String appServer = sshClient.executeCommand("ps -ef | grep -v grep | grep Dwas.install.root | awk '{ split($0,a,\" \"); print a[NF]}' |xargs");
//
//		StringTokenizer token = new StringTokenizer(appServer," ");
//
//		//String wasHome =
//
//		while(token.hasMoreTokens()){
//
//			appServerList.add(token.nextToken());
//		}
//		return appServerList;
//	}
//
//	public String getWASOver5Version(String wasHome, String wasCell, String wasNode) throws IOException, InterruptedException{
//		logger.debug("getWASOver5Version");
//		String path = wasHome+"/config/cells/"+wasCell+"/nodes/"+wasNode+"/node-metadata.properties";
//		String version = sshClient.executeCommand("grep com.ibm.websphere.baseProductVersion "+path);
//		version = version.replace("com.ibm.websphere.baseProductVersion","");
//		version = version.replace("=","");
//		logger.info("WAS5_VERSION: "+version);
//		return version;
//	}
//
//	public String getWASVersion(String wasHome) throws IOException, InterruptedException{
//		logger.debug("getWasVersion");
//		String path = wasHome+"/properties/version/WAS.product";
//		String version = sshClient.executeCommand("cat " + path + " | grep version | sed 's/<[^>]*>//g' |xargs");
//		logger.info("WAS_VERSION: "+version);
//		if (version.trim().length()==0) version="1";
//		return version;
//	}
//
//	public String getWasInstanceNames(String wasHome, String wasCell, String wasNode) throws IOException, InterruptedException{
//		logger.debug("getWasInstanceNames");
//		String path = wasHome+"/config/cells/"+wasCell+"/nodes/"+wasNode;
//		String wasInstanceNames = sshClient.executeCommand("grep ServerEntry "+path+"/serverindex.xml | awk '{ split($0,a,\"serverName=\"); split(a[2],b,\"serverType=\"); print b[1]\",\"; }' | xargs");
//		wasInstanceNames = wasInstanceNames.replace("$"," ");
//		wasInstanceNames = wasInstanceNames.replace("\r\n"," ");
//		if((wasInstanceNames.trim().length()==0)||(wasInstanceNames.contains("not found"))){
//			logger.debug("getNode - NOT FOUND");
//			return null;
//		}
//		logger.info("WAS_INSTANCE_NAMES : "+wasInstanceNames);
//		return wasInstanceNames;
//	}
//
//	public String getWAS5Version(String wasHome) throws IOException, InterruptedException{
//		logger.debug("getWAS5Version");
//		String path = wasHome+"/properties/version/BASE.product";
//		if( wasHome.contains("DeploymentManager") ){
//		   path = wasHome+"/properties/version/ND.product";
//		}
//		String version = sshClient.executeCommand("grep version "+path);
//		version = version.replace("<version>","");
//		version = version.replace("</version>","");
//		logger.info("WAS5_VERSION: "+version);
//		if(version.trim().length()==0)version="1";
//		return version;
//	}
//
//	public String getHttpTransportWas5(String wasHome, String wasCell , String wasNode, String appserver) throws IOException, InterruptedException{
//		   logger.debug("getHttpTransportWas5");
//		   String path = wasHome+"/config/cells/"+wasCell+"/nodes/"+wasNode+"/servers/"+appserver;
//
//		   String command = "grep EndPoint '"+path+"/server.xml' | awk '{ split($0,a,\"port=\"); print substr(a[2], 2, 5); }' | sed -e 's/\"//g' | cut -f 1 -d / | xargs";
//		   String httpTransportWas5 =  sshClient.executeCommand(command);
//		   logger.debug("HTTP_TRANSPORT_WAS_5 COMMAND = "+command);
//		   httpTransportWas5 = httpTransportWas5.replace("\r\n","");
//		   httpTransportWas5 = httpTransportWas5.replace("$","");
//			if((httpTransportWas5.trim().length()==0)||(httpTransportWas5.contains("not found"))){
//				return null;
//			}
//			httpTransportWas5 = httpTransportWas5.replace("/","");
//		   logger.info("HTTP_TRANSPORT_WAS_5 : "+httpTransportWas5);
//		   return httpTransportWas5;
//	}
//
//	public String getSystemOut(String wasHome, String wasCell , String wasNode, String appserver) throws IOException, InterruptedException{
//		   logger.debug("getSystemOut");
//		   String path = wasHome+"/config/cells/"+wasCell+"/nodes/"+wasNode+"/servers/"+appserver;
//		   String command = "grep outputStreamRedirect '"+path+"/server.xml' | awk '{ split($0,a,\"fileName=\"); split(a[2],b,\" \"); print b[1]; }' | xargs";
//		   logger.debug("SYSTEM_OUT COMMAND = "+command);
//		   String systemOut =  sshClient.executeCommand(command);
//		   if(systemOut.contains("${")){
//			   String contentAfter = systemOut.substring(systemOut.indexOf("}")+2);
//			   systemOut = contentAfter;
//		   }
//		   systemOut = systemOut.replace("$(LOG_ROOT)/$(SERVER)/","");
//		   systemOut = systemOut.replace("$(SERVER_LOG_ROOT)/","");
//		   logger.info("SYSTEM_OUT : "+systemOut);
//		   return systemOut;
//	}
//
//	public String getSystemErr(String wasHome, String wasCell , String wasNode, String appserver) throws IOException, InterruptedException{
//		   logger.debug("getSystemErr");
//		   String path = wasHome+"/config/cells/"+wasCell+"/nodes/"+wasNode+"/servers/"+appserver;
//		   String command = "grep errorStreamRedirect '"+path+"/server.xml' | awk '{ split($0,a,\"fileName=\"); split(a[2],b,\" \"); print b[1]; }' | xargs";
//		   logger.debug("SYSTEM_ERR - COMMAND = "+command);
//		   String systemErr =  sshClient.executeCommand(command);
//		   if(systemErr.contains("${")){
//			  String contentAfter = systemErr.substring(systemErr.indexOf("}")+2);
//			  systemErr = contentAfter;
//		   }
//		   systemErr = systemErr.replace("$(LOG_ROOT)/$(SERVER)/","");
//		   systemErr = systemErr.replace("$(SERVER_LOG_ROOT)/","");
//		   logger.info("SYSTEM_ERR : "+systemErr);
//		   return systemErr;
//	}
//
//	public String getWasNodeDmgr(String pathCell) throws IOException, InterruptedException{
//		logger.debug("getWasNodeDmgr");
//		String wasNodeDmgr = sshClient.executeCommand( "fgrep sslConfig "+pathCell+"/security.xml | awk '{ split($0,a,\"sslConfig=\"); print a[2] }' | sed -e 's/\"//g' | cut -f 1 -d / | tail -1");
//		logger.info("WAS_NODE_DMGR : "+wasNodeDmgr);
//		return wasNodeDmgr;
//	}
//
//	public String getWasHostnameDmgr(String pathWasNodeDmgr) throws IOException, InterruptedException{
//		logger.debug("getWasHostnameDmgr");
//		String wasHostnameDmgr = sshClient.executeCommand( "fgrep host "+pathWasNodeDmgr+"/serverindex.xml | awk '{ split($0,a,\"host=\"); print a[2] }' | xargs | cut -f 1 -d \" \"");
//		logger.info("WAS_HOSTNAME_DMGR : "+wasHostnameDmgr);
//		return wasHostnameDmgr;
//	}
//
//	public String getWas5AdminUser(String wasInstall) throws IOException, InterruptedException{
//		logger.debug("getWas5AdminUser");
//
//		String command = "fgrep loginUserid '"+wasInstall+"/properties/soap.client.props' | awk '{ split($0,a,\"loginUserid=\"); print a[2] }'";
//		logger.debug("COMMAND : "+command);
//
//		String was5AdminUser = sshClient.executeCommand( command );
//
//		if(was5AdminUser.trim().equals("$")){
//		   return null;
//		}
//		logger.info("WAS5_ADMIN_USER : "+was5AdminUser);
//		return was5AdminUser;
//	}
//
//	public String getWasPortSSLConsole(String pathcellDmgr) throws IOException, InterruptedException{
//		logger.debug("getWasPortSSLConsole");
//		String command = "cat '"+pathcellDmgr+"/serverindex.xml' | xargs | awk '{ split($0,a,\"WC_adminhost_secure\"); split(a[2],b,\"port=\"); print substr(b[2], 0, 5) }'";
//		logger.debug("COMMAND : "+command);
//		String wasPortSSLConsole = sshClient.executeCommand( command );
//		if(wasPortSSLConsole.trim().equals("$")){
//		   return null;
//		}
//		wasPortSSLConsole = wasPortSSLConsole.replace("/","");
//		logger.info("WAS_PORT_SSL_CONSOLE : "+wasPortSSLConsole);
//		return wasPortSSLConsole;
//	}
//
//	public String getWasPortSOAP(String pathcellDmgr) throws IOException, InterruptedException{
//		logger.debug("getWasPortSOAP");
//		String wasPortSOAP = sshClient.executeCommand( "cat '"+pathcellDmgr+"/serverindex.xml' | xargs | awk '{ split($0,a,\"SOAP_CONNECTOR_ADDRESS\"); split(a[2],b,\"port=\"); print substr(b[2], 0, 5) }'");
//		if(wasPortSOAP.trim().equals("$")){
//		   return null;
//		}
//		wasPortSOAP = wasPortSOAP.replace("/","");
//		logger.info("WAS_PORT_SOAP : "+wasPortSOAP);
//		return wasPortSOAP;
//	}
//
//	public String getSecurityRepository(String wasHome, String wasCell ) throws IOException, InterruptedException{
//	   logger.debug("getSecurityRepository");
//	   String path = wasHome+"/config/cells/"+wasCell;
//	   String securityRepository =  sshClient.executeCommand("grep 'activeUserRegistry=' '"+path+"/security.xml' | awk '{ split($0,a,\"activeUserRegistry=\"); split(a[2],b,\" \"); print b[1];}' | xargs");
//	   logger.info("WAS_PORT_SOAP : "+securityRepository);
//	   return securityRepository;
//	}
//
//	public String getWasProfile(String propertiesPath) throws IOException, InterruptedException{
//		String retorno = sshClient.executeCommand( "awk '{ split($0,a,\"name=\"); split(a[2],b,\" \"); print b[1] }' "+propertiesPath+"/profileRegistry.xml | xargs");
//
//		String msg = retorno.replace("\r\n","");
//		if(msg.trim().length()==0){
//			retorno = "notfound";
//		}
//		return retorno;
//	}
//
//	public List<String> getWasProfilePath(String propertiesPath) throws IOException, InterruptedException{
//		String retorno = sshClient.executeCommand( "awk '{ split($0,a,\"path=\"); split(a[2],b,\" \"); print b[1] }' "+propertiesPath+"/profileRegistry.xml | xargs");
//		List<String> listProfiles = new ArrayList<String>();
//		String msg = retorno.replace("\r\n","");
//		if(msg.trim().length()==0){
//			listProfiles.add("notfound");
//		}
//
//		StringTokenizer token = new StringTokenizer(retorno," ");
//
//		while(token.hasMoreTokens()){
//			String value = token.nextToken();
//			boolean command = chkPath(value, 'd');
//
//			if(command){
//				logger.debug("ADD LIST_PROFILES - " + value.trim());
//				listProfiles.add( value.trim());
//			}
//		}
//
//		if(listProfiles.isEmpty()){
//			listProfiles.add("notfound");
//		}
//		return listProfiles;
//	}
//
//	public boolean isProfileInstalled(String profile) throws IOException, InterruptedException{
//		String retorno = sshClient.executeCommand("cd "+profile+"/bin");
//
//		if( retorno.contains("No such file") || retorno.contains("Permission denied") ){
//			return false;
//		} else {
//			return true;
//		}
//
//	}
//
//	public boolean isDmgr(String profile) throws IOException, InterruptedException{
//		String retorno = sshClient.executeCommand("ls "+profile+"/bin/stopManager.sh");
//
//		if( retorno.contains("No such file") || retorno.contains("not found") || retorno.contains("Permission denied") ){
//			return false;
//		} else {
//			return true;
//		}
//
//	}
//	
//	public String getCellWas6(String pathProfile) throws IOException, InterruptedException{
//		sshClient.executeCommand("cd "+pathProfile);
//		return sshClient.executeCommand("grep \"WAS_CELL=\" setupCmdLine.sh | awk '{ split($0,a,\"=\"); print a[2]; }'");
//	}
//
//	public String getNodeWas6(String pathProfile) throws IOException, InterruptedException{
//		sshClient.executeCommand("cd "+pathProfile);
//		return sshClient.executeCommand("grep \"WAS_NODE=\" setupCmdLine.sh | awk '{ split($0,a,\"=\"); print a[2]; }'");
//	}
//
//	public String getServerEntryNumbers(String wasHome, String wasCell, String wasNode) throws IOException, InterruptedException{
//		String path = wasHome+"/config/cells/"+wasCell+"/nodes/"+wasNode;
//		String retorno = sshClient.executeCommand("grep ServerEntry "+path+"/serverindex.xml | awk '{ split($0,a,\"ServerEntry_\"); print substr(a[2], 1, 10); }' | xargs");
//		retorno = retorno.replace("\r\n","");
//		retorno = retorno.replace("$","");
//		if((retorno.trim().length()==0)||(retorno.contains("not found"))){
//			return null;
//		}
//
//		return retorno;
//	}
//
//	public String getEndPointDefaultHost(String wasHome, String wasCell, String wasNode, String serverEntryNumber ) throws IOException, InterruptedException{
//		String path = wasHome+"/config/cells/"+wasCell+"/nodes/"+wasNode;
//		String retorno = sshClient.executeCommand("grep "+serverEntryNumber+" "+path+"/serverindex.xml | xargs | awk '{ split($0,a,\"WC_defaulthost\"); split(a[2],b,\"port=\"); print substr(b[2], 1, 5); }'");
//		retorno = retorno.replace("\r\n","");
//		retorno = retorno.replace("$","");
//
//		if((retorno.trim().length()==0)||(retorno.contains("not found"))){
//			return null;
//		}
//		retorno = retorno.replace("/","");
//		return retorno;
//	}
//
//	public String getEndPointDefaultHostSecure(String wasHome, String wasCell, String wasNode, String serverEntryNumber ) throws IOException, InterruptedException{
//		String path = wasHome+"/config/cells/"+wasCell+"/nodes/"+wasNode;
//		String retorno = sshClient.executeCommand("grep "+serverEntryNumber+" "+path+"/serverindex.xml | xargs | awk '{ split($0,a,\"WC_defaulthost_secure\"); split(a[2],b,\"port=\"); print substr(b[2], 1, 5); }'");
//		retorno = retorno.replace("\r\n","");
//		retorno = retorno.replace("$","");
//
//		if((retorno.trim().length()==0)||(retorno.contains("not found"))){
//			return null;
//		}
//		retorno = retorno.replace("/","");
//		return retorno;
//	}
//
//	public String getWasNodeDmgr2(String pathCell) throws IOException, InterruptedException{
//		String retorno = sshClient.executeCommand( "fgrep authDataEntries "+pathCell+"/security.xml | awk '{ split($0,a,\"alias=\"); print a[2] }' | sed -e 's/\"//g' | cut -f 1 -d / | xargs");
//		return retorno;
//	}
//
//	public String getWasNodeDmgr3(String pathCellNode) throws IOException, InterruptedException{
//		sshClient.executeCommand( "cd "+pathCellNode);
//		return sshClient.executeCommand( "ls | grep 'Manager'");
//	}
//
//	public String getWasNodeDmgr4(String pathCellNode) throws IOException, InterruptedException{
//
//		sshClient.executeCommand( "cd "+pathCellNode );
//
//		String listDirNode = sshClient.executeCommand( "ls | xargs");
//		String retorno = "";
//		String dirNode = "";
//		StringTokenizer token = new StringTokenizer(listDirNode, " ");
//		while(token.hasMoreTokens()) {
//			dirNode = token.nextToken();
//			retorno = sshClient.executeCommand( "fgrep serverType "+dirNode+"/serverindex.xml | awk '{ split($0,a,\"serverType=\"); print a[2] }' | sed -e 's/\"//g' | cut -f 1 -d '>' | xargs");
//			if(retorno.contains("DEPLOYMENT_MANAGER")){
//				break;
//			} else {
//				dirNode="";
//			}
//		}
//		return dirNode;
//	}
//
//	public String getWasHostnameDmgr2(String pathWasNodeDmgr) throws IOException, InterruptedException{
//		String retorno = sshClient.executeCommand( "fgrep host "+pathWasNodeDmgr+"/serverindex.xml | awk '{ split($0,a,\"hostName=\"); print a[2] }' | xargs | cut -f 1 -d '>'");
//		return retorno;
//	}
//
//	public String getWas6AdminUser(String profilePath, String cellname) throws IOException, InterruptedException{
//		String retorno = "";
//		retorno = sshClient.executeCommand( "grep com.ibm.SOAP.loginUserid "+profilePath+"/properties/soap.client.props 2>/dev/null");
//		int index = retorno.indexOf("=");
//		retorno = retorno.substring(index+1);
//		return retorno;
//	}
//
//	public List<String> getCoregroupsName(Profile profile) throws IOException, InterruptedException{
//		List<String> coregroupList = new ArrayList<String>();
//
//		String coreGroupPath = profile.getPath()+"/config/cells/"+profile.getCell()+"/coregroups";
//		String coreGroups = sshClient.executeCommand("ls "+coreGroupPath+" | xargs");
//
//		StringTokenizer token = new StringTokenizer(coreGroups," ");
//		while(token.hasMoreTokens()){
//			coregroupList.add(token.nextToken());
//		}
//
//		return coregroupList;
//	}
//
//	public String getCoregroup_DATASTACK(Profile profile, String coregroupName) throws IOException, InterruptedException{
//		String singleCoreGroupPath = profile.getPath()+"/config/cells/"+profile.getCell()+"/coregroups/"+coregroupName+"/coregroup.xml";
//		String value = sshClient.executeCommand("grep IBM_CS_DATASTACK_MEG "+singleCoreGroupPath+" | awk '{ split($0,a,\"value=\"); print a[2]}' | xargs");
//		if(!value.contains("No such file")){
//			value = value.replace("/","");
//			value = value.replace(">","");
//		}
//		return value;
//	}
//
//	public String getCoregroup_WIRE_FORMAT_VERSION(Profile profile, String coregroupName) throws IOException, InterruptedException{
//		String singleCoreGroupPath = profile.getPath()+"/config/cells/"+profile.getCell()+"/coregroups/"+coregroupName+"/coregroup.xml";
//		String value = sshClient.executeCommand("grep IBM_CS_WIRE_FORMAT_VERSION "+singleCoreGroupPath+" | awk '{ split($0,a,\"value=\"); print a[2]}' | xargs");
//		value = value.replace("/","");
//		value = value.replace(">","");
//		return value;
//	}
//
//	public String getCoregroupServerName(Profile profile, String coregroupName) throws IOException, InterruptedException{
//		String singleCoreGroupPath = profile.getPath()+"/config/cells/"+profile.getCell()+"/coregroups/"+coregroupName+"/coregroup.xml";
//		String value = sshClient.executeCommand("grep "+profile.getNode()+" "+singleCoreGroupPath+" | awk '{ split($0,a,\"serverName=\"); print a[2]; }' | xargs");
//		value = value.replace("/","");
//		value = value.replace(">","");
//		return value;
//	}
//
//	public String getCoregroupServerNameByNode(Profile profile, String coregroupName, String nodeName) throws IOException, InterruptedException{
//		String singleCoreGroupPath = profile.getPath()+"/config/cells/"+profile.getCell()+"/coregroups/"+coregroupName+"/coregroup.xml";
//		String value = sshClient.executeCommand("grep "+nodeName+" "+singleCoreGroupPath+" | awk '{ split($0,a,\"serverName=\"); print a[2]; }' | xargs");
//		value = value.replace("/","");
//		value = value.replace(">","");
//		return value;
//	}
//
//	public String getTransportBufferSize(Profile profile, boolean isDmgr) throws IOException, InterruptedException{
//
//		String hamanagerservicePath = profile.getPath()+"/config/cells/"+profile.getCell()+"/nodes/"+profile.getNode()+"/servers/nodeagent/hamanagerservice.xml";
//		if(isDmgr){
//			hamanagerservicePath = profile.getPath()+"/config/cells/"+profile.getCell()+"/nodes/"+profile.getNode()+"/servers/dmgr/hamanagerservice.xml";
//		}
//		String value = sshClient.executeCommand("grep transportBufferSize "+hamanagerservicePath+" | awk '{ split($0,a,\"transportBufferSize=\"); split(a[2],b,\" \"); print b[1] }' | xargs");
//		if(!value.contains("No such file")){
//			return value;
//		}
//		return null;
//	}
//
//	public String getIPSolares(String hostname) throws IOException, InterruptedException{
//		String retorno = sshClient.executeCommand("nslookup "+hostname+" | xargs");
//
//		retorno = retorno.substring(retorno.indexOf(hostname));
//		retorno = retorno.replace(hostname, "").trim();
//		retorno = retorno.replace("Address:", "").trim();
//		return retorno;
//	}
//
//	public String getOwnerID(String home) throws IOException, InterruptedException{
//
//		boolean command = chkPath(home, 'd');
//		String ownerID1=null;
//		String ownerID2=null;
//		if(command){
//			ownerID1 = sshClient.executeCommand("ls -ld "+home+"| awk '{ print $3 }' | xargs");
//			logger.debug("ownerID = " + ownerID1);
//			if(ownerID1.trim().length()==0) ownerID1 = "unknown";
//
//			ownerID2 = sshClient.executeCommand("ps -ef |grep -vE \"grep|//|vnc\" |grep " + home + "| awk '{ print $1 }' |grep -vE \"conf|root|nobody\" |sort |uniq | xargs");
//			logger.debug("ownerID = " + ownerID2);
//			if(ownerID2.trim().length()==0) ownerID2 = "unknown";
//
//			// The user has to be different from system users.
//			if( ownerID1.trim().equals("root") ||
//				ownerID2.trim().equals("root") ||
//				ownerID1.trim().equals("nobody")||
//				ownerID2.trim().equals("nobody")||
//				!ownerID1.trim().equals(ownerID2)) ownerID1 = ownerID2;
//
//			logger.debug("ownerID = " + ownerID1);
//
//		} else {
//			ownerID1 = "unknown";
//			logger.debug("ownerID = " + ownerID1);
//		}
//		return ownerID1;
//	}
//
//	public String getAdminHost() throws IOException, InterruptedException{
//		logger.debug("getAdminHost");
//		String adminHost = sshClient.executeCommand("hostname");
//		return adminHost;
//	}
//
//	public String getFs(String path) throws IOException, InterruptedException{
//		logger.debug("getFs");
//		String fs = sshClient.executeCommand("df -P " + path + "|grep -vi Filesystem |xargs |awk '{split($0,a,\" \"); print $1}'");
//
//		if(fs.trim().length()==0) fs = "notfound";
//		return fs;
//	}

}
