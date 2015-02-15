package com.mxdeploy.swt;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.MessageBox;

import com.mxdeploy.AccountConfig;
import com.mxdeploy.MXTerminal;
import com.mxdeploy.Preferences;
import com.mxdeploy.SocksConfig;
import com.mxdeploy.api.domain.Database;
import com.mxdeploy.api.service.PreferencesService;
import com.mxdeploy.images.Constant;
import com.mxsecurity.util.PWSec;
import com.mxterminal.swt.util.Socks5Property;
import com.mxterminal.swt.util.TerminalDomainRule;
import com.mxterminal.swt.util.TerminalExecuteCommand;
import com.mxterminal.swt.util.TerminalPrivateKey;
import com.mxterminal.swt.util.TerminalProperty;
import com.mxterminal.swt.util.Update;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

public class MainHelper {
	//static Logger logger = Logger.getLogger(MainHelper.class);
	
	Update update = Update.getInstance();
	String loginname = System.getProperty("user.name");
    
	//User user = null;
	String macAddress = null;
	public String intranetID = ""; 
	String passWordNotes = null;
	public static String databaseHostname=null;
	public static String[] serverComboItems;
	
	public void loadGlobalSystemProps(String account){
		Properties sysProps = new Properties();
		try {
			if(account!=null){
				Database.WORKSPACE_NAME=account;
				System.out.println(Database.WORKSPACE_NAME);
			} else {
				Database.WORKSPACE_NAME=System.getenv("MXD_ACCOUNT");
			}
			Database.MODULE_PATH=Database.WORKSPACE_PATH+"/"+Database.WORKSPACE_NAME+"/"+"modules";
			Database.MODULE_GROUP_FILE=Database.WORKSPACE_PATH+"/"+Database.WORKSPACE_NAME+"/"+"modules/module-groups.xml";	
			
		    String path = Database.HOME+"/mxterminal.properties"; //System.getProperty("user.dir")
			
			FileInputStream finp = new FileInputStream(path);  
			
			sysProps.load(finp);
			if (sysProps.getProperty("terminal.savelines")!=null){
				TerminalProperty.setSavelines( sysProps.getProperty("terminal.savelines") );
			}
			
			if (sysProps.getProperty("terminal.fontName")!=null){
				TerminalProperty.setFontName( sysProps.getProperty("terminal.fontName") );
			}		
			
			if (sysProps.getProperty("terminal.fontSize")!=null){
				TerminalProperty.setFontSize( sysProps.getProperty("terminal.fontSize") );
			}	
			
			if (sysProps.getProperty("terminal.encoding")!=null){
				TerminalProperty.setEncoding( sysProps.getProperty("terminal.encoding") );
			}	

			if (sysProps.getProperty("terminal.visual-bell")!=null){
				TerminalProperty.setVisualBell( sysProps.getProperty("terminal.visual-bell") );
			}	
			
			if (sysProps.getProperty("terminal.visual-bell")!=null){
				TerminalProperty.setVisualBell( sysProps.getProperty("terminal.visual-bell") );
			}	
			if (sysProps.getProperty("termina.copy-select")!=null){
				TerminalProperty.setCopySelect( sysProps.getProperty("termina.copy-select") );
			}	
			if (sysProps.getProperty("terminal.paste-button")!=null){
				TerminalProperty.setPasteButton( sysProps.getProperty("terminal.paste-button") );
			}	
			if (sysProps.getProperty("terminal.fg-color")!=null){
				TerminalProperty.setFgColor( sysProps.getProperty("terminal.fg-color") );
			}	
			if (sysProps.getProperty("terminal.bg-color")!=null){
				TerminalProperty.setBgColor( sysProps.getProperty("terminal.bg-color") );
			}	
			if (sysProps.getProperty("terminal.cursor-color")!=null){
				TerminalProperty.setCursorColor( sysProps.getProperty("terminal.cursor-color") );
			}			
			if (sysProps.getProperty("terminal.backspace-send")!=null){
				TerminalProperty.setBackspaceSend( sysProps.getProperty("terminal.backspace-send") );
			}	
			if (sysProps.getProperty("terminal.delete-send")!=null){
				TerminalProperty.setDeleteSend( sysProps.getProperty("terminal.delete-send") );
			}
			if (sysProps.getProperty("terminal.keepassx.path")!=null){
				File file = new File(sysProps.getProperty("terminal.keepassx.path"));
				if( file.exists() ){				
					TerminalProperty.setKeepassxPath( sysProps.getProperty("terminal.keepassx.path") );
				}
			}		
			
			if (sysProps.getProperty("terminal.keepassx.keyfile")!=null){
				File file = new File(sysProps.getProperty("terminal.keepassx.keyfile"));
				if( file.exists() ){
					TerminalProperty.setKeepassxKeyfile( sysProps.getProperty("terminal.keepassx.keyfile") );
				}
			}				
			
			if (sysProps.getProperty("terminal.keepassx.imageNumber")!=null){
				TerminalProperty.setKeepassxIconNumber( sysProps.getProperty("terminal.keepassx.imageNumber") );
			}	

			if (sysProps.getProperty("beanshell.geany.enable")!=null){
				TerminalProperty.setBeanshellGeanyEnable( sysProps.getProperty("beanshell.geany.enable") );
			} else {
				TerminalProperty.setBeanshellGeanyEnable("false");
			}
			
			int count=0;
			while(true){
				count++;
				if (sysProps.getProperty("terminal.private-key.id."+Database.WORKSPACE_NAME+"."+count)!=null){
					boolean saveIt = true;
					TerminalPrivateKey tpk = new TerminalPrivateKey();
					tpk.setId(sysProps.getProperty("terminal.private-key.id."+Database.WORKSPACE_NAME+"."+count));
					
					if (sysProps.getProperty("terminal.private-key.path."+Database.WORKSPACE_NAME+"."+count)!=null){
						tpk.setPrivateKey(sysProps.getProperty("terminal.private-key.path."+Database.WORKSPACE_NAME+"."+count));
						
						if (sysProps.getProperty("terminal.private-key.regexp."+Database.WORKSPACE_NAME+"."+count)!=null){
							tpk.setRegexp(sysProps.getProperty("terminal.private-key.regexp."+Database.WORKSPACE_NAME+"."+count));
						} else {
							saveIt=false;
						}						
					} else {
						saveIt=false;
					}
					
					if (sysProps.getProperty("terminal.private-key.username."+Database.WORKSPACE_NAME+"."+count)!=null){
						tpk.setUsername(sysProps.getProperty("terminal.private-key.username."+Database.WORKSPACE_NAME+"."+count));
					} 
					
					if(saveIt){
					   TerminalProperty.addTerminalPrivateKey(tpk);
					}
				} else {
					break;
				}
			}			

			count=0;
			while(true){
				count++;
				if (sysProps.getProperty("terminal.command.execute."+Database.WORKSPACE_NAME+"."+count)!=null){
					boolean saveIt = true;
					TerminalExecuteCommand tec = new TerminalExecuteCommand();
					tec.setExecuteCommand(sysProps.getProperty("terminal.command.execute."+Database.WORKSPACE_NAME+"."+count));
					
					
					if (sysProps.getProperty("terminal.command.regexp."+Database.WORKSPACE_NAME+"."+count)!=null){
						tec.setRegexp(sysProps.getProperty("terminal.command.regexp."+Database.WORKSPACE_NAME+"."+count));
					} else {
						tec.setRegexp(".*");
					}						
					
					if(saveIt){
					   TerminalProperty.addTerminalExecuteCommand(tec);
					}
				} else {
					break;
				}
			}			
			
			count=0;
			while(true){
				count++;
				if (sysProps.getProperty("terminal.domain.name."+Database.WORKSPACE_NAME+"."+count)!=null){
					boolean saveIt = true;
					TerminalDomainRule tdr = new TerminalDomainRule();
					tdr.setDomainName(sysProps.getProperty("terminal.domain.name."+Database.WORKSPACE_NAME+"."+count));
					
					
					if (sysProps.getProperty("terminal.domain.regexp."+Database.WORKSPACE_NAME+"."+count)!=null){
						tdr.setRegexp(sysProps.getProperty("terminal.domain.regexp."+Database.WORKSPACE_NAME+"."+count));
					} else {
						tdr.setRegexp(".*");
					}						
					
					if(saveIt){
					   TerminalProperty.addTerminalDomainRule(tdr);
					}
				} else {
					break;
				}
			}			
						
			finp.close();
			
		} catch (IOException e) {
			MainShell.sendMessage(Constant.MSN_SEE_LOG_ERROR, SWT.OK | SWT.ICON_ERROR);
			e.printStackTrace();
		}
	}		
	
