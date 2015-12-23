/*
 * $Id: ModuleContextImpl.java, 2015-2-25 上午10:21:21 sufeng Exp $
 * 
 *  
 * All rights reserved.
 * 
 * This software is copyrighted and owned by SF or the copyright holder
 * specified, unless otherwise noted, and may not be reproduced or distributed
 * in whole or in part in any form or medium without express written permission.
 */
package com.sf.core.bootstrap.impl;

import java.lang.reflect.Constructor;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.sf.base.log.def.Logger;
import com.sf.base.log.def.SfLog;
import com.sf.core.bootstrap.def.ContainerConst;
import com.sf.core.bootstrap.def.ModuleContext;
import com.sf.core.bootstrap.def.ModuleContextAware;
import com.sf.core.bootstrap.def.module.Module;
import com.sf.core.bootstrap.def.module.Module4Container;
import com.sf.core.bootstrap.def.module.ModuleContext4Container;
import com.sf.core.bootstrap.def.module.ModulePriority;
import com.sf.core.bootstrap.impl.loader.ClassLoaderManager;
import com.sf.core.bootstrap.impl.loader.ContainerClassLoader;
import com.sf.core.bootstrap.impl.module.ModuleDependenceAnalysis;
import com.sf.core.bootstrap.impl.module.ModuleEntity;
import com.sf.core.bootstrap.impl.module.ModulePriorityImpl;

/**
 * <p>
 * Title: ModuleContextImpl
 * </p>
 * <p>
 * Description:模块管理器的实现类
 * </p>
 * 
 * @author sufeng
 * created 2015-2-25 上午10:21:21
 * modified [who date description]
 * check [who date description]
 */
public final class ModuleContextImpl extends ModuleContext implements ModuleContext4Container{

    /**
     * 配置解析器
     */
    private ConfigParser configParser;
    
    /**
     * 模块优先级
     */
    private ModulePriority modulePriority;
    
    /** 全局的系统参数,来自于xml*/
    private Map<String, String> systemParams = new ConcurrentHashMap<String, String>();

    /** 模块集 */
    private Map<Class<? extends Module>, Module> modules = new ConcurrentHashMap<Class<? extends Module>, Module>();
    
    /** 类加载器mgr */
    private ClassLoaderManager classLoaderManager=new ClassLoaderManager();

    /** 模块依赖关系分析器 */
    private ModuleDependenceAnalysis moduleDependenceAnalysis=new ModuleDependenceAnalysis();

    @Override
    public ModulePriority getModulePriority() {
        return modulePriority;
    }

    public ModuleDependenceAnalysis getModuleDependenceAnalysis() {
        return moduleDependenceAnalysis;
    }
    
    @Override
    public void initModulePriority(List<Module> modules){
        modulePriority = new ModulePriorityImpl(modules);
    }
    
    @Override
    public Map<String, String> getSystemParams() {
        return systemParams;
    }

    @Override
    public String getSystemParam(String configKey) {
        return systemParams.get(configKey);
    }

