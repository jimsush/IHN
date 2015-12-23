/*
 * $Id: ModuleResourceReader.java, 2015-3-23 上午10:49:43  Exp $
 * 
 *  
 * All rights reserved.
 * 
 * This software is copyrighted and owned by SF or the copyright holder
 * specified, unless otherwise noted, and may not be reproduced or distributed
 * in whole or in part in any form or medium without express written permission.
 */
package com.sf.core.resource;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.JarURLConnection;
import java.net.URISyntaxException;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;

import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.util.ResourceUtils;

import com.sf.base.exception.SfException;
import com.sf.core.bootstrap.def.ContainerConst;
import com.sf.core.bootstrap.def.ModuleContext;

/**
 * <p>
 * Title: ModuleResourceReader
 * </p>
 * <p>
 * Description:
 * 平台资源文件读取,用于读取模块配置文件
 * </p>
 * 
 * @author 
 * created 2015-3-23 上午10:49:43
 * modified [who date description]
 * check [who date description]
 */
public class ModuleResourceReader {
    
    
    public ModuleResourceReader(){
        
    }
    
    /**
     * 获取模块下指定的文件资源
     * @param moduleName
     * @param configFileName
     * @return
     */
    public InputStream getConfigFile(String moduleName,String configFileName){
        String homeDir=ModuleContext.getInstance().getSystemParam(ContainerConst.KEY_HOME_DIR);
        String configFileFullName=homeDir+File.separator+"modules"+File.separator+moduleName+File.separator+configFileName;
        try {
            return new FileInputStream(configFileFullName);
        } catch (FileNotFoundException e) {
           throw new RuntimeException(e);
        }
    }
    /**
     * 读取模块配置文件jar包中的指定文件
     * @param configFileName
     * @return
     */
    public InputStream getConfigFile(String moduleName,String cfgjar,String configFileName){
        String homeDir=ModuleContext.getInstance().getSystemParam(ContainerConst.KEY_HOME_DIR);
        String jarPath=homeDir+File.separator+"modules"+File.separator+moduleName+File.separator+"lib"+File.separator+cfgjar+File.separator;
        Resource rootDirResource=new FileSystemResource(jarPath);
        JarFile jarFile;
        String jarFileUrl;
        //String rootEntryPath;
        boolean newJarFile;
     
        jarFile = null;
        jarFileUrl = null;
        //rootEntryPath = null;
        newJarFile = false;
        try{
            java.net.URLConnection con = rootDirResource.getURL().openConnection();
        if(con instanceof JarURLConnection)
        {
            JarURLConnection jarCon = (JarURLConnection)con;
            jarCon.setUseCaches(false);
            jarFile = jarCon.getJarFile();
            jarFileUrl = jarCon.getJarFileURL().toExternalForm();
            //JarEntry jarEntry = jarCon.getJarEntry();
        } else
        {
            String urlFile = rootDirResource.getURL().getFile();
            int separatorIndex = urlFile.indexOf("!/");
            if(separatorIndex != -1)
            {
                jarFileUrl = urlFile.substring(0, separatorIndex);
                jarFile = getJarFile(jarFileUrl);

            } else
            {
                jarFile = new JarFile(jarPath);
                jarFileUrl = urlFile;
            }
        }
        
        ZipEntry entry=jarFile.getEntry(configFileName);
        return jarFile.getInputStream(entry);

        }catch(Exception ex){
            throw new SfException(ex);
        }finally{
            try{
            if(newJarFile)
                jarFile.close();
            }catch(Exception ex){
                throw new SfException(ex);
            }
        }
        
    }
    
    /**
     * file url变为JarFile
     * @param jarFileUrl
     * @return
     * @throws IOException
     */
    private JarFile getJarFile(String jarFileUrl)
    throws IOException
    {
    if(jarFileUrl.startsWith("file:"))
        try
        {
            return new JarFile(ResourceUtils.toURI(jarFileUrl).getSchemeSpecificPart());
        }
        catch(URISyntaxException ex)
        {
            return new JarFile(jarFileUrl.substring("file:".length()));
        }
    else
        return new JarFile(jarFileUrl);
    }
}
