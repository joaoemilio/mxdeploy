package com.mxscript.swt.event;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collection;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

import com.mxdeploy.plugin.event.EventHandler;
import com.mxdeploy.swt.MainShell;
import com.mxscript.poi.ExcelUtils;
import com.mxscript.swt.BeanShellFormComposite;

public class ExportToExcelEvent implements EventHandler {

	private BeanShellFormComposite composite;
	
	public ExportToExcelEvent(BeanShellFormComposite composite){
		this.composite = composite;
	}
	
	public void execute() {
		FileDialog dialog = new FileDialog(MainShell.sShell,SWT.OPEN);
		dialog.setText("Export Excel");
		dialog.setFilterPath("C:/");
        String[] filterExt = { "*.xls" };
        dialog.setFilterExtensions(filterExt);
		String filePath = dialog.open();
		
		int columnCount = composite.getTable().getColumnCount();
		String[] headers = new String[columnCount];
		for( int i=0; i< columnCount;i++){
			headers[i] = ((TableColumn)composite.getTable().getColumn(i)).getText();
		}
		
		int itemCount = composite.getTable().getItemCount();
		Collection<String[]> rows = new ArrayList<String[]>();
		for( int i=0; i<itemCount;i++ ){
			TableItem tableItem = composite.getTable().getItem(i);
			String[] itens = new String[columnCount];
			for( int j=0; j< columnCount;j++){
				if(tableItem.getText(j)!=null)
				   itens[j] = tableItem.getText(j);
			}
			rows.add(itens);
		}
		
		if(filePath==null || filePath.length()==0){
			return;
		}
		
		if(!filePath.endsWith(".xls")){
			filePath = filePath+".xls";
		}
		
		try {
			ExcelUtils.exportToExcel(filePath, rows, headers);
		} catch (FileNotFoundException e) {
			MainShell.sendMessage("The file is being used, please close the Excel or choose other name.", SWT.ABORT);
		}
	}

}
