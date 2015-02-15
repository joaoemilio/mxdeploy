package com.mxterminal.console;

import java.util.Properties;

import com.mxterminal.console.component.ComponentView;

/**
 * Interface that defines a terminal emulator from the perspective
 * of a ConsoleWindow.
 */
public interface Console {

    final static public int GRAVITY_SOUTHWEST = 0;
    final static public int GRAVITY_NORTHWEST = 1;

    public String terminalType();
    public boolean setTerminalType(String type);
    public void setDumbMode(boolean dumb);

    public void reset();
    public void close();
    public int getRows();
    public int getCols();
    public void paste(String selection);
    public void keyHandler(char c, int virtualKey, int modifiers);
    public void mouseHandler(int visTop, int x, int y, boolean press,
                             int modifiers);
    public void setInputCharset(String charset);
    public boolean fromHost(char c);
    public void setUpdate(boolean enable);
    public boolean setSize(int rows, int cols);
    public boolean setSaveLines(int lines);
    public void clearSaveLines();

    public void doClickSelect(int visTop, int row, int col,
                              String selectDelims);
    public String getSelection(String eol);
    public void setSelection(int visTop, int row1, int col1,
                             int row2, int col2);
    public void setSelection(int row1, int col1,
                             int row2, int col2);
    public void selectAll();
    public void resetSelection();
    public void resetClickSelect();
    
    public void setTerminalWindow(ConsoleWindows termWin);
    public void setDisplay(ComponentView display);

    public void setProperties(Properties newProps);
    public boolean setProperty(String key, String value, boolean forceSet);
    public Properties getProperties();
    public String getProperty(String key);
    public boolean getPropsChanged();
    public void setPropsChanged(boolean value);
    public String getDefaultProperty(String key);
    public ConsoleOption[] getOptions();

    public ConsoleSearchContext search(ConsoleSearchContext lastContext, String key,
                                boolean reverse, boolean caseSens);

    // Special methods, needed by SSHInteractiveClient to make a pretty
    // user interface.

    public void setAttributeBold(boolean set);
    public void clearScreen();
    public void ringBell();
    public void setCursorPos(int row, int col);
    public void clearLine();
    public boolean getIgnoreChar(char c);    
}

