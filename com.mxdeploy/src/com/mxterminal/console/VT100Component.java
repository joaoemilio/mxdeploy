package com.mxterminal.console;

import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Properties;

import com.mxterminal.channel.ChannelEventAdapter;
import com.mxterminal.console.charset.ConsoleCharsetException;
import com.mxterminal.console.charset.ConsoleCharsetFactory;
import com.mxterminal.console.charset.ConsoleCharsetTranslator;
import com.mxterminal.console.component.ComponentModel;
import com.mxterminal.console.component.ComponentView;

public class VT100Component implements Console, CompatConsole,
    ComponentModel {

	public static final int COMMAND_ENTER = 10;
	public static final int COMMAND_SPACE = 13;
	ChannelEventAdapter channelEventListener = null;
	
    static private ConsoleOption optRevVideo =
        new ConsoleOption("rev-video", "Reverse Video", "false");
    static private ConsoleOption optAutoWrap =
        new ConsoleOption("autowrap", "Auto Wraparound", "true");
    static private ConsoleOption optRevAutoWrap =
        new ConsoleOption("rev-autowrap", "Reverse Wraparound", "false");
    static private ConsoleOption optInsertMode =
        new ConsoleOption("insert-mode", "Insert mode", "false");
    static private ConsoleOption optAutoLF =
        new ConsoleOption("auto-linefeed", "Auto Linefeed", "false");
    static private ConsoleOption optReposInput =
        new ConsoleOption("repos-input", "Scroll to Bottom On Key Press",
                           "true");
    static private ConsoleOption optRposOutput =
        new ConsoleOption("repos-output", "Scroll to Bottom On Tty Output",
                           "true");
    static private ConsoleOption optVisCursor =
        new ConsoleOption("visible-cursor", "Visible Cursor", "true");
    static private ConsoleOption optLocalEcho =
        new ConsoleOption("local-echo", "Local Echo", "false");
    static private ConsoleOption optVisBell =
        new ConsoleOption("visual-bell", "Visual Bell", "false");
    static private ConsoleOption optMapCtrlSpace =
        new ConsoleOption("map-ctrl-space", "Map <CTRL>+<SPC> To ^@", "true");
    static private ConsoleOption opt80x132Toggle =
        new ConsoleOption("80x132-toggle", "80/132 Columns", "false");
    static private ConsoleOption optLocalPgKeys =
        new ConsoleOption("local-pgkeys","Local PgUp/PgDown", "false");
    static private ConsoleOption optAsciiLine =
        new ConsoleOption("ascii-line","Use ASCII for line draw", "false");
    static private ConsoleOption optBackspaceSend =
        new ConsoleOption("backspace-send", "Backspace sends", "del", 
                           new String[] { "del", "bs", "erase" } );
    static private ConsoleOption optDeleteSend =
        new ConsoleOption("delete-send", "Delete sends", "bs",
                           new String[] { "del", "bs", "erase" } );

    static private ConsoleOption optionsDef[] = {
        optRevVideo,
        optAutoWrap,
        optRevAutoWrap,
        optInsertMode,
        optAutoLF,
        optReposInput,
        optRposOutput,
        optVisCursor,
        optLocalEcho,
        optVisBell,
        optMapCtrlSpace,
        optLocalPgKeys,
        optAsciiLine,
        optBackspaceSend,
        optDeleteSend,
    };

    private static String terminalTypes[] = {
        "xterm", "linux", "scoansi",  "att6386", "sun", "aixterm",
        "vt220", "vt100", "ansi",  "vt52", "xterm-color", "linux-lat",
        "at386", "vt320", "vt102"
                                            };

    private static int personalities[] = {
        XConsole.EMUL_XTERM,
        XConsole.EMUL_LINUX,
        XConsole.EMUL_SCOANSI,
        XConsole.EMUL_ATT6386,
        XConsole.EMUL_SUN,
        XConsole.EMUL_AIX,
        XConsole.EMUL_VT220,
        XConsole.EMUL_VT100,
        XConsole.EMUL_ANSI,
        XConsole.EMUL_VT52,
        XConsole.EMUL_XTERMCOL,
        XConsole.EMUL_LINUXLAT,
        XConsole.EMUL_AT386,
        XConsole.EMUL_VT102,
        XConsole.EMUL_VT320,
    };

    static private Properties defaultProperties;
    private Properties props;
    private boolean propsChanged;

    static {
        int i;
        defaultProperties = new Properties();

        for (i = 0; i < optionsDef.length; i++) {
            defaultProperties.put(optionsDef[i].getKey(),
                                  optionsDef[i].getDefault());
        }
    }

    private ComponentView display;
    private ConsoleScreen screen;
    private boolean termOptions[];
    private ConsoleInterpreter interpreter;
    private ConsoleCharsetTranslator translator;
    private ConsoleWindows termWin;

    private String bsString;
    private String delString;

    public static String[] getTerminalTypes() {
        return terminalTypes;
    }
    public static Console getTerminal(String type) {
        for (int i = 0; i < terminalTypes.length; i++) {
            if (terminalTypes[i].equals(type)) {
                ConsoleInterpreter tmp = new XConsole(personalities[i]);
                return new VT100Component(tmp);
            }
        }
        return null;
    }
    public static ConsoleOption[] getTerminalOptions() {
        return optionsDef;
    }

    public VT100Component(ConsoleInterpreter interpreter) {
        this.interpreter = interpreter;
        interpreter.setTerminal(this);
        termOptions = new boolean[OPT_LAST_OPT];
        screen = new ConsoleScreen(24, 80);
        props = new Properties(defaultProperties);
    }

    public String terminalType() {
        return interpreter.terminalType();
    }

    public boolean setTerminalType(String type) {
        for (int i = 0; i < terminalTypes.length; i++)
            if (terminalTypes[i].equals(type)) {
                XConsole tmp = (XConsole)interpreter;
                tmp.setTerminalType(personalities[i]);
                return true;
            }
        return false;
    }

    public void setDumbMode(boolean dumb) {
        interpreter.setDumbMode(dumb);
    }

    public void close() {
        display = null;
        if (interpreter != null)
            interpreter.setTerminal(null);
        interpreter = null;
        termWin = null;
        if (screen != null)
            screen.setDisplay(null);
        screen = null;
    }
    
    public void reset() {
        interpreter.vtReset();
    }
    public int getRows() {
        return screen.getRows();
    }
    public int getCols() {
        return screen.getCols();
    }

    private void repaint() {
        if (display != null && doupdate) {
            display.repaint();
        }
    }
    private void localPageCtrlKeys(int virtKey) {
        switch(virtKey) {
        case KeyEvent.VK_PAGE_UP:
            if (display != null) {
                display.setVisTopDelta(-screen.getRows(), true);
            }
            break;
        case KeyEvent.VK_PAGE_DOWN:
            if (display != null) {
                display.setVisTopDelta(screen.getRows(), true);
            }
            break;
        case KeyEvent.VK_HOME:
            if (display != null) {
                display.setVisTop(0, true);
            }
            break;
        case KeyEvent.VK_END:
            if (display != null) {
                display.setVisTop(screen.getVisTop(), true);
            }
            break;
        }
    }

    public void paste(String selection) {
        if (selection == null) {
            return;
        }

        char chars[] = selection.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            // This is a bit crude...
            keyHandler(chars[i], 0, 0);
        }
    }

    private void simulateKeys(String keys, int modifiers) {
        for (int i=0; i<keys.length(); i++) {
            keyHandler(keys.charAt(i), KeyEvent.VK_UNDEFINED, modifiers);
        }
    }

    public void keyHandler(char c, int virtualKey, int modifiers) {
        boolean keyProcessed = false;

        switch(virtualKey) {
        case KeyEvent.VK_BACK_SPACE:
            if (bsString.length() == 1) {
                c = bsString.charAt(0);
            } else {
                simulateKeys(bsString, modifiers);
                keyProcessed = true;
            }
            break;
        case KeyEvent.VK_DELETE:
            if (delString.length() == 1) {
                c = delString.charAt(0);
            } else {
                simulateKeys(delString, modifiers);
                keyProcessed = true;
            }
            break;
        case KeyEvent.VK_PAGE_UP:
        case KeyEvent.VK_PAGE_DOWN:
        case KeyEvent.VK_HOME:
        case KeyEvent.VK_END:
            if(((modifiers & InputEvent.SHIFT_MASK) != 0) ||
                    termOptions[OPT_LOCAL_PGKEYS]) {
                localPageCtrlKeys(virtualKey);
                keyProcessed = true;
            }
            break;

        case KeyEvent.VK_SPACE: // To do ctrl-space (for emacs of
                                // course)
            if((modifiers & InputEvent.CTRL_MASK) != 0 &&
                    termOptions[OPT_MAP_CTRLSP]) {
                c = (char) 0;
            }
            break;
        case KeyEvent.VK_PAUSE:
            termWin.sendBreak();
            keyProcessed = true;
            break;
        }
        if (keyProcessed) {
            return;
        }

        if(termOptions[OPT_LOCAL_ECHO]) {
            write(c);
        }

        // Reset window to bottom on keypress option
        if(termOptions[OPT_SCROLL_SK]) {
            if (display != null) {
                display.setVisTop(screen.getVisTop());
            }
        }

        interpreter.keyHandler(c, virtualKey, modifiers);
    }

    public void mouseHandler(int visTop, int x, int y, boolean press,
                             int modifiers) {
        if ((visTop + x) >= screen.getVisTop()) {
            // Don't forward mouse click in save lines
            interpreter.mouseHandler(x, y, press, modifiers);
        }
    }
    public void setInputCharset(String charset)
    throws IllegalArgumentException {
        try {
            this.translator = ConsoleCharsetFactory.create(charset);
        } catch (ConsoleCharsetException e) {
            throw new IllegalArgumentException(
                "unknown input-charset '" + charset + "'");
        }
    }

    private boolean doupdate = true;
    public void setUpdate(boolean enabled) {
        doupdate = enabled;
    }
    
    public boolean fromHost(char c) {
        int iic = interpreter.interpretChar(c);
        boolean retorno = false;
        if (iic != ConsoleInterpreter.IGNORE) {
            char ic;

            if (translator == null) {
                ic = (char)iic;
            } else {
                ic = translator.translate((char)iic);
            }
            screen.writeChar(ic);
            retorno = true;
        }
        if(display!=null){
		   display.setCursorReady(true);
        }
		
        repaint();
        sendMessage(c,retorno);

        return retorno;
    }
    
    public void sendMessage(char c, boolean tosend){
    	if(display.getDynamicBuffer()!=null && !display.getDynamicBuffer().isClose()){
            if(tosend) {
	        	try {
					display.getDynamicBuffer().getOutputStream().write(c);
				} catch (IOException e) {
					e.printStackTrace();
				}
            } else {
             	ConsoleWin terminalWin = (ConsoleWin)this.termWin;
     	    	byte[] b = {};
                 b = terminalWin.filter.convertTo(c);
                 if(b[0]==COMMAND_ENTER || b[0]==COMMAND_SPACE){
                 	try {
     					display.getDynamicBuffer().getOutputStream().write(c);
     				} catch (IOException e) {
     					e.printStackTrace();
     				}
                 }                	    	
             }   
        } 
    }
    
    public boolean getIgnoreChar(char c){
    	boolean ignore = true;
    	int iic = interpreter.interpretChar(c);
    	if (iic != ConsoleInterpreter.IGNORE) {
    		ignore = false;
    	}
    	return ignore;
    }
    
    public boolean setSize(int rows, int cols) {
        screen.resize(rows, cols);
        if (display != null) {
            display.setGeometry(getRows(), getCols());
        }
        return true;
    }
    public boolean setSaveLines(int lines) {
        return screen.setSaveLines(lines);
    }
    public void clearSaveLines() {
        screen.clearSaveLines();
    }

    public void doClickSelect(int visTop, int row, int col,
                              String selectDelims) {
        screen.doClickSelect(visTop + row, col, selectDelims);
    }
    public String getSelection(String eol) {
        return screen.getSelection(eol);
    }
    public void setSelection(int visTop, int row1, int col1, int row2,
                             int col2) {
        setSelection(visTop + row1, col1, visTop + row2, col2);
    }
    public void setSelection(int row1, int col1, int row2, int col2) {
        screen.setSelection(row1, col1, row2, col2);
    }
    public void selectAll() {
        screen.selectAll();
    }
    public void resetSelection() {
        screen.resetSelection();
    }
    public void resetClickSelect() {
        screen.resetClickSelect();
    }

    public void setTerminalWindow(ConsoleWindows termWin) {
        this.termWin = termWin;
    }
    
    public void setDisplay(ComponentView display) {
        this.display = display;
        screen.setDisplay(display);
        screen.clearSaveLines();
        screen.clearScreen();
        display.setModel(this);
        display.updateScrollbarValues();
    }

    public void setProperties(Properties newProps) {
        Enumeration e = newProps.keys();
        while(e.hasMoreElements()) {
            String name  = (String)e.nextElement();
            String value = newProps.getProperty(name);
            name = ConsoleDefProps.backwardCompatProp(name);
            setProperty(name, value);
        }
    }

    public boolean setProperty(String key, String value) {
        return setProperty(key, value, false);
    }

    public boolean setProperty(String key, String value, boolean forceSet) {
        boolean isEqual = false;
        String val = getProperty(key);
        boolean boolVal = Boolean.valueOf(value).booleanValue();

        if(val != null && val.equals(value)) {
            isEqual = true;
            if(!forceSet)
                return true;
        }

        if (optRevVideo.getKey().equals(key)) {
            if(!value.equals(val)) {
                if (display != null) {
                    display.reverseColors();
                }
            }
            termOptions[OPT_REV_VIDEO] = boolVal;
        } else if (key.equals(optAutoWrap.getKey())) {
            screen.setAutoWrap(boolVal);
            termOptions[OPT_AUTO_WRAP] = boolVal;
        } else if (key.equals(optRevAutoWrap.getKey())) {
            screen.setAutoReverseWrap(boolVal);
            termOptions[OPT_REV_WRAP] = boolVal;
        } else if (key.equals(optInsertMode.getKey())) {
            screen.setInsertMode(boolVal);
            termOptions[OPT_INSERTMODE] = boolVal;
        } else if (key.equals(optAutoLF.getKey())) {
            screen.setAutoLF(boolVal);
            termOptions[OPT_AUTO_LF] = boolVal;
        } else if (key.equals(optReposInput.getKey())) {
            termOptions[OPT_SCROLL_SK] = boolVal;
        } else if (key.equals(optRposOutput.getKey())) {
            if (display != null) {
                display.setVisTopChangeAllowed(boolVal);
            }
            termOptions[OPT_SCROLL_SI] = boolVal;
        } else if (key.equals(optVisCursor.getKey())) {
            if (display != null) {
                repaint();
            }
            termOptions[OPT_VIS_CURSOR] = boolVal;
        } else if (key.equals(optLocalEcho.getKey())) {
            termOptions[OPT_LOCAL_ECHO] = boolVal;
        } else if (key.equals(optVisBell.getKey())) {
            termOptions[OPT_VIS_BELL] = boolVal;
        } else if (key.equals(optMapCtrlSpace.getKey())) {
            termOptions[OPT_MAP_CTRLSP] = boolVal;
        } else if (key.equals(opt80x132Toggle.getKey())) {
            cursorSetPos(0, 0, false);
            setSize(screen.getRows(), boolVal ? 132 : 80);
            clearScreen();
        } else if (key.equals(optLocalPgKeys.getKey())) {
            termOptions[OPT_LOCAL_PGKEYS] = boolVal;
        } else if (key.equals(optAsciiLine.getKey())) {
            termOptions[OPT_ASCII_LDC] = boolVal;
        } else if (key.equals(optBackspaceSend.getKey())) {
            if (value.equalsIgnoreCase("del")) {
                bsString = "\177"; // 0x7f DEL
            } else if (value.equalsIgnoreCase("erase")) {
                bsString = "\033[3~"; // ^E[3~ vt220 erase
            } else { // BS
                bsString = "\010"; // 0x08 BS
            }
        } else if (key.equals(optDeleteSend.getKey())) {
            if (value.equalsIgnoreCase("bs")) {
                delString = "\010"; // 0x08 BS
            } else if (value.equalsIgnoreCase("erase")) {
                delString = "\033[3~"; // ^E[3~ vt220 erase
            } else { // DEL
                delString = "\177"; // 0x7f DEL
            }
        } else {
            return false;
        }
        props.put(key, value);

        if(!isEqual) {
            propsChanged = true;
        }
        return true;
    }
    public Properties getProperties() {
        return props;
    }
    public String getProperty(String key) {
        return props.getProperty(key);
    }
    public boolean getPropsChanged() {
        return propsChanged;
    }
    public void setPropsChanged(boolean value) {
        propsChanged = value;
    }
    public String getDefaultProperty(String key) {
        return defaultProperties.getProperty(key);
    }
    public ConsoleOption[] getOptions() {
        return optionsDef;
    }

    public void setAttributeBold(boolean set
                                    ) {
        screen.setAttribute(ComponentModel.ATTR_BOLD, set
                               );
    }
    public void ringBell() {
        doBell();
    }
    public void setCursorPos(int row, int col) {
        screen.cursorSetPos(row, col, false);
    }

    // DisplayModel interface
    public char[] getChars(int visTop, int row) {
        return screen.getCharsAt(visTop + row);
    }
    public int[] getAttribs(int visTop, int row) {
        return screen.getAttribsAt(visTop + row);
    }
    public int getDisplayRows() {
        return screen.getRows();
    }
    public int getDisplayCols() {
        return screen.getCols();
    }
    public int getBufferRows() {
        return screen.getVisTop() + screen.getRows();
    }

    // CompatTerminal interface
    // Compability layer so I don't have to rewrite the XTerm interpreter
    public void setTitle(String title) {
        if (termWin != null) {
            termWin.setTitle(title);
        }
    }
    public int rows() {
        return getRows();
    }
    public int cols() {
        return getCols();
    }

    public void fillScreen(char c) {
        screen.fillScreen(c);
        repaint();
    }

    public void write(char c) {
        if (termWin != null) {
            termWin.write(c);
        }
    }
    public void write(char[] c, int off, int len) {
        if (termWin != null) {
            termWin.write(c, off, len);
        }
    }
    public void write(String str) {
        if (termWin != null) {
            termWin.write(str);
        }
    }
    public void writeLineDrawChar(char c) {
        screen.writeLineDrawChar(c);
        repaint();
    }

    public void typedChar(char c) {
        if (termWin != null) {
            termWin.typedChar(c);
        }
    }
    public void sendBytes(byte[] b) {
        if (termWin != null) {
            termWin.sendBytes(b);
        }
    }

    public void doBell() {
        if (display != null) {
            display.doBell(termOptions[OPT_VIS_BELL]);
        }
    }
    public void doBS() {
        screen.doBS();
        repaint();
    }
    public void doTab() {
        screen.doTab();
        repaint();
    }
    public void doTabs(int n) {
        screen.doTabs(n);
        repaint();
    }
    public void doBackTabs(int n) {
        screen.doBackTabs(n);
        repaint();
    }
    public void setTab(int col) {
        screen.setTab(col);
        repaint();
    }
    public void clearTab(int col) {
        screen.clearTab(col);
        repaint();
    }
    public void resetTabs() {
        screen.resetTabs();
        repaint();
    }
    public void clearAllTabs() {
        screen.clearAllTabs();
        repaint();
    }
    public void doCR() {
        screen.doCR();
        repaint();
    }
    public void doLF() {
        screen.doLF();
        repaint();
    }

    public void resetInterpreter() {
        interpreter.vtReset();
    }
    public void resetWindow() {
        screen.resetWindow();
        repaint();
    }
    public void setWindow(int top, int bottom) {
        screen.setWindow(top, bottom);
    }
    public void setWindow(int top, int right, int bottom, int left) {
        screen.setWindow(top, right, bottom, left);
        repaint();
    }
    public int  getWindowTop() {
        return screen.getWindowTop();
    }
    public int  getWindowBottom() {
        return screen.getWindowBottom();
    }
    public int  getWindowLeft() {
        return screen.getWindowLeft();
    }
    public int  getWindowRight() {
        return screen.getWindowRight();
    }

    public int getCursorV() {
        return screen.getCursorV();
    }
    public int getCursorH() {
        return screen.getCursorH();
    }

    public void cursorSetPos(int v, int h, boolean relative) {
        screen.cursorSetPos(v, h, relative);
        repaint();
    }
    public void cursorUp(int n) {
        screen.cursorUp(n);
        repaint();
    }
    public void cursorDown(int n) {
        screen.cursorDown(n);
        repaint();
    }
    public void cursorForward(int n) {
        screen.cursorForward(n);
        repaint();
    }
    public void cursorBackward(int n) {
        screen.cursorBackward(n);
        repaint();
    }
    public void cursorIndex(int n) {
        screen.cursorIndex(n);
        repaint();
    }
    public void cursorIndexRev(int n) {
        screen.cursorIndexRev(n);
        repaint();
    }

    public void cursorSave() {
        screen.cursorSave();
        repaint();
    }
    public void cursorRestore() {
        screen.cursorRestore();
        repaint();
    }

    public void scrollUp(int n) {
        screen.scrollUp(n);
        repaint();
    }
    public void scrollDown(int n) {
        screen.scrollDown(n);
        repaint();
    }

    public void clearBelow() {
        screen.clearBelow();
        repaint();
    }
    public void clearAbove() {
        screen.clearAbove();
        repaint();
    }
    public void clearScreen() {
        screen.clearScreen();
        repaint();
    }
    public void clearRight() {
        screen.clearRight();
        repaint();
    }
    public void clearLeft() {
        screen.clearLeft();
        repaint();
    }
    public void clearLine() {
        screen.clearLine();
        repaint();
    }

    public void eraseChars(int n) {
        screen.eraseChars(n);
        repaint();
    }
    public void insertChars(int n) {
        screen.insertChars(n);
        repaint();
    }
    public void insertLines(int n) {
        screen.insertLines(n);
        repaint();
    }
    public void deleteChars(int n) {
        screen.deleteChars(n);
        repaint();
    }
    public void deleteLines(int n) {
        screen.deleteLines(n);
        repaint();
    }

    public void printScreen() {
        if (termWin != null) {
            termWin.printScreen();
        }
    }
    public void startPrinter() {
        if (termWin != null) {
            termWin.startPrinter();
        }
    }
    public void stopPrinter() {
        if (termWin != null) {
            termWin.stopPrinter();
        }
    }

    private String mapToOptionKey(int opt) {
        String key = null;

        switch (opt) {
        case CompatConsole.OPT_REV_VIDEO:
            key = optRevVideo.getKey();
            break;
        case CompatConsole.OPT_AUTO_WRAP:
            key = optAutoWrap.getKey();
            break;
        case CompatConsole.OPT_REV_WRAP:
            key = optRevAutoWrap.getKey();
            break;
        case CompatConsole.OPT_INSERTMODE:
            key = optInsertMode.getKey();
            break;
        case CompatConsole.OPT_AUTO_LF:
            key = optAutoLF.getKey();
            break;
        case CompatConsole.OPT_SCROLL_SK:
            key = optReposInput.getKey();
            break;
        case CompatConsole.OPT_SCROLL_SI:
            key = optRposOutput.getKey();
            break;
        case CompatConsole.OPT_VIS_CURSOR:
            key = optVisCursor.getKey();
            break;
        case CompatConsole.OPT_LOCAL_ECHO:
            key = optLocalEcho.getKey();
            break;
        case CompatConsole.OPT_VIS_BELL:
            key = optVisBell.getKey();
            break;
        case CompatConsole.OPT_MAP_CTRLSP:
            key = optMapCtrlSpace.getKey();
            break;
        case CompatConsole.OPT_DECCOLM:
            key = opt80x132Toggle.getKey();
            break;
        case CompatConsole.OPT_LOCAL_PGKEYS:
            key = optLocalPgKeys.getKey();
            break;
        case CompatConsole.OPT_ASCII_LDC:
            key = optAsciiLine.getKey();
            break;
        }
        return key;
    }

    public void setOption(int opt, boolean value) {
        String val = String.valueOf(value);
        String key = mapToOptionKey(opt);
        if (key == null) {
            return;
        }
        setProperty(key, val);
    }
    public boolean getOption(int opt) {
        String key = mapToOptionKey(opt);
        if (key == null) {
            return false;
        }
        return Boolean.valueOf(getProperty(key)).booleanValue();
    }

    public ConsoleSearchContext search(ConsoleSearchContext lastContext, String key,
                                boolean reverse, boolean caseSens) {
        ConsoleSearchContext result = screen.search(lastContext, key,
                                             reverse, caseSens);
        if (result != null) {
            display.setVisTop(result.getStartRow());
            screen.setSelection(result.getStartRow(), result.getStartCol(),
                                result.getEndRow(), result.getEndCol());
            // Might set the selection when the terminal window is out
            // of focus, so better do a forcible repaint
            display.repaint(true);
        }
        return result;
    }

    public void setAttribute(int attr, boolean val) {
        screen.setAttribute(attr, val);
    }
    public boolean getAttribute(int attr) {
        return screen.getAttribute(attr);
    }
    public void setForegroundColor(int c) {
        //screen.setForegroundColor(c);
    }
    public void setBackgroundColor(int c) {
        screen.setBackgroundColor(c);
    }
    public void clearAllAttributes() {
        screen.clearAllAttributes();
    }
}


