package com.mxdeploy.plugin.menu;

import java.util.ArrayList;
import java.util.List;

import com.mxdeploy.plugin.event.EventHandler;

public class MenuBT {
	
	private List<SubMenu> subMenuList = new ArrayList<SubMenu>();
	private EventHandler eventHandler;

	private FileMenu fileMenu;
	/**
	 * @return the subMenuList
	 */
	public List<SubMenu> getSubMenuList() {
		return subMenuList;
	}

	/**
	 * @param subMenuList the subMenuList to set
	 */
	public void addSubMenu(SubMenu subMenu) {
		this.subMenuList.add(subMenu);
	}

	/**
	 * @return the eventHandler
	 */
	public EventHandler getEventHandler() {
		return eventHandler;
	}

	/**
	 * @param eventHandler the eventHandler to set
	 */
	public void setEventHandler(EventHandler eventHandler) {
		this.eventHandler = eventHandler;
	}
	


}
