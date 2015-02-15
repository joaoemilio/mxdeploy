package com.mxterminal.swt.view.helper;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;

import com.mxdeploy.api.domain.Database;
import com.mxdeploy.api.domain.Template;
import com.mxterminal.ssh.InteractiveClientSSH;
import com.mxterminal.swt.view.CentralTabFolderView;
import com.mxterminal.swt.view.CommandComposite;
import com.mxterminal.swt.view.ToolBarViewComposite;

public class CommandHelper {
	static Logger logger = Logger.getLogger(CommandHelper.class);
	
	protected CommandComposite composite;
	
	public CommandHelper(CommandComposite composite){
		this.composite = composite;
	}
	
	public InteractiveClientSSH getSSHInteractiveClient(){
		CentralTabFolderView cTabFolderConsole = (CentralTabFolderView)composite.getParent();
		return cTabFolderConsole.getConsoleHelper().getSSHInteractiveClient();
	}	
	
	public TerminalHelper getConsoleHelper(){
		CentralTabFolderView cTabFolderConsole = (CentralTabFolderView)composite.getParent();
		return cTabFolderConsole.getConsoleHelper();
	}	
	
	public void setStatusModifyText(boolean status){
		//redrawTextArea();		
		CentralTabFolderView cTabFolderConsole = (CentralTabFolderView)composite.getParent();
		cTabFolderConsole.getToolBarViewComposite().saveToolItem.setEnabled(status);
		cTabFolderConsole.saveEnable = status;
	}		
	
	public void verifyText(VerifyEvent e){
		if(e.stateMask == SWT.CTRL && e.keyCode != SWT.CTRL){
			if(e.keyCode==115 || e.keyCode==32 ){ 
			   e.doit=false;
			}
		}
        Text text = (Text)e.widget;
        if(text.getSelectionCount()>0){
           setStatusModifyText(true);
        }
	}
	
	public ToolBarViewComposite getToolBarConsoleComposite(){
		CentralTabFolderView cTabFolderConsole = (CentralTabFolderView)composite.getParent();
		return cTabFolderConsole.getToolBarViewComposite();
	}		
	
	public void sendCommand(String text){
		List<String> list = getAllCommands(text);
		for(String line : list){
			logger.debug("Running command : "+line);
			getSSHInteractiveClient().getTerminalWin().executeCommand(line+"\n");
		}
	}
	
	public void mouseUp(MouseEvent e) {
		if(e.widget instanceof Text){
		   Text text = (Text)e.widget;
		   enableRunBotton(text);
		   enableCreateProcedureBotton(text);
		}
	}
	
	protected void enableRunBotton(Text text){
		if (getConsoleHelper().getSSHInteractiveClient()!= null && !getConsoleHelper().getSSHInteractiveClient().isAuthenticated()){
			return;
		}

		String sel = text.getSelectionText();
		CentralTabFolderView cTabFolderConsole = (CentralTabFolderView)composite.getParent();		
		if(sel.trim().length()>0){
			cTabFolderConsole.getToolBarViewComposite().runToolItem.setEnabled(true);		
		} else {
			cTabFolderConsole.getToolBarViewComposite().runToolItem.setEnabled(false);
		}
	}
	
	protected void enableCreateProcedureBotton(Text text){
		String sel = text.getSelectionText();
		CentralTabFolderView cTabFolderConsole = (CentralTabFolderView)composite.getParent();		
		if(sel.trim().length()>0 && getConsoleHelper().getProject()!=null ){
			cTabFolderConsole.getToolBarViewComposite().createProcedureToolItem.setEnabled(true);		
		} else {
			cTabFolderConsole.getToolBarViewComposite().createProcedureToolItem.setEnabled(false);
		}
	}	
	
