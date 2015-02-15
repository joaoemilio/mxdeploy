package com.mxterminal.console.component;

/**
 * Control interface for the display classes
 */
public interface ComponentController {
    public static int LEFT_BUTTON    = 0;
    public static int MIDDLE_BUTTON  = 1;
    public static int RIGHT_BUTTON   = 2;
    public static int UNKNOWN_BUTTON = -1;

    // Mouse events
    public void mouseClicked(int visTop, int row, int col, int modifier,
                             int which);
    public void mousePressed(int visTop, int row, int col, int modifier,
                             int which, int x, int y);
    public void mouseReleased(int visTop, int row, int col, int modifier,
                              int which);
    public void mouseDragged(int visTop, int row, int col, int modifier,
                             int which, int delta);

    public void scrollUp();
    public void scrollDown();

    // Display event
    public void displayDragResize(int newRows, int newCols);
    public void displayResized(int newRows, int newCols,
                               int vpixels, int hpixels);
}

