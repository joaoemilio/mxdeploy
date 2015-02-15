package com.mxdeploy.api.domain;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

public class Server implements Cloneable, Serializable {

	public static Integer SERVERTYPE_SSH = 1;
	public static Integer SERVERTYPE_TELNET = 2;
	public static Integer SERVERTYPE_WINDOWS = 3;
	public static Integer SERVERTYPE_UNKNOWN = 4;
	
	public static String SERVERTYPE_SSH_NAME = "SSH";
	public static String SERVERTYPE_TELNET_NAME = "TELNET";
	public static String SERVERTYPE_WINDOWS_NAME = "WINDOWS";
	public static String SERVERTYPE_UNKNOWN_NAME = null;
	
    /** 
     * Persistent Instance variables. This data is directly 
     * mapped to the columns of database table.
     */
    private String id;
    private String  name;
    private String  hostname;
    private Integer port;
    private Integer type;
    private String  odcs;
    private String  alias;
    private String  address;
	private String defaultSocksUUID;
    private List<ServerCommand> serverCommands = null;
    private Property property;
    private List<Attribute> serverAttributes = new ArrayList<Attribute>();

    public String getDefaultSocksUUID() {
		return defaultSocksUUID;
	}

	public void setDefaultSocksUUID(String defaultSocksUUID) {
		this.defaultSocksUUID = defaultSocksUUID;
	}

    
    public void addServerAttribute(String key, String value){
    	Attribute attr = new Attribute();
    	attr.setKey(key);
    	attr.setValue(value);
    	addServerAttribute(attr);
    }
    
    public void addServerAttribute(Attribute attr){
    	serverAttributes.add(attr);
    }
    
    public String getServerAttribute(String key){
    	Attribute attr = null;
    	for(Attribute at: serverAttributes){
    		if(at.getKey().equals(key)){
    			attr = at;
    			break;
    		}
    	}
    	String result = attr == null?null:attr.getValue();
    	return result;
    }
    
    public void setServerAttribute(String key, String value){
    	Attribute attr = null;
    	for(Attribute at: serverAttributes){
    		if(at.getKey().equals(key)){
    			attr = at;
    			break;
    		}
    	}
    	if(attr != null){
    		attr.setValue(value);
    	}else{
    		addServerAttribute(key, value);
    	}
    }
    
    public List<Attribute> getServerAttributes() {
    	return this.serverAttributes;
    }
    
	/**
	 * @return the properties
	 */
//	public List<Property> getProperties() {
//		return properties;
//	}
//
//	/**
//	 * @param properties the properties to set
//	 */
//	public void addProperty(Property property) {
//		if(this.properties==null){
//			this.properties = new ArrayList<Property>();
//		}
//		this.properties.add(property);
//	}
//
//	public void cleanProperties(){
//		this.properties = new ArrayList<Property>();
//	}
	/** 
     * Constructors. DaoGen generates two constructors by default.
     * The first one takes no arguments and provides the most simple
     * way to create object instance. The another one takes one
     * argument, which is the primary key of the corresponding table.
     */

    public Server () {

    }

    public Server (String idIn) {

          this.id = idIn;

    }


    /** 
     * Get- and Set-methods for persistent variables. The default
     * behaviour does not make any checks against malformed data,
     * so these might require some manual additions.
     */

    public String getId() {
          return this.id;
    }
    public void setId(String idIn) {
          this.id = idIn;
    }

    public String getName() {
          return this.name;
    }
    public void setName(String nameIn) {
          this.name = nameIn;
    }

    public String getHostname() {
          return this.hostname;
    }
    public void setHostname(String hostnameIn) {
          this.hostname = hostnameIn;
    }

    /**
	 * @return the port
	 */
	public Integer getPort() {
		return port;
	}

	/**
	 * @param port the port to set
	 */
	public void setPort(Integer port) {
		this.port = port;
	}

	/**
	 * @return the type
	 */
	public Integer getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(Integer type) {
		this.type = type;
	}
	
	public static Integer getServerType(String serverType) {
		if(serverType == null){
			return SERVERTYPE_UNKNOWN;
		} else if (serverType.equals( SERVERTYPE_SSH_NAME )) {
			return SERVERTYPE_SSH;
		} else if (serverType.equals( SERVERTYPE_TELNET_NAME )) {
			return SERVERTYPE_TELNET;
		} else {
			return SERVERTYPE_WINDOWS;
		}
	}
	
	public static ArrayList<String> getServerTypeList(){
		ArrayList<String> list = new ArrayList<String>();
		list.add(SERVERTYPE_SSH_NAME);
//		list.add(SERVERTYPE_TELNET_NAME);
//		list.add(SERVERTYPE_WINDOWS_NAME);
		return list;
	}

