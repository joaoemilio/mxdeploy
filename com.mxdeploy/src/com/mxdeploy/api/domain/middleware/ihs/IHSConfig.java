package com.mxdeploy.api.domain.middleware.ihs;

import java.util.ArrayList;
import java.util.List;

import com.mxdeploy.api.domain.TransferObject;

public class IHSConfig extends TransferObject {
	
	private List<Location> configLocations = new ArrayList<Location>();
	private List<LocationMatch> configLocationsMatch = new ArrayList<LocationMatch>();
	private List<VirtualHost> virtualHosts = new ArrayList<VirtualHost>();
	private List<LogConfig> logConfigs = new ArrayList<LogConfig>();
	private List<String> modules = new ArrayList<String>();
	private String path;
	private IBMHTTPServer ihs;
	private List<Alias> aliases = new ArrayList<Alias>();
	private List<Alias> aliasesMatch = new ArrayList<Alias>();
	private List<Alias> scriptAliases = new ArrayList<Alias>();
	private List<Directory> directories = new ArrayList<Directory>();
	private String accessConfig;
	
	public IBMHTTPServer getIhs() {
		return ihs;
	}

	public void addDirectory(Directory directory){
		directories.add(directory);
	}
	
	public List<Directory> getDirectories() {
		return directories;
	}

	public void setDirectories(List<Directory> directories) {
		this.directories = directories;
	}

	public String getAccessConfig() {
		return accessConfig;
	}

	public void setAccessConfig(String accessConfig) {
		this.accessConfig = accessConfig;
	}

	public void setIhs(IBMHTTPServer ihs) {
		this.ihs = ihs;
	}

	public List<Alias> getAliasesMatch() {
		return aliasesMatch;
	}

	public void setAliasesMatch(List<Alias> aliasesMatch) {
		this.aliasesMatch = aliasesMatch;
	}

	public void setModules(List<String> modules) {
		this.modules = modules;
	}

	public void setAliases(List<Alias> aliases) {
		this.aliases = aliases;
	}

	public void setScriptAliases(List<Alias> scriptAliases) {
		this.scriptAliases = scriptAliases;
	}

	public void addConfigLocation(Location value){
		this.configLocations.add(value);
	}
	
	public void addConfigLocationMatch(LocationMatch value){
		this.configLocationsMatch.add(value);
	}
	
	public void addAlias(Alias value){
		aliases.add(value);
	}
	
	public void addAliasMatch(Alias value){
		aliasesMatch.add(value);
	}
	
	public void addScriptAlias(Alias value){
		scriptAliases.add(value);
	}
	
	public List<Alias> getAliases(){
		return this.aliases;
	}
	
	public List<Alias> getScriptAliases(){
		return this.scriptAliases;
	}
	
	public IHSConfig(IBMHTTPServer ihs){
		this.ihs = ihs;
	}
	
	public IBMHTTPServer getIHS(){
		return this.ihs;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}
	
	public void addModule(String module){
		modules.add(module);
	}
	
	public List<String> getModules(){
		return this.modules;
	}

	public void addLogConfig(LogConfig logConfig){
		logConfigs.add(logConfig);
	}
	
	public List<LogConfig> getLogConfigs() {
		return logConfigs;
	}

	public void setLogConfigs(List<LogConfig> logConfigs) {
		this.logConfigs = logConfigs;
	}
	
	public void addVirtualHost( VirtualHost vh ){
		virtualHosts.add(vh);
	}
	
	public List<Location> getConfigLocations() {
		return configLocations;
	}

	public void setConfigLocations(List<Location> configLocations) {
		this.configLocations = configLocations;
	}

	public List<LocationMatch> getConfigLocationsMatch() {
		return configLocationsMatch;
	}

	public void setConfigLocationsMatch(List<LocationMatch> configLocationsMatch) {
		this.configLocationsMatch = configLocationsMatch;
	}

	public List<VirtualHost> getVirtualHosts() {
		return virtualHosts;
	}

	public void setIHSConfigLocations(List<Location> configLocations){
		this.configLocations = configLocations;
	}
	
	public void setIHSConfigLocationsMatch(List<LocationMatch> configLocationsMatch){
		this.configLocationsMatch = configLocationsMatch;
	}
	
	public void setVirtualHosts(List<VirtualHost> virtualHosts){
		this.virtualHosts = virtualHosts;
	}
	
	public boolean isServerStatusEnabled(){
		boolean result = false;
		for(Location configLoc:configLocations ){
			if(configLoc.isServerStatusEnabled()){
				result = true;
				break;
			}
		}
		return result;
	}
	
	public String getModuleFullPath(String module){
		String fullPath = "";
		if(module.trim().substring(0,1).equals("/")){
			fullPath = module;
		}else{
			fullPath = this.getIHS().getServerRoot() + module;
		}
		return fullPath;
	}
	
	
	
}
