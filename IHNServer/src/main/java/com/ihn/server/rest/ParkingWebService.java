package com.ihn.server.rest;

import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONObject;

import com.ihn.server.internal.launch.BizContext;
import com.ihn.server.internal.parking.ParkingService;
import com.ihn.server.internal.parking.model.PropertyAsset;
import com.ihn.server.internal.security.SecurityService;

@Path("/park")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class ParkingWebService {

	@GET
	@Path("properties")
	@Produces(MediaType.APPLICATION_JSON)
	public String getProperties(@QueryParam("username") String userName) throws Exception{
		SecurityService securityService=BizContext.getBean("securityService",SecurityService.class);
		Set<String>  scopes=securityService.getManagedProperties(userName);
		if(scopes==null || scopes.size()==0){
			return JSONUtils.makeJSONString("result", false, "reason", "No scope for this user");
		}
		
		ParkingService parkingService=BizContext.getBean("parkingService",ParkingService.class);
		Set<PropertyAsset> assets=new HashSet<PropertyAsset>();
		JSONArray list = new JSONArray();
		for(String propertyAssetId : scopes){
			PropertyAsset propertyAsset = parkingService.getPropertyAsset(propertyAssetId);
			assets.add(propertyAsset);
			
			JSONObject json=new JSONObject();
			json.put("id",propertyAsset.getId());
			json.put("name",propertyAsset.getName());
			json.put("corp",propertyAsset.getCorp());
			json.put("city",propertyAsset.getCity());
			json.put("address",propertyAsset.getAddress());
			json.put("longitude",propertyAsset.getLongitude());
			json.put("latitude",propertyAsset.getLatitude());
			list.put(json);
		}
		return list.toString();//JSONUtils.makeJSONStringFromCollection(assets);
	}
	
	@GET
	@Path("property")
	@Produces(MediaType.APPLICATION_JSON)
	public String getProperty(@QueryParam("id") String propertyAssetId){
		try{
			ParkingService parkingService=BizContext.getBean("parkingService",ParkingService.class);
			PropertyAsset propertyAsset = parkingService.getPropertyAsset(propertyAssetId);
			return JSONUtils.makeJSONString("result", true, "data", propertyAsset);
		}catch(Exception ex){
			return JSONUtils.makeJSONString("result", false, "reason", ex.getMessage());
		}
		
		/*
		return "[{\"id\":\"LJZ_P1\", \"name\": \"LuJiaZui Century Financial Plaza\","
				+"\"city\" : \"Shanghai\","
				+ " \"longitude\": 121.540844, \"latitude\":31.216669},"
				
				+"{\"id\" : \"PDSP_P1\",\"name\":\"PuDong Software Park\",\"city\":\"Shanghai\",\"longitude\":121.535813,\"latitude\":31.226057},"
				+"{\"id\" : \"PS_P1\",\"name\":\"People Squre\",\"city\":\"Shanghai\",\"longitude\":121.479399,\"latitude\":31.23847}"
				
				+"]";
		*/
	}
	
	@GET
	@Path("map")
	@Produces(MediaType.APPLICATION_JSON)
	public String getInitedMap(@QueryParam("id") String propertyId){
		return "readFrom(myjson)";
	}
	
	@GET
	@Path("search")
	@Produces(MediaType.APPLICATION_JSON)
	public String searchPlaces(@QueryParam("keyword") String keyword){
		return "{'result':'true','exit': 'LJZ_P1_B1_Exit1'}";
	}
	
}