	/**
	 * @return the commands
	 */
	public List<Command> getCommandByProjectAndServer(Project projectValue) {
		List<Command> commandList = new ArrayList<Command>();
		
		this.setNullToServerCommands();
		forceLoadCommandXML(projectValue);
		
		if(this.serverCommands!=null){
			for(ServerCommand serverCommand : this.serverCommands){
				if(serverCommand.getIdProject().equals(projectValue.getId())){
					commandList.add(serverCommand.getCommand());
				}
			}
		}
		
		return commandList;
	}
	
	public List<ServerCommand> getServerCommandByProject(Project projectValue, boolean reload) {
		
		if(reload){
		   loadCommandXML(projectValue);
		} else if(this.serverCommands==null){
		   loadCommandXML(projectValue);
		}
		
		return this.serverCommands;
	}	
	

	/**
	 * @param commands the commands to set
	 */
	public void addServerCommand(ServerCommand serverCommand) {
		if( this.serverCommands == null ){
			this.serverCommands = new ArrayList<ServerCommand>();
		}
		if(serverCommand!=null)
		   this.serverCommands.add(serverCommand);
	}
	
	public void createServerCommandList(){
		if( this.serverCommands == null ){
			this.serverCommands = new ArrayList<ServerCommand>();
		}		
	}
	
	public void setNullToServerCommands(){
		this.serverCommands=null;
	}
	
	public void loadCommandXML(Project projectValue){
		this.serverCommands = new ArrayList<ServerCommand>();
		
		String serverCommandPath = Database.getProjectPath() + "/" + projectValue.getAlias() + "/serverCommands";
		String commandsPath = Database.getProjectPath() + "/" + projectValue.getAlias() + "/commands";
		List<String> serverCommandNameList = new ArrayList<String>();
		
		File file = new File(serverCommandPath);
		File[] files = file.listFiles();
		for(int i=0; i < files.length; i++){
			if(!files[i].isDirectory() && files[i].getName().endsWith(".xml")){
				serverCommandNameList.add(files[i].getName());
			}
		}
		
		XStream xstream = new XStream(new DomDriver());
		if(!serverCommandNameList.isEmpty()){
			for(String serverCommandName : serverCommandNameList ){
				try {
					ServerCommand serverCommand = (ServerCommand)xstream.fromXML(new FileReader(serverCommandPath + "/"+ serverCommandName));
					Command command = (Command)xstream.fromXML(new FileReader(commandsPath + "/"+ serverCommand.getIdCommand()+".xml"));
					serverCommand.setCommand(command);
					
					for(Server server : projectValue.getServers()){
						if( getId().equals(server.getId()) && server.getId().equals(serverCommand.getIdServer()) ){
							server.addServerCommand(serverCommand);
							break;
						}
					}
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public void forceLoadCommandXML(Project projectValue){
		
		String serverCommandPath = Database.getProjectPath() + "/" + projectValue.getAlias() + "/serverCommands";
		String commandsPath = Database.getProjectPath() + "/" + projectValue.getAlias() + "/commands";
		List<String> serverCommandNameList = new ArrayList<String>();
		
		File file = new File(serverCommandPath);
		File[] files = file.listFiles();
		for(int i=0; i < files.length; i++){
			if(!files[i].isDirectory() && files[i].getName().endsWith(".xml")){
				serverCommandNameList.add(files[i].getName());
			}
		}
		
		XStream xstream = new XStream(new DomDriver());
		if(!serverCommandNameList.isEmpty()){
			for(String serverCommandName : serverCommandNameList ){
				try {
					ServerCommand serverCommand = (ServerCommand)xstream.fromXML(new FileReader(serverCommandPath + "/"+ serverCommandName));
					if(this.getId().equals(serverCommand.getIdServer())){
					   Command command = (Command)xstream.fromXML(new FileReader(commandsPath + "/"+ serverCommand.getIdCommand()+".xml"));
					   serverCommand.setCommand(command);
					   this.addServerCommand(serverCommand);
//					   for(Server server : projectValue.getServers()){
//						   if( getId().equals(server.getId()) && server.getId().equals(serverCommand.getIdServer()) ){
//							server.addServerCommand(serverCommand);
//						   break;
//						}
					}
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	/**
	 * @return the serverCommands
	 */
	public List<ServerCommand> getServerCommands() {
		return serverCommands;
	}

	/**
	 * @return the property
	 */
	public Property getProperty() {
		return property;
	}

	/**
	 * @param property the property to set
	 */
	public void setProperty(Property property) {
		this.property = property;
	}
  
	/**
	 * @return the odcs
	 */
	public String getOdcs() {
		return odcs;
	}

	/**
	 * @param odcs the odcs to set
	 */
	public void setOdcs(String odcs) {
		this.odcs = odcs;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}    
	
}


