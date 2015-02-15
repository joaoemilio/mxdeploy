package com.mxterminal.console.component;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Panel;
import java.awt.Rectangle;
import java.awt.Scrollbar;
import java.awt.Toolkit;
import java.awt.event.ActionListener;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelListener;

import org.apache.log4j.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.ScrollBar;

import com.mxterminal.channel.ChannelEventAdapter;
import com.mxterminal.console.ConsoleFocusKludge;
import com.mxterminal.util.io.DynamicBuffer;



public class ComponentAWT extends Canvas
    implements ComponentView, AdjustmentListener, MouseListener,
    MouseMotionListener, ComponentListener, FocusListener {
 
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	static Logger logger = Logger.getLogger(ComponentAWT.class);
	
	//private static Log log = LogFactory.getLog(ComponentAWT.classf);
	
	private Boolean cursorReady = new Boolean(false);
    private DynamicBuffer dynamic = null;
    private ChannelEventAdapter channelEventAdapter=new ChannelEventAdapter();
    boolean pendingShow = true;
    
    boolean visTopChangeAllowed = true;

    final static int REPAINT_SLEEP = 70; // ms

    final static boolean DEBUG         = false;

    final static public int MIN_ROWS = 2;
    final static public int MIN_COLS = 8;
    final static public int MAX_COLS = 512;
    final static public int MAX_ROWS = 512;

    private Scrollbar scrollbar;
    private ScrollBar scrollBarSWT=null; 
    
    private boolean   haveScrollbar;
    private volatile boolean updateScrollbar = false;

    private Panel     myPanel;
    private Frame     ownerFrame;
    private org.eclipse.swt.widgets.Composite composite=null;

    private boolean logoDraw;
    //private Image   logoImg;
    private int     logoX;
    private int     logoY;
    private int     logoW;
    private int     logoH;
    private int     centerLogoX;
    private int     centerLogoY;

    private boolean isDirty = false;
    private int     dirtyTop;
    private int     dirtyBottom;
    private int     dirtyLeft;
    private int     dirtyRight;

    private int rows; // Number of displayed rows
    private int cols; // Number of displayed columns
    private int vpixels;
    private int hpixels;
    private int borderWidth  = 2;
    private int borderHeight = 2;
    private int xPos; // Position of display windows on screen
    private int yPos; // Position of display windows on screen

    private int charWidth;
    private int charHeight;
    private int baselineIndex;
    private int lineSpaceDelta;

    private boolean cursorHollow = false;
    private boolean hasCursor = false;
    private int curRow;
    private int curCol;

    private int     lastKeyKludge     = 0;
    private boolean lastKeyWasPressed = false;

    private Color origBgColor;
    private Color origFgColor;
    private Color cursorColor;

    private int visTop;         // The buffer row number of the top displayed row
    private boolean visTopChangePending = false; // Should vis top change on resize
    private int visTopChange = 0; // New visTop value

    private Image     memImage;
    private Graphics  memGraphics;
    private Dimension memImageSize;

    private Font plainFont;
    private Font boldFont;

    private ComponentModel model;
    private ComponentController controller;

    private boolean hasSelection = false;
    private int selectionTopRow;
    private int selectionTopCol;
    private int selectionBottomRow;
    private int selectionBottomCol;
    
    private int workFlowSP1 = 0;
    private String lastResult = "";  



    /** This class collapses repaint requests.
      This thread class sleeps for a couple of milli-sec, wakes up to see if
     * repainting is needed (and repaints if that is the case) and then
     * go to sleep again. The idea is that a Canvas instance is a
     * heavy-weight object and a call to it's repaint method will be
     * executed directly, and not put on an event queue.
     */
    private class Repainter extends Thread {
        protected int sleepTime;
        protected boolean repaintRequested;
        protected boolean hasSlept;

        Repainter(int sleepTime) {
        	
            super("ComponentAWT.repainter");
            //logger.debug("Repainter");
            
            this.sleepTime = sleepTime;
            repaintRequested = false;
            hasSlept = false;

            synchronized (this) {
                start();
                try {
                    this.wait();
                } catch (InterruptedException e) {}
            }
        }

        public void run() {
        	
        	//logger.debug("run()");
           synchronized (this) {
                this.notify();
           }

            while (ownerFrame != null) {
                try {
                    synchronized (this) {
                        this.wait(sleepTime);
                        if (repaintRequested) {
                            doRepaint();
                            repaintRequested = false;
                            hasSlept = false;
                        } else {
                            hasSlept = true;
                        }
                    }
                } catch (InterruptedException e) {
                	logger.debug("Repainter is interrupted!");
                }
            }
        }

        synchronized void repaint(boolean force) {
        	//logger.debug("repaint");
            repaintRequested = true;
            if (force || hasSlept) {
                synchronized (this) {
                    this.notify();
                }
            }
        }
    }

    private Repainter repainter;


    public ComponentAWT(Frame ownerFrame) {
        super();

        scrollbar     = null;
        scrollBarSWT = null;
        haveScrollbar = false;
        visTop        = 0;

        isDirty = false;

        this.ownerFrame  = ownerFrame;

        // !!! We don't receive the proper component-events on the Canvas IMHO?
        //
        ownerFrame.addComponentListener(this);

        // !!! Ok, in spite of all our efforts here, we seem to need this
        // for certain situations, I give up once again...
        //
        // ownerFrame.addKeyListener(this);

        addComponentListener(this);
        addFocusListener(this);
        addMouseMotionListener(this);
        addMouseListener(this);

        try {
            /*
             * This is put in separate file to be able to use without reflect
             * package since otherwise we can't link this class in some cases.
             */
            new ConsoleFocusKludge(ownerFrame);
            new ConsoleFocusKludge(this);
        } catch (Throwable t) {
            // only needed/available in jdk1.4
        }

        repainter = new Repainter(REPAINT_SLEEP);
    }

    public void setModel(ComponentModel model) {
        this.model = model;
    }
    public ComponentModel getModel() {
        return model;
    }
    public void setController(ComponentController controller) {
        this.controller = controller;
    }

    public void setKeyListener(KeyListener keyListener) {
        addKeyListener(keyListener);
    }

    public void delKeyListener(KeyListener keyListener) {
        removeKeyListener(keyListener);
    }


    private boolean isInsideSelection(int row, int col) {
        if (!hasSelection) {
            return false;
        }
        if (row < selectionTopRow || row > selectionBottomRow) {
            return false;
        }
        if (row == selectionTopRow && col < selectionTopCol) {
            return false;
        }
        if (row == selectionBottomRow && col > selectionBottomCol) {
            return false;
        }
        return true;
    }

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

    public void setFont(String name, int size) {
        plainFont = new Font(name, Font.PLAIN, size);
        boldFont  = new Font(name, Font.BOLD, size);

        super.setFont(plainFont);
        calculateCharSize();

        if(isShowing()) {
            setGeometry(rows, cols);
        }
    }

    public void setLineSpaceDelta(int delta) {
        lineSpaceDelta = delta;
    }

    public void setFont(Font font) {
        setFont(font.getName(), font.getSize());
    }

    public void setVisTopChangeAllowed(boolean set) {
        visTopChangeAllowed = set;
    }
    public void setVisTopDelta(int delta) {
        setVisTopDelta(delta, visTopChangeAllowed);
    }
    public void setVisTopDelta(int delta, boolean changeAllowed) {
        setVisTop(visTop + delta, changeAllowed);
    }
    public void setVisTop(int visTop) {
        setVisTop(visTop, visTopChangeAllowed);
    }
    public void setVisTop(int visTop, boolean changeAllowed) {
        if (model == null) {
            return;
        }
        visTopChangePending = false;
        visTop = fenceVisTop(visTop);
        if (this.visTop != visTop) {
            if (changeAllowed) {
                this.visTop = visTop;
                repaint();
                updateScrollbarValues();
            }
        }
    }
    public void setPendingVisTopChange(int visTop) {
        visTopChangePending = true;
        visTopChange = visTop;
    }
    private int fenceVisTop(int visTop) {
        int min = 0;
        int max = model.getBufferRows() - rows;
        if (visTop < min) {
            visTop = min;
        }
        if (visTop > max) {
            visTop = max;
        }
        return visTop;
    }

    public void updateScrollbarValues() {
        if (model == null)
            return;
        if (haveScrollbar || scrollBarSWT!=null)
            updateScrollbar = true;
    }
    
    public Container getPanelWithScrollbar(String scrollPos) {
        if(myPanel != null)
            return myPanel;

        scrollbar = new Scrollbar(Scrollbar.VERTICAL);
        updateScrollbarValues();
        scrollbar.addAdjustmentListener(this);

        myPanel = new Panel(new BorderLayout());
        myPanel.add(this, BorderLayout.CENTER);
        if(scrollPos.equals("left")) {
            myPanel.add(scrollbar, BorderLayout.WEST);
            haveScrollbar = true;
        } else if(scrollPos.equals("right")) {
            myPanel.add(scrollbar, BorderLayout.EAST);
            haveScrollbar = true;
        } else {
            haveScrollbar = false; // No scrollbar
        }

        return myPanel;
    }

    public Container getPanelWithScrollbarSWT(final org.eclipse.swt.widgets.Composite composite) {
        if(myPanel != null)
            return myPanel;

        myPanel = new Panel(new BorderLayout());
        myPanel.add(this, BorderLayout.CENTER);
        haveScrollbar = false; // No scrollbar
        scrollBarSWT = composite.getVerticalBar();
        scrollBarSWT.addListener(SWT.Selection, new Listener() {
		      public void handleEvent(Event e) {
			        visTop = scrollBarSWT.getSelection();
			        updateScrollbarValues();
			        repaint();
		      }
		    });		

        myPanel.addMouseWheelListener(new MouseWheelListener(){
    		public void mouseWheelMoved(final java.awt.event.MouseWheelEvent e) {
    			if (e.getScrollType() != java.awt.event.MouseWheelEvent.WHEEL_UNIT_SCROLL)
    				return;
    			    	Display.getDefault().syncExec(new Runnable(){
    	    				public void run(){  
    	    					if (scrollBarSWT != null) {
    	    						int move = e.getWheelRotation() * scrollBarSWT.getIncrement();
    	    						scrollBarSWT.setSelection(scrollBarSWT.getSelection() + move);
    	    						scrollBarSWT.notifyListeners(SWT.Selection, new Event());
    	    					}
    	    				}
    	    			});     					
    		}
        });        

        return myPanel;
    }
    
    
    public void moveScrollbar(String scrollPos) {
        if(myPanel == null) {
            return;
        }
        if(scrollPos.equals("left") || scrollPos.equals("right")) {
            if(scrollbar != null) {
                myPanel.remove(scrollbar);
                if(scrollPos.equals("right")) {
                    myPanel.add(scrollbar, BorderLayout.EAST);
                } else {
                    myPanel.add(scrollbar, BorderLayout.WEST);
                }
                ownerFrame.pack();
                requestFocus();
                haveScrollbar = true;
            }
        } else if(scrollPos.equals("none")) {
            if(scrollbar != null) {
                myPanel.remove(scrollbar);
            }
            ownerFrame.pack();
            requestFocus();
            haveScrollbar = false;
        }
    }

    private synchronized final void makeAllDirty() {
        // Reset dirty area since it can be larger than display after
        // a resize
        dirtyTop = 0;
        dirtyLeft = 0;
        dirtyBottom = rows;
        dirtyRight = cols;
    }

    private final void makeCursorDirty() {
        makeAreaDirty(curRow, curCol, curRow+1, curCol+1);
    }

    private final void makeSelectionDirty() {
        int dirtyTop, dirtyLeft, dirtyBottom, dirtyRight;
        dirtyTop = selectionTopRow;
        dirtyBottom = selectionBottomRow;
        if (dirtyTop != dirtyBottom) {
            dirtyLeft = 0;
            dirtyRight = cols;
        } else {
            if (selectionTopCol < selectionBottomCol) {
                dirtyLeft = selectionTopCol;
                dirtyRight = selectionBottomCol;
            } else {
                dirtyRight = selectionTopCol;
                dirtyLeft = selectionBottomCol;
            }
        }
        makeAreaDirty(dirtyTop, dirtyLeft, dirtyBottom+1, dirtyRight+1);
    }

    public void updateDirtyArea(int top, int left, int bottom, int right) {
        makeAreaDirty(top, left, bottom, right);
        //System.out.println("Dirty: ("+top+","+left+","+bottom+","+right+")");
    }
    // input is buffer coordinates, dirty is screen coordinates
    private synchronized final void makeAreaDirty(int top, int left,
            int bottom, int right) {
        if (bottom < visTop || top > (visTop + rows)) {
            // Dirt outside visible area, ignore
            return;
        }

        // Translate to screen coordinates
        top = top - visTop;
        bottom = bottom - visTop;

        if (!isDirty) {
            //System.out.print("No dirt. ");
            dirtyTop = top;
            dirtyBottom = bottom;
            dirtyLeft = left;
            dirtyRight = right;
            isDirty = true;
        } else {
            //System.out.print("Old dirt=("+dirtyTop+","+dirtyLeft+")("+dirtyBottom+","+dirtyRight+"). ");
            // Grow dirty area to include all dirty spots on screen
            if(top < dirtyTop) {
                dirtyTop = top;
            }
            if(bottom > dirtyBottom) {
                dirtyBottom = bottom;
            }
            if(left < dirtyLeft) {
                dirtyLeft = left;
            }
            if(right > dirtyRight) {
                dirtyRight = right;
            }
            if (dirtyTop == dirtyBottom) {
                dirtyBottom++;
            }
            if (dirtyLeft == dirtyRight) {
                dirtyRight++;
            }
        }
        // Make sure that values are sane
        dirtyTop = (dirtyTop < 0) ? 0 : dirtyTop;
        dirtyBottom = (dirtyBottom > rows) ? rows : dirtyBottom;
        dirtyLeft  = (dirtyLeft < 0) ? 0 : dirtyLeft;
        dirtyRight = (dirtyRight > cols) ? cols : dirtyRight;

        // Make sure that the dirty area is a box, so if the new
        // dirt spans many lines, the entire screen width should
        // be repainted.
        if (dirtyBottom - dirtyTop > 1) {
            dirtyLeft = 0;
            dirtyRight = cols;
        }
        //System.out.println("New dirt=("+dirtyTop+","+dirtyLeft+")("+dirtyBottom+","+dirtyRight+"). ");
    }


    //
    // FocusListener, AdjustmentListener, MouseListener, MouseMotionListener, ComponentListener
    //
    public void focusGained(FocusEvent e) {
        setCursor(Cursor.getPredefinedCursor(Cursor.TEXT_CURSOR));
        cursorHollow = false;
        makeCursorDirty();
        repaint(true);
    }
    public void focusLost(FocusEvent e) {
        setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        cursorHollow = true;
        makeCursorDirty();
        repaint(true);
    }

    public boolean isFocusTraversable() {
        return true;
    }

    // !!! Since the realization of the window is very different on different
    // platforms (w.r.t. generated events etc.) we don't listen to
    // componentResized event until window is shown, in that instance we also
    // do the pending setGeometry.
    //
    public void componentMoved(ComponentEvent e) {
        // !!! TODO: Do we want to save absolute positions???
    }
    public void emulateComponentShown() {
        componentShown(new ComponentEvent(ownerFrame, 0));
    }
    public synchronized void componentShown(ComponentEvent e) {
        if(e.getComponent() == ownerFrame && pendingShow) {
            // !!! Ad-hoc wait to let AWT thread in, seems to prevent it from
            // sending a componentResized when getSize() returns bad value
            // AFTER we have done setSize(), this only occurs on Linux (as far
            // as I have seen) due to thread-scheduler lag ?!?
            try {
                this.wait(100);
            } catch(InterruptedException ee) {}
            pendingShow = false;
            calculateCharSize();
            setGeometry(rows, cols);
            setPosition(xPos, yPos);
        }
    }

    public void componentHidden(ComponentEvent e) {}

    public synchronized void componentResized(ComponentEvent e) {
        Dimension dim = getSize();
        int newCols = (dim.width  - (2 * borderWidth))  / charWidth;
        int newRows = (dim.height - (2 * borderHeight)) / charHeight;
        //if(pendingShow || (e != null && e.getComponent() != this) || (newCols <= 0 || newRows <= 0)) {
        pendingShow = false;
        if(pendingShow || (newCols <= 0 || newRows <= 0)) {
            return;
        }

        if(newRows != rows || newCols != cols) {
            if (controller != null) {
                controller.displayDragResize(newRows, newCols);
            }
        }
    }

    public synchronized void adjustmentValueChanged(AdjustmentEvent e) {
        visTop = e.getValue();
        updateScrollbarValues();
        repaint();
    }

    private final int mouseRow(int y) {
        int mouseRow = (y - borderHeight) / charHeight;
        if(mouseRow < 0)
            mouseRow = 0;
        else if(mouseRow >= rows)
            mouseRow = rows - 1;
        return mouseRow;
    }
    private final int mouseCol(int x) {
        int mouseCol = (x - borderWidth)  / charWidth;
        if(mouseCol < 0)
            mouseCol = 0;
        else if(mouseCol >= cols)
            mouseCol = cols - 1;
        return mouseCol;
    }

    private static int getWhichButton(MouseEvent e) {
        int m = e.getModifiers();
        if ((m & MouseEvent.BUTTON1_MASK) != 0)
            return ComponentController.LEFT_BUTTON;
        else if ((m & MouseEvent.BUTTON2_MASK) != 0)
            return ComponentController.MIDDLE_BUTTON;
        else if ((m & MouseEvent.BUTTON3_MASK) != 0)
            return ComponentController.RIGHT_BUTTON;
        return ComponentController.UNKNOWN_BUTTON;
    }

    public void mouseMoved(MouseEvent e) {}
    public void mouseEntered(MouseEvent e) {}
    public void mouseExited(MouseEvent e) {}
    public synchronized void mouseClicked(MouseEvent e) {
        if (e == null)
            return;

        int row = mouseRow(e.getY());
        int col = mouseCol(e.getX());
        int mod = e.getModifiers();

        if (controller != null)
            controller.mouseClicked(visTop, row, col, mod, getWhichButton(e));
    }
    public synchronized void mousePressed(MouseEvent e) {
        if (e == null)
            return;

        int row = mouseRow(e.getY());
        int col = mouseCol(e.getX());
        int mod = e.getModifiers();

        if (controller != null)
            controller.mousePressed
                (visTop, row, col, mod, getWhichButton(e), e.getX(), e.getY());
    }
    public synchronized void mouseReleased(MouseEvent e) {
        if (e == null)
            return;

        int row = mouseRow(e.getY());
        int col = mouseCol(e.getX());
        int mod = e.getModifiers();

        if (controller != null)
            controller.mouseReleased(visTop, row, col, mod, getWhichButton(e));
    }
    public synchronized void mouseDragged(MouseEvent e) {
        if (e == null)
            return;

        int row = mouseRow(e.getY());
        int col = mouseCol(e.getX());
        int mod = e.getModifiers();
        if (controller != null)
            controller.mouseDragged(visTop, row, col, mod, getWhichButton(e), 0);
    }

    //
    // Methods overridden from super-class Component + some helper functions
    //

    private void calculateCharSize() {
        int charMaxAscent;

		int charMaxDescent;
        int charLeading;
        FontMetrics fm = getFontMetrics(getFont());
        charWidth      = -1; // !!! Does not seem to work: fm.getMaxAdvance();
        charHeight     = fm.getHeight() + lineSpaceDelta;
        charMaxAscent  = fm.getMaxAscent();
        charMaxDescent = fm.getMaxDescent();
        charLeading    = fm.getLeading();
        baselineIndex  = charMaxAscent + charLeading - 1;

        if(charWidth == -1) {
            charWidth = fm.charWidth('W');
        }
    }

    public boolean isWide(char c) {
        return !ComponentUtil.isBoxOrBlockChar(c) && 
            getFontMetrics(getFont()).charWidth(c) > charWidth;
    }

    public Dimension getDimensionOfText(int rows, int cols) {
        //calculateCharSize();
        return new Dimension((cols * charWidth) + (2 * borderHeight),
                             (rows * charHeight) + (2 * borderWidth));
    }

    public Dimension getPreferredSize() {
        Dimension dim = getDimensionOfText(rows, cols);
        if(DEBUG)
            System.out.println("getPreferredSize " + cols + "x" + rows
                               + "(" + dim + ")");
        return dim;
    }

    public Dimension getMinimumSize() {
        return getDimensionOfText(MIN_ROWS, MIN_COLS);
    }

    public Dimension getMaximumSize() {
        return getDimensionOfText(MAX_ROWS, MAX_COLS);
    }

    final Rectangle getClipRect(Graphics g) {
        Rectangle clipRect = g.getClipBounds();
        if(clipRect == null) {
            Dimension winSize = getSize();
            clipRect = new Rectangle(0, 0, winSize.width, winSize.height);
        }
        return clipRect;
    }

    private void clearDirtyArea(Graphics source, Graphics dest, 
                                int left, int top, int right, int bottom) {
        boolean clearAll = (left   == 0    &&
                            right  == cols &&
                            top    == 0    &&
                            bottom == rows);
        int x, y, w, h;

        if(clearAll) {
            Dimension dim = getSize();
            x = 0;
            y = 0;
            w = dim.width;
            h = dim.height;
        } else {
            x = borderWidth + (charWidth   * left);
            y = borderHeight + (top    * charHeight);
            w = (charWidth   * (right  - left));
            h = (charHeight  * (bottom - top));
        }

        source.setColor(origBgColor);
        source.fillRect(x, y, w, h);
        source.setColor(origFgColor);
        dest.setClip(x, y, w, h);
    }

    void doRepaint() {
        super.repaint();
    }
    public void repaint() {
        repaint(false);
    }
    public void repaint(boolean force) {
    	if(repainter!=null){
           repainter.repaint(force);
    	}
    }

    public void paint(final Graphics g) {
				update(g);
    }

    public void update(Graphics g) {
        if (model == null) {
            return;
        }

        // This should not happen but better safe than sorry...
        if(hpixels == 0 || vpixels == 0) {
            Dimension dim = getSize();
            vpixels = dim.height;
            hpixels = dim.width; 
            if(hpixels == 0 || vpixels == 0) {
                return;
            }
        }

        Rectangle clipRect;
        int dirtyLeft, dirtyRight, dirtyTop, dirtyBottom;
        boolean isDirty;
        
        synchronized (this) {
            dirtyLeft   = this.dirtyLeft;
            dirtyRight  = this.dirtyRight;
            dirtyTop    = this.dirtyTop;
            dirtyBottom = this.dirtyBottom;
            isDirty     = this.isDirty;
            
            // Reset dirty area (i.e. we have take responsibility for it)
            this.isDirty = false;
        }        

        if((memGraphics == null) ||
                (memImageSize == null) ||
                (hpixels != memImageSize.width) ||
                (vpixels != memImageSize.height)) {
            memImageSize = new Dimension(hpixels, vpixels);
            memImage     = createImage(hpixels, vpixels);
        }
        if( memImage == null ){
        	return;
        }
        memGraphics = memImage.getGraphics();

        if(isDirty) {
            clearDirtyArea(memGraphics, g, dirtyLeft, dirtyTop, dirtyRight, dirtyBottom);
            clipRect = getClipRect(g);
        } else {
            // If nothing is dirty, the cause for update must be
            // "destroyed" window content.
            //
            makeAllDirty();
            dirtyTop    = 0;
            dirtyBottom = rows;
            dirtyLeft   = 0;
            dirtyRight  = cols;

            clipRect    = getClipRect(g);
            memGraphics.setClip(clipRect.x, clipRect.y, clipRect.width,
                                clipRect.height);
            memGraphics.setColor(origBgColor);
            memGraphics.fillRect(clipRect.x, clipRect.y, clipRect.width,
                                 clipRect.height);
            memGraphics.setColor(origFgColor);
        }

        int x, y, curX = 0, curY = 0;
        boolean doCursor = false;
        boolean doCursorInverse = false;
    	
    	String resposta = "";
        for(int i = dirtyTop; i < dirtyBottom; i++) {
            y = borderHeight + (i * charHeight);
            if(model==null){
               continue;	
            }
            int[] attrRow = model.getAttribs(visTop,  i);
            char[] charRow = model.getChars(visTop, i); 

            // Sanity checks to see if the model is resized between calls
            // of getAttribs() and getChars()
            if (attrRow == null || charRow == null) {
                continue;
            }
            if (attrRow.length != charRow.length) {
                continue;
            }
            if (dirtyLeft > attrRow.length || dirtyRight > attrRow.length) {
                continue;
            }

            for(int j = dirtyLeft; j < dirtyRight; j++) {
                Color bgColor = origBgColor;
                Color fgColor = origFgColor;
                int attr       = attrRow[j];
                int attrMasked = (attr & ComponentModel.MASK_ATTR);
                boolean doDraw = false;
                x = borderWidth + (charWidth * j);
                if (((attr & ComponentModel.ATTR_INVERSE) != 0) ^
                        isInsideSelection(visTop + i, j)) {
                    if ((attr & ComponentModel.ATTR_FGCOLOR) != 0) {
                        bgColor = termColors[(attr & ComponentModel.MASK_FGCOL) 
                                             >>> ComponentModel.SHIFT_FGCOL];
                    } else {
                        bgColor = origFgColor;
                    }
                    if ((attr & ComponentModel.ATTR_BGCOLOR) != 0) {
                        fgColor = termColors[(attr & ComponentModel.MASK_BGCOL) 
                                             >>> ComponentModel.SHIFT_BGCOL];
                    } else {
                        fgColor = origBgColor;
                    }

                    if ((attr & ComponentModel.ATTR_LOWINTENSITY) != 0) {
                        bgColor = ComponentUtil.makeDimmerColor(bgColor);
                    }
                    doDraw = true;
                } else {
                    if((attr & ComponentModel.ATTR_BGCOLOR) != 0) {
                        bgColor = termColors[(attr & ComponentModel.MASK_BGCOL) 
                                             >>> ComponentModel.SHIFT_BGCOL];
                        doDraw = true;
                    }
                    if((attr & ComponentModel.ATTR_FGCOLOR) != 0) {
                        fgColor = termColors[(attr & ComponentModel.MASK_FGCOL) 
                                             >>> ComponentModel.SHIFT_FGCOL];
                    }

                    if ((attr & ComponentModel.ATTR_LOWINTENSITY) != 0) {
                        fgColor = ComponentUtil.makeDimmerColor(fgColor);
                    }
                }

                if (hasCursor && (visTop + i) == curRow && j == curCol) {
                    doCursor = true;
                    doCursorInverse = ((attr & ComponentModel.ATTR_INVERSE) != 0);
                    curX = x;
                    curY = y;
                } else {
                    // Only draw if bg is different from what we cleared area with
                    if (doDraw) {
                        memGraphics.setColor(bgColor);
                        memGraphics.fillRect(x, y, charWidth, charHeight);
                    }
                    memGraphics.setColor(fgColor);
                }

                if ((attrMasked & ComponentModel.ATTR_CHARDRAWN) != 0) {
                    if ((attr & ComponentModel.ATTR_INVISIBLE) != 0) {
                        // Don't draw anything invisible, but the
                        // underline should be drawn anyway.
                    } else if((attr & ComponentModel.ATTR_LINEDRAW) != 0) {
                        ComponentUtil.drawLineDrawChar(memGraphics, x, y,
                                                     baselineIndex,
                                                     charRow[j],
                                                     charWidth, charHeight);
                        resposta = resposta+charRow[j];
                    } else if((attr & ComponentModel.ATTR_BOLD) != 0 ||
                              (attr & ComponentModel.ATTR_BLINKING) != 0) {
                        // Approximate blinking with bold font until
                        // a special update thread is implemented
                        memGraphics.setFont(boldFont);
                        memGraphics.drawChars(charRow, j, 1, x, y + baselineIndex);
                        memGraphics.setFont(plainFont);
                        resposta = resposta+charRow[j];
                    } else if (ComponentUtil.isBoxOrBlockChar(charRow[j])) {
                        ComponentUtil.drawBoxOrBlockChar(memGraphics, x, y, baselineIndex, charRow[j], charWidth, charHeight);
                        resposta = resposta+charRow[j];
                    } else if (charRow[j] != ' ') { // no need to draw spaces
                        memGraphics.drawChars(charRow, j, 1, x, y + baselineIndex);
                        resposta = resposta+charRow[j];
                    } else {
                    	resposta = resposta+charRow[j];
                    }
                    if((attr & ComponentModel.ATTR_UNDERLINE) != 0){
                        memGraphics.drawLine(x, y + baselineIndex, x + charWidth, y + baselineIndex);
                    }
                    
                    
                }
            }
        }
    	
        if (doCursor) {
        	memGraphics.setColor(cursorColor);
            memGraphics.setXORMode(doCursorInverse ? origFgColor : origBgColor);
            if (cursorHollow) {
                memGraphics.drawRect(curX, curY, charWidth-1, charHeight-1);
            } else {
                memGraphics.fillRect(curX, curY, charWidth, charHeight);
            }
            memGraphics.setPaintMode();
            memGraphics.setColor(origFgColor);
        }

        g.drawImage(memImage, 0, 0, this);

//        if(logoDraw && logoImg != null) {
//            x = logoX;
//            y = logoY;
//            if(x == -1) {
//                if (centerLogoX == -1) {
//                    centerLogoX = (hpixels / 2) - (logoW / 2);
//                }
//                x = centerLogoX;
//            }
//            if (y == -1) {
//                if (centerLogoY == -1) {
//                    centerLogoY = (vpixels / 2) - (logoH / 2);
//                }
//                y = centerLogoY;
//            }
//            g.drawImage(logoImg, x, y, logoW, logoH, this);
//        }
    	Display.getDefault().asyncExec(new Runnable(){
			public void run(){ 
		        if (updateScrollbar) {
		            updateScrollbar = false;
		            if(scrollBarSWT!=null){
		//                Thread threadRun = new Thread() {
		//    			    public void run() {   
		    	    					if(model!=null && scrollBarSWT!=null ){
		    	    	            	   scrollBarSWT.setMaximum(model.getBufferRows());
		    	    	            	   scrollBarSWT.setMinimum(0);
		    	    	            	   scrollBarSWT.setSelection(visTop);
		    	    					}
		//    			    }
		//                };
		//                logger.debug("threadRun.start()");
		//                threadRun.start();            	
		            } else {
		              scrollbar.setValues(visTop, rows, 0, model.getBufferRows());
		              scrollbar.setBlockIncrement(rows);
		            }
		        }
			}
		});    	      

        
        if(new Boolean(doCursor) && isCursorReady() ){
        	String respostaClone = new String(resposta);
        	//System.out.println(resposta);
           	resposta = "";
	        	if( curX > 2 && getDynamicBuffer()!=null && !dynamic.isClose()) {
					if(channelEventAdapter==null){ 
					   channelEventAdapter = new ChannelEventAdapter();
					}
					//log.debug("onDataReceived="+respostaClone);
					if( channelEventAdapter.onDataReceived(this, respostaClone.getBytes())){
					    setCursorReady(false);
					}
				}
	        	
	        	if( workFlowSP1<=1 ){
	        		String strLinha = respostaClone;
	        		if ( workFlowSP1==0 && strLinha.contains("assword:") ){
	        			workFlowSP1=1;
	        		} else if (workFlowSP1==1 && strLinha.trim().length()>0 && !strLinha.equals("*")){
	        			  this.lastResult=respostaClone;
	        		} 
	        	} 
	        	
//	        	if(respostaClone.length()>10){  
//	        	   System.out.println("RespostaClone = "+respostaClone.substring(respostaClone.length()-9));	
//	        	}
	        	//System.out.println(Thread.currentThread().getName()+"this.lastResult="+this.lastResult);
	        	
        }
    }
    
    public String getLastResult(){ 
    	return this.lastResult;
    }

    public void setPosition(int xPos, int yPos) {
        Dimension sDim  = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension tDim  = getDimensionOfText(rows, cols);
        Insets    fIns  = ownerFrame.getInsets();
        int       sbSz  = (haveScrollbar?scrollbar.getSize().width:0);

        if(xPos < 0) {
            xPos += sDim.width - tDim.width - fIns.left - fIns.right - sbSz;
        }
        if(yPos < 0) {
            yPos += sDim.height - tDim.height - fIns.top - fIns.bottom;
        }
        this.xPos = xPos;
        this.yPos = yPos;

        if (isShowing()) {
            ownerFrame.setLocation(xPos, yPos);
            ownerFrame.pack();
            requestFocus();
        } else {
            pendingShow = true;
        }
        repaint(true);
    }

    public synchronized void setGeometry(int row, int col) {
        /*
        if (row == rows && col == cols) {
            return;
        }
        */

        Dimension tDim  = getDimensionOfText(row, col);
        if (vpixels == tDim.height && hpixels == tDim.width) {
            return;
        }

        vpixels = tDim.height;
        hpixels = tDim.width;
        rows = row;
        cols = col;
        setSize(tDim);

        if (visTopChangePending) {
            this.visTop = fenceVisTop(visTopChange);
            visTopChangePending = false;
        }

        if (isShowing()) {
            memGraphics = null;
            updateScrollbarValues();
            makeAllDirty();
            ownerFrame.pack();
            //** PLEASE requestFocus() must to be commented.. it crashed Windows 7 ***/
            //requestFocus();
        } else {
            pendingShow = true;
        }
        repaint();

        if (controller != null) {
            controller.displayResized(row, col, vpixels, hpixels);
        }
    }

    public synchronized void resetSelection() {
        hasSelection = false;
        makeSelectionDirty();
        repaint();
    }

    public synchronized void setSelection(int row1, int col1, int row2, int col2) {
        if (hasSelection) {
            makeSelectionDirty();
        } else {
            hasSelection = true;
        }

        if (row1 < row2) {
            selectionTopRow =    row1;
            selectionTopCol =    col1;
            selectionBottomRow = row2;
            selectionBottomCol = col2;
        } else if (row1 == row2) {
            selectionTopRow = selectionBottomRow = row1;
            if (col1 < col2) {
                selectionTopCol    = col1;
                selectionBottomCol = col2;
            } else {
                selectionTopCol    = col2;
                selectionBottomCol = col1;
            }
        } else {
            selectionTopRow =    row2;
            selectionTopCol =    col2;
            selectionBottomRow = row1;
            selectionBottomCol = col1;
        }

        makeSelectionDirty();
        repaint();
    }

    public void setNoCursor() {
        if (hasCursor) {
            hasCursor = false;
            makeCursorDirty();
            repaint();
        }
    }
    public synchronized void setCursorPosition(int row, int col) {
        makeCursorDirty();
        if (!hasCursor) {
            hasCursor = true;
        }
        curRow = row;
        curCol = col;
        makeCursorDirty();
    }

    public void setBackgroundColor(Color c) {
        origBgColor = c;
        setBackground(origBgColor);
        makeAllDirty();
        repaint();
    }

    public void setForegroundColor(Color c) {
        origFgColor = c;
        setForeground(origFgColor);
        makeAllDirty();
        repaint();
    }

    public void setCursorColor(Color c) {
        cursorColor = c;
        makeAllDirty();
        repaint();
    }

    public void reverseColors() {
        Color swap  = origBgColor;
        origBgColor = origFgColor;
        origFgColor = swap;
        makeAllDirty();
        repaint();
    }

    public void doBell() {
        doBell(false);
    }

    public void doBell(boolean visualBell) {
        if (visualBell) {
            reverseColors();
            try {
                Thread.sleep(25);
            } catch (InterruptedException e) {}
            reverseColors();
        } else {
            Toolkit toolkit = Toolkit.getDefaultToolkit();
            if(toolkit != null) {
                try {
                    toolkit.beep();
                } catch (Exception e) {
                    // Could not beep, we are probably an unpriviliged applet
                    // Automatically enable visual-bell now and "sound" it
                    // instead
                    doBell(true);
                }
            }
        }
    }

    public Component getAWTComponent() { return this; }

    public void setIgnoreClose() {
        // Do nothing
    }

    public void windowClosed() {
        removeComponentListener(this);
        removeFocusListener(this);
        removeMouseMotionListener(this);
        removeMouseListener(this);
        ownerFrame.removeComponentListener(this);
        ownerFrame = null;
        controller = null;
        model      = null;
        repainter  = null;
        
        if (myPanel != null) 
            myPanel.removeAll();
        myPanel = null;
        scrollbar = null;
    }

    public Component mkButton(String label,String cmd,ActionListener listener){
        Button button = new Button(label);
        button.setActionCommand(cmd);
        button.addActionListener(listener);
        return button;
    }

	public Boolean isCursorReady() {
   		return cursorReady;
	}

	public synchronized DynamicBuffer createDynamicBuffer() {
//		while(!isCursorReady()){
//		   try {
//			Thread.sleep(350);
//		   } catch (InterruptedException e) {
//			e.printStackTrace();
//		   }
//		}
		dynamic = new DynamicBuffer();
		//System.out.println("Create DynamicBuffer");
		return dynamic;
	}

	public synchronized DynamicBuffer getDynamicBuffer() {
		return dynamic;
	}
	
	public synchronized void closeDynamicBuffer() {
		dynamic.close();
		//System.out.println("closeDynamicBuffer");
	}


	/**
	 * @param cursorReady the cursorReady to set
	 */
	public void setCursorReady(Boolean cursorReady) {
		this.cursorReady = cursorReady;
	}

	/**
	 * @return the channelEventAdapter
	 */
	public ChannelEventAdapter getChannelEventAdapter() {
		return channelEventAdapter;
	}
	
}

