package com.mxterminal.ssh2.auth;


import java.util.StringTokenizer;
import java.util.concurrent.CancellationException;

import org.apache.log4j.Logger;
import org.eclipse.swt.widgets.Display;

import com.mxdeploy.swt.dialogs.authentication.SshAuthenticationDialog;
import com.mxterminal.ssh2.ListUtilSSH2;
import com.mxterminal.ssh2.SSH2;
import com.mxterminal.ssh2.TransportPDUSSH2;
import com.mxterminal.ssh2.TransportSSH2;
import com.mxterminal.ssh2.exception.ExceptionSSH2;
import com.mxterminal.ssh2.exception.UserCancelException;
import com.mxterminal.util.Trigger;


public final class UserAuth {
	static Logger logger = Logger.getLogger(UserAuth.class);
	
    private TransportSSH2     transport;
    private Authenticator authenticator;
    private AuthModule    authModule;
    private volatile boolean isAuthenticated;
    private String password = null;
    
    String  currentMethod;
    boolean retryPointless;

    String service;
    public String user;
    String ourMethods;

    Trigger procQueue;
    
    boolean isFirstTry = true;

    /**
     * This is the constructor. It uses the transport layer. It takes a
     * <code>SSH2Authenticator</code> which contains the user to authenticate
     * and provides a list of the authentication methods to try. It is also used
     * to report authentication events.
     *
     * @param transport     the transport layer
     * @param authenticator the authenticator containing authentication info for
     * the user it represents.
     */
    public UserAuth(TransportSSH2 transport,
                        Authenticator authenticator) {
        this.transport       = transport;
        this.authenticator   = authenticator;
        this.isAuthenticated = false;
        this.procQueue       = new Trigger();
    }

    /**
     * Gets our transport layer.
     *
     * @return the transport layer
     */
    public TransportSSH2 getTransport() {
        return transport;
    }

    /**
     * Gets our authenticator.
     *
     * @return the authenticator in use
     */
    public Authenticator getAuthenticator() {
        return authenticator;
    }

    /**
     * Authenticates the user represented by the authenticator to run the given
     * service (currently "ssh-connection" is the only defined service). The
     * authentication process is run in the callers thread hence the call blocks
     * until either the user is authenticated or the authentication fails.
     *
     * @param service     the service to request
     *
     * @return a boolean indicating whether authentication succeeded or not
     */
    public boolean authenticateUser(String service) throws CancellationException {
        return authenticateUser(service, 0);
    }

