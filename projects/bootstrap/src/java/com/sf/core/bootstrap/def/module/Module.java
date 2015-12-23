package com.sf.core.bootstrap.def.module;

import java.util.Map;

import com.sf.base.log.def.Logger;


/**
 * <p>
 * Title: Module
 * </p>
 * <p>
 * Description: ģ��
 * </p>
 * 
 * modified [who date description]
 * check [who date description]
 */
public interface Module extends ModuleConstants{

    /**
     * ��ȡģ����
     * @return ģ����
     */
    public String getModuleName();
    
    /**
     * ��ȡģ�鵱ǰ״̬ 
     * @return ģ��״̬
     */
    public String getModuleStatus();

    /**
     * ��ȡģ�����в�����Ϣ
     * @return ģ�������Ϣ
     */
    public Map<String, String> getModuleParams();
    
    /**
     * ��ȡģ���Logger
     * @return ģ��Logger
     */
    public Logger getLogger();
    /**
     * ��ȡģ������·��
     * @return
     */
    public String getModulePath();
    
}
