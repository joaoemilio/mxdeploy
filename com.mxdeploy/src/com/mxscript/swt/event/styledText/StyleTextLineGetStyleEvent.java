package com.mxscript.swt.event.styledText;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.LineStyleEvent;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Display;

import com.mxdeploy.api.domain.Database;
import com.mxdeploy.plugin.event.EventHandler;
import com.mxscript.swt.BeanShellScriptComposite;

public class StyleTextLineGetStyleEvent implements EventHandler {

	private BeanShellScriptComposite composite;
	private LineStyleEvent event;
	
	public StyleTextLineGetStyleEvent(BeanShellScriptComposite composite,LineStyleEvent event){
		this.composite = composite;
		this.event = event;
	}
	
	public void execute(){

		List<String> keyWordsList = listFileContent(null,Database.HOME+"/keywords.txt");
		List<String> bshWordsList = listFileContent(null,Database.HOME+"/bshworkds.txt");
		
		java.util.List<StyleRange> styles = new java.util.ArrayList<StyleRange>();
		int indexOf = -1;
    	for(String keyWords : keyWordsList){
    		if ( ( indexOf = event.lineText.indexOf(keyWords)) != -1 ) {
				StyleRange styleRange = new StyleRange();
				styleRange.start = event.lineOffset + indexOf;
				styleRange.length = keyWords.length();
				styleRange.foreground = new Color(Display.getCurrent(), 128, 0, 0);
				styleRange.fontStyle = SWT.BOLD;
				styles.add(styleRange);
    		}
    	}        	
    	
    	for(String bshWords : bshWordsList){
    		if ( ( indexOf = event.lineText.indexOf(bshWords)) != -1 ) {
				StyleRange styleRange = new StyleRange();
				styleRange.start = event.lineOffset + indexOf;
				styleRange.length = bshWords.length();
				styleRange.foreground = event.display.getSystemColor(SWT.COLOR_BLUE);
				styles.add(styleRange);
    		}
    	}
    	
    	if ( ( indexOf = event.lineText.indexOf("\"")) != -1 ) {
    		String next = event.lineText;
    		int lineOffset = 0;
	    	while(true){ 
	    		if ( ( indexOf = next.indexOf("\"")) != -1 ) {
	    			next = next.substring(next.indexOf("\"")+1);
	    			int last = next.indexOf("\"");
					StyleRange styleRange = new StyleRange();
					styleRange.start = event.lineOffset + indexOf + lineOffset;
					styleRange.length = lineOffset+last+2;
					styleRange.foreground = event.display.getSystemColor(SWT.COLOR_BLUE);
					styles.add(styleRange);    		
//					String value = event.lineText.substring(indexOf,indexOf+styleRange.length);
//					System.out.println(value+" = start:"+styleRange.start+" lenght:"+styleRange.length);
					
					lineOffset += indexOf+last+2;
					next = event.lineText.substring(lineOffset);
	    		} else { 
	    			break;
	    		}
	    	}
    	}
    	
    	if ( ( indexOf = event.lineText.indexOf("\'")) != -1 ) {
    		String next = event.lineText;
    		int lineOffset = 0;
	    	while(true){ 
	    		if ( ( indexOf = next.indexOf("\'")) != -1 ) {
	    			next = next.substring(next.indexOf("\'")+1);
	    			int last = next.indexOf("\'");
					StyleRange styleRange = new StyleRange();
					styleRange.start = event.lineOffset + indexOf + lineOffset;
					styleRange.length = lineOffset+last+2;
					styleRange.foreground = event.display.getSystemColor(SWT.COLOR_BLUE);
					styles.add(styleRange);    		
//					String value = event.lineText.substring(indexOf,indexOf+styleRange.length);
//					System.out.println(value+" = start:"+styleRange.start+" lenght:"+styleRange.length);
					
					lineOffset += indexOf+last+2;
					next = event.lineText.substring(lineOffset);
	    		} else { 
	    			break;
	    		}
	    	}
    	}	
    	event.styles = (StyleRange[]) styles.toArray(new StyleRange[0]);
	}
	
	public List<String> listFileContent(List<String> list, String filePath){
	    
		if(list==null){
			list = new ArrayList<String>();
		}
	    
		FileReader input = null;
		try {
			input = new FileReader( filePath );
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} 
		
		BufferedReader bufRead = new BufferedReader(input);
		String line; // String that holds current file
		try {
			while ( ( line = bufRead.readLine())!=null ) {
				line = line.trim();
				line = line.replace("\r", "");
				line = line.replace("\n", "");
				
				list.add(line); 
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		try { 
			bufRead.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return list;
		
	}		

}
