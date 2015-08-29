package com.ihn.server.rest;

import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

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
		for(String propertyAssetId : scopes){
			PropertyAsset propertyAsset = parkingService.getPropertyAsset(propertyAssetId);
			if(propertyAsset!=null){
				assets.add(propertyAsset);
			}else{
				//
				
			}
		}
		return JSONUtils.makeJSONStringFromObject(assets);
	}
		
	@POST
	@Path("properties")
	@Produces(MediaType.APPLICATION_JSON)
	public String createNewProperty(PropertyAsset asset){
		ParkingService parkingService=BizContext.getBean("parkingService",ParkingService.class);
		parkingService.createPropertyAsset(asset);
		
		SecurityService securityService=BizContext.getBean("securityService",SecurityService.class);
		String userName=asset.getUserObject();
		Set<String>  scopes=securityService.getManagedProperties(userName);
		scopes.add(asset.getId());
		securityService.updateManagedProperties(userName,scopes);
		
		return JSONUtils.makeJSONStringFromObject(asset);
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
