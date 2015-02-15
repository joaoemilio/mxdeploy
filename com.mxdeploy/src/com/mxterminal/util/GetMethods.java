package com.mxterminal.util;

import java.lang.reflect.Method;

public class GetMethods {
    public static void main(String[] args) {
        GetMethods object = new GetMethods();
        Class clazz = object.getClass();

        //
        // Get all declared methods including public, protected, private and
        // package (default) access but excluding the inherited methods.
        //
        Method[] methods = clazz.getDeclaredMethods();
        for (Method method : methods) {
            System.out.println("Method name        = " + method.getName());
            System.out.println("Method return type = " + method.getReturnType().getName());

            Class[] paramTypes = method.getParameterTypes();
           // method.getd
            for (int i=0; i < paramTypes.length; i++) {
            	System.out.println("Param type         = " + paramTypes[i].getSimpleName());
                System.out.println("Param name         = " + paramTypes[i].getDeclaredFields().toString());
            }

            System.out.println("----------------------------------------");
        }

        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");

        //
        // Get all methods including the inherited method. Using the getMethods()
        // we can only access public methods.
        //
//        methods = clazz.getMethods();
//        for (Method method : methods) {
//            System.out.println("Method name        = " + method.getName());
//            System.out.println("Method return type = " + method.getReturnType().getName());
//
//            Class[] paramTypes = method.getParameterTypes();
//            for (Class c : paramTypes) {
//                System.out.println("Param type         = " + c.getName());
//            }
//
//            System.out.println("----------------------------------------");
//        }
//
//        try {
//            //
//            // We can also get method by their name and parameter types, here we
//            // are tryinh to get the add(int, int) method.
//            //
//            Method method = clazz.getMethod("add", new Class[] {int.class,  int.class});
//            System.out.println("Method name: " + method.getName());
//        } catch (NoSuchMethodException e) {
//            e.printStackTrace();
//        }
    }

    public int add(int numberA, int numberB) {
        return numberA + numberB;
    }

    protected int multiply(int numberA, int numberB) {
        return numberA * numberB;
    }

    private double div(int numberA, int numberB) {
        return numberA / numberB;
    }
}
