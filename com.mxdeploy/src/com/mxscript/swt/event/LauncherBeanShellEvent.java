package com.mxscript.swt.event;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TreeItem;

import bsh.EvalError;
import bsh.Interpreter;

import com.mxdeploy.api.domain.Database;
import com.mxdeploy.api.domain.Property;
import com.mxdeploy.plugin.event.EventHandler;
import com.mxdeploy.swt.MainShell;
import com.mxdeploy.swt.dialogs.authentication.TaskAuth;
import com.mxscript.Task;
import com.mxscript.TaskManager;
import com.mxscript.ThreadPoolTask;
import com.mxscript.logger.Logger;
import com.mxscript.swt.BeanShellFormComposite;

public class LauncherBeanShellEvent implements EventHandler, Runnable {
	
	protected BeanShellFormComposite composite;
	protected String scriptName;
	//private PasswordManagerService service = new PasswordManagerService();
	protected boolean runAll = false;
	protected int itemCount=0;
	protected String filePath = null;
	private String stringName = null;
	protected String hostnameItem;
	protected int threadNumber = 20;
	protected ThreadPoolTask threadPoolTask;
	protected Logger logger;
	protected Map<String, String> map = null;
	protected ArrayList<TableValue> tableContents = new ArrayList<TableValue>();
	protected boolean running = false;
	protected Integer numberOfColumns = 2;
	protected  List<Task> tasktList;
	//protected List<String> lineList = new ArrayList<String>();
	protected String aux;
	private   int lineNumber=0;
	private TaskManager taskManager = null;
	private List<Property> localPropertyList = new ArrayList<Property>();
	private List<Property> globalPropertyList = new ArrayList<Property>();
	
	protected Thread terminarThread = null;
	private List<TaskAuth> taskAuthList;
	
	public LauncherBeanShellEvent(final BeanShellFormComposite composite, String filePath, String stringName, List<TaskAuth> taskAuthList, boolean runAll){
		this.composite = composite;
		
		this.runAll = runAll;
		this.taskAuthList = taskAuthList;
		this.filePath = filePath;
		this.stringName = stringName;
		Display.getDefault().syncExec(new Runnable(){
			public void run(){ 
				threadNumber = new Integer(composite.getToolBarConsoleComposite().getCombo().getText());
				TreeItem[] treeItems = composite.getTreeProperty().getItems();
				
				if( treeItems[0]!= null && treeItems[0].getItems().length > 0 ){
					for( TreeItem treeItem : treeItems[0].getItems() ){
						 globalPropertyList.add((Property)treeItem.getData());
					}
				}

				if( treeItems[1]!= null && treeItems[1].getItems().length > 0 ){
					for( TreeItem treeItem : treeItems[1].getItems() ){
						localPropertyList.add((Property)treeItem.getData());
					}
				}
			}
		});
		
	}
	
	public TaskManager getTaskManager() {
		return this.taskManager;
	}
	
