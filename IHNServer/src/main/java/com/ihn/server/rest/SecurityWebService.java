package com.ihn.server.rest;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.ihn.server.internal.launch.BizContext;
import com.ihn.server.internal.security.SecurityService;

@Path("/sm")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class SecurityWebService {
	
	@GET
	@Path("login")
	@Produces(MediaType.APPLICATION_JSON)
	public String  login(@QueryParam("username") String userName, @QueryParam("password")String password){
		try{
			SecurityService securityService=BizContext.getBean("securityService",SecurityService.class);
			return securityService.login(userName,password);
		}catch(Throwable th){
			return "{'result':'false', 'reason':'"+th.getMessage()+"'}";
		}
	}
	
	@GET
	@Path("properties")
	@Produces(MediaType.APPLICATION_JSON)
	public String getManagedProperties(@QueryParam("username") String userName){
		try{
			SecurityService securityService=BizContext.getBean("securityService",SecurityService.class);
			return securityService.getManagedProperties(userName);
		}catch(Throwable th){
			return "{'result':'false', 'reason':'"+th.getMessage()+"'}";
		}
	}
	
}
