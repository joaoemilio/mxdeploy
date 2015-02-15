package com.mxdeploy.api.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

public class XLSManipulation {
	
	public void removeDuplicated(String xlsFile){
		
		POIFSFileSystem fs = null;
		HSSFWorkbook wb = null;
		FileOutputStream fileOut;
		
		try {
			
			fs = new POIFSFileSystem(new FileInputStream(xlsFile));
			wb = new HSSFWorkbook(fs);
			
		} catch (FileNotFoundException e) {

			e.printStackTrace();
		} catch (IOException e) {

			e.printStackTrace();
		}
		
		HSSFSheet sheet = wb.getSheet("list");
		int size = sheet.getLastRowNum() + 1;
		HSSFRow rowS, rowT;
		HSSFCell cellHostS,cellPolicyS,cellDescS,cellHostT,cellPolicyT,cellDescT;
				
		for(int i=0;i<size;i++){
			
			if(sheet.getRow(i) == null){
				continue;
			}
			
			rowS = sheet.getRow(i);
			cellHostS = rowS.getCell(0);
			cellPolicyS = rowS.getCell(1);
			cellDescS = rowS.getCell(2);
			
			for(int j = i + 1; j < size; j++){
			
				if(sheet.getRow(j) == null){
					continue;
				}
				
				rowT = sheet.getRow(j);
				cellHostT = rowT.getCell(0);
				cellPolicyT = rowT.getCell(1);
				cellDescT = rowT.getCell(2);
				

				if(cellHostT.toString().trim().equals(cellHostS.toString().trim())&&
						cellPolicyT.toString().trim().equals(cellPolicyS.toString().trim())&&
						cellDescT.toString().trim().equals(cellDescS.toString().trim())){	
					sheet.removeRow(rowT);				
					System.out.println("Line " + j + ": " + cellHostT + " " + cellPolicyT +" " + cellDescT);
				}
				
			}
			
		}		
		
		try {			
			fileOut = new FileOutputStream(xlsFile);
			wb.write(fileOut);
			fileOut.close();
		} catch (FileNotFoundException e) {
			System.out.println("Could not created file");
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public void splitDR(String nonCompList, String DRList){
		
		File drList = new File(DRList);
		FileReader fr;
		BufferedReader br = null;
		boolean eof = false;
		ArrayList<String> hostNameDRList = new ArrayList<String>();
		ArrayList<String> drServerList = new ArrayList<String>();
		ArrayList<String> otherServerList = new ArrayList<String>();
		
		try {
			
			fr = new FileReader(drList);
			br = new BufferedReader(fr);
						
		} catch (FileNotFoundException e) {
			System.out.println("DR List " + DRList + " not found");
			e.printStackTrace();
		}
		
		while(!eof){
			
			try {
				
				String line = br.readLine();
				
				if(line != null){
					
					hostNameDRList.add(line);
				
				}else{
					
					eof = true;
				
				}
				
			} catch (IOException e) {
				System.out.println("Couln't  read " + DRList + " list");
				e.printStackTrace();
			}			
			
		}
		
		POIFSFileSystem fs = null;
		HSSFWorkbook wb = null;
		HSSFWorkbook wbDR = new HSSFWorkbook();
		HSSFWorkbook wbOther = new HSSFWorkbook();
		FileOutputStream fileOut;
		
		try {
			
			fs = new POIFSFileSystem(new FileInputStream(nonCompList));
			wb = new HSSFWorkbook(fs);
			
		} catch (FileNotFoundException e) {

			e.printStackTrace();
		} catch (IOException e) {

			e.printStackTrace();
		}
		
		HSSFSheet sheet = wb.getSheet("list");
		HSSFRow row;
		HSSFCell cell;
		
		int size = sheet.getLastRowNum() + 1;
		
		//DR List
		for(int i =0; i < size;i++){
			
			if(sheet.getRow(i) == null){
				continue;
			}
			
			row = sheet.getRow(i);
			cell = row.getCell(0);
			
			for(int j=0;j < hostNameDRList.size();j++){
				
				if(cell.toString().trim().equals(hostNameDRList.get(j))){
					
					if(!drServerList.contains(hostNameDRList.get(j))){

						drServerList.add(hostNameDRList.get(j).trim());
						
					}				
					
				}
				
			}
			
		}
		
		//Other List
		for(int i =0; i < size;i++){
			
			if(sheet.getRow(i) == null){
				continue;
			}
			
			row = sheet.getRow(i);
			cell = row.getCell(0);
			
			if(!drServerList.contains(cell.toString().trim())){
				otherServerList.add(cell.toString().trim());
			}
			
		}
		
		//Create WB's
		
		//DR WB
		HSSFSheet sheetTemp = wbDR.createSheet("DR Servers");
		HSSFRow newRow;
		HSSFCell newCell;
		
		for(int i=0; i < drServerList.size();i++){
			for(int j=0; j <size;j++){
				row = sheet.getRow(j);
				cell = row.getCell(0);
				
				if(cell.toString().trim().equals(drServerList.get(i))){
					newRow = sheetTemp.createRow(j);				
					
					newCell = newRow.createCell(0);
					newCell.setCellValue(new HSSFRichTextString(cell.toString()));
					
					cell = row.getCell(1);
					newCell = newRow.createCell(1);
					newCell.setCellValue(new HSSFRichTextString(cell.toString()));
					
					cell = row.getCell(2);
					newCell = newRow.createCell(2);
					newCell.setCellValue(new HSSFRichTextString(cell.toString()));
					
				}

			}
		}
		
		try {
			fileOut = new FileOutputStream("dr_server_list.xls");
			wbDR.write(fileOut);
			fileOut.close();
		} catch (FileNotFoundException e) {
			System.out.println("Couldn't create file dr_server_list.xls");
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("Couldn't create file dr_server_list.xls");
			e.printStackTrace();
		}
		
		//Others WB
		sheetTemp = wbOther.createSheet("Others Servers");
		for(int i=0; i < otherServerList.size();i++){
			for(int j=0; j < size ;j++){
				row = sheet.getRow(j);
				cell = row.getCell(0);
				
				if(cell.toString().trim().equals(otherServerList.get(i))){
					newRow = sheetTemp.createRow(j);				
					
					newCell = newRow.createCell(0);
					newCell.setCellValue(new HSSFRichTextString(cell.toString()));
					
					cell = row.getCell(1);
					newCell = newRow.createCell(1);
					newCell.setCellValue(new HSSFRichTextString(cell.toString()));
					
					cell = row.getCell(2);
					newCell = newRow.createCell(2);
					newCell.setCellValue(new HSSFRichTextString(cell.toString()));
					
				}

			}
		}
		
		try {
			fileOut = new FileOutputStream("others_server_list.xls");
			wbOther.write(fileOut);
			fileOut.close();
		} catch (FileNotFoundException e) {
			System.out.println("Couldn't create file others_server_list.xls");
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("Couldn't create file others_server_list.xls");
			e.printStackTrace();
		}
		
		
	}

}
