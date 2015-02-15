package com.mxdeploy.api.service;

import java.io.File;
import java.io.IOException;

import pl.sind.keepass.exceptions.KeePassDataBaseException;
import pl.sind.keepass.exceptions.UnsupportedDataBaseException;
import pl.sind.keepass.kdb.KeePassDataBase;
import pl.sind.keepass.kdb.KeePassDataBaseManager;
import pl.sind.keepass.kdb.v1.Entry;
import pl.sind.keepass.kdb.v1.Group;
import pl.sind.keepass.kdb.v1.KeePassDataBaseV1;

import com.mxdeploy.api.domain.Database;
import com.mxdeploy.api.domain.PasswordManager;
import com.mxterminal.swt.util.TerminalProperty;

public class KeepassxService {
	
	public void load(){
		if ( TerminalProperty.getKeepassxPath() != null && TerminalProperty.getKeepassxKeyfile()!=null ){
			int index = 0;
			int imageNumber = 0;
			if ( TerminalProperty.getKeepassxIconNumber()!= null ){
				imageNumber = Integer.valueOf( TerminalProperty.getKeepassxIconNumber() );
			}
			KeePassDataBase keePassDb;
			try {
				keePassDb = KeePassDataBaseManager.openDataBase(new File(TerminalProperty.getKeepassxPath()), new File(TerminalProperty.getKeepassxKeyfile()),null);
				KeePassDataBaseV1 kdb1 = (KeePassDataBaseV1) keePassDb; 
				for( Group group : kdb1.getGroups() ){
					if( group.getGroupName().getText().equals(Database.WORKSPACE_NAME)){
						int groupID = group.getGroupId().getId();
						for( Entry entry : kdb1.getEntries() ){
							 if( entry.getGroupId().getId() == groupID ){
								 if( entry.getImageId().getId() == imageNumber ){
									 PasswordManager passManger = new PasswordManager();
									 passManger.setAccount(Database.WORKSPACE_NAME);
									 passManger.setUsername(entry.getUsername().getText());
									 passManger.setPassword(entry.getPassword().getText());
									 passManger.setTitle(entry.getTitle().getText());
									 passManger.setId(index);
									 Database.getInstance().getPasswordManagerList().add(passManger);
									 index++;
								 }
							 }
						}						 
						break;
					}
				} 
				

				
			} catch (UnsupportedDataBaseException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (KeePassDataBaseException e) {
				e.printStackTrace();
			}			 
		}
		
	}

}
