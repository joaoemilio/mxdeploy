package com.mxssh;

import java.io.IOException;
import java.io.InputStream;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.StringTokenizer;

import org.apache.log4j.Logger;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.ProgressBar;

import com.jcraft.jsch.JSchException;
import com.mxdeploy.api.domain.Database;
import com.mxdeploy.images.Constant;
import com.mxdeploy.swt.MainShell;
import com.mxdeploy.swt.dialogs.authentication.SshAuthenticationDialog;
import com.mxterminal.swt.util.Socks5Property;
import com.mxterminal.swt.util.TerminalProperty;

public class ManageGateway {

	public static boolean isStarted = false;
	private String username;
	private String password = null;

	private ProgressBar progressBar;
	private CLabel obsCLabel;
	int count = 0;
	int total = 0;
	
	static Logger logger = Logger.getLogger(ManageGateway.class);
	
	public void start() {

		logger.debug("Starting gateway");
		if( TerminalProperty.getSocks5PropertyList() == null || TerminalProperty.getSocks5PropertyList().size() == 0   ) {
			MainShell.sendMessage("Gateway is not configured");
			logger.debug("[Gateway starts] EXITING - TerminalProperty.getSocks5PropertyList() == null or size() == 0");
			return;
		}
		
		Socks5Property socks5Property = null;
		
		for ( Socks5Property socks5Prop : TerminalProperty.getSocks5PropertyList() ){
			socks5Property = socks5Prop;
			break;
		}
		
		if ( socks5Property.getGateway() == null || socks5Property.getGateway().isEmpty() 
		  || socks5Property.getPort()==null || socks5Property.getPort().isEmpty()){
			isStarted = true;
			logger.debug("[Gateway starts] EXITING - socks5Property.getGateway() == null or size() == 0 ");
			Display.getDefault().syncExec(new Runnable() {
				public void run() {
					MainShell.getToolBarFooterComposite().getGatewayItem().setImage(Constant.IMAGE_GATEWAY_GREEN);
				}
			});				
			return;
		}
		
		total = 10;

		Display.getDefault().syncExec(new Runnable() {
			public void run() {
				logger.debug("[Gateway starts] Start Progress Bar ");
				progressBar = MainShell.getToolBarFooterComposite().getProgressBar();
				obsCLabel = MainShell.getToolBarFooterComposite().getObsCLabel();
				obsCLabel.setText("Gateway is runnning, please wait");
				progressBar.setVisible(true);
				progressBar.setMaximum(total);				
				count = count+4;
				progressBar.setSelection(count);
			}
		});
		
		String WORKSPACE = System.getenv("MXD_WORKSPACE");
		logger.info(WORKSPACE);		
		String proxy = socks5Property.getProxy();
		String proxyPort = socks5Property.getProxyPort();
		logger.debug("[Gateway starts] proxy = "+proxy);
		if ( proxy != null && !proxy.isEmpty() ){
			
			if( proxyPort==null ||  proxyPort.isEmpty()){
				logger.debug("[Gateway starts] EXITING - Proxy port can not be null or empty ");
				isStarted=false;
				MainShell.sendMessage("Proxy port can not be null or empty");
				Display.getDefault().syncExec(new Runnable() {
					public void run() {
						progressBar.setSelection(10);
						progressBar.setVisible(false);
						obsCLabel.setText("");
						MainShell.getToolBarFooterComposite().getGatewayItem().setImage(Constant.IMAGE_GATEWAY_RED);
					}
				});				
				return;				
			}
			
			final SshAuthenticationDialog dialog = new SshAuthenticationDialog(proxy);
			Display.getDefault().syncExec(new Runnable() {
				public void run() {
					dialog.setLabelTitle("Serve : "+Database.WORKSPACE_NAME+" Proxy");
					dialog.openShell();
				}
			});
				
			if (dialog.getCanceled()) {
				logger.debug("[Gateway starts] EXITING - Login Canceled ");
				isStarted=false;
				Display.getDefault().syncExec(new Runnable() {
					public void run() {
						progressBar.setSelection(10);
						progressBar.setVisible(false);
						obsCLabel.setText("");
						MainShell.getToolBarFooterComposite().getGatewayItem().setImage(Constant.IMAGE_GATEWAY_RED);
					}
				});				
				return;
			}
			username = dialog.getUsername();
			password = dialog.getPassword();
	
			try {
				String scriptProxy = "start_proxy.sh";
				if ( !System.getProperty("os.name").contains("Linux")){
					scriptProxy = "start_proxy.bat";
				}
				
				String cmd = WORKSPACE+"/"+Database.WORKSPACE_NAME+"/"+scriptProxy+" "+Database.WORKSPACE_NAME+" "+proxy+" "+proxyPort+" "+username+" "+password;
				logger.debug("[Gateway starts] EXITING - cmd = "+WORKSPACE+"/"+Database.WORKSPACE_NAME+"/"+scriptProxy+" "+Database.WORKSPACE_NAME+" "+proxy+" "+proxyPort+" "+username+" XXXX");
				Process process = Runtime.getRuntime().exec(cmd); 
				
				InputStream in = process.getInputStream();
				StringBuilder strb = new StringBuilder();
				int ch;
				while((ch = in.read()) != -1) {
					String valor = String.valueOf((char)ch);
					strb.append( valor );
				}
	
				logger.debug("[Gateway starts] RESULT = "+strb.toString());
				if ( strb.toString().contains(Database.WORKSPACE_NAME+" "+proxy+" was started successfully") 
		         ||  strb.toString().contains(Database.WORKSPACE_NAME+" "+proxy+" was up and running") ){
					logger.info(Database.WORKSPACE_NAME+" "+proxy+" is up and running !");
				} else {
					logger.info("The dynamic port was not started, please validate !");
					MainShell.sendMessage("The proxy service was not started, please validate ! ");
					Display.getDefault().syncExec(new Runnable() {
						public void run() {
							progressBar.setSelection(10);
							progressBar.setVisible(false);
							obsCLabel.setText("");
							MainShell.getToolBarFooterComposite().getGatewayItem().setImage(Constant.IMAGE_GATEWAY_RED);
						}
					});
					return;
				}
			} catch ( IOException e ){
				logger.error("[ IOException ] - "+e.getMessage());
			}
		}
		
		Display.getDefault().syncExec(new Runnable() {
			public void run() {
				count = count+2;
				progressBar.setSelection(count);
			}
		});
			
		String gateway = socks5Property.getGateway();
		String gatewayPort = socks5Property.getPort();
		if ( gateway != null && !gateway.isEmpty() ){
			
			if( gatewayPort==null ||  gatewayPort.isEmpty()){
				logger.debug("[Gateway starts] EXITING - Gateway port can not be null or empty ");
				isStarted=false;
				MainShell.sendMessage("Gateway port, can not be null or empty");
				Display.getDefault().syncExec(new Runnable() {
					public void run() {
						progressBar.setSelection(10);
						progressBar.setVisible(false);
						obsCLabel.setText("");
						MainShell.getToolBarFooterComposite().getGatewayItem().setImage(Constant.IMAGE_GATEWAY_RED);
					}
				});				
				return;				
			}
			
			final SshAuthenticationDialog dialog = new SshAuthenticationDialog(gateway);
			Display.getDefault().syncExec(new Runnable() {
				public void run() {
					dialog.setLabelTitle("Serve : "+Database.WORKSPACE_NAME+" Gateway");
					dialog.openShell();
				}
			});
				
			if (dialog.getCanceled()) {
				Display.getDefault().syncExec(new Runnable() {
					public void run() {
						progressBar.setSelection(10);
						progressBar.setVisible(false);
						obsCLabel.setText("");
						MainShell.getToolBarFooterComposite().getGatewayItem().setImage(Constant.IMAGE_GATEWAY_RED);
					}
				});	
				return;
			}
			username = dialog.getUsername();
			password = dialog.getPassword();
	
			
			try {
				if( proxyPort==null ||  proxyPort.isEmpty()){
					proxyPort = "0000";
				}
				
				String scriptGateway = "start_gateway.sh";
				if ( !System.getProperty("os.name").contains("Linux")){
					scriptGateway = "start_gateway.bat";
				}

				String cmd = WORKSPACE+"/"+Database.WORKSPACE_NAME+"/"+scriptGateway+" "+Database.WORKSPACE_NAME+" "+gateway+" "+gatewayPort+" "+proxyPort+" "+username+" "+password;
				Process process = Runtime.getRuntime().exec(cmd); 
				
				InputStream in = process.getInputStream();
				StringBuilder strb = new StringBuilder();
				int ch;
				while((ch = in.read()) != -1) {
					String valor = String.valueOf((char)ch);
					strb.append( valor );
				}
	
				logger.info("Result = "+strb.toString());
				if ( strb.toString().contains(Database.WORKSPACE_NAME+" "+gateway+" was started successfully") 
		         ||  strb.toString().contains(Database.WORKSPACE_NAME+" "+gateway+" was up and running") ){
					logger.info(Database.WORKSPACE_NAME+" "+gateway+" is up and running !");
					isStarted = true;
				} else {
					logger.info("The dynamic port was not started, please validate !");
					isStarted = false;
				}
			} catch ( IOException e ){
				logger.error("[ IOException ] - "+e.getMessage());
				isStarted = false;
				Display.getDefault().syncExec(new Runnable() {
					public void run() {
						progressBar.setSelection(10);
						progressBar.setVisible(false);
						obsCLabel.setText("");
					}
				});	
			}
		}

		
		Display.getDefault().syncExec(new Runnable() {
			public void run() {
				progressBar.setSelection(10);
				progressBar.setVisible(false);
				obsCLabel.setText("");
				if( isStarted ){
					MainShell.getToolBarFooterComposite().getGatewayItem().setImage(Constant.IMAGE_GATEWAY_GREEN);
				} else {
					MainShell.getToolBarFooterComposite().getGatewayItem().setImage(Constant.IMAGE_GATEWAY_RED);
				}
			}
		});			
		
	}

