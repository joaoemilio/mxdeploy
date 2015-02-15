package com.mxdeploy.api.domain;

import java.io.Serializable;

public class CommandItem implements Cloneable, Serializable {
    /** 
     * Persistent Instance variables. This data is directly 
     * mapped to the columns of database table.
     */
    private Integer id;
    private String idCommand;
    private String commandSSH;


    /** 
     * Constructors. DaoGen generates two constructors by default.
     * The first one takes no arguments and provides the most simple
     * way to create object instance. The another one takes one
     * argument, which is the primary key of the corresponding table.
     */

    public CommandItem () {

    }

    public CommandItem (Integer idIn) {

          this.id = idIn;

    }


    /** 
     * Get- and Set-methods for persistent variables. The default
     * behaviour does not make any checks against malformed data,
     * so these might require some manual additions.
     */

    public Integer getId() {
          return this.id;
    }
    public void setId(Integer idIn) {
          this.id = idIn;
    }

    public String getIdCommand() {
          return this.idCommand;
    }
    public void setIdCommand(String idCommandIn) {
          this.idCommand = idCommandIn;
    }

    public String getCommandSSH() {
          return this.commandSSH;
    }
    public void setCommandSSH(String commandSSHIn) {
          this.commandSSH = commandSSHIn;
    }


    /** 
     * setAll allows to set all persistent variables in one method call.
     * This is useful, when all data is available and it is needed to 
     * set the initial state of this object. Note that this method will
     * directly modify instance variales, without going trough the 
     * individual set-methods.
     */

    public void setAll(Integer idIn,
          String idCommandIn,
          String commandSSHIn,
          String descriptionIn) {
          this.id = idIn;
          this.idCommand = idCommandIn;
          this.commandSSH = commandSSHIn;
    }


    /** 
     * hasEqualMapping-method will compare two CommandItem instances
     * and return true if they contain same values in all persistent instance 
     * variables. If hasEqualMapping returns true, it does not mean the objects
     * are the same instance. However it does mean that in that moment, they 
     * are mapped to the same row in database.
     */
    public boolean hasEqualMapping(CommandItem valueObject) {

          if (this.id == null) {
                    if (valueObject.getId() != null)
                           return(false);
          } else if (!this.id.equals(valueObject.getId())) {
                    return(false);
          }
          if (this.idCommand == null) {
                    if (valueObject.getIdCommand() != null)
                           return(false);
          } else if (!this.idCommand.equals(valueObject.getIdCommand())) {
                    return(false);
          }
          if (this.commandSSH == null) {
                    if (valueObject.getCommandSSH() != null)
                           return(false);
          } else if (!this.commandSSH.equals(valueObject.getCommandSSH())) {
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
        out.append("\nclass CommandItem, mapping to table TB_COMMAND_ITEM\n");
        out.append("Persistent attributes: \n"); 
        out.append("id = " + this.id + "\n"); 
        out.append("idCommand = " + this.idCommand + "\n"); 
        out.append("commandSSH = " + this.commandSSH + "\n"); 
        return out.toString();
    }


    /**
     * Clone will return identical deep copy of this valueObject.
     * Note, that this method is different than the clone() which
     * is defined in java.lang.Object. Here, the retuned cloned object
     * will also have all its attributes cloned.
     */
    public Object clone() {
        CommandItem cloned = new CommandItem();

        if (this.id != null)
             cloned.setId(this.id); 
        if (this.idCommand != null)
             cloned.setIdCommand(this.idCommand); 
        if (this.commandSSH != null)
             cloned.setCommandSSH(new String(this.commandSSH)); 
        return cloned;
    }



    /** 
     * getDaogenVersion will return information about
     * generator which created these sources.
     */
    public String getDaogenVersion() {
        return "DaoGen version 2.4.1";
    }

}
