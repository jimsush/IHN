/*
 * $Id: ModuleFileLoader.java, 2015-9-20 ����04:45:17 sufeng Exp $
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
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.FileUtils;

/**
 * <p>
 * Title: ModuleFileLoader
 * </p>
 * <p>
 * Description: �������ļ��ж�ȡ����ģ���jar,conf���ļ�
 * </p>
 * 
 * @author sufeng
 * created 2015-9-20 ����04:45:17
 * modified [who date description]
 * check [who date description]
 */
public class ModuleFileLoader {
    
    /**
     * ����ģ���µ�jar,conf
     * @param homeDir
     * @param moduleName
     * @return ����jar,confĿ¼�µ��ļ���url
     */
    @SuppressWarnings("unchecked")
    public List<URL> getJarAndConf(String homeDir,String moduleName){
        List<URL>  urls=new ArrayList<URL>();
        
        // ��lib�¶�ȡjar�����ļ����ŵ�classpath��
        String dir=homeDir+"/modules/"+moduleName+"/lib";
        Collection files = null;
        try{
            files=FileUtils.listFiles(new File(dir), new String[]{"jar"}, true);
        }catch(Exception ex){
            files=null;
            System.out.println("can't find any jar file,lib dir="+dir);
        }
        if(CollectionUtils.isNotEmpty(files)){
            for(Object file : files){
                try {
                    URL url=new URL("file:/"+file.toString());
                    urls.add(url);
                } catch (Exception e) {
                    throw new RuntimeException("jar file to url failed");
                }
            }
        }
        
        // ��conf�¶�ȡxml�����ļ����ŵ�classpath��
        dir=homeDir+"/modules/"+moduleName+"/conf";
        try{
            files = FileUtils.listFiles(new File(dir), new String[]{"xml"}, false);
        }catch(Exception ex){
            files=null;
            //System.out.println("can't find any xml file,lib dir="+dir);
        }
        
        if(CollectionUtils.isNotEmpty(files)){
            for(Object res : files){
                try {
                    URL url=new URL("file:/"+res.toString());
                    urls.add(url);
                } catch (Exception e) {
                    throw new RuntimeException("conf file to url failed");
                }
            }
        }
        return urls;
    }

    /**
     * �õ�ָ����ģ��Ĺ�ͬjar(modules/lib/dir/*.jar)
     * @param homeDir
     * @param jarNames ָ����jar���ƻ���Ŀ¼������spring/spring.jar��quartz��Ŀ¼����
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<URL> getPublicJars(String homeDir,List<String> jarNames){
        List<URL>  urls=new ArrayList<URL>();
        String publicJarDir=homeDir+"/modules/lib"; 
        for(String jarName : jarNames){
            if(jarName.endsWith(".jar")){
                // is file
                File jarFile=new File(publicJarDir+"/"+jarName);
                try {
                    URL url=new URL("file:/"+jarFile.toString());
                    urls.add(url);
                } catch (Exception e) {
                    throw new RuntimeException("public jar file to url failed");
                }
            }else{
                // is dir
                Collection jarFilesInDir = null;
                try{
                    jarFilesInDir = FileUtils.listFiles(new File(publicJarDir+"/"+jarName), new String[]{"jar"}, true);
                }catch(Exception ex){
                    jarFilesInDir=null;
                    System.out.println("can't find any public jar file in dir "+publicJarDir+"/"+jarName);
                }
                if(CollectionUtils.isNotEmpty(jarFilesInDir)){
                    for(Object res : jarFilesInDir){
                        try {
                            URL url=new URL("file:/"+res.toString());
                            urls.add(url);
                        } catch (Exception e) {
                            throw new RuntimeException("public jar file to url failed");
                        }
                    }
                }
            }
        }
        return urls;
    }

}