	public void loadSystemProps(String account){
		Properties sysProps = new Properties();
		try {
			if(account!=null){
				Database.WORKSPACE_NAME=account;
				System.out.println(Database.WORKSPACE_NAME);
			} else {
				Database.WORKSPACE_NAME=System.getenv("MXD_ACCOUNT");
			}
			
			String path = Database.WORKSPACE_PATH+"/"+Database.WORKSPACE_NAME+"/mxterminal.properties";
			File fbtprop = new File(path);
			if( !fbtprop.exists() ){
			     return;
			}
			
			FileInputStream finp = new FileInputStream(path);  
			
			sysProps.load(finp);
			if (sysProps.getProperty("terminal.savelines")!=null){
				TerminalProperty.setSavelines( sysProps.getProperty("terminal.savelines") );
			}
			
			if (sysProps.getProperty("terminal.fontName")!=null){
				TerminalProperty.setFontName( sysProps.getProperty("terminal.fontName") );
			}		
			
			if (sysProps.getProperty("terminal.fontSize")!=null){
				TerminalProperty.setFontSize( sysProps.getProperty("terminal.fontSize") );
			}	
			
			if (sysProps.getProperty("terminal.encoding")!=null){
				TerminalProperty.setEncoding( sysProps.getProperty("terminal.encoding") );
			}	

			if (sysProps.getProperty("terminal.visual-bell")!=null){
				TerminalProperty.setVisualBell( sysProps.getProperty("terminal.visual-bell") );
			}	
			
			if (sysProps.getProperty("terminal.visual-bell")!=null){
				TerminalProperty.setVisualBell( sysProps.getProperty("terminal.visual-bell") );
			}	
			if (sysProps.getProperty("termina.copy-select")!=null){
				TerminalProperty.setCopySelect( sysProps.getProperty("termina.copy-select") );
			}	
			if (sysProps.getProperty("terminal.paste-button")!=null){
				TerminalProperty.setPasteButton( sysProps.getProperty("terminal.paste-button") );
			}	
			if (sysProps.getProperty("terminal.fg-color")!=null){
				TerminalProperty.setFgColor( sysProps.getProperty("terminal.fg-color") );
			}	
			if (sysProps.getProperty("terminal.bg-color")!=null){
				TerminalProperty.setBgColor( sysProps.getProperty("terminal.bg-color") );
			}	
			if (sysProps.getProperty("terminal.cursor-color")!=null){
				TerminalProperty.setCursorColor( sysProps.getProperty("terminal.cursor-color") );
			}			
			if (sysProps.getProperty("terminal.backspace-send")!=null){
				TerminalProperty.setBackspaceSend( sysProps.getProperty("terminal.backspace-send") );
			}	
			if (sysProps.getProperty("terminal.delete-send")!=null){
				TerminalProperty.setDeleteSend( sysProps.getProperty("terminal.delete-send") );
			}
			
			int count=0;
			while(true){
				count++;
				if (sysProps.getProperty("terminal.command.execute."+Database.WORKSPACE_NAME+"."+count)!=null){
					boolean saveIt = true;
					TerminalExecuteCommand tec = new TerminalExecuteCommand();
					tec.setExecuteCommand(sysProps.getProperty("terminal.command.execute."+Database.WORKSPACE_NAME+"."+count));
					
					
					if (sysProps.getProperty("terminal.command.regexp."+Database.WORKSPACE_NAME+"."+count)!=null){
						tec.setRegexp(sysProps.getProperty("terminal.command.regexp."+Database.WORKSPACE_NAME+"."+count));
					} else {
						tec.setRegexp(".*");
					}						
					
					if(saveIt){
					   TerminalProperty.addTerminalExecuteCommand(tec);
					}
				} else {
					break;
				}
			}			
			
			count=0;
			while(true){
				count++;
				if (sysProps.getProperty("terminal.domain.name."+Database.WORKSPACE_NAME+"."+count)!=null){
					boolean saveIt = true;
					TerminalDomainRule tdr = new TerminalDomainRule();
					tdr.setDomainName(sysProps.getProperty("terminal.domain.name."+Database.WORKSPACE_NAME+"."+count));
					
					
					if (sysProps.getProperty("terminal.domain.regexp."+Database.WORKSPACE_NAME+"."+count)!=null){
						tdr.setRegexp(sysProps.getProperty("terminal.domain.regexp."+Database.WORKSPACE_NAME+"."+count));
					} else {
						tdr.setRegexp(".*");
					}						
					
					if(saveIt){
					   TerminalProperty.addTerminalDomainRule(tdr);
					}
				} else {
					break;
				}
			}			
						
			finp.close();
			
		} catch (IOException e) {
			MainShell.sendMessage(Constant.MSN_SEE_LOG_ERROR, SWT.OK | SWT.ICON_ERROR);
			e.printStackTrace();
		}
	}		
	
