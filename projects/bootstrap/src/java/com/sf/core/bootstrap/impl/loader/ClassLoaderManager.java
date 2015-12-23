/*
 * $Id: ClassLoaderManager.java, 2015-12-13 ����08:56:22 sufeng Exp $
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
 * Description: ��������ع�����
 * </p>
 * 
 * @author sufeng
 * created 2015-12-13 ����08:56:22
 * modified [who date description]
 * check [who date description]
 */
public class ClassLoaderManager {
    
    /**
     * ����jar��url
     */
    private List<URL> containerURLs=new ArrayList<URL>();
    
    /**
     * ��module jar url
     */
    private Map<String,List<URL>> moduleURLs=new HashMap<String, List<URL>>();
    
    /**
     * ����classloader
     */
    private ContainerClassLoader containerClassLoader;
    
    /**
     * ģ��classloader
     */
    private Map<String,ModuleClassLoader> moduleClassLoaders=new ConcurrentHashMap<String, ModuleClassLoader>();
    
    /**
     * ��������class path����URL
     * @param urls
     */
    public void addContainerURLs(List<URL> urls){
        containerURLs.addAll(urls);
    }
    
    /**
     * �趨һ��coreģ��
     * @param coreModuleName
     */
    public void setCoreModuleName(String coreModuleName) {
        containerClassLoader.setCoreModuleName(coreModuleName);
    }
    
    /**
     * ��module��class path����URL
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
     * lib�µĹ���jar
     */
    private Set<String> publicJarUrls=new HashSet<String>();
    
    /**
     * ��module��class path���빫��jar��modules/lib/dir/*.jar����URL
     * @param moduleName
     * @param urls
     */
    public void addModulePublicURLs(String moduleName,List<URL> urls){
        List<URL> list = moduleURLs.get(moduleName);
        if(list==null){
            list=new ArrayList<URL>();
            moduleURLs.put(moduleName, list);
        }
        
        // ����Ƿ�������ģ���ظ������ˣ�����Ѿ����أ�����Ҫ����
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
     * ��ʼ��������class loader
     */
    public void initContainerLoader(){
        ClassLoader parent = getClass().getClassLoader();
        containerClassLoader = new ContainerClassLoader(containerURLs.toArray(new URL[0]), parent);
        Thread.currentThread().setContextClassLoader(containerClassLoader);
    }
    
    /**
     * ������class loader
     * @return
     */
    public ContainerClassLoader getContainerClassLoader() {
        return containerClassLoader;
    }
    
    /**
     * ����module��class loader
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
     * ��ȡģ���class loader
     * @param moduleName
     * @return
     */
    public ClassLoader getClassLoaderByModule(String moduleName){
        ModuleClassLoader moduleClassLoader=moduleClassLoaders.get(moduleName);
        return (moduleClassLoader==null ? containerClassLoader : moduleClassLoader);
    }
    
}
