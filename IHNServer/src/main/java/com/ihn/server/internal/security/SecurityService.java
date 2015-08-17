package com.ihn.server.internal.security;

import java.util.Set;



public interface SecurityService {
	
	/** login with username and password */
	public boolean login(String userName, String password);
	
	public Set<String> getManagedProperties(String userName);

}
