package com.mxterminal.console;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Frame;
import java.util.Hashtable;
import java.util.Properties;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.awt.SWT_AWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Device;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;

import com.mxssh.ManageGateway;
import com.mxterminal.ssh.InteractiveClientSSH;
import com.mxterminal.ssh.PropertyHandlerSSH;
import com.mxterminal.ssh.SSH;
import com.mxterminal.ssh.StdIOSSH;
import com.mxterminal.swt.util.TerminalProperty;

public class ConsoleComposite extends Composite implements Runnable {  
	static Logger logger = Logger.getLogger(ConsoleComposite.class);
	
    private Frame        frame;
    ConsoleWin          term = null;
    InteractiveClientSSH client;
    InteractiveClientSSH sshClone;
    StdIOSSH             console;
    Thread               clientThread;
    Properties 	         sshProps;
    Properties 			 termProps;
    
    String  hostname 	 = null;  //  @jve:decl-index=0:

    boolean haveGUI      = true;
    boolean exitOnLogout = true;
    boolean quiet        = true;
    boolean useSwing     = false;

    boolean autoSaveProps = true;
    boolean autoLoadProps = true;
    boolean savePasswords = false;
    boolean isClosing     = false;

    boolean        separateFrame = true;
    boolean        useAWT = false;

    Hashtable terminals = new Hashtable();    //  @jve:decl-index=0:
    Properties paramTerm = new Properties();  //  @jve:decl-index=0:
    Properties paramSSH  = new Properties();  //  @jve:decl-index=0:
    boolean automaticLogin = true;
    
    
	public ConsoleComposite(Composite parent, int style) {
		super(parent, style);
		//this.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BORDER));
        this.sshProps  = paramSSH;
        this.termProps = paramTerm;
        
        addTerminal(this);
        
        termProps.put("passthru-prn-enable", "true");
        termProps.put("copy-crnl", "false");
        
        termProps.put("resizable", "true");
        termProps.put("rev-video", "false");
        termProps.put("autowrap", "true");
        termProps.put("rev-autowrap", "true");
        termProps.put("insert-mode", "false");
        termProps.put("repos-input", "true");
        termProps.put("repos-output", "true");
        termProps.put("visible-cursor", "true");
        termProps.put("local-echo", "false"); 
        
    	termProps.put("map-ctrl-space", "false");
    	termProps.put("local-pgkeys", "true"); 
    	termProps.put("ascii-line", "false");

    	termProps.put("term-type", "xterm"); 
    	
    	if( TerminalProperty.getCopySelect()==null ){
    		termProps.put("copy-select", "true");
       	} else {
       		termProps.put("copy-select", TerminalProperty.getCopySelect());	
       	}
    	
    	if( TerminalProperty.getVisualBell()==null ){
    		termProps.put("visual-bell", "true");
       	} else {
       		termProps.put("visual-bell", TerminalProperty.getVisualBell());	
       	}
    	
    	if( TerminalProperty.getBackspaceSend()==null ){
    		termProps.put("backspace-send", "bs");
       	} else {
       		termProps.put("backspace-send", TerminalProperty.getBackspaceSend());	
       	}
    	
    	if( TerminalProperty.getDeleteSend()==null ){
    		termProps.put("delete-send", "del");
       	} else {
       		termProps.put("delete-send", TerminalProperty.getDeleteSend());	
       	}
    	
    	if( TerminalProperty.getEncoding()==null ){
    	  termProps.put("encoding", "utf-8"); 	
      	} else {
      	  termProps.put("encoding", TerminalProperty.getEncoding()); 	
      	}
    	
    	if( TerminalProperty.getFontName()==null ){
    	  termProps.put("font-name", "Monospaced"); 	
    	} else {
    	  termProps.put("font-name", TerminalProperty.getFontName()); 	
    	}
    	
    	if( TerminalProperty.getFontSize()==null ){
      	  termProps.put("font-size", "13"); 	
      	} else {
      	  termProps.put("font-size", TerminalProperty.getFontSize()); 	
      	}    
    	
    	if( TerminalProperty.getSavelines()==null ){
        	termProps.put("save-lines", "3000"); 	
        } else {
        	termProps.put("save-lines", TerminalProperty.getSavelines()); 	
        }     	
    	
    	termProps.put("scrollbar", "right");
    	
    	if( TerminalProperty.getPasteButton()==null ){
    		termProps.put("paste-button", "right");
       	} else {
       		termProps.put("paste-button", TerminalProperty.getPasteButton());
       	}
    	
    	if( TerminalProperty.getFgColor()==null ){
    		termProps.put("fg-color", "187,187,187");
    		Color fgColor = getRGBColor("187,187,187");
    		setForeground(fgColor);
       	} else {
       		termProps.put("fg-color", TerminalProperty.getFgColor());
       		Color fgColor = getRGBColor( TerminalProperty.getFgColor() );
       		setForeground(fgColor);
       	}
    	
    	if( TerminalProperty.getBgColor()==null ){
    		termProps.put("bg-color", "0,0,0");
       		Color bgColor = getRGBColor( "0,0,0" );
       		setBackground(bgColor);    		
       	} else {
       		termProps.put("bg-color", TerminalProperty.getBgColor());
       		Color bgColor = getRGBColor(  TerminalProperty.getBgColor() );
       		setBackground(bgColor);          		
       	}   
    	
    	if( TerminalProperty.getCursorColor()==null ){
    		termProps.put("cursor-color", "0,255,0");
       	} else {
       		termProps.put("cursor-color", TerminalProperty.getCursorColor());
       	}     	
    	//initProp.put("geometry", newCols+"x"+newRows); 
    	
    	termProps.put("ignore-null", "true"); 
    	 
    	//initProp.put("fg-color", "white");
    	
    	termProps.put("auth-method", "kbd-interact,publickey,password");
    	
    	init();
	}

