package com.mxterminal.util;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.log4j.Logger;



public class FileParameterReader {

	static final Logger hciLog = Logger.getLogger(FileParameterReader.class);
	
    private static ArrayList<String[]> parametros = new ArrayList<String[]>();

    private static void readParamFile(String fileToRead){
       
        FileReader fr = null;

        try {
           fr  = new FileReader(fileToRead);
        } catch (FileNotFoundException ex) {
            System.out.println(ex.getMessage());
            ex.printStackTrace();
        }

        BufferedReader buff = new BufferedReader(fr);
        boolean eof = false;

        while(!eof){
            try {

                String line = buff.readLine();
               
                if(line==null){

                    eof=true;

                }else{

                    String lineWOBlank = line.trim();
                    String paramValue[];

                    paramValue = lineWOBlank.split(" ");

                    if(paramValue.length == 2){
                        parametros.add(paramValue);
                    }

                }
            } catch (IOException ex) {
                System.out.println(ex.getMessage());
                ex.printStackTrace();
            }
        }           

    }

    private static String readParameter(String paramName){

        String value = "";       
       
        for(int i = 0;i < parametros.size();i++){
            if(parametros.get(i)[0].equals(paramName)){
                value = parametros.get(i)[1];
            }
        }

        return value;
    }

    public static String  readParameter(String fileToRead,String paramName){
        String value="";
        readParamFile(fileToRead);
        value = readParameter(paramName);
        return value;
    }

}
