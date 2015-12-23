/*
 * $Id: SchedulerExecutorFactoryImpl.java, 2015-11-10 ����09:37:31 Administrator Exp $
 * 
 *  
 * All rights reserved.
 * 
 * This software is copyrighted and owned by SF or the copyright holder
 * specified, unless otherwise noted, and may not be reproduced or distributed
 * in whole or in part in any form or medium without express written permission.
 */
package com.sf.base.scheduler;



/**
 * <p>
 * Title: SchedulerExecutorFactoryImpl
 * </p>
 * <p>
 * Description:
 * <br>���ȹ����������ӿ�<code>SchedulerExecutorFactory<code>ʵ����
 * </p>
 * 
 * @author 
 * created 2015-11-10 ����09:37:31
 * modified [who date description]
 * check [who date description]
 */
public class SchedulerExecutorFactoryImpl implements SchedulerExecutorFactory {
    
    @Override
    public SchedulerExecutor createSchedulerExecutor() {
        SchedulerExecutor schedulerExecutor= new DefaultSchedulerExecutor();
        schedulerExecutor.start();
        return schedulerExecutor;
    }


}
