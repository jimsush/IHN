/*
 * $Id: ListStringHandler.java, 2015-11-10 ����01:09:51  Exp $
 * 
 * 
 * All rights reserved.
 * 
 * This software is copyrighted and owned by SF or the copyright holder
 * specified, unless otherwise noted, and may not be reproduced or distributed
 * in whole or in part in any form or medium without express written permission.
 */
package com.sf.base.xml.handler;

import java.util.ArrayList;
import java.util.List;

import org.exolab.castor.mapping.GeneralizedFieldHandler;

/**
 * 
 * <p>
 * Title: ListStringHandler
 * </p>
 * <p>
 * Description:
 * castorXML�ļ������У�ListStringHandler��aaa,bbb,ccc,dddd����ת��ΪList��ţ�
 * �෴��List��������aaa,bbb,ccc,dddd����ʽ��ŵ�XML�ļ�
 * </p>
 * 
 * @author 
 * created 2015-11-10 ����01:09:51
 * modified [who date description]
 * check [who date description]
 */
public class ListStringHandler extends GeneralizedFieldHandler {

	/* (non-Javadoc)
	 * @see org.exolab.castor.mapping.GeneralizedFieldHandler#convertUponGet(java.lang.Object)
	 */
	@Override
	public Object convertUponGet(Object value) {
		return value;
	}

	/* (non-Javadoc)
	 * @see org.exolab.castor.mapping.GeneralizedFieldHandler#convertUponSet(java.lang.Object)
	 */
	@Override
	public Object convertUponSet(Object value) {
		String temp=value.toString();
		String[] array=temp.split(",");
		List<String> list=new ArrayList<String>();
		for(String s:array){
			list.add(s);
		}
		return list;
	}

	/* (non-Javadoc)
	 * @see org.exolab.castor.mapping.GeneralizedFieldHandler#getFieldType()
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Class getFieldType() {
		return List.class;
	}

}
