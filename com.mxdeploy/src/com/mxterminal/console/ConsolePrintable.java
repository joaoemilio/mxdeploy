package com.mxterminal.console;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.print.PageFormat;
import java.awt.print.Printable;

import com.mxterminal.console.component.ComponentModel;
import com.mxterminal.console.component.ComponentUtil;
import com.mxterminal.console.component.ComponentView;

public class ConsolePrintable implements Printable {
    /* What to print */
    public static int SCREEN = 1;
    public static int BUFFER = 2;

    private static Color BG_COLOR = Color.white;
    private static Color FG_COLOR = Color.black;

    private ComponentModel model;
    private int topLine;
    private int fontSize;
    private String fontName;
    private int lineSpaceDelta = fontSize/10;

    // Font metrics
    private boolean fontMetricsInitialized = false;
    private int charWidth;
    private int charHeight;
    private int baselineIndex;

    private Font plainFont;
    private Font boldFont;

    public ConsolePrintable(ComponentModel model, int what,
                             String fontName, int fontSize) {
        this.model = model;
        this.fontName = fontName;
        this.fontSize = fontSize;

        // Where to start printing
        if (what == SCREEN) {
            topLine = model.getBufferRows()-model.getDisplayRows();
        } else {
            topLine = 0;
        }

        plainFont = new Font(fontName, Font.PLAIN, fontSize);
        boldFont  = new Font(fontName, Font.BOLD, fontSize);

    }

    private void initFontMetrics(Graphics g) {
        g.setFont(plainFont);
        FontMetrics fm = g.getFontMetrics();
        charWidth      = fm.charWidth('W');
        charHeight     = fm.getHeight() + lineSpaceDelta;
        baselineIndex  = fm.getMaxAscent() + fm.getLeading() - 1;
        fontMetricsInitialized = true;
    }

    /*
     */
    public int print(Graphics g, PageFormat f, int pageIndex) {
        
        if (!fontMetricsInitialized) {
            initFontMetrics(g);
        }

        // This assumes all pages have identical format
        int height = (int)(f.getImageableHeight()-f.getImageableY()*2);
        int pageLines = height/fontSize;
        int pageTop = pageLines * pageIndex + topLine;

        if (pageTop > model.getBufferRows()) {
            return NO_SUCH_PAGE;
        }

        int pageEnd = pageTop + pageLines;
        if (pageEnd > model.getBufferRows()) {
            pageEnd = model.getBufferRows();
        }

        g.setFont(plainFont);
        for (int row = pageTop; row < pageEnd; row++) {
            int y = (int)f.getImageableY() + (row-pageTop)*fontSize;
            int[] attrRow = model.getAttribs(0,  row);
            char[] charRow = model.getChars(0, row);


            for(int col = 0; col < model.getDisplayCols(); col++) {
                Color bgColor = BG_COLOR;
                Color fgColor = FG_COLOR;
                int attr       = attrRow[col];
                int attrMasked = (attr & ComponentModel.MASK_ATTR);
                boolean doDraw = false;
                int x = (int)f.getImageableX() + (charWidth * col);
                if (((attr & ComponentModel.ATTR_INVERSE) != 0)) {
                    if ((attr & ComponentModel.ATTR_FGCOLOR) != 0) {
                        bgColor = ComponentView.termColors[
                            (attr & ComponentModel.MASK_FGCOL) 
                            >>> ComponentModel.SHIFT_FGCOL];
                    } else {
                        bgColor = FG_COLOR;
                    }
                    if ((attr & ComponentModel.ATTR_BGCOLOR) != 0) {
                        fgColor = ComponentView.termColors[
                            (attr & ComponentModel.MASK_BGCOL) 
                            >>> ComponentModel.SHIFT_BGCOL];
                    } else {
                        fgColor = BG_COLOR;
                    }

                    if ((attr & ComponentModel.ATTR_LOWINTENSITY) != 0) {
                        bgColor = ComponentUtil.makeDimmerColor(bgColor);
                    }
                    doDraw = true;
                } else {
                    if((attr & ComponentModel.ATTR_BGCOLOR) != 0) {
                        bgColor = ComponentView.termColors[
                            (attr & ComponentModel.MASK_BGCOL) 
                            >>> ComponentModel.SHIFT_BGCOL];
                        doDraw = true;
                    }
                    if((attr & ComponentModel.ATTR_FGCOLOR) != 0) {
                        fgColor = ComponentView.termColors[
                            (attr & ComponentModel.MASK_FGCOL) 
                            >>> ComponentModel.SHIFT_FGCOL];
                    }

                    if ((attr & ComponentModel.ATTR_LOWINTENSITY) != 0) {
                        fgColor = ComponentUtil.makeDimmerColor(fgColor);
                    }
                }

                // Only draw if bg is different from what we cleared area with
                if (doDraw) {
                    g.setColor(bgColor);
                    g.fillRect(x, y, charWidth, charHeight);
                }
                g.setColor(fgColor);

                if ((attrMasked & ComponentModel.ATTR_CHARDRAWN) != 0) {
                    if ((attr & ComponentModel.ATTR_INVISIBLE) != 0) {
                        // Don't draw anything invisible, but the
                        // underline should be drawn anyway.
                    } else if((attr & ComponentModel.ATTR_LINEDRAW) != 0) {
                        ComponentUtil.drawLineDrawChar(g, x, y, baselineIndex,
                                                     charRow[col],
                                                     charWidth, charHeight);
                    } else if((attr & ComponentModel.ATTR_BOLD) != 0 ||
                              (attr & ComponentModel.ATTR_BLINKING) != 0) {
                        // Approximate blinking with bold font
                        g.setFont(boldFont);
                        g.drawChars(charRow, col, 1, x, y + baselineIndex);
                        g.setFont(plainFont);
                    } else if (charRow[col] != ' ') { // no need to draw spaces
                        g.drawChars(charRow, col, 1, x, y + baselineIndex);
                    }
                    if((attr & ComponentModel.ATTR_UNDERLINE) != 0)
                        g.drawLine(x, y + baselineIndex, x + charWidth,
                                   y + baselineIndex);
                }
            }
        }
        return PAGE_EXISTS;
    }
}