	public void loadGlobalGatewaySystemProps(String account){
		Properties sysProps = new Properties();
		String path = Database.HOME+"/ssh.properties"; //System.getProperty("user.dir")
		FileInputStream finp = null;
		
		try {
			if(account!=null){
				Database.WORKSPACE_NAME=account;
				System.out.println(Database.WORKSPACE_NAME);
			} else {
				Database.WORKSPACE_NAME=System.getenv("MXD_ACCOUNT");
			}
			
			finp = new FileInputStream(path);
			
			sysProps.load(finp);
			
			String localhostusername = sysProps.getProperty("ssh.localhost.username");
			if (localhostusername!=null && localhostusername.trim().length()>0 ){
				TerminalProperty.setLocalhostUsername(localhostusername);
				
				String localhostprivatekey = sysProps.getProperty("ssh.localhost.private-key");
				if( localhostprivatekey!=null && localhostprivatekey.trim().length()>0 ){
					TerminalProperty.setLocalhostPrivateKey(localhostprivatekey);
				}
				
			}
			
			int count=0;
			while(true){
				count++;
				
				String socks5port = sysProps.getProperty("ssh.socks5.port."+Database.WORKSPACE_NAME+"."+count);
				if( socks5port!=null && socks5port.trim().length()>0 ){
					
					Socks5Property socks5Property = new Socks5Property();
					socks5Property.setPort( socks5port );
						
					String socks5regexp = sysProps.getProperty("ssh.socks5.regexp."+Database.WORKSPACE_NAME+"."+count);
					if( socks5regexp!=null && socks5regexp.trim().length()>0 ){
						socks5Property.setRegexp( socks5regexp );
					} else {
						socks5Property.setRegexp(".*");
					}
					
					String proxy = sysProps.getProperty("ssh.socks5.proxy."+Database.WORKSPACE_NAME+"."+count);
					if( proxy!=null && proxy.trim().length()>0 ){
						socks5Property.setProxy( proxy );
					}	
					
					String proxyPort = sysProps.getProperty("ssh.socks5.proxy.port."+Database.WORKSPACE_NAME+"."+count);
					if( proxyPort!=null && proxyPort.trim().length()>0 ){
						socks5Property.setProxyPort( proxyPort );
					}	
					
					String gateway = sysProps.getProperty("ssh.socks5.gateway."+Database.WORKSPACE_NAME+"."+count);
					if( gateway!=null && gateway.trim().length()>0 ){
						socks5Property.setGateway(gateway);
					}				
					TerminalProperty.addSocks5Property( socks5Property );
					
				} else {
					break;
				}
			}

			
			
						
			
			
		} catch (IOException e) {
			MainShell.sendMessage(Constant.MSN_SEE_LOG_ERROR, SWT.OK | SWT.ICON_ERROR);
			e.printStackTrace();
		} finally {
			try {
				finp.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}	

	public void loadGatewaySystemProps(String account){
		Properties sysProps = new Properties();
		String path = Database.WORKSPACE_PATH+"/"+Database.WORKSPACE_NAME+"/ssh.properties";
		
		File fbtprop = new File(path);
		if( !fbtprop.exists() ){
		     return;
		}
		
		FileInputStream finp = null;
		
		try {
			if(account!=null){
				Database.WORKSPACE_NAME=account;
				System.out.println(Database.WORKSPACE_NAME);
			} else {
				Database.WORKSPACE_NAME=System.getenv("MXD_ACCOUNT");
			}
			
			finp = new FileInputStream(path);
			
			sysProps.load(finp);
			
		
			int count=0;
			while(true){
				count++;
				
				String socks5port = sysProps.getProperty("ssh.socks5.port."+Database.WORKSPACE_NAME+"."+count);
				if( socks5port!=null && socks5port.trim().length()>0 ){
					
					Socks5Property socks5Property = new Socks5Property();
					socks5Property.setPort(socks5port);
						
					String socks5regexp = sysProps.getProperty("ssh.socks5.regexp."+Database.WORKSPACE_NAME+"."+count);
					if( socks5regexp!=null && socks5regexp.trim().length()>0 ){
						socks5Property.setRegexp(socks5regexp);
					} else {
						socks5Property.setRegexp(".*");
					}
					
					String proxy = sysProps.getProperty("ssh.socks5.proxy."+Database.WORKSPACE_NAME+"."+count);
					if( proxy!=null && proxy.trim().length()>0 ){
						socks5Property.setProxy(proxy);
					}	

					String proxyPort = sysProps.getProperty("ssh.socks5.proxy.port."+Database.WORKSPACE_NAME+"."+count);
					if( proxyPort!=null && proxyPort.trim().length()>0 ){
						socks5Property.setProxyPort(proxyPort);
					}	
					
					String gateway = sysProps.getProperty("ssh.socks5.gateway."+Database.WORKSPACE_NAME+"."+count);
					if( gateway!=null && gateway.trim().length()>0 ){
						socks5Property.setGateway(gateway);
					}						
					TerminalProperty.addSocks5Property(socks5Property);
					
				} else {
					break;
				}
			}

			
		} catch (IOException e) {
			MainShell.sendMessage(Constant.MSN_SEE_LOG_ERROR, SWT.OK | SWT.ICON_ERROR);
			e.printStackTrace();
		} finally {
			try {
				finp.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}	
	
	/**
	 * Load Socks.xml to SocksConfig object
	 *
	 */
	public void loadAccountConfig() {
		String dir = Database.WORKSPACE_PATH+ "/" + Database.WORKSPACE_NAME;
		String path = dir + "/config.xml";
		File file = new File(path);
		if(!file.exists()) {
			System.err.println("config.xml has not been found on path: " + path);
			return;
		}
		
		XStream xstream = new XStream(new DomDriver());
		try {
			File filePrefs = new File(dir + "/preferences.xml");
			PreferencesService service = new PreferencesService();
			if(!filePrefs.exists()) {
				Preferences p1 = new Preferences();
				service.save(p1);
				System.err.println("Preferences not found. Creating it at: " + dir);
			}
			Preferences prefs = service.loadPreferences();
			MXTerminal.setPreferences(prefs);
					
			AccountConfig config = (AccountConfig)xstream.fromXML(new FileInputStream(file));
			AccountConfig.setInstance(config);
			
			List<SocksConfig> list = config.getSocksList();
			
			//check for encoding requests
			boolean regenconfig = false;
			for(SocksConfig cfg: list) {
				if(cfg.getEncondedPassword() != null && cfg.getEncondedPassword().contains("$encrypt$")) {
					regenconfig = true;
					String password = cfg.getEncondedPassword().substring(9);
					String encodedPassword = password;
					try {
						encodedPassword = PWSec.encrypt(password);
					} catch (Exception e) {
						System.err.println("Error while trying to encrypt password for socks config: " + cfg.getName());
						e.printStackTrace();
					}
					cfg.setEncodedPassword(encodedPassword);
				}
			}
			
			if(regenconfig) {
				try {
					xstream.toXML(config, new FileWriter(file));
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * @return the loginname
	 */
	public String getLoginname() {
		return loginname;
	}
	
	/**
	 * @return the update
	 */
	public Update getUpdate() {
		return update;
	}

//	public void setTextLabelFooter(String str, boolean isAlert){
//		MainShell.getToolBarFooterComposite().getObsCLabel().setText(str);
//		if(isAlert){
//			MainShell.getToolBarFooterComposite().getObsCLabel().setFont( new Font(Display.getDefault(), "Tahoma", 8, SWT.BOLD) );
//			MainShell.getToolBarFooterComposite().getObsCLabel().setForeground(MainShell.display.getSystemColor(SWT.COLOR_RED));
//		} else {
//			MainShell.getToolBarFooterComposite().getObsCLabel().setFont(new Font(Display.getDefault(), "Tahoma", 8, SWT.NONE));
//			MainShell.getToolBarFooterComposite().getObsCLabel().setForeground(MainShell.display.getSystemColor(SWT.COLOR_BLACK));
//		}
//	}

	/**
	 * @return the macAddress
	 */
	public String getMacAddress() {
		return macAddress;
	}

	/**
	 * @param macAddress the macAddress to set
	 */
	public void setMacAddress(String macAddress) {
		this.macAddress = macAddress;
	}
	
    public boolean warning(String warning){
    	MessageBox messageBox = new MessageBox(MainShell.sShell, SWT.ICON_QUESTION | SWT.OK);
        messageBox.setMessage(warning);
        messageBox.setText("Warning");
        if (messageBox.open() == SWT.OK)
        {
          return true;
        }
        return true;
    }

	/**
	 * @return the passWordNotes
	 */
	public String getPassWordNotes() {
		return passWordNotes;
	}

	/**
	 * @return the intranetID
	 */
	public String getIntranetID() {
		return intranetID;
	}
	
    public static List<String> getHostNames() {
    	List<String>        list = null;
    	File        tmpFile;
		String      fileName     = null;
		String sshHomeDir = Database.getServerPath(); //System.getProperty("user.dir");
	  
		fileName = sshHomeDir; //+ "/hostkeys";
		tmpFile = new File(fileName);

		if(!tmpFile.exists()) {
			try {
				tmpFile.mkdir();
			} catch (Throwable t) {
				//logger.error(t.getMessage());
			}
		}

		if(!tmpFile.exists() || !tmpFile.isDirectory()) {
		  //logger.warn("No hostkeys directory");
          return null;
		}
		File[] files = tmpFile.listFiles();
		list = new ArrayList<String>();
    	for (int i = 0; i < files.length; i++) {
    		if(files[i].getName().endsWith(".xml")){
	    		//int lengh = new Long(files[i].getName().length()-4).intValue();
	    		//String hostname = files[i].getName().substring(7,lengh);
    			String name = files[i].getName().replace(".xml", "");
	    		list.add(name);
    		}
    	}    
    	return list;
    }
    
    public static String[] loadServerComboItem(){
        final List<String> list = getHostNames();
        serverComboItems = new String[list.size()];
        if ( list !=null ){

    		    	if( list !=null){
   		   		        int count=0;
   						for(String serverName : list){
   						    serverComboItems[count]=serverName;
   						    count++;
   		   		        }
   		   		        
   				        Arrays.sort(serverComboItems);
   					}

    	  return serverComboItems;
        }
        return null;
    }
	
  
}
