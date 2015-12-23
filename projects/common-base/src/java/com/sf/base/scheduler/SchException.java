/*
 * $Id: SchException.java, 2015-2-18 ����11:01:15  Exp $
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
 * <br>�������ģ���쳣����
 * </p>
 * 
 * @author 
 * created 2015-2-18 ����11:01:15
 * modified [who date description]
 * check [who date description]
 */
final public class SchException extends SfException {
    
    private static final long serialVersionUID = 2498844824074679236L;

    /** ��ʼ��������������� */
    public static final int INIT_SCHEDULER_JOB = 7000;
    /** ������������ */
    public static final int START_SCHEDULER_JOB = 7001;
    /** �رյ������� */
    public static final int SHUTDOWN_SCHEDULER_JOB = 7002;
    /** ��ͣ�������� */
    public static final int PAUSE_SCHEDULER_JOB = 7003;
    /** �ָ��������� */
    public static final int RESUME_SCHEDULER_JOB = 7004;
    /** ����������� */
    public static final int RESET_SCHEDULER_JOB = 7005;
    /** general���� */
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
