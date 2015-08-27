package com.ihn.server.rest;

import java.util.Collection;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;


public class JSONUtils {
	
	public static String makeJSONString(String key, Object value){
		JSONObject json=new JSONObject();
		json.put(key, value);
		return json.toString();
	}
	
	public static String makeJSONString(String key1, Object value1,String key2, Object value2){
		JSONObject json=new JSONObject();
		json.put(key1, value1);
		json.put(key2, value2);
		return json.toString();
	}
	
	
	public static String makeJSONString(String key1, Object value1,String key2, Object value2,String key3, Object value3){
		JSONObject json=new JSONObject();
		json.put(key1, value1);
		json.put(key2, value2);
		json.put(key3, value3);
		return json.toString();
	}
	
	public static String makeJSONString(String[] key, Object[] value){
		JSONObject json=new JSONObject();
		int len=key.length;
		for(int i=0;i<len;i++){
			json.put(key[i], value[i]);
		}
		return json.toString();
	}
	
	@SuppressWarnings("rawtypes")
	public static String makeJSONStringFromCollection(Collection objs){
		JSONArray obj=JSONArray.fromObject(objs);
		return obj.toString();
		
	}
	
	@SuppressWarnings("rawtypes")
	public static String makeJSONStringFromMap(Map objs){
		JSONArray obj=JSONArray.fromObject(objs);
		return obj.toString();
	}
	
	@SuppressWarnings("rawtypes")
	public static String makeJSONStringFromObject(Object obj){
		if(obj instanceof Collection)
			return makeJSONStringFromCollection((Collection)obj);
		else if(obj instanceof Map)
			return makeJSONStringFromMap((Map)obj);
		
		JSONObject json=JSONObject.fromObject(obj);
		return json.toString();
		
	}

}

