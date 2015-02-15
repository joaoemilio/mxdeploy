package com.mxterminal.swt.util;

public class Expect {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
        String pegar = "ps -ef | grep <iest:property:was_home > | grep <iest:input> | <iest:property:was_soap> ";
        
        int index = pegar.indexOf("<iest:input>");
        while(index!=-1){
        	//str = pegar.substring(index,index+11);
        	pegar = pegar.substring(0,index)+"java"+pegar.substring(index+12,pegar.length());
        	//System.out.println(pegar);
        	index = pegar.indexOf("<iest:input>");
        }
        
        index = pegar.indexOf("<iest:property:");
        while(index!=-1){
        	pegar = pegar.substring(0,index)+pegar.substring(index+15,pegar.length());
        	int index2 = pegar.indexOf(">");
        	String variavel = pegar.substring(index,index2);
        	
        	pegar = pegar.substring(0,index)+variavel.trim()+pegar.substring(index2+1,pegar.length());
        	//System.out.println(pegar);
        	index = pegar.indexOf("<iest:property:");
        }        
        	
	}

}
