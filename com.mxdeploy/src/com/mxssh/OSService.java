package com.mxssh;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.mxdeploy.api.domain.operatingsystem.Sudoers;
import com.mxdeploy.api.domain.operatingsystem.UnixGroup;
import com.mxdeploy.api.domain.operatingsystem.UnixUser;
import com.mxdeploy.api.domain.operatingsystem.Sudoers.Alias;
import com.mxdeploy.api.domain.operatingsystem.Sudoers.Cmndalias;
import com.mxdeploy.api.domain.operatingsystem.Sudoers.Hostalias;
import com.mxdeploy.api.domain.operatingsystem.Sudoers.Runalias;
import com.mxdeploy.api.domain.operatingsystem.Sudoers.UserSpecification;
import com.mxdeploy.api.domain.operatingsystem.Sudoers.Useralias;
import com.mxdeploy.api.util.StringUtils;

public class OSService {

	private SSHConsoleService ssh = null;
	
	public OSService(SSHConsoleService ssh){
		this.ssh = ssh;
	}
	
	private String getId(String part) {
		int index = part.indexOf("(");
		int end = part.indexOf(")");
		//System.out.println(index + " - " + end );
		return part.substring(index+1, end);
	}
	
	public Sudoers parseSudoersFile(String path) {
		Sudoers domain = new Sudoers();
		
		List<String> list = FileUtilDummy.getInstance().getContents(path);
		list = normalizeLines(list);
		
		List<Cmndalias> cmndAliases = new ArrayList<Cmndalias>(); 
		domain.setListCmndalias(cmndAliases);
		List<Hostalias> hostAliases = new ArrayList<Hostalias>(); 
		domain.setListHostaliases(hostAliases);
		List<Useralias> userAliases = new ArrayList<Useralias>(); 
		domain.setListUseralias(userAliases);
		List<Runalias> listRunaliases = new ArrayList<Runalias>(); 
		domain.setListRunaliases(listRunaliases);

		for(int i = 0; i < list.size(); i++){
			String line = list.get(i);
			
			//Host_Alias
			if(testLine(line, "Host_Alias")){
				//System.out.println(line);
				hostAliases.add(getHostAlias(domain, list, i, "Host_Alias"));
			}
			
			//Cmnd_Alias
			if(testLine(line, "Cmnd_Alias")){
				//System.out.println(line);
				cmndAliases.add(getCmndAlias(domain, list, i, "Cmnd_Alias"));
			}
			
			//Runas_Alias
			if(testLine(line, "Runas_Alias")){
				//System.out.println(line);
				listRunaliases.add(getRunasAlias(domain, list, i, "Runas_Alias"));
			}
			
			//User_Alias
			if(testLine(line, "User_Alias")){
				//System.out.println(line);
				userAliases.add(getUserAlias(domain, list, i, "User_Alias"));
			}
			
			if(!testLine(line, "User_Alias") &&
					!testLine(line, "Runas_Alias") &&
					!testLine(line, "Host_Alias") &&
					!testLine(line, "Cmnd_Alias") &&
					!testLine(line, "Defaults")
					) {
				domain.addUserSpecification(getUserSpecification(domain, list, i));
			}
			
		}
		
		return domain;
	}
	
	private List<String> normalizeLines(List<String> list){
		List<String> normalized = new ArrayList<String>();
		
		boolean continuationLine = false;
		boolean startLine = true;
		for(int i=0; i<list.size(); i++){
			String line = list.get(i);

			if(!validLine(line)){
				continue;
			}
			
			line = StringUtils.replaceSpacesAndTabs(line);
			
			if(startLine){
				normalized.add(line.replaceAll("\\\\", ""));
			} else {
				String previousLine = normalized.get(normalized.size()-1);
				String newLine = previousLine + " " + line;
				newLine = newLine.replaceAll("\\\\", "");
				normalized.set(normalized.size()-1, newLine);
				//System.out.println(newLine);
			}
			
			if(isContinuationLine(line) ){
				continuationLine = true;
				startLine = false;
			} else {
				continuationLine = false;
				startLine = true;
			}
			
		}
		
		return normalized;
	}
	
