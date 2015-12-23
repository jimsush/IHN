/*
 * $Id: ClassLoaderManager.java, 2015-12-13 上午08:56:22 sufeng Exp $
 * 
 *  
 * All rights reserved.
 * 
 * This software is copyrighted and owned by SF or the copyright holder
 * specified, unless otherwise noted, and may not be reproduced or distributed
 * in whole or in part in any form or medium without express written permission.
 */
package com.sf.core.bootstrap.impl.loader;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import com.sf.core.bootstrap.impl.BootstrapUtils;

/**
 * <p>
 * Title: ClassLoaderManager
 * </p>
 * <p>
 * Description: 容器类加载管理器
 * </p>
 * 
 * @author sufeng
 * created 2015-12-13 上午08:56:22
 * modified [who date description]
 * check [who date description]
 */
public class ClassLoaderManager {
    
    /**
     * 容器jar的url
     */
    private List<URL> containerURLs=new ArrayList<URL>();
    
    /**
     * 各module jar url
     */
    private Map<String,List<URL>> moduleURLs=new HashMap<String, List<URL>>();
    
    /**
     * 容器classloader
     */
    private ContainerClassLoader containerClassLoader;
    
    /**
     * 模块classloader
     */
    private Map<String,ModuleClassLoader> moduleClassLoaders=new ConcurrentHashMap<String, ModuleClassLoader>();
    
    /**
     * 往容器的class path加入URL
     * @param urls
     */
    public void addContainerURLs(List<URL> urls){
        containerURLs.addAll(urls);
    }
    
    /**
     * 设定一下core模块
     * @param coreModuleName
     */
    public void setCoreModuleName(String coreModuleName) {
        containerClassLoader.setCoreModuleName(coreModuleName);
    }
    
    /**
     * 往module的class path加入URL
     * @param moduleName
     * @param urls
     */
    public void addModuleURLs(String moduleName,List<URL> urls){
        List<URL> list = moduleURLs.get(moduleName);
        if(list==null){
            list=new ArrayList<URL>();
            moduleURLs.put(moduleName, list);
        }
        list.addAll(urls);
    }
    
    /**
     * lib下的公共jar
     */
    private Set<String> publicJarUrls=new HashSet<String>();
    
    /**
     * 往module的class path加入公共jar（modules/lib/dir/*.jar）的URL
     * @param moduleName
     * @param urls
     */
    public void addModulePublicURLs(String moduleName,List<URL> urls){
        List<URL> list = moduleURLs.get(moduleName);
        if(list==null){
            list=new ArrayList<URL>();
            moduleURLs.put(moduleName, list);
        }
        
        // 检查是否有其他模块重复加载了，如果已经加载，则不需要加载
        for(URL url : urls){
            String urlStr=url.toString();
            if(publicJarUrls.contains(urlStr)){
                BootstrapUtils.getLogger().info("["+moduleName+"] jar ["+url+"] duplicated,will ignore");
                continue;
            }else{
                list.add(url);
                publicJarUrls.add(urlStr);
            }
        }
    }
    
    /**
     * 初始化容器的class loader
     */
    public void initContainerLoader(){
        ClassLoader parent = getClass().getClassLoader();
        containerClassLoader = new ContainerClassLoader(containerURLs.toArray(new URL[0]), parent);
        Thread.currentThread().setContextClassLoader(containerClassLoader);
    }
    
    /**
     * 容器的class loader
     * @return
     */
    public ContainerClassLoader getContainerClassLoader() {
        return containerClassLoader;
    }
    
    /**
     * 创建module的class loader
     * @param moduleName
     */
    public ModuleClassLoader initModuleLoader(String moduleName){
        List<URL> list = moduleURLs.get(moduleName);
        if(list==null || list.size()==0)
            return null;
        ModuleClassLoader moduleClassLoader=new ModuleClassLoader(list.toArray(new URL[0]),containerClassLoader,moduleName);
        moduleClassLoaders.put(moduleName,moduleClassLoader);
        containerClassLoader.addModuleClassLoader(moduleName, moduleClassLoader);
        return moduleClassLoader;
    }
    
    /**
     * 获取模块的class loader
     * @param moduleName
     * @return
     */
    public ClassLoader getClassLoaderByModule(String moduleName){
        ModuleClassLoader moduleClassLoader=moduleClassLoaders.get(moduleName);
        return (moduleClassLoader==null ? containerClassLoader : moduleClassLoader);
    }
    
}
