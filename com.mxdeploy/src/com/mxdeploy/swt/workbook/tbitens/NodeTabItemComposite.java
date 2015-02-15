package com.mxdeploy.swt.workbook.tbitens;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;

public class NodeTabItemComposite extends Composite {
	private Text nodeText;
	private Table table;
	private Button removeButton;
	private Button addButton;
	private Button btnCancel;
	private Button updateButton;

	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public NodeTabItemComposite(Composite parent, int style) {
		super(parent, SWT.BORDER);
		GridLayout gridLayout = new GridLayout(1, false);
		gridLayout.marginWidth = 0;
		gridLayout.verticalSpacing = 0;
		gridLayout.marginHeight = 0;
		gridLayout.horizontalSpacing = 0;
		setLayout(gridLayout);
		
		Composite composite = new Composite(this, SWT.NONE);
		composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
		GridLayout gl_composite = new GridLayout(6, false);
		gl_composite.marginWidth = 20;
		composite.setLayout(gl_composite);
		
		Label label = new Label(composite, SWT.NONE);
		label.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		label.setText("Node :");
		
		nodeText = new Text(composite, SWT.BORDER);
		nodeText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		addButton = new Button(composite, SWT.NONE);
		addButton.setText("  Add   ");
		
		updateButton = new Button(composite, SWT.NONE);
		updateButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				TableItem tableItem = table.getItem(table.getSelectionIndex());
				if( nodeText.getText().length() == 0 ){
					sendMessage("Node name can't be null !");
					return;
				}
				if( nodeExist(nodeText.getText(), table.getSelectionIndex()) ){
					sendMessage("Node already exist !");
					return;	
				}
				tableItem.setText(nodeText.getText().trim());
				updateButton.setEnabled(false);
				removeButton.setEnabled(false);				
			}
		});
		updateButton.setEnabled(false);
		updateButton.setText("Update");
		
		removeButton = new Button(composite, SWT.NONE);
		removeButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if( table.getSelectionIndex() != -1 ){
					removeButton.setEnabled(false);
					updateButton.setEnabled(false);
					nodeText.setText("");
					table.remove(table.getSelectionIndex());
				}				
			}
		});
		removeButton.setEnabled(false);
		removeButton.setText("Remove");
		
		btnCancel = new Button(composite, SWT.NONE);
		btnCancel.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				addButton.setEnabled(true);
				updateButton.setEnabled(false);
				removeButton.setEnabled(false);
				nodeText.setText("");
			}
		});
		btnCancel.setText("Cancel");
		
		Composite composite_1 = new Composite(this, SWT.NONE);
		composite_1.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		GridLayout gl_composite_1 = new GridLayout(1, false);
		gl_composite_1.verticalSpacing = 0;
		gl_composite_1.marginHeight = 0;
		gl_composite_1.horizontalSpacing = 0;
		gl_composite_1.marginWidth = 0;
		composite_1.setLayout(gl_composite_1);
		
		table = new Table(composite_1, SWT.BORDER | SWT.FULL_SELECTION);
		table.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if( table.getSelectionIndex() != -1 ){
					removeButton.setEnabled(true);
					updateButton.setEnabled(true);
					TableItem tableItem = table.getItem(table.getSelectionIndex());
					nodeText.setText(tableItem.getText());
				}
			}
		});
		table.setLinesVisible(true);
		table.setHeaderVisible(true);
		table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		TableColumn tableColumn = new TableColumn(table, SWT.NONE);
		tableColumn.setWidth(100);
		tableColumn.setText("Name");
		
		addButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if( nodeText.getText().length() == 0 ){
					sendMessage("Node name can't be null !");
					return;
				}
				if( nodeExist(nodeText.getText(), -1) ){
					sendMessage("Node already exist !");
					return;	
				}
				TableItem tableItem= new TableItem(table, SWT.NONE);
				tableItem.setText(nodeText.getText().trim());
				updateButton.setEnabled(false);
				removeButton.setEnabled(false);
				nodeText.setText("");
				
			}
		});		

	}


	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}

	public Text getNodeText() {
		return nodeText;
	}

	public void setNodeText(Text nodeText) {
		this.nodeText = nodeText;
	}

	public Table getTable() {
		return table;
	}

	public void setTable(Table table) {
		this.table = table;
	}

	public Button getRemoveButton() {
		return removeButton;
	}

	public void setRemoveButton(Button removeButton) {
		this.removeButton = removeButton;
	}

	public Button getAddButton() {
		return addButton;
	}

	public void setAddButton(Button addButton) {
		this.addButton = addButton;
	}

    public void sendMessage(final String msg){
		Display.getDefault().asyncExec(new Runnable() {
		    public void run() {
		    	MessageBox msgBox = new MessageBox(getShell(), SWT.OK );
		    	msgBox.setMessage(msg);
		    	msgBox.open();
		    }
		});
    }
    public boolean nodeExist(String nodeName, int index){
		for( int i=0; i < table.getItemCount(); i++){
			if( i == index){
				continue;
			}			
			TableItem tableItem = table.getItem(i);
			if( tableItem.getText().trim().equals(nodeName.trim())){
				return true;
			}
		}    	
		return false;
    }
}