	private boolean isContinuationLine(String line){
		 if(line.trim().substring(line.trim().length()-1).equals("\\")){
			 return true;
		 }else{
			 return false;
		 }
	}
	
	private boolean validLine(String line){
		if(line != null && !line.trim().equals("") && !line.trim().substring(0, 1).equals("#")){
			//System.out.println("valid line: " + line);
			return true;
		}else{
			//System.out.println("line not active: " + line);
			return false;
		}
	}
	
	private UserSpecification getUserSpecification(Sudoers sudoers, List<String> list, int i) {
		UserSpecification userSpecification = sudoers.new UserSpecification();
		
		boolean continueParsing = false;
		
		String name = null;
		String host = null;
		String[] args = null;
		String values[] = null;
		String userAlias = null;
		for(int j = i; j < list.size(); j++){
			String line = list.get(j);
			//System.out.println(line);
			if(!continueParsing){
				/**
				*/
				args = line.split("=");
				name = args[0];
				name = name.substring(0, name.indexOf(" "));
				host = args[0].substring(args[0].indexOf(" ")+1);
				values = args[1].split(",");
				
				int beginIndex = values[0].indexOf("(");
				int endIndex = values[0].indexOf(")");
				if(beginIndex != -1 && endIndex != -1){
					userAlias = values[0].substring(beginIndex+1,endIndex);
					values[0] = values[0].substring(endIndex+1);
				}else{
					userAlias = "";
				}
				userSpecification.setHostalias(host);
				userSpecification.setName(name);
				userSpecification.setOperatorList(userAlias);
			}else{
				values = line.split(",");
			}
			
			/**
			 * Now parsing second part.  
			 * If last item does not contains '\', it means we can break the loop. 
			 * Otherwise, we will parse next line to look for new values 
			 */
			continueParsing = false;
			for(int k = 0; k < values.length; k++){
				if(values[k].equals("\\")){
					continueParsing = true;
					break;
				}
				userSpecification.addCommand(values[k]);
			}
			if(!continueParsing){
				break;
			}
		}
		return userSpecification;
	}
	
	private Sudoers.Hostalias getHostAlias(Sudoers sudoers, List<String> list, int i, String aliasType){
		Hostalias alias = sudoers.new Hostalias();
		setAlias(alias, sudoers, list, i, aliasType);
		return alias;
	}
	
	private Sudoers.Runalias getRunasAlias(Sudoers sudoers, List<String> list, int i, String aliasType){
		Runalias alias = sudoers.new Runalias();
		setAlias(alias, sudoers, list, i, aliasType);
		return alias;
	}
	
	private Sudoers.Cmndalias getCmndAlias(Sudoers sudoers, List<String> list, int i, String aliasType){
		Cmndalias alias = sudoers.new Cmndalias();
		setAlias(alias, sudoers, list, i, aliasType);
		return alias;
	}
	
	private Sudoers.Useralias getUserAlias(Sudoers sudoers, List<String> list, int i, String aliasType){
		Useralias alias = sudoers.new Useralias();
		setAlias(alias, sudoers, list, i, aliasType);
		return alias;
	}
	
	private void setAlias(Alias alias, Sudoers sudoers, List<String> list, int i, String aliasType){
		boolean continueParsing = false;
		String name = null;
		String[] args = null;
		String values[] = null;
		for(int j = i; j < list.size(); j++){
			String line = list.get(j);
			if(!continueParsing){
				/**
				 * Removing spaces, first part will look like:
				 * before User_Alias       STUDENT_WORKERS
				 * after User_AliasSTUDENT_WORKERS
				 * Now I can remove User_Alias and it will give me only the alias name itself
				*/
				args = line.split("=");
				name = args[0];
				name = name.substring(aliasType.length());
				values = args[1].split(",");  
				alias.setName(name);
			}else{
				values = line.split(",");
			}
			
			/**
			 * Now parsing second part.  
			 * If last item does not contains '\', it means we can break the loop. 
			 * Otherwise, we will parse next line to look for new values 
			 */
			continueParsing = false;
			for(int k = 0; k < values.length; k++){
				if(values[k].equals("\\")){
					continueParsing = true;
					break;
				}
				alias.addValue(values[k]);
			}
			if(!continueParsing){
				break;
			}
		}
	}
	
