package com.mxdeploy.api.dao.xml;

import com.mxdeploy.api.domain.BeanShell;
import com.mxdeploy.api.domain.BeanShellApplication;
import com.mxdeploy.api.domain.BeanShellProject;
import com.mxdeploy.api.domain.Document;
import com.mxdeploy.api.domain.Image;
import com.mxdeploy.api.domain.Resource;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

public class XStreamHelper {
	
	XStream xstream = new XStream(new DomDriver());
	
	public void init() {
		xstream.alias("beanShellApplication", BeanShellApplication.class);
		xstream.alias("beanShell", BeanShell.class);
		xstream.alias("beanShellProject", BeanShellProject.class);
		xstream.alias("image", Image.class);
		xstream.alias("resource", Resource.class);
		xstream.alias("document", Document.class);
	}
	
	
}
