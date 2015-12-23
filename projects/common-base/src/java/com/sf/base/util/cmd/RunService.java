/*
 * $Id: RunService.java, 2015-9-17 下午03:40:51 sufeng Exp $
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
 * Description: 启动一个外部服务
 * </p>
 * 
 * @author sufeng
 * created 2015-9-17 下午03:43:10
 * modified [who date description]
 * check [who date description]
 */
public interface RunService {
	/**
	 * 启动service
	 */
	public boolean startService();
	
	/**
	 * 停止service
	 */
	public boolean stopService();
	
	/**
	 * 初始化service的配置
	 */
	public boolean initService();
}
