package com.mxscript.poi;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

public class ExcelUtils {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		ArrayList<Object> rows = new ArrayList<Object>();
		for(int i = 0; i < 10; i++){
			rows.add(new TestRow(i, "string " + i, new Long(i), new Date()));
		}
		String[] attrs = new String[]{"index", "string", "longInt", "date"};
		ExcelUtils.exportToExcel("c:/temp/TestRow.xls", rows, TestRow.class, attrs); 

		Collection<String[]> rows2 = new ArrayList<String[]>();
		for(int i = 0; i < 10; i++){
			rows2.add(new String[]{"g01cxwas00" + i, "9.8.17.5" + i, ""+i+3, ""+i+2});
		}
		String[] attrs2 = new String[]{"Hostname", "IP Address", "IHS", "WAS"};
		try {
			ExcelUtils.exportToExcel("c:/temp/JVMCounter.xls", rows2,attrs2);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} 

	}
	
	public static void exportToExcel(String xlsFile, Collection<Object> rows, Class className, String[] attributes){
		
		int line = 0;
		HSSFWorkbook wb = null;
		FileOutputStream fileOut = null;
		try {
			wb = new HSSFWorkbook();
			fileOut = new FileOutputStream(xlsFile);
			HSSFSheet sheet = wb.createSheet();
			HSSFRow hssFRow = sheet.createRow(0);
			HSSFCell cell = null;
			for(int i = 0; i < attributes.length; i++){
				cell = hssFRow.createCell(i, HSSFCell.CELL_TYPE_STRING);
				cell.setCellValue(new HSSFRichTextString(attributes[i]));
			}
			
			for(Object row: rows){
				line ++;
				hssFRow = sheet.createRow(line);
				for(int i = 0; i < attributes.length; i++){
					try {
						cell = hssFRow.createCell(i, HSSFCell.CELL_TYPE_STRING);
						Class clazz = PropertyUtils.getPropertyType(row, attributes[i]);
						Object obj = PropertyUtils.getProperty(row, attributes[i]);
						System.out.println("class name: " + clazz.getName());
						String value = "";
						if( obj instanceof java.util.Date ){
							Date date = (Date)obj;
							value = DateFormatter.getInstance().formatDate(date);
							cell.setCellValue(new HSSFRichTextString(value));
						} else if(obj instanceof Short) {
							cell.setCellValue((Short)obj);
						} else if(obj instanceof Long) {
							System.out.println("entrou no long");
							cell.setCellValue((Long)obj);
						} else if(obj instanceof Integer) {
							System.out.println("entrou no integer");
							cell.setCellValue((Integer)obj);
						}else{
							value = BeanUtils.getProperty(row, attributes[i]);
							cell.setCellValue(new HSSFRichTextString(value));
						}
						System.out.println("row: " + line + ": attr: " + attributes[i] + " = " + value);
					} catch (IllegalAccessException e) {
						e.printStackTrace();
					} catch (InvocationTargetException e) {
						e.printStackTrace();
					} catch (NoSuchMethodException e) {
						e.printStackTrace();
					}
				}
			}
		} catch (FileNotFoundException e) {

			e.printStackTrace();
		}finally{
		    try {
				wb.write(fileOut);
			    fileOut.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
	
	public static void exportToExcel(String xlsFile, Collection<String[]> rows, String[] headers) throws FileNotFoundException{
		
		int line = 0;
		//POIFSFileSystem fs = null;
		HSSFWorkbook wb = null;
		FileOutputStream fileOut = null;
		try {
			wb = new HSSFWorkbook();
			fileOut = new FileOutputStream(xlsFile);
			HSSFSheet sheet = wb.createSheet();
			HSSFRow hssFRow = sheet.createRow(0);
			HSSFCell cell = null;
			for(int i = 0; i < headers.length; i++){
				cell = hssFRow.createCell(i, HSSFCell.CELL_TYPE_STRING);
				cell.setCellValue(new HSSFRichTextString(headers[i]));
			}
			
			for(String[] row: rows){
				line ++;
				hssFRow = sheet.createRow(line);
				for(int i = 0; i < headers.length; i++){
					cell = hssFRow.createCell(i, HSSFCell.CELL_TYPE_STRING);
					String value = row[i];
					try{
						Integer.parseInt(value);
						cell.setCellValue(new Double(value));
					}catch(Exception e){
						cell.setCellValue(new HSSFRichTextString(value));
					}
					System.out.println("row: " + line + ": attr: " + row[i] + " = " + value);
				}
			}
		}finally{
		    try {
				wb.write(fileOut);
			    fileOut.close();
			} catch (IOException e) {
				e.printStackTrace();
			} catch(Throwable e){
				e.printStackTrace();
			}
		}
		
	}
	
}
