package com.mxterminal.swt.util;

import java.util.ArrayList;
import java.util.List;


public class TerminalProperty {

	private static String savelines = "256";
    private static String tunnelSSH= null;
    private static Integer serverGroup=null;
    private static String sudosu = null;
    private static Boolean bash = false;
    private static Boolean setovi = false;
    private static Boolean afterstart = false;
    private static String ps1 = null;
    private static String taxi=null;
    private static String fontName = "Monospaced";
    private static String fontSize = "12";
    private static String encoding= "utf-8";
    private static String visual_bell = "true";
    private static String copy_select = "true";
    private static String paste_button = "right";
    private static String fg_color = "187,187,187";
    private static String bg_color = "0,0,0";
    private static String cursor_color="0,255,0";
    private static String backspace_send="bs";
    private static String delete_send="del";
    private static String keepassx_path;
    private static String keepassx_keyfile;		
    private static String keepassx_iconNumber;		
    private static String beanshellGeanyEnable;	
    
    private static String pbrun = null;
    
    private static String localhostUsername = null;
    private static String localhostPrivateKey = null;

    private static String gateway = null;

    private static List<TerminalPrivateKey>  terminalPrivateKeyList;
    private static List<String> gatewayPropertyList;
    private static List<Socks5Property> socks5PropertyList;
    private static List<TerminalExecuteCommand> terminalExecuteCommandList;
    private static List<TerminalDomainRule> terminalDomainRuleList;
    
    
	public static List<TerminalDomainRule> getTerminalDomainRuleList() {
		return terminalDomainRuleList;
	}
	
	public static void addTerminalDomainRule(TerminalDomainRule terminalExecuteCommand) {
		if( terminalDomainRuleList == null ){
			terminalDomainRuleList = new ArrayList<TerminalDomainRule>();
		}
		
		terminalDomainRuleList.add(terminalExecuteCommand);
	}
	
	public static List<TerminalExecuteCommand> getTerminalExecuteCommandList() {
		return terminalExecuteCommandList;
	}
	
	public static void addTerminalExecuteCommand(TerminalExecuteCommand terminalExecuteCommand) {
		if( terminalExecuteCommandList == null ){
			terminalExecuteCommandList = new ArrayList<TerminalExecuteCommand>();
		}
		
		terminalExecuteCommandList.add(terminalExecuteCommand);
	}
	
	public static List<Socks5Property> getSocks5PropertyList() {
		return socks5PropertyList;
	}
	
	public static void addSocks5Property(Socks5Property socks5Property) {
		if( socks5PropertyList == null ){
			socks5PropertyList = new ArrayList<Socks5Property>();
		}
		
		socks5PropertyList.add(socks5Property);
	}
	
	public static List<String> getGatewayPropertyList() {
		return gatewayPropertyList;
	}

	public static void addGatewayPropertyList(String gatewayProperty) {
		if( gatewayPropertyList == null ){
			gatewayPropertyList = new ArrayList<String>();
		}
		
		gatewayPropertyList.add(gatewayProperty);
	}

	public static List<TerminalPrivateKey> getTerminalPrivateKeyList() {
		return terminalPrivateKeyList;
	}
	
	public static void addTerminalPrivateKey(TerminalPrivateKey tpk){
		if( terminalPrivateKeyList == null ){
			terminalPrivateKeyList = new ArrayList<TerminalPrivateKey>();
		}
		
		terminalPrivateKeyList.add(tpk);
	}

	public static String getFontName() {
		return fontName;
	}

	public static void setFontName(String fontName) {
		TerminalProperty.fontName = fontName;
	}

	/**
	 * @return the afterstart
	 */
	public static Boolean getAfterstart() {
		return afterstart;
	}

	/**
	 * @param afterstart the afterstart to set
	 */
	public static void setAfterstart(Boolean afterstart) {
		TerminalProperty.afterstart = afterstart;
	}

	/**
	 * @return the serverGroup
	 */
	public static Integer getServerGroup() {
		return serverGroup;
	}

	/**
	 * @param serverGroup the serverGroup to set
	 */
	public static void setServerGroup(Integer serverGroup) {
		TerminalProperty.serverGroup = serverGroup;
	}

	/**
	 * @return the ssh
	 */
	public static String getTunnelSSH() {
		return tunnelSSH;
	}

	/**
	 * @param ssh the ssh to set
	 */
	public static void setTunnelSSH(String tunnelSSH) {
		TerminalProperty.tunnelSSH = tunnelSSH;
	}

	/**
	 * @return the savelines
	 */
	public static String getSavelines() {
		return savelines;
	}

	/**
	 * @param savelines the savelines to set
	 */
	public static void setSavelines(String savelines) {
		TerminalProperty.savelines = savelines;
	}

