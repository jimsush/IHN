/*
 * $Id: RunService.java, 2015-9-17 ����03:40:51 sufeng Exp $
 * 
 * 
 * All rights reserved.
 * 
 * This software is copyrighted and owned by SF or the copyright holder
 * specified, unless otherwise noted, and may not be reproduced or distributed
 * in whole or in part in any form or medium without express written permission.
 */
package com.sf.base.util.cmd;

/**
 * <p>
 * Title: RunService
 * </p>
 * <p>
 * Description: ����һ���ⲿ����
 * </p>
 * 
 * @author sufeng
 * created 2015-9-17 ����03:43:10
 * modified [who date description]
 * check [who date description]
 */
public interface RunService {
	/**
	 * ����service
	 */
	public boolean startService();
	
	/**
	 * ֹͣservice
	 */
	public boolean stopService();
	
	/**
	 * ��ʼ��service������
	 */
	public boolean initService();
}
