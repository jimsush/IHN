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
		UserDao userDao=BizContext.getBean("userDao", UserDao.class);
		List<User> users = userDao.getAll();
		User user = userDao.getByKey("su");
		if(user!=null){
			user.setPassword("password");
			userDao.update(user);
			userDao.delete("su");
		}else{
			user=new User();
			user.setUserName("su");
			user.setPassword("password");
			user.setRoles(SysUtils.array2Set(new Object[]{"admin","viewer"}));
			userDao.insert(user);
		}
		
		System.out.println(user);
	}

	@Override
	public void stop() {
		
	}

	@Override
	public void destroy() {
		
	}

}
