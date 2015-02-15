package com.mxterminal.console;

import java.awt.Panel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.StringTokenizer;

import com.mxterminal.console.component.ComponentView;

public class VTKeyboard extends Panel implements ActionListener {
    private ConsoleWin terminal;

    public VTKeyboard(String keylist, ConsoleWin terminal,
                           ComponentView display) {
        this(arrayFromListNoTrim(keylist, ","), terminal, display);
    }

    public VTKeyboard(String[] keys, ConsoleWin terminal,
                           ComponentView display) {
        this.terminal = terminal;
        for (int i=0; i<keys.length; i++) {
            String label;
            String action;
            int index = keys[i].indexOf('=');
            if (index == -1) {
                label = action = keys[i].trim();
            } else {
                label = keys[i].substring(0, index).trim();
                action = keys[i].substring(index+1);
            }
            add(display.mkButton(label, action, this));
        }
    }

    /**
     * Convert a list expressed as a delimited string to an array.
     * This is similar to the function in SSH2ListUtil but does not
     * trim the entries.
     *
     * @param list List to split.
     * @param delim Delimiter.
     *
     * @return Resulting array.
     */
    public static String[] arrayFromListNoTrim(String list, String delim) {
        if(list == null) {
            return new String[0];
        }
        StringTokenizer st = new StringTokenizer(list, delim);
        int cnt = 0;
        String[] sa = new String[st.countTokens()];
        while(st.hasMoreTokens()) {
            sa[cnt++] = st.nextToken();
        }
        return sa;
    }

    /*
     * ActionListener interface
     */
    public void actionPerformed(ActionEvent e) {
        terminal.sendString(e.getActionCommand());
        terminal.requestFocus();
    }
}
