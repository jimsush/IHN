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
		return "{'result':'true','id':'LJZ_P1','name':'LuJiaZui Financial Plaza', 'longitude':121.284, 'latitude':31.15, 'floors':['B1', 'B2', 'B3']}";
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
