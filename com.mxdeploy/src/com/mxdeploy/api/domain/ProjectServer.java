package com.mxdeploy.api.domain;

import java.io.Serializable;

public class ProjectServer implements Cloneable, Serializable {

    /** 
     * Persistent Instance variables. This data is directly 
     * mapped to the columns of database table.
     */
    private String idProject;
    private String idServer;



    /** 
     * Constructors. DaoGen generates two constructors by default.
     * The first one takes no arguments and provides the most simple
     * way to create object instance. The another one takes one
     * argument, which is the primary key of the corresponding table.
     */

    public ProjectServer () {

    }

    public ProjectServer (String idProjectIn) {

          this.idProject = idProjectIn;

    }


    /** 
     * Get- and Set-methods for persistent variables. The default
     * behaviour does not make any checks against malformed data,
     * so these might require some manual additions.
     */

    public String getIdProject() {
          return this.idProject;
    }
    public void setIdProject(String idProjectIn) {
          this.idProject = idProjectIn;
    }

    public String getIdServer() {
          return this.idServer;
    }
    public void setIdServer(String idServerIn) {
          this.idServer = idServerIn;
    }



    /** 
     * setAll allows to set all persistent variables in one method call.
     * This is useful, when all data is available and it is needed to 
     * set the initial state of this object. Note that this method will
     * directly modify instance variales, without going trough the 
     * individual set-methods.
     */

    public void setAll(String idProjectIn,
    		String idServerIn) {
          this.idProject = idProjectIn;
          this.idServer = idServerIn;
    }


    /** 
     * hasEqualMapping-method will compare two ProjectServer instances
     * and return true if they contain same values in all persistent instance 
     * variables. If hasEqualMapping returns true, it does not mean the objects
     * are the same instance. However it does mean that in that moment, they 
     * are mapped to the same row in database.
     */
    public boolean hasEqualMapping(ProjectServer valueObject) {

          if (this.idProject == null) {
                    if (valueObject.getIdProject() != null)
                           return(false);
          } else if (!this.idProject.equals(valueObject.getIdProject())) {
                    return(false);
          }
          if (this.idServer == null) {
                    if (valueObject.getIdServer() != null)
                           return(false);
          } else if (!this.idServer.equals(valueObject.getIdServer())) {
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
        out.append("\nclass ProjectServer, mapping to table TB_PROJECT_SERVER\n");
        out.append("Persistent attributes: \n"); 
        out.append("idProject = " + this.idProject + "\n"); 
        out.append("idServer = " + this.idServer + "\n"); 
        return out.toString();
    }


    /**
     * Clone will return identical deep copy of this valueObject.
     * Note, that this method is different than the clone() which
     * is defined in java.lang.Object. Here, the retuned cloned object
     * will also have all its attributes cloned.
     */
    public Object clone() {
        ProjectServer cloned = new ProjectServer();

        if (this.idProject != null)
             cloned.setIdProject(this.idProject); 
        if (this.idServer != null)
             cloned.setIdServer(this.idServer); 
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


