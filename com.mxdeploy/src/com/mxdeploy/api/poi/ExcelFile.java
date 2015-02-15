package com.mxdeploy.api.poi;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

public class ExcelFile {

	private HSSFWorkbook wb = null;
	private HSSFSheet sheet = null;
	private String fileName;

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public ExcelFile(String templateDir, String templateFile) {
		//Creation parameters for xls	
		POIFSFileSystem template = null;

		template =	getTemplate(templateDir, templateFile);
		try {
			this.wb = new HSSFWorkbook(template);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	protected POIFSFileSystem getTemplate(String templateDir, String template){
		POIFSFileSystem fs = null;
		try {
			fs = new POIFSFileSystem(new FileInputStream(templateDir + "/" + template));
			System.out.println("Template " + template + " opened");
			
		} catch (FileNotFoundException e) {
 
			System.out.println("Template " + template + " couldn't be found");
			e.printStackTrace();
		} catch (IOException e) {
			
			System.out.println("Template " + template + " couldn't be opened");
			e.printStackTrace();
		}

		return fs;
		
	}


	public void setSheet(String value) {
		sheet  = wb.getSheet(value);
	}


	public void setCellValue(int irow, int jcell, String value) {
		HSSFRow row;
		HSSFCell cell;

		//HostName
		row = this.sheet.getRow(irow);
		cell = row.getCell(jcell);
		cell.setCellValue(new HSSFRichTextString(value));
		
	}

	public void setCellValue(int i, int j, Integer value) {
		HSSFRow row;
		HSSFCell cell;

		//HostName
		row = this.sheet.getRow(i);
		cell = row.getCell(j);
		cell.setCellValue(value);
		
	}
	
	public void setCellValue(int i, int j, Date value) {
		HSSFRow row;
		HSSFCell cell;

		//Date
		row = sheet.getRow(i);
		cell = row.getCell(j);		
		HSSFCellStyle cellStyle = cell.getCellStyle();
		cell.setCellValue(new Date() );		
		cell.setCellStyle(cellStyle);
	}


	public void setCellBackground(int i, int j, String string) {
		HSSFCellStyle style = wb.createCellStyle();
		HSSFRow row;
		HSSFCell cell;

		//Date
		row = sheet.getRow(i);
		cell = row.getCell(j);		

		if(string.equals("RED")) {
			style.setFillBackgroundColor(HSSFColor.RED.index);
			style.setFillForegroundColor(HSSFColor.RED.index);
		} else if( string.equals("GREEN")) {
			style.setFillBackgroundColor(HSSFColor.GREEN.index);
			style.setFillForegroundColor(HSSFColor.GREEN.index);
		}
		style.setFillPattern(HSSFCellStyle.BIG_SPOTS);
		style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		cell.setCellStyle(style);
	}


	public String getItemValue(int k, int i) {
		HSSFRow row;
		HSSFCell cell;

		row = this.sheet.getRow(k);				
		cell = row.getCell(i);
		String value = "";
		if(cell != null && cell.getRichStringCellValue()!= null) {
			value = cell.getRichStringCellValue().getString();			
		}
		
		return value;
	}


	public void createRow(int i) {
		sheet.createRow(i);
	}


	public void createCell(int i, int j) {
		HSSFRow row = sheet.getRow(i);
		row.createCell(j);
	}


	public void writeFile() {
		String fileOutName = this.fileName;
		FileOutputStream fileOut = null;
		try {
			fileOut = new FileOutputStream( new File(fileOutName) );
			wb.write(fileOut);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if(fileOut != null) {
				try {
					fileOut.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	
	

}
