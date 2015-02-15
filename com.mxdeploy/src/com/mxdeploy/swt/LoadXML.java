package com.mxdeploy.swt;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.ProgressBar;

import com.mxdeploy.api.domain.Database;

public class LoadXML {
	
	private int count = 0;
	
	private List<String> getProjectNames() {
		List<String> projectNameList = new ArrayList<String>();

		File file = new File(Database.getProjectPath());
		if(file.exists()){
			System.out.println(file.getAbsolutePath()); 
		}
		File[] files = file.listFiles();
		if(files != null){
			if (files != null || files.length > 0) {
				for (int i = 0; i < files.length; i++) {
					if (files[i].isDirectory()) {
						if (!files[i].getName().startsWith("."))
							projectNameList.add(files[i].getName());
					}
				}
			}
		}
		
		return projectNameList;
	}

	public void loadAllProjectXML(final ProgressBar progressBar, final Label label) {
		final List<String> projectNameList = getProjectNames();
		//List<Project> projectList = new ArrayList<Project>();
		List<Thread> threads = new ArrayList<Thread>();
		
		Display.getDefault().syncExec(new Runnable() {
			public void run() {
				progressBar.setMaximum(projectNameList.size());
			}
		});

		count = 0;
		if (!projectNameList.isEmpty()) {
			for (final String projectName : projectNameList) {
				Display.getDefault().syncExec(new Runnable() {
					public void run() {
						label.setText("   Loading Project : " + projectName);
						progressBar.setSelection(count++);
					}
				});
			    Thread threadRun = new Thread() {
				    public void run() {
				    	Database.getInstance().loadProjectXML(projectName);
				    }
			    };
			    threads.add(threadRun);
			    threadRun.start();				
				//Project project = Database.getInstance().loadProjectXML(projectName);
				//projectList.add(project);
			}
		}
		
		boolean needBreak = true;
		while(true){
			needBreak = true;
			for( Thread thread : threads){
				if( thread.isAlive() ){
					try {
						Thread.sleep(300);
						needBreak=false;
						break;
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
			if(needBreak){ 
				break;
			}
		}		

		//Database.getInstance().setProjects(projectList);
	}

//	public void loadAllPropertyXML(final ProgressBar progressBar,	final Label label) {
//		//List<Property> properties = new ArrayList<Property>();
//		List<Thread> threads = new ArrayList<Thread>();
//		
//		File file = new File(Database.getPropertyPath());
//
//		final File[] files = file.listFiles(new FilenameFilter() {
//			public boolean accept(File dir, String name) {
//				if (name.toLowerCase().endsWith(".xml")) {
//					return true;
//				}
//				return false;
//			}
//		});
//
//		Display.getDefault().syncExec(new Runnable() {
//			public void run() {
//				if(files!=null){
//				   progressBar.setMaximum(files.length);
//				}
//			}
//		});
//
//		count = 0;
//		if (files != null && files.length > 0) {
//			for (int i = 0; i < files.length; i++) {
//				final String name = files[i].getName();
//				Display.getDefault().syncExec(new Runnable() {
//					public void run() {
//						label.setText("   Loading Server : " + name);
//						progressBar.setSelection(count++);
//					}
//				});
//				
//			    Thread threadRun = new Thread() {
//				    public void run() {
//				    	Database.getInstance().loadPropertyXML(name);
//				    }
//			    };
//			    threads.add(threadRun);
//			    threadRun.start();
//			    
//				//Property property = (Property) Database.getInstance().loadPropertyXML(name);
//				//properties.add(property);
//
//			}
//		}
//		
//	}
	

}

