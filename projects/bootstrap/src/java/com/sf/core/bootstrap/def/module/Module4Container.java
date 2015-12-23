package com.sf.core.bootstrap.def.module;

import com.sf.base.log.def.Logger;



/**
 * <p>
 * Title: Module4Container
 * </p>
 * <p>
 * Description: ��������ģ����ڲ�����ӿڣ�����¶���ϲ�Ӧ�ã�
 * </p>
 * 
 * @author sufeng
 * modified [who date description]
 * check [who date description]
 */
public interface Module4Container {

    /**
     * ����ģ����
     * @param name
     */
    public void setModuleName(String name);
 
    /***
     * ����ģ���״̬
     * @param moduleStatus
     */
    public void setModuleStatus(String moduleStatus);
    
    /**
     * �����������
     * @param classLoader
     */
    public void setClassLoader(ClassLoader classLoader);
    
    /**
     * ����module����־
     * @param logger
     */
    public void setLogger(Logger logger);

    /**
     * ģ���������ʼ��
     * @return
     */
    public void start();
 
    /**
     * ֹͣģ��
     * @return
     */
    public void stop();

    
}
