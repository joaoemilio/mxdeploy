package com.mxdeploy;

import java.io.IOException;
import java.io.InputStream;

public class StartPlinkWindows {
	
	public static void main(String args[]){
		try {
			//Runtime rt = Runtime.getRuntime();
			//String[] commands = {"start /b","-get t"};
			Process process = Runtime.getRuntime().exec("cmd /c netstat -an | find /i \"[::1]:8888\"");
			InputStream in = process.getInputStream();
			String result = "";
			StringBuilder strb = new StringBuilder();
			int ch;
	        while((ch = in.read()) != -1) {
	        	strb.append( String.valueOf((char)ch) );
	        }
	        if(!result.contains("8888")){
	           Runtime.getRuntime().exec("cmd /c start /b c:\\MXTerminal_32\\plink -N -load amex_proxy -nc %host:%port -l fsilva -pw 8#hLdDfk");
	        }
	        Thread.sleep(3000);
	        strb = new StringBuilder();
	        int count=0;
	        while(true){
				process = Runtime.getRuntime().exec("cmd /c netstat -an | find /i \"[::1]:8888\"");
				in = process.getInputStream();
				result = "";
		        while((ch = in.read()) != -1) {
		        	strb.append( String.valueOf((char)ch) );
		        }	 
		        count++;
		        if(count==3){
		        	break;
		        }
		        Thread.sleep(3000);
	        }
	        
	        if(strb.toString().contains("8888")){
		       Runtime.getRuntime().exec("cmd /c start /b c:\\MXTerminal_32\\plink -N -load amex_gateway -nc %host:%port -l fsilva -pw 8#hLdDfk");
		    }
	        
	        Thread.sleep(3000);
	        strb = new StringBuilder();
	        count=0;
	        while(true){
				process = Runtime.getRuntime().exec("cmd /c netstat -an | find /i \"[::1]:8889\"");
				in = process.getInputStream();
				result = "";
		        while((ch = in.read()) != -1) {
		        	strb.append( String.valueOf((char)ch) );
		        }	 
		        count++;
		        if(count==3){
		        	break;
		        }
		        Thread.sleep(3000);
	        }	      
	        System.out.println(" READY ! ");
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
