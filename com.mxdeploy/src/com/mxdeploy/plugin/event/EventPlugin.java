package com.mxdeploy.plugin.event;

import java.util.List;

import com.mxdeploy.plugin.domain.Event;
import com.mxdeploy.plugin.domain.Plugin;
import com.mxdeploy.plugin.util.URLClassLoaderPlugin;

public class EventPlugin {

	public static SSHEventHandler getSSHEventHandler(int type){
		
		List<Plugin> pluginList = URLClassLoaderPlugin.getPluginList();
		if( pluginList==null || pluginList.size()>0 ){
			for(Plugin plugin : pluginList){
				if(plugin.getEventList()== null) {
					continue;
				}
				for( Event event : plugin.getEventList()){
					if( event.getType() == type ){
						try {
							@SuppressWarnings("rawtypes")
							Class clazz = Class.forName(event.getClassEvent());
							return (SSHEventHandler)clazz.newInstance();
						} catch (IllegalAccessException e) {
							e.printStackTrace();
						} catch (InstantiationException e) {
							e.printStackTrace();
						} catch (ClassNotFoundException e) {
							e.printStackTrace();
						} catch (SecurityException e) {
							e.printStackTrace();
						}
					}
				}
			}
		}
		return null;
		
	}
	
	public static EventHandler getEventHandler(int type){
		
		List<Plugin> pluginList = URLClassLoaderPlugin.getPluginList();
		if( pluginList==null || pluginList.size()>0 ){
			for(Plugin plugin : pluginList){
				if(plugin.getEventList()== null) {
					continue;
				}
				for( Event event : plugin.getEventList()){
					if( event.getType() == type ){
						try {
							@SuppressWarnings("rawtypes")
							Class clazz = Class.forName(event.getClassEvent());
							return (EventHandler)clazz.newInstance();
						} catch (IllegalAccessException e) {
							e.printStackTrace();
						} catch (InstantiationException e) {
							e.printStackTrace();
						} catch (ClassNotFoundException e) {
							e.printStackTrace();
						} catch (SecurityException e) {
							e.printStackTrace();
						}
					}
				}
			}
		}
		return null;
	}	
	
	public static GatewayEventHandler getGatewayEventHandler(int type){
		
		List<Plugin> pluginList = URLClassLoaderPlugin.getPluginList();
		if( pluginList==null || pluginList.size()>0 ){
			for(Plugin plugin : pluginList){
				if(plugin.getEventList()== null) {
					continue;
				}
				for( Event event : plugin.getEventList()){
					if( event.getType() == type ){
						try {
							@SuppressWarnings("rawtypes")
							Class clazz = Class.forName(event.getClassEvent());
							return (GatewayEventHandler)clazz.newInstance();
						} catch (IllegalAccessException e) {
							e.printStackTrace();
						} catch (InstantiationException e) {
							e.printStackTrace();
						} catch (ClassNotFoundException e) {
							e.printStackTrace();
						} catch (SecurityException e) {
							e.printStackTrace();
						}
					}
				}
			}
		}
		return null;
	}		
}