	protected List<String> getAllCommands(String text){
		List<String> list = new ArrayList<String>();
		byte[] b = text.getBytes();
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
	
	public void keyPressed(KeyEvent e){
    	CentralTabFolderView cTabFolderConsole = (CentralTabFolderView)composite.getParent();
    	
		if(e.stateMask == SWT.CTRL && e.keyCode != SWT.CTRL){
			if(e.keyCode==115){ 
			   cTabFolderConsole.getToolBarViewComposite().getHelper().save();
			   return;
			} else if(e.keyCode==32){ 
				if ((cTabFolderConsole.getToolBarViewComposite().runToolItem.getEnabled()==true)
			     && (cTabFolderConsole.getToolBarViewComposite().stopToolItem.getEnabled()==false)){
					cTabFolderConsole.getToolBarViewComposite().getHelper().run();
				} 
				return;
//			} else if(e.keyCode==99){
//				if(cTabFolderConsole.getConsoleHelper().getSSHInteractiveClient().isAuthenticated()){
//					if(e.keyCode==99){
//					   String cmd = new String(new char[] {'\003'});
//					   cTabFolderConsole.getConsoleHelper().getSSHInteractiveClient().getTerminalWin().sendString(cmd);
//					}
//				}
			}

		}
		
		if (cTabFolderConsole.getToolBarViewComposite().stopToolItem.getEnabled()==false){
			if(e.widget instanceof Text){
				Text text = (Text)e.widget;
			   enableRunBotton(text);
			   enableCreateProcedureBotton(text);
			}
		}		
		
	}
	
//	public void keyPressed2(KeyEvent e){
//    	CTabFolderConsole cTabFolderConsole = (CTabFolderConsole)composite.getParent();
//    	
//		if(e.stateMask == SWT.CTRL && e.keyCode != SWT.CTRL){
//			if(e.keyCode==115){ 
//			   cTabFolderConsole.getToolBarConsoleComposite().getHelper().save();
//			   return;
//			}
//		}
//		
//		Text obj = (Text)e.widget;
//    	Properties property = cTabFolderConsole.getCommandProp();
//
//		byte[] textByte = obj.getText().getBytes();
//
//		List<Integer> listAfter = new ArrayList<Integer>();
//		List<Integer> listBefore = new ArrayList<Integer>();
//
//		
//		int after=obj.getCaretOffset();
//		int before=obj.getCaretOffset();
//		boolean afterRunning=true;
//		boolean beforeRunning=true;
//		while(beforeRunning || afterRunning){
//			if(afterRunning && obj.getText().length()>after && textByte[after]!=32 && textByte[after]!=10 && textByte[after]!=13){
//				listAfter.add(after);
//				after++;
//			} else {
//				afterRunning=false;
//			}
//			
//			if(beforeRunning && before>0 && textByte[before-1]!=32 && textByte[before-1]!=10 && textByte[before-1]!=13 ){
//				listBefore.add(before-1);
//				before--;
//			} else {
//				beforeRunning=false;
//			}
//		}
//		
//		byte[] bword = new byte[listBefore.size()+listAfter.size()];
//		int count = 0;
//		if(!listBefore.isEmpty()){
//			for(int i=listBefore.size()-1;i >= 0;i--, count++){
//				Integer var = listBefore.get(i);
//				bword[count] = textByte[var];
//			}
//		}
//		
//		if(!listAfter.isEmpty()){
//			for(Integer var : listAfter){
//				bword[count] = textByte[var];
//				count++;
//			}
//		}		
//		
//		String word = new String(bword);
//		System.out.println(word);
//		
//		String pword = property.getProperty(word);
//		if(pword!=null && pword.equals("1")){
//			StyleRange styleRange = new StyleRange();
//			styleRange.start = before;
//			styleRange.length = word.length();
//			styleRange.foreground = new Color(Display.getCurrent(), 128, 0, 0);
//			obj.setStyleRange(styleRange);
//		} else if (before != after){
//			StyleRange styleRange = new StyleRange();
//			styleRange.start = before;
//			styleRange.length = word.length();
//			styleRange.foreground = new Color(Display.getCurrent(), 0, 0, 0);
//			obj.setStyleRange(styleRange);
//		}
//	}
	
    public List<String> getTemplates() {
		List<String> list = null;
		File tmpFile;
		String fileName = null;
		//String sshHomeDir = Database.HOME; 

		fileName = Database.WORKSPACE_PATH+"/templates";
		tmpFile = new File(fileName);

		if (!tmpFile.exists()) {
			try {
				tmpFile.mkdir();
			} catch (Throwable t) {
				//logger.error(t.getMessage());
			}
		}

		if (!tmpFile.exists() || !tmpFile.isDirectory()) {
			//logger.warn("There is no IEST_HOME/workspace/templates directory");
			return null;
		}
		File[] files = tmpFile.listFiles();
		list = new ArrayList<String>();
		for (int i = 0; i < files.length; i++) {
			if (files[i].getName().endsWith(".template")) {
				list.add(files[i].getName());
			}
		}
		return list;
	}
  
	public void reloadTemplate(){
	    List<Template> templateList = new ArrayList<Template>();
        List<String> listFileTemplate = getTemplates();
        for(String filename : listFileTemplate){
      	      Template template = new Template();
	          String home = Database.WORKSPACE_PATH+"/templates/";
			  File file = new File(home+filename);
			  if(!file.exists()){
				  continue;
			  }
			  BufferedReader input;
			  try {
				  input = new BufferedReader(new FileReader(file));
				  try {
			          StringBuffer lineBuffer = new StringBuffer();
			          String line = null;
			          boolean first=true;
			          while (( line = input.readLine()) != null){
			        	  if(first){
		        		     template.setTitle(line);
		        		     first=false;
			        	  }
			        	  lineBuffer.append(line+"\r\n");
			          }
			          template.setFileName(filename);
			          template.setValue(lineBuffer.toString());
			          templateList.add(template);
				  } catch (FileNotFoundException e) {
					e.printStackTrace();
				  } finally {
	  		    try {
						input.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
		          }
		      } catch (IOException e) {
				e.printStackTrace();
		      }					  
        }
        reloadTable(templateList);
        setStatusModifyText(false);
	}    
	
	public void reloadTable(List<Template> templateList){
		  composite.getTable().removeAll();		
          boolean firstAll = true;
          TableItem item = null;
          Collections.sort(templateList, new Template()); 
          
          for(Template template : templateList){
    		  item = new TableItem(composite.getTable(),SWT.NONE);
    		  item.setText(template.getTitle());  
	          item.setData(template.getValue());
	          item.setData("filename", template.getFileName());    		
	          if(firstAll){
	        	  composite.getContentTemplateTextArea().setText(template.getValue());
	    		  composite.getTable().setSelection(item);			        	  
	        	  firstAll=false;
	          }  	          
          }
	}
	
	public void loadTextSelectItem(TableItem item){
		if(item.getData()!=null){
		   composite.getContentTemplateTextArea().setText(item.getData().toString());
		   setStatusModifyText(false);
		} 
	}
	
	public void newTemplate(){
         TableItem item = new TableItem(composite.getTable(),SWT.NONE);
  		 item.setText("New Template");
  		 item.setData("");
  		 composite.getTable().setSelection(item);
  		 composite.getContentTemplateTextArea().setText("");
  		 composite.getContentTemplateTextArea().setFocus();
  		 setStatusModifyText(true);
	}
	
	public void removeTemplate(){
		 String filename = (String)composite.getTable().getSelection()[0].getData("filename");
         String home = Database.WORKSPACE_PATH+"/templates/";
		 File file = new File(home+filename);
		 if(file.exists()){
			file.delete();
		 }
		 composite.getTable().getSelection()[0].dispose();
		 if(composite.getTable().getItemCount()>0){
			composite.getTable().setSelection(0);
			String str = (String)composite.getTable().getSelection()[0].getData();
			composite.contentTemplateTextArea.setText(str);
		 }
	}
	
}
