package com.ihn.server.internal.security.dao;

import java.util.List;

import com.ihn.server.internal.security.model.User;

public interface UserDao {
	
	public void initCode();
	
	public List<User> getAll();
	
	public User getByKey(String userName);
	
	public void insert(User user);
	
	public void update(User user);
	
	public void delete(String userName);
	
}
