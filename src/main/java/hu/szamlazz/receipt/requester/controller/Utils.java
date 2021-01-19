package hu.szamlazz.receipt.requester.controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
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
    
public static boolean store(String content) throws FileNotFoundException, IOException  {
		
		File subfolder = new File("/szamlazz/log");
		subfolder.mkdirs();
		
		File target = new File(subfolder, "log" + (new Date()).getTime() + ".log");
		target.createNewFile();
		
		FileOutputStream fileOut = new FileOutputStream(target);
		fileOut.write(content.getBytes());
		fileOut.close();
		
		return true;
	}

}
