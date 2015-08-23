package test.ihn.server.rest;

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

}
