/*
 * $Id: SchException.java, 2015-2-18 上午11:01:15  Exp $
 * 
 * 
 * All rights reserved.
 * 
 * This software is copyrighted and owned by SF or the copyright holder
 * specified, unless otherwise noted, and may not be reproduced or distributed
 * in whole or in part in any form or medium without express written permission.
 */
package com.sf.base.scheduler;

import com.sf.base.exception.SfException;


/**
 * <p>
 * Title: SchException
 * </p>
 * <p>
 * Description: 
 * <br>任务调度模块异常定义
 * </p>
 * 
 * @author 
 * created 2015-2-18 上午11:01:15
 * modified [who date description]
 * check [who date description]
 */
final public class SchException extends SfException {
    
    private static final long serialVersionUID = 2498844824074679236L;

    /** 初始化调度任务管理器 */
    public static final int INIT_SCHEDULER_JOB = 7000;
    /** 启动调度任务 */
    public static final int START_SCHEDULER_JOB = 7001;
    /** 关闭调度任务 */
    public static final int SHUTDOWN_SCHEDULER_JOB = 7002;
    /** 暂停调度任务 */
    public static final int PAUSE_SCHEDULER_JOB = 7003;
    /** 恢复调度任务 */
    public static final int RESUME_SCHEDULER_JOB = 7004;
    /** 重设调度任务 */
    public static final int RESET_SCHEDULER_JOB = 7005;
    /** general错误 */
    public static final int COMMON_SCHEDULER_JOB=7006;

    public SchException(int errorCode, String... source) {
        super(errorCode, source);
    }

    public SchException(int errorCode, Throwable th, String... source) {
        super(errorCode, th, source);
    }
    
    public SchException(Throwable th) {
        super(th);
    }

}
