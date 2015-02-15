package com.mxterminal.ssh2.auth;

import org.apache.log4j.Logger;

import com.mxterminal.ssh2.InteractorSSH2;
import com.mxterminal.ssh2.SSH2;
import com.mxterminal.ssh2.TransportPDUSSH2;
import com.mxterminal.ssh2.exception.UserCancelException;

/**
 * This class implements a module for keyboard-interactive authentication as
 * defined in the auth-kbdinteract protocol spec. It uses the interface
 * <code>SSH2Interactor</code> for all interactions generated in the
 * authentication process. The keyboard-interactive method is suitable for any
 * authentication mechanism where the user enters authentication data via the
 * keyboard (e.g. SecurID and CryptoCard). No specifics about the
 * authentication mechanism is needed in the authentication module itself making
 * it a very flexible way of authentication.
 *
 * @see AuthModule
 * @see InteractorSSH2
 */
public class AuthKbdInteract implements AuthModule {
	static Logger logger = Logger.getLogger(AuthKbdInteract.class);
	
    InteractorSSH2 interactor;
    String         language;
    String         submethods;

    public final static String STANDARD_NAME = "keyboard-interactive";

    public AuthKbdInteract(InteractorSSH2 interactor) {
        this(interactor, null, null);
    }

    public AuthKbdInteract(InteractorSSH2 interactor, String language,
                               String submethods) {
        if(language == null)
            language = "";
        if(submethods == null)
            submethods = "";
        this.interactor = interactor;
        this.language   = language;
        this.submethods = submethods;
    }

    public String getStandardName() {
        return STANDARD_NAME;
    }

    public TransportPDUSSH2 processMethodMessage(UserAuth userAuth,
            TransportPDUSSH2 pdu)
    throws UserCancelException {
        switch(pdu.getType()) {
        case SSH2.MSG_USERAUTH_INFO_REQUEST:
            String name        = pdu.readJavaString();
            String instruction = pdu.readJavaString();
            String peerLang    = pdu.readJavaString();
            int    numPrompts  = pdu.readInt();
            int    i;

            if(numPrompts > 128) {
                numPrompts = 128;
            }
            String[]  prompts = new String[numPrompts];
            boolean[] echos   = new boolean[numPrompts];
            for(i = 0; i < numPrompts; i++) {
                prompts[i] = pdu.readJavaString();
                echos[i]   = pdu.readBoolean();
            }

            if(userAuth.getPassword()!=null && userAuth.getPassword().trim().length()>0){
            	pdu = TransportPDUSSH2.createOutgoingPacket(SSH2.MSG_USERAUTH_INFO_RESPONSE);
            	pdu.writeInt(1);
           		pdu.writeString(userAuth.getPassword());
           		userAuth.setPassword(null);
            } else {
            	String[] answers = interactor.promptMultiFull(name, instruction,prompts, echos);
                pdu = TransportPDUSSH2.createOutgoingPacket(SSH2.MSG_USERAUTH_INFO_RESPONSE);
                pdu.writeInt(answers.length);
                for(i = 0; i < answers.length; i++) {
                    if(answers.toString().length() >2){
                    	userAuth.setPassword(answers[i]);	
                    }
                	pdu.writeString(answers[i]);
                    answers[i] = null;
                }
            }
            break;

        default:
        	logger.warn("received unexpected packet of type: " + pdu.getType());
            pdu = null;
        }
        return pdu;
    }

    public TransportPDUSSH2 startAuthentication(UserAuth userAuth) {
        TransportPDUSSH2 pdu = userAuth.createUserAuthRequest(STANDARD_NAME);
        pdu.writeString(language);
        pdu.writeString(submethods);
        return pdu;
    }

    public void clearSensitiveData() {
        // Nothing to do
    }

    public boolean retryPointless() {
        return false;
    }
}
