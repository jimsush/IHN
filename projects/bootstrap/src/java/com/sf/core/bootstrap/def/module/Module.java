package com.sf.core.bootstrap.def.module;

import java.util.Map;

import com.sf.base.log.def.Logger;


/**
 * <p>
 * Title: Module
 * </p>
 * <p>
 * Description: 模块
 * </p>
 * 
 * modified [who date description]
 * check [who date description]
 */
public interface Module extends ModuleConstants{

    /**
     * 获取模块名
     * @return 模块名
     */
    public String getModuleName();
    
    /**
     * 获取模块当前状态 
     * @return 模块状态
     */
    public String getModuleStatus();

    /**
     * 获取模块所有参数信息
     * @return 模块参数信息
     */
    public Map<String, String> getModuleParams();
    
    /**
     * 获取模块的Logger
     * @return 模块Logger
     */
    public Logger getLogger();
    /**
     * 获取模块运行路径
     * @return
     */
    public String getModulePath();
    
}
