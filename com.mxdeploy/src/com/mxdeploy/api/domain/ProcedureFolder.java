package com.mxdeploy.api.domain;

import java.util.ArrayList;
import java.util.List;

public class ProcedureFolder {
	
	private String name;
	private List<ProcedureFolder> subFolders = new ArrayList<ProcedureFolder>();
    private List<BeanShell> beanShells = new ArrayList<BeanShell>();
	
	/**
	 * @param servers the servers to set
	 */
	public void addBeanShell(BeanShell beanShell) {
		this.beanShells.add(beanShell);
	}

    public List<BeanShell> getBeanShells() {
		return beanShells;
	}

	public void setBeanShells(List<BeanShell> beanShells) {
		this.beanShells = beanShells;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void addSubFolder(ProcedureFolder pFolder) {
		subFolders.add(pFolder);
	}

	public List<ProcedureFolder> getSubFolders() {
		return subFolders;
	}

	public void setSubFolders(List<ProcedureFolder> subFolders) {
		this.subFolders = subFolders;
	}
	
}
