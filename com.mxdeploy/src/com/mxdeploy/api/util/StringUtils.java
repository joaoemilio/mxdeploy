package com.mxdeploy.api.util;

public class StringUtils {

	public static String replaceSpacesAndTabs(String line){
		line = line.replaceAll("\t", "  ");
		line = line.replaceAll("                 ", " ");
		line = line.replaceAll("                ", " ");
		line = line.replaceAll("               ", " ");
		line = line.replaceAll("              ", " ");
		line = line.replaceAll("             ", " ");
		line = line.replaceAll("            ", " ");
		line = line.replaceAll("           ", " ");
		line = line.replaceAll("          ", " ");
		line = line.replaceAll("         ", " ");
		line = line.replaceAll("        ", " ");
		line = line.replaceAll("       ", " ");
		line = line.replaceAll("      ", " ");
		line = line.replaceAll("     ", " ");
		line = line.replaceAll("    ", " ");
		line = line.replaceAll("   ", " ");
		line = line.replaceAll("  ", " ");
		return line;
	}
	
	
}
