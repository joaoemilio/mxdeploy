package com.mxdeploy.swt;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.PopupList;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

public class PopupListUsing {
  public static void main(String[] args) {
    Display display = new Display();
    final Shell shell = new Shell(display);
    shell.setText("PopupList Test");
    shell.setLayout(new RowLayout());

    // Create a button to launch the list
    Button button = new Button(shell, SWT.PUSH);
    button.setText("Push Me");
    button.addSelectionListener(new SelectionAdapter() {
      public void widgetSelected(SelectionEvent event) {
        PopupList list = new PopupList(shell);

        list.setItems(new String[]{"A","B","C"});

        String selected = list.open(shell.getBounds());
        System.out.println(selected);
      }
    });

    shell.pack();
    shell.open();
    while (!shell.isDisposed()) {
      if (!display.readAndDispatch()) {
        display.sleep();
      }
    }
    display.dispose();

  }
}
