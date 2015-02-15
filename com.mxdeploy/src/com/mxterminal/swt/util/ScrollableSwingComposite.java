package com.mxterminal.swt.util;

import org.eclipse.swt.SWT;
import org.eclipse.swt.awt.SWT_AWT;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.ScrollBar;

public class ScrollableSwingComposite extends Canvas {
	protected Composite embeddedComposite;
	protected java.awt.Frame embeddedFrame;

	protected ScrollBar hBar = null, vBar = null;
	protected javax.swing.JScrollPane scrollpane;

	static int checkStyle(int style) {
		style = style & (~(SWT.EMBEDDED));
		return style;
	}

	public ScrollableSwingComposite(Composite parent, int style) {
		super(parent, checkStyle(style));

		setLayout(new FillLayout());

		hBar = getHorizontalBar();
		vBar = getVerticalBar();

		embeddedComposite = new Composite(this, SWT.EMBEDDED);

		scrollpane = new javax.swing.JScrollPane(javax.swing.JScrollPane.VERTICAL_SCROLLBAR_NEVER,
				javax.swing.JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scrollpane.setWheelScrollingEnabled(true);

		embeddedFrame = SWT_AWT.new_Frame(embeddedComposite);
		embeddedFrame.setLayout(new java.awt.BorderLayout());
		embeddedFrame.add(scrollpane, java.awt.BorderLayout.CENTER);

		if (vBar != null) {
			vBar.addListener(SWT.Selection, new VBarListener());
			scrollpane.getVerticalScrollBar().addAdjustmentListener(new ScrollAdjustmentListener());
		}
		if (hBar != null) {
			hBar.addListener(SWT.Selection, new HBarListener());
			scrollpane.getHorizontalScrollBar().addAdjustmentListener(new ScrollAdjustmentListener());
		}

		addListener(SWT.Resize, new ResizeListener());
		scrollpane.addMouseWheelListener(new MouseWheelListener());
	}
	
	public void setViewportView(java.awt.Component component) {
		scrollpane.setViewportView(component);
	}
	
	private void updateSWTScrollbar() {
		if (scrollpane.getViewport() == null || scrollpane.getViewport().getView() == null)
			return;

		java.awt.Rectangle rect = scrollpane.getViewport().getView().getBounds();

		Rectangle client = getClientArea();
		if (hBar != null) {
			hBar.setMinimum(0);
			hBar.setMaximum(rect.width);
			hBar.setThumb(Math.min(rect.width, client.width));
			hBar.setIncrement(hBar.getThumb() / 4);
			hBar.setPageIncrement(hBar.getThumb());
			hBar.setSelection(scrollpane.getHorizontalScrollBar().getValue());
		}
		if (vBar != null) {
			vBar.setMinimum(0);
			vBar.setMaximum(rect.height);
			vBar.setThumb(Math.min(rect.height, client.height));
			vBar.setIncrement(vBar.getThumb() / 4);
			vBar.setPageIncrement(vBar.getThumb());
			vBar.setSelection(scrollpane.getVerticalScrollBar().getValue());
		}
	}

	private class HBarListener implements Listener {
		public void handleEvent(Event e) {
			final int hSelection = hBar.getSelection();

			javax.swing.SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					scrollpane.getHorizontalScrollBar().setValue(hSelection);
				}
			});

		}
	}

	private class MouseWheelListener implements java.awt.event.MouseWheelListener {
		public void mouseWheelMoved(final java.awt.event.MouseWheelEvent e) {
			if (e.getScrollType() != java.awt.event.MouseWheelEvent.WHEEL_UNIT_SCROLL)
				return;

			getDisplay().asyncExec(new Runnable() {
				public void run() {
					if (vBar != null) {
						int move = e.getWheelRotation() * vBar.getIncrement();
						vBar.setSelection(vBar.getSelection() + move);
						vBar.notifyListeners(SWT.Selection, new Event());
					}
					else if (hBar != null) {
						int move = e.getWheelRotation() * hBar.getIncrement();
						hBar.setSelection(hBar.getSelection() + move);
						hBar.notifyListeners(SWT.Selection, new Event());
					}
				}
			});
		}
	}

	private class ResizeListener implements Listener {
		public void handleEvent(Event e) {
			updateSWTScrollbar();
		}
	}
	private class ScrollAdjustmentListener implements java.awt.event.AdjustmentListener {
		public void adjustmentValueChanged(java.awt.event.AdjustmentEvent e) {
			getDisplay().asyncExec(new Runnable() {
				public void run() {
					updateSWTScrollbar();
				}
			});
		}
	}

	private class VBarListener implements Listener {
		public void handleEvent(Event e) {
			final int vSelection = vBar.getSelection();

			javax.swing.SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					scrollpane.getVerticalScrollBar().setValue(vSelection);
				}
			});

		}
	}
}

