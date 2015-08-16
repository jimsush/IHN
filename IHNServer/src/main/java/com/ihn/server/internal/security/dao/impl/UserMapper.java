package com.ihn.server.internal.security.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Set;

import org.springframework.jdbc.core.RowMapper;

import com.ihn.server.internal.security.model.User;
import com.ihn.server.util.SysUtils;

public class UserMapper implements RowMapper {

	@Override
	public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
		User user=new User();
		user.setUserName(rs.getString("username"));
		user.setPassword(rs.getString("password"));
		user.setRole(rs.getString("role"));
		user.setCorp(rs.getString("corp"));
		
		Set<String> scopeSet = SysUtils.string2Set(rs.getString("scopes"), ",");
		user.setScopes(scopeSet);
		return user;
	}

}
