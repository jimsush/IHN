package com.ihn.server.rest;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

public class JSONUtils {
	
	public static String makeJSONString(String key, Object value){
		JSONObject json=new JSONObject();
		try {
			json.put(key, value);
		} catch (JSONException e) {
			throw new RuntimeException(e.getMessage());
		}
		return json.toString();
	}
	
	public static String makeJSONString(String key1, Object value1,String key2, Object value2){
		JSONObject json=new JSONObject();
		try {
			json.put(key1, value1);
			json.put(key2, value2);
		} catch (JSONException e) {
			throw new RuntimeException(e.getMessage());
		}
		return json.toString();
	}
	
	
	public static String makeJSONString(String key1, Object value1,String key2, Object value2,String key3, Object value3){
		JSONObject json=new JSONObject();
		try {
			json.put(key1, value1);
			json.put(key2, value2);
			json.put(key3, value3);
		} catch (JSONException e) {
			throw new RuntimeException(e.getMessage());
		}
		return json.toString();
	}
	
	public static String makeJSONString(String[] key, Object[] value){
		JSONObject json=new JSONObject();
		int len=key.length;
		try {
			for(int i=0;i<len;i++){
				json.put(key[i], value[i]);
			}
		} catch (JSONException e) {
			throw new RuntimeException(e.getMessage());
		}
		return json.toString();
	}

}

