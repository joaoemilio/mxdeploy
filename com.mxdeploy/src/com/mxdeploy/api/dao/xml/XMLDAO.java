package com.mxdeploy.api.dao.xml;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

import com.mxdeploy.api.domain.TransferObject;
import com.mxdeploy.api.util.io.FileUtilities;

public class XMLDAO {

	
	protected XStreamHelper xsHelper = new XStreamHelper();
	
	protected void create(Object domain, File file) throws DAOException {
		String xml = xsHelper.xstream.toXML(domain);
		
		try {
			FileUtils.writeStringToFile(file, xml);
		} catch (IOException e) {
			throw new DAOException(e);
		}
	}
	
	protected void create(TransferObject domain, File file) throws DAOException {
		String xml = xsHelper.xstream.toXML(domain);
		
		try {
			FileUtils.writeStringToFile(file, xml);
		} catch (IOException e) {
			throw new DAOException(e);
		}
	}
	
	protected void update(TransferObject domain, File file) throws DAOException {
		String xml = xsHelper.xstream.toXML(domain);
		try {
			File backupFile = FileUtilities.getInstance().backupFile(file);
			if(backupFile.exists()) {
				file.delete();
				FileUtils.writeStringToFile(file, xml);
			}else{
				throw new IOException("Backup File could not be created. Update Failed");
			}
		} catch (IOException e) {
			throw new DAOException(e);
		}
	}
	
}
