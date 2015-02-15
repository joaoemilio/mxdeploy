package com.mxterminal.console;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.Frame;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.lang.reflect.Method;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.NoSuchElementException;
import java.util.Properties;
import java.util.StringTokenizer;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.eclipse.swt.widgets.Display;

import com.mxdeploy.swt.dialogs.authentication.SshAuthenticationDialog;
import com.mxsecurity.util.RandomSeed;
import com.mxterminal.channel.ChannelEventListener;
import com.mxterminal.console.charset.ConsoleCharsetFilter;
import com.mxterminal.console.component.ComponentAWT;
import com.mxterminal.console.component.ComponentController;
import com.mxterminal.console.component.ComponentView;

/**
 * The actual implementation of a terminal window. The terminal window
 * may use AWT or Swing widgets depending on the owner frame type.
 */
public final class ConsoleWin extends WindowAdapter
    implements ConsoleWindows, ComponentController, KeyListener {

	//private static Log log = LogFactory.getLog(TerminalWin.class);
	
	static Logger logger = Logger.getLogger(ConsoleWin.class);
	
    final static boolean DEBUG         = false;
    final static boolean DEBUGKEYEVENT = false;

    boolean altKeyKludge  = false;
    boolean ctrlKeyKludge  = false;

    int lastKeyKludge   = 0;
    int lastKeyWasEvent = KeyEvent.KEY_RELEASED;

    Vector                   inListeners;
    Vector                   outListeners;
    ConsolePrinter          printer;
    boolean                  printerActive;
    ConsoleClipboardHandler clipboard;
    Frame                    ownerFrame;
    Object                   writeLock;

    public   ConsoleCharsetFilter  filter;
    boolean                  ignoreNull = true;
    boolean                  dumbMode = false;

    final static char[] mc4 = new char[] { (char)27, '[', '4', 'i' };
    int mc4MatchIdx;

    private int popupButton = InputEvent.BUTTON3_MASK;
    private int pasteButton = InputEvent.BUTTON2_MASK;

    Container container;
    VTKeyboard virtualKeyboard;

    //TerminalMenuHandler menuHandler;

    String              title;

    Properties props;
    boolean propsChanged;
    String savedGeomPos;
    boolean insideConstructor;

    int vpixels;
    int hpixels;

    int     selectRowAnchor;
    int     selectColAnchor;
    int     selectRowLast;
    int     selectColLast;
    int     selectVisTop;

    boolean hasSelection;
    boolean selectReverse;
    String  selectDelims;
    int     selectClickRow = -1;
    boolean selectClickState;
    long    lastLeftClick  = 0;

    private Hashtable keymappings = null;
    
    Color origBgColor;
    Color origFgColor;
    Color cursorColor;

    /*
     * For printing
     */
    String printFontName = "Courier";
    int printFontSize = 9;
    
       
    
	public static final int COMMAND_ENTER = 10;
	public static final int COMMAND_SPACE = 13;
	public static final int COMMAND_STOP  = 3;
	public static final int COMMAND_TRASH = 8;
	public static final int COMMAND_FINISHED = -1;
	
	
	//protected List<ChannelListener> listeners;	
	
	
	private ChannelEventListener channelEventListener = null;
	
	private String command = null;
	private String resposta= "";
	private boolean isCommand = false;
	private boolean isEnter = false;
	private boolean isProcedureRunning = false;    
	private int countPassword = 0;
	private String hostname = "";
    /**
     * Predefined colors
     */
    public final static Color termColors[] = {
        Color.black,
        Color.red.darker(),
        Color.green.darker(),
        Color.yellow.darker(),
        Color.blue.darker(),
        Color.magenta.darker(),
        Color.cyan.darker(),
        Color.white,
        Color.darkGray,
        Color.red,
        Color.green,
        Color.yellow,
        Color.blue,
        Color.magenta,
        Color.cyan,
        Color.white
    };

    /**
     * The names of the predefined colors
     */
    public final static String[] termColorNames = {
        "black", "red", "green", "yellow", "blue", "magenta", "cyan", "white",
        "i_black", "i_red", "i_green", "i_yellow",
        "i_blue","i_magenta", "i_cyan", "i_white"
    };

    // (NOTE: The real terminal attributes are in Terminal.java)
    //
    private final static int ATTR_CHARNOTDRAWN = 0x0000;
    private final static int ATTR_LINEDRAW     = 0x0100;
    private final static int ATTR_SELECTED     = 0x1000;
    private final static int ATTR_CHARDRAWN    = 0x8000;

    private final static int MASK_ATTR   = 0x0000ffff;
    private final static int MASK_FGCOL  = 0x00ff0000;
    private final static int MASK_BGCOL  = 0xff000000;
    private final static int SHIFT_FGCOL = 16;
    private final static int SHIFT_BGCOL = 24;

    private final static char[] byte2char = new char[256];

    // Options
    static private ConsoleOption optPassthruPrint =
    new ConsoleOption("passthru-prn-enable",
                       "Enable Passthrough Print", "true");
    static private ConsoleOption optCopyCrNl =
    new ConsoleOption("copy-crnl","Copy <cr><nl> line ends", "false");
    static private ConsoleOption optCopySelect =
    new ConsoleOption("copy-select","Copy on select", "true");
    static private ConsoleOption optResizable =
    new ConsoleOption("resizable", "Terminal window is resizable", "true");

    // Settings
    static private ConsoleOption optEncoding =new ConsoleOption("encoding", "Character encoding to use", "iso-8859-1");
    static private ConsoleOption optIgnNull = new ConsoleOption("ignore-null", "Ignore null characters", "true");
    static private ConsoleOption optCRLF = new ConsoleOption("crlf","Send carriage returns as telnet <CR><LF>", "false");
    static private ConsoleOption optFontName =  new ConsoleOption("font-name", "", "Courier New");
    static private ConsoleOption optFontSize =  new ConsoleOption("font-size", "", "12");
    static private ConsoleOption optGeometry =  new ConsoleOption("geometry", "", "80x24");
    static private ConsoleOption optTermType =  new ConsoleOption("term-type", "", "xterm");
    static private ConsoleOption optSaveLines = new ConsoleOption("save-lines", "", "512");
    static private ConsoleOption optScrollBar = new ConsoleOption("scrollbar", "", "right");
    static private ConsoleOption optBgColor = new ConsoleOption("bg-color", "", "white");
    static private ConsoleOption optFgColor = new ConsoleOption("fg-color", "", "black");
    static private ConsoleOption optCursorColor = new ConsoleOption("cursor-color", "", "i_blue");
    static private ConsoleOption optSelectDelim = new ConsoleOption("select-delim", "", "\" \"");
    static private ConsoleOption optPasteButton = new ConsoleOption("paste-button", "", "middle");
    static private ConsoleOption optInputCharset = new ConsoleOption("input-charset", "", "none");
    static private ConsoleOption optLineSpaceDelta = new ConsoleOption("line-space-delta", "", "0");
    static private ConsoleOption optKeyKludge = new ConsoleOption("key-kludge", "Workaround for MS JVM", "false");
    static private ConsoleOption optKeyMappings = new ConsoleOption("key-mappings", "", "");
    static private ConsoleOption optVirtualKeyboard = new ConsoleOption("virtual-kbd","Virtual keyboard", "");
    static private ConsoleOption optPrintFontName = new ConsoleOption("print-font-name","Font name when printing", "Courier");
    static private ConsoleOption optPrintFontSize = new ConsoleOption("print-font-size","Font size when printing", "8");
    static private ConsoleOption optionsDef[] = { optPassthruPrint, optCopyCrNl,optCopySelect,optResizable  };
    static private ConsoleOption settingsDef[] = {
        optEncoding,
        optIgnNull,
        optCRLF,
        optFontName,
        optFontSize,
        optTermType,
        optSaveLines,
        optGeometry,
        optScrollBar,
        optBgColor,
        optFgColor,
        optCursorColor,
        optSelectDelim,
        optPasteButton,
        optInputCharset,
        optLineSpaceDelta,
        optKeyMappings,
        optVirtualKeyboard,
        optPrintFontName,
        optPrintFontSize
   };
    static private String obsoleteOptions[] = {
        "resize-gravity",
        "80x132-enable",
        "80x132-toggle"
    };

    static private Properties defaultProperties;
    static private String propertyNames[];
    static private Class terminalList[];

    static {
        int i;
        byte[] b = new byte[1];
        char[] c;
        for(i = 0; i < 256; i++) {
            b[0] = (byte)i;
            c = (new String(b)).toCharArray();
            if(c.length > 0) {
                byte2char[i] = c[0];
            } else {
                byte2char[i] = (char)i;
            }
        }

        String terminalClasses[] = {
            "com.mxterminal.console.VT100Component"};
        
        terminalList = new Class[terminalClasses.length];
        for (i = 0; i < terminalClasses.length; i++) {
            try {
                terminalList[i] = Class.forName(terminalClasses[i]);
            } catch (Exception e) {
                terminalList[i] = null;
            }
        }

        defaultProperties = new Properties();

        ConsoleOption to[];
        to = settingsDef;
        for (i = 0; i < to.length; i++) {
            defaultProperties.put(to[i].getKey(), to[i].getDefault());
        }
        to = optionsDef;
        for (i = 0; i < to.length; i++) {
            defaultProperties.put(to[i].getKey(), to[i].getDefault());
        }
        to = getTerminalsOptions();
        for (i = 0; i < to.length; i++) {
            defaultProperties.put(to[i].getKey(), to[i].getDefault());
        }

        propertyNames = new String[defaultProperties.size()];
        Enumeration names = defaultProperties.propertyNames();
        int j = 0;
        while (names.hasMoreElements()) {
            String name = (String) names.nextElement();
            propertyNames[j++] = name;
        }
    }

    private static Console getTerminal(String name) {
        Console ret = null;
        Class signature[] = { String.class };
        Object args[] = { name };
        for (int i = 0; i < terminalList.length; i++) {
            if (terminalList[i] == null) {
                continue;
            }
            try {
                Method m = terminalList[i].getMethod("getTerminal", signature);
                ret = (Console) m.invoke(null, args);
                if (ret != null) {
                    break;
                }
            } catch (Exception e) {
            	e.printStackTrace();
            }
        }
        
        return ret;
    }

    /**
     * Get a list of terminal types this terminal can emulate.
     *
     * @return an array of terminal type names
     */
    static public String[] getTerminalTypes() {
        String ret[] = new String[0];

        for (int i = 0; i < terminalList.length; i++) {
            if (terminalList[i] == null) {
                continue;
            }
            try {
                String terms[];
                Method m = terminalList[i].getMethod("getTerminalTypes",
                                                     (Class[])null);
                terms = (String[]) m.invoke(null, (Object[])null);
                if (terms != null) {
                    String tmp[] = new String[ret.length + terms.length];
                    System.arraycopy(ret, 0, tmp, 0, ret.length);
                    System.arraycopy(terms, 0, tmp, ret.length, terms.length);
                    ret = tmp;
                }
            } catch (Exception e) {
            }
        }
        
        if (ret.length == 0) {
            return null;
        }
        return ret;
    }

    static private ConsoleOption[] getTerminalsOptions() {
        ConsoleOption ret[] = new ConsoleOption[0];

        for (int i = 0; i < terminalList.length; i++) {
            if (terminalList[i] == null) {
                continue;
            }
            try {
                ConsoleOption terms[];
                Method m = terminalList[i].getMethod("getTerminalOptions",
                                                     (Class[])null);
                terms = (ConsoleOption[]) m.invoke(null, (Object[])null);
                if (terms != null) {
                    ConsoleOption tmp[] = new ConsoleOption[ret.length +
                                                              terms.length];
                    System.arraycopy(ret, 0, tmp, 0, ret.length);
                    System.arraycopy(terms, 0, tmp, ret.length, terms.length);
                    ret = tmp;
                }
            } catch (Exception e) {
            }
        }
        
        return ret;
    }

    private boolean[] termOptions;
    private static int OPT_PASSTHRU_PRN = 0;
    private static int OPT_COPY_SEL     = 1;
    private static int OPT_KEY_KLUDGE   = 2;
    private static int OPT_LAST_OPT     = 3;

    private String copyEol;

    private ComponentView display;
    private Console terminal;

    /**
     * Create a new terminal window with the default values of all
     * properties. The window will use AWT or Swing widgets depending
     * on if the owner frame is a <code>Frame</code> or
     * <code.JFrame</code>.
     *
     * @param ownerFrame frame used to determine if AWT or Swing
     * should be used.
     */
    public ConsoleWin(Frame ownerFrame) throws IllegalArgumentException,
        NoSuchElementException {
        this(ownerFrame, defaultProperties);
    }

    /**
     * Create a new terminal window with the given properties. The
     * window will use AWT or Swing widgets depending on if the owner
     * frame is a <code>Frame</code> or <code.JFrame</code>.
     *
     * @param ownerFrame frame used to determine if AWT or Swing
     * should be used.
     * @param initProps initial properties
     */
    public ConsoleWin(Frame ownerFrame, Properties initProps)
        throws IllegalArgumentException
    {
        this(ownerFrame, initProps, false);
    }

    /**
     * Create a new terminal window with the given properties. The
     * window will use AWT or Swing widgets depending on if the owner
     * frame is a <code>Frame</code> or <code.JFrame</code>.
     *
     * @param ownerFrame frame used to determine if AWT or Swing
     * should be used.
     * @param initProps initial properties
     * @param setAsDefault mark these properties as default and use
     * them if no others are specified in the future.
     */
    public ConsoleWin(Frame ownerFrame, Properties initProps,  boolean setAsDefault)
        throws IllegalArgumentException
    {
        this.title         = null;
        this.termOptions   = new boolean[OPT_LAST_OPT];
        this.writeLock     = new Object();
        this.terminal      = getTerminal("xterm");

        if(setAsDefault) {
            Enumeration e = initProps.keys();
            while(e.hasMoreElements()) {
                String name  = (String)e.nextElement();
                String value = initProps.getProperty(name);
                name = ConsoleDefProps.backwardCompatProp(name);
                defaultProperties.put(name, value);
            }
        }

        savedGeomPos = "";

        this.ownerFrame = ownerFrame;
        
        try {
            display =  new ComponentAWT(ownerFrame); 
        } catch (Throwable t) {
            t.printStackTrace();
        }
        
        ownerFrame.addWindowListener(this);
        display.setController(this);
        
        display.setKeyListener(this);
        terminal.setDisplay(display);
        terminal.setTerminalWindow(this);

        insideConstructor = true;
        setProperties(initProps, true);
        insideConstructor = false;
        propsChanged = false;
    }
    
//    public boolean isSwingJFrame(Frame f) {
//        try {
//            Method m = f.getClass().getMethod("getJMenuBar", (Class[])null);
//            return m != null;
//        } catch (Throwable t) {
//        }
//        return false;
//    }    

    /**
     * Generate a syntetic <code>ComponentShown</code> event.
     */
    public void emulateComponentShown() {
        display.emulateComponentShown();
    }

    /**
     * Causes the terminal emulator to request the focus.
     */
    public void requestFocus() {
    	if(display!=null){
           display.requestFocus();
    	}
    }

//    /**
//     * Set the menu handler
//     */
//    public void setMenus(TerminalMenuHandler menus) {
//        menuHandler = menus;
//    }
    
    /**
     * Return the menu handler which is responsible for the menus
     * attached to this terminal window.
     *
     * @return the menu handler
     */
//    public TerminalMenuHandler getMenus() {
//        return menuHandler;
//    }

    /**
     * Return the current display view object which handles the actual
     * displaying of data in the terminal window.
     */
    public ComponentView getDisplay() {
        return display;
    }

    /**
     * Called when the menus should be updated.
     */
//    public void updateMenus() {
//        if (menuHandler != null) 
//            menuHandler.update();
//    }

    /**
     * Set the logo to show on the login screen.
     *
     * @param logoImg the actual logo
     * @param x x position to show it at
     * @param y y position to show it on
     * @param w width of logo when shown
     * @param h height of logo when shown
     */
//    public void setLogo(Image logoImg, int x, int y, int w, int h) {
//        display.setLogo(logoImg, x, y, w, h);
//    }
//
//    /**
//     * Get the logo image.
//     *
//     * @return the logo image
//     */
//    public Image getLogo() {
//        return display.getLogo();
//    }
//
//    /**
//     * Show the logo on the terminal.
//     */
//    public boolean showLogo() {
//        return display.showLogo();
//    }
//
//    /**
//     * Hide the logo, that is do not show it.
//     */
//    public void hideLogo() {
//        display.hideLogo();
//    }

    public void setProperties(Properties newProps, boolean merge)
        throws IllegalArgumentException
    {
        String name, value;
        int i;
        Properties oldProps = props;

        props = new Properties(defaultProperties);

        if(merge && oldProps != null) {
            Enumeration e = oldProps.keys();
            while(e.hasMoreElements()) {
                name  = (String)e.nextElement();
                value = oldProps.getProperty(name);
                props.put(name, value);
            }
        }

        // Order is important to get this right, set "normal" settings first,
        // then options
        // !!! OUCH
        //
        String  oldVal;

        for(i = 0; i < settingsDef.length; i++) {
            name  = settingsDef[i].getKey();;
            value = newProps.getProperty(name);
            name  = ConsoleDefProps.backwardCompatProp(name);
            if(value == null)
                value = props.getProperty(name);
            if(!merge && oldProps != null) {
                oldVal = oldProps.getProperty(name);
                setProperty(name, value, !value.equals(oldVal));
            } else {
                setProperty(name, value, insideConstructor);
            }
        }
        for(i = 0; i < optionsDef.length; i++) {
            name  = optionsDef[i].getKey();;
            value = newProps.getProperty(name);
            name  = ConsoleDefProps.backwardCompatProp(name);
            if(value == null)
                value = props.getProperty(name);
            if(!merge && oldProps != null) {
                oldVal = oldProps.getProperty(name);
                setProperty(name, value, !value.equals(oldVal));
            } else {
                setProperty(name, value, insideConstructor);
            }
        }

        // Add options for our terminal as well
        ConsoleOption opts[] = terminal.getOptions();
        for(i = 0; i < opts.length; i++) {
            name  = opts[i].getKey();;
            value = newProps.getProperty(name);
            name  = ConsoleDefProps.backwardCompatProp(name);
            if(value == null)
                value = props.getProperty(name);
            if(!merge && oldProps != null) {
                oldVal = oldProps.getProperty(name);
                setProperty(name, value, !value.equals(oldVal));
            } else {
                setProperty(name, value, insideConstructor);
            }
        }
    }

    private static Properties propertiesUnion(Properties p1, Properties p2) {
        Properties ret = new Properties();
        Properties p;
        Enumeration keys;

        if (p1 != null) {
            p = p1;
            keys = p.keys();
            while (keys.hasMoreElements()) {
                String key = (String) keys.nextElement();
                ret.put(key, p.getProperty(key));
            }
        }

        if (p2 != null) {
            p = p2;
            keys = p.keys();
            while (keys.hasMoreElements()) {
                String key = (String) keys.nextElement();
                ret.put(key, p.getProperty(key));
            }
        }

        return ret;
    }

    public Properties getProperties() {
        return propertiesUnion(props, terminal.getProperties());
    }

    public boolean getPropsChanged() {
        return propsChanged || terminal.getPropsChanged();
    }

    public void setPropsChanged(boolean value) {
        terminal.setPropsChanged(value);
        propsChanged = value;
    }

    /**
     * Check if the passed property name is a valid property for this
     * terminal window.
     *
     * @param key a property name
     *
     * @return true if this is a terminal property
     */
    static public boolean isProperty(String key) {
        // defaultProperties contains all possible terminal options

        if (defaultProperties.containsKey(key))
            return true;

        for (int i=0; i<obsoleteOptions.length; i++)
            if (key.equals(obsoleteOptions[i]))
                return true;

        return false;
    }

    public void resetToDefaults() {
        setProperties(defaultProperties, false);
    }

    /**
     * Get the name of all properties the terminal emulator cares
     * about.
     *
     * @return an array of property names
     */
    static public String[] getPropertyNames() {
        return propertyNames;
    }

    public String getProperty(String key) {
        String value;
        key = ConsoleDefProps.backwardCompatProp(key);

        value = this.terminal.getProperty(key);
        if (value != null) {
            return value;
        }
        return props.getProperty(key);
    }

    /**
     * Get the default value of the given property.
     *
     * @param key name of peroperty
     *
     * @return the value
     */
    public String getDefaultProperty(String key) {
        key  = ConsoleDefProps.backwardCompatProp(key);
        String value = terminal.getDefaultProperty(key);
        if (value != null) {
            return value;
        }
        return defaultProperties.getProperty(key);
    }

    /**
     * Reset a property back to its default value.
     *
     * @param key name of property to reset
     */
    public void resetProperty(String key) {
        key  = ConsoleDefProps.backwardCompatProp(key);
        setProperty(key, getDefaultProperty(key));
    }

    public void setProperty(String key, String value)
        throws IllegalArgumentException, NoSuchElementException
    {
        setProperty(key, value, false);
    }


    public ConsoleOption[] getOptions() {
        ConsoleOption tOptions[] = terminal.getOptions();
        ConsoleOption options[] = new ConsoleOption[optionsDef.length +
                                                      tOptions.length];
        for (int i = 0; i < optionsDef.length; i++) {
            options[i] = optionsDef[i].copy();
        }
        for (int i = 0; i < tOptions.length; i++) {
            options[optionsDef.length + i] = tOptions[i].copy();
        }
        for (int i = 0; i < options.length; i++) {
            options[i].setValue(getProperty(options[i].getKey()));
        }
        return options;
    }

    private static int toKeyCode(String k) {
        try {
            java.lang.reflect.Field f = KeyEvent.class.getField("VK_"+k.toUpperCase());
            return f.getInt(null);
        } catch (Throwable t) {
            t.printStackTrace();
        }
        return -1;
    }

	private void addKeyMapping(String key, String value) 
        throws IllegalArgumentException 
    {
        key = key.toLowerCase();
        String[] a = com.mxterminal.ssh2.ListUtilSSH2.arrayFromList(key, "+");
        int mod = 0;
        int code = -1;
        for(int i=0; i<a.length; i++) {
            if (a[i].equals("shift")) {
                mod |= InputEvent.SHIFT_MASK;
            } else if (a[i].equals("ctrl")) {
                mod |= InputEvent.CTRL_MASK;
            } else if (a[i].equals("alt")) {
                mod |= InputEvent.ALT_MASK;
            } else if (a[i].equals("altgraph")) {
                mod |= InputEvent.ALT_GRAPH_MASK;
            } else if (a[i].equals("meta")) {
                mod |= InputEvent.META_MASK;
            } else if (code != -1) {
                throw new IllegalArgumentException("Illegal key specification: '" + key + "'");
            } else {
                code = toKeyCode(a[i]);
                if (code == -1)
                    throw new IllegalArgumentException("Unknown key code: '" + a[i] + "'");
            }
        }

        if (keymappings == null)
            keymappings = new Hashtable();
        keymappings.put(code+":"+mod, value);
    }

    private static Properties string2props(String s) {
        Properties p = new Properties();
        if (s == null) return p;
        StringBuffer tmp = new StringBuffer();
        StringBuffer key = new StringBuffer();
        StringBuffer val = new StringBuffer();
        char[] b = s.toCharArray();
        int i=0;
        do {
            key.setLength(0);
            val.setLength(0);

            /* read key */
            while (i<b.length) {
                if (b[i] == '\\' && i<b.length-1) {
                    key.append(b[i++]);
                } else if (b[i] == '=') {
                    i++;
                    break;
                }
                key.append(b[i++]);
            }
            if (i >= b.length) break;

            /* read value */
            while (i<b.length) {
                if (b[i] == '\\' && i<b.length-1) {
                    val.append(b[i++]);
                } else if (b[i] == ',') {
                    i++;
                    break;
                }
                val.append(b[i++]);
            }

            tmp.append(key.toString());
            tmp.append('=');
            tmp.append(val.toString());
            tmp.append('\n');
        } while (i < b.length);

        try {
            final StringReader r = new StringReader(tmp.toString());
            p.load(new InputStream() {
                public int read() throws IOException { 
                    return r.read();
                }
            });
        } catch (Throwable t) {
        }

        return p;
    }

    private void addKeyMappings(String value) 
        throws IllegalArgumentException 
    {
        Properties p = string2props(value);
        Enumeration e = p.keys();
        while (e.hasMoreElements()) {
            String k = (String)e.nextElement();
            String v = p.getProperty(k);
            addKeyMapping(k, v);
        }
    }

    /**
     * Set a property.
     *
     * @param key name of property
     * @param value value of property
     * @param forceSet if true then the property os set event if the
     * key is not a valid terminal property name
     */
    public synchronized void setProperty(String key, String value,
                                         boolean forceSet)
        throws IllegalArgumentException, NoSuchElementException
    {
        boolean isEqual = false;
        key = ConsoleDefProps.backwardCompatProp(key);

        if (terminal != null && !terminal.setProperty(key, value, true)) {
            // Option did not belong to the terminal, must be mine then
            String val = props.getProperty(key);
            if(val != null && val.equals(value)) {
                isEqual = true;
                if(!forceSet)
                    return;
            }
            
            boolean boolVal = Boolean.valueOf(value).booleanValue();

            if (key.equals(optPassthruPrint.getKey())) {
                termOptions[OPT_PASSTHRU_PRN] = boolVal;

            } else if (key.equals(optCopyCrNl.getKey())) {
                if (boolVal) {
                    copyEol = "\r\n";
                } else {
                    copyEol = "\r";
                }
            } else if (key.equals(optCopySelect.getKey())) {
                termOptions[OPT_COPY_SEL] = boolVal;
            } else if (key.equals(optKeyKludge.getKey())) {
                termOptions[OPT_KEY_KLUDGE] = boolVal;
            } else if (key.equals(optResizable.getKey())) {
                ownerFrame.setResizable(boolVal);
    
            } else if(key.equals(optTermType.getKey())) {
                if (!terminal.terminalType().equals(value)) {
                    // try to reset the existing terminal to new type
                    if (!terminal.setTerminalType(value)) {
                        // A new terminal type, initialize it
                        Console newTerm = getTerminal(value);
                        if (newTerm == null) {
                            throw new IllegalArgumentException(
                                "terminal type '" + value + "' unknown");
                        }
                        terminal = newTerm;
                    }
                    terminal.setDisplay(display);
                    terminal.setTerminalWindow(this);
                    terminal.reset();

                    // Set some settings for the new terminal
                    setProperty(optSaveLines.getKey(),
                                getProperty(optSaveLines.getKey()), true);

                    // Notify our input listeners that the terminal type
                    // has changed
                    if(inListeners != null) {
                        int n = inListeners.size();
                        for(int i = 0; i < n; i++) {
                            ConsoleInputListener inListener =
                                (ConsoleInputListener)inListeners.elementAt(i);
                            if(inListener != null) {
                                inListener.signalTermTypeChanged(value);
                            }
                        }
                    }
                }
            } else if(key.equals(optEncoding.getKey())) {
                try {
                    Class c = 
                        Class.forName("com.mxterminal.console.filter.Filter");
                    Method m = c.getMethod("create",new Class[]{String.class});
                    filter = (ConsoleCharsetFilter)m.invoke(null, 
                                                     new Object[] { value });
                    if (null == filter) {
                        throw new IllegalArgumentException(
                            "Encoding not supported");
                    }
                } catch (IllegalArgumentException e) {
                    throw e;
                } catch (Throwable t) {
                    System.out.println("System has no support for encodings");
                }
            } else if(key.equals(optIgnNull.getKey())) {
                ignoreNull = boolVal;
            } else if(key.equals(optCRLF.getKey())) {
                // Do nothing
            } else if(key.equals(optFontName.getKey())) {
                setFont(value,
                        Integer.parseInt(getProperty(optFontSize.getKey())));

            } else if(key.equals(optFontSize.getKey())) {
                try {
                    setFont(getProperty(optFontName.getKey()),
                            Integer.parseInt(value));
                } catch (NumberFormatException e) {
                    throw new IllegalArgumentException(
                        "value for '" + key + "' must be an integer");
                }

            } else if(key.equals(optSaveLines.getKey())) {
                try {
                    int sl = Integer.parseInt(value);
                    if(sl < 0 || sl > 8192)
                        throw new NumberFormatException();
                    if (!terminal.setSaveLines(sl)) {
                        write("\n\rOut of memory allocating scrollback " +
                              "buffer!");
                    }
                } catch (NumberFormatException e) {
                    throw new IllegalArgumentException(
                        "value for '" + key + "' must be an integer (0-8192)");
                }

            } else if(key.equals(optScrollBar.getKey())) {
                if(value.equals("left") ||
                   value.equals("right") ||
                   value.equals("none")) 
                {
                    display.moveScrollbar(value);
                    updateScrollbarValues();
                } else {
                    throw new IllegalArgumentException(
                        "scrollbar can be right, left or none");
                }

            } else if(key.equals("bg-color") ||
                      key.equals("fg-color") ||
                      key.equals("cursor-color")) {
                Color c;
                try {
                    if(Character.isDigit(value.charAt(0))) {
                        c = getTermRGBColor(value);
                    } else {
                        c = getTermColor(value);
                    }
                } catch (NumberFormatException e) {
                    throw new IllegalArgumentException(
                        "valid colors: 'color-name' or '<r>,<g>,<b>'");
                }
                if(key.equals("bg-color")) {
                    origBgColor = c;
                    display.setBackgroundColor(origBgColor);
                } else if(key.equals("cursor-color")) {
                    display.setCursorColor(c);
                } else {
                    origFgColor = c;
                    display.setForegroundColor(origFgColor);
                }

            } else if(key.equals("resize-gravity")) {
                // This option is obsolete
                return;

            } else if(key.equals(optGeometry.getKey())) {
                setGeometry(value);
                // the displayResized callback is responsible for putting
                // the new geometry string into the properties table.
                // This is to avoid looping.
                return;

            } else if(key.equals(optSelectDelim.getKey())) {
                if(!(value.charAt(0) == '"'
                     && value.charAt(value.length() - 1) == '"')) {
                    value = "\"" + value + "\"";
                }
                selectDelims = value.substring(1, value.length());

            } else if(key.equals(optPasteButton.getKey())) {
                if(value.equals("middle")) {
                    pasteButton = InputEvent.BUTTON2_MASK;
                } else if(value.equals("right")) {
                    pasteButton = InputEvent.BUTTON3_MASK;
                } else if(value.equals("shift+left")) {
                    pasteButton = InputEvent.BUTTON1_MASK |
                        InputEvent.SHIFT_MASK;
                } else {
                    throw new IllegalArgumentException(
                        "mouse paste button can be 'middle' or 'right'");
                }

            } else if(key.equals(optInputCharset.getKey())) {
                setInputCharset(value);

            } else if(key.equals(optLineSpaceDelta.getKey())) {
                display.setLineSpaceDelta(Integer.parseInt(value));
                
            } else if(key.equals(optKeyMappings.getKey())) {
                addKeyMappings(value);
                
            } else if(key.equals(optVirtualKeyboard.getKey())) {
                if (virtualKeyboard != null && container != null) {
                    container.remove(virtualKeyboard);
                    virtualKeyboard = null;
                }
                if (value.trim().length() > 0) {
                    virtualKeyboard = new VTKeyboard(value, this,display);
                    if (container != null) {
                        container.add(virtualKeyboard, BorderLayout.SOUTH);
                    }
                }
                if (container != null) {
                    container.validate();
                }
            } else if(key.equals(optPrintFontName.getKey())) {
                printFontName = value;

            } else if(key.equals(optPrintFontSize.getKey())) {
                printFontSize = Integer.parseInt(value);

            } else {
                throw new NoSuchElementException(
                    "unknown terminal-property '" + key + "'");
            }
            props.put(key, value);
        }

        if(!isEqual || terminal.getPropsChanged()) {
            propsChanged = true;
            //updateMenus();
        }
    }

    /**
     * Get a color as specified by an R,G,B tuple.
     *
     * @param value a string with the R,G,B value of the form
     * 0,0,0. Legal values for each element are 0-255.
     */
    public static Color getTermRGBColor(String value)
        throws NumberFormatException {
        int r, g, b, c1, c2;
        Color c;
        c1 = value.indexOf(',');
        c2 = value.lastIndexOf(',');
        if(c1 == -1 || c2 == -1)
            throw new NumberFormatException();
        r = Integer.parseInt(value.substring(0, c1).trim());
        g = Integer.parseInt(value.substring(c1 + 1, c2).trim());
        b = Integer.parseInt(value.substring(c2 + 1).trim());
        c = new Color(r, g, b);
        return c;
    }

    /**
     * Get a predefined terminal color.
     *
     * @param name name of desired color
     */
    public static Color getTermColor(String name)
        throws IllegalArgumentException {
        int i;
        for(i = 0; i < termColors.length; i++) {
            if(termColorNames[i].equalsIgnoreCase(name))
                break;
        }
        if(i == termColors.length)
            throw new IllegalArgumentException("Unknown color: " + name);
        return termColors[i];
    }

    /**
     * Set the geometry to use, The geometry is a string which looks
     * something like <code>80x24+23+42</code> which creates a window
     * which is 80 columns with 24 rows and which is placed at
     * x=23,y=24. It is also possible to just specify the size or the
     * position and any of the position values can be preceded by a minus
     * sign in which case it is countyed from the right/bottom edge
     * instead.
     *
     * @param geometry string representing the geometry
     */
    public void setGeometry(String geometry) throws IllegalArgumentException {
		int ro, co, xPos = 0, yPos = 0, xSz, ySz;
        int delim = geometry.indexOf('x');

        try {
            if(delim == -1)
                throw new Exception();

            int delX = geometry.indexOf('+');
            int delY = geometry.indexOf('-');

            if(delY != -1)
                delX = ((delX > delY || delX == -1) ? delY : delX);

            co = Integer.parseInt(geometry.substring(0, delim).trim());
            ro = Integer.parseInt(
                geometry.substring(delim + 1, (delX == -1 ? geometry.length() :
                                               delX)).trim());
            if(delX != -1) {
                delY = geometry.indexOf('+', delX + 1);
                if(delY == -1) {
                    delY = geometry.indexOf('-', delX + 1);
                    if(delY == -1)
                        throw new Exception();
                }

                xPos =Integer.parseInt(geometry.substring(delX+1,delY).trim());
                yPos =Integer.parseInt(geometry.substring(delY + 1).trim());
                savedGeomPos = geometry.substring(delX).trim();

            } else {
                savedGeomPos = "";
            }
        } catch(Exception e) {
            throw new IllegalArgumentException(
                "geometry must be '<cols>x<rows>[pos]', e.g. '80x24+0-0'");
        }

        if (terminal.setSize(ro, co)) {
            display.setPosition(xPos, yPos);
        }
    }

    /**
     * Set the font to use. The font should be a fixed-width font or
     * the terminal may behave strangely.
     *
     * @param name name of font
     * @param size size of font
     */
    public void setFont(String name, int size) {
        display.setFont(name, size);
    }

    /**
     * Set the font to use. The font should be a fixed-width font or
     * the terminal may behave strangely.
     *
     * @param font font to use
     */
    public void setFont(Font font) {
        display.setFont(font.getName(), font.getSize());
    }

    public void setTitle(String title) {
        if(title != null && !title.equals(this.title)) {
            this.title = title;
            signalWindowChanged(terminal.getRows(), terminal.getCols(),
                                vpixels, hpixels);
        }
    }

    public String getTitle() {
        return title;
    }

    /**
     * Set which mouse button should cause the popup menu to appear.
     *
     * @param buttonNum button number. One of InputEvent.BUTTON1_MASK,
     * InputEvent.BUTTON2_MASK or InputEvent.BUTTON3_MASK
     */
    public void setPopupButton(int buttonNum) {
        switch(buttonNum) {
            case 1:
                popupButton = InputEvent.BUTTON1_MASK;
                break;
            case 2:
                popupButton = InputEvent.BUTTON2_MASK;
                break;
            case 3:
                popupButton = InputEvent.BUTTON3_MASK;
                break;
            default:
                break;
        }
    }

    void updateScrollbarValues() {
        display.updateScrollbarValues();
    }

    /**
     * Return the panel containing the actual terminal window,
     * including the scrollbar.
     */
    public Container getPanelWithScrollbar() {
        container = display.getPanelWithScrollbar(getProperty("scrollbar"));
        if (virtualKeyboard != null) {
            container.add(virtualKeyboard, BorderLayout.SOUTH);
        }
        return container;
    }

    public Container getPanelWithScrollbarSWT(org.eclipse.swt.widgets.Composite composite) {
        container = display.getPanelWithScrollbarSWT(composite);
        if (virtualKeyboard != null) {
            container.add(virtualKeyboard, BorderLayout.SOUTH);
        }
        return container;
    }
    
    /**
     * Forgets any saved lines in the scrollback buffer.
     */
    public void clearSaveLines() {
        terminal.clearSaveLines();
    }

    /**
     * Set the charset used to input text.
     */
    public void setInputCharset(String charset)
        throws IllegalArgumentException{
        terminal.setInputCharset(charset);
    }

    public String terminalType() {
        return terminal.terminalType();
    }
    public int rows() {
        return terminal.getRows();
    }
    public int cols() {
        return terminal.getCols();
    }
    public int vpixels() {
        return vpixels;
    }
    public int hpixels() {
        return hpixels;
    }

    public void write(byte b) {
        if (filter != null) {
            write(filter.convertFrom(b));
        } else {
            write((char)b);
        }
    }

    public boolean write(char c) {
    	boolean retorno = false;
    	
        if (c == 0 && ignoreNull) {
            return retorno;
        }
        synchronized(writeLock) {
            if(outListeners != null) {
                int n = outListeners.size();
                for(int i = 0; i < n; i++) {
                    ConsoleOutputListener outListener =
                        (ConsoleOutputListener) outListeners.elementAt(i);
                    if(outListener != null) {
                        outListener.write(c);
                    }
                }
            }

            if(printerActive) {
                if(mc4[mc4MatchIdx] == c) {
                    mc4MatchIdx++;
                    mc4MatchIdx %= 4;
                } else {
                    if(mc4MatchIdx > 0) {
                        for(int i = 0; i < mc4MatchIdx; i++) {
                            printer.write(mc4[i]);
                        }
                        mc4MatchIdx = 0;
                    }
                    printer.write(c);
                }
            }

            if (terminal != null){
            	retorno = terminal.fromHost(c);
            }
            
        }
        return retorno;
    }

    public void write(char[] c, int off, int len) {
    	if(display!=null && display.getDynamicBuffer()!=null && !display.getDynamicBuffer().isClose()){
    		writeProcedure(c, off, len);
    	} else {
	        synchronized(writeLock) {
	        	StringBuilder localStringBuilder = new StringBuilder();
	            if (terminal != null)
	                terminal.setUpdate(false);
	            int end = off + len;
	            for(int i = off; i < end-1; i++){
	                write(c[i]);
	                if (c[i] != ConsoleInterpreter.IGNORE) {
	                	localStringBuilder.append(c[i]);
	                }
	            }
	            if (terminal != null)
	                terminal.setUpdate(true);
	            if (end > 0){
	                write(c[end-1]);

	                if (c[end-1] != ConsoleInterpreter.IGNORE) {
		                localStringBuilder.append(c[end-1]);
	                	logger.debug(localStringBuilder);

//	                	if ( (localStringBuilder!=null)&&(localStringBuilder.toString().contains("assword:")) ){
//	        	         	    final SshAuthenticationDialog dialog = new SshAuthenticationDialog(getHostname());
//	        		       		Display.getDefault().syncExec(new Runnable(){
//	        		   			public void run(){ 
//	        		         	    dialog.disableLDAPCheckBox();
//	        		         	    dialog.disableUsername();
//	        		         	    dialog.enableMethod();
//	        			        	dialog.openShell();
//	        		   				}
//	        		   			});
//	        	                
//	        		       		try {
//	        	    				Thread.sleep(300);
//	        	    			} catch (InterruptedException e) {
//	        	    				e.printStackTrace();
//	        	    			}
//	        	                
//	        	        	    if ( dialog.getCanceled() ){
//	        	        	    	String cmd = new String(new char[] {'\003'});
//	        	        	    	sendString(cmd+"\n");
//	        		                try {
//	        		    				Thread.sleep(300);
//	        		    			} catch (InterruptedException e) {
//	        		    				e.printStackTrace();
//	        		    			}
//	        	        	    } else {
//		        	        	    String password = dialog.getPassword(); 
//		        	        	    sendString(password+"\n");
//	        	        	    }
//	        	                try {
//	        	    				Thread.sleep(300);
//	        	    			} catch (InterruptedException e) {
//	        	    				e.printStackTrace();
//	        	    			}
//	        				}
	        			}  
	            }
	            localStringBuilder=null;
	        }
    	}
    }
    

    public void writeProcedure(char[] c, int off, int len) {
        synchronized(writeLock) {
            if (terminal != null)
                terminal.setUpdate(false);
            int end = off + len;
            byte[] b = {};
            for( int i = off; i < end-1; i++ ){
                 if( write(c[i]) ){
                    this.resposta+=c[i];
                 }
                 b = filter.convertTo(c[i]);
                 if( b.length >0 ){
	                 if(b.length>0 && ( b[0]==COMMAND_ENTER || b[0]==COMMAND_SPACE) ){
	                    this.resposta+=c[i];
	                    if(b[0]==COMMAND_ENTER)
	                       isEnter=true;                    
	                 }
                 }
            }
            if (terminal != null)
                terminal.setUpdate(true);
            if (end > 0){
            	if( c.length>0 ){
	            	if( write(c[end-1])){
	            		this.resposta+=c[end-1];
	                }
            	}
                b = filter.convertTo(c[end-1]);
                if(b.length>0 && ( b[0]==COMMAND_ENTER || b[0]==COMMAND_SPACE) ){
                   this.resposta+=c[end-1];
                   if(b[0]==COMMAND_ENTER)
                      isEnter=true;
                }
            }
            
//            if(display.getDynamicBuffer()!=null && !display.getDynamicBuffer().isClose()) {
//                if(channelEventListener==null)
//             	  channelEventListener = new ChannelEventAdapter();
//                
//                if ( isEnter ){
//			        if(channelEventListener.onDataReceived(display, this.resposta.getBytes())){
//			           resposta="";
//			           isEnter=false;
//			        }
//                }
//            }
        }
    }
    

    public void write(byte[] c, int off, int len) {
        if (filter != null) {
            write(filter.convertFrom(c, off, len));
        } else {
            writeInt(c, off, len);
        }
    }

    private void writeInt(byte[] c, int off, int len) {
        synchronized(writeLock) {
            if (terminal != null)
                terminal.setUpdate(false);
            int end = off + len;
            for(int i = off; i < end-1; i++)
                write(byte2char[(int)(c[i] & 0xff)]);
            if (terminal != null)
                terminal.setUpdate(true);
            if (end > 0)
                write(byte2char[(int)(c[end-1] & 0xff)]);
        }
    }

    public void write(String s) {
        char[] carr = s.toCharArray();
        write(carr, 0, carr.length);
    }

	public void addInputListener(ConsoleInputListener inListener) {
        if(inListeners == null) {
            inListeners = new Vector();
        }
        inListeners.removeElement(inListener);
        inListeners.addElement(inListener);
    }

    public void removeInputListener(ConsoleInputListener inListener) {
        if(inListeners != null) {
            inListeners.removeElement(inListener);
            if(inListeners.size() == 0) {
                inListeners = null;
            }
        }
    }

	public void addOutputListener(ConsoleOutputListener outListener) {
        if(outListeners == null) {
            outListeners = new Vector();
        }
        outListeners.removeElement(outListener);
        outListeners.addElement(outListener);

    }

    public void removeOutputListener(ConsoleOutputListener outListener) {
        if(outListeners != null) {
            outListeners.removeElement(outListener);
            if(outListeners.size() == 0) {
                outListeners = null;
            }
        }
    }

    public void attachPrinter(ConsolePrinter printer) {
        this.printer = printer;
    }

    public void detachPrinter() {
        this.printer = null;
    }

    /**
     * Set the clipboard handler to use in this terminal window.
     *
     * @param clipboard clipboard handler to use
     */
    public void setClipboard(ConsoleClipboardHandler clipboard) {
        this.clipboard = clipboard;
        String selection = terminal.getSelection(copyEol);
        if(selection != null) {
            clipboard.setSelection(selection);
        } else {
            clipboard.clearSelection();
        }
    }

    /**
     * Get the clipboard used.
     *
     * @return the current clipboard
     */
    public ConsoleClipboardHandler getClipboard() {
        return clipboard;
    }

    public void typedChar(char c) {
        if(DEBUG) {
            System.out.println("typedChar: "+c+"("+(int)c+")");
        }
        if (filter != null && !dumbMode) {
            byte[] b = filter.convertTo(c);
            if (b.length > 0) {
                typedCharInt(b);
            }
        } else {
            typedCharInt(c);
        }
    }

    public void typedCharInt(char c) {
        if(inListeners != null) {
            int n = inListeners.size();
            for(int i = 0; i < n; i++) {
                ConsoleInputListener inListener = (ConsoleInputListener)
                    inListeners.elementAt(i);
                if(inListener != null) {
                    inListener.typedChar(c);
                }
            }
        }
    }

    public void typedCharInt(byte[] b) {
        if(inListeners != null) {
            int n = inListeners.size();
            for(int i = 0; i < n; i++) {
                ConsoleInputListener inListener = (ConsoleInputListener)
                    inListeners.elementAt(i);
                if(inListener != null) {
                    inListener.typedChar(b);
                }
            }
        }
    }

    public final void sendBytes(byte[] b) {
        if(DEBUG) System.out.println("Sending " + b.length + " bytes: " + com.mxsecurity.util.HexDump.toString(b));
        if (filter != null && !dumbMode) {
            b = filter.convertTo(b);
            if (b.length > 0) {
                sendBytesInt(b);
            }
        } else {
            sendBytesInt(b);
        }
    }

    public final void sendBytesInt(byte[] b) {
        if(inListeners != null) {
            int n = inListeners.size();
            for(int i = 0; i < n; i++) {
                ConsoleInputListener inListener = (ConsoleInputListener)
                    inListeners.elementAt(i);
                if(inListener != null) {
                    inListener.sendBytes(b);
                }
            }
        }
    }

    public final void sendBreak() {
        if(DEBUG) System.out.println("Sending break");
        if(inListeners != null) {
            int n = inListeners.size();
            for(int i = 0; i < n; i++) {
                ConsoleInputListener inListener = (ConsoleInputListener)
                    inListeners.elementAt(i);
                if(inListener != null) {
                    inListener.sendBreak();
                }
            }
        }
    }

    /**
     * Tell all our input listeners that the terminal size has changed.
     *
     * @param rows new number of rows
     * @param cols new number of columns
     * @param vpixels new number of vertical pixels
     * @param hpixels new number of horizontal pixels
     */
    public void signalWindowChanged(int rows, int cols,
                                    int vpixels, int hpixels) {
        if(DEBUG) System.out.println("SIGWINCH: " + rows + ", " + cols);
        if(inListeners != null) {
            int n = inListeners.size();
            for(int i = 0; i < n; i++) {
                ConsoleInputListener inListener = (ConsoleInputListener)
                    inListeners.elementAt(i);
                if(inListener != null) {
                    inListener.signalWindowChanged(rows, cols,
                                                   vpixels, hpixels);
                }
            }
        }
    }

    public void reset() {
        terminal.reset();
    }
    public void printScreen() {
        if(DEBUG) System.out.println("printScreen");
        if(printer != null && termOptions[OPT_PASSTHRU_PRN]) {
            printer.printScreen();
        }
    }

    public void startPrinter() {
        if(DEBUG) System.out.println("startPrinter");
        if(printer != null && termOptions[OPT_PASSTHRU_PRN]) {
            printer.startPrinter();
            printerActive = true;
        }
    }

    public void stopPrinter() {
        if(DEBUG) System.out.println("stopPrinter");
        if(printer != null && termOptions[OPT_PASSTHRU_PRN]) {
            printerActive = false;
            printer.stopPrinter();
        }
    }

    public ConsoleSearchContext search(ConsoleSearchContext lastContext, String key,
                                boolean reverse, boolean caseSens) {
        return terminal.search(lastContext, key, reverse, caseSens);
    }

    private char keyKludgeFilter(char c, int keyCode, int mod) {
        //
        // The KeyEvent content seems to be confusing (to say the least...)
        // in some situations given different locale's and especially different
        // platforms... This is not very funny, but then again who said
        // anything about terminal-stuff beeing some kind of amusement...
        //

        int transC = (int)c;

        if (keyCode == KeyEvent.VK_SHIFT ||
            keyCode == KeyEvent.VK_CONTROL ||
            keyCode == KeyEvent.VK_CAPS_LOCK ||
            keyCode == KeyEvent.VK_ALT) {
            // !!! Swallow keypress for these, seems to contain key chars on
            // some combinations of JVM/OS/national keyboard
            return KeyEvent.CHAR_UNDEFINED;

        } else if ((mod & InputEvent.CTRL_MASK) != 0
                   && (mod & InputEvent.ALT_MASK) != 0) {
            // This happens when one press AltGr on windows (XP jre
            // 1.5). Unfortunately this means that it is not possible
            // to distinguish between Ctrl-AltGr-@ and AltGr-@ (on
            // Swedish keyboards) so it is not possible to generate
            // all control characters on those keyboards:-(

            // Is this really OK for 6530?
            return KeyEvent.CHAR_UNDEFINED;

        } else if ((mod & InputEvent.CTRL_MASK) != 0) {
            if (keyCode == KeyEvent.VK_M) {
                // Bug in MRJ sent 0x0a on ^M, should be ^J
                transC = 0x0d;
            } else if (keyCode == 0) {
                if (c == ' ' || c == '@') {
                    transC = 0x00;
                } else if (c == '[') {
                    transC = 0x1b;
                } else if (c == '\\') {
                    transC = 0x1c;
                } else if (c == ']') {
                    transC = 0x1d;
                } else if (c == '^' || c == '~' || c == '`') {
                    transC = 0x1e;
                } else if (c == '_' || c == '?') {
                    transC = 0x1f;
                } else if (c >= 'a' && c <= 'z') {
                    // Java 1.6 sends control combinations as the character
                    transC = c - 'a' + 1;
                }
            }
        } else {
            // We always send 0x0d ^M on ENTER no matter where we are...
            //
            if (transC == 0x0a && !ctrlKeyKludge) {
                transC = 0x0d;
            } else if(c == 65535) {
                // OUCH, JDK 1.2 generates this on the Shift and Caps keys(!)
                //
                transC = KeyEvent.CHAR_UNDEFINED;
            } else if(c == 65406) {
                // OUCH, IBM JDK 1.1.6 on Linux generates this on right alt
                // (alt_gr) (swedish-keyboard)
                //
                transC = KeyEvent.CHAR_UNDEFINED;
            }
        }

        return (char)transC;
    }

    /**
     * Resize the terminal during a drag operation. That is the size
     * is changed but the server is not notified.
     *
     * @param newRows new number of rows
     * @param newCols new number of columns
     */
    public void displayDragResize(int newRows, int newCols) {
        terminal.setSize(newRows, newCols);
    }

    /**
     * Claled when the terminal window has been resized. This will
     * cause the server to be notified of the new size.
     *
     * @param newRows new number of rows
     * @param newCols new number of columns
     * @param vpixels vertical pixesl
     * @param hpixels horizontal pixesl
     */
    public void displayResized(int newRows, int newCols,
                               int vpixels, int hpixels) {
        signalWindowChanged(newRows, newCols, vpixels, hpixels);
        String newGM = (newCols+"x"+newRows + savedGeomPos);
        propsChanged = true;
        props.put("geometry", newGM);
        //updateMenus();
    }

    /**
     * Select all text
     */
    public void selectAll() {
        terminal.selectAll();
        if(termOptions[OPT_COPY_SEL])
            doCopy();
    }

    /**
     * Removes the selection if there is one.
     */
    public void clearSelection() {
        if (!hasSelection) {
            return;
        }
        if (clipboard != null) {
            clipboard.clearSelection();
        }
        terminal.resetSelection();
        hasSelection = false;
    }

    /**
     * Get the current selection.
     *
     * @return the current selection
     */
    public String getSelection() {
        return terminal.getSelection(copyEol);
    }
   
    /**
     * Store a copy of the selection in the clipboard.
     */
    public void doCopy() {
        if(clipboard != null) {
            clipboard.setSelection(getSelection());
        }
    }

    /**
     * Paset the contents of the clipboard.
     */
    public void doPaste() {
        if(clipboard != null) {
            terminal.paste(clipboard.getSelection());
        }
    }

    /**
     * Enable/disable dumb mode. In dumb mode none of the cursor keys
     * work. This is needed when the user is prompted for a line of
     * input.
     *
     * @param dumb true if dumb mode should be enabled.
     */
    public void setDumbMode(boolean dumb) {
    	if(terminal!=null){
           terminal.setDumbMode(dumb);
    	}
        dumbMode = dumb;
    }

    /**
     * Set the frame to ignore window close events.
     */
    public void setIgnoreClose() {
        display.setIgnoreClose();
    }

    //
    // DisplayInputListener interface
    //
    /**
     * Handle a mouse click event
     *
     * @param visTop index of top visual line
     * @param row row of character the mouse was pointing at
     * @param col column of character the mouse was pointing at
     * @param modifier modifiers active
     */
    public void mouseClicked(int visTop, int row, int col, int modifier, int which) {
        // !!! TODO: Why is this if here ??? (avoid doing at "start-up"?)
        if(inListeners != null) {
            if(modifier == pasteButton) {
                doPaste();
            }
        }
        if((modifier & (InputEvent.BUTTON1_MASK |
                        InputEvent.SHIFT_MASK |
                        InputEvent.CTRL_MASK |
                        InputEvent.META_MASK)) == InputEvent.BUTTON1_MASK)
        {
            display.requestFocus();
        }
    }

    /**
     * Handle a mouse press event
     *
     * @param visTop index of top visual line
     * @param mouseRow row of character the mouse was pointing at
     * @param mouseCol column of character the mouse was pointing at
     * @param modifier modifiers active
     * @param x x coordinate of mouse
     * @param y y coordinate of mouse
     */
    public void mousePressed(int visTop, int mouseRow, int mouseCol,
                             int modifier, int which, int x, int y) {

        long now = System.currentTimeMillis();

        if (which == ComponentController.RIGHT_BUTTON && pasteButton != InputEvent.BUTTON3_MASK) {
            selectRowLast = mouseRow;
            selectColLast = mouseCol;
            terminal.setSelection(selectVisTop + selectRowAnchor, selectColAnchor, 
                                  visTop + selectRowLast, selectColLast);
            hasSelection = true;
            if (termOptions[OPT_COPY_SEL])
                doCopy();
        } else {
            clearSelection();
            selectRowAnchor = mouseRow;
            selectColAnchor = mouseCol;
            selectRowLast   = mouseRow;
            selectColLast   = mouseCol;
            selectVisTop    = visTop;
        }

        if ((now - lastLeftClick) < 250) {
            terminal.doClickSelect(visTop, mouseRow, mouseCol, selectDelims);
            hasSelection = true;
        } else {
            terminal.resetClickSelect();
        }
        lastLeftClick  = now;

        if(modifier == (popupButton | InputEvent.CTRL_MASK)) {
            // !!! Kludge, the ctrl-button upevent is caught elsewhere
            // (and we don't seem to get focusLost?!)
            ctrlKeyKludge = false;
//            if (menuHandler != null)
//                menuHandler.showPopupMenu(x, y);
        }

        terminal.mouseHandler(visTop, mouseRow, mouseCol, true, modifier);
    }

    /**
     * Handle a mouse release event
     *
     * @param visTop index of top visual line
     * @param mouseRow row of character the mouse was pointing at
     * @param mouseCol column of character the mouse was pointing at
     * @param modifier modifiers active
     */
    public void mouseReleased(int visTop, int mouseRow, int mouseCol,
                              int modifier, int which) {
        if ((modifier & (InputEvent.BUTTON1_MASK |
                         InputEvent.SHIFT_MASK |
                         InputEvent.CTRL_MASK |
                         InputEvent.META_MASK)) == InputEvent.BUTTON1_MASK)
        {
            if (hasSelection && termOptions[OPT_COPY_SEL]) {
                doCopy();
            }
        }

        terminal.mouseHandler(visTop, mouseRow, mouseCol, false, modifier);
    }

    /**
     * Handle a mouse dragged event
     *
     * @param visTop index of top visual line
     * @param mouseRow row of character the mouse was pointing at
     * @param mouseCol column of character the mouse was pointing at
     * @param modifier modifiers active
     */
    public void mouseDragged(int visTop, int mouseRow, int mouseCol,
                             int modifier, int which, int delta) {
        if (hasSelection && (selectRowLast == mouseRow &&
                             selectColLast == mouseCol) && (delta == 0))
            return;

        selectRowAnchor += delta;        
        selectRowLast = mouseRow;
        selectColLast = mouseCol;
        terminal.setSelection(visTop, selectRowAnchor, selectColAnchor,
                              selectRowLast, selectColLast);
        hasSelection = true;
    }

    public void scrollUp() {
        display.setVisTopDelta(-1, true);
    }

    public void scrollDown() {
        display.setVisTopDelta(1, true);
    }

    /**
     * Checks if a key event is a new event or if it is the same as
     * the last checked event.
     */
    private boolean isNewKeyEvent(KeyEvent e) {
        int ch = (int) e.getKeyChar();

        if (lastKeyWasEvent != e.getID()) {
            if (ch == lastKeyKludge) {
                return false;
            }
            // There may be more cases like this
            if ((lastKeyKludge == 0x0a && ch == 0x0d)
                || (lastKeyKludge == 0x0d && ch == 0x0a)
                || (lastKeyKludge == 0x08 && ch == 0x7f)) {
                return false;
            }
        }
        lastKeyKludge   = ch;
        lastKeyWasEvent = e.getID();
        return true;
    }

    /**
     * Simulate key events, just send the specified characters
     */
    public void sendString(String s) {
        if (DEBUG) {
            byte[] b = s.getBytes();
            logger.debug("Sending " + b.length + " bytes: " + com.mxsecurity.util.HexDump.toString(b));
        }
        char ca[] = s.toCharArray();
        for (int i=0; i<ca.length; i++){
            terminal.keyHandler(ca[i], 0, 0);
            if(ca.length-1==1){
               logger.debug("++++++++++++++++last keyHandler++++++++++++++++++++++++++");
            }
        }
    }

    /** 
     * Send a number of bytes.
     *
     * @param b Array of bytes to send. 
     */
    public synchronized String executeCommand(final String command) {
    	    byte[] bLastResult = getDisplay().getLastResult().getBytes();
    	    byte[] b20 = new byte[20];
    	    int count =0;
    	    
    	    String lastResult = "";
    	    
    	    if( bLastResult.length > 20 ){
	    	    for(int i=0; i < bLastResult.length;i++){
    	    		if ( (bLastResult.length-i) < 20 ) {
    	    		  b20[count] = (byte)bLastResult[i];
    	    		  count++;
    	    		}
	    	    }
	    	    lastResult = new String(b20,0,count);
	    	} else {
   	    	    lastResult = new String(bLastResult,0,bLastResult.length);
   	    	}
    	    
    	    //setProcedureRunning(true);
    	    String resultado = null;
            display.createDynamicBuffer();
            this.command = command;
            isCommand=true;
            this.resposta="";  
            isEnter=false;
                
            sendString(command);
	    	
            try {
				Thread.sleep(300);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			logger.debug("readDynamicBuffer : Start");
	    	resultado = readDynamicBuffer(command);  
			countPassword = 0;
//		    if ( (resultado!=null)&&(resultado.contains("assword:")) ){
//				while(resultado.contains("assword:")&&(countPassword<3)){
//					if(countPassword==2){
//	        	    	String cmd = new String(new char[] {'\003'});
//	        	    	sendString(cmd);
//		                try {
//		    				Thread.sleep(300);
//		    			} catch (InterruptedException e) {
//		    				e.printStackTrace();
//		    			}
//		    			logger.debug("readDynamicBuffer : Start");
//		    	    	return readDynamicBuffer(command);  
//					}
//					 
//	         	    final SshAuthenticationDialog dialog = new SshAuthenticationDialog(getHostname());
//		       		Display.getDefault().syncExec(new Runnable(){
//		   			public void run(){ 
//		   				countPassword++;
//		         	    dialog.disableLDAPCheckBox();
//		         	    dialog.disableUsername();
//		         	    dialog.enableMethod();
//			        	dialog.openShell();
//		   				}
//		   			});
//	        	    if ( dialog.getCanceled() ){
//	        	    	String cmd = new String(new char[] {'\003'});
//	        	    	sendString(cmd);
//		                try {
//		    				Thread.sleep(300);
//		    			} catch (InterruptedException e) {
//		    				e.printStackTrace();
//		    			}
//		    			logger.debug("readDynamicBuffer : Start");
//		    	    	return readDynamicBuffer(command);  
//	        	    }
//
//	        	    String password = dialog.getPassword(); 
//	        	    sendString(password+"\n");
//	        	    
//	                try {
//	    				Thread.sleep(300);
//	    			} catch (InterruptedException e) {
//	    				e.printStackTrace();
//	    			}
//	    			logger.debug("readDynamicBuffer : Start");
//	    	    	resultado = readDynamicBuffer(command);  
//				}
//			}  
	    	
	    	logger.debug("readDynamicBuffer Result : "+resultado);
    	    resposta=null;
    	    return resultado;
    } 
    
	public synchronized String readDynamicBuffer(String titulo){
		int read = 0;
		byte[] bRead = new byte[32768];
		int len =0;
		int offset = 0;

		String linha = null;
		String resultado = "";
		try { 
			logger.debug("readDynamicBuffer - "+titulo);
			while(true){
				read = display.getDynamicBuffer().getInputStream().read();
				if(read!=-1){
				   bRead[len] = (byte)read;
				   len++;
				}
		       	linha = new String(bRead,offset,len);
				switch (read) {
				case COMMAND_ENTER:
			       	linha = new String(bRead,offset,len);
				    resultado = resultado+linha;
				    logger.debug("readDynamicBuffer - COMMAND_ENTER - "+resultado);
			    	len=0;
					break;
				case COMMAND_FINISHED:
					logger.debug("readDynamicBuffer - COMMAND_FINISHED");
			       	linha = new String(bRead,offset,len);
			       	if( (linha.contains("assword:")) ){
			       		resultado = linha; 
			       	} else {
			       		resultado = resultado+linha;
			       	}
			    	len=0;
					break;
				default:
				}
				if(read==COMMAND_FINISHED){
				   break;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
        isCommand=true;

        logger.debug("BEFOR TOKEN RESULT="+resultado);
		if(resultado!=null && resultado.trim().length()>0){
		   StringTokenizer token = new StringTokenizer(resultado,"\r\n");
		   if(token.hasMoreTokens()){
			  token.nextToken();
			  if(token.hasMoreTokens()){
				 resultado=token.nextToken();
			  }
		   }
		   if(token.countTokens()<=2){
				if ( resultado.trim().endsWith("#")|| resultado.trim().contains(":websrvr]") ||
					 resultado.trim().endsWith("$")|| resultado.endsWith(">") ) {
					 resultado="";
				}
		   }
		}
		   
		return resultado; 
   }
	
//	public void notifyListeners(String message, int messageType){
//		if(listeners != null){
//			for(ChannelListener listener: listeners){
//				listener.receiveMessage(message, messageType);
//			}
//		}
//	}
//	
//	public void addListener(ChannelListener listener){
//		if(listeners == null){
//		   listeners = new ArrayList<ChannelListener>();
//		}
//		listeners.add(listener);
//	}
	
    
    
    /**
     * Process key events. This can get called from both keyPressed
     * and keyTyped.
     */
    private void processKey(KeyEvent e, boolean doreplace) {
        // Ignore duplicate events
        if (!isNewKeyEvent(e)) {
            return;
        }

        int    keyCode = e.getKeyCode();
        int    mod     = e.getModifiers();
        char   c       = e.getKeyChar();
        
        if (doreplace) {
            String r = (String)keymappings.get(keyCode + ":" + mod);
            if (r != null) {
                sendString(r);
                return;
            }
        }

        // Do special handling of some keys
        switch (keyCode) {
        case 54:
            if( System.getProperty("os.name").equals("Linux")) {        
          	   if ( mod == 1)
          	      c = '^';
          	   else
          		  c = '6';
          	   break;
             }
        case 128:
           if( System.getProperty("os.name").equals("Linux")) {        
        	   if ( mod == 1)
        	      c = '~';
        	   else
        		  c = '`';
        	   break;
           }
        case 129:
            if( System.getProperty("os.name").equals("Linux")) {        
         	   if ( mod == 1)
         	      c = '"';
         	   else
         		  c = '\'';
         	  break;
            }           
        case KeyEvent.VK_TAB:
        case KeyEvent.VK_F10: // Consume TAB so we do not lose focus
            e.consume();
            break;

        case KeyEvent.VK_INSERT: // Handle copy/paste
            if((mod & InputEvent.SHIFT_MASK) != 0) {
                doPaste();
                return;
            } else if((mod & InputEvent.CTRL_MASK) != 0) {
                doCopy();
                return;
            }
            break;

        case KeyEvent.VK_ALT:
            if(ctrlKeyKludge) {
                // !!! Seems that MS-lost sends ctrl+alt for right ALT !!!
                ctrlKeyKludge = false;
            } else {
                altKeyKludge = true;
            }
            return;

        case KeyEvent.VK_CONTROL:
            ctrlKeyKludge = true;
            return;

        case KeyEvent.VK_SHIFT:
        case KeyEvent.VK_CAPS_LOCK:
            // For some reason there seems to be characters in
            // keyevents with shift/caps, better filter them out
            return;
        }

        // Handle copy & paste on MacOSX
        if ((mod & InputEvent.META_MASK) != 0) {
            if (c == 'c') {
                doCopy();
                return;
            } else if (c == 'v') {
                doPaste();
                return;
            }
        }

        // The keyKludgeFilter fixes some key events
		int oc = (int)c;
        c = keyKludgeFilter(c, keyCode, mod);

        if (termOptions[OPT_KEY_KLUDGE]) {
            // Workaround for broken modifier mask on old MS JVM
            mod &= ~InputEvent.CTRL_MASK;
            if (ctrlKeyKludge) {
                mod |= InputEvent.CTRL_MASK;
            }
            mod &= ~InputEvent.ALT_MASK;
            if (altKeyKludge) {
                mod |= InputEvent.ALT_MASK;
            }
        }

        terminal.keyHandler(c, keyCode, mod);
    }

    private RandomSeed seed;
    public void addAsEntropyGenerator(RandomSeed seed) {
        this.seed = seed;
        seed.addEntropyGenerator(display.getAWTComponent());
    }

    public void setAttributeBold(boolean set) {
        terminal.setAttributeBold(set);
    }
    public void clearScreen() {
        terminal.clearScreen();
    }
    public void ringBell() {
        terminal.ringBell();
    }
    public void setCursorPos(int row, int col) {
        terminal.setCursorPos(row, col);
    }
    public void clearLine() {
        terminal.clearLine();
    }

    /**
     * This is notified when the window is closed and is used to tell
     * the display class to kill the repainter thread.
     */
    public void windowClosed(WindowEvent e) {
    	windowClosed();
    }
    
    public void windowClosed() {
        if (seed != null) {
            seed.removeEntropyGenerator(display.getAWTComponent());
            seed = null;
        }
        if(display!=null){
           display.delKeyListener(this);
           display.windowClosed();
           display = null;
        }
        if(terminal!=null){
           terminal.close();
        }
        terminal = null;
        virtualKeyboard = null;
        printer = null;
        clipboard = null;
        ownerFrame = null;
        inListeners = null;
        outListeners = null;
        //menuHandler = null;
    }    

    //
    // KeyListener interface
    //
	public void keyPressed(KeyEvent e) {
        int    virtKey = e.getKeyCode();
        char   c       = e.getKeyChar();

        if (DEBUGKEYEVENT) {
            System.out.println("keyPressed: keyCode=" + virtKey +
                               " keyChar=" + (int)c +
                               " mod=" + e.getModifiers() +
                               " keyText=" + e.getKeyText(virtKey));
            //System.out.println(e.paramString());
        }

        if (keymappings != null) {
            if (keymappings.get(virtKey + ":" + e.getModifiers()) != null) {
                processKey(e, true);
                return;
            }
        }

        // Only handle some events here. We assume we will get key
        // typed events for the rest.
        switch(e.getKeyCode()) {
        case KeyEvent.VK_INSERT:
        case KeyEvent.VK_HOME:
        case KeyEvent.VK_PAGE_UP:
        case KeyEvent.VK_PAGE_DOWN:
        case KeyEvent.VK_DELETE:
        case KeyEvent.VK_BACK_SPACE:
        case KeyEvent.VK_END:
        case KeyEvent.VK_UP:
        case KeyEvent.VK_DOWN:
        case KeyEvent.VK_LEFT:
        case KeyEvent.VK_RIGHT:
        case KeyEvent.VK_ESCAPE:
        case KeyEvent.VK_F1:
        case KeyEvent.VK_F2:
        case KeyEvent.VK_F3:
        case KeyEvent.VK_F4:
        case KeyEvent.VK_F5:
        case KeyEvent.VK_F6:
        case KeyEvent.VK_F7:
        case KeyEvent.VK_F8:
        case KeyEvent.VK_F9:
        case KeyEvent.VK_F10:
        case KeyEvent.VK_F11:
        case KeyEvent.VK_F12:
        case KeyEvent.VK_TAB:
        case KeyEvent.VK_CIRCUMFLEX:
        case 54:
        case 128:
        case 129:
            processKey(e, false);
        }
    }

	public void keyTyped(KeyEvent e) {
        char c = e.getKeyChar();

        if(c == ';'){ //verificar se o Terminal esta travado
        	//logger.debug("keyTyped: keyCode=" + e.getKeyCode() + " keyChar=" + (int)c +  " mod=" + e.getModifiers() +      " keyText=" + e.getKeyText(e.getKeyCode()));
        }
        if (DEBUGKEYEVENT) {
            System.out.println("keyTyped: keyCode=" + e.getKeyCode() +
                               " keyChar=" + (int)c +
                               " mod=" + e.getModifiers() +
                               " keyText=" + e.getKeyText(e.getKeyCode()));
            System.out.println(e.paramString());
        }

        processKey(e, false);
    }

    public void keyReleased(KeyEvent e) {
        int    virtKey = e.getKeyCode();
        int    mod     = e.getModifiers();

        if (DEBUGKEYEVENT) {
            System.out.println(e.paramString());
        }

        switch(virtKey) {
        case KeyEvent.VK_ALT:
            altKeyKludge = false;
            break;
        case KeyEvent.VK_CONTROL:
            ctrlKeyKludge = false;
            break;
        case 0x87 /*KeyEvent.VK_DEAD_DIAERESIS*/:
            // kludge for ~ on swedish keyboards
            if (mod != 0 && (mod & InputEvent.SHIFT_MASK) == 0)
                terminal.keyHandler('~', virtKey, mod);
            break;
        }
    }

	/**
	 * @return the filter
	 */
	public ConsoleCharsetFilter getFilter() {
		return filter;
	}

//	public boolean isProcedureRunning() {
//		return this.isProcedureRunning;
//	}  
    
	public void setProcedureRunning(boolean isProcedureRunning) {
		this.isProcedureRunning = isProcedureRunning;
	}

	public String getHostname() {
		return hostname;
	}

	public void setHostname(String hostname) {
		this.hostname = hostname;
	}
	
	
	
//	public String getResult(String resultado){
//		return getResult(resultado,false);
//	}
//	
//	public String getResult(String resultado, boolean onlyFirst){
//		String resultadoLocal = "";
//		
//		StringTokenizer token = new StringTokenizer(resultado,"\r\n");
//		
//		if(token.countTokens()<3){
//			return null;
//		}
//		
//		token.nextToken();
//		
//		while(token.hasMoreTokens()){
//			if(token.countTokens()==1){
//				break;
//			} else {		
//				if(onlyFirst){
//					resultadoLocal = token.nextToken();
//					break;
//				} else {
//					if(token.countTokens()>2){
//					   resultadoLocal += token.nextToken()+"\r\n"; 
//					} else {
//					   resultadoLocal += token.nextToken();
//					}
//				}
//			}
//		}
//		
//        try {
//		  Thread.sleep(500);
//	    } catch (InterruptedException e) {
//		  e.printStackTrace();
//	    }	
//		
//		
//	    log.debug("getResult = "+resultadoLocal);
//		return resultadoLocal.length()>0 ? resultadoLocal : null;
//	}
	
    
}
