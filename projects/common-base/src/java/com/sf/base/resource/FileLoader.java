/*
 * $Id: FileLoader.java, 2015-2-9 下午01:07:41  Exp $
 * 
 *  
 * All rights reserved.
 * 
 * This software is copyrighted and owned by SF or the copyright holder
 * specified, unless otherwise noted, and may not be reproduced or distributed
 * in whole or in part in any form or medium without express written permission.
 */
package com.sf.base.resource;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Properties;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.util.ResourceUtils;

import com.sf.base.exception.ExceptionUtils;
import com.sf.base.util.sys.SysUtils;

/**
 * <p>
 * Title: FileLoader
 * </p>
 * <p>
 * Description: 获取文件和文件stream的工具类
 * </p>
 * 
 * @author 
 * created 2015-2-9 下午01:07:41
 * modified [who date description]
 * check [who date description]
 */
public class FileLoader {
    
    /**
     * config文件的位置
     */
    public static final String CONF_DIR = "conf/";
    
    public static final String ROOT_DIR = "modules/";

    /**
     * "images/"
     */
    public static final String ICON_DIR = "images/";
    
    
    public static final String I18N_DIR = "i18n/";
    /**
     * "sound/"
     */
    public static final String AUDIO_DIR = "sound/";

    private static Resource base;
    static {
        URL url = FileLoader.class.getResource("/");
        if (url != null) {
            base = new UrlResource(url);
        }
    }

    /**
     * 读取文件流
     * 
     * @param filePath
     * 可传3类路径：
     * 1,文件路径,d:/config/data/deviceinfo-data.xml
     * 2,URL路径,file:/d:/config/data/deviceinfo-data.xml
     * 3,classpath:作为前缀的路径,classpath:config/data/deviceinfo-data.xml
     * @return
     */
    public static InputStream getInputStream(final String filePath) {
        // 1， 从目录下面读
        InputStream is = null;
        try{
            is = getInputStreamFromFile(filePath);
        }catch(Exception ex){
            //System.err.println("getInputStreamFromFile not found:"+filePath);
        }
        if (is != null)
            return is;
        
        // 2， 从url下面读
        try{
            is = getInputStreamFromUrl(filePath);
        }catch(Exception ex){
            //System.err.println("getInputStreamFromUrl not found:"+filePath);
        }
        if (is != null) 
            return is;

        // 3，从classpath(classpath路径和jar包)中读取
        try{
            is = getInputStreamFromClasspath(filePath);
        }catch(Exception ex){
            //System.err.println("getInputStreamFromClasspath not found:"+filePath);
        }
        if (is != null) 
            return is;
        
        // 4，返回结果
        System.err.println("读资源 " + filePath + " 的文件流InputStream为null！");
        return null;
    }

    /**
     * 从jar获取inputstream
     * @param jarFilePath 比如 d:/a.jar
     * @param filePath    jar内的文件,比如 com/sf/Test.class
     * @return InputStream
     */
    public static InputStream getInputStreamFromJar(final String jarFilePath,final String filePath) throws Exception  {
        JarFile file=new JarFile(jarFilePath);
        JarEntry entry=new JarEntry(filePath);
        InputStream inputStream = file.getInputStream(entry);
        return inputStream;
    }

    /**
     * 从文件获取InputStream
     * @param filePath 比如d:/resource/config.xml
     * @return
     * @throws Exception
     */
    public static InputStream getInputStreamFromFile(String fileFullPath) throws Exception {
        URL url=getUrlFromFile(fileFullPath);
        if(url==null)
            return null;
        return getInputStreamFromUrl(url.toString());
    }
    
    /**
     * 从URL路径获取InputStream
     * 
     * @param fileUrl 文件路径的URL比如file:/D:/resource/config.xml
     * @return
     */
    public static InputStream getInputStreamFromUrl(String fileUrl) throws Exception {
        if (base != null) {
            Resource res = base.createRelative(fileUrl);
            if (res.exists()) {
                return res.getInputStream();
            }
        }
        return null;
    }

    /**
     * 从classpath(classpath的路径或者jar中都能读取到)中读取文件
     * @param filePath 以classpath开头,比如classpath:meta-inf/config/a.xml
     * @return
     */
    public static InputStream getInputStreamFromClasspath(String filePath) throws Exception {
        URL url=ResourceUtils.getURL(filePath);
        if(url!=null){
            Resource res=new UrlResource(url);
            if(res.exists())
                return res.getInputStream();
        }
        return null;
    }

