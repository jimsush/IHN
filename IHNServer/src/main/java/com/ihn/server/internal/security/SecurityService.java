package com.ihn.server.internal.security;



public interface SecurityService {
	
	/** login with username and password */
	public String  login(String userName, String password);
	
	public String getManagedProperties(String userName);

}