    @Override
    public <T extends Module> void putModule2Container(Class<T> moduleClass, Module module) {
        modules.put(moduleClass, module);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends Module> T getModule(Class<T> moduleClass) {
        return (T) modules.get(moduleClass);
    }

    @Override
    public Map<Class<? extends Module>, Module> getModules() {
        return modules;
    }
    
    @Override
    public List<String> initContainer(String homeDir, String productName) {
        configParser = new ConfigParser();
        configParser.setClassLoaderManager(classLoaderManager);
        
        // 解析配置文件
        configParser.init(homeDir,productName);
        // 加载jar的url
        configParser.loadJarAndConf(homeDir);
        
        // 处理classloader classpath
        classLoaderManager.initContainerLoader();

        systemParams.put(ContainerConst.KEY_HOME_DIR, homeDir);
        systemParams.putAll(configParser.getSystemParams());
        
        // 初始化模块的依赖顺序
        moduleDependenceAnalysis.initModuleDepenceMesh(configParser.getAllModules());
        ContainerClassLoader containerClassLoader = classLoaderManager.getContainerClassLoader();
        containerClassLoader.setDependenceAnalysis(moduleDependenceAnalysis);
        List<String> sortedModuleNames=moduleDependenceAnalysis.getAllModulesByOrder();
        return sortedModuleNames;
    }
    
    @Override
    public Module loadModule(String moduleName) {
        // 加载module
        try{
            ModuleEntity moduleEntity = configParser.getModule(moduleName);
            classLoaderManager.initModuleLoader(moduleName);

            ClassLoader clsLoader=classLoaderManager.getClassLoaderByModule(moduleName);
            Class<?> moduleClass = Class.forName(moduleEntity.getModuleClass(), false, clsLoader);
            Constructor<?> constructor = moduleClass.getConstructor();
            Object moduleObject = constructor.newInstance();
            if (moduleObject instanceof Module4Container) {
                Module4Container managedModule = (Module4Container) moduleObject;
                managedModule.setModuleName(moduleName);
                managedModule.setClassLoader(clsLoader);
                managedModule.setModuleStatus(Module.STATUS_LOADED);

                Module module=(Module)moduleObject;
                if (moduleEntity.getModuleParams() != null)
                    module.getModuleParams().putAll(moduleEntity.getModuleParams());
                
                if(module instanceof ModuleContextAware){
                    ((ModuleContextAware)module).setModuleContext(this);
                }
                
                // 把模块放到容器中
                putModule2Container(module.getClass(), module);
                return module;
            }
        }catch(Exception ex){
            String errorInfo="[error] "+ex.getClass().getSimpleName()+","+ex.getMessage();
            getLogger().info(errorInfo);
            throw new RuntimeException(ex);
        }
        return null;
    }
    
    @Override
    public void startModule(Module module) {
        Module4Container managedModule=(Module4Container)module;
        managedModule.setModuleStatus(Module.STATUS_STARTING);
        // 注入Logger
        Logger logger=SfLog.getLogger(module.getModuleName());
        managedModule.setLogger(logger);
        // 启动模块
        managedModule.start();
        managedModule.setModuleStatus(Module.STATUS_ACTIVE);
    }
    
    @Override
    public void stopModule(Module module) {
        if(module instanceof Module4Container){
            Module4Container module4Container=(Module4Container)module;
            module4Container.setModuleStatus(Module.STATUS_STOPPING);
            module4Container.stop();
            module4Container.setModuleStatus(Module.STATUS_DEACTIVE);
        }
    }

    @Override
    public void unloadModule(Module module) {
        if(!Module.STATUS_DEACTIVE.equals(module.getModuleStatus())){
            throw new RuntimeException("module is not in DEACTIVE status");
        }
        modules.remove(module.getClass());
    }
    
    public void setContainerLogger(Logger logger) {
        BootstrapUtils.setLogger(logger);
    }
    
    public Logger getLogger(){
        return BootstrapUtils.getLogger();
    }
    
    public static void init(){
        instance=new ModuleContextImpl();
    }
    
    /**
     * 核心模块的名称
     */
    private Module coreModule;
    
    @Override
    public Module getCoreModule() {
        return coreModule;
    }
    
    @Override
    public void setCoreModule(Module coreModule) {
        this.coreModule=coreModule;
        // 告诉classloader哪个模块是core
        classLoaderManager.setCoreModuleName(coreModule.getModuleName());
    }
    
    /**
     * 模块是否已clean
     */
    private boolean moduleCleaned=false;
    /**
     * clean模块的lock
     */
    private byte[] cleanMonitor=new byte[0];
    
    @Override
    public void cleanup(){
        synchronized (cleanMonitor) {
            if(moduleCleaned) //一定要有标志位,避免重复clean module
                return;
            getLogger().info("will stop all modules");
            List<Module> modules = modulePriority.getShutdownOrder();
            ClassLoader oldClsLoader = Thread.currentThread().getContextClassLoader();//ContainerClassLoader
            for (Module module : modules){
                getLogger().info("stop ["+module.getModuleName()+"]");
                Thread.currentThread().setContextClassLoader(module.getClass().getClassLoader());
                try{
                    stopModule(module);
                }catch(Exception ex){
                    getLogger().error("stop module ["+module.getModuleName()+"] failed.",ex);
                }
            }
            Thread.currentThread().setContextClassLoader(oldClsLoader);
            moduleCleaned=true;
        }
    }
    
    /**
     * 模块是否已经clean
     * @return
     */
    public boolean isModuleCleaned(){
        return moduleCleaned;
    }

    /**
     * 子系统退出
     */
    public static void notifyExit(){
        try {
            instance.cleanup();
        } catch (Exception ex) {
            instance.getCoreModule().getLogger().info("info ModuleContextImpl.directExit failed," + ex.getClass().getSimpleName()+","+ex.getMessage());
        }
        synchronized(instance) {
            instance.notifyAll();
        }
    }

}
