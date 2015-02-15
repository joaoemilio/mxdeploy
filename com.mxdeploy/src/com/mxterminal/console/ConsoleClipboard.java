package com.mxterminal.console;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.lang.reflect.Method;

public class ConsoleClipboard implements ConsoleClipboardHandler {

    // Singleton instance of GlobalClipboard
    //
    private static ConsoleClipboard globalClipboard = null;

    private Toolkit   toolkit;
    //private Vector    menuHandlers;
    private boolean   selectionAvailable;
    private Clipboard jvmClipboard;

    private ConsoleClipboard() {
        this.toolkit            = Toolkit.getDefaultToolkit();
        this.selectionAvailable = false;
        //this.menuHandlers       = new Vector();
    }

    public static synchronized ConsoleClipboard getClipboardHandler() {
        if(globalClipboard == null) {
            globalClipboard = new ConsoleClipboard();
        }

    	return globalClipboard;
    }

    public void setSelection(String selection) {
        // We always try to set both the Clipboard and the SystemSelection
        Clipboard cb = getClipboard();
        Clipboard ss = getSystemSelection();

        if(selection == null)
            selection = "";
        StringSelection sl = new StringSelection(selection);

        if(cb != null) {
            cb.setContents(sl, sl);
        }
        if (ss != null) {
            ss.setContents(sl, sl);
        }
        if (cb != null || ss != null) {
            //selectionAvailable(true);
        }
    }

    public String getSelection() {
        String    sl = null;

        // Prefer to fetch from SystemSelection if available

        Clipboard cb = getSystemSelection();
        if (cb == null) {
            cb = getClipboard();
        }
        if(cb == null) {
            return sl;
        }

        Transferable t = cb.getContents(this);

        if(t != null) {
            try {
                sl = (String) t.getTransferData(DataFlavor.stringFlavor);
            } catch (Exception e) {
                try {
                    toolkit.beep();
                } catch (Throwable ee) {
                    // Could not beep, we are probably an unpriviliged applet
                }
            }
        } else {
            try {
                toolkit.beep();
            } catch (Throwable e) {
                // Could not beep, we are probably an unpriviliged applet
            }
        }

        return sl;
    }

    public void clearSelection() {
        //selectionAvailable(false);
    }

    private synchronized Clipboard getClipboard() {

        Clipboard cb;
        if(jvmClipboard == null) {
            try {
                cb = toolkit.getSystemClipboard();
            } catch (Throwable e) {
                //
                // If we can't access the system clipboard we use our own
                // "global" one.
                //
                cb = jvmClipboard = new Clipboard("IESTTerm-local-clipboard");
            }
        } else {
            cb = jvmClipboard;
        }
        return cb;
    }

    // This is the PRIMARY selection on typical *nix systems and e.g. xterm reads it.
    // CDE writes to this as well as CLIPBOARD

    private synchronized Clipboard getSystemSelection() {

        Clipboard ss = null;
        try {
            Class c = toolkit.getClass();
            Method m = c.getMethod("getSystemSelection", new Class[] {} );

            ss = (Clipboard) (m.invoke(toolkit, new Object[]{}));
        } catch (Throwable e) {
            //
        }
        return ss;
    }

}
