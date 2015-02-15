package com.mxdeploy.swt.menu.event;

import org.eclipse.swt.widgets.Display;

import com.mxdeploy.images.Constant;
import com.mxdeploy.plugin.event.EventHandler;
import com.mxdeploy.swt.MainShell;
import com.mxssh.ManageGateway;

public class StopGatewayHandler implements EventHandler {


	public void execute() {
		Display.getDefault().syncExec(new Runnable() {
			public void run() {
				MainShell.getToolBarFooterComposite().getGatewayItem().setImage(Constant.IMAGE_GATEWAY_YELLOW);
			}
		});
	    Thread threadRun = new Thread() {
		    public void run() {
				ManageGateway manageGateway = new ManageGateway();
				manageGateway.stop();
				Display.getDefault().syncExec(new Runnable() {
					public void run() {
						MainShell.getToolBarFooterComposite().getGatewayItem().setImage(Constant.IMAGE_GATEWAY_RED);
					}
				});								
		    }
	    };	
	    threadRun.start();
	}

}