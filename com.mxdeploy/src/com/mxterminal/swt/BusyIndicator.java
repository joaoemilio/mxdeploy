package com.mxterminal.swt;

import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;

public class BusyIndicator extends Canvas {

	private Image[] images;
	private Image image;

	private Thread busyThread=null;
	private boolean stop;
	
	/**
	 * BusyWidget constructor comment.
	 * @param parent org.eclipse.swt.widgets.Composite
	 * @param style int
	 */
	public BusyIndicator(Composite parent, int style) {
		super(parent, style);
		images = new Image[13];
		for (int i = 0 ; i < 13; i++) {
			String name = "/enabled/busy/busy"+(i+1)+".gif";
			Image img = new Image(Display.getCurrent(), BusyIndicator.class.getClass().getResourceAsStream(name));			
			images[i] = img;
		}
	
		addPaintListener(new PaintListener() {
			public void paintControl(PaintEvent event) {
				onPaint(event);
			}
		});
	
		image = images[0];
	}
	
	public Point computeSize(int wHint, int hHint, boolean changed) {
		return new Point(25, 25);
	}
	
	/**
	 * Creates a thread to animate the image.
	 */
	protected synchronized void createBusyThread() {
		if (busyThread != null)
			return;
	
		stop = false;
		busyThread = new Thread() {
			protected int count;
			public void run() {
				try {
					count = 1;
					while (!stop) {
								if (!stop) {
									if (count < 13)
										Display.getDefault().syncExec(new Runnable() { 
											public void run() {										
										       setImage(images[count]);
											}
										});
										
									count++;
									if (count > 12)
										count = 1;
								}
						try {
							sleep(125);
						} catch (Exception e) {
							// ignore
						}
					}
					if (busyThread == null)
						Display.getDefault().asyncExec(new Thread() {
							public void run() {
								setImage(images[0]);
							}
						});
				} catch (Exception e) {
					//Trace.trace(Trace.WARNING, "Busy error", e); //$NON-NLS-1$
				}
			}
		};
	
		busyThread.setPriority(Thread.NORM_PRIORITY + 2);
		busyThread.setDaemon(true);
		busyThread.start();
	}
	
	public void dispose() {
		stop = true;
		busyThread = null;
		super.dispose();
	}
	
	/**
	 * Return the image or <code>null</code>.
	 */
	public Image getImage() {
		return image;
	}

	/**
	 * Returns true if it is currently busy.
	 *
	 * @return boolean
	 */
	public boolean isBusy() {
		return (busyThread != null);
	}

	/* 
	 * Process the paint event
	 */
	protected void onPaint(PaintEvent event) {
		Rectangle rect = getClientArea();
		if (rect.width == 0 || rect.height == 0)
			return;
	
		GC gc = event.gc;
		if (image != null)
			gc.drawImage(image, 2, 2);
	}

	/**
	 * Sets the indicators busy count up (true) or down (false) one.
	 *
	 * @param busy boolean
	 */
	public synchronized void setBusy(boolean busy) {
		if (busy) {
			if (busyThread == null)
				createBusyThread();
		} else {
			if (busyThread != null) {
				stop = true;
				busyThread = null;
			}
		}
	}

	/**
	 * Set the image.
	 * The value <code>null</code> clears it.
	 */
	public void setImage(Image image) {
		if (image != this.image && !isDisposed()) {
			this.image = image;
			redraw();
		}
	}
	
}