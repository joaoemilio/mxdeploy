package com.mxterminal.ssh;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.UnknownHostException;
import java.util.Enumeration;
import java.util.Properties;
import java.util.Vector;
import java.util.concurrent.CancellationException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.eclipse.swt.widgets.Display;

import com.mxdeploy.swt.dialogs.authentication.SshAuthenticationDialog;
import com.mxsecurity.util.RandomSeed;
import com.mxterminal.channel.ChannelSSH2;
import com.mxterminal.channel.SessionChannel;
import com.mxterminal.channel.TCPChannel;
import com.mxterminal.console.ConsoleInputListener;
import com.mxterminal.console.ConsoleWin;
import com.mxterminal.console.ConsoleWindows;
import com.mxterminal.ssh2.ConnectionEventAdapterSSH2;
import com.mxterminal.ssh2.ConnectionSSH2;
import com.mxterminal.ssh2.HostKeyVerifierSSH2;
import com.mxterminal.ssh2.InteractorSSH2;
import com.mxterminal.ssh2.ListUtilSSH2;
import com.mxterminal.ssh2.ListenerSSH2;
import com.mxterminal.ssh2.PreferencesSSH2;
import com.mxterminal.ssh2.SSH2;
import com.mxterminal.ssh2.StreamFilterFactory;
import com.mxterminal.ssh2.StreamSnifferSSH2;
import com.mxterminal.ssh2.TerminalAdapterImplSSH2;
import com.mxterminal.ssh2.TerminalAdapterSSH2;
import com.mxterminal.ssh2.TransportEventAdapterSSH2;
import com.mxterminal.ssh2.TransportSSH2;
import com.mxterminal.ssh2.auth.AuthHostBased;
import com.mxterminal.ssh2.auth.AuthKbdInteract;
import com.mxterminal.ssh2.auth.AuthPassword;
import com.mxterminal.ssh2.auth.AuthPublicKey;
import com.mxterminal.ssh2.auth.AuthSSHComSecurID;
import com.mxterminal.ssh2.auth.Authenticator;
import com.mxterminal.ssh2.auth.UserAuth;
import com.mxterminal.ssh2.exception.AccessDeniedException;
import com.mxterminal.ssh2.exception.ExceptionSSH2;
import com.mxterminal.ssh2.exception.FatalExceptionSSH2;
import com.mxterminal.ssh2.exception.UserCancelException;
import com.mxterminal.ssh2.key.BTSignature;
import com.mxterminal.ssh2.key.BTSignatureException;
import com.mxterminal.ssh2.key.KeyFingerprint;
import com.mxterminal.ssh2.key.KeyPairFile;
import com.mxterminal.swt.util.TerminalExecuteCommand;
import com.mxterminal.swt.util.TerminalPrivateKey;
import com.mxterminal.swt.util.TerminalProperty;

