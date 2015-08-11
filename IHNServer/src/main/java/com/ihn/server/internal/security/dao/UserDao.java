package com.ihn.server.internal.security.dao;

import com.ihn.server.internal.security.model.User;

public interface UserDao {
	
	public User getUser(String userName);

}
