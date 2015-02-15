package com.mxdeploy.api.domain;

public class ApplicationServer {

	private String name;
	private String systemOut;
	private String systemErr;
	private String portSSL;
	private String portSOAP;
	private String securityRepository;
	private String httpPort;
	private String httpsPort;
	private String dirLog;
	private Boolean isRunnig;
	
	/**
	 * @return the dirLog
	 */
	public String getDirLog() {
		return dirLog;
	}
	/**
	 * @param dirLog the dirLog to set
	 */
	public void setDirLog(String dirLog) {
		this.dirLog = dirLog;
	}
	/**
	 * @return the httpPort
	 */
	public String getHttpPort() {
		return httpPort;
	}
	/**
	 * @param httpPort the httpPort to set
	 */
	public void setHttpPort(String httpPort) {
		this.httpPort = httpPort;
	}
	/**
	 * @return the httpsPort
	 */
	public String getHttpsPort() {
		return httpsPort;
	}
	/**
	 * @param httpsPort the httpsPort to set
	 */
	public void setHttpsPort(String httpsPort) {
		this.httpsPort = httpsPort;
	}
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return the portSOAP
	 */
	public String getPortSOAP() {
		return portSOAP;
	}
	/**
	 * @param portSOAP the portSOAP to set
	 */
	public void setPortSOAP(String portSOAP) {
		this.portSOAP = portSOAP;
	}
	/**
	 * @return the portSSL
	 */
	public String getPortSSL() {
		return portSSL;
	}
	/**
	 * @param portSSL the portSSL to set
	 */
	public void setPortSSL(String portSSL) {
		this.portSSL = portSSL;
	}
	/**
	 * @return the securityRepository
	 */
	public String getSecurityRepository() {
		return securityRepository;
	}
	/**
	 * @param securityRepository the securityRepository to set
	 */
	public void setSecurityRepository(String securityRepository) {
		this.securityRepository = securityRepository;
	}
	/**
	 * @return the systemErr
	 */
	public String getSystemErr() {
		return systemErr;
	}
	/**
	 * @param systemErr the systemErr to set
	 */
	public void setSystemErr(String systemErr) {
		this.systemErr = systemErr;
	}
	/**
	 * @return the systemOut
	 */
	public String getSystemOut() {
		return systemOut;
	}
	/**
	 * @param systemOut the systemOut to set
	 */
	public void setSystemOut(String systemOut) {
		this.systemOut = systemOut;
	}
	/**
	 * @return the isRunnig
	 */
	public Boolean IsRunnig() {
		return isRunnig;
	}
	/**
	 * @param isRunnig the isRunnig to set
	 */
	public void setIsRunnig(Boolean isRunnig) {
		this.isRunnig = isRunnig;
	}
	
	
}
