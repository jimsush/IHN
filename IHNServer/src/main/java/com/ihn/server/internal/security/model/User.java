package com.ihn.server.internal.security.model;

import java.util.Set;

import com.ihn.server.util.SysUtils;

public class User {
	
	private String userName;
	private String password;
	
	private Set<String> roles;

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Set<String> getRoles() {
		return roles;
	}

	public void setRoles(Set<String> roles) {
		this.roles = roles;
	}
	
	@Override
	public String toString(){
		return "username:"+this.userName+" password:"+this.password+" role="+SysUtils.set2String(this.roles, ",");
		
	}

}
