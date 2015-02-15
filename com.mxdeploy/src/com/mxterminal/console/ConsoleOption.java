 
 
  
  
  
  
  
  
  
  
  
  
 
  

package com.mxterminal.console;


public class ConsoleOption {
    private String key;
    private String description;
    private String defaultValue;
    private String value;
    private String[] choices;

    public ConsoleOption(String key, String description, String defValue) {
        this(key, description, defValue, null);
    }
    
    public ConsoleOption(String key, String description, String defValue,
                          String[] choices) {
        this.key = key;
        this.description = description;
        this.defaultValue = defValue;
        this.value = defValue;
        this.choices = choices;
    }

    public ConsoleOption copy() {
        ConsoleOption to = new ConsoleOption(key, description, defaultValue,
                                               choices);
        to.value = value;
        return to;
    }
    
    public String getKey() {
        return key;
    }

    public String getDescription() {
        return description;
    }

    public String getDefault() {
        return defaultValue;
    }

    public String[] getChoices() {
        return choices;
    }
    
    public String getValue() {
        return value;
    }

    public boolean getValueB() {
        return new Boolean(value).booleanValue();
    }

    public void setValue(String v) {
        value = v;
    }
}
