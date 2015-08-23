package com.ihn.server.internal.security;

import java.util.Set;



public interface SecurityService {
	
	/** login with username and password */
	public boolean login(String userName, String password);
	
	/** get managed properties ID */
	public Set<String> getManagedProperties(String userName);

}