    public static Color getRGBColor(String value)
    throws NumberFormatException {
        int r, g, b, c1, c2;
        Color c;
        Device device = Display.getCurrent ();
        
        if( value.contains("black")){
        	return Display.getCurrent().getSystemColor(SWT.COLOR_BLACK);
        } else if ( value.contains("red")){
        	return Display.getCurrent().getSystemColor(SWT.COLOR_RED);
        } else if ( value.contains("green")){
        	return Display.getCurrent().getSystemColor(SWT.COLOR_GREEN);
        } else if ( value.contains("yellow")){
        	return Display.getCurrent().getSystemColor(SWT.COLOR_YELLOW);
        } else if ( value.contains("blue")){
        	return Display.getCurrent().getSystemColor(SWT.COLOR_BLUE);        	
        } else if ( value.contains("magenta")){
        	return Display.getCurrent().getSystemColor(SWT.COLOR_MAGENTA);        	
        } else if ( value.contains("cyan")){
        	return Display.getCurrent().getSystemColor(SWT.COLOR_CYAN);        	
        } else if ( value.contains("white")){
        	return Display.getCurrent().getSystemColor(SWT.COLOR_WHITE);        	
        }
        
        c1 = value.indexOf(',');
        c2 = value.lastIndexOf(',');
        if(c1 == -1 || c2 == -1)
            throw new NumberFormatException();
        r = Integer.parseInt(value.substring(0, c1).trim());
        g = Integer.parseInt(value.substring(c1 + 1, c2).trim());
        b = Integer.parseInt(value.substring(c2 + 1).trim());
        c = new Color(device,r, g, b);
        return c;
    }
    
	public boolean forceFocus()	{
		
		boolean focus = super.forceFocus();
		//System.out.println("Terminal Composite forceFocus "+focus);
		if(isConnected()){
		   getTerminalWin().requestFocus();
		}
		return focus;
	}
	
