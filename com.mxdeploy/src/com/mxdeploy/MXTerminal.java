package com.mxdeploy;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class MXTerminal {

	public static String HOME = System.getenv("MXD_HOME");
	public static String WORKSPACE = System.getenv("MXD_WORKSPACE");
	public static String MYWORKSPACE = WORKSPACE + "/myworkspace";

	private static Preferences preferences = new Preferences();
	
	public static Preferences getPreferences() {
		return MXTerminal.preferences;
	}
	
	public static void setPreferences(Preferences preferences) {
		MXTerminal.preferences = preferences;
	}
	
	public static void main(String args[]){
		if( System.getProperty("os.name").equals("Linux")) { 
			linuxVersion();
		}
	}

	public static void linuxVersion(){
		try {  
            Process p = Runtime.getRuntime().exec("nohup plink -N -load amex_proxy -nc %host:%port -l fsilva -pw 8#hLdDfk >/dev/null 2>&1 &");  
            BufferedReader in = new BufferedReader(  
                                new InputStreamReader(p.getInputStream()));  
            String line = null;  
            while ((line = in.readLine()) != null) {  
                System.out.println(line);  
            }  
        } catch (IOException e) {  
            e.printStackTrace();  
        } 		
	}
	
}
