package com.mxdeploy.api.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Command implements  Comparator<Command>, Cloneable, Serializable {

    /** 
     * Persistent Instance variables. This data is directly 
     * mapped to the columns of database table.
     */
    private String id;
    private String name;
    private String description;
    private List<CommandItem> commandItens = new ArrayList<CommandItem>();
    private String runtime;

    /**
	 * @return the commandItens
	 */
	public List<CommandItem> getCommandItens() {
		return commandItens;
	}

	/**
	 * @param commandItens the commandItens to set
	 */
	public void addCommandItem(CommandItem commandItem) {
		commandItens.add(commandItem);
	}

	/** 
     * Constructors. DaoGen generates two constructors by default.
     * The first one takes no arguments and provides the most simple
     * way to create object instance. The another one takes one
     * argument, which is the primary key of the corresponding table.
     */

    public Command () {

    }

    public Command (String idIn) {

          this.id = idIn;

    }


    /** 
     * Get- and Set-methods for persistent variables. The default
     * behaviour does not make any checks against malformed data,
     * so these might require some manual additions.
     */

    public String getId() {
          return this.id;
    }
    public void setId(String idIn) {
          this.id = idIn;
    }

    public String getName() {
          return this.name;
    }
    public void setName(String nameIn) {
          this.name = nameIn;
    }

    public String getDescription() {
          return this.description;
    }
    public void setDescription(String descriptionIn) {
          this.description = descriptionIn;
    }

    /**
	 * @return the runtime
	 */
	public String getRuntime() {
		return runtime;
	}

	/**
	 * @param runtime the runtime to set
	 */
	public void setRuntime(String runtime) {
		this.runtime = runtime;
	}

	/** 
     * setAll allows to set all persistent variables in one method call.
     * This is useful, when all data is available and it is needed to 
     * set the initial state of this object. Note that this method will
     * directly modify instance variales, without going trough the 
     * individual set-methods.
     */

    public void setAll(String idIn,
          String nameIn,
          String descriptionIn) {
          this.id = idIn;
          this.name = nameIn;
          this.description = descriptionIn;
    }


    /** 
     * hasEqualMapping-method will compare two Command instances
     * and return true if they contain same values in all persistent instance 
     * variables. If hasEqualMapping returns true, it does not mean the objects
     * are the same instance. However it does mean that in that moment, they 
     * are mapped to the same row in database.
     */
    public boolean hasEqualMapping(Command valueObject) {

          if (this.id == null) {
                    if (valueObject.getId() != null)
                           return(false);
          } else if (!this.id.equals(valueObject.getId())) {
                    return(false);
          }
          if (this.name == null) {
                    if (valueObject.getName() != null)
                           return(false);
          } else if (!this.name.equals(valueObject.getName())) {
                    return(false);
          }
          if (this.description == null) {
                    if (valueObject.getDescription() != null)
                           return(false);
          } else if (!this.description.equals(valueObject.getDescription())) {
                    return(false);
          }

          return true;
    }



    /**
     * toString will return String object representing the state of this 
     * valueObject. This is useful during application development, and 
     * possibly when application is writing object states in textlog.
     */
    public String toString() {
        StringBuffer out = new StringBuffer(this.getDaogenVersion());
        out.append("\nclass Command, mapping to table TB_COMMAND\n");
        out.append("Persistent attributes: \n"); 
        out.append("id = " + this.id + "\n"); 
        out.append("name = " + this.name + "\n"); 
        out.append("description = " + this.description + "\n"); 
        return out.toString();
    }


    /**
     * Clone will return identical deep copy of this valueObject.
     * Note, that this method is different than the clone() which
     * is defined in java.lang.Object. Here, the retuned cloned object
     * will also have all its attributes cloned.
     */
    public Object clone() {
        Command cloned = new Command();

        if (this.id != null)
             cloned.setId(this.id); 
        if (this.name != null)
             cloned.setName(new String(this.name)); 
        if (this.description != null)
             cloned.setDescription(new String(this.description)); 
        return cloned;
    }



    /** 
     * getDaogenVersion will return information about
     * generator which created these sources.
     */
    public String getDaogenVersion() {
        return "DaoGen version 2.4.1";
    }
    
    public void cleanCommandItemList(){
    	this.commandItens = new ArrayList<CommandItem>();
    }

	public int compare(Command o1, Command o2) {
		return o1.getName().compareTo(o2.getName());
	}
}


             