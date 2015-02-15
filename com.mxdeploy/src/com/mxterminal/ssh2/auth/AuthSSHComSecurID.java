package com.mxterminal.ssh2.auth;

import org.apache.log4j.Logger;

import com.mxterminal.ssh2.InteractorSSH2;
import com.mxterminal.ssh2.SSH2;
import com.mxterminal.ssh2.TransportPDUSSH2;
import com.mxterminal.ssh2.exception.UserCancelException;

/**
 * This class implements a module for SecurID authentication as defined by SSH
 * Inc as the 'local' method 'securid-1@ssh.com'. It uses the interface
 * <code>SSH2Interactor</code> for all interactions generated in the
 * authentication process. This method is not part of any public standard, it's
 * currently only available with SSH2 servers from SSH Communcations.
 *
 * @see AuthModule
 * @see InteractorSSH2
 */
public class AuthSSHComSecurID implements AuthModule {
	static Logger logger = Logger.getLogger(AuthSSHComSecurID.class);
    public final static String STANDARD_NAME = "securid-1@ssh.com";

    InteractorSSH2 interactor;
    String         promptPIN;
    String         promptNext;
    String         promptNew;
    String         promptNew2;
    String         promptSelect;
    String         promptSystem;

    public AuthSSHComSecurID(InteractorSSH2 interactor,
                                 String promptPIN,
                                 String promptNext,
                                 String promptNew,
                                 String promptNew2,
                                 String promptSelect,
                                 String promptSystem) {
        this.interactor   = interactor;
        this.promptPIN    = promptPIN;
        this.promptNext   = promptNext;
        this.promptNew    = promptNew;
        this.promptNew2   = promptNew2;
        this.promptSelect = promptSelect;
        this.promptSystem = promptSystem;
    }

    public String getStandardName() {
        return STANDARD_NAME;
    }

    public TransportPDUSSH2 processMethodMessage(UserAuth userAuth,
            TransportPDUSSH2 pdu)
    throws UserCancelException {
        switch(pdu.getType()) {
        case SSH2.MSG_USERAUTH_SECURID_CHALLENGE:
            pdu = getPasscodeResponse(userAuth, true);
            break;
        case SSH2.MSG_USERAUTH_SECURID_NEW_PIN_REQD:
            int     minPinLen      = pdu.readInt();
            int     maxPinLen      = pdu.readInt();
            int     userSelectable = pdu.readByte();
            boolean alphaNumeric   = pdu.readBoolean();
            String  systemPIN      = pdu.readJavaString();

            String newPIN = getNewPIN(systemPIN, userSelectable, alphaNumeric,
                                      minPinLen, maxPinLen);

            pdu = createResponse(userAuth, newPIN, true);
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
        return getPasscodeResponse(userAuth, false);
    }

    private String getNewPIN(String systemPIN, int userSelectable,
                             boolean alphaNumeric,
                             int minLen, int maxLen)
    throws UserCancelException {
        String newPIN = "";
        String a      = null;
        if(userSelectable == SSH2.SSH_SECURID_USER_SELECTABLE_PIN) {
            a = null;
            while (a == null) {
                a = interactor.promptLine(promptSelect, true);
            }
            if(a.equalsIgnoreCase("yes") || a.equalsIgnoreCase("y")) {
                userSelectable = SSH2.SSH_SECURID_MUST_CHOOSE_PIN;
            } else {
                userSelectable = SSH2.SSH_SECURID_CANNOT_CHOOSE_PIN;
            }
        }
        if(userSelectable == SSH2.SSH_SECURID_CANNOT_CHOOSE_PIN) {
            a = null;
            while (a == null) {
                a = interactor.promptLine(promptSystem + systemPIN +
                                          " (yes/no)? ", true);
            }
            if(a.equalsIgnoreCase("yes") || a.equalsIgnoreCase("y")) {
                newPIN = systemPIN;
            }
        } else if(userSelectable == SSH2.SSH_SECURID_MUST_CHOOSE_PIN) {
            String[] aa;
            do {
                aa = interactor.promptMulti(new String[] { promptNew +
                                            " (" + minLen + " - " + maxLen +
                                            (alphaNumeric ? "characters" :
                                             "digits") + ")", promptNew2 },
                                            new boolean[] { false, false });
            } while(!aa[0].equals(aa[1]));
            newPIN = aa[0];
        }
        return newPIN;
    }

    private TransportPDUSSH2 getPasscodeResponse(UserAuth userAuth,
            boolean challenge)
    throws UserCancelException {
        String prompt;
        if(challenge) {
            prompt = promptNext;
        } else {
            prompt = promptPIN;
        }

        String pin = null;
        while (pin == null) {
            pin = interactor.promptLine(prompt, true);
        }

        return createResponse(userAuth, pin, challenge);
    }

    private TransportPDUSSH2 createResponse(UserAuth userAuth,
                                            String pin,
                                            boolean challenge) {
        TransportPDUSSH2 pdu =
            userAuth.createUserAuthRequest(STANDARD_NAME);
        pdu.writeBoolean(challenge);
        pdu.writeString(pin);
        return pdu;
    }

    public void clearSensitiveData() {
        // Nothing to do
    }

    public boolean retryPointless() {
        return false;
    }
}