	public void execute() {
		composite.setStopBeanShell(false);
		
		this.tasktList = new ArrayList<Task>();
		 
		try {
			logger = new Logger(composite,scriptName);	
			logger.debug("preparing script: " + filePath );					
			Display.getDefault().syncExec(new Runnable(){
				public void run(){ 
					composite.getToolBarConsoleComposite().getCombo().setText( String.valueOf(threadNumber) );	
					setLineNumber(composite.getTable().getItemCount());
				}
			});
			
			Interpreter bshInit = createInterpreter(scriptName);
			
			logger.debug("executing method init()");
			bshInit.eval("init()");
			
			logger.debug("setting concurrent thread number");
			Display.getDefault().syncExec(new Runnable(){
				public void run(){ 
					composite.getToolBarConsoleComposite().getCombo().setText( String.valueOf(threadNumber) );	
				}
			});
		
			int count = taskAuthList.size();

			logger.debug("start threads");
			taskManager = new TaskManager(threadNumber);
			for(int i=0; i < count; i++){
				if(composite.isStopBeanShell()){
				   break;
				}
				TaskAuth  taskAuth = taskAuthList.get(i);
				logger.debug("adding task for host: " + taskAuth.getHostname());
				final Task task = new Task(this, this.filePath, this.scriptName, logger, taskAuth.getIndexItem(),taskAuth.getHostname());
				task.setUsername(taskAuth.getUsername());
				task.setPassword(taskAuth.getPassword());
				task.setFirst(taskAuth.isFirst());
				task.setLocalPropertyList(localPropertyList);
				task.setGlobalPropertyList(globalPropertyList);
				Display.getDefault().syncExec(new Runnable(){
					public void run(){ 
						composite.getTable().getItem(task.getIndexItem()).setData(task);	
					}
				});				
				taskManager.addTask(task);
				this.tasktList.add(task);
			}
			
			taskManager.start();
			
			//wait all tasks to complete
			while( (!taskManager.isTaskListCompleted()) && (!taskManager.isStopped() ) ) {
				Thread.sleep(3000);
			}
			logger.info("Beanshell: " + filePath + " has been executed.");
			logger.info("Average Time: " + taskManager.getAverageTime());
		} catch (FileNotFoundException ef) {
			ef.printStackTrace();
			logger.error(ef.getMessage());
		} catch (IOException eio) {
			if (!composite.isStopBeanShell()){
				eio.printStackTrace();
				logger.error(eio.getMessage());
			}
		} catch (EvalError e) { 
			if (!composite.isStopBeanShell()){
				e.printStackTrace();
				logger.error("======================================================================================================");
				logger.error("File Path : "+e.getErrorSourceFile());
				logger.error(e.getScriptStackTrace());
				logger.error(e.getMessage());
				logger.error("Line [ "+e.getErrorLineNumber()+" ] " + e.getErrorText());			
				logger.error("======================================================================================================");
			}
		} catch(Throwable t){
			if (!composite.isStopBeanShell()){
			    t.printStackTrace();
			    logger.error(t.getMessage());	
			}
		} finally {		
		
			logger.info("disconnecting open connections");
			taskManager.disconnectAll();
			
		}
		
		
	}

	public void updateTable() {
		for(TableValue tv: tableContents){
			setColumnValue(tv.getLine(), tv.getColumn(), tv.getValue());
		}
	}
	
//	public Map openDynamicForm(String[] fields) {
//		
//		Display.getDefault().syncExec(new Runnable(){
//			public void run(){ 
//				logger.debug("criar dialog");
//				DynamicFormComposite dialog = new DynamicFormComposite(MainShell.sShell);
//				logger.debug("setar campos din�micos");
//				dialog.setInputFields(new String[]{"User", "Password", "New Password"});
//				logger.debug("abrir dialog com campos din�micos");
//				map = dialog.open();
//				logger.debug("verificar dados informados em cada campo din�mico");
//				Set<String> set = map.keySet();
//				for(String key: set){
//					String value = (String)map.get(key);
//				    logger.debug(key + "=" + value);
//				}
//			}
//		});
//		return map;
//	}
	

//	private synchronized String getHostnameItem(final int index){
//		Display.getDefault().syncExec(new Runnable(){
//			public void run(){ 
//				hostnameItem = composite.getTable().getItem(index).getText(0);			
//			}
//		});			
//		return hostnameItem;
//	}	
	
	public int getItemCount(){
		Display.getDefault().syncExec(new Runnable(){
			public void run(){ 
				itemCount = composite.getTable().getItemCount();			
			}
		});			
		return itemCount;
	}
	
	
	private String getScriptName(){
		Display.getDefault().syncExec(new Runnable(){
			public void run(){ 
				scriptName = MainShell.getCTopTabFolder().getSelection().getText();			
			}
		});		
		
		return scriptName;
	}
	
	public void print(final String valeu){
		Display.getDefault().syncExec(new Runnable(){
			public void run(){ 
				String log = valeu + " " +valeu;
				String txt = composite.getStyledText().getText()+"\n "+log;	
				composite.getStyledText().setText(txt);
			}
		});		
	}
	
