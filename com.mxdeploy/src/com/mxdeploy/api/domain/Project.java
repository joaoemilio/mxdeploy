package com.mxdeploy.api.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Project implements Cloneable, Serializable, Comparable<Project> {
	  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/** 
     * Persistent Instance variables. This data is directly   
     * mapped to the columns of database table.
     */
    private String  id;
    private String  name; 
    private String  alias;
    private String  description;
    private String  docLink;
    private Integer isOpen;
    private String  sdm;
    private String  dpe;
    private String  isCritical;
    private List<Server> servers = new ArrayList<Server>();
    //private WorkbookProject workbookProject = null;
    private List<Url> urls = new ArrayList<Url>();
    private List<BeanShell> beanShells = new ArrayList<BeanShell>();
    
    public List<BeanShell> getBeanShells() {
		return beanShells;
	}

	public void setBeanShells(List<BeanShell> beanShells) {
		this.beanShells = beanShells;
	}

	private List<ProcedureFolder> subFolders = new ArrayList<ProcedureFolder>();
    private ProcedureFolder rootFolder = new ProcedureFolder();

	public ProcedureFolder getRootFolder() {
		return rootFolder;
	}

	public void setRootFolder(ProcedureFolder rootFolder) {
		this.rootFolder = rootFolder;
	}

	/** 
     * Constructors. DaoGen generates two constructors by default.
     * The first one takes no arguments and provides the most simple
     * way to create object instance. The another one takes one
     * argument, which is the primary key of the corresponding table.
     */

    public Project() {  }

    public Project (String idIn) {
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

    public String getAlias() {
          return this.alias;
    }
    public void setAlias(String alias) {
          this.alias = alias;
    }

    public String getDescription() {
          return this.description;
    }
    public void setDescription(String descriptionIn) {
          this.description = descriptionIn;
    }

	/**
	 * @return the docLink
	 */
	public String getDocLink() {
		return docLink;
	}

	/**
	 * @param docLink the docLink to set
	 */
	public void setDocLink(String docLink) {
		this.docLink = docLink;
	}
	
	/**
	 * @return the servers
	 */
	public List<Server> getServers() {
		return servers;
	}

	/**
	 * @param servers the servers to set
	 */
	public void addUrl(Url url) {
		this.urls.add(url);
	}

	/**
	 * @return the servers
	 */
	public List<Url> getUrls() {
		return urls;
	}

	/**
	 * @param servers the servers to set
	 */
	public void addServer(Server server) {
		this.servers.add(server);
	}
	
	public void clearUrls(){
		this.urls = new ArrayList<Url>();
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
          String alias,
          String descriptionIn) {
          this.id = idIn;
          this.name = nameIn;
          this.alias = alias;
          this.description = descriptionIn;
    }


    /** 
     * hasEqualMapping-method will compare two Project instances
     * and return true if they contain same values in all persistent instance 
     * variables. If hasEqualMapping returns true, it does not mean the objects
     * are the same instance. However it does mean that in that moment, they 
     * are mapped to the same row in database.
     */
    public boolean hasEqualMapping(Project valueObject) {

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
          if (this.alias == null) {
                    if (valueObject.getAlias() != null)
                           return(false);
          } else if (!this.alias.equals(valueObject.getAlias())) {
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
        out.append("\nclass Project, mapping to table TB_PROJECT\n");
        out.append("Persistent attributes: \n"); 
        out.append("id = " + this.id + "\n"); 
        out.append("name = " + this.name + "\n"); 
        out.append("sigla = " + this.alias + "\n"); 
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
        Project cloned = new Project();

        if (this.name != null)
             cloned.setName(new String(this.name)); 
        if (this.alias != null)
             cloned.setAlias(new String(this.alias)); 
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

	/**
	 * @return the isOpen
	 */
	public Integer isOpen() {
		return isOpen;
	}

	/**
	 * @param isOpen the isOpen to set
	 */
	public void setIsOpen(Integer isOpen) {
		this.isOpen = isOpen;
	}

	/**
	 * @return the dpe
	 */
	public String getDpe() {
		return dpe;
	}

	/**
	 * @param dpe the dpe to set
	 */
	public void setDpe(String dpe) {
		this.dpe = dpe;
	}

	/**
	 * @return the isCritical
	 */
	public String getIsCritical() {
		return isCritical;
	}

	/**
	 * @param isCritical the isCritical to set
	 */
	public void setIsCritical(String isCritical) {
		this.isCritical = isCritical;
	}

	/**
	 * @return the sdm
	 */
	public String getSdm() {
		return sdm;
	}

	/**
	 * @param sdm the sdm to set
	 */
	public void setSdm(String sdm) {
		this.sdm = sdm;
	}

	public void addSubFolder(ProcedureFolder subFolder) {
		this.subFolders.add(subFolder);
	}
	
	public List<ProcedureFolder> getSubFolders() {
		return this.subFolders;
	}

//	public WorkbookProject getWorkbookProject() {
//		return this.workbookProject;
//	}
//
//	public void setWorkbookProject(WorkbookProject workbookProject) {
//		this.workbookProject = workbookProject;
//	}
	
	@Override
	public int compareTo(Project arg0) {
		// TODO Auto-generated method stub
		Project p = (Project)arg0;

		return this.name.compareTo(p.name);
	}	
	
}



