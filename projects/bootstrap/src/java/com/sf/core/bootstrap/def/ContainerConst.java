/*
 * $Id: ContainerConst.java, 2015-2-25 ����10:42:43 sufeng Exp $
 * 
 *  
 * All rights reserved.
 * 
 * This software is copyrighted and owned by SF or the copyright holder
 * specified, unless otherwise noted, and may not be reproduced or distributed
 * in whole or in part in any form or medium without express written permission.
 */
package com.sf.core.bootstrap.def;

/**
 * <p>
 * Title: ContainerConst
 * </p>
 * <p>
 * Description: bootstrap�õ��ĳ����͹��߷���
 * </p>
 * 
 * @author sufeng
 * created 2015-2-25 ����10:42:43
 * modified [who date description]
 * check [who date description]
 */
public class ContainerConst {
    
    /**
     * ��ϵͳ��Ŀ¼
     */
    public static final String KEY_HOME_DIR="homeDir";
    
    /**
     * param key:����logback�ļ���·��,��valueһ������Ϊconf/logback.xml����modules/xxcore/Ϊ��
     */
    public static final String PARAM_KEY_LOGGER_LOCATION="loggerConfigLocation";
    
    /**
     * param key:����logback�ļ���·��,��valueһ������Ϊconf/logback.xml����modules/xxcore/Ϊ��
     */
    public static final String PARAM_KEY_DEFAULT_LOGGER="defaultLoggerName";
    
    /**
     * �˳�ϵͳ
     * @param status
     */
    public static void exitSystem(int status){
        System.out.println("subsystem exit.");
        System.exit(status);
    }

}
