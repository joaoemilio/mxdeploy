package com.mxterminal.console;

import java.io.IOException;
import java.io.OutputStream;

public final class ConsoleCapture implements ConsoleOutputListener {

    private OutputStream captureTarget;
    private ConsoleWin     terminal;

    public ConsoleCapture(OutputStream captureTarget) {
        this.captureTarget = captureTarget;
    }

    public void startCapture(ConsoleWin terminal) {
        this.terminal = terminal;
        terminal.addOutputListener(this);
    }

    public void endCapture() {
        terminal.removeOutputListener(this);
    }

    public OutputStream getTarget() {
        return captureTarget;
    }

    public void write(char c) {
        write(new byte[] { (byte)c }, 0, 1);
    }

    private void write(byte[] c, int off, int len) {
        try {
            captureTarget.write(c, off, len);
        } catch (IOException e) {
            // !!! TODO report this to someone...
        }
    }

}
