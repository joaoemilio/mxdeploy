package com.mxdeploy.api.domain.operatingsystem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class UnixFileSystemHealthCheck {
	
	private List<UnixFileSystem> unixFileSystemList = new ArrayList<UnixFileSystem>();
	private String hostname;
	private String ipAddress;
	private Double percentUsed;
	private Double percentFree;
	private Double threshold;
	private List<UnixFile> topConsumers = new ArrayList<UnixFile>();
	private HashMap<String, String> mapCustom = new HashMap<String, String>();
	private String rca;
	private String findOutput;
	private String dfminusk;
	
	public void addTopConsumer(UnixFile unixFile) {
		topConsumers.add(unixFile);
	}
	
	public List<UnixFile> getTopConsumers() {
		return this.topConsumers;
	}
	
	public String getFileSystemStatus() {
		if(this.percentUsed > this.threshold ) {
			return "NOK";
		}else{
			return "OK";
		}
	}

	public Double getPercentUsed() {
		return percentUsed;
	}

	public void setPercentUsed(Double percentUsed) {
		this.percentUsed = percentUsed;
	}

	public Double getPercentFree() {
		return percentFree;
	}

	public void setPercentFree(Double percentFree) {
		this.percentFree = percentFree;
	}

	public Double getThreshold() {
		return threshold;
	}

	public void setThreshold(Double threshold) {
		this.threshold = threshold;
	}

	public String getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	public void setUnixFileSystemList(List<UnixFileSystem> unixFileSystemList) {
		this.unixFileSystemList = unixFileSystemList;
	}

	public void setHostname(String hostname) {
		this.hostname = hostname;
	}

	public void addFileSystem(UnixFileSystem ufs) {
		this.unixFileSystemList.add(ufs);
	}
	
	public List<UnixFileSystem> getUnixFileSystemList() {
		return this.unixFileSystemList;
	}

	public String getHostname() {
		return this.hostname;
	}
	public HashMap<String, String> getMapCustom() {
		return mapCustom;
	}

	public void setMapCustom(HashMap<String, String> mapCustom) {
		this.mapCustom = mapCustom;
	}
	
	public void addCustomField(String key, String value) {
		mapCustom.put(key, value);
	}
	
	public String getCustomFieldValue(String key) {
		return mapCustom.get(key);
	}

	public String getRca() {
		if( this.percentUsed < this.threshold ) {
			return "RCA: Server peak file system usage. Situation no longer persists. This incident will fall into the Defect Prevention Process." +
					"\n\nResolution: Checked the file system and utilization is below " + this.getThreshold().toString() + "%. Server is running under normal " +
					"parameters of operation. According to System Operations Procedures, closing the ticket accordingly." + "\n"
					+ "\n" +
					"ADD NOTE" +
					"\n=====================================\n\n" +
					"Hi Team, \n" +
					"We have checked File System usage and the alert is FALSE:\n" +
					this.dfminusk; 
		}
		StringBuffer rcaSB = new StringBuffer();

		rcaSB.append("Hi Team,\n"); 
		rcaSB.append("We have checked File System usage and the alert is real:\n");
		rcaSB.append("\n");
		rcaSB.append("Host: " + this.getHostname() + "/" + this.getIpAddress() );
		String perc = this.getPercentUsed().toString();
		int s1 = perc.length();
		if(s1 > 5) s1 = 5; 
		perc = perc.substring(0,s1);
		rcaSB.append("\n");
		rcaSB.append("\nFile System Usage is running at " + perc + "%\n");
		rcaSB.append("\n");
		rcaSB.append("Files consuming space are:\n");
		rcaSB.append(this.getFindOutput());
		rcaSB.append("\n\n");
		rcaSB.append("Could You please assist?\n");
		rcaSB.append("Thank You\n");
		rcaSB.append("Distributed Operations" );

		return rcaSB.toString();
	}

	public void setRca(String rca) {
		this.rca = rca;
	}

	public String getFindOutput() {
		return findOutput;
	}

	public void setFindOutput(String findOutput) {
		this.findOutput = findOutput;
	}

	public String getDfminusk() {
		return dfminusk;
	}

	public void setDfminusk(String dfminusk) {
		this.dfminusk = dfminusk;
	}

	public void setTopConsumers(List<UnixFile> topConsumers) {
		this.topConsumers = topConsumers;
	}


}

