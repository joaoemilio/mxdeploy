package com.mxterminal.swt.view.helper;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

import com.mxdeploy.AccountConfig;
import com.mxdeploy.api.domain.Command;
import com.mxdeploy.api.domain.CommandItem;
import com.mxdeploy.api.domain.Database;
import com.mxdeploy.api.domain.Project;
import com.mxdeploy.api.domain.Server;
import com.mxdeploy.api.service.CommandService;
import com.mxdeploy.api.service.ServiceException;
import com.mxdeploy.images.Constant;
import com.mxdeploy.swt.MainShell;
import com.mxdeploy.swt.command.CommandByServerFactory;
import com.mxdeploy.swt.dialogs.ExpectDialog;
import com.mxdeploy.swt.dialogs.InputPasswordDialog;
import com.mxssh.ManageGateway;
import com.mxterminal.console.ConsoleWin;
import com.mxterminal.ssh.InteractiveClientSSH;
import com.mxterminal.swt.TerminalFace;
import com.mxterminal.swt.view.CentralTabFolderView;
import com.mxterminal.swt.view.CommandComposite;
import com.mxterminal.swt.view.ToolBarViewComposite;
import com.mxterminal.swt.view.ViewComposite;

public class ToolBarViewHelper {
	static Logger logger = Logger.getLogger(ToolBarViewHelper.class);
	
	private ToolBarViewComposite composite = null;
    private CommandItem commandItem = null;
    private boolean isPause = false; 
    private List<TreeItem> treeItemList = null;
    
    private Date startTime = null;
    private Date endTime = null;
    
    //private String resultLast = null;
    private String resultExpect = null;
    private String command = null;

    private TreeItem itemChild = null;
    private int countPassword = 0;
    
    String runTime;
    String hostname;
    
    private InteractiveClientSSH client;
    private TerminalHelper consoleHelper;
    
	public ToolBarViewHelper(ToolBarViewComposite toolBarConsoleComposite){
		this.composite = toolBarConsoleComposite;
		this.consoleHelper = getTerminalHelper();
	}
	
	/**
	 * @return the runToolItem
	 */
	public ToolItem getRunToolItem() {
		return composite.runToolItem;
	}

	/**
	 * @return the stopToolItem
	 */
	public ToolItem getStopToolItem() {
		return composite.stopToolItem;
	}
	
	public TerminalHelper getTerminalHelper(){
		ViewComposite consoleComposite = (ViewComposite)composite.getParent().getParent();
		return consoleComposite.getTerminalHelper();
	} 
	
