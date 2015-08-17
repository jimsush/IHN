package com.ihn.server.internal.security.service;

import java.util.Set;

import com.ihn.server.internal.launch.BizContext;
import com.ihn.server.internal.security.IhnSecurityException;
import com.ihn.server.internal.security.SecurityService;
import com.ihn.server.internal.security.dao.UserDao;
import com.ihn.server.internal.security.model.User;

public class SecurityServiceImpl implements SecurityService{

	@Override
	public boolean login(String userName, String password){
		UserDao userDao=BizContext.getBean("userDao",UserDao.class);
		User user = userDao.getByKey(userName);
		if(user==null){
			throw new IhnSecurityException(IhnSecurityException.ID_USER_NOT_EXIST,userName);
		}else{
			if(!password.equals(user.getPassword())){
				throw new IhnSecurityException(IhnSecurityException.ID_PASSWORD_INCORRECT,userName);
			}
		}
		
		return true;
	}

	@Override
	public Set<String> getManagedProperties(String userName) {
		UserDao userDao=BizContext.getBean("userDao",UserDao.class);
		User user = userDao.getByKey(userName);
		Set<String> scopes = user.getScopes();
		if(scopes==null || scopes.size()==0){
			throw new IhnSecurityException(IhnSecurityException.ID_NO_SCOPE,userName);
		}
		
		return scopes;
	}
	
}
