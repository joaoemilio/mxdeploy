package com.mxterminal.console;

public abstract class ConsoleInterpreter {

    protected CompatConsole term;

    public final static int IGNORE = -1;

    abstract public String terminalType();
    abstract public int interpretChar(char c);

    public void vtReset() {}

    public void keyHandler(char c, int virtualKey, int modifiers) {
        if (term != null) {
            term.typedChar(c);
        }
    }
    public void mouseHandler(int x, int y, boolean press, int modifiers) {}

    public final void setTerminal(CompatConsole term) {
        this.term = term;
    }

    public void setDumbMode(boolean dumb) {}
}
