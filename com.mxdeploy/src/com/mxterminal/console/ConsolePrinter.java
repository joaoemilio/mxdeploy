package com.mxterminal.console;

/**
 * Interface for printers which may be attached to terminals.
 */
public interface ConsolePrinter extends ConsoleOutputListener {
    /**
     * Print a dump of the current screen.
     */
    public void printScreen();

    /**
     * Start printing everything which is shown on the terminal from
     * now on.
     */
    public void startPrinter();

    /**
     * Stop dumping data to the printer.
     */
    public void stopPrinter();
}
