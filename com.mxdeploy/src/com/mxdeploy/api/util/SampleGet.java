package com.mxdeploy.api.util;

import java.lang.reflect.Field;

class SampleGet {

	   public static void main(String[] args) {
		   String r = new String("  ");
		   printHeight(r);
	   }

	   static void printHeight(String r) {
	      Field heightField;
	      Integer heightValue;
	      Class c = r.getClass();
	      try {
	         heightField = c.getField("height");
	         heightValue = (Integer) heightField.get(c);
	         System.out.println(
	            "Height: " + heightValue.toString());
	      } catch (NoSuchFieldException e) {
	         System.out.println(e);
	      } catch (SecurityException e) {
	         System.out.println(e);
	      } catch (IllegalAccessException e) {
	         System.out.println(e);
	      }
	   }
	} 
