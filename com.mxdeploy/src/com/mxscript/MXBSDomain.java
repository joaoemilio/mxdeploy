package com.mxscript;

import java.io.Serializable;

public class MXBSDomain implements Serializable{

	protected String status;
	protected String statusDescription;
	
	public String getStatus(){
		return status;
	}
	
	public void setStatus(String status) {
		this.status = status;
	}

	public String getStatusDescription() {
		return statusDescription;
	}

	public void setStatusDescription(String statusDescription) {
		this.statusDescription = statusDescription;
	}
	
}