	/**
	 * @return the bash
	 */
	public static Boolean getBash() {
		return bash;
	}

	/**
	 * @param bash the bash to set
	 */
	public static void setBash(Boolean bash) {
		TerminalProperty.bash = bash;
	}

	/**
	 * @return the setovi
	 */
	public static Boolean getSetovi() {
		return setovi;
	}

	/**
	 * @param setovi the setovi to set
	 */
	public static void setSetovi(Boolean setovi) {
		TerminalProperty.setovi = setovi;
	}

	/**
	 * @return the sudosu
	 */
	public static String getSudosu() {
		return sudosu;
	}

	/**
	 * @param sudosu the sudosu to set
	 */
	public static void setSudosu(String sudosu) {
		TerminalProperty.sudosu = sudosu;
	}

	/**
	 * @return the ps1
	 */
	public static String getPs1() {
		return ps1;
	}

	/**
	 * @param ps1 the ps1 to set
	 */
	public static void setPs1(String ps1) {
		TerminalProperty.ps1 = ps1;
	}

	/**
	 * @return the taxi
	 */
	public static String getTaxi() {
		return taxi;
	}

	/**
	 * @param taxi the taxi to set
	 */
	public static void setTaxi(String taxi) {
		TerminalProperty.taxi = taxi;
	}

	public static String getFontSize() {
		return fontSize;
	}

	public static void setFontSize(String fontSize) {
		TerminalProperty.fontSize = fontSize;
	}

	public static String getPbrun() {
		return pbrun;
	}

	public static void setPbrun(String pbrun) {
		TerminalProperty.pbrun = pbrun;
	}

	public static String getLocalhostUsername() {
		return localhostUsername;
	}

	public static void setLocalhostUsername(String localhostUsername) {
		TerminalProperty.localhostUsername = localhostUsername;
	}

	public static String getLocalhostPrivateKey() {
		return localhostPrivateKey;
	}

	public static void setLocalhostPrivateKey(String localhostPrivateKey) {
		TerminalProperty.localhostPrivateKey = localhostPrivateKey;
	}

	public static String getEncoding() {
		return encoding;
	}

	public static void setEncoding(String encoding) {
		TerminalProperty.encoding = encoding;
	}

	public static String getVisualBell() {
		return visual_bell;
	}

	public static void setVisualBell(String visual_bell) {
		TerminalProperty.visual_bell = visual_bell;
	}

	public static String getCopySelect() {
		return copy_select;
	}

	public static void setCopySelect(String copy_select) {
		TerminalProperty.copy_select = copy_select;
	}

	public static String getPasteButton() {
		return paste_button;
	}

	public static void setPasteButton(String paste_button) {
		TerminalProperty.paste_button = paste_button;
	}

	public static String getFgColor() {
		return fg_color;
	}

	public static void setFgColor(String fg_color) {
		TerminalProperty.fg_color = fg_color;
	}

	public static String getBgColor() {
		return bg_color;
	}

	public static void setBgColor(String bg_color) {
		TerminalProperty.bg_color = bg_color;
	}

	public static String getCursorColor() {
		return cursor_color;
	}

	public static void setCursorColor(String cursor_color) {
		TerminalProperty.cursor_color = cursor_color;
	}

	public static String getBackspaceSend() {
		return backspace_send;
	}

	public static void setBackspaceSend(String backspace_send) {
		TerminalProperty.backspace_send = backspace_send;
	}

	public static String getDeleteSend() {
		return delete_send;
	}

	public static void setDeleteSend(String delete_send) {
		TerminalProperty.delete_send = delete_send;
	}

	public static String getKeepassxPath() {
		return keepassx_path;
	}

	public static void setKeepassxPath(String keepassx_path) {
		TerminalProperty.keepassx_path = keepassx_path;
	}

	public static String getKeepassxKeyfile() {
		return keepassx_keyfile;
	}

	public static void setKeepassxKeyfile(String keepassx_keyfile) {
		TerminalProperty.keepassx_keyfile = keepassx_keyfile;
	}

	public static String getKeepassxIconNumber() {
		return keepassx_iconNumber;
	}

	public static void setKeepassxIconNumber(String keepassx_iconNumber) {
		TerminalProperty.keepassx_iconNumber = keepassx_iconNumber;
	}

	public static String getGateway() {
		return gateway;
	}

	public static void setGateway(String gateway) {
		TerminalProperty.gateway = gateway;
	}

	public static String getBeanshellGeanyEnable() {
		return beanshellGeanyEnable;
	}

	public static void setBeanshellGeanyEnable(String beanshellGeanyEnable) {
		TerminalProperty.beanshellGeanyEnable = beanshellGeanyEnable;
	}
	
}
