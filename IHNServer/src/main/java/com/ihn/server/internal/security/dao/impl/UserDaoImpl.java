package com.ihn.server.internal.security.dao.impl;

import org.springframework.jdbc.core.JdbcTemplate;

import com.ihn.server.internal.security.dao.UserDao;
import com.ihn.server.internal.security.model.User;

public class UserDaoImpl implements UserDao{

	private JdbcTemplate jdbcTemplate;
	
	@Override
	public User getUser(String userName) {
		User user = (User) jdbcTemplate.queryForObject("select * from user where userName='"+userName+"'",User.class);
		return user;
	}

	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	
}
