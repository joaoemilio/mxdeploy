package com.mxssh.service.was;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.mxdeploy.api.domain.middleware.Middleware;
import com.mxdeploy.api.domain.middleware.UserID;
import com.mxdeploy.api.domain.middleware.WebSphere60;
import com.mxdeploy.api.domain.middleware.WebSphereAppServerProfile;
import com.mxdeploy.api.domain.operatingsystem.UnixUser;
import com.mxssh.CannotSudoException;
import com.mxssh.OSService;
import com.mxssh.SSHServiceNew;

public class WebSphereAppServer6Service { //extends WebSphereAppServerService {
//
//	protected String WAS_VERSION;
//	
//	public WebSphereAppServer6Service(SSHServiceNew sshClient) {
//		super(sshClient);
//		// TODO Auto-generated constructor stub
//	}
//
//	
//	public List<Middleware> retrieveMiddlewareList() throws IOException, InterruptedException {
//		return null;
//	}
//	
//	/**
//	 * This method is meant to retrieve a list of WAS 6.0 instances installed
//	 * It will look for WAS instances currently running and also look at common directories 
//	 * @return
//	 * @throws InterruptedException 
//	 * @throws IOException 
//	 *
//	public List<Middleware> retrieveMiddlewareList() throws IOException, InterruptedException {
//		List<Middleware> list = new ArrayList<Middleware>();
//		//OSService osservice = new OSService(sshClient);
//		
//		//retrieve a list of WAS Home running or installed
//		List<String> homes = getWasHomes();
//		
//		//create a list of WebSphereAppServer 6.0 objects 
//		for(String wasHome: homes) {
//			//checking WAS version
//			String version = getWASVersion(wasHome);
//			if(!(version != null && version.startsWith(getServiceWASVersion())) ) {
//				continue;
//			}
//			WebSphere60 was = new WebSphere60();
//			was.setWasHome(wasHome);
//			was.setVersion(version);
//			
//			//set process id
//			String processID = getOwnerID(wasHome);
//			UnixUser uuser = osservice.getUnixUser(processID); 
//			was.setProcessID(uuser.getId(), uuser.getDefaultGroup() );
//			
//			try {
//				sshClient.sudoSUToUser(uuser.getId());
//			} catch (CannotSudoException e) {
//				e.printStackTrace();
//			}
//
//			//retrieve list of WAS Profiles by providing a WAS HOME install
//			List<String> wasProfilePathList = this.getWasProfilePath(was.getWasHome()+"/properties");
//			for(String profilePath: wasProfilePathList) {
//				if (!this.isProfileInstalled(profilePath)){
//					continue;	
//				}
//				
//				WebSphereAppServerProfile profile = new WebSphereAppServerProfile();
//				profile.setPath(profilePath);
//
//				// retrieve Cell Name by using the profile path
//				profile.setCellName(this.getCellName(profilePath));
//				
//				// set port SOAP
//				String wasNodeDmgr = this.getWasNodeDmgr3(profilePath+"/config/cells/"+profile.getCellName()+"/nodes");
//				if(wasNodeDmgr.trim().equals("")){
//					wasNodeDmgr = this.getWasNodeDmgr4(profile.getPath()+"/config/cells/"+profile.getCellName()+"/nodes");
//				} 
//				String soapPort = this.getWasPortSOAP(profile.getPath()+"/config/cells/"+profile.getCellName()+"/nodes/"+wasNodeDmgr);
//				profile.setSoapPort(soapPort);
//				
//				//retrieve Server ID from security.xml
//				profile.setServerID(getSecurityServerID(profile));
//				
//				//retrieve and set Server ID Password
//				if(profile.getServerID() != null && !profile.getServerID().trim().equals("")) {
//					profile.setServerIDPassword(getSecurityServerIDPassword(profile));
//				}
//				
//				//add profile to WAS
//				was.addProfile(profile);
//			}
//			
//			//add WAS to the list
//			list.add(was);
//		}
//		
//		return list;
//	}
//	*/
//
//	protected String getServiceWASVersion() {
//		return "6.0";
//	}
//
//
//	/**
//	 * Retrieve the name of the cell for a specific WAS profile
//	 * @param wasProfilePath => Path of the profile 
//	 * @return
//	 * @throws InterruptedException 
//	 * @throws IOException 
//	 */
//	public String getCellName(String wasProfilePath) throws IOException, InterruptedException {
//		sshClient.executeCommand("cd "+wasProfilePath + "/bin");
//		return sshClient.executeCommand("grep \"WAS_CELL=\" setupCmdLine.sh | awk '{ split($0,a,\"=\"); print a[2]; }'");
//	}
//	
//	/**
//	 * Retrieve serverID for a specified profile 
//	 * @param profile
//	 * @return
//	 * @throws IOException
//	 * @throws InterruptedException
//	 */
//	public String getSecurityServerID(WebSphereAppServerProfile profile) throws IOException, InterruptedException {
//		sshClient.executeCommand("cd " + profile.getPath() + "/config/cells/" + profile.getCellName() + "/");
//		return sshClient.executeCommand("cat security.xml |grep serverId| awk '{split($0,a,\"serverId=\");split(a[2],b,\" \");print b[1]}'|sed 's/\"//g'|sort |uniq |xargs");
//	}
//
//	/**
//	 * Retrieve serverID password for a specified profile
//	 * @param profile
//	 * @return
//	 * @throws IOException
//	 * @throws InterruptedException
//	 */
//	public String getSecurityServerIDPassword(WebSphereAppServerProfile profile) throws IOException, InterruptedException {
//		sshClient.executeCommand("cd " + profile.getPath() + "/config/cells/" + profile.getCellName() + "/");
//		return sshClient.executeCommand("cat security.xml |grep " + profile.getServerID() + " | awk '{split($0,a,\"serverPassword=\");split(a[2],b,\" \");print b[1]}'|sed 's/\"//g'|sort |uniq |xargs");
//	}
//	
}
