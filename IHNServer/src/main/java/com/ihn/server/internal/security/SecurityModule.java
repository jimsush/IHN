package com.ihn.server.internal.security;

import java.util.Map;

import com.ihn.server.internal.launch.BizContext;
import com.ihn.server.internal.modules.CommonModule;

public class SecurityModule implements CommonModule{

	@Override
	public String getModuleName() {
		return "Security";
	}

	@Override
	public void init(Map<String, Object> params) {
		
	}

	@Override
	public void start() {
		BizContext.getLogger().info("start SecurityModule");
	}

	@Override
	public void stop() {
		
	}

	@Override
	public void destroy() {
		
	}

}
