package com.mxdeploy.api.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class IPManipulation {

	private ArrayList<String> ips = new ArrayList<String>();
	
	public ArrayList<String> getIps() {
		return ips;
	}

	public void setIps(ArrayList<String> ips) {
		this.ips = ips;
	}
	
	public IPManipulation(String ipFileSource){
		
		File fileSource = new File(ipFileSource);
		FileReader fr;
		BufferedReader br;
		
		try {
			
			fr = new FileReader(fileSource);
			br = new BufferedReader(fr);
			
			boolean eof = false;
			
			while(!eof){
				
				String line = br.readLine();
				
				if (line == null ){
										
					eof = true;
					
				}else{
					
					ips.add(line);
					
				}	
				
			}
			
		} catch (FileNotFoundException e) {

			e.printStackTrace();
			System.out.println("Source file " + ipFileSource + " couldn't be found");
		} catch (IOException e) {

			System.out.println("Source file " + ipFileSource + " couldn't be opened");
			e.printStackTrace();
		}		
		
	}
	
	public String getIP(String hostName){
		
		String ip = null;
		
		for (int i=0; i < ips.size(); i++){
			
			if(ips.get(i).contains(hostName)){				
				String temp[] = ips.get(i).trim().split(" ");
				ip = temp[1].trim();	
				if(ip == null){
					ip = "";
					System.out.println("Hostname: " + hostName + " has no IP");
				}
			}
			
		}		
		
		return ip;
	}
	
}