	/**
	 * This is a helper method, just to avoid the same kind of tests. It tests if the line is null, empty and uncommented before to test 
	 * @param line
	 * @return
	 */
	private boolean testLine(String line, String contains){
		if(line != null && !line.trim().equals("") && !line.trim().substring(0, 1).equals("#") && line.contains(contains)){
			//System.out.println("found: " + contains);
			return true;
		}else{
			return false;
		}
	}
	
	public void exec(String... cmd){
        try {
        	
        	System.out.println("exec: ");
        	String command ="";
        	for(String cmd1: cmd){
        		System.out.print(" " + cmd1);
        		command += cmd1 + " ";
        	}
        	ssh.executeCommand(command);
        } catch (Exception ex) {
        	ex.printStackTrace();
        }		
	}
	
	public String hasSystemAuthorityID(Sudoers sudoers, String id){
		String result = "PASS";
		
		List<Useralias> listUserAlias = sudoers.getListUseralias();
		for(Useralias ua: listUserAlias){
			List<String> values = ua.getValues();
			for(String value: values){
				if(value.equals(id)){
					result = "WARNING";
					System.out.println("found user alias: " + ua.getName() + " - " + value);
				}
			}
		}
		
		List<UserSpecification> listUserSpecification = sudoers.getListUserSpecification();
		for(UserSpecification userSpec: listUserSpecification){
			if( userSpec.getName().equals(id)){
				result = "WARNING";
				System.out.println("found user spec (ID): " + userSpec.getName());
			}
		}
		
		return result;
	}
	
	public String hasSystemAuthorityGroup(Sudoers sudoers, String id){
		String result = "PASS";
		
		List<Useralias> listUserAlias = sudoers.getListUseralias();
		List<Useralias> listUA = new ArrayList<Useralias>();
		for(Useralias ua: listUserAlias){
			List<String> values = ua.getValues();
			for(String value: values){
				if(value.equals("%" + id)){
					System.out.println("found user alias: " + ua.getName() + " - " + value);
					listUA.add(ua);
					result = "WARNING";
					break;
				}
			}
		} 
		
		List<UserSpecification> listUserSpecification = sudoers.getListUserSpecification();
		List<UserSpecification> listUS = new ArrayList<UserSpecification>();
		for(UserSpecification userSpec: listUserSpecification){
			if( userSpec.getName().equals("%"+id)){
				result = "WARNING";
				System.out.println("-------------------------------------");
				System.out.println("Name: " + userSpec.getName() + "<-----");
				System.out.println("Hostalias: " + userSpec.getHostalias());
				System.out.println("Operator List: " + userSpec.getOperatorList());
				List<String> values = userSpec.getCommandList();
				for(String value: values){
					System.out.println("\tCommand: " + value);
				}
				System.out.println("-------------------------------------");
				System.out.println("found user spec (group): " + userSpec.getName());
				
				listUS.add(userSpec);
			}
		}
		
		return result;
	}
	
	public boolean isDefaultGroup(String user, String group) throws IOException, InterruptedException {
		UnixUser uuser = getUnixUser(user);
		
		if(uuser.getDefaultGroup().equals(group)){
			return true;
		}else{
			return false;
		}
	}
	
	public UnixGroup getUnixGroup(String group) throws IOException, InterruptedException {
		UnixGroup ugroup = new UnixGroup();

		String msg = this.ssh.executeCommand("cat /etc/group | grep " + group);
		
		String[] lines = msg.split("\n");
		
		for(String groupLine: lines){
			//System.out.println(groupLine);
			groupLine = groupLine.replaceAll(" ", "");
			groupLine = groupLine.replaceAll("\t", "");
			String parts[] = groupLine.split(":");
			if(!parts[0].equals(group)){
				continue;
			}
			ugroup.setName(parts[0]);
			if(parts.length == 4){
				String users[] = parts[3].split(",");
				
				for(String user: users){
					UnixUser uuser = getUnixUser(user);
					ugroup.addUser(uuser);
				}
			}else if(parts.length == 3){
				UnixUser uuser = getUnixUser(ugroup.getName());
				ugroup.addUser(uuser);
			}
			
		}
		
		return ugroup;
	}
	private List<String> convertArrayToList(String lines) {
		List<String> result = new ArrayList<String>();
		if(lines == null) return result;
		lines = lines.replaceAll("\r", "");
		String[] aLines = lines.split("\n");
		
		for(int i = 0; i < aLines.length; i++){
			result.add(aLines[i]);
		}
		return result;
	}
	

