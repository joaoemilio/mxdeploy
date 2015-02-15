package com.mxdeploy.api.domain.operatingsystem;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class UnixCPUHealthCheck {
	
	private Double percentIdle;
	private List<UnixProcess> top5ProcessList = new ArrayList<UnixProcess>();
	private Double threshold;
	private String hostname;
	private String ipaddress;
	private String rca;
	private HashMap<String, String> mapCustom = new HashMap<String, String>();
	private String lines;
	private String vmstat;
	
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

	public Double getPercentIdle() {
		return percentIdle;
	}

	public void setTop5ProcessList(List top5ProcessList) {
		this.top5ProcessList = top5ProcessList;
	}

	public void addProcess(UnixProcess up) {
		this.top5ProcessList.add(up);
	}
	
	public List<UnixProcess> getTop5ProcessList() {
		return this.top5ProcessList;
	}
	
	public void setPercentIdle(Double percentIdle) {
		this.percentIdle = percentIdle;
	}
	
	public Double percentIdle() {
		return this.percentIdle;
	}
	
	public String getCpuStatus() {
		System.out.println("percent idle: " + this.percentIdle);
		System.out.println( "ok/nok: " + ( (100.0-this.percentIdle) > this.threshold ) );
		System.out.println( "threshold: " + this.threshold );
		if((100.0-this.percentIdle) > this.threshold ) {
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
		Double percIdle = this.percentIdle();
		if( percIdle > (100- this.threshold) ) {
			return "RCA: Peak CPU usage. situation no longer persists. This incident will fall into the Defect Prevention Process."
			+ "\n\nResolution: Server CPU usage was checked and is within normal parameters of operation,  according to " +
			"System Operations Procedures, no action is needed. Closing the ticket accordingly." + "\n"
			+ "\n" +
			"ADD NOTE\n" +
			"==============================\n" +
			"Hi Team, we have checked CPU usage and the alert is FALSE: \n\n" + 
 			this.vmstat; 
		}
		StringBuffer rcaSB = new StringBuffer();

		List<UnixProcess> list = this.top5ProcessList;
		String perc = this.getPercentIdle().toString();
		int s1 = perc.length();
		if(s1 > 5) s1 = 5; 
		perc = perc.substring(0,s1);
		
		rcaSB.append("Hi Team,\n"); 
		rcaSB.append("We have checked CPU usage and the alert is real:\n");
		rcaSB.append("\n");
		rcaSB.append("Host: " + this.getHostname() + "/" + this.getIpaddress() );
		rcaSB.append("\n");
		rcaSB.append("CPU Usage (avg) is running at " + NumberFormat.getInstance().format(100.0-this.getPercentIdle()) + "%\n");
		rcaSB.append("\n");
		rcaSB.append("Processes consuming CPU are:\n");
		rcaSB.append(this.lines);
		rcaSB.append("\n\n");
		rcaSB.append("Could You please assist?\n");
		rcaSB.append("Thank You\n");
		rcaSB.append("Distributed Operations" );
		
		return rcaSB.toString();
	}

	public void setRca(String rca) {
		this.rca = rca;
	}

	public void setLines(String procs) {
		this.lines = procs;
	}

	public String getVmstat() {
		return vmstat;
	}

	public void setVmstat(String vmstat) {
		this.vmstat = vmstat;
	}

}
