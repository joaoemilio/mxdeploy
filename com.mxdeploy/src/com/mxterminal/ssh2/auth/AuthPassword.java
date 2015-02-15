package com.mxterminal.ssh2.auth;

import org.apache.log4j.Logger;

import com.mxterminal.ssh2.InteractorSSH2;
import com.mxterminal.ssh2.SSH2;
import com.mxterminal.ssh2.TransportPDUSSH2;
import com.mxterminal.ssh2.exception.UserCancelException;

/**
 * This class implements a module for password authentication as defined in the
 * userauth protocol spec. It can either be used passively (i.e. the password is
 * known beforehand), or it can be used interactively through the
 * <code>SSH2Interactor</code> callback interface.
 *
 * @see AuthModule
 */
public class AuthPassword implements AuthModule {

    public final static String STANDARD_NAME = "password";
    static Logger logger = Logger.getLogger(AuthPassword.class);
    
    private String password;
    private String newPassword;
    private InteractorSSH2 interactor;
    private String prompt;

    /**
     * Creates an instance which will never query the user. It will
     * only use the given password. The password is only tried once,
     * after that the module gives up.
     *
     * @param password Password to authenticate with once
     */
    public AuthPassword(String password) {
        this(null, null, password);
    }

    /**
     * Creates an instance which will always query the user.
     *
     * @param interactor Interactor used to query the user
     * @param prompt The prompt which will be shown to the user
     */
    public AuthPassword(InteractorSSH2 interactor, String prompt) {
        this(interactor, prompt, null);
    } 

    /**
     * Creates an instance which will first test with the given
     * password. If that fails then the user will be queried interactively.
     *
     * @param interactor Interactor used to query the user
     * @param prompt The prompt which will be shown to the user
     * @param password Password to try to authenticate with once
     */
    public AuthPassword(InteractorSSH2 interactor, String prompt,
                            String password) {
        this.interactor = interactor;
        this.prompt     = prompt;
        setPassword(password);
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    protected String getPassword() throws UserCancelException {
        if(password != null) {
            String tmp = new String(password);
            password = null;
            return tmp;
        }
        if(interactor != null) {
            while (password == null) {
                password = interactor.promptLine(prompt, false);
            }
        } else {
            password = "";
        }
        return password;
    }

    public String getPasswordConnected()  {
    	return password;
    }
    
    protected String getNewPassword(String prompt, String language)
    throws UserCancelException {
        if(newPassword != null)
            return newPassword;
        if(interactor != null) {
            // !!! TODO how is one expected to give user a chance to
            // rewrite password... (given only one prompt)
            //
            // TODO 2 language tag
            //
            while (newPassword == null) {
                newPassword = interactor.promptLine(prompt, false);
            }
        } else {
            newPassword = "";
        }
        return newPassword;
    }

    public String getStandardName() {
        return STANDARD_NAME;
    }

    public TransportPDUSSH2 processMethodMessage(UserAuth userAuth,
            TransportPDUSSH2 pdu)
    throws UserCancelException {
        switch(pdu.getType()) {
        case SSH2.MSG_USERAUTH_PASSWD_CHANGEREQ:
            String prompt   = pdu.readJavaString();
            String language = pdu.readJavaString();
            pdu = createChangeRequest(userAuth, prompt, language);
            break;

        default:
        	logger.warn(
                    "received unexpected packet of type: " + pdu.getType());
            pdu = null;
        }

        return pdu;
    }

    public TransportPDUSSH2 startAuthentication(UserAuth userAuth)
    throws UserCancelException {
        TransportPDUSSH2 pdu = userAuth.createUserAuthRequest(STANDARD_NAME);
        pdu.writeBoolean(false);
        if(userAuth.getPassword()!=null && userAuth.getPassword().trim().length()>0){
        	pdu.writeString(userAuth.getPassword());
        	password=userAuth.getPassword(); 
        } else {
           pdu.writeString(getPassword());
        }
        return pdu;
    }

    private TransportPDUSSH2 createChangeRequest(UserAuth userAuth,
            String prompt,
            String language)
    throws UserCancelException {
        TransportPDUSSH2 pdu = userAuth.createUserAuthRequest(STANDARD_NAME);
        pdu.writeBoolean(true);
        if (password != null && newPassword != null) {
            pdu.writeString(getPassword());
            pdu.writeString(getNewPassword(prompt, language));
        } else {
            String prompts[] = new String[3];
            boolean echos[] = { false, false, false };
            String passwords[];
            String badpass = "";
            
            prompts[0] = "Old password: ";
            prompts[1] = "New password: ";
            prompts[2] = "Retype new password: ";
            
            do {       
                passwords = interactor.promptMultiFull(
                    "Change password",
                    badpass + 
                    (prompt!=null ? prompt : ""),
                    prompts, echos);            
                badpass = "New passwords differs. ";
            } while ( false == passwords[1].equals(passwords[2]));
            pdu.writeString(passwords[0]);
            pdu.writeString(passwords[1]);

        }
        return pdu;
    }

    public void clearSensitiveData() {
        password = null;
        newPassword = null;
    }

    public boolean retryPointless() {
        return password == null && interactor == null;
    }
}
