package com.mxdeploy.api.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
/**
 * @author jbento
 */
public class DateFormatter {

	private static DateFormatter me;
	private static SimpleDateFormat formatter;
	
	private DateFormatter(){
		formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		formatter.setLenient(false);
	}
	
	public static DateFormatter getInstance(){
		if(me==null){
			me = new DateFormatter();
		}
		return me;
	}

	/**
	 * Transformar um Date e uma string formatada 
	 * @param data
	 * @return data formatada
	 */
	public String formatDate(Date data) {
		if( data == null ){
			return "";
		}
		return formatter.format(data);
	}
	
	/**
	 * Transformar um Date e uma string formatada com hora 
	 * @param data
	 * @return data formatada
	 */
	public String formatDataHora(Date data) {
		if( data == null ){
			return "";
		}
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
		return formatter.format(data);
	}
	
	/**
	 * Transformar um Date e uma string formatada com hora 
	 * @param data
	 * @return data formatada
	 */
	public String formatDataHoraPattern(Date data, String pattern) {
		if( data == null ){
			return "";
		}
		SimpleDateFormat formatter = new SimpleDateFormat(pattern, Locale.ENGLISH);
		return formatter.format(data);
	}
	
	/**
	 * Transformar uma string formatada de data em um Date 
	 * @param data (string formatada)
	 * @return Date
	 */
	public Date parseDate(String data) throws ParseException {
		if( data == null || data.trim().length() == 0 ){
			return null;
		}
		return formatter.parse(data);
	}
	
	/**
	 * Transformar uma string de data em um Date baseado em um pattern 
	 * retorna null se não conseguir fazer o parse
	 * @param data (string formatada)
	 * @param data (string pattern)
	 * @return Date
	 */
	public Date parseDate(String data, String pattern) {
		if( data == null || data.trim().length() == 0 ){
			return null;
		}
		SimpleDateFormat formatter = new SimpleDateFormat(pattern);
		try {
			return formatter.parse(data);
		} catch (ParseException e) {
			return null;
		}
	}
	
	/**
	 * main de exemplos
	 */
	public static void main(String args[]) {
		String sdata = "25/01/2005";
		Date ddata = new Date();
		
		try{
			System.out.println( "data valida: " + DateFormatter.getInstance().parseDate(sdata) );
		}catch(ParseException pe){
			System.out.println(pe);
		}

		try{
			System.out.println( "data inv�lida: " + DateFormatter.getInstance().parseDate("30/02/2005") );
		}catch(ParseException pe){
			System.out.println(pe);
		}
		
		System.out.println(DateFormatter.getInstance().formatDate(ddata));
		System.out.println(DateFormatter.getInstance().formatDate(null));
	}
	
}
