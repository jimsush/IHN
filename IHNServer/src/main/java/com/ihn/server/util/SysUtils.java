package com.ihn.server.util;

import java.util.HashSet;
import java.util.Set;

public class SysUtils {

	public static String getOSPlatform(){
		return System.getProperty("os.name");
	}
	
	public static void sleepNotException(long millis){
        try{
            Thread.sleep(millis);
        }catch(Exception ex){
            System.out.println("sleep ex,"+ex.getClass().getSimpleName());
        }
    }
	
	public static Set array2Set(Object[] objs){
		Set set=new HashSet();
		if(objs==null || objs.length==0)
			return set;
		
		for(Object obj : objs)
			set.add(obj);
		
		return set;
	}
	
	public static Set<String> string2Set(String string,String separator){
		if(string==null || string.length()==0)
			return new HashSet<String>();
		String[] fields=string.split(separator);
		return (Set<String>)array2Set(fields);
	}
	
	/**
	 * format a set to a string
	 * @param set
	 * @param separator  such as , | || ; 
	 * @return a,b,c,d  <p>return null if set is null
	 */
	public static String set2String(Set set, String separator){
		if(set==null || set.size()==0)
			return null;
		
		StringBuilder sb=new StringBuilder();
		int i=0;
		for(Object obj : set){
			if(i==0){
				i=1;
			}else{
				sb.append(separator);
			}
			sb.append(obj);
		}
		return sb.toString();
	}
	
	
	
}
