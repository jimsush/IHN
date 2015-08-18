package com.ihn.server.rest;

import java.util.Set;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.ihn.server.internal.launch.BizContext;
import com.ihn.server.internal.security.IhnSecurityException;
import com.ihn.server.internal.security.SecurityService;

@Path("/sm")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class SecurityWebService {
	
	@GET
	@Path("login")
	@Produces(MediaType.APPLICATION_JSON)
	public String  login(@QueryParam("username") String userName, @QueryParam("password")String password){
		if(password==null || userName==null){
			return JSONUtils.makeJSONString("result", false, "reason", "username or password is null");
		}
		
		try{
			SecurityService securityService=BizContext.getBean("securityService",SecurityService.class);
			boolean result=securityService.login(userName,password);
			return JSONUtils.makeJSONString("result", result);
		}catch(IhnSecurityException ex){
			return JSONUtils.makeJSONString("result", false, "code", ex.getErrorCode(), "reason", ex.getMessage());
		}catch(Throwable th){
			return JSONUtils.makeJSONString("result", false, "reason", th.getMessage());
		}
	}
	
	@GET
	@Path("properties")
	@Produces(MediaType.APPLICATION_JSON)
	public String getManagedProperties(@QueryParam("username") String userName){
		if(userName==null){
			return JSONUtils.makeJSONString("result", false, "reason", "username is null");
		}
	
		try{
			SecurityService securityService=BizContext.getBean("securityService",SecurityService.class);
			Set<String>  scopes=securityService.getManagedProperties(userName);
			return JSONUtils.makeJSONString("result", true, "properties", scopes);
		}catch(IhnSecurityException ex){
			return JSONUtils.makeJSONString("result", false, "code", ex.getErrorCode(), "reason", ex.getMessage());
		}catch(Throwable th){
			return JSONUtils.makeJSONString("result", false, "reason", th.getMessage());
		}
	}
	
}
