package com.mxdeploy.swt.command;

import java.util.ArrayList;
import java.util.List;

import com.mxdeploy.api.domain.Command;
import com.mxdeploy.api.domain.CommandItem;
import com.mxdeploy.api.domain.Project;
import com.mxdeploy.api.service.ProjectService;

public class CommandByServerFactory {
	//static Logger logger = Logger.getLogger(AddCommandProjectServerHelper.class);
	
	public static void openDialogCreateCommand(List<String> list){
    	CommandByServerDialog dialog = new CommandByServerDialog(); 
    	dialog.createSShell("New Procedure");
    	dialog.getHelper().addNewTreeItemProjectServer();
    	dialog.getHelper().setEnableServerButton(false);
    	if(list!=null && !list.isEmpty()){
    		dialog.getHelper().addCommandItemInTable(list);
    	}
    	dialog.openShell();
	}
	
    public static void openAddCommandItemDialog(CommandByServerHelper commandByServerHelper){ 
    	CommandItemDialog dialog = new CommandItemDialog();
    	dialog.createSShell();
    	dialog.getHelper().setCommandByServerHelper(commandByServerHelper);
    	dialog.getHelper().fillStyleText();
    	dialog.openShell();
    }
    
	public static void openEditCommandDialog(Project project, Command command, boolean openCommandItem){
    	CommandByServerDialog dialog = new CommandByServerDialog(); 
    	dialog.createSShell("Edit Procedure");
    	
    	dialog.getHelper().setCommand(command);	
    	
    	dialog.getHelper().loadTreeItemProjectServer(project.getId(), command);
    	
    	List<CommandItem> commandItemList = command.getCommandItens();
    	if(commandItemList!=null && !commandItemList.isEmpty()){
    	   for(CommandItem commandItem : commandItemList){
    		   dialog.getHelper().addCommandItemInTable(commandItem.getCommandSSH());
    	   }
    	}
    	
    	//dialog.getHelper().setEnableServerButton(false);
    	
    	dialog.getHelper().setStatusUpdate(command.getId()); 
    	
    	dialog.openShell(); 
    	
    	if(openCommandItem){
    		dialog.getHelper().openAddCommandItemDialog();
    	}
	} 
	
	public static void openAddCommandProjectServerDialog(String idProject, CommandByServerHelper commandByServerHelper) {
			ProjectService projectService = new ProjectService();
			Project projectResult = projectService.obtemProjectAndServerByCommand(idProject,commandByServerHelper.commandEdit.getId());
            List<Project> listProject = new ArrayList<Project>();
            listProject.add(projectResult);
            
			AddCommandProjectServerDialog dialog = new AddCommandProjectServerDialog();
			dialog.createSShell();

			dialog.getHelper().setCommand(commandByServerHelper.commandEdit);
			dialog.getHelper().setProject(projectResult);
			//dialog.getHelper().loadComboBox();

			dialog.getHelper().setListProject(listProject);
			dialog.getHelper().setCommandByServerHelper(commandByServerHelper);

			if (listProject != null && !listProject.isEmpty()) {
				for (Project project : listProject) {
					dialog.getHelper().addTreeItemSelectedTree(project);
				}
			}

			dialog.openShell();
			dialog.getHelper().search( projectResult.getId() );
	}
	

}
