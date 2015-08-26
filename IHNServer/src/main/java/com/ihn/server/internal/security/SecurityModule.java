package com.ihn.server.internal.security;

import java.util.Map;

import com.ihn.server.internal.launch.BizContext;
import com.ihn.server.internal.modules.CommonModule;
import com.ihn.server.internal.security.dao.UserDao;

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
		BizContext.getBean("userDao",UserDao.class).initCode();
	}

	@Override
	public void stop() {
		
	}

	@Override
	public void destroy() {
		
	}

}
