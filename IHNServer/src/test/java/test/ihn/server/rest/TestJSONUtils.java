package test.ihn.server.rest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

import com.ihn.server.internal.parking.model.PropertyAsset;
import com.ihn.server.rest.JSONUtils;

 
public class TestJSONUtils {

	@Test
	public void testA(){
		String json=JSONUtils.makeJSONString("result", true);
		Assert.assertEquals("{\"result\":true}", json);
	} 
	
	@Test
	public void testObject(){
		PropertyAsset asset=new PropertyAsset();
		asset.setId("LJZ_P1");
		asset.setName("LJZ Plaza");
		asset.setCorp("LJZ");
		asset.setCity("Shanghai");
		String json=JSONUtils.makeJSONString("result", asset);
		System.out.println(json);
	}
	
	@Test
	public void testCollection(){
		PropertyAsset asset=new PropertyAsset();
		asset.setId("LJZ_P1");
		asset.setName("LJZ Plaza");
		asset.setCorp("LJZ");
		asset.setCity("Shanghai");
		
		List<PropertyAsset> assets=new ArrayList<PropertyAsset>();
		assets.add(asset);
		
		String json1=JSONUtils.makeJSONStringFromObject(assets);
		String json2=JSONUtils.makeJSONStringFromCollection(assets);
		System.out.println(json1);
		System.out.println(json2);
		Assert.assertEquals(json1, json2);
		
		Map<String, PropertyAsset> map=new HashMap<String,PropertyAsset>();
		map.put("result",asset);
		String json3=JSONUtils.makeJSONStringFromMap(map);
		System.out.println(json3);
		
		String json4=JSONUtils.makeJSONStringFromObject(map);
		System.out.println(json4);
		
		Assert.assertEquals(json3, json4);
		
	}

}
