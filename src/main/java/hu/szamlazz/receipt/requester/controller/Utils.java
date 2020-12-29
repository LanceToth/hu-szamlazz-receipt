package hu.szamlazz.receipt.requester.controller;

import java.util.Date;

public class Utils {
	
    public static String nullOrString(Object object) {
    	return object == null? "null": object.toString();
    }
    
    public static void log(String message) {
    	log(null, message);
    }
    
    public static void log(String method, String message) {
    	System.out.println("==" + new Date() + (method != null? " Method: " + method: "") + " " + message);
    }

}
