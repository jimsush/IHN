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
