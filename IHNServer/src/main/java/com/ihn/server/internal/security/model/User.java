package com.ihn.server.internal.security.model;

import java.util.Set;

import javax.xml.bind.annotation.XmlRootElement;

import com.ihn.server.util.SysUtils;

@XmlRootElement
public class User {
	
	/** super */
	public static String ROLE_ADMIN="admin";
	
	/** admin for particular property corp, able to read/write */
	public static String ROLE_CORP_ADMIN="corpadmin";
	
	/** admin for particular property, able to read/write */
	public static String ROLE_PROERTY_OPERATOR="operator";
	
	/** mobile user, able to read */
	public static String ROLE_END_USER="mobileuser";
	
	private String userName;
	private String password;
	
	/** role, each user has 1 role */
	private String role;
	
	/** property corp name */
	private String corp;
	
	/** property set the use can manage */
	private Set<String> scopes;

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

	public Set<String> getScopes() {
		return scopes;
	}

	public void setScopes(Set<String> scopes) {
		this.scopes = scopes;
	}
	
	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getCorp() {
		return corp;
	}

	public void setCorp(String corp) {
		this.corp = corp;
	}
	
	@Override
	public String toString(){
		return "username:"+this.userName+" password:"+this.password+
				" role:"+this.role+" corp:"+this.corp+" scope:"
				+SysUtils.set2String(this.scopes, ",");
	}

}
