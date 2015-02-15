package com.mxscript.swt.event;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

import com.mxdeploy.images.Constant;
import com.mxdeploy.plugin.event.EventHandler;
import com.mxdeploy.swt.MainShell;
import com.mxscript.swt.BeanShellFormComposite;

public class ImportServerEvent implements EventHandler {
	
	private BeanShellFormComposite beanShellFormComposite;
	
	public ImportServerEvent(BeanShellFormComposite beanShellFormComposite){
		this.beanShellFormComposite = beanShellFormComposite;
	}
	
	public void execute() {
		beanShellFormComposite.getTable().removeAll();
		if( beanShellFormComposite.getTable().getColumnCount()>2 ){
			TableColumn[] tableColumn = beanShellFormComposite.getTable().getColumns();
			for(int i=2; i < tableColumn.length; i++){
				tableColumn[i].dispose();
			}
		}
		
		FileDialog dialog = new FileDialog(MainShell.sShell,SWT.OPEN);
		dialog.setText("Import Servers");
		dialog.setFilterPath("C:/");
        String[] filterExt = { "*.txt" };
        dialog.setFilterExtensions(filterExt);
		String filePath = dialog.open();
		
		if(filePath==null || filePath.trim().length()==0){
			return;
		}
		
		File file = new File(filePath);
		if(!file.exists()){
			MainShell.sendMessage("File not found");
			return;
		}
		
		List<String> listServer = listServer(filePath);
		
		
		boolean createColumn = true;
		for(String line : listServer){
			StringTokenizer token = new StringTokenizer(line,";");
			
			if(createColumn){
				for(int i=1; i <= token.countTokens()-1; i++){
					TableColumn tcolumn = new TableColumn(beanShellFormComposite.getTable(),SWT.NONE);
					tcolumn.setText("Column"+i);
					tcolumn.setWidth(100);	
				}
				createColumn=false;
			}
			
			TableItem tableItem =  new TableItem(beanShellFormComposite.getTable(),SWT.NONE);
			int count=0;
			while( token.hasMoreTokens() ){
				if(count==0){
				   tableItem.setText(token.nextToken());
				   tableItem.setImage(Constant.IMAGE_SERVER);
				} else {
				   tableItem.setText(count+1, token.nextToken());
				}
				count++;
			}
		}
		
		beanShellFormComposite.getToolBarConsoleComposite().getRunToolItem().setEnabled(true);
		beanShellFormComposite.getDeleteServerToolItem().setEnabled(true);		
		
	}
	
	public List<String> listServer(String filePath){
	    List<String> listServer = new ArrayList<String>();
	    
		FileReader input = null;
		try {
			input = new FileReader( filePath );
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} 
		
		BufferedReader bufRead = new BufferedReader(input);
		String line; // String that holds current file
		try {
			while ( ( line = bufRead.readLine())!=null ) {
				line = line.trim();
				line = line.replace("\r", "");
				line = line.replace("\n", "");
				
			    listServer.add(line); 
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		try { 
			bufRead.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return listServer;
		
	}	

}