	public void stop() {
		
		logger.debug("Stopping gateway");
		if( TerminalProperty.getSocks5PropertyList() == null || TerminalProperty.getSocks5PropertyList().size() == 0   ) {
			MainShell.sendMessage("Gateway is not configured");
			logger.debug("[Gateway starts] EXITING - TerminalProperty.getSocks5PropertyList() == null or size() == 0");
			return;
		}
		
		Socks5Property socks5Property = null;
		
		for ( Socks5Property socks5Prop : TerminalProperty.getSocks5PropertyList() ){
			socks5Property = socks5Prop;
			break;
		}
		
		if ( socks5Property.getGateway() == null || socks5Property.getGateway().isEmpty() 
		  || socks5Property.getPort()==null || socks5Property.getPort().isEmpty()){
			isStarted = false;
			logger.debug("[Gateway stops] EXITING - socks5Property.getGateway() == null or size() == 0 ");
			return;
		}
		
		String WORKSPACE = System.getenv("MXD_WORKSPACE");
		logger.info(WORKSPACE);		
		
		String proxy = socks5Property.getProxy();
		String proxyPort = socks5Property.getProxyPort();
		logger.debug("[Gateway starts] proxy = "+proxy);
		if ( proxy != null && !proxy.isEmpty() ){
			
			if( proxyPort==null ||  proxyPort.isEmpty()){
				logger.debug("[Gateway stops] EXITING - Proxy port can not be null or empty ");
				isStarted=false;
				MainShell.sendMessage("Proxy port can not be null or empty");
				Display.getDefault().syncExec(new Runnable() {
					public void run() {
						MainShell.getToolBarFooterComposite().getGatewayItem().setImage(Constant.IMAGE_GATEWAY_RED);
					}
				});				
				return;				
			}
			
			try {
				String scriptProxy = "stop_gateway.sh";
				if ( !System.getProperty("os.name").contains("Linux")){
					scriptProxy = "stop_gateway.bat";
				}
				
				String cmd = WORKSPACE+"/"+Database.WORKSPACE_NAME+"/"+scriptProxy+" "+Database.WORKSPACE_NAME+" "+proxy+" "+proxyPort;
				logger.debug("[Gateway stops] EXITING - cmd = "+WORKSPACE+"/"+Database.WORKSPACE_NAME+"/"+scriptProxy+" "+Database.WORKSPACE_NAME+" "+proxy+" "+proxyPort);
				Process process = Runtime.getRuntime().exec(cmd); 
				
				InputStream in = process.getInputStream();
				StringBuilder strb = new StringBuilder();
				int ch;
				while((ch = in.read()) != -1) {
					String valor = String.valueOf((char)ch);
					strb.append( valor );
				}
	
				logger.debug("[Gateway stops] RESULT = "+strb.toString());
				if ( strb.toString().contains(Database.WORKSPACE_NAME+" "+proxy+" was stopped successfully") 
		         ||  strb.toString().contains(Database.WORKSPACE_NAME+" "+proxy+" was down") ){
					logger.info(Database.WORKSPACE_NAME+" "+proxy+" was stopped !");
				}
				
				Display.getDefault().syncExec(new Runnable() {
					public void run() {
						MainShell.getToolBarFooterComposite().getGatewayItem().setImage(Constant.IMAGE_GATEWAY_RED);
					}
				});		
				return;
			} catch ( IOException e ){
				logger.error("[ IOException ] - "+e.getMessage());
			}
		}
		
		String gateway = socks5Property.getGateway();
		String gatewayPort = socks5Property.getPort();
		if ( gateway != null && !gateway.isEmpty() ){
			
			if( gatewayPort==null ||  gatewayPort.isEmpty()){
				logger.debug("[Gateway stops] EXITING - Gateway port can not be null or empty ");
				MainShell.sendMessage("Gateway port, can not be null or empty");
				return;				
			}
			
			try {
			
				String scriptGateway = "stop_gateway.sh";
				if ( !System.getProperty("os.name").contains("Linux")){
					scriptGateway = "stop_gateway.bat";
				}

				String cmd = WORKSPACE+"/"+Database.WORKSPACE_NAME+"/"+scriptGateway+" "+Database.WORKSPACE_NAME+" "+gateway+" "+gatewayPort;
				Process process = Runtime.getRuntime().exec(cmd); 
				
				InputStream in = process.getInputStream();
				StringBuilder strb = new StringBuilder();
				int ch;
				while((ch = in.read()) != -1) {
					String valor = String.valueOf((char)ch);
					strb.append( valor );
				}
	
				logger.info("Result = "+strb.toString());
				isStarted = false;
				
				Display.getDefault().syncExec(new Runnable() {
					public void run() {
						MainShell.getToolBarFooterComposite().getGatewayItem().setImage(Constant.IMAGE_GATEWAY_RED);
					}
				});			
				
			} catch ( IOException e ){
				logger.error("[ IOException ] - "+e.getMessage());
			}
		}
		
		
	}

	public boolean checkStatus(String username, String password) {

		SSHConsoleService ssh = new SSHConsoleService();
		try {
			ssh.connect("localhost", username, password);

			String PIDs = ssh
					.executeCommand("ps -ef | grep -v grep | grep NumberOfPasswordPrompts=1 | awk '{ print $2 }' | xargs");
			StringTokenizer token = new StringTokenizer(PIDs, " ");
			if (token.countTokens() >= 2) {
				isStarted = true;
				return true;
			}

		} catch (UnknownHostException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (SocketException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (JSchException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} finally {
			ssh.disconnect();
		}

		isStarted = false;
		return false;
	}

}
