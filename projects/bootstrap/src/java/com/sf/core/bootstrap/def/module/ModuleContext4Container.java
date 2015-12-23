package com.sf.core.bootstrap.def.module;

import java.util.List;

/**
 * <p>
 * Title: CoreContext4Container
 * </p>
 * <p>
 * Description:模块管理器接口对容器的管理接口
 * </p>
 * 
 * @author sufeng
 * modified [who date description]
 * check [who date description]
 */
public interface ModuleContext4Container {

    /**
     * 初始化容器
     * @param homeDir 运行的根路径
     * @param productName 产品名
     * @return 模块名称,按先后顺序排列
     */
    public List<String> initContainer(String homeDir,String productName);

    /**
     * 从文件系统中加载模块（此时模块还不在容器中）
     * @param moduleName 模块名
     * @return
     */
    public Module loadModule(String moduleName);
    
    /**
     * 初始化module优先级管理器
     * @param modules 模块
     */
    public void initModulePriority(List<Module> modules);
    
    /**
     * 把模块放入到容器中
     * @param <T> 模块类型
     * @param moduleClass 模块类
     * @param module 模块实例
     */
    public <T extends Module> void putModule2Container(Class<T> moduleClass, Module module);
    
    /**
     * 启动模块
     * @param module 模块
     */
    public void startModule(Module module);

    /**
     * 停止模块
     * @param module 模块
     */
    public void stopModule(Module module);
    
    /**
     * 卸载模块
     * @param module 模块
     */
    public void unloadModule(Module module);

}
