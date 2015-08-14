package com.ihn.server.internal.security;

import java.util.List;
import java.util.Map;

import com.ihn.server.internal.launch.BizContext;
import com.ihn.server.internal.modules.CommonModule;
import com.ihn.server.internal.security.dao.UserDao;
import com.ihn.server.internal.security.model.User;
import com.ihn.server.util.SysUtils;

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
