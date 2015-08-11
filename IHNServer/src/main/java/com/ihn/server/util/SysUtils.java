package com.ihn.server.util;

public class SysUtils {

	public static void sleepNotException(long millis){
        try{
            Thread.sleep(millis);
        }catch(Exception ex){
            System.out.println("sleep ex,"+ex.getClass().getSimpleName());
        }
    }
	
}
