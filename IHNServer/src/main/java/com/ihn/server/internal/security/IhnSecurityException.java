package com.ihn.server.internal.security;

import java.util.HashMap;
import java.util.Map;

import com.ihn.server.util.exception.IHNException;

public class IhnSecurityException extends IHNException{

	private static final long serialVersionUID = 1257308670309077596L;
	
	public static final int ID_USER_NOT_EXIST=1;
	public static final int ID_PASSWORD_INCORRECT=11;
	public static final int ID_NO_SCOPE=21;
	
	private static Map<Integer, String> id2Info=new HashMap<Integer,String>();
	static {
		id2Info.put(ID_USER_NOT_EXIST,"user not exist");
		id2Info.put(ID_PASSWORD_INCORRECT,"incorrect password");
		id2Info.put(ID_NO_SCOPE, "no managed scope");
	}
	
	public IhnSecurityException(int errorCode, String user) {
		super(id2Info.get(errorCode), errorCode, user);
	}

}