	public synchronized void addColumn(final String title, final int width){
		Display.getDefault().syncExec(new Runnable(){
			public void run(){ 
				for(int i=0; i<composite.getTable().getColumnCount(); i++){
					if( composite.getTable().getColumn(i).getText().equals(title) ){
						return;
					}
				}
				TableColumn tcolumn = new TableColumn(composite.getTable(),SWT.NONE);
				tcolumn.setText(title);
				tcolumn.setWidth(width);
				numberOfColumns = composite.getTable().getColumnCount();
			}
		});			
	}

	public synchronized void addColumnValue(final int line, final int column, final String value){
		TableValue tv = new TableValue();
		tv.setColumn(column);
		tv.setLine(line);
		tv.setValue(value);
		tableContents.add(tv);
	}
	
	public synchronized void setColumnValue(final int indexItem, final int indexColumn, final String value){
		Display.getDefault().syncExec(new Runnable(){
			public void run(){ 
				if( value != null){
					composite.getTable().getItem(indexItem).setText(indexColumn, value);
				}
			}
		});
	}
	
	public synchronized void setColumnTitle(final int indexColumn, final String value){
		Display.getDefault().syncExec(new Runnable(){
			public void run(){ 
				if(value != null){
					composite.getTable().getColumn(indexColumn).setText(value);
				}
			}
		});
	}	
	
	public synchronized String getColumnValue(final int indexItem, final int indexColumn){
		Display.getDefault().syncExec(new Runnable(){
			public void run(){ 
				aux = composite.getTable().getItem(indexItem).getText(indexColumn);
			}
		});
		return aux;
	}	
	
//	public synchronized String getColumnValue(final int indexItem, final int indexColumn){
//			String line = lineList.get(indexItem);
//			if(indexColumn==0){
//			   return line;
//			} else {
//				StringTokenizer token = new StringTokenizer(line,";");
//				String value;
//				for(int i=0; i<=indexColumn;i++){
//					value = token.nextToken();
//					System.out.println(value);
//				}
//				value = token.nextToken();
//				System.out.println(value);
//				return value;
//			}
//	}
	
	public Interpreter createInterpreter(String scriptName) throws EvalError, FileNotFoundException, IOException {
		logger.debug("creating beanshell interpreter"); 
		Interpreter bsh = new Interpreter();
		
		logger.debug("setting up beanshell variables: shell, event, logger, map" );
		bsh.set("shell", MainShell.sShell);
		bsh.set("event", this);
		bsh.set("logger", logger);
		//bsh.set("map", new HashMap());
		
		logger.debug("adding beanshell library");
		for(String commandFile : getCommandList() ){
			logger.debug("adding lib: " + commandFile);
			bsh.source(Database.HOME+"/lib/commands/"+commandFile); 
		}
		
		logger.debug("loading beanshell: " + filePath);
		bsh.source(filePath);
		
		return bsh;
		
	}
	
	public static List<String> getCommandList(){
		
		List<String> commandNameList = new ArrayList<String>();
		
		File file = new File(Database.HOME+"/lib/commands/");
		File[] files = file.listFiles();
		for(int i=0; i < files.length; i++){
			if(!files[i].isDirectory()){
				if(files[i].getName().endsWith(".bsh")){
					commandNameList.add(files[i].getName());
				}
			}
		}
		
		return commandNameList;
	}	
	
	public ThreadPoolTask getThreadPoolTask() { 
		return threadPoolTask;
	}

	public Thread getTerminarThread() {
		return terminarThread;
	}

	public Logger getLogger() {
		return logger;
	}
	
	public Task getTask(int index){
		if( tasktList.size() == 1 ){
			return tasktList.get(0);
		}
		return tasktList.get(index);
	}
	
	public List<Task> getTaskList(){
		return tasktList;
	}



	public int getThreadNumber() {
		return threadNumber;
	}



	public void setThreadNumber(int threadNumber) {
		this.threadNumber = threadNumber;
	}



	public int getLineNumber() {
		return lineNumber;
	}



	public void setLineNumber(int lineNumber) {
		this.lineNumber = lineNumber;
	}

	@Override
	public void run() {
		execute();
	}	

}
