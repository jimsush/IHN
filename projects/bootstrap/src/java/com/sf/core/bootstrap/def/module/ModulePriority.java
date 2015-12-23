package com.sf.core.bootstrap.def.module;

import java.util.List;

/**
 * <p>
 * Title: ModulePriority
 * </p>
 * <p>
 * Description: 模块的优先级
 * </p>
 * 
 * @author sufeng
 * modified [who date description]
 * check [who date description]
 */
public interface ModulePriority {

    /**
     * 按启动顺序排列的module
     * @return
     */
    public List<Module> getStartOrder();
    
    /**
     * 关闭时或卸载时的顺序
     * @return
     */
    public List<Module> getShutdownOrder();

}
