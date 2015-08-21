package com.ihn.server.rest;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

@Path("/park")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class ParkingWebService {

	@GET
	@Path("property")
	@Produces(MediaType.APPLICATION_JSON)
	public String getProperty(@QueryParam("id") String propertyId){
		return "[{\"id\":\"LJZ_P1\", \"name\": \"LuJiaZui Century Financial Plaza\","
				+"\"city\" : \"Shanghai\","
				+ " \"longitude\": 121.540844, \"latitude\":31.216669},"
				
				+"{\"id\" : \"PDSP_P1\",\"name\":\"PuDong Software Park\",\"city\":\"Shanghai\",\"longitude\":121.535813,\"latitude\":31.226057},"
				+"{\"id\" : \"PS_P1\",\"name\":\"People Squre\",\"city\":\"Shanghai\",\"longitude\":121.479399,\"latitude\":31.23847}"
				
				+"]";
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
