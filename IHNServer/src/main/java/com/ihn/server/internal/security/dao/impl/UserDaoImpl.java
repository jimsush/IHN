package com.ihn.server.internal.security.dao.impl;

import java.sql.Types;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;

import com.ihn.server.internal.security.dao.UserDao;
import com.ihn.server.internal.security.model.User;
import com.ihn.server.util.SysUtils;

public class UserDaoImpl implements UserDao{

	private JdbcTemplate jdbcTemplate;
	private UserMapper userMapper=new UserMapper();
	
	@Override
	public User getByKey(String userName) {
		List objs = jdbcTemplate.query("select * from user where userName=?", new Object[]{userName}, new int[]{Types.VARCHAR}, this.userMapper);
		if(objs==null || objs.size()==0)
			return null;
		return (User)objs.get(0);
	}

	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	@Override
	public void insert(User user) {
		jdbcTemplate.update("insert into user(username, password, role) values(?,?,?)", 
				new Object[]{ user.getUserName(), user.getPassword(), SysUtils.set2String(user.getRoles(),",") }, 
				new int[]{Types.VARCHAR, Types.VARCHAR, Types.VARCHAR});
	}
	
	@Override
	public void update(User user) {
		jdbcTemplate.update("update user set password=?, role=? where username=?", 
				new Object[]{ user.getPassword(), SysUtils.set2String(user.getRoles(),","),  user.getUserName() },
				new int[]{Types.VARCHAR, Types.VARCHAR, Types.VARCHAR});
	}
	
	@Override
	public List<User> getAll(){
		List objs = jdbcTemplate.query("select * from user", this.userMapper);
		return objs;
	}

	@Override
	public void delete(String userName) {
		jdbcTemplate.update("delete from user where username=?", new Object[]{userName}, new int[]{Types.VARCHAR} );
	}

	
}
