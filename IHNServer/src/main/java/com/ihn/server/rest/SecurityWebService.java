package com.ihn.server.rest;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

@Path("/sm")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class SecurityWebService {

	@GET
	@Path("login")
	@Produces(MediaType.APPLICATION_JSON)
	public String  login(@QueryParam("username") String userName, @QueryParam("password")String password){
		if(userName.equals("su"))
			return "ok";
		else
			return "failed";
	}
	
	
}
