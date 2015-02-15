package com.mxterminal.console;

/**
 * Interface describing a clipboard for the terminal emulator.
 *
 * @see ConsoleWin
 */
public interface ConsoleClipboardHandler {
    /**
     * Set the content of the clipboard to the specified string.
     *
     * @param selection new contents of the clipboard.
     */
    public void   setSelection(String selection);

    /**
     * Get the current contents of the clipboard
     *
     * @return the current clipboard contents.
     */
    public String getSelection();

    /**
     * Clear the clipboard contents.
     */
    public void   clearSelection();
}
