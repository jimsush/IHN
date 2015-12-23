/*
 * $Id: ContainerClassLoader.java, 2015-12-10 下午05:24:34 sufeng Exp $
 * 
 *  
 * All rights reserved.
 * 
 * This software is copyrighted and owned by SF or the copyright holder
 * specified, unless otherwise noted, and may not be reproduced or distributed
 * in whole or in part in any form or medium without express written permission.
 */
package com.sf.core.bootstrap.impl.loader;

import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.sf.core.bootstrap.impl.BootstrapUtils;
import com.sf.core.bootstrap.impl.module.DoubleLinkModuleNode;
import com.sf.core.bootstrap.impl.module.ModuleDependenceAnalysis;

/**
 * <p>
 * Title: ContainerClassLoader
 * </p>
 * <p>
 * Description:容器的Class Loader，先容器，然后各个Module的ModuleClassLoader
 * </p>
 * 
 * @author sufeng
 * created 2015-12-10 下午05:24:34
 * modified [who date description]
 * check [who date description]
 */
public class ContainerClassLoader extends URLClassLoader {

    /**
     * 模块依赖关系分析器
     */
    private ModuleDependenceAnalysis dependenceAnalysis;
    
    private String coreModuleName;
    
    /**
     * 每个模块的类加载
     */
    private Map<String,ModuleClassLoader> moduleClassLoaders=new ConcurrentHashMap<String, ModuleClassLoader>();

    public ContainerClassLoader(URL[] urls, ClassLoader parent) {
        super(urls, parent);
    }
    
    public void addModuleClassLoader(String moduleName,ModuleClassLoader mcl){
        moduleClassLoaders.put(moduleName,mcl);
    }
    
    public void removeModuleClassLoader(String moduleName){
        moduleClassLoaders.remove(moduleName);
    }
    
    public void setCoreModuleName(String coreModuleName) {
        this.coreModuleName = coreModuleName;
    }
    
    /**
     * 设置模块依赖关系分析器
     * @param dependenceAnalysis
     */
    public void setDependenceAnalysis(ModuleDependenceAnalysis dependenceAnalysis) {
        this.dependenceAnalysis = dependenceAnalysis;
    }

    @Override
    public Class<?> loadClass(String name) throws ClassNotFoundException {
        return findAndLoadClass(name, true, null);
    }
    
    /**
     * 搜索并加载类(cache,parent class,system class, self class,module's class)
     * @param name              类名
     * @param findFromModule    是否从下面的模块中加载
     * @param ignoredModuleName 不需要加载的module(在module加载的时候,往往需要忽略自己),可能为null
     * @return
     * @throws ClassNotFoundException
     */
    public Class<?> findAndLoadClass(String name,boolean findFromModule,String ignoredModuleName) throws ClassNotFoundException {
        // 从cache,自己的classpath或者父loader,system loader中加载class
        ClassNotFoundException rootCauseEx=null;
        Class<?> c = null;
        try{
            c=super.loadClass(name);
        }catch(NoClassDefFoundError ex2){
            rootCauseEx=throwClassNotFound(name,ex2);
        }catch(ClassNotFoundException ex){
            rootCauseEx=ex;
        }
        if(c!=null)
            return c;
        
        // 从下面的module中加载class(如果module中的class有相同的，则先载入的有效)
        if(findFromModule){
            // 从该模块依赖的其他模块中加载文件
            if(ignoredModuleName!=null){
                Collection<String> dependentModules=getDependentModules(ignoredModuleName);
                for(String dependentModule : dependentModules){
                    ModuleClassLoader moduleClassLoader = moduleClassLoaders.get(dependentModule);
                    try{
                        c=moduleClassLoader.findAndLoadClass(name,false);
                    }catch(Exception ex){
                        // do nothing
                    }
                    if(c!=null) //如果有重复的,第一个有效
                        return c;
                }
            }
        }
        
        if(c==null && rootCauseEx!=null)
            throw rootCauseEx;
        
        return c;
    }
    
    private ClassNotFoundException throwClassNotFound(String name,NoClassDefFoundError ex2){
        ClassNotFoundException rootCauseEx;
        Throwable cause2 = ex2.getCause();
        if(cause2!=null){
            if(cause2.getClass().equals(ClassNotFoundException.class)){
                rootCauseEx=(ClassNotFoundException)cause2;
                if(!name.equals(rootCauseEx.getMessage()))//当前class继承的祖先没找到
                    BootstrapUtils.getLogger().warn("ClassNotFoundException:"+rootCauseEx.getMessage()+",refered by "+name);
            }else{
                throw ex2;
            }
        }else{
            throw ex2;
        }
        return rootCauseEx;
    }
   
    /**
     * 模块依赖的模块名列表
     * @param currentModule
     * @return
     */
    private Collection<String> getDependentModules(String currentModule){
        if(currentModule.equals(coreModuleName)){
            // core可以读取所有模块的类(client core必须这样，否则无法加载menu等资源)
            return dependenceAnalysis.getAllModuleNames();
        }else{
            // other module，只能委托给其依赖的祖先模块
            DoubleLinkModuleNode moduleDependence = dependenceAnalysis.getModule(currentModule); 
            return moduleDependence.getAllDependentModules(); //该模块间接、直接依赖的所有模块
        }
    }
    
    /**
     * 从currentModule的依赖module中搜索资源文件
     * @param resourceName
     * @param currentModule
     * @return
     */
    public URL findResourceFromDependModule(String resourceName,String currentModule){
        Collection<String> dependentModules=getDependentModules(currentModule);
        for(String dependentModule : dependentModules){
            ModuleClassLoader moduleClassLoader = moduleClassLoaders.get(dependentModule);
            URL url = moduleClassLoader.findResource2(resourceName,false);
            if(url!=null)
                return url;
        }
        return null;
    }
    
    /**
     * 从currentModule的依赖module中搜索资源文件
     * @param resourceName
     * @param currentModule
     * @return
     * @throws IOException
     */
    public Enumeration<URL> findResourcesFromDependModule(String resourceName,String currentModule) throws IOException {
        Collection<String> dependentModules=getDependentModules(currentModule);
        for(String dependentModule : dependentModules){
            ModuleClassLoader moduleClassLoader = moduleClassLoaders.get(dependentModule);
            Enumeration<URL> urls = moduleClassLoader.findResources2(resourceName,false);
            if(urls!=null && urls.hasMoreElements())
                return urls;
        }
        return null;
    }
    
    @Override
    public String toString() {
        return "ContainerClassLoader";
    }
    
}
