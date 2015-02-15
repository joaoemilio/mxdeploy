package com.mxscript.swt.event;

import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.FileReader;

import org.apache.log4j.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

import xml.module;
import xml.modulegroup;

import com.mxdeploy.api.domain.Database;
import com.mxdeploy.images.Constant;
import com.mxdeploy.plugin.event.EventHandler;
import com.mxscript.swt.CTabItemModuleViewer;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

public class LoadTreeModuleEvent implements EventHandler {

	static Logger logger = Logger.getLogger(LoadTreeModuleEvent.class);
	private CTabItemModuleViewer cTabItemModuleViewer = null;
	
	public LoadTreeModuleEvent(CTabItemModuleViewer cTabItemModuleViewer){
		this.cTabItemModuleViewer = cTabItemModuleViewer;
	}
	
	@Override
	public void execute() {
		Tree tree = cTabItemModuleViewer.getTreeModule();
		tree.removeAll();

		modulegroup _modulegroup_ = (modulegroup)cTabItemModuleViewer.getComboModule().getData(cTabItemModuleViewer.getComboModule().getText());
		String dir = _modulegroup_.getDir();
		
		File file = new File(Database.MODULE_PATH+"/"+dir);
		if ( !file.exists() ){
			 logger.info("Can not load Module "+dir+", because it doesn't exist");
			 return;
		}
		
		FileFilter directoryFilter = new FileFilter() {
			public boolean accept(File file) {
				return file.isDirectory();
			}
		};
		
		FileFilter moduleFilter = new FileFilter() {
			public boolean accept(File file) {
				return file.getName().equals("module.xml");
			}
		};
		
		File[] folders = file.listFiles(directoryFilter);
		for ( File folder : folders ){
			  File[] moduleFiles = folder.listFiles(moduleFilter);
			  for ( File moduleFile : moduleFiles ){
				  module _module_ = loadXML(moduleFile.getAbsolutePath());
				  _module_.setFullpath(folder.getAbsolutePath()+"/"+_module_.getFile());
				  TreeItem treeItem = new TreeItem(tree,SWT.NULL);
				  treeItem.setText(_module_.getName());
				  treeItem.setImage(Constant.IMAGE_MODULE_ITEM);
				  treeItem.setData(_module_);
			  }
		}
	}
	
	public module loadXML(String path) { 
		module _module_;
		XStream xstream = new XStream(new DomDriver());
		try {
			_module_ = (module) xstream.fromXML(new FileReader( path ) );
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		}
		return _module_;
	}		

}
