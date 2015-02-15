package com.mxdeploy.swt.project.helper;

import java.util.List;

import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TreeItem;

import com.mxdeploy.api.domain.Project;
import com.mxdeploy.api.service.ProjectService;
import com.mxdeploy.swt.MainShell;
import com.mxdeploy.swt.explorer.event.AddInMyProjectTreeItemEventHandler;
import com.mxdeploy.swt.explorer.helper.ControlPanelHelper;
import com.mxdeploy.swt.project.ProjectComposite;

public class ProjectHelper {
	//static Logger logger = Logger.getLogger(ProjectHelper.class);
	
	private ProjectComposite composite = null;
	
	private Project project = null;
	
	//String idProject = null;
	
	Project oldProject = null;
	
	public ProjectHelper(ProjectComposite composite) {
		this.composite = composite;
	}
	
	public boolean save(){
		this.project = new Project();
		ProjectService service =  new ProjectService();
		
		if( (composite.aliasText.getText()!=null)&&(composite.aliasText.getText().length()>0) ){
			String alias = composite.aliasText.getText();
			if ( alias.contains("*")||alias.contains("\\")||alias.contains("/")||alias.contains("?")
			   ||alias.contains("\"")||alias.contains("<")||alias.contains(">")||alias.contains("|")){
				 MainShell.sendMessage("Alias can not contain \\ / : * ? \" < > | ! ");
				 return false;
			} else {
			  this.project.setAlias(composite.aliasText.getText());
			}
		} else {
			MainShell.sendMessage(" Alias is Mandatory ! ");
			composite.aliasText.setFocus();
			return false;
		}
		
		if (oldProject == null) {
			List<Project> list = service.search(project);
			if( !list.isEmpty() ){
				MainShell.sendMessage("Alias already exist !");
				composite.aliasText.setFocus();
				return false;
			}
		}
		
		if( (composite.nmText.getText()!=null)&&(composite.nmText.getText().length()>0) ){
			this.project.setName(composite.nmText.getText());
		} else {
			MainShell.sendMessage("Name is Mandatory !");
			composite.nmText.setFocus();
			return false;
		}
		
		this.project.setDescription(composite.descriptionTextArea.getText());
		this.project.setIsOpen(1);

		if (oldProject == null) {
			service.create(project);
		} else {
			this.project.setId( oldProject.getId() );
			service.update(this.project);
		}
		
		return true;
	}
	
	public boolean saveDB(){
		this.project = new Project();
		ProjectService service =  new ProjectService();
		
		if( (composite.aliasText.getText()!=null)&&(composite.aliasText.getText().length()>0) ){
			this.project.setAlias(composite.aliasText.getText());
		} else {
			MainShell.sendMessage("Alias is Mandatory !");
			composite.aliasText.setFocus();
			return false;
		}

		if (oldProject == null) {
			List<Project> list = service.search(project);
			if( !list.isEmpty() ){
				MainShell.sendMessage("Alias already exist !");
				composite.aliasText.setFocus();
				return false;
			}
		}

		if( (composite.nmText.getText()!=null)&&(composite.nmText.getText().length()>0) ){
			this.project.setName(composite.nmText.getText());
		} else {
			MainShell.sendMessage("Name is Mandatory !");
			composite.nmText.setFocus();
			return false;
		}
		
		if( (composite.descriptionTextArea.getText()!=null)&&(composite.descriptionTextArea.getText().length()>0) ){
			this.project.setDescription(composite.descriptionTextArea.getText());
		} else {
			this.project.setDescription(" ");
		}
		
		if (oldProject != null) {
			this.project.setId(oldProject.getId());
			this.project.setIsOpen(oldProject.isOpen());
			service.update(this.project);
		} else {
			service.create(this.project);
			AddInMyProjectTreeItemEventHandler.execute(this.project);
		}			
        return true;
	}
	
	public void close(){
		Shell shell = (Shell)composite.getParent();
		shell.close();
		shell.dispose();
	}

	public void loadFields(Project project){
		
		oldProject   = project;
		this.project = MainShell.getControlPanelHelper().getProjectSelectedInMyProjectTree();
		if( (project.getName()!=null)&&(project.getName().trim().length()>0) ){
			composite.nmText.setText(project.getName());
		}
		
		if( (project.getAlias()!=null)&&(project.getAlias().trim().length()>0) ){
			composite.aliasText.setText(project.getAlias().toString());
			composite.aliasText.setEnabled(false);
		}

		if( (project.getDescription()!=null)&&(project.getDescription().trim().length()>0) ){
			composite.descriptionTextArea.setText( project.getDescription() );
		}
		
	}

	public void refreshTreeItem(){
		TreeItem projectThreeItemSelected = MainShell.getControlPanelHelper().getMyProjectTreeItemSelected();
		if( projectThreeItemSelected!=null){
			projectThreeItemSelected.setText(composite.aliasText.getText());
			projectThreeItemSelected.setData(this.project);
		} else {
			AddInMyProjectTreeItemEventHandler.execute(this.project);
		}
	}  	
}
