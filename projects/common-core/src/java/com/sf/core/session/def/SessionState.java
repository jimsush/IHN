/*
 * $Id: SessionState.java, 2015-2-20 下午02:05:08  Exp $
 * 
 *  
 * All rights reserved.
 * 
 * This software is copyrighted and owned by SF or the copyright holder
 * specified, unless otherwise noted, and may not be reproduced or distributed
 * in whole or in part in any form or medium without express written permission.
 */
package com.sf.core.session.def;

/**
 * <p>
 * Title: SessionState
 * <p>
 * 对会话状态的枚举描述, 平台通过<code>SessionState</code>定义了会话中的所有可能的状态.
 * <p>
 * 会话状态包括以下几种:<br>
 * 连接状态(Linup)
 * 活动状态(Active,连接并初始化完毕，正在工作状态)
 * 不活动状态(Deactive)
 * 失连状态(Linkdown):
 * 
 * @author 
 * created 2015-2-20 下午02:05:08
 * 
 * modified [who date description]
 * check [who date description]
 */
public enum SessionState {
   
    /** 连接状态 */
    Linkup,
    
    /**
     * 活跃的（必须通过子系统的认证后才算活跃）
     */
    Active,
    
    /**
     * 不活跃
     */
    Deactive,
    
    /** 去连接 */
    Linkdown;
    
}
