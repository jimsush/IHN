/*
 * $Id: CoreContainer.java, 2015-2-25 ����10:11:41 sufeng Exp $
 * 
 *  
 * All rights reserved.
 * 
 * This software is copyrighted and owned by SF or the copyright holder
 * specified, unless otherwise noted, and may not be reproduced or distributed
 * in whole or in part in any form or medium without express written permission.
 */
package com.sf.core.bootstrap.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.sf.base.log.def.Logger;
import com.sf.base.log.def.SfLog;
import com.sf.core.bootstrap.def.ContainerConst;
import com.sf.core.bootstrap.def.ModuleContext;
import com.sf.core.bootstrap.def.module.Module;
import com.sf.core.bootstrap.def.module.Module4Container;
import com.sf.core.bootstrap.def.module.ModuleConstants;

/**
 * <p>
 * Title: CoreContainer
 * </p>
 * <p>
 * Description: ����������
 * </p>
 * 
 * @author sufeng
 * created 2015-2-25 ����10:11:41
 * modified [who date description]
 * check [who date description]
 */
public class CoreContainer {

    /**
     * ��Ʒ��
     */
    private String productName;

    /**
     * ��·��
     */
    private String homeDir;
    
    /**
     * ģ�������
     */
    private ModuleContextImpl moduelContext;
    
    public CoreContainer(String homeDir,String productName) {
        this.productName=productName;
        this.homeDir = homeDir;
    }
    
    /**
     * ��������
     */
    private void start() {
        // construct
        ModuleContextImpl.init();
        moduelContext=(ModuleContextImpl) ModuleContext.getInstance();
        
        // ��ʼ������
        List<String> moduleNames = moduelContext.initContainer(homeDir, productName);
        
        // ��ʼ����־
        String logbackConfigFileLocation=moduelContext.getSystemParam(ContainerConst.PARAM_KEY_LOGGER_LOCATION);
        if(StringUtils.isEmpty(logbackConfigFileLocation))
            throw new RuntimeException("[bootstrap] can not define system param "+ContainerConst.PARAM_KEY_LOGGER_LOCATION+" in "+productName+".xml");
        String fullPath=homeDir+File.separator+logbackConfigFileLocation;
        String defaultLogName=moduelContext.getSystemParam(ContainerConst.PARAM_KEY_DEFAULT_LOGGER);
        SfLog.initLogging(fullPath, defaultLogName);
        Logger defaultLogger = SfLog.getDefaultLogger();
        moduelContext.setContainerLogger(defaultLogger);
        
        // ���ظ���ģ��
        List<Module> modules=new ArrayList<Module>();
        Map<String,Module> tmpModules=new HashMap<String, Module>();
        for(String moduleName : moduleNames){
            Module module=moduelContext.loadModule(moduleName);
            modules.add(module);
            tmpModules.put(moduleName,module);
        }
        // ����ģ������ȼ�
        moduelContext.initModulePriority(modules);

        // ��ʼ������ģ��
        moduelContext.getLogger().info("module count:"+moduleNames.size());
        int moduleIndex=1;
        ClassLoader oldClsLoader = Thread.currentThread().getContextClassLoader();
        for(String moduleName : moduleNames){
            Module module = tmpModules.get(moduleName);
            Thread.currentThread().setContextClassLoader(module.getClass().getClassLoader());
            moduelContext.getLogger().info("start module "+" ["+moduleName+":"+moduleIndex+"]");
            try{
                moduelContext.startModule(module);
            }catch(Exception ex){
                // ����ģ��ʧ����
                moduelContext.getLogger().error("start module ["+moduleName+"] failed.",ex);
                if (module instanceof Module4Container) {
                    ((Module4Container)module).setModuleStatus(ModuleConstants.STATUS_FAILED);
                }
            }
            moduelContext.getLogger().info("start module "+" ["+moduleName+":"+moduleIndex+"] completed");
            moduleIndex++;
        }
        // �ָ�classloader
        Thread.currentThread().setContextClassLoader(oldClsLoader);
    
        // ���ô���
        waitForShutdown();
    }

    /**
     * ���̵߳ȴ���ֹ֪ͨͣ����
     */
    private void waitForShutdown() {
        moduelContext.getLogger().info("Core container start finished");
        
        new ProcessorShutdownHook();
        
        try {
            // �ȴ��˳�
            synchronized (moduelContext) {
                while(!moduelContext.isModuleCleaned()){
                    try {
                        moduelContext.wait();
                    } catch (InterruptedException e) {
                        moduelContext.getLogger().info("Core container wait break,will exit");
                    }
                }
            }
            // ����Ѿ�module clean������
            if(!moduelContext.isModuleCleaned())
                moduelContext.cleanup();
        } catch (Exception ex) {
            moduelContext.getLogger().info("[info] waitForShutdown failed," + ex.getClass().getSimpleName()+","+ex.getMessage());
        }

        ContainerConst.exitSystem(0);
    }
    
    /**
     * �������
     * @param args
     */
    public static void main(String[] args) {
        Map<String, String> argMap = parseArg(args);
        if(argMap.keySet().contains("h")){
            System.out.println("usage: CoreContainer [-p productname]");
            System.out.println("");
            System.out.println("Options:");
            System.out.println("-p ��Ʒ��,modules���ж�Ӧxml�����ļ�");
            System.out.println("");
            return;
        }
        // ��ȡ��������
        String[] dirAndProduct=getDirAndProductParam(argMap);
        CoreContainer coreContainer = new CoreContainer(dirAndProduct[0],dirAndProduct[1]);
        coreContainer.start();
    }
    
    /**
     * ��������������ŵ�map��
     * @param args
     * @return
     */
    private static Map<String,String> parseArg(String[] args){
        Map<String,String> map=new HashMap<String, String>();
        if(args==null || args.length==0)
            return map;
        
        String lastKey=null;
        for(int i=0; i<args.length; i++){
            if(args[i]==null || args[i].equals(""))
                continue;
            if(args[i].startsWith("-")){
                lastKey=args[i].substring(1);
            }else{
                if(lastKey!=null){
                    map.put(lastKey,args[i]);
                    lastKey=null;
                }else{
                    map.put(args[i],null);
                }
            }
        }
        if(lastKey!=null)
            map.put(lastKey, null);
        return map;
     }
    
    /**
     * ������������
     * @param argMap
     * @return
     */
    private static String[] getDirAndProductParam(Map<String, String> argMap){
        String[] params=new String[2];
        if(argMap==null)
            argMap=new HashMap<String, String>();
        
        
        String productName="yuep"; // default product name
        String tempProductName=argMap.get("product");
        if(tempProductName!=null){
            productName=tempProductName;
        }else{
            tempProductName= argMap.get("p");
            if(tempProductName!=null)
                productName=tempProductName;
        }
        
        String homeDir;
        String userDir = getUserDir();
        int pos = userDir.indexOf("bootstrap");
        if (pos != -1) {
            homeDir = userDir.substring(0, pos) + "yuep-build";
        } else {
            homeDir = userDir;
        }
        
        params[0]=homeDir;
        params[1]=productName;
        return params;
    }
    
    /**
     * �û���home·��
     * @return
     */
    private static String getUserDir(){
        String userDir = null;
        try{
            userDir=System.getProperty("user.dir");
        }catch(Exception ex){
            System.err.println("getUserDir ex:"+ex.getMessage());
        }
        return userDir;
    }
    
    
}
