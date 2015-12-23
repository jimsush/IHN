/*
 * $Id: ClassPathFileSearcherImpl.java, 2015-6-2 下午04:05:53  Exp $
 * 
 *  
 * All rights reserved.
 * 
 * This software is copyrighted and owned by SF or the copyright holder
 * specified, unless otherwise noted, and may not be reproduced or distributed
 * in whole or in part in any form or medium without express written permission.
 */
package com.sf.base.resource;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;

import com.sf.base.util.format.SfObjectUtils;

/**
 * <p>
 * Title: ClassPathFileSearcherImpl
 * </p>
 * <p>
 * Description:
 * 类路径文件搜索接口<code>ClassPathFileSearcher<code>实现类
 * </p>
 * 
 * @author 
 * created 2015-6-2 下午04:05:53
 * modified [who date description]
 * check [who date description]
 */

public class ClassPathFileSearcherImpl implements ClassPathFileSearcher {
    
    /**
     * 
     * @see com.sf.base.resource.ClassPathFileSearcher#search(java.lang.String)
     */
    @Override
    public List<String> search(String matchPattern) {
        List<String> results=new ArrayList<String>();
        List<URL> resources = searchResource(matchPattern);
        try{
            for (URL resource : resources) {
                String filePath = resource.getPath();
                if(filePath==null)
                    filePath=resource.toString();
                results.add(filePath);
            }
        }catch(Exception ex){
            throw new IllegalArgumentException(ex);
        }
        return results;
    }

    /**
     * 搜索资源
     * @see com.sf.base.resource.ClassPathFileSearcher#searchResource(java.lang.String)
     */
    @Override
    public List<URL> searchResource(String matchPattern) {
        return searchResource(matchPattern,null);
    }
    
    @Override
    public List<URL> searchResource(String matchPattern, ClassLoader classLoader) {
        ResourcePatternResolver resourceResolver=null;
        List<URL> results=new ArrayList<URL>();
        if(classLoader==null)
            resourceResolver=new PathMatchingResourcePatternResolver();
        else
            resourceResolver=new PathMatchingResourcePatternResolver(classLoader);
        Resource[] resources = null;
        try {
            resources = resourceResolver.getResources("classpath*:"+matchPattern);
            if(resources!=null){
                for(int i=0; i<resources.length; i++){
                    results.add(resources[i].getURL());
                }
            }
            return results;
        } catch (Exception ex) {       
            throw new IllegalArgumentException(ex);
        }
    }
    
    /**
     * 搜索资源内部实现
     * @param matchPattern
     * @param classLoader
     * @return
     */
    private List<Resource> searchResource2(String matchPattern, ClassLoader classLoader) {
        // 使用spring的工具类来搜索资源
        ResourcePatternResolver resourceResolver=null;
        List<Resource> results=new ArrayList<Resource>();
        if(classLoader==null)
            resourceResolver=new PathMatchingResourcePatternResolver();
        else
            resourceResolver=new PathMatchingResourcePatternResolver(classLoader);
        Resource[] resources = null;
        try {
            // classpath中搜索
            resources = resourceResolver.getResources("classpath*:"+matchPattern);
            if(resources!=null){
                results.addAll(SfObjectUtils.newArrayList(resources));
            }
            return results;
        } catch (Exception ex) {       
            throw new IllegalArgumentException(ex);
        }
    }
    
    @Override
    public List<String> searchClass(String matchPattern, ClassLoader classLoader) {
        List<Resource> ress = searchResource2(matchPattern,classLoader);
        List<String> clz=new ArrayList<String>();
        if(ress==null)
            return clz;
        
        // 解析出class信息
        ResourcePatternResolver resourceResolver=new PathMatchingResourcePatternResolver();
        MetadataReaderFactory readerFactory = new CachingMetadataReaderFactory(resourceResolver);
        try{
            for(Resource res: ress){
                MetadataReader reader = readerFactory.getMetadataReader(res);
                String className = reader.getClassMetadata().getClassName();
                clz.add(className);
            }
        }catch(IOException ex){
            throw new RuntimeException(ex);
        }
        return clz;
    }
    
    @Override
    public List<String> searchClass(String matchPattern){
        return searchClass(matchPattern,null);
    }

}
