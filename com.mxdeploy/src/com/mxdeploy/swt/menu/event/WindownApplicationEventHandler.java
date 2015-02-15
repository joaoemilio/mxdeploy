package com.mxdeploy.swt.menu.event;

import org.eclipse.swt.custom.CTabFolder;

import com.mxdeploy.plugin.event.EventHandler;
import com.mxdeploy.swt.MainShell;
import com.mxterminal.swt.TerminalFace;
import com.mxterminal.swt.view.CentralComposite;
import com.mxterminal.swt.view.helper.TerminalHelper;

public class WindownApplicationEventHandler implements EventHandler {

	public void execute() {
		CTabFolder tabFolderServer = MainShell.getCTopTabFolder();
		if (tabFolderServer.getMaximized()) {
			tabFolderServer.setMaximized(false);
			MainShell.getSashFormComposite().sashForm.setMaximizedControl(null);
		} else {
			tabFolderServer.setMaximized(true);
			MainShell.getSashFormComposite().sashForm
					.setMaximizedControl(tabFolderServer);
		}
		if (tabFolderServer.getSelection() == null) {
			return;
		}
		if (tabFolderServer.getSelection().getControl() instanceof TerminalFace) {
			TerminalFace face = (TerminalFace) tabFolderServer.getSelection()
					.getControl();
			if (face.getTerminalWin() != null) {
				face.getTerminalWin().requestFocus();
				// logger.debug("TerminalFace:termianlFace.getTerminalWin().requestFocus();");
			}
		} else if (tabFolderServer.getSelection().getControl() instanceof CentralComposite) {
			CentralComposite serverComposite = (CentralComposite) tabFolderServer
					.getSelection().getControl();
			TerminalHelper consoleHelper2 = (TerminalHelper) serverComposite.getData();
			if (consoleHelper2.getSSHInteractiveClient() != null) {
				consoleHelper2.getSSHInteractiveClient().getTerminalWin()
						.requestFocus();
				// logger.debug("ServerComposite:termianlFace.getTerminalWin().requestFocus();");
			}
		}
	}

	public void openMaximized() {
		CTabFolder tabFolderServer = MainShell.getCTopTabFolder();
		tabFolderServer.setMaximized(true);
		MainShell.getSashFormComposite().sashForm.setMaximizedControl(tabFolderServer);
		if (tabFolderServer.getSelection() == null) {
			return;
		}
	}

}
