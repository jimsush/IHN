package com.sf.core.bootstrap.def.module;

import java.util.List;

/**
 * <p>
 * Title: ModulePriority
 * </p>
 * <p>
 * Description: ģ������ȼ�
 * </p>
 * 
 * @author sufeng
 * modified [who date description]
 * check [who date description]
 */
public interface ModulePriority {

    /**
     * ������˳�����е�module
     * @return
     */
    public List<Module> getStartOrder();
    
    /**
     * �ر�ʱ��ж��ʱ��˳��
     * @return
     */
    public List<Module> getShutdownOrder();

}