    /**
     * 返回文件绝对的路径,以user.dir为起点
     * 
     * @param filePath
     *            文件相对路径，比如 modules/sf.xml
     * @return
     */
    public static String getAbsoluteFilePath(final String filePath) {
        String homeDir = SysUtils.getUserDir();
        return homeDir+File.separator+filePath;
    }

    /**
     * 得到URL
     * 
     * @param filePath
     * 可传3类路径：
     * 1,绝对文件路径,d:/config/data/deviceinfo-data.xml
     * 2,相对路径,deviceinfo-data.xml
     * 3,classpath:作为前缀的路径,classpath:config/data/deviceinfo-data.xml
     * @return
     */
    public static URL getUrl(final String filePath) {
        URL url = null;
        // 1， 从绝对目录读
        try {
            url = getUrlFromFile(filePath);
        } catch (Exception e) {
            //System.err.println("getUrlFromFile not found:"+filePath);
        }
        if (url != null) 
            return url;
        
        // 2,如果传的是相对路径，则从路径下找
        String fullFilePath = getAbsoluteFilePath(filePath);
        try {
            url = getUrlFromFile(fullFilePath);
        } catch (Exception e) {
            //System.err.println("getUrlFromFile not found:"+filePath);
        }
        if (url != null) 
            return url;

        // 3，从classpath中读取
        try{
            url = getUrlFromClasspath(filePath);
        } catch (Exception e) {
            //System.err.println("getUrlFromClasspath not found:"+filePath);
        }
        
        // 4，返回结果
        if (url == null) {
            System.out.println("资源 " + filePath + " 的URL不存在！");
            return null;
        }
        return url;
    }

    /**
     * 从file path转换为URL
     * @param fileFullPath 路径 eg:d:\resource\conf.xml
     * @return
     */
    public static URL getUrlFromFile(String fileFullPath)
            throws Exception {
        File f = new File(fileFullPath);
        if (f.exists()) {
            URL url = f.toURI().toURL();
            return url;
        }
        return null;
    }

    /**
     * 从classpath(classpath的路径或者jar中都能读取到)中得到url
     * @param filePath 比如classpath:meta-inf/config/a.xml
     * @return
     */
    public static URL getUrlFromClasspath(String filePath) throws Exception{
        URL url=ResourceUtils.getURL(filePath);
        return url;
    }
    
    /**
     * jar包中读取文件流时，filePath需要以"/"开始
     * 
     * @param filePath
     *          比如classpath:meta-inf/config/a.xml
     * @return
     */
    private static String initFilePathForJar(final String filePath) {
        if (!filePath.startsWith("/")) {
            return "/" + filePath;
        }
        return filePath;
    }
    
    /**
     * @param filePath
     *            相对METF-INF目录的路径
     * @return
     */
    public static Properties getProperties(String filePath) {
        if (filePath == null)
            throw new IllegalArgumentException();
        InputStream fis = null;
        try {
            Properties result = new Properties();
            // 从目录读取文件流
            String apexFilePath = getAbsoluteFilePath(filePath);
            if (new File(apexFilePath).exists()) {
                fis = new FileInputStream(apexFilePath);
            } else {
                // 读取失败，从jar包读取文件流
                filePath = initFilePathForJar(filePath);
                fis = getInputStream(filePath);
            }
            if (fis != null)
                result.load(fis);
            return result;
        } catch (Exception e) {
            System.err.println(ExceptionUtils.getCommonExceptionInfo(e));
        } finally {
            if (fis != null)
                try {
                    fis.close();
                } catch (IOException e) {
                    System.err.println(ExceptionUtils.getCommonExceptionInfo(e));
                }
        }
        return null;
    }

    /**
     * @param prop 属性
     * @param filePath
     *            文件存储的相对路径，指相对目录的路径
     */
    public static void storeProperties(Properties prop, String filePath) {
        if (prop == null || filePath == null)
            throw new IllegalArgumentException();
        
        FileOutputStream fos = null;
        try {
            String saveFilePath = getAbsoluteFilePath(filePath);
            File file = new File(filePath);
            if (!file.exists()) {
                File file2 = new File(saveFilePath);
                if (!file2.exists()){
                    if(!file2.createNewFile())
                        return;
                }
            }
            
            fos = new FileOutputStream(saveFilePath);
            prop.store(fos, null);
            fos.close();
        } catch (Exception e) {
            System.err.println(ExceptionUtils.getCommonExceptionInfo(e));
        } finally {
            if (fos != null)
                try {
                    fos.close();
                } catch (IOException e) {
                    System.err.println(ExceptionUtils.getCommonExceptionInfo(e));
                }
        }
    }
    
}
