/*
 * $Id: ContainerClassLoader.java, 2015-12-10 ����05:24:34 sufeng Exp $
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
 * Description:������Class Loader����������Ȼ�����Module��ModuleClassLoader
 * </p>
 * 
 * @author sufeng
 * created 2015-12-10 ����05:24:34
 * modified [who date description]
 * check [who date description]
 */
public class ContainerClassLoader extends URLClassLoader {

    /**
     * ģ��������ϵ������
     */
    private ModuleDependenceAnalysis dependenceAnalysis;
    
    private String coreModuleName;
    
    /**
     * ÿ��ģ��������
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
     * ����ģ��������ϵ������
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
     * ������������(cache,parent class,system class, self class,module's class)
     * @param name              ����
     * @param findFromModule    �Ƿ�������ģ���м���
     * @param ignoredModuleName ����Ҫ���ص�module(��module���ص�ʱ��,������Ҫ�����Լ�),����Ϊnull
     * @return
     * @throws ClassNotFoundException
     */
    public Class<?> findAndLoadClass(String name,boolean findFromModule,String ignoredModuleName) throws ClassNotFoundException {
        // ��cache,�Լ���classpath���߸�loader,system loader�м���class
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
        
        // �������module�м���class(���module�е�class����ͬ�ģ������������Ч)
        if(findFromModule){
            // �Ӹ�ģ������������ģ���м����ļ�
            if(ignoredModuleName!=null){
                Collection<String> dependentModules=getDependentModules(ignoredModuleName);
                for(String dependentModule : dependentModules){
                    ModuleClassLoader moduleClassLoader = moduleClassLoaders.get(dependentModule);
                    try{
                        c=moduleClassLoader.findAndLoadClass(name,false);
                    }catch(Exception ex){
                        // do nothing
                    }
                    if(c!=null) //������ظ���,��һ����Ч
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
                if(!name.equals(rootCauseEx.getMessage()))//��ǰclass�̳е�����û�ҵ�
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
     * ģ��������ģ�����б�
     * @param currentModule
     * @return
     */
    private Collection<String> getDependentModules(String currentModule){
        if(currentModule.equals(coreModuleName)){
            // core���Զ�ȡ����ģ�����(client core���������������޷�����menu����Դ)
            return dependenceAnalysis.getAllModuleNames();
        }else{
            // other module��ֻ��ί�и�������������ģ��
            DoubleLinkModuleNode moduleDependence = dependenceAnalysis.getModule(currentModule); 
            return moduleDependence.getAllDependentModules(); //��ģ���ӡ�ֱ������������ģ��
        }
    }
    
    /**
     * ��currentModule������module��������Դ�ļ�
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
     * ��currentModule������module��������Դ�ļ�
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
