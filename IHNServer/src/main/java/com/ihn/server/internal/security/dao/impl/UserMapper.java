package com.ihn.server.internal.security.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

import org.springframework.jdbc.core.RowMapper;

import com.ihn.server.internal.security.model.User;

public class UserMapper implements RowMapper {

	@Override
	public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
		User user=new User();
		user.setUserName(rs.getString("username"));
		user.setPassword(rs.getString("password"));
		Set<String> roleSet=new HashSet<String>();
		user.setRoles(roleSet);
		
		String role = rs.getString("role");
		if(role!=null && role.length()>0){
			String[] roles = role.split(",");
			if(roles!=null){
				for(String r : roles){
					roleSet.add(r);
				}
			}
		}
		return user;
	}

}
