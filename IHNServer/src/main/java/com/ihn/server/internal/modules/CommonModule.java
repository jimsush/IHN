package com.ihn.server.internal.modules;

import java.util.Map;

/**
 * generic module
 * @author tong
 *
 */
public interface CommonModule {

	public String getModuleName();
	
	public void init(Map<String,Object> params);
	
	public void start();
	
	public void stop();
	
	public void destroy();
	
}
 