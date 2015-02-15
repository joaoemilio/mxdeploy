package com.mxdeploy.plugin.domain;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

public class Plugin {
	
	private String version;
	public Menu menu;
	private MenuExplorer topMenuExplorer;
	private MenuExplorer childMenuExplorer;
	public List<Event> eventList = new ArrayList<Event>();
	private CTabItemExplorerDomain cTabItemExplorer;
	private TreeItemExplorerDomain treeItemExplorer;

	
	public TreeItemExplorerDomain getTreeItemExplorer() {
		return treeItemExplorer;
	}

	public void setTreeItemExplorer(TreeItemExplorerDomain treeItemExplorer) {
		this.treeItemExplorer = treeItemExplorer;
	}

	/** 
	 * @return the eventList
	 */
	public List<Event> getEventList() {
		return eventList;
	}

	/**
	 * @param eventList the eventList to set
	 */
	public void addEvent(Event event) {
		this.eventList.add(event);
	}

	/**
	 * @return the menu
	 */
	public Menu getMenu() {
		return menu;
	}

	/**
	 * @param menu the menu to set
	 */
	public void addMenu(Menu menu) {
		this.menu = menu;
	}
	
	public static void main(String[] args){
		
		Plugin plugin = new Plugin();
		plugin.setVersion("7.0.0");
		
		Menu menu = new Menu();
		menu.setType(Menu.SUBMENU_CUSTOM);
		menu.setClassAddOn("com.ds.plugin.raptor.menu.MenuAddonAdapter");
		
		plugin.addMenu(menu);
		
		XStream xstream = new XStream(new DomDriver());
		try {
			xstream.toXML(plugin, new FileWriter("c:\\temp\\plugin.xml"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @return the version
	 */
	public String getVersion() {
		return version;
	}

	/**
	 * @param version the version to set
	 */
	public void setVersion(String version) {
		this.version = version;
	}

	public MenuExplorer getChildMenuExplorer() {
		return childMenuExplorer;
	}

	public void setChildMenuExplorer(MenuExplorer childMenuExplorer) {
		this.childMenuExplorer = childMenuExplorer;
	}

	public MenuExplorer getTopMenuExplorer() {
		return topMenuExplorer;
	}

	public void setTopMenuExplorer(MenuExplorer topMenuExplorer) {
		this.topMenuExplorer = topMenuExplorer;
	}

	public CTabItemExplorerDomain getcTabItemExplorer() {
		return cTabItemExplorer;
	}

	public void setcTabItemExplorer(CTabItemExplorerDomain cTabItemExplorer) {
		this.cTabItemExplorer = cTabItemExplorer;
	}




	
	

}
