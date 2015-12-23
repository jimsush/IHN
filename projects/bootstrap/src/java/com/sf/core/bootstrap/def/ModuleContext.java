/*
 * $Id: ModuleContext.java, 2015-2-25 上午10:07:02 sufeng Exp $
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
 * Description: 模块管理器
 * </p>
 * 
 * @author sufeng
 * created 2015-2-25 上午10:07:02
 * modified [who date description]
 * check [who date description]
 */
public abstract class ModuleContext {

    /**
     * 所有系统参数
     * @return
     */
    public abstract Map<String, String> getSystemParams();

    /**
     * 获取一个系统参数的值
     * @param configKey 参数key
     * @return
     */
    public abstract String getSystemParam(String configKey);

    /**
     * 获取一个module
     * @param <T> 模块类
     * @param moduleClass 模块类
     * @return
     */
    public abstract <T extends Module> T getModule(Class<T> moduleClass);
    
    /**
     * 获取core模块
     * @return
     */
    public abstract Module getCoreModule();
    
    /**
     * 设置core模块
     * @param coreModule 内核模块
     * @return
     */
    public abstract void setCoreModule(Module coreModule);

    /**
     * 获取所有module
     * @return
     */
    public abstract Map<Class<? extends Module>, Module> getModules();

    /**
     * 模块优先级管理器
     * @return
     */
    public abstract ModulePriority getModulePriority();
    
    /**
     * 设置容器使用的Logger，可以由core模块来设置
     * @param logger 日志
     */
    public abstract void setContainerLogger(Logger logger);

    /**
     * 关闭所有模块，清理资源
     */
    public abstract void cleanup();
    
    /**
     * 获取模块管理器
     * @return
     */
    public static ModuleContext getInstance() {return instance;}
    protected static ModuleContext instance = null;

}
