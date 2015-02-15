package com.mxdeploy.api.domain.operatingsystem;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class UnixMemoryHealthCheck {
	static{
		System.out.println("UnixMemoryHealthCheck");
	}
	
	private Double percentUsed;
	private List<UnixProcess> top5ProcessList = new ArrayList<UnixProcess>();
	private Double threshold;
	private String hostname;
	private String ipaddress;
	private String rca;
	private HashMap<String, String> mapCustom = new HashMap<String, String>();
	private String lines;
	private String vmstat;
	
	public Double getPercentUsed() {
		return percentUsed;
	}

	public void setPercentUsed(Double percentUsed) {
		this.percentUsed = percentUsed;
	}

	public String getHostname() {
		return hostname;
	}

	public void setHostname(String hostname) {
		this.hostname = hostname;
	}

	public String getIpaddress() {
		return ipaddress;
	}

	public void setIpaddress(String ipaddress) {
		this.ipaddress = ipaddress;
	}

	public Double getThreshold() {
		return threshold;
	}

	public void setThreshold(Double threshold) {
		this.threshold = threshold;
	}

	public void setTop5ProcessList(List top5ProcessList) {
		this.top5ProcessList = top5ProcessList;
	}

	public void addProcess(UnixProcess up) {
		this.top5ProcessList.add(up);
	}
	
	public void addNewProcess(UnixProcess up) {
		this.top5ProcessList.add(up);
	}
	
	public List getTop5ProcessList() {
		return this.top5ProcessList;
	}
	
	public String getMemStatus() {
		if(this.percentUsed > (100.0-this.threshold) ) {
			return "NOK";
		}else{
			return "OK";
		}
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
		StringBuffer rcaSB = new StringBuffer();

		String result = "Host: " + this.getHostname()+"\n"+
		                "Memory Available (avg) is running at " + NumberFormat.getInstance().format(100.0-this.getPercentUsed()) + "%\n"+
                 		this.lines; 
		return result;
	}
	
//	public String getRca() {
//		if( this.percentUsed < new Double(100.0-this.threshold) ) {
//			return "RCA: Server peak memory usage. Situation no longer persists. This incident will fall into the Defect Prevention Process." +
//					"\n\nResolution: Checked the server health and available memory is above " + this.getThreshold().toString() + "%. Server is running under normal " +
//					"parameters of operation. According to System Operations Procedures, closing the ticket accordingly." + "\n"
//					+ "\n" +
//					"ADD NOTE" +
//					"\n=====================================\n\n" +
//					"Hi Team, \n" +
//					"We have checked Memory usage and the alert is FALSE:\n" +
//					this.vmstat; 
//		}
//		StringBuffer rcaSB = new StringBuffer();
//
//		rcaSB.append("Hi Team,\n"); 
//		rcaSB.append("We have checked Memory usage and the alert is real:\n");
//		rcaSB.append("\n");
//		rcaSB.append("Host: " + this.getHostname() + "/" + this.getIpaddress() );
//		rcaSB.append("\n");
//		rcaSB.append("Memory Available (avg) is running at " + NumberFormat.getInstance().format(100.0-this.getPercentUsed()) + "%\n");
//		rcaSB.append("\n");
//		rcaSB.append("Processes consuming memory are:\n");
//		rcaSB.append(this.lines);
//		rcaSB.append("\n\n");
//		rcaSB.append("Could You please assist?\n");
//		rcaSB.append("Thank You\n");
//		rcaSB.append("Distributed Operations" );
//
//		return rcaSB.toString();
//	}

	public void setRca(String rca) {
		this.rca = rca;
	}

	public void setLines(String lines) {
		this.lines = lines;
	}
	
	public String getLines() {
		return this.lines;
	}

	public String getVmstat() {
		return vmstat;
	}

	public void setVmstat(String vmstat) {
		this.vmstat = vmstat;
	}
	
}
