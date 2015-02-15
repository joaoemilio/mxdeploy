package com.mxdeploy.launcher;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import org.eclipse.swt.widgets.Display;

import com.mxdeploy.api.domain.Database;
import com.mxdeploy.launcher.account.AccountDialog;
import com.mxdeploy.launcher.account.event.AccountEvent;
import com.mxdeploy.launcher.splash.SplashDialog;
import com.mxdeploy.launcher.util.SynchronizeFiles;
import com.mxdeploy.launcher.util.URLClassLoaderLauncher;

public class Launcher {

	private static SplashDialog splashDialog;
	public static Display display = null;
	
	public static void main(String args[]) {
	    start();
	}

	public static void start() {
		URLClassLoaderLauncher.loadSharedLibrary(); 
		splashDialog = new SplashDialog();
		splashDialog.openShell();
		
		if(Database.WORKSPACE_NAME==null){
			Display.getDefault().syncExec(new Runnable() {
				public void run() {

					List<String> accounts = AccountEvent.getAccounts();
					AccountDialog dialog = new AccountDialog();
					Database.WORKSPACE_NAME = dialog.open(accounts);
					
					splashDialog.getProgressBar().setVisible(true);
				}
			});				
		}
		
		SynchronizeFiles.synchronize(splashDialog);

		try {
			Class clazz = Class.forName("com.mxdeploy.swt.MainShell"); 
			Method m = clazz.getMethod("start", SplashDialog.class, String.class );
	        m.invoke(clazz.newInstance(), splashDialog, Database.WORKSPACE_NAME  );	 
	        
	        splashDialog.closeShell();
	        
			m = clazz.getMethod("waitWhileRunning", String.class );
			m.invoke(clazz.newInstance() , "waitWhileRunning");
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
			e.getCause().printStackTrace();
		} catch (ClassNotFoundException e1) {
			e1.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		}
		
	}
	
}
