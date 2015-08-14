package com.ihn.server.internal.security.service;

import com.ihn.server.internal.launch.BizContext;
import com.ihn.server.internal.security.SecurityService;
import com.ihn.server.internal.security.dao.UserDao;
import com.ihn.server.internal.security.model.User;

public class SecurityServiceImpl implements SecurityService{

	@Override
	public String  login(String userName, String password){
		if(password==null || userName==null){
			return "{'result':'false','reason':'username or password is null'}";
		}
		
		UserDao userDao=BizContext.getBean("userDao",UserDao.class);
		User user = userDao.getByKey(userName);
		if(user==null){
			return "{'result':'false','reason':'user is not existed'}";
		}else{
			if(password.equals(user.getPassword())){
				return "{'result':'true'}";
			}else{
				return "{'result':'false','reason':'password is incorrect'}";
			}
		}
	}
	
}