	public UnixUser getUnixUser(String id) throws IOException, InterruptedException{
		UnixUser user = new UnixUser();

		System.out.println("id " + id);
		String msg = ssh.execute("id " + id);
		List<String> list = convertArrayToList(msg);
		
		int beginIndex = -1;
		int endIndex = -1;
		for(String line: list){
			if(line != null && line.substring(0,3).equals("uid")){
				//System.out.println(line);
				//ID
				String[] xargs = line.split(" ");
				//System.out.println(xargs[0]);
				user.setId(getId(xargs[0]));
				
				//Default Group
				beginIndex = line.indexOf("gid");
				beginIndex = line.indexOf("(", beginIndex)+1;
				endIndex = line.indexOf(")", beginIndex);
				String gid = line.substring(beginIndex, endIndex);
				user.setDefaultGroup(gid);
				
				//other groups
				beginIndex = line.indexOf("groups");
				String groups = line.substring(beginIndex);
				String[] values = groups.split(",");
				for(String group: values){
					//System.out.println(group);
					beginIndex = group.indexOf("(")+1;
					endIndex = group.indexOf(")", beginIndex);
					String otherGroup = group.substring(beginIndex, endIndex);
					UnixGroup ugroup = new UnixGroup();
					ugroup.setName(otherGroup);
					user.addGroup(ugroup);
				}
				
			}
			
		}
		
		return user;
	}	
	
	public boolean isGroupMember(String user, String group) throws IOException, InterruptedException {
		boolean result = false;
		
		UnixGroup ugroup = getUnixGroup(group);
		
		List<UnixUser> uusers = ugroup.getUsers();
		for(UnixUser uuser: uusers){
			if(uuser.getId().equals(user)){
				System.out.println("isGroupMember: " + user + " - " + uuser.getId() + " - group: " + group);
				result = true;
			}
		}
		
		return result;
	}
	
	public boolean isGroupMemberOfAnyRootGroup(String user) throws IOException, InterruptedException {
		boolean result = false;
		
		UnixUser uuser = getUnixUser("root");
		List<UnixGroup> ugroups = uuser.getGroups();
		for(UnixGroup ugroup: ugroups){
			//System.out.println("isGroupMemberOfAnyRootGroup: " + user + " - group: " + ugroup.getName() );
			if(	isGroupMember(user, ugroup.getName()) ){
				System.out.println("user: " + user + " is part of Root group: " + ugroup.getName() );
				result = true;
				break;
			}
		}

		return result;
	}
	
	/**
	 * Verify if the group passed is one of the groups that the root user is member of 
	 * @param group
	 * @return
	 * @throws InterruptedException 
	 * @throws IOException 
	 */
	public boolean isRootGroup(String group) throws IOException, InterruptedException {
		boolean result = false;
		
		UnixUser uuser = getUnixUser("root");
		List<UnixGroup> ugroups = uuser.getGroups();
		for(UnixGroup ugroup: ugroups){
			//System.out.println("isGroupMemberOfAnyRootGroup: " + user + " - group: " + ugroup.getName() );
			if(	ugroup.getName().equals(group) ){
				System.out.println("group: " + group + " is a Root group: " + ugroup.getName() );
				result = true;
				break;
			}
		}

		return result;
	}
	
	public boolean isLocalUser(String id) throws IOException, InterruptedException{
		boolean result = false;
		
		String msg = ssh.executeCommand("cat /etc/passwd | grep " + id );
		
		List<String> lines = convertArrayToList(msg);
		if(lines != null && lines.size() > 0){
			String line = lines.get(0);
			if(line.contains(id)){
				result = true;
			}
		}
		
		return result;		
	}
	
	public Collection<File> retrieveFileList(String path, Boolean recursively){
		Collection<File> list = org.apache.commons.io.FileUtils.listFiles(new File(path), null, recursively);
		
		return list; 
	}
	
	
	
	

}