    public boolean authenticateUser(String service, long timeout) throws CancellationException {
        this.service       = service;
        this.user          = authenticator.getUsername();
        this.currentMethod = null;
        this.retryPointless = false;

        transport.setUserAuth(this);
        transport.requestService("ssh-userauth");

        String  peerMethods = null;
        boolean retry       = false;
        boolean partial     = false;
        int     lastType    = -1;
        boolean exitIfFails = false;

authLoop:
        while(!isAuthenticated) {
            if (!transport.isConnected())
                break;

            TransportPDUSSH2 pdu = null;

            if(!retry) {
                pdu = (TransportPDUSSH2)procQueue.getFirst(timeout);
                if(pdu == null) {
                    authenticator.authError();
                    break authLoop;
                }
                lastType = pdu.getType();
            }

            try {
                switch(lastType) {
                case SSH2.MSG_USERAUTH_FAILURE:
                	
                    if(!retry) {
                        peerMethods = pdu.readJavaString();
                        partial     = pdu.readBoolean();
                        authenticator.peerMethods(peerMethods);
                    }
                    retry = false; 

                    logger.info( "failure continuation: " + peerMethods + " (partial: " + partial + ")");

                    if(currentMethod != null && retryPointless) {
                        ourMethods =  ListUtilSSH2.removeFirstFromList(ourMethods, currentMethod);
                        authenticator.authFailure(currentMethod, partial);
                    } else {
                        ourMethods = authenticator.getMethods();
                    }

                    logger.info("our remaining methods: " +(ourMethods.length() > 0 ? ourMethods : "<none>"));

                    currentMethod = ListUtilSSH2.chooseFromList(ourMethods, peerMethods);

                    if(currentMethod != null && !exitIfFails) {
                    	logger.info("trying method: " + currentMethod);
                        authModule = authenticator.getModule(currentMethod);
                        retryPointless = authModule.retryPointless();
                        
                        if(authModule instanceof AuthPassword ){ 
                        	if( isFirstTry ){
                        		isFirstTry = false;
                        	} else {
	        	         	    final SshAuthenticationDialog dialog = new SshAuthenticationDialog(authenticator.getHostname());
	        		       		Display.getDefault().syncExec(new Runnable(){
	        		   			public void run(){ 
	    			         	    dialog.disableLDAPCheckBox();
	    			         	    dialog.disableUsername();
	    			         	    dialog.getComposite().getComboBox().removeAll();   
	    			         	    dialog.disableMethod();
	    			         	   dialog.getComposite().getUsernameText().setText(user);
	        			        	dialog.openShell();		   				
	        		   				}
	        		   			});
	        	        	    if ( dialog.getCanceled() ){
	        	        	    	throw new CancellationException();
	        	        	    }
	        	        		
	        	        	    password = dialog.getPassword(); 
	        	        	    exitIfFails=true;
                        	}
                        }
                        
                        TransportPDUSSH2 modPDU = authModule.startAuthentication(this);
                        transport.transmit(modPDU);
                        
                    } else {
                        authenticator.noMoreMethods();
                        logger.info("no more authentication methods, giving up");
                        break authLoop;
                    }
                    break;

                case SSH2.MSG_USERAUTH_SUCCESS:
                	logger.info(
                                              "successful authentication with " +
                                              currentMethod);
                    isAuthenticated = true;
                    authenticator.authSuccess(currentMethod);
                    break;

                case SSH2.MSG_USERAUTH_BANNER:
                    String msg = pdu.readJavaString();
                    logger.info("banner: " +  msg); 
                    
                    authenticator.displayBanner("");
                    
                    StringTokenizer token = new StringTokenizer(msg,"\n");
                    while(token.hasMoreTokens()){
                    	String msgToken = token.nextToken();
                    	authenticator.displayBanner(msgToken);
                    }
                    
                    break;

                case SSH2.MSG_SERVICE_ACCEPT:
                    try {
                        String s = pdu.readJavaString();
                        logger.info(
                                                "server accepted: " + s);
                    } catch (Error e) {
                    	logger.info(
                                                "server accepted " +
                                                service +
                                                " (draft incompatible)");
                    }
                    doNoneAuth();
                    break;

                case SSH2.FIRST_USERAUTH_METHOD_PACKET:
                case 61:
                case 62:
                case 63:
                case 64:
                case 65:
                case 66:
                case 67:
                case 68:
                case 69:
                case 70:
                case 71:
                case 72:
                case 73:
                case 74:
                case 75:
                case 76:
                case 77:
                case 78:
                case SSH2.LAST_USERAUTH_METHOD_PACKET:
                    if(authModule != null) {
                        TransportPDUSSH2 modPDU = authModule.processMethodMessage(this, pdu);
                        if(modPDU != null)
                            transport.transmit(modPDU);
                    } else {
                        transport.fatalDisconnect(SSH2.DISCONNECT_PROTOCOL_ERROR,
                                                  "Received userauth method " +
                                                  "packet when no method selected");
                        break authLoop;
                    }
                    break;
                }
            } catch (UserCancelException e) {
                String msg = e.getMessage();
                if (msg == null) {
                    msg = "User cancel";
                }
                authenticator.moduleCancel(currentMethod, "This connection was canceled");
                transport.fatalDisconnect(SSH2.DISCONNECT_AUTH_CANCELLED_BY_USER, "Canceled");
                logger.info("user canceled authentication: "+ msg);
                break authLoop;
            } catch (ExceptionSSH2 e) {
            	logger.error(
                                         
                                         "error in module '" +
                                         currentMethod + "': " +
                                         e.getMessage());
                authenticator.moduleFailure(currentMethod, e);
                partial  = false;
                retry    = true;
                lastType = SSH2.MSG_USERAUTH_FAILURE;
            }
            if(pdu != null) {
                pdu.release();
            }
        }

        return isAuthenticated;
    }

    /**
     * Creates a packet of type USERAUTH_REQUEST (as defined in the userauth
     * protocol spec.). This is a convenience method which creates the whole
     * packet given the method name (i.e. fills in username and service). It is
     * typically used by <code>SSH2AuthModule</code> implementors to create the
     * packet to return from the method <code>startAuthentication</code>.
     *
     * @param method the name of the authentication method
     *
     * @return the complete USERAUTH_REQUEST packet
     */
    public TransportPDUSSH2 createUserAuthRequest(String method) {
        TransportPDUSSH2 pdu =
            TransportPDUSSH2.createOutgoingPacket(SSH2.MSG_USERAUTH_REQUEST);
        pdu.writeString(user);
        pdu.writeString(service);
        pdu.writeString(method);
        return pdu;
    }

    /**
     * Terminates the authentication process.
     */
    public void terminate() {
        procQueue.setBlocking(false);
    }

    /**
     * Checks if the user represented by the <code>SSH2Authenticator</code> we
     * process has been authenticated yet.
     *
     * @return a boolean indicating if the user is authenticated or not
     */
    public boolean isAuthenticated() {
        return isAuthenticated;
    }

    private void doNoneAuth() {
        TransportPDUSSH2 pdu = createUserAuthRequest("none");
        transport.transmit(pdu);
    }

    public void processMessage(TransportPDUSSH2 pdu) {
        procQueue.putLast(pdu);
    }

	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @param password the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}
	
	public String getUsername(){
		return this.authenticator.getUsername();		
	}
	
}