public final class InteractiveClientSSH extends ClientSSH
            implements Runnable, InteractorSSH, InteractorSSH2,
    ConsoleInputListener {
	static Logger logger = Logger.getLogger(InteractiveClientSSH.class);
    public boolean       isSSH2 = false;
    public TransportSSH2 transport;
    ConnectionSSH2       connection;
    TerminalAdapterSSH2  termAdapter;

    public boolean wantHelpInfo       = true;
    public String  customStartMessage = null;

    StdIOSSH           sshStdIO;
    PropertyHandlerSSH propsHandler;
    
    private UserAuth userAuth = null;

    public boolean quiet;
    public boolean exitOnLogout;
    boolean        initQuiet;
    boolean        isFirstPasswdAuth;
    private SessionChannel session = null;
    public boolean automaticLogin = true;
    public boolean isUnknownHost = false;    
    //private String password = null;
    private int countPassword=0;
    
    public InteractiveClientSSH(boolean quiet, boolean exitOnLogout, PropertyHandlerSSH propsHandler, boolean automaticLogin) {
        super(propsHandler, propsHandler);
        this.automaticLogin = automaticLogin;
        this.propsHandler = propsHandler;
        this.interactor   = this; // !!! OUCH

        propsHandler.setInteractor(this);
        propsHandler.setClient(this);

        this.quiet        = quiet;
        this.exitOnLogout = exitOnLogout;
        this.initQuiet    = quiet;

        setConsole(new StdIOSSH());
        sshStdIO = (StdIOSSH)console;
        sshStdIO.setClient(this);
        
//        if( propsHandler.getPassword()!=null){
//        	this.password = propsHandler.getPassword();
//        }
        
        
    }

    public InteractiveClientSSH(InteractiveClientSSH clone,boolean automaticLogin) {
        this(true, true, new PropertyHandlerSSH(clone.propsHandler),true);
        
        this.activateTunnels = false;

        this.wantHelpInfo       = clone.wantHelpInfo;
        this.customStartMessage = clone.customStartMessage;
        //propsHandler.setPassword( clone.getSSH2UserAuth().getPassword() );
    }

    public PropertyHandlerSSH getPropertyHandler() {
        return propsHandler;
    }

    public void splashScreen() {
        ConsoleWin t = getTerminalWin();

        if(t != null) {
            t.clearScreen();
            t.setCursorPos(0, 0);
        }

        console.println("Initializing MXTerminal... "); // + Version.version);
//        console.println(Version.copyright);
//        console.println(Version.licenseMessage);

    }

    public ByteArrayOutputStream readResource(String name) {
        InputStream in = getClass().getResourceAsStream(name);
        ByteArrayOutputStream baos = null;
        if(in != null) {
            baos = new ByteArrayOutputStream();
            try {
                int c;
                while((c = in.read()) >= 0)
                    baos.write(c);
            } catch(IOException e) {
                // !!!
                System.err.println("ERROR reading resource " + name + " : " + e);
            }
        }
        return baos;
    }

    void initRandomSeed() {
        if(!haveSecureRandom()) {
            //console.print("\rInitializing...");
            initSeedGenerator();
            //console.print("done");
        }
    }

    public void doSingleCommand(String commandLine)
    throws Exception {
        this.commandLine = commandLine;

        splashScreen();
        initRandomSeed();
        startSSHClient(false);
    }

    public void run() {
        boolean gotExtMsg;
        isUnknownHost = false;
        //installLogo();

        boolean keepRunning = true;
        while(keepRunning) {
            gotExtMsg = false;
            try {
                splashScreen();

                initRandomSeed();

                startSSHClient(true);

                if(sshStdIO.isConnected()) {
                    // Server died on us without sending disconnect
                    sshStdIO.serverDisconnect("\n\r\n\rServer died or connection lost");
                    disconnect(false);
                    propsHandler.clearServerSetting();
                }

                // !!! Wait for last session to close down entirely (i.e. so
                // disconnected gets a chance to be called...)
                //
                Thread.sleep(1000);

                try {
                    propsHandler.checkSave();
                } catch (IOException e) {
                    alert("Error saving settings!");
                }

            } catch(ClientSSH.AuthFailException e) {
                alert("Authentication failed, " + e.getMessage());
                propsHandler.clearPasswords();
            } catch(StdIOSSH.SSHExternalMessage e) {
                gotExtMsg = true;
                String msg = e.getMessage();

                if(msg != null && msg.trim().length() > 0) {
                    alert(e.getMessage());
                }

            } catch(UnknownHostException e) {
                String host = e.getMessage();
                isUnknownHost=true;
                if(propsHandler.getProperty("proxytype").equals("none")) {
                    alert("\r\nUnknown host: " + host);
                } else {
                    alert("\r\nUnknown proxy host: " + host);
                }
                propsHandler.clearServerSetting();

            } catch(FileNotFoundException e) {
                alert("\r\nFile not found: " + e.getMessage());

            } catch(Exception e) {
                String msg = e.getMessage();
                if(msg == null || msg.trim().length() == 0)
                    msg = e.toString();
                    msg = "\r\nCan not connect on server " + propsHandler.getProperty("server") + "\r\n" + "Message : Unknown Host"
                    		+ "\r\n" + "Please check :"
                    		+ "\r\n" + "[1] Are you using the correct server name or IP Address ?"
                    		+ "\r\n" + "[2] Are you using Gateway and it was started correctly ?"
                    		+ "\r\n" + "[3] Are you using Domain Rules and it was configured correctly ?"
                    		+ "\r\n" + "[4] Are you using Proxy and it was configured correctly ?"
                    		+ "\r\n" + "[5] Is the network working correctly ?";
                alert(msg);
                if(SSH.DEBUGMORE) {
                    e.printStackTrace();
                }

            } catch(ThreadDeath death) {
                if(controller != null)
                    controller.killAll();
                controller = null;
                throw death;
            }

            propsHandler.passivateProperties();
            activateTunnels = true;

            if(!gotExtMsg) {
                if(!propsHandler.savePasswords || usedOTP) {
                    propsHandler.clearPasswords();
                }
                propsHandler.currentPropsFile = null;
                if(!propsHandler.autoLoadProps) {
                    propsHandler.clearPasswords();
                    initQuiet = false;
                }
                quiet = false;
            }

            controller = null;

            ConsoleWin t = getTerminalWin();
            if(t != null)
                t.setTitle(null);

            keepRunning = !exitOnLogout;
        }
    }

    public long getConnectTimeout() {
        return propsHandler.getPropertyI("connect-timeout")*1000;
    }

    public long getHelloTimeout() {
        return propsHandler.getPropertyI("hello-timeout")*1000;
    }    

    private void startSSHClient(boolean shell)  throws Exception {
        isFirstPasswdAuth = true;

        // This starts a connection to the sshd and all the related stuff...
        //
        bootSSH(shell, true);

        int    major = 2;
        String proto = propsHandler.getProperty("protocol");

        long hellotimeout = getHelloTimeout();

        if("auto".equals(proto)) {
            int oldtimeout = sshSocket.getSoTimeout();
            if (hellotimeout >= 0)
                sshSocket.setSoTimeout((int)hellotimeout);
            sshSocket = new VersionSpySocketSSH(sshSocket);
            major = ((VersionSpySocketSSH)sshSocket).getMajorVersion();
            if (hellotimeout >= 0)
                sshSocket.setSoTimeout(oldtimeout);
        } else if("ssh1".equals(proto)) {
            major = 1;
        }

        if(major == 1) {
            console.println("Warning connecting using ssh1, consider upgrading server!");
            console.println("");
            boot(shell, sshSocket);

            // Join main receiver channel thread and wait for session to end
            //
            controller.waitForExit();
        } else {
            runSSH2Client();
        }
    }

    public boolean isDumb() {
        return (console.getTerminal() == null);
    }

    public ConsoleWin getTerminalWin() {
        ConsoleWindows term = console.getTerminal();
        if(term != null && term instanceof ConsoleWin)
            return (ConsoleWin)term;
        return null;
    }

    public void updateTitle() {
        sshStdIO.updateTitle();
    }

    public String promptLine(String prompt, boolean echo)
        throws UserCancelException {
        try {
            String ret = null;
            while (ret == null) {
                if(echo) {
                    ret = promptLine(prompt, "");
                } else {
                    ret = promptPassword(prompt);
                }
            }
            return ret;
        } catch (IOException e) {
            throw new UserCancelException(e.getMessage());
        }
    }

    public String[] promptMulti(String[] prompts, boolean[] echos)
    throws UserCancelException {
        return promptMultiFull(null, null, prompts, echos);
    }

    public String[] promptMultiFull(String name, String instruction,
                                    String[] prompts, boolean[] echos)
    throws UserCancelException {
        try {
            if (name != null && name.length() > 0) {
                console.println(name);
            }
            if (instruction != null && instruction.length() > 0) {
                console.println(instruction);
            }
            String[] resp = new String[prompts.length];
            String tmp;
            for(int i = 0; i < prompts.length; i++) {
                tmp = null;
                while (tmp == null) {
                    if(echos[i]) {
                        tmp = promptLine(prompts[i], "");
                    } else {
//                        if(userAuth.getPassword()!=null && userAuth.getPassword().trim().length()>0){
//                        	tmp=userAuth.getPassword(); 
//                        } else {
//                        	tmp = promptPassword(prompts[i]);
//                        } 
                    	tmp = promptPassword(prompts[i]);
                    }
                }
                resp[i] = tmp;
            }
            return resp;
        } catch (IOException e) {
            throw new UserCancelException(e.getMessage());
        }
    }

    public int promptList(String name, String instruction, String[] choices)
    throws UserCancelException {
        try {
            console.println(name);
            console.println(instruction);
            for(int i = 0; i < choices.length; i++) {
                console.println(i + ") " + choices[i]);
            }
            String choice = null;
            while (choice == null) {
                choice = promptLine("Choice", "0");
            }
            return Integer.parseInt(choice);
        } catch (Exception e) {
            throw new UserCancelException(e.getMessage());
        }
    }

    public void startNewSession(ClientSSH client) {
        // Here we can have a login-dialog with proxy-info also (or
        // configurable more than one method)
    }

    public void sessionStarted(ClientSSH client) {
        quiet = initQuiet;
    }

    public boolean quietPrompts() {
        return (commandLine != null || quiet);
    }

    public boolean isVerbose() {
        return wantHelpInfo;
    }

    public String promptLine(String prompt, String defaultVal) throws IOException {
        return sshStdIO.promptLine(prompt, defaultVal, false);
    }

    public String promptPassword(String prompt) throws IOException {
        return sshStdIO.promptLine(prompt, "", true);
    }

    public boolean askConfirmation(String message, boolean defAnswer) {
        boolean confirm = false;
        try {
            confirm = askConfirmation(message, true, defAnswer);
        } catch (IOException e) {
            // !!!
        }
        return confirm;
    }

    public boolean askConfirmation(String message, boolean preferDialog,
                                   boolean defAnswer)
    throws IOException {
        boolean confirm = false;
        String answer = null;
        while (answer == null) {
            answer = promptLine(message + (defAnswer ? " ([yes]/no) " :
                                           "(yes/[no]) "), "");
        }
        if(answer.equalsIgnoreCase("yes") || answer.equals("y")) {
            confirm = true;
        } else if(answer.equals("")) {
            confirm = defAnswer;
        }
        return confirm;
    }

    public void connected(ClientSSH client) {
        console.println("Connected to server"); // running " + srvVersionStr);
    }

    public void open(ClientSSH client) {
        updateTitle();
    }

    public void disconnected(ClientSSH client, boolean graceful) {
        sshStdIO.breakPromptLine("Login aborted by user");
    }

    public void report(String msg) {
        if(msg != null && msg.length() > 0) {
            console.println(msg);
        }
        //console.println(""); 
    }

    public InteractorSSH2 getInteractor() {
        return this;
    }

    public void alert(String msg) {
        if (msg == null)
            msg = "Unknown error (null)";
        report(msg);
    }

    public void forcedDisconnect() {
        if(isSSH2) {
            transport.normalDisconnect("Closed by user");
        } else {
            super.forcedDisconnect();
        }
    }

    public void requestLocalPortForward(String localHost, int localPort,
                                        String remoteHost, int remotePort,
                                        String plugin)
    throws IOException {
        if(isSSH2) {
            StreamFilterFactory filter = null;
            if("sniff".equals(plugin)) {
                filter = StreamSnifferSSH2.getFilterFactory();
            }
            connection.newLocalForward(localHost, localPort,
                                       remoteHost, remotePort, filter);
        } else {
            super.requestLocalPortForward(localHost, localPort,
                                          remoteHost, remotePort, plugin);
        }
    }

    public void addRemotePortForward(String remoteHost, int remotePort,
                                     String localHost, int localPort,
                                     String plugin) { 
        super.addRemotePortForward(remoteHost, remotePort, localHost, localPort, plugin);
        if(isSSH2) {
            connection.newRemoteForward(remoteHost, remotePort,
                                        localHost, localPort);
        }
    }

    public void delLocalPortForward(String localHost, int port) {
        boolean isop = isOpened;
        if(isSSH2) {
            connection.deleteLocalForward(localHost, port);
            isOpened = false;
        }
        super.delLocalPortForward(localHost, port);
        isOpened = isop;
    }

    public void delRemotePortForward(String remoteHost, int port) {
        super.delRemotePortForward(remoteHost, port);
        if(isSSH2) {
            connection.deleteRemoteForward(remoteHost, port);
        }
    }

    void setAliveInterval(int i) {
        if(isSSH2) {
            transport.enableKeepAlive(i);
        } else {
            super.setAliveInterval(i);
        }
    }

    void runSSH2Client() throws IOException {
        try {
            PreferencesSSH2 prefs;
            
            isSSH2 = true;
            prefs  = new PreferencesSSH2(propsHandler.getProperties());
            isUnknownHost = false;
            
            if(SSH.DEBUGMORE) {
                prefs.setPreference(PreferencesSSH2.LOG_LEVEL, "7");
            }
            transport = new TransportSSH2(sshSocket, prefs, null, secureRandom()); 

            transport.setEventHandler(new TransportEventAdapterSSH2() {
                public boolean kexAuthenticateHost(TransportSSH2 tp,
                                                   BTSignature serverHostKey) {
                    try {
                        propsHandler.showFingerprint(serverHostKey.getPublicKeyBlob(), serverHostKey.getAlgorithmName());
                        if(fingerprintMatch(serverHostKey)) {
                            return true;
                        }
                        return propsHandler.verifyKnownSSH2Hosts(InteractiveClientSSH.this, serverHostKey);
                    } catch (ExceptionSSH2 e) {
                    	logger.error("Error " + e.getMessage());
                    } catch (IOException e) {
                    	logger.error("Error " + e.getMessage());
                    }
                    return false;
                }
                public void gotConnectInfoText(TransportSSH2 tp, String text) {
                    alert(text);
                }

            });

            transport.boot();

            srvVersionStr = transport.getServerVersion();
            connected(null);

            if(!transport.waitForKEXComplete()) {
                throw new IOException("Key exchange failed: " +
                                      transport.getDisconnectMessage());
            }

            isConnected = true;

            Authenticator authenticator =
                new Authenticator() {
                    public void peerMethods(String methods) {
                        addAuthModules(this, methods);
                    }
                    public void displayBanner(String banner) {
                        alert(banner);
                    }
                };
                
            String password = null;                 
            
            if( ( propsHandler.getSrvHost().equals("localhost") || propsHandler.getSrvHost().equals("127.0.0.1") ) 
            	&& TerminalProperty.getLocalhostUsername()!=null && TerminalProperty.getLocalhostPrivateKey()!=null ){
            		 authenticator.setUsername(TerminalProperty.getLocalhostUsername()); 
            		 propsHandler.setProperty("auth-method", "publickey,password,kbd-interact");
            		 propsHandler.setProperty("private-key", TerminalProperty.getLocalhostPrivateKey());
            } else {            
	            boolean isPublickey = false;
	            if( TerminalProperty.getTerminalPrivateKeyList()!=null && TerminalProperty.getTerminalPrivateKeyList().size()>0){
		            for(TerminalPrivateKey tpk : TerminalProperty.getTerminalPrivateKeyList() ){
				        String regex = tpk.getRegexp();  
					    Pattern pattern = Pattern.compile( regex );  
					    String host = propsHandler.getSrvHost(); 
					    logger.warn("HOSTNAME = "+host);
					    Matcher matcher = pattern.matcher( host );         
					    
					    if( host.matches( regex ) ) {
						     if( matcher.matches() ) {  
						    	 if( tpk.getUsername()!=null ){
						    	     authenticator.setUsername(tpk.getUsername());
						    	 }
						    	 propsHandler.setProperty("auth-method", "publickey,password,kbd-interact");
						    	 propsHandler.setProperty("private-key", tpk.getPrivateKey());
						    	 isPublickey=true;
						    	 break;
						     }
					    }       
		            } 
	            } 
	            
	            authenticator.setHostname(propsHandler.getSrvHost());
	            if( !isPublickey ){
	            	propsHandler.setProperty("auth-method", "password,kbd-interact");
	         	    final SshAuthenticationDialog dialog = new SshAuthenticationDialog(propsHandler.getSrvHost());

		       		Display.getDefault().syncExec(new Runnable(){
		   			public void run(){ 
		   				    dialog.enableMethod();
		   				    dialog.disableLDAPCheckBox();
			         	    dialog.openShell();
		   				}
		   			});
	        	    if ( dialog.getCanceled() ){ 
	        	    	transport.fatalDisconnect(SSH2.DISCONNECT_AUTH_CANCELLED_BY_USER, "Canceled");
	        	    	console.println("This connection was canceled");
	        			return;
	        	    }
	        		
	        	    String username = dialog.getUsername();
	        	    password = dialog.getPassword();
	        	   
	        	    authenticator.setUsername(username);;
	            }

            }
            
//			if( authenticator.getUsername()==null || authenticator.getUsername().trim().length()==0){
//			    authenticator.setUsername(propsHandler.getUsername(null));
//			}
            
            userAuth = new UserAuth(transport, authenticator);
            if( password!= null ){
            	userAuth.setPassword(password);
            }
            
            if(!userAuth.authenticateUser("ssh-connection")) {
       			throw new AuthFailException("permission denied");
            }
            
            connection = new ConnectionSSH2(userAuth, transport, null);
            connection.setEventHandler(new ConnectionEventAdapterSSH2() {
                public void localSessionConnect(ConnectionSSH2 connection,
                                                ChannelSSH2 channel) {
                }
                @SuppressWarnings("unchecked")
				public void localDirectConnect(ConnectionSSH2 connection,
                                               ListenerSSH2 listener,
                                               ChannelSSH2 channel) {
                    tunnels.addElement(channel);
                }
                @SuppressWarnings("unchecked")
				public void remoteForwardConnect(ConnectionSSH2 connection,
                                                 String remoteAddr, int remotePort,
                                                 ChannelSSH2 channel) {
                    tunnels.addElement(channel);
                }
                public void channelClosed(ConnectionSSH2 connection,
                                          ChannelSSH2 channel) {
                    tunnels.removeElement(channel);
                }
            });
            transport.setConnection(connection);
            authenticator.clearSensitiveData();

            if(console != null)
                console.serverConnect(null, null);
            isOpened = true;
            open(null);

            propsHandler.passivateProperties();
            propsHandler.activateProperties();

            ConsoleWin terminal = getTerminalWin();
            terminal.setHostname(propsHandler.getSrvHost());

            terminal.addInputListener(this);
            termAdapter = new TerminalAdapterImplSSH2(terminal);
            
            session = connection.newTerminal(termAdapter);
            // !!! OUCH must do this here since activateProperties is above
            if(propsHandler.hasKeyTimingNoise()) {
                termAdapter.startChaff();
            }
            if(session.openStatus() != ChannelSSH2.STATUS_OPEN) {
                throw new IOException("Failed to open ssh2 session channel");
            }

            if(user.wantX11Forward()) {
                session.requestX11Forward(false, 0);
            }
            if(user.wantPTY()) {
                session.requestPTY(terminal.terminalType(),terminal.rows(),terminal.cols(),null);
            }

            session.doShell();

//            SSHEventHandler eventHandler = EventPlugin.getSSHEventHandler(Event.TYPE_AFTER_START_TERMINAL_EVENT);
//            if(eventHandler!=null){
//               System.out.println("START AMEX PLUGIN");
//               eventHandler.execute(this);
//               System.out.println("FINISHED AMEX PLUGIN");
//            }
            
            if( TerminalProperty.getTerminalExecuteCommandList() != null && TerminalProperty.getTerminalExecuteCommandList().size()>0){
            	//String ssh_server = getPropertyHandler().getDefaultProperty("ssh_server");
            	while(true){
            		if( isConnected() ){
            			break;
            		}
                   	try {
        				Thread.sleep(300);
        			} catch (InterruptedException e) {
        				e.printStackTrace();
        			}          		
            	}
            	
        	    String resultado = null;
        	    getTerminalWin().getDisplay().createDynamicBuffer();
                    
                try {
    				Thread.sleep(300);
    			} catch (InterruptedException e) {
    				e.printStackTrace();
    			}
    			logger.debug("readDynamicBuffer : Start");
    	    	resultado = getTerminalWin().readDynamicBuffer("");  
    	    	logger.debug("readDynamicBuffer Result : "+resultado);

    	    	try {
    				Thread.sleep(500);
    			} catch (InterruptedException e) {
    				e.printStackTrace();
    			}

    	    	if ( (resultado!=null)&&(!resultado.contains("assword:")) ){
    	            for(TerminalExecuteCommand tec : TerminalProperty.getTerminalExecuteCommandList() ){
    			        String regex = tec.getRegexp();  
    				    Pattern pattern = Pattern.compile( regex );  
    				    String host = propsHandler.getSrvHost(); 
    				    logger.warn("HOSTNAME = "+host);
    				    Matcher matcher = pattern.matcher( host );         
    				    
    				    if( host.matches( regex ) ) {
    					     if( matcher.matches() ) {  
    					    	 getTerminalWin().executeCommand(tec.getExecuteCommand()+"\n");
    					     }
    				    }       
    	            } 	                	
    	    	}
            }
            
            getTerminalWin().requestFocus();

            int status = session.waitForExit(0);

            if(terminal != null) {
                terminal.removeInputListener(this);
            }
            termAdapter.detach();

            transport.normalDisconnect("Disconnect by user");

            console.serverDisconnect(propsHandler.getSrvHost() + " disconnected: " + status);
            disconnect(true);

            sshStdIO.setTerminal(terminal);
        } catch ( CancellationException ecancel) {
            disconnect(false);
            throw new IOException("Canceled by user");        	
        } catch (IOException e) {
            disconnect(false);
            throw e;
        } catch (Exception e) {
            System.err.println("** Error in ssh2: ");
            e.printStackTrace();
            disconnect(false);
            throw new IOException("Error in ssh2: " + e.getMessage() + "{" + e + "}");
        } finally {
        
            //((SSHMenuHandlerFull)menus).modulesDisconnect();
            connection = null;
            transport = null;
            isSSH2 = false;
        }
    }

    public boolean fingerprintMatch(BTSignature serverHostKey) {
        String fp = propsHandler.getProperty("fingerprint");
        if(fp == null) {
            fp = propsHandler.getProperty("fingerprint." +
                                          propsHandler.getProperty("server") +
                                          "." +
                                          propsHandler.getProperty("port"));
        }
        if(fp != null) {
            if(HostKeyVerifierSSH2.compareFingerprints(fp, serverHostKey)) {
                return true;
            }
            if(propsHandler.askChangeKeyConfirmation()) {
                byte[] blob = null;
                try {
                    blob = serverHostKey.getPublicKeyBlob();
                } catch (BTSignatureException e) {
                    return false;
                }
                String fpMD5Hex = KeyFingerprint.md5Hex(blob);
                propsHandler.setProperty("fingerprint", fpMD5Hex);
            }
        }
        return false;
    }

    public void typedChar(char c) {}
    public void typedChar(byte[] b) {}

    public void sendBytes(byte[] b) {}
    public void sendBreak() {}

    public void signalWindowChanged(int rows, int cols,
                                    int vpixels, int hpixels) {
        updateTitle();
    }
    public void signalTermTypeChanged(String newTermType) {}

    public void addAuthModules(Authenticator authenticator, String methods) {
        try {
            int[] authTypes = propsHandler.getAuthTypes(null);
            boolean allUnsupported = true;
            for(int i = 0; i < authTypes.length; i++) {
                int type = authTypes[i];
                if(!ListUtilSSH2.isInList(methods, SSH.getAuthName(type)) &&
                   !ListUtilSSH2.isInList(methods, SSH.getAltAuthName(type)) &&
                   !((type == AUTH_SDI) &&
                     ListUtilSSH2.isInList(methods, "securid-1@ssh.com"))) {
                    continue;
                } 
                allUnsupported = false;
                switch(type) {
                case AUTH_PUBLICKEY: {        
                    String keyFile = propsHandler.getProperty("idfile");
                    if(keyFile.indexOf(File.separator) == -1) {
                        //keyFile = propsHandler.getSSHHomeDir() + keyFile;
                    	String home = System.getProperty("user.home");
                    	
                    	keyFile = home+"/.ssh/id_rsa";
                    }

                    KeyPairFile kpf = new KeyPairFile();
                    try {
                        kpf.load(keyFile, "");
                    } catch (FatalExceptionSSH2 e) {
                        throw new IOException(e.getMessage());
                    } catch (AccessDeniedException e) {
                        String comment = kpf.getComment();
                        if(comment == null || comment.trim().length() == 0) {
                            comment = keyFile;
                        }
                        String prompt = "Key '" + comment + "' password: ";
                        String passwd = propsHandler.getIdentityPassword(prompt);
                        kpf.load(keyFile, passwd);
                    }

                    String        alg  = kpf.getAlgorithmName();
                    BTSignature sign = BTSignature.getInstance(alg);

                    sign.initSign(kpf.getKeyPair().getPrivate());
                    sign.setPublicKey(kpf.getKeyPair().getPublic());

                    authenticator.addModule(new AuthPublicKey(sign));

                    break;
                }                    
                case AUTH_PASSWORD:
                    if (isFirstPasswdAuth) {
                        isFirstPasswdAuth = false;
                    } else {
                        propsHandler.eraseProperty("password");
                    }
                    //String p = getProperty("username") + "@" + getProperty("server") + "'s password: ";
                    String p = "password: ";
                    authenticator.addModule(new AuthPassword(this, p));
                    break;
                case AUTH_HOSTBASED: {
                    String keyFile = propsHandler.getProperty("private-host-key");
                    if(keyFile.indexOf(File.separator) == -1) {
                        keyFile = propsHandler.getSSHHomeDir() + keyFile;
                    }

                    KeyPairFile kpf = new KeyPairFile();
                    try {
                        kpf.load(keyFile, "");
                    } catch (FatalExceptionSSH2 e) {
                        throw new IOException(e.getMessage());
                    } catch (AccessDeniedException e) {
                        String comment = kpf.getComment();
                        if(comment == null || comment.trim().length() == 0) {
                            comment = keyFile;
                        }
                        String prompt = "Key '" + comment + "' password: ";
                        String passwd = propsHandler.getIdentityPassword(prompt);
                        kpf.load(keyFile, passwd);
                    }

                    String        alg  = kpf.getAlgorithmName();
                    BTSignature sign = BTSignature.getInstance(alg);

                    sign.initSign(kpf.getKeyPair().getPrivate());
                    sign.setPublicKey(kpf.getKeyPair().getPublic());

                    authenticator.addModule(new AuthHostBased(sign));
                    break;
                }                    
                case AUTH_SDI:
                case AUTH_TIS:
                case AUTH_CRYPTOCARD:
                case AUTH_KBDINTERACT:
                    authenticator.addModule(new AuthKbdInteract(this));
                    authenticator.addModule(
                        new AuthSSHComSecurID(
                            this,
                            "Enter Passcode: ",
                            "Wait for token to change and enter Passcode: ",
                            "New PIN:",
                            "Confirm new PIN: ",
                            "Do you want to create your own new PIN (yes/no)? ",
                            "Accept the server assigned PIN: "));
                    break;
                default:
                    throw new IOException("Authentication type " +
                                          authTypeDesc[authTypes[i]] +
                                          " is not supported in SSH2");
                }
            }
            if (allUnsupported) {
                for(int i = 0; i < authTypes.length; i++) {
                    report("Authentication method '" +
                           SSH.getAuthName(authTypes[i]) +
                           "' not supported by server.");
                }
            }
        } catch (Exception e) {
            if(SSH.DEBUGMORE) {
                System.out.println("Error when setting up authentication: ");
                int[] t = propsHandler.getAuthTypes(null);
                for(int i = 0; i < t.length; i++) {
                    System.out.print(t[i] + ", ");
                }
                System.out.println("");
                e.printStackTrace();
            }
            alert("Error when setting up authentication: " + e.getMessage());
        }
    }

    public String getVersionId(boolean client) {
        String idStr = "SSH-" + SSH_VER_MAJOR + "." + SSH_VER_MINOR + "-";
        idStr += propsHandler.getProperty("package-version");
        return idStr;
    }

    public void closeTunnelFromList(int listIdx) {
        if(isSSH2) {
            ChannelSSH2 c = (ChannelSSH2)tunnels.elementAt(listIdx);
            c.close();
        } else {
            controller.closeTunnelFromList(listIdx);
        }
    }

    private Vector tunnels = new Vector();
    public String[] listTunnels() {
        if(isSSH2) {
            String[] list   = new String[tunnels.size()];
            Enumeration e   = tunnels.elements();
            int         cnt = 0;
            while(e.hasMoreElements()) {
                TCPChannel c = (TCPChannel)e.nextElement();
                list[cnt++] = c.toString();
            }
            return list;
        } else {
            return controller.listTunnels();
        }
    }

    public String getHost() {
        //return getServerAddr().getHostName();
    	try {
			return propsHandler.getSrvHost();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
    	return " ";
    }

    public int getPort() {
        return propsHandler.getSrvPort();
    }

    public Properties getProperties() {
        Properties  props     = new Properties(propsHandler.getProperties());
        ConsoleWin term      = getTerminalWin();
        Properties  termProps = (term != null ? term.getProperties() : null);
        if(termProps != null) {
            Enumeration e = termProps.keys();
            while(e.hasMoreElements()) {
                String key = (String)e.nextElement();
                String val = termProps.getProperty(key);
                props.put(key, val);
            }
        }
        return props;
    }

    public String getProperty(String name) {
        String value = propsHandler.getProperty(name);
        if(value == null) {
            ConsoleWin term = getTerminalWin();
            if(term != null) {
                value = term.getProperty(name);
            }
        }
        return value;
    }

    public void setProperty(String name, String value) {
        propsHandler.setProperty(name, value);
    }

    public String getUserName() {
        return propsHandler.getProperty("username");
    }

    public String getAppName() {
        return "IESTTerm";
    }

    public RandomSeed getRandomSeed() {
        return randomSeed();
    }

    public TransportSSH2 getTransport() {
        return transport;
    }

    public ConnectionSSH2 getConnection() {
        return connection;
    }

//    public ConsoleRemoteInterface getConsoleRemote() {
//        ConsoleRemoteInterface remote = null;
//        if(isSSH2) {
//            remote = new ConsoleRemoteSSH2(getConnection());
//        } else {
//            quiet = true;
//            try {
//                remote  = new ConsoleClientSSH(propsHandler.getSrvHost(),
//                                               propsHandler.getSrvPort(),
//                                               propsHandler, null);
//                ((ConsoleClientSSH)remote).setClientUser(propsHandler);
//            } catch (IOException e) {
//                alert("Error creating remote console: " + e.getMessage());
//            }
//        }
//        return remote;
//    }


    public boolean isAuthenticated(){
    	if(session==null)
    		return false;
    	else
    	    return session.openStatus() == ChannelSSH2.STATUS_OPEN;
    }
    
    public TerminalAdapterImplSSH2 getTermAdapter(){
      return (TerminalAdapterImplSSH2)termAdapter;	
    }
    
	public StdIOSSH getSSHStdIO(){
		return sshStdIO;
	}    
	
	public boolean isUnknownHost() {
		return isUnknownHost; 
	}

	/**
	 * @return the userAuth
	 */
	public UserAuth getSSH2UserAuth() {
		return userAuth;
	}

	public boolean licenseDialog(String license) {
		// TODO Auto-generated method stub
		return false;
	}

	public void propsStateChanged(PropertyHandlerSSH props) {
		// TODO Auto-generated method stub
		
	}	
}
