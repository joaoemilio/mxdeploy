package com.mxterminal.ssh2;

import com.mxterminal.ssh2.auth.AuthKbdInteract;
import com.mxterminal.ssh2.auth.AuthPassword;
import com.mxterminal.ssh2.exception.UserCancelException;

/**
 * This interface defines the different types of prompts which are needed for
 * interactive authentication. It's made generic to be able to allow
 * flexibility in the level of sophistication one wants for user
 * interaction.
 * <p>
 * Which of these functions are called and with which prompts are
 * provided is entirely up to the ssh-server. The server expects that
 * these functions will interact via graphical dialogs with the
 * user. Therefore the function calls includes things as instructions
 * and suggested names of dialogs and settings which tells if the user
 * should see what they type <code>echo</code> or not, as when
 * entering passwords.
 *
 * @see AuthKbdInteract
 * @see AuthPassword
 */
public interface InteractorSSH2 {
    /**
     * Prompt for a single string.
     *
     * @param prompt The prompt string to show
     * @param echo   True if the text the user enters should be
     *               echoed.
     *
     * @return The text entered by the user.
     */
    public String promptLine(String prompt, boolean echo)
    throws UserCancelException;

    /**
     * Prompt for multiple strings. The expectation here is that the
     * client will put up a dialog where the user sees multiple prompts
     * and input fields.
     *
     * @param prompts List of prompts to show
     * @param echos   List of boolean values which indicates if the
     *                text entered for the corresponding prompt should
     *                be echoed.
     *
     * @return An array of strings which contains on element for each
     * prompt, in the same order. The elements should contain the text
     * the user entered.
     */
    public String[] promptMulti(String[] prompts, boolean[] echos)
    throws UserCancelException;

    /**
     * Prompt for multiple strings. The expectation here is that the
     * client will put up a dialog where the user sees multiple prompts
     * and input fields. This version of the call includes more
     * elements which should be shown in the dialog.
     *
     * @param name    Suggested title of the dialog
     * @param instruction Instructions to show to user in the dialog
     * @param prompts List of prompts to show
     * @param echos   List of boolean values which indicates if the
     *                text entered for the corresponding prompt should
     *                be echoed.
     *
     * @return An array of strings which contains on element for each
     * prompt, in the same order. The elements should contain the text
     * the user entered.
     */
    public String[] promptMultiFull(String name, String instruction,
                                    String[] prompts, boolean[] echos)
    throws UserCancelException;

    public int promptList(String name, String instruction, String[] choices)
    throws UserCancelException;
}
