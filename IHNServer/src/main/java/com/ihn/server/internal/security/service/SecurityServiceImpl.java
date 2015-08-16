package com.ihn.server.internal.security.service;

import java.util.Set;

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

	@Override
	public String getManagedProperties(String userName) {
		if(userName==null){
			return "{'result':'false','reason':'username is null'}";
		}
		
		UserDao userDao=BizContext.getBean("userDao",UserDao.class);
		User user = userDao.getByKey(userName);
		Set<String> scopes = user.getScopes();
		if(scopes==null || scopes.size()==0){
			return "{'result':'false','reason':'no managed property'}";
		}else{
			StringBuilder sb=new StringBuilder();
			sb.append("{'result':'true','properties':[");
			int i=0;
			for(String scope : scopes){
				if(i==0){
					i++;
				}else{
					sb.append(",");
				}
				sb.append("'").append(scope).append("'");
			}
			sb.append("]");
			sb.append("}");
			return sb.toString();
		}
	}
	
}
