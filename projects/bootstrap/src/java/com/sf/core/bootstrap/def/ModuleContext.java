/*
 * $Id: ModuleContext.java, 2015-2-25 ����10:07:02 sufeng Exp $
 * 
 *  
 * All rights reserved.
 * 
 * This software is copyrighted and owned by SF or the copyright holder
 * specified, unless otherwise noted, and may not be reproduced or distributed
 * in whole or in part in any form or medium without express written permission.
 */
package com.sf.core.bootstrap.def;

import java.util.Map;

import com.sf.base.log.def.Logger;
import com.sf.core.bootstrap.def.module.Module;
import com.sf.core.bootstrap.def.module.ModulePriority;


/**
 * <p>
 * Title: ModuleContext
 * </p>
 * <p>
 * Description: ģ�������
 * </p>
 * 
 * @author sufeng
 * created 2015-2-25 ����10:07:02
 * modified [who date description]
 * check [who date description]
 */
public abstract class ModuleContext {

    /**
     * ����ϵͳ����
     * @return
     */
    public abstract Map<String, String> getSystemParams();

    /**
     * ��ȡһ��ϵͳ������ֵ
     * @param configKey ����key
     * @return
     */
    public abstract String getSystemParam(String configKey);

    /**
     * ��ȡһ��module
     * @param <T> ģ����
     * @param moduleClass ģ����
     * @return
     */
    public abstract <T extends Module> T getModule(Class<T> moduleClass);
    
    /**
     * ��ȡcoreģ��
     * @return
     */
    public abstract Module getCoreModule();
    
    /**
     * ����coreģ��
     * @param coreModule �ں�ģ��
     * @return
     */
    public abstract void setCoreModule(Module coreModule);

    /**
     * ��ȡ����module
     * @return
     */
    public abstract Map<Class<? extends Module>, Module> getModules();

    /**
     * ģ�����ȼ�������
     * @return
     */
    public abstract ModulePriority getModulePriority();
    
    /**
     * ��������ʹ�õ�Logger��������coreģ��������
     * @param logger ��־
     */
    public abstract void setContainerLogger(Logger logger);

    /**
     * �ر�����ģ�飬������Դ
     */
    public abstract void cleanup();
    
    /**
     * ��ȡģ�������
     * @return
     */
    public static ModuleContext getInstance() {return instance;}
    protected static ModuleContext instance = null;

}