    synchronized boolean isLastTerminal() {
        return terminals.isEmpty();
    }

	synchronized void addTerminal(ConsoleComposite terminalComposite) {
        terminals.put(terminalComposite, terminalComposite);
    }

    synchronized void removeTerminal(InteractiveClientSSH term) {
        terminals.remove(term);
    }

    public void init() {
        autoSaveProps  = false;
        autoLoadProps  = false;
        savePasswords  = false;
        
		if( System.getProperty("os.name").equals("Linux")){
			frame =  SWT_AWT.new_Frame(this);
			   String regex = "\\d?\\d?\\d[,]\\d?\\d?\\d[,]\\d?\\d?\\d";
			   String colorStr = TerminalProperty.getBgColor();
			   Pattern pattern = Pattern.compile( regex );  
			   Matcher matcher = pattern.matcher( colorStr );         
			    
			   if( colorStr.matches( regex ) ) {
			     if( matcher.matches() ) {    
			    	 StringTokenizer token = new StringTokenizer(colorStr,",");
			    	 frame.setBackground(new java.awt.Color( Integer.valueOf(token.nextToken()),
			    			                                 Integer.valueOf(token.nextToken()),
			    			                                 Integer.valueOf(token.nextToken()) ) );
			     }
			   }			
		}
    }

    public void run() {
        //try {
            PropertyHandlerSSH propsHandler =  new PropertyHandlerSSH(termProps, true);

            if ( getSshClone() != null ){
            	client = new InteractiveClientSSH(getSshClone(),true);
            } else {
                client = new InteractiveClientSSH(quiet, exitOnLogout, propsHandler,automaticLogin);
            }
            
            if(TerminalProperty.getTunnelSSH()!=null && !ManageGateway.isStarted ){
            	propsHandler.setDefaultProperty("server", TerminalProperty.getTunnelSSH());
            	propsHandler.setDefaultProperty("ssh_server", getHostName());
            } else {
            	propsHandler.setDefaultProperty("server", getHostName());
            }
            
            console = (StdIOSSH)client.getConsole();

            if(haveGUI) {
                initGUI();
                console.setTerminal(term);
                console.setOwnerContainer(frame);
                console.setOwnerName(SSH.VER_IESTTERM);
                console.updateTitle();
                
                try {
                    while(!frame.isShowing()){
                    	logger.debug("getDynamicBuffer().getInputStream().read();");
                        Thread.sleep(50);
                    }
                } catch(InterruptedException e) {
                    logger.error(e.getMessage());
                } 

                if(!separateFrame) {
                    term.emulateComponentShown();
                }
            }

            propsHandler.setAutoSaveProps(autoSaveProps);
            propsHandler.setAutoLoadProps(autoLoadProps);
            propsHandler.setSavePasswords(savePasswords);
            
//		    Display.getDefault().syncExec(new Runnable(){	    	    	
//			   public void run(){ 
	                clientThread = new Thread(client, "Thread [ "+getHostName()+" ]");
	                logger.debug("clientThread.start()");
	                clientThread.start();
//			   }
//		    });             

        //} catch (IllegalArgumentException ae) {
//            if(client != null)
//                client.alert(ae.getMessage());
//            logger.debug("Error: " + ae.getMessage());
//        } catch (Exception e) {
//            if(client != null){
//                client.alert("Error: "  + e.getMessage());
//                logger.debug("Error: " + e.getMessage());
//                e.printStackTrace();
//            }
//        }

        // Do not ask for confirmation in this case
        confirmedClose = true;

        //windowClosing(null);
        //if(isLastTerminal())
        //    doExit();
    }


