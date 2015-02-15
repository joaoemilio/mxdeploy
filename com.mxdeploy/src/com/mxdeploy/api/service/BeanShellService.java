package com.mxdeploy.api.service;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.UUID;

import com.mxdeploy.api.dao.BeanShellDAO;
import com.mxdeploy.api.domain.BeanShell;
import com.mxdeploy.api.domain.Database;

public class BeanShellService {

//	public String createBeanShell(BeanShell bsh) {
//		BeanShellDAO dao = new BeanShellDAO();
//		String id = UUID.randomUUID().toString();
//		bsh.setUuid(UUID.randomUUID());
//		bsh.setPath(Database.getBeanShellPath()+"/bsh/"+id+".bsh");
//		dao.save(bsh);
//		saveFile(bsh," ");
//		return id;
//	}
	
	public void updateBeanShell(BeanShell bsh) throws ServiceException {
		BeanShellDAO dao = new BeanShellDAO();
		dao.save(bsh);
	}	
	
	public void saveFile(BeanShell beanShell, String scriptBSH){
		String beanShellpath = Database.getBeanShellPath()+"/bsh/"+beanShell.getUuid()+".bsh";
 	    File file = new File(beanShellpath);
 	    Writer output;
	       try {
				output = new BufferedWriter(new FileWriter(file));
			    output.write(scriptBSH+"\r\n");
			    output.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
	}   
	

		
}
