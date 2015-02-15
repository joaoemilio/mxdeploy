package com.mxterminal.console;

/**
 * Holds the context of a search, so that a new search can be started
 * from the last position
 */
public class ConsoleSearchContext {
    private int startRow;
    private int startCol;
    private int endRow;
    private int endCol;

    public ConsoleSearchContext(int startRow, int startCol, int endRow, int endCol) {
        this.startRow = startRow;
        this.startCol = startCol;
        this.endRow = endRow;
        this.endCol = endCol;
    }

    public int getStartRow() {
        return startRow;
    }
    public int getStartCol() {
        return startCol;
    }
    public int getEndRow() {
        return endRow;
    }
    public int getEndCol() {
        return endCol;
    }

    public String toString() {
        return getStartRow() + "," + getStartCol() + " - " +
               getEndRow() + "," + getEndCol();
    }
}