    public void initGUI() {
        Container container;
        if( System.getProperty("os.name").equals("Linux")){
        	container = frame;
        } else  if (separateFrame) {
            if (!useAWT) {
                //frame = AWTConvenience.tryToAllocateJFrame();
                frame =  SWT_AWT.new_Frame(this); 
                frame.setBackground(new java.awt.Color(230,230,250));
            }
            if (frame == null) { // fallback to use AWT
                useSwing = false;
                frame = new Frame();
            } else {
                useSwing = true;
            }
            
            //frame.addWindowListener(this);
            container = frame;
        } else {
        	container = frame;
        }

        term = new ConsoleWin(frame, termProps, (sshClone == null));
		frame.setLayout(new BorderLayout());
		frame.add(term.getPanelWithScrollbarSWT(this.getParent()), BorderLayout.CENTER);
		
		term.setClipboard(ConsoleClipboard.getClipboardHandler());
        
        if (separateFrame) {
            term.setIgnoreClose();
        }

        term.addAsEntropyGenerator(client.randomSeed());
        
        getContentPane(container).setLayout(new BorderLayout());
        getContentPane(container).add(term.getPanelWithScrollbar(), BorderLayout.CENTER);

        frame.pack();
        frame.setVisible(true);
        //MainShell.sashFormComposite.getWorkingSetComposite().setFocusTabFolder();

        term.requestFocus();
    }

    public Container getContentPane(Container cont) {
        if (! (cont instanceof Frame)) 
            return cont;

        Frame f = (Frame)cont;
//        if (isSwingJFrame(f)) {
//            try {
//                Class c = Class.forName("javax.swing.JFrame");
//                Method m = c.getMethod("getContentPane", new Class[] {} );
//                return (Container)m.invoke(f, (Object[])null);
//            } catch (Throwable tt) {
//            }
//        }
        return f;
    }    
    
//    public boolean isSwingJFrame(Frame f) {
//        try {
//            Method m = f.getClass().getMethod("getJMenuBar", (Class[])null);
//            return m != null;
//        } catch (Throwable t) {
//        }
//        return false;
//    }    
    
//    public void doExit() {
//        System.out.println("Thank you for using Blue Terminal...");
//        if(!separateFrame && term != null) {
//            term.clearScreen();
//            term.setAttributeBold(true);
//            term.write("Thank you for using Blue Terminal...");
//        }
//    }

    boolean confirmedClose = false;

//    public void destroy() {
//        Enumeration e = terminals.elements();
//        while(e.hasMoreElements()) { 
//        	InteractiveClientSSH client = (InteractiveClientSSH)e.nextElement();
//            if(client != null && client.isSSH2) {
//                client.transport.normalDisconnect("User exited");
//            }
//       }
//    }


	public String getHostName() { 
		return hostname;
	}


	public void setHostName(String hostname) {
		this.hostname = hostname;
	}


	public ConsoleWin getTerminalWin() { 
		return term;
	}
	
	public boolean isConnected(){
		if(client==null)
			return false;
		return client.isConnected();
	}
	 
	public boolean isAuthenticated(){
		if(client==null)
			return false;
		return client.isAuthenticated();
	}	
	
    public InteractiveClientSSH getSSHInteractiveClient(){ 
        return client; 
    }

	/**
	 * @return the frame
	 */
	public Frame getFrame() {
		return frame;
	}

	/**
	 * @return the sshClone
	 */
	public InteractiveClientSSH getSshClone() {
		return sshClone;
	}

	/**
	 * @param sshClone the sshClone to set
	 */
	public void setSshClone(InteractiveClientSSH sshClone) {
		this.sshClone = sshClone;
	}

	/**
	 * @return the sshProps
	 */
	public Properties getSshProps() {
		return sshProps;
	}

	/**
	 * @return the automaticLogin
	 */
	public boolean isAutomaticLogin() {
		return automaticLogin;
	}

	/**
	 * @param automaticLogin the automaticLogin to set
	 */
	public void setAutomaticLogin(boolean automaticLogin) {
		this.automaticLogin = automaticLogin;
	}
    
    

}  //  @jve:decl-index=0:visual-constraint="10,10"
