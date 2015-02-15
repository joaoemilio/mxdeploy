package com.mxdeploy.plugin.menu;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;

import com.mxdeploy.plugin.event.EventHandler;

public class SubMenu {
	
	List<SubMenu>   subMenuList = new ArrayList<SubMenu>();
    private String  text;
    private Image   image;
    private EventHandler eventHandler=null;
    private String  shortCut;
    private int     SWT_Style =  SWT.PUSH;
    private boolean createSeparatorBefore = false;
    private int  accelerator = 0;
    private boolean enabled = true;

	/**
	 * @return the enable
	 */
	public boolean isEnabled() {
		return enabled;
	}

	/**
	 * @param enable the enable to set
	 */
	public void setEnabled(boolean enable) {
		this.enabled = enable;
	}

	/**
	 * @return the createSeparatorBefore
	 */
	public boolean isCreateSeparatorBefore() {
		return createSeparatorBefore;
	}

	/**
	 * @param createSeparatorBefore the createSeparatorBefore to set
	 */
	public void setCreateSeparatorBefore(boolean createSeparatorBefore) {
		this.createSeparatorBefore = createSeparatorBefore;
	}

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

	/**
	 * @return the image
	 */
	public Image getImage() {
		return image;
	}

	/**
	 * @param image the image to set
	 */
	public void setImage(Image image) {
		this.image = image;
	}

	/**
	 * @return the shortCut
	 */
	public String getShortCut() {
		return shortCut;
	}

	/**
	 * @param shortCut the shortCut to set
	 */
	public void setShortCut(String shortCut) {
		this.shortCut = shortCut;
	}

	/**
	 * @return the text
	 */
	public String getText() {
		return text;
	}

	/**
	 * @param text the text to set
	 */
	public void setText(String text) {
		this.text = text;
	}

	/**
	 * @param subMenuList the subMenuList to set
	 */
	public void setSubMenuList(List<SubMenu> subMenuList) {
		this.subMenuList = subMenuList;
	}

	/**
	 * @return the sWT_Style
	 */
	public int getSWT_Style() {
		return SWT_Style;
	}

	/**
	 * @param style the sWT_Style to set
	 */
	public void setSWT_Style(int style) {
		SWT_Style = style;
	}

	/**
	 * @return the accelerator
	 */
	public int getAccelerator() {
		return accelerator;
	}

	/**
	 * @param accelerator the accelerator to set
	 */
	public void setAccelerator(int accelerator) {
		this.accelerator = accelerator;
	}

}