	public void save(){
		CentralTabFolderView cTabFolderConsole = (CentralTabFolderView)composite.getParent();	
		if( cTabFolderConsole.getSelection().getControl() instanceof CommandComposite ){
			CommandComposite commandComposite = (CommandComposite)cTabFolderConsole.getSelection().getControl();
			String home = Database.getServerPath() +"/";
			String hostname = getTerminalHelper().getTermianlFace().getHostname();
			String filename = hostname+".txt";
			File file = new File(home+filename);
			
			Writer output;
			
			try {
				output = new BufferedWriter(new FileWriter(file));
			    output.write(commandComposite.getTextArea().getText()+"\r\n");
			    output.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
//			if( commandComposite.getTable().getSelection().length > 0){
//				String templateFilename = null;
//				if(commandComposite.getTable().getSelection()[0].getText().equals("New Template")){
//					templateFilename = UUID.randomUUID().toString()+".template";
//				} else {
//				    templateFilename = (String)commandComposite.getTable().getSelection()[0].getData("filename");
//				}
//				
//				home = Database.WORKSPACE_PATH +"/templates/";
//				file = new File(home+templateFilename);	
//				try {
//					output = new BufferedWriter(new FileWriter(file));
//				    output.write(commandComposite.getContentTemplateTextArea().getText()+"\r\n");
//				    output.close();
//				} catch (IOException e) {
//					e.printStackTrace();
//				} 
//				
//				byte[] bText = commandComposite.getContentTemplateTextArea().getText().getBytes();
//				StringBuffer title = new StringBuffer();
//				title.append("");
//				int count = 0;
//				for(int i=0;bText.length>i;i++){
//					if( bText[i] == 10 || bText[i] == 13 ){
//					   break;	
//					}
//					count++;
//				}
//				byte[] btitle = new byte[count];
//				for(int j=0;count>j;j++){
//					btitle[j]=bText[j];
//				}
//				commandComposite.getTable().getSelection()[0].setText(new String(btitle));
//				commandComposite.getTable().getSelection()[0].setData(commandComposite.getContentTemplateTextArea().getText());
//			}
		}
		composite.saveToolItem.setEnabled(false);
		cTabFolderConsole.saveEnable=false;
	}
	
	
	public void run(){
		consoleHelper = getTerminalHelper();		
		client = consoleHelper.getSSHInteractiveClient();
		
		CentralTabFolderView cTabFolderConsole = (CentralTabFolderView)composite.getParent();	
		if( cTabFolderConsole.getSelection().getControl() instanceof CommandComposite ){
			executeCommandSelected();
			return;
		}
		
		final TreeItem item = consoleHelper.getItemProcedimentoSelecionado();
	    if (!client.isAuthenticated()){ 
	    	sendMessage("Server is not connected.");
	    } else  if (item == null){
	    	sendMessage("There is no item selected.");
	    } else { 
	    	
			if (item.getData() instanceof Command) { 
				final Command cmd = (Command)item.getData();
				item.setExpanded(true);
				
				if( !confirm(cmd.getName()) ){
				  return;	
				}
				
				ThreadRunProcedure threadRun = new ThreadRunProcedure();
			    threadRun.start();
			    
			} else if (item.getData() instanceof CommandItem) {
				if( !confirm( ((CommandItem)item.getData()).getCommandSSH() ) ) {
					return;	
				}

			    Thread threadRun2 = new Thread() {
				    public void run() {
				    	String command = null;
				    	//client.getTermAdapter().setRunningProcedure(true);
						Display.getDefault().syncExec(new Runnable(){
							public void run(){ 
								composite.stopToolItem.setEnabled(true);	
								composite.pauseToolItem.setEnabled(true);
								composite.runToolItem.setEnabled(false);
								//getTerminalFace().getBusy().setBusy(true);
								//toolBarConsoleComposite.menuToolItem.setEnabled(false);
							}
						});
				    	
						try{
							Display.getDefault().syncExec(new Runnable(){
								public void run(){ 
									commandItem = (CommandItem)item.getData();
									item.setImage(Constant.IMAGE_TREE_ITEM_RUNNING);
								}
							});
							
							command = getExpect(commandItem.getCommandSSH());
					        if(command==null){
					        	return;
					        }
					        
					        if(command.contains("breakIf")){
					        	return;
					        }					        
							
						    command = command+"\n";
						    client.getTerminalWin().executeCommand(command);
						
						}finally{
							Display.getDefault().syncExec(new Runnable(){
								public void run(){ 
									item.setImage(Constant.IMAGE_COMMAND_ITEM);
								}
							});
							
							Display.getDefault().syncExec(new Runnable(){
								public void run(){ 
									composite.stopToolItem.setEnabled(false);
									composite.pauseToolItem.setEnabled(false);
									composite.runToolItem.setEnabled(true);
									//getTerminalFace().getBusy().setBusy(false);
									//toolBarConsoleComposite.menuToolItem.setEnabled(true);
									client.getTerminalWin().requestFocus();
								}
							}); 
							//client.getTermAdapter().setRunningProcedure(false);
						}
				    }
			    };
			    threadRun2.start();
			}
		}
	}
	
	public String getExpect(final String strCommandParam){
		String strCommand = strCommandParam;
		Display.getDefault().syncExec(new Runnable(){
			public void run(){ 
				client = consoleHelper.getSSHInteractiveClient();
			}
		});		
		
//		if(consoleHelper.getServer()!=null && consoleHelper.getServer().getProperty()!=null ){
//		  if( consoleHelper.getServer().getProperty() instanceof ProductInstalled){
//		      return strCommandParam;	
//		  }
//		} else {
//			  return strCommandParam;
//		}
		 
//		final WebSphere webSphere = (WebSphere)consoleHelper.getServer().getProperty();
		
		int index = strCommand.indexOf("<bt:breakIf");
		if (index != -1){
			index = strCommand.indexOf("command=");
			String command = strCommand.substring(index+9,strCommand.length());
			index = command.indexOf("\"");
			command = command.substring(0,index);

			index = strCommand.indexOf("diff=");
			String result = strCommand.substring(index+6,strCommand.length());
			index = result.indexOf("\"");
			result = result.substring(0,index);
			
			String resultado = client.getTerminalWin().executeCommand(command+"\n");
			
			if(!resultado.trim().contains(result.trim())){
				if(!client.getTermAdapter().isStopped()){
					client.getTermAdapter().setStopped(true);			        
					client.getTermAdapter().stopCommand();
		        }
			}
			return "breakIf";
		}
		
        index = strCommand.indexOf("<bt:input>");
        while(index!=-1){
			Display.getDefault().syncExec(new Runnable(){
				public void run(){ 
					  ExpectDialog dialog = new ExpectDialog();
					  dialog.createSShell();
					  resultExpect = dialog.open();
				} 
			});								
			if(resultExpect==null||resultExpect.trim().length()==0){
        		return null;
        	} 
			strCommand = strCommand.substring(0,index)+resultExpect+strCommand.substring(index+10,strCommand.length());
        	index = strCommand.indexOf("<bt:input>");
        }
        
//        if (webSphere!=null) {
//        	index = strCommand.indexOf("<iest:property:");
//	        while( index!=-1 ){
//	        	resultExpect = null;
//	        	
//	        	//strCommand = strCommand.substring(0,index)+strCommand.substring(index+15,strCommand.length());
//	        	int index2 = strCommand.indexOf("/>");
//	        	if(index2==-1){
//	        	   index2 = strCommand.indexOf(">");
//	        	}
//
//	        	final String variavel = strCommand.substring(index,index2);
//	        	
//	        	String valueVar = null;
//	        	
//       			valueVar = getWebSpherePropertyValue(variavel.trim());
//	        	
//	        	final String valueVariable = valueVar;				        	
//	        	
//				Display.getDefault().syncExec(new Runnable(){
//					public void run(){ 
//						  ExpectDialog dialog = new ExpectDialog();
//						  dialog.createSShell();
//						  dialog.setLabelValue(variavel+":");
//						  if(valueVariable!=null){
//						     dialog.setValue(valueVariable);
//						  }
//						  resultExpect = dialog.open();
//					}
//				});								
//	        	if(resultExpect==null||resultExpect.trim().length()==0){
//	        		return null;
//	        	} 
//
//	        	strCommand = strCommand.substring(0,index)+resultExpect+strCommand.substring(index2+1,strCommand.length());
//	        	//logger.debug(command);
//	        	
//	        	index = strCommand.indexOf("<iest:property:");
//	        }    
//        }
        
        return strCommand;
		
	}
	
	public void stop(){
		//logger.debug("Inicio a Parada um procedimento.......");

		if(!getTerminalHelper().getSSHInteractiveClient().getTermAdapter().isStopped()){
			getTerminalHelper().getSSHInteractiveClient().getTermAdapter().setStopped(true);			        
			getTerminalHelper().getSSHInteractiveClient().getTermAdapter().stopCommand();
			Display.getDefault().syncExec(new Runnable(){
				public void run(){ 
					composite.stopToolItem.setEnabled(false);
				}
			}); 
			if( (treeItemList!=null)&&(!treeItemList.isEmpty()) ){
			   for(final TreeItem itemChild : treeItemList){
				Display.getDefault().syncExec(new Runnable(){
					public void run(){  
				    	itemChild.setImage(Constant.IMAGE_COMMAND_ITEM);
					}
				}); 
			   }
			}
        }
		//logger.debug("Fim a Parada um procedimento.......");
	}
	
	public void pause(){
		if(isPause){
		    isPause=false;
			Display.getDefault().syncExec(new Runnable(){
				public void run(){ 
					composite.stopToolItem.setEnabled(true);
					composite.pauseToolItem.setEnabled(true);
					composite.runToolItem.setEnabled(false);
					//toolBarConsoleComposite.menuToolItem.setEnabled(false);
				}
			});
		} else {
		    isPause = true;
			Display.getDefault().syncExec(new Runnable(){
				public void run(){ 
					composite.stopToolItem.setEnabled(false);
					composite.pauseToolItem.setEnabled(true);
					composite.runToolItem.setEnabled(false);
					//toolBarConsoleComposite.menuToolItem.setEnabled(false);
				}
			});
		}
	}
	
    public void sendMessage(String msg){
	    MessageBox msgBox = new MessageBox(MainShell.sShell, SWT.OK);
	    msgBox.setMessage(msg);
	    
	    msgBox.open();
    }
     
    public void openCreateCommandDialog(){ 
    	//Server server = getConsoleHelper().getServer();
    	CommandByServerFactory.openDialogCreateCommand(null);
    }

    public void openEditCommandDialog(boolean openCommandItem){ 
    	Project project = getTerminalHelper().getProject();
		TreeItem item = getTerminalHelper().getItemProcedimentoSelecionado();
		Command commad = null;
		if (item.getData() instanceof Command) {
			commad = (Command)item.getData();
		} else if (item.getData() instanceof CommandItem) {
			commad = (Command)item.getParentItem().getData();
		}
    	CommandByServerFactory.openEditCommandDialog(project, commad,openCommandItem);
    }

    public void removeServerCommand(){
		TreeItem item = getTerminalHelper().getItemProcedimentoSelecionado();
		Command command = null;
		if (item.getData() instanceof Command) {
			command = (Command)item.getData();
		} else if (item.getData() instanceof CommandItem) {
			command = (Command)item.getParentItem().getData();
		}
    	CommandService service =  new CommandService();
    	    	
		service.deleteServerCommand( getTerminalHelper().getProject(), getTerminalHelper().getServer(), command );
    }
    
    public boolean confirm(String commandName){
    	MessageBox messageBox = new MessageBox(MainShell.sShell, SWT.ICON_QUESTION | SWT.YES | SWT.NO);
        messageBox.setMessage("Do you want to perform '"+commandName+"' ?");
        messageBox.setText("Confirm");
        if (messageBox.open() == SWT.NO) 
        {
          return false;
        }
        return true;
    }
    
    class ThreadRunProcedure extends Thread {
    	
    	TreeItem item;
    	TreeItem[] treeItens;
    	Command cmd;
    	String resultLast = " ";
    	InteractiveClientSSH client = null;
    	
    	ThreadRunProcedure(){
    		item = consoleHelper.getItemProcedimentoSelecionado();
    		treeItens = item.getItems();
    		cmd = (Command)item.getData();
    	}
    	
	    public void run() {
			startTime = new Date(); 
			
	    	Display.getDefault().syncExec(new Runnable(){
				public void run(){ 
					client = getTerminalHelper().getSSHInteractiveClient();
					composite.stopToolItem.setEnabled(true);	
					composite.pauseToolItem.setEnabled(true);
					composite.runToolItem.setEnabled(false);
					//getTerminalFace().getBusy().setBusy(true);
					hostname = getTerminalHelper().getServer().getHostname();
				}
			});

			for(int countItens=0; countItens < treeItens.length;countItens++){
				itemChild = treeItens[countItens]; 
				
				if(client.getTermAdapter().isStopped()){
				   break;
				}
				  
				Display.getDefault().syncExec(new Runnable(){
					public void run(){ 
						commandItem = (CommandItem)itemChild.getData();
						itemChild.setImage(Constant.IMAGE_TREE_ITEM_RUNNING);
					}
				});  
                try{
 					String expect = getExpect(commandItem.getCommandSSH());
 					if(expect!=null){
 					   command=expect;        
 					} else {
 					   command = commandItem.getCommandSSH();
 					}
			        
			        if(command==null){
			        	return;
			        }
			        
			        if(command.contains("breakIf")){
			        	continue;
			        }
			        
				    command = command+"\n";
					resultLast = client.getTerminalWin().executeCommand(command);
					
					if(resultLast.equals("promptMsg")){
				    	Display.getDefault().syncExec(new Runnable(){
							public void run(){ 						
					           MainShell.sendMessage("Prompt must to finish with '$' or '#' ");
							}
				    	}); 
						client.getTermAdapter().setStopped(false);
						client.getTerminalWin().requestFocus();
						break;				    	
					}
					
					if ( ( ( resultLast.contains("yes") && resultLast.contains("no") ) 
						|| ( ( resultLast.contains("Yes") && resultLast.contains("No") ) ) )
						&& resultLast.contains(":") ){
						 client.getTerminalWin().executeCommand("yes\n");
					}
					
					countPassword = 0;
//				    if ( (resultLast!=null)&&(resultLast.contains("assword:")) ){
//						while(resultLast.contains("assword:")&&(countPassword<3)){
//							if(countPassword==2){
//								throw new MXTerminalBeanShellException();
//							}
//        	         	    final SshAuthenticationDialog dialog = new SshAuthenticationDialog(hostname);
//        		       		Display.getDefault().syncExec(new Runnable(){
//        		   			public void run(){ 
//        		   				countPassword++;
//    			         	    dialog.disableLDAPCheckBox();
//    			         	    dialog.disableUsername();
//    			         	    dialog.enableMethod();
//        			        	dialog.openShell();
//        		   				}
//        		   			});
//        	        	    if ( dialog.getCanceled() ){
//        	        	    	throw new MXTerminalBeanShellException();
//        	        	    }
//        	        		
//        	        	    command = dialog.getPassword(); 
//							resultLast = client.getTerminalWin().executeCommand(command+"\n");
//	        				  try {
//									Thread.sleep(500);
//	        				  } catch (InterruptedException e) {
//									e.printStackTrace(); 
//							  }									
//						}
//					}  
					
					while(isPause){
						try {
							Thread.sleep(1000);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
//                } catch (MXTerminalBeanShellException e) {
//					Display.getDefault().syncExec(new Runnable(){
//						public void run(){ 
//							toolBarConsoleComposite.getHelper().stop();
//						}
//					});
//					e.printStackTrace();
				}finally{
					Display.getDefault().syncExec(new Runnable(){
						public void run(){ 
					    	itemChild.setImage(Constant.IMAGE_COMMAND_ITEM);
						}
					});
                }
		    }

			endTime = new Date();
			
			Display.getDefault().syncExec(new Runnable(){
				public void run(){ 
					composite.stopToolItem.setEnabled(false);
					composite.pauseToolItem.setEnabled(false);
					composite.runToolItem.setEnabled(true);
					//getTerminalFace().getBusy().setBusy(false);
					client.getTerminalWin().requestFocus();
				}
			});
			
			if(countPassword==3){
				return;
			}
			
			if(client.getTermAdapter().isStopped()){
				client.getTermAdapter().setStopped(false);				
				return;
			}
			
			long startTimeMS = startTime.getTime();
			long endTimeMS = endTime.getTime();
			long difference = Math.abs(endTimeMS - startTimeMS);
			long seconds = difference / 1000;
			long hours =  seconds / 3600;
			long timeInSeconds = seconds - (hours * 3600);
			long minutes = timeInSeconds / 60;
			runTime = hours+":"+minutes+":"+timeInSeconds;
			
			StringTokenizer token = new StringTokenizer(runTime,":");
			String hour = token.nextToken();
			String min = token.nextToken();
			String sec = token.nextToken();
			if(hour.length()==1){
				hour = "0"+hour;
			}
			if(min.length()==1){
			   min = "0"+min;
			}
			if(sec.length()==1){
			   sec = "0"+sec;
			}
			runTime = hour+":"+min+":"+sec;
			Display.getDefault().syncExec(new Runnable(){ 
				public void run(){ 
					item.setText(1,runTime);
				}
			});			
			
		   SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
		   @SuppressWarnings("unused")
		   Date date = null;
		   try {
			   date = sdf.parse(hours+":"+minutes+":"+timeInSeconds);
		   } catch (ParseException e) {
			   e.printStackTrace();
		   }
			
			boolean needUpdate =false;
			if(cmd.getRuntime()!=null){ 
			   StringTokenizer tokenbd = new StringTokenizer(cmd.getRuntime(),":");
			   String hourbd = tokenbd.nextToken();
			   String minbd = tokenbd.nextToken();
			   String secondbd = tokenbd.nextToken();
			   SimpleDateFormat sdfDB = new SimpleDateFormat("HH:mm:ss");
			   Date dateDB = null;
			   try {
				   dateDB = sdfDB.parse(hourbd+":"+minbd+":"+secondbd);
			   } catch (ParseException e) {
				   e.printStackTrace();
			   }
			   if(dateDB.after(dateDB)){
				   needUpdate=true;
			   }
			}
			if( (needUpdate)||(cmd.getRuntime()==null) ){
					final CommandService service = new CommandService();
					cmd.setRuntime(runTime);
					Display.getDefault().syncExec(new Runnable(){ 
						public void run(){ 
							try {							
							  Project project = getTerminalHelper().getProject();
							  service.updateCommand(project,cmd);
							
							  item.setText(1, runTime);
						    } catch (ServiceException e) {
							  MainShell.sendMessage(Constant.MSN_SEE_LOG_ERROR, SWT.OK | SWT.ICON_ERROR);
							  e.printStackTrace();
						    }							
						}
					});							
			}
	    }
    	
    }
	
    public TerminalFace getTerminalFace(){
    	return (TerminalFace)getTerminalHelper().getTermianlFace();
    }
    
    class ThreadRunCommands extends Thread {
    	
    	Command cmd;
    	String password="";
    	String resultLast = " ";
    	InteractiveClientSSH client = null;
    	List<String> list = null;
    	
    	ThreadRunCommands(List<String> list){
    		this.list = list;
    	}
    	
	    public void run() {
	    	
			

	    	Display.getDefault().syncExec(new Runnable(){
				public void run(){ 
					client = getTerminalHelper().getSSHInteractiveClient();
					composite.stopToolItem.setEnabled(true);	
					composite.pauseToolItem.setEnabled(true);
					composite.runToolItem.setEnabled(false);
					//getTerminalFace().getBusy().setBusy(true);
					//toolBarConsoleComposite.menuToolItem.setEnabled(false);
				}
			});
			
			for(String commandFor : this.list ){
				
				if(client.getTermAdapter().isStopped()){
					client.getTermAdapter().setStopped(false);								
				   break;
				}
				command = commandFor;
		    	Display.getDefault().syncExec(new Runnable(){
					public void run(){ 
						logger.debug("ThreadRunCommands : getExpect");
						String expect = getExpect(command);
						if ( expect != null){
						     command = expect;
						}
					}
				});				
		    	
		        
			        
		        if(command==null){
		        	return;
		        }
		        
		        if(command.contains("breakIf")){
		        	continue;
		        }
		        
			    command = command+"\n";
				//logger.debug("Comando do For = "+command);
				resultLast = client.getTerminalWin().executeCommand(command);
				
				if(resultLast.equals("promptMsg")){
			    	Display.getDefault().syncExec(new Runnable(){
						public void run(){ 						
				           MainShell.sendMessage("Prompt must to finish with '$' or '#'");
						}
			    	}); 					
					client.getTermAdapter().setStopped(false);
					client.getTerminalWin().requestFocus();
					break;
				}

				countPassword = 0;
			    if ( (resultLast!=null)&&(resultLast.contains("assword:")) ){
					while(resultLast.contains("assword:")&&(countPassword<3)){
				    	Display.getDefault().syncExec(new Runnable(){
							public void run(){  
								  countPassword++;
								  String hostname = client.getHost();
								  if(getTerminalHelper().getServer()!=null){
									  hostname = getTerminalHelper().getServer().getHostname();
								  }
								  InputPasswordDialog dialog = new InputPasswordDialog(hostname);
								  dialog.setPassword(client.getSSH2UserAuth().getPassword());
								  password = dialog.open();
								}
						});
						  resultLast = client.getTerminalWin().executeCommand(password+"\n"); 
        				  try {
							Thread.sleep(500);
						  } catch (InterruptedException e) {
							e.printStackTrace(); 
						  }					    	
					}
				}  

				if(countPassword==3){
					break;
				}
				
				while(isPause){
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
		    }
			
			Display.getDefault().syncExec(new Runnable(){
				public void run(){ 
					composite.stopToolItem.setEnabled(false);
					composite.pauseToolItem.setEnabled(false);
					composite.runToolItem.setEnabled(true);
					//getTerminalFace().getBusy().setBusy(false);
					//toolBarConsoleComposite.menuToolItem.setEnabled(true);
					//client.getTerminalWin().requestFocus();
				}
			});
			
//			if(countPassword==3){
//				return;
//			}
//			
//			if(client.getTermAdapter().isStopped()){
//				return;
//			}
	    }
    	
    }
       
    private void executeCommandSelected(){
    	CentralTabFolderView cTabFolderConsole = (CentralTabFolderView)composite.getParent();
		CommandComposite commandComposite = (CommandComposite)cTabFolderConsole.getSelection().getControl();
		String commandSelected = commandComposite.getTextArea().getSelectionText();
		final List<String> list = getListFromTextArea(commandSelected);
		if( getTerminalHelper().getSSHInteractiveClient() == null ){
			return;
		}
		
		final ConsoleWin console = getTerminalHelper().getSSHInteractiveClient().getTerminalWin();
		
		if( list.size()==1){
			for(String c:list) {
				command = c;
			}				
			Thread thredRun = new Thread(){
				public void run(){
//					Display.getDefault().syncExec(new Runnable(){
//						public void run(){ 
							console.executeCommand(command + "\n");	
//						}
//					});					
					
				}
			};
			thredRun.start();
		} else { 
			Thread thredRun = new Thread(){
				public void run(){			
					for(String c:list) {
						command = c;
//						Display.getDefault().syncExec(new Runnable(){
//							public void run(){ 
								console.executeCommand(command + "\n");	
//							}
//						});					
					}
				}
			};
			thredRun.start();			
		}
		
		/*
		ThreadRunCommands threadRun = new ThreadRunCommands(list);
	    threadRun.start();
	    */
    }
    
    
    public void createProcedure(){
    	CentralTabFolderView cTabFolderConsole = (CentralTabFolderView)composite.getParent();
    	if(cTabFolderConsole.getSelection().getControl() instanceof CommandComposite){
		   CommandComposite commandComposite = (CommandComposite)cTabFolderConsole.getSelection().getControl();
		   String commandSelected = commandComposite.getTextArea().getSelectionText();
		   List<String> list = getListFromTextArea(commandSelected);
		   CommandByServerFactory.openDialogCreateCommand(list);
    	}
    }
    
    public List<String> getListFromTextArea(String textarea){
    	List<String> list = new ArrayList<String>();
		byte[] b = textarea.getBytes();

		boolean char10 = false;
		boolean char13 = false;
		int count = 0;
		byte[] bline = new byte[b.length]; 
		for(int i=0;b.length>i;i++){
			if(b[i]==13){
			   char10 = true;
			} else if(char10 && b[i]==10){
			   char13 = true;	
			} else {
			   char10 = false;
			}
			
			if(char10 && char13){
				count--;
				String line = new String(bline,0,count);		
				if(line.trim().length()>1){
					list.add(line);
				}
				char10 = false;
				char13 = false;
				count = 0;
				bline = new byte[b.length];
			} else {
				bline[count] = b[i];
				count++;				
			}
		}
		
		if(count>0){
		   String line = new String(bline,0,count);				
			if(line.trim().length()>1){
				list.add(line);
			}
		}
		
		return list;
    	
    }
    
    public void refresh(){
    	CentralTabFolderView cTabFolderConsole = (CentralTabFolderView)composite.getParent();
    	if(cTabFolderConsole.getSelection().getControl() instanceof Tree){
	    	getTerminalHelper().getProcedureTreeHelper().getTreeProcedure().removeAll();
	    	getTerminalHelper().getServerHelper().loadTable();    	
    	}
    }
	
    public void focusCombo(){
    	composite.getCombo().setFocus();
    }
    
    public void configureToolBarView(){
    	composite.getCombo().setVisible(false);
    	composite.lblHostname.setVisible(false);
    	composite.connectionToolBar.setVisible(false);
    	composite.tunnelToolItem.setEnabled(true);
    } 

    public void connect() {
        if( composite.getCombo().getText().trim().length()==0)
     		return;
     
        String hostname = composite.getCombo().getText(); 
        if( ! ManageGateway.isStarted ){
 	       if( AccountConfig.getInstance()!=null && AccountConfig.getInstance().isConnectViaIP() ){
 	    	   for(Server server : Database.getInstance().getServerList()){
 	    		   if(server.getHostname().contains(composite.getCombo().getText())){
 	    			   if(server.getAddress()!=null && server.getAddress().trim().length()>0){
 	    			      hostname = server.getAddress();
 	    			      break;
 	    			   }
 	    		   }
 	    	   }
 	       }
        }
        
        getTerminalFace().getTerminalComposite().setHostName(hostname);
 	   
        getTerminalFace().getTerminalComposite().run();
 	   
        MainShell.getToolBarViewHelper().configureToolBarView();
        
 	    CTabFolder tabFolderServer = MainShell.getCTopTabFolder();
 	   
 	    String regex = "\\d?\\d?\\d[.]\\d?\\d?\\d[.]\\d?\\d?\\d[.]\\d?\\d?\\d";  
 	    Pattern pattern = Pattern.compile( regex );  
 	    Matcher matcher = pattern.matcher( composite.getCombo().getText() );         
 	    
 	    if( composite.getCombo().getText().matches( regex ) ) {
 	        if( matcher.matches() ) {    
 	    	    tabFolderServer.getSelection().setText(composite.getCombo().getText());
 	        }
 	    } else {
 		    int indexof = composite.getCombo().getText().indexOf(".");
 		    if(indexof==-1){
 		       tabFolderServer.getSelection().setText(composite.getCombo().getText());
 		    } else {
 		       String host = composite.getCombo().getText().substring(0,indexof);
 		       tabFolderServer.getSelection().setText(host);
 		    }
 	   } 
 	   
 	   tabFolderServer.setMaximized(true);
 	   MainShell.getSashFormComposite().sashForm.setMaximizedControl(tabFolderServer);
 	   
     }
    
}
