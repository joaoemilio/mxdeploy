package com.mxdeploy.swt.command;

import org.eclipse.swt.widgets.Shell;

import com.mxdeploy.api.domain.Command;
import com.mxdeploy.api.domain.Project;
import com.mxdeploy.api.domain.Server;
import com.mxdeploy.api.domain.ServerCommand;
import com.mxdeploy.api.service.CommandService;
import com.mxdeploy.swt.MainShell;
import com.mxterminal.swt.view.helper.ServerHelper;

public class CommandHelper {
	//static Logger logger = Logger.getLogger(CommandHelper.class);
	
	private CommandComposite composite = null;
	
	private ServerHelper serverHelper = null;
	
	public CommandHelper(CommandComposite composite) {
	   this.composite = composite;
	   serverHelper = MainShell.getCTopTabFolder().getServerHelper();
	}
	
	protected void close(){
		Shell shell = (Shell)composite.getParent();
		shell.close();
		shell.dispose();
	}
 
	protected boolean create(){
		Command command = new Command();
		
		if( (composite.nmText.getText()!=null)&&(composite.nmText.getText().length()>0) ){
		  command.setName(composite.nmText.getText());
		} else {
			MainShell.sendMessage("Name is mandatory !");
			return false;
		}
		
		if( (composite.descriptionTextArea.getText()!=null)&&(composite.descriptionTextArea.getText().length()>0) ){
		  command.setDescription(composite.descriptionTextArea.getText());
		}

		Server server = serverHelper.getTerminalHelper().getServer();
		Project project = serverHelper.getTerminalHelper().getProject(); 
		
		CommandService service =  new CommandService();
		service.createCommand(project,command);
		
		ServerCommand serverCommand = new ServerCommand();
		serverCommand.setIdCommand(command.getId());
		serverCommand.setIdServer(server.getId());
		serverCommand.setIdProject(project.getId());
		
		service.addServerCommand(project, serverCommand);

		return true;
	}
	
	protected void refreshServerCommandTable(){
		serverHelper.getViewHelper().getTreeProcedure().removeAll();
		serverHelper.loadTable();
	}
	
	

}
