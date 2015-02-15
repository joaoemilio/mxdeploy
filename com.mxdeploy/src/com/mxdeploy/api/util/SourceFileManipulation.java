package com.mxdeploy.api.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.StringTokenizer;

public class SourceFileManipulation {
	
	private ArrayList<String> lines = new ArrayList<String>();
	
	public ArrayList<String> getLines() {
		return lines;
	}

	public void setLines(ArrayList<String> lines) {
		this.lines = lines;
	}

	public SourceFileManipulation(String srcFile){

		File fileSource = new File(srcFile);
		FileReader fr;
		BufferedReader br;
		
		try {
			fr = new FileReader(fileSource);
			br = new BufferedReader(fr);
			System.out.println("File " + srcFile + " opened");
			boolean eof = false;
			while(!eof){
				String line = br.readLine();
				if (line == null ){
					eof = true;
				}else{
					lines.add(line);
				}	
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			System.out.println("Source file " + srcFile + " couldn't be found");
		} catch (IOException e) {
			System.out.println("Source file " + srcFile + " couldn't be opened");
			e.printStackTrace();
		}		
	}
	
	public String getParameter(String parameterName){
		
		String parameter = null;
		
		for (int i=0; i < lines.size(); i++){
			
			if(lines.get(i).contains(parameterName)){				
				StringTokenizer st = new StringTokenizer(lines.get(i),":");			
				st.nextToken();			
				parameter = st.nextToken();				
				break;
			}
			
		}		
		
		return parameter;
	}
	
	public String getTestStatus(String check){

		String status = "";
		
		for(int i=0;i < lines.size();i++){
			
			if(lines.get(i).contains("========  REPORT SUMMARY  ========") ||
					lines.get(i).contains("==== Report Summary =====")){
				
				for(int j=i;j < lines.size();j++){
					if(lines.get(j).contains(check)){
						String cleanString = lines.get(j).trim();
						cleanString = cleanString.replaceFirst("\\*", "");
						cleanString = cleanString.replaceFirst(">", "");						
						String[] temp = cleanString.trim().split(" ");						
						status = temp[0].toUpperCase();
						break;
					}
				}
			}
			
		}
		
		return status;
		
	}
	
	public boolean checkScriptAbort(){
		
		boolean aborted = false;
		
		for(int i=0;i < lines.size();i++){
			
			if(lines.get(i).contains("SCRIPT OUTPUT ABORTED")){
				
				aborted = true;
				break;
				
			}
			
		}
		
		return aborted;
	}
	
	public ArrayList<String> getFailList(String type){
		
		ArrayList<String> faillist = new ArrayList<String>();
		String[] policy = null;
		
		if(type.equals("IHS")){
			String[] temp = {"1.1.4.1/2","1.1.4.3/4","1.1.4.3","0.1.2.1.3.3","4.1.1.2","4.1.1.5","5.1.1.1.a",
					"5.1.1.1.b","5.1.2","5.1.2.1","5.1.4","5.1.5.4","5.1.6.5","5.1.5.1b","5.1.6.1","6.1.1","7.1.6"};			
			policy = temp;
			
			
			
			
		}else{
			String[] temp = {"1.1.0","1.1.8.3","0.1.2.1.3.3","1.1.4a","1.1.4b",
					"1.1.9.2","1.1.8.1","1.1.8.4","1.1.9.4",
					"2.0.1","2.0.2","2.0.3","2.0.4","3.0.1","4.1.1.7",
					"4.1.1.8","4.1.4","5.1.4","5.1.2.1/3","5.1.2.4","5.1.6",
					"5.1.7","7.1.6","5.1.1.1"};			
			policy = temp;
		}


		
		for(int i=0;i < policy.length;i++){
			
			for(int j=0;j < lines.size();j++){
				
				if(lines.get(j).contains("========  REPORT SUMMARY  ========") ||
						lines.get(j).contains("==== Report Summary =====")){
					
					for(int k=j;k < lines.size();k++){
						
							if(lines.get(k).contains(policy[i])){
								String cleanString = lines.get(k).trim();
								cleanString = cleanString.replaceFirst("\\*", "");
								cleanString = cleanString.replaceFirst(">", "");						
								String[] temp = cleanString.trim().split(" ");						
								if(temp[0].toUpperCase().equals("FAIL")){
									faillist.add(cleanString.replaceFirst("FAIL", ""));
									continue;
								}
								
						}
						
					}
				}
				
			}
			
		}
		
		return faillist;
		
	}
	
	public String getWASVersion(){
		
		String version = null;
		
		for(int i=0;i<lines.size();i++ ){
			if(lines.get(i).contains("Version =")){
				version = lines.get(i).split(";")[1].split("=")[1].trim().replace("(", "").replace(")", "");
				break;
			}
		}
		
		return version;
	}
	
}
