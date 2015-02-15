package com.mxscript.swt;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class DynamicFormComposite extends Dialog {
  private String[] fields;
  private String[] fieldTypes;
  private ArrayList<Text> listText = new ArrayList<Text>();
  HashMap map = new HashMap();

  /**
   * @param parent
   */
  public DynamicFormComposite(Shell parent) {
    super(parent);
  }

  /**
   * @param parent
   * @param style
   */
  public DynamicFormComposite(Shell parent, int style) {
    super(parent, style);
  }

  public void setInputFields(String[] fields){
	  this.setInputFields(fields, null);
  }
	  
  public void setInputFields(String[] fields, String[] fieldTypes){
	this.fields = fields;
	this.fieldTypes = fieldTypes;
  }
	  
  /**
   * Makes the dialog visible.
   * 
   * @return
   */
  public Map<String, String> open() {
    Shell parent = getParent();
    final Shell shell =
      new Shell(parent, SWT.TITLE | SWT.BORDER | SWT.APPLICATION_MODAL);
    shell.setText("Beanshell - Init");
    
    GridLayout gd = new GridLayout(2, true);
    shell.setLayout(gd);

    for(String field: fields){
        Label label = new Label(shell, SWT.NULL);
        label.setText(field);
        final Text text = new Text(shell, SWT.SINGLE | SWT.BORDER);
        text.setData(field);
        listText.add(text);
    }

    final Button buttonOK = new Button(shell, SWT.PUSH);
    buttonOK.setText("Ok");
    buttonOK.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_END));
    Button buttonCancel = new Button(shell, SWT.PUSH);
    buttonCancel.setText("Cancel");

    buttonOK.addListener(SWT.Selection, new Listener() {
      public void handleEvent(Event event) {
	    for(Text text: listText){
	    	map.put(text.getData().toString(), text.getText());
	        text.setText("");
	    }
    	    
        shell.dispose();
      }
    });

    buttonCancel.addListener(SWT.Selection, new Listener() {
      public void handleEvent(Event event) {
        shell.dispose();
      }
    });
    
    shell.addListener(SWT.Traverse, new Listener() {
      public void handleEvent(Event event) {
        if(event.detail == SWT.TRAVERSE_ESCAPE)
          event.doit = false;
      }
    });

    shell.pack();
    shell.open();

    Display display = parent.getDisplay();
    while (!shell.isDisposed()) {
      if (!display.readAndDispatch())
        display.sleep();
    }

    return map;
  }

  public static void main(String[] args) {
    Shell shell = new Shell();
    DynamicFormComposite dialog = new DynamicFormComposite(shell);
    dialog.setInputFields(new String[]{"User", "Password", "New Password"});
    Map<String, String> map = dialog.open();
    Set<String> set = map.keySet();
    for(String key: set){
    	String value = (String)map.get(key);
    	System.out.println(key + "=" + value);
    }
  }
}
