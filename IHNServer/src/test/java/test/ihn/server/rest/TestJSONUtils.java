package test.ihn.server.rest;

import org.junit.Assert;
import org.junit.Test;

import com.ihn.server.rest.JSONUtils;

 
public class TestJSONUtils {

	@Test
	public void testA(){
		String json=JSONUtils.makeJSONString("result", true);
		Assert.assertEquals("{\"result\":true}", json);
	} 

}
